/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.checker;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.enricher.WebElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * Checker for icon attributes (e.g. rich:accordion, rich:panelMenu)
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @param <A>
 */
public class IconsChecker<A extends AttributeEnum> {

    private static final String BG_IMAGE = "background-image";
    private static final String CLASS_STRING = "class";
    private static final String DISABLED = "disabled";
    private static final String DISABLED_CLASS = "rf-ico-t-dis";
    private static final String DISABLED_ICON_POSITION = "0px -32px";
    private static final String HEADER_ICON_CLASS = "rf-ico-t-hdr";
    private static final String HEADER_ICON_POSITION = "0px -16px";
    private static final String NONE = "none";
    private static final String STANDARD_ICON_POSITION = "0px 0px";
    private static final String TRANSPARENT = "transparent";
    private static final Map<String, String> cssImageIcons = new HashMap<String, String>();
    private static final Map<String, String> imageIcons = new HashMap<String, String>();

    private final Attributes<A> attributes;
    private final WebDriver driver;

    static {
        cssImageIcons.put("chevron", "rf-ico-chevron");
        cssImageIcons.put("chevronDown", "rf-ico-chevron-down");
        cssImageIcons.put("chevronLeft", "rf-ico-chevron-left");
        cssImageIcons.put("chevronUp", "rf-ico-chevron-up");
        cssImageIcons.put("disc", "rf-ico-disc");
        cssImageIcons.put("grid", "rf-ico-grid");
        cssImageIcons.put("triangle", "rf-ico-triangle");
        cssImageIcons.put("triangleDown", "rf-ico-triangle-down");
        cssImageIcons.put("triangleLeft", "rf-ico-triangle-left");
        cssImageIcons.put("triangleUp", "rf-ico-triangle-up");
        cssImageIcons.put(TRANSPARENT, "rf-ico-transparent");

        imageIcons.put("nonexisting", "nonexisting");
        imageIcons.put("star", "star.png");
    }

    /**
     * Create a new instance of icons checker
     *
     * @param driver instance of WebDriver
     * @param attributes Attributes instance used to set component attributes
     */
    public IconsChecker(WebDriver driver, Attributes<A> attributes) {
        Validate.notNull(driver);
        Validate.notNull(attributes);
        this.driver = driver;
        this.attributes = attributes;
    }

    /**
     * Performs checkCssImageIcons, checkImageIcons and checkNone.
     *
     * @param attribute icon attribute
     * @param icon icon element
     * @param image By for finding image element on page
     * @param isIconDivReplacedByImg set to TRUE if the presence of image element causes that icon element shouldn't be present
     * @param isHeaderIcon
     */
    public void checkAll(A attribute, WebElement icon, By image, boolean isIconDivReplacedByImg, boolean isHeaderIcon) {
        checkAll(attribute, icon, WebElementUtils.findElementLazily(image, driver), isIconDivReplacedByImg, isHeaderIcon);
    }

    /**
     * Performs checkCssImageIcons, checkImageIcons and checkNone.
     *
     * @param attribute icon attribute
     * @param icon icon element
     * @param image image element
     * @param isIconDivReplacedByImg set to TRUE if the presence of image element causes that icon element shouldn't be present
     * @param isHeaderIcon
     */
    public void checkAll(A attribute, WebElement icon, WebElement image, boolean isIconDivReplacedByImg, boolean isHeaderIcon) {
        checkCssImageIcons(attribute, icon, isHeaderIcon);
        checkImageIcons(attribute, icon, image, isIconDivReplacedByImg);
        checkNone(attribute, icon);
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which produce an image)
     *
     * @param attribute icon attribute
     * @param icon icon element
     * @param isHeaderIcon
     */
    public void checkCssImageIcons(A attribute, WebElement icon, boolean isHeaderIcon) {
        boolean isDisabled = attribute.toString().toLowerCase().contains(DISABLED);
        // option -> css class
        for (String cssIcon : cssImageIcons.keySet()) {
            if (!setAttributeSilently(attribute, cssIcon)) {
                continue;
            }
            String classAtt = icon.getAttribute(CLASS_STRING);
            assertTrue(classAtt.contains(cssImageIcons.get(cssIcon)));
            if (cssIcon.equals(TRANSPARENT)) {
                assertTrue(icon.getCssValue(BG_IMAGE).equals(NONE));
            } else {
                assertTrue(icon.getCssValue(BG_IMAGE).contains(cssIcon));
            }
            boolean containsHeaderStyleClass = classAtt.contains(HEADER_ICON_CLASS);
            assertTrue(isHeaderIcon ? containsHeaderStyleClass : !containsHeaderStyleClass,
                "Icon should contain class 'rf-ico-t-hdr' when it is placed in header.");
            boolean containsDisabledClass = classAtt.contains(DISABLED_CLASS);
            assertTrue(isDisabled ? containsDisabledClass : !containsDisabledClass,
                "Icon should contain class 'rf-ico-t-dis' when it is disabled.");
            // https://issues.jboss.org/browse/RFPL-3718:
            // checks https://issues.jboss.org/browse/RF-10817 after https://issues.jboss.org/browse/RF-14039
            String bgPosition = icon.getCssValue("background-position");
            assertTrue(isDisabled
                ? bgPosition.contains(DISABLED_ICON_POSITION)// disabled image position
                : isHeaderIcon
                    ? bgPosition.contains(HEADER_ICON_POSITION)
                    : bgPosition.contains(STANDARD_ICON_POSITION),
                "The icon color/position does not match its state."
            );
        }
    }

    /**
     * Checks whether icon with custom URL works properly
     *
     * @param attribute icon attribute
     * @param icon icon element
     * @param image By for finding image element on page
     * @param isIconDivReplacedByImg set to TRUE if the presence of image element causes that icon element shouldn't be present
     */
    public void checkImageIcons(A attribute, WebElement icon, By image, boolean isIconDivReplacedByImg) {
        checkImageIcons(attribute, icon, WebElementUtils.findElementLazily(image, driver), isIconDivReplacedByImg);
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
     * @param attribute icon attribute
     * @param icon root of icon "element" - it is div for CSS icons, and img for image icons (2nd make sense in this test)
     * @param image image element
     * @param isIconDivReplacedByImg set to TRUE if the presence of image element causes that icon element shouldn't be present
     */
    public void checkImageIcons(A attribute, WebElement icon, WebElement image, boolean isIconDivReplacedByImg) {
        // option -> image
        for (String imageIcon : imageIcons.keySet()) {
            if (!setAttributeSilently(attribute, imageIcon)) {
                continue;
            }
            if (isIconDivReplacedByImg) {
                assertFalse(new WebElementConditionFactory(icon).isPresent().apply(driver),
                    "Icon's div should not be present when icon=" + imageIcon + ".");
            }
            // iconElem as var to easier debug/verify what element was reached
            assertTrue(new WebElementConditionFactory(image).isPresent().apply(driver),
                "Icon's image should be rendered when icon=" + imageIcon + ".");
            assertTrue(image.getAttribute("src").contains(imageIcons.get(imageIcon)),
                "Icon's src attribute should contain " + imageIcons.get(imageIcon) + " when icon=" + imageIcon + ".");
        }
    }

    public void checkNone(A attribute, WebElement icon) {
        if (!setAttributeSilently(attribute, NONE)) {
            return;
        }
        assertFalse(new WebElementConditionFactory(icon).isPresent().apply(driver),
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
}
