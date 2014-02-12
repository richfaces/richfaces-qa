/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.checker;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * Checker for icon attributes (e.g. rich:accordion, rich:panelMenu)
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class IconsCheckerWebdriver<A extends AttributeEnum> {

    private final Attributes<A> attributes;
    private final WebDriver driver;
    private final String iconPrefix;
    private final String iconSuffix;

    /**
     * Create a new instance of icons checker
     *
     * @param driver
     *            instance of WebDriver
     * @param attributes
     *            Attributes instance used to set component attributes
     * @param iconPrefix
     *            prefix used for icon elements
     * @param iconSuffix
     *            suffix used for icon elements
     */
    public IconsCheckerWebdriver(WebDriver driver, Attributes<A> attributes, String iconPrefix, String iconSuffix) {
        Validate.notNull(driver);
        Validate.notNull(attributes);
        Validate.notNull(iconPrefix);
        Validate.notNull(iconSuffix);
        this.driver = driver;
        this.attributes = attributes;
        this.iconPrefix = iconPrefix;
        this.iconSuffix = iconSuffix;
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which produce an image)
     *
     * @param attribute
     *            icon attribute
     * @param icon
     * @param classSuffix
     */
    public void checkCssImageIcons(A attribute, ElementLocator icon, String classSuffix) {
        String imageNameSuffix = "";
        if (classSuffix.contains("dis")) {
            imageNameSuffix = "Disabled";
        }
        // option -> css class
        Map<String, String> cssImageIcons = new HashMap<String, String>();
        cssImageIcons.put("chevron", "chevron");
        cssImageIcons.put("chevronDown", "chevron-down");
        cssImageIcons.put("chevronUp", "chevron-up");
        cssImageIcons.put("disc", "disc");
        cssImageIcons.put("grid", "grid");
        cssImageIcons.put("triangle", "triangle");
        cssImageIcons.put("triangleDown", "triangle-down");
        cssImageIcons.put("triangleUp", "triangle-up");
        for (String cssIcon : cssImageIcons.keySet()) {
            if (!setAttributeSilently(attribute, cssIcon)) {
                continue;
            }
            assertTrue(
                icon.findElement().getAttribute("class")
                    .contains(iconPrefix + cssImageIcons.get(cssIcon) + iconSuffix + classSuffix),
                "Div should have set class " + iconPrefix + cssImageIcons.get(cssIcon) + iconSuffix + classSuffix + ".");
            assertTrue(icon.findElement().getCssValue("background-image").contains(cssIcon + imageNameSuffix),
                "Icon should contain a " + cssIcon + ".");
        }
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which don't produce any image)
     *
     * @param attribute
     *            icon attribute
     * @param icon
     * @param classSuffix
     */
    public void checkCssNoImageIcons(A attribute, ElementLocator icon, String classSuffix) {
        String[] cssNoImageIcons = new String[] { "transparent" };
        for (String cssIcon : cssNoImageIcons) {
            if (!setAttributeSilently(attribute, cssIcon)) {
                continue;
            }
            assertTrue(
                icon.findElement().getAttribute("class").contains(iconPrefix + cssIcon + iconSuffix + classSuffix),
                "Div should have set class " + iconPrefix + cssIcon + iconSuffix + classSuffix + ".");
            assertTrue(icon.findElement().getCssValue("background-image").equals("none"),
                "Icon should not contain any image.");
        }
    }

    /**
     * Checks whether icon with custom URL works properly
     *
     * @param attribute
     *            icon attribute
     * @param icon
     * @param image
     * @param classSuffix
     */
    public void checkImageIcons(A attribute, ElementLocator icon, By image, String classSuffix) {
        checkImageIcons(attribute, icon, image, classSuffix, true);
    }

    /**
     * Checks whether icon with custom URL works properly
     *
     * @param attribute
     *            icon attribute
     * @param icon
     * @param image
     * @param classSuffix
     * @param disableIcon
     *            set to TRUE if the presence of image element causes that icon element shouldn't be present
     */
    public void checkImageIcons(A attribute, ElementLocator icon, By image, String classSuffix, boolean disableIcon) {
        // option -> image
        Map<String, String> imageIcons = new HashMap<String, String>();
        imageIcons.put("nonexisting", "nonexisting");
        imageIcons.put("star", "star.png");
        for (String imageIcon : imageIcons.keySet()) {
            if (!setAttributeSilently(attribute, imageIcon)) {
                continue;
            }
            if (disableIcon) {
                assertFalse(new WebElementConditionFactory(icon.findElement()).isPresent().apply(driver),
                    "Icon's div (" + icon + ") should not be present when icon=" + imageIcon + ".");
            }
            assertTrue(new WebElementConditionFactory(driver.findElement(image)).isPresent().apply(driver),
                "Icon's image should be rendered (" + image + ") when icon=" + imageIcon + ".");
            assertTrue(driver.findElement(image).getAttribute("src").contains(imageIcons.get(imageIcon)),
                "Icon's src attribute (" + image + ") should contain " + imageIcons.get(imageIcon) + " when icon="
                    + imageIcon + ".");
        }
    }

    /**
     * Verify img icon at location given as icon param
     *
     * It is useful when using page fragments where icon element is reliable and unique localized, but img child element
     * doesn't contains any additional attributes for unique identification. Which is no longer needed since parent
     * element is reliable identified.
     *
     * Used e.g. for PanelMenuGroup icons, where more groups present within menu, but icon images are difficult to
     * localize unique over whole page.
     *
     * @param attribute
     *            icon attribute
     * @param icon
     *            - root of icon "element" - it is div for CSS icons, and img for image icons (2nd make sense in this
     *            test)
     * @param classSuffix
     * @param disableIcon
     *            set to TRUE if the presence of image element causes that icon element shouldn't be present
     */
    public void checkImageIcons(A attribute, ElementLocator icon, WebElement imgIconElem, String classSuffix,
        boolean disableIcon) {
        // option -> image
        Map<String, String> imageIcons = new HashMap<String, String>();
        imageIcons.put("nonexisting", "nonexisting");
        imageIcons.put("star", "star.png");
        for (String imageIcon : imageIcons.keySet()) {
            if (!setAttributeSilently(attribute, imageIcon)) {
                continue;
            }
            if (disableIcon) {
                assertFalse(new WebElementConditionFactory(icon.findElement()).isPresent().apply(driver),
                    "Icon's div (" + icon + ") should not be present when icon=" + imageIcon + ".");
            }
            // iconElem as var to easier debug/verify what element was reached
            assertTrue(new WebElementConditionFactory(imgIconElem).isPresent().apply(driver),
                "Icon's image should be rendered (" + icon + ") when icon=" + imageIcon + ".");
            assertTrue(imgIconElem.getAttribute("src").contains(imageIcons.get(imageIcon)), "Icon's src attribute ("
                + icon + ") should contain " + imageIcons.get(imageIcon) + " when icon=" + imageIcon + ".");
        }
    }

    public void checkNone(A attribute, ElementLocator icon, String classSuffix) {
        if (!setAttributeSilently(attribute, "none")) {
            return;
        }
        assertFalse(new WebElementConditionFactory(icon.findElement()).isPresent().apply(driver),
            "Icon should not be present when icon=none.");
    }

    private boolean setAttributeSilently(A attribute, String value) {

        try {
            attributes.set(attribute, value);
            return true;
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    public static class ByElementLocator implements ElementLocator {
        private final WebDriver driver;
        private final By by;

        public ByElementLocator(WebDriver driver, By by) {
            this.driver = driver;
            this.by = by;
        }

        @Override
        public WebElement findElement() {
            return driver.findElement(by);
        }

        @Override
        public List<WebElement> findElements() {
            return driver.findElements(by);
        }

    }

    public static class WebElementLocator implements ElementLocator {
        private final WebElement element;

        public WebElementLocator(WebElement element) {
            this.element = element;
        }

        @Override
        public WebElement findElement() {
            return element;
        }

        @Override
        public List<WebElement> findElements() {
            return Arrays.asList(element);
        }

    }

}
