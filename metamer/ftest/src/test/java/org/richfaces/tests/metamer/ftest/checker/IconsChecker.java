/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionLabel;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionLabelLocator;

/**
 * Checker for icon attributes (e.g. rich:accordion, rich:panelMenu)
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class IconsChecker {

    private String iconPrefix;
    private String iconSuffix;
    private AjaxSelenium selenium;

    /**
     * Create a new instance of icons checker
     *
     * @param selenium instance of ajax selenium
     * @param iconPrefix prefix used for icon elements
     * @param iconSuffix suffix used for icon elements
     */
    public IconsChecker(AjaxSelenium selenium, String iconPrefix, String iconSuffix) {
        Validate.notNull(iconPrefix);
        Validate.notNull(iconSuffix);
        Validate.notNull(selenium);
        this.iconPrefix = iconPrefix;
        this.iconSuffix = iconSuffix;
        this.selenium = selenium;
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which produce an image)
     *
     * @param attributeInput
     * @param icon
     * @param classSuffix
     */
    public void checkCssImageIcons(ElementLocator<JQueryLocator> attributeInput, ElementLocator<JQueryLocator> icon, String classSuffix) {
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
        for(String cssIcon : cssImageIcons.keySet()) {
            if (!selectOptionSilently(attributeInput, cssIcon)) {
                continue;
            }
            assertTrue(selenium.belongsClass(icon, iconPrefix + cssImageIcons.get(cssIcon) + iconSuffix + classSuffix),
                "Div should have set class " + iconPrefix + cssImageIcons.get(cssIcon) + iconSuffix + classSuffix + ".");
            assertTrue(
                selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains(cssIcon + imageNameSuffix),
                "Icon should contain a " + cssIcon + ".");
        }
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which don't produce any image)
     *
     * @param attributeInput
     * @param icon
     * @param classSuffix
     */
    public void checkCssNoImageIcons(ElementLocator<JQueryLocator> attributeInput, ElementLocator<JQueryLocator> icon, String classSuffix) {
        String[] cssNoImageIcons = new String[] { "transparent" };
        for(String cssIcon : cssNoImageIcons) {
            if (!selectOptionSilently(attributeInput, cssIcon)) {
                continue;
            }
            assertTrue(selenium.belongsClass(icon, iconPrefix + cssIcon + iconSuffix + classSuffix),
                "Div should have set class " + iconPrefix + cssIcon + iconSuffix + classSuffix +".");
            assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).equals("none"),
                "Icon should not contain any image.");
        }
    }

    /**
     * Checks whether icon with custom URL works properly
     *
     * @param attributeInput
     * @param icon
     * @param image
     * @param classSuffix
     */
    public void checkImageIcons(ElementLocator<JQueryLocator> attributeInput, ElementLocator<JQueryLocator> icon, ElementLocator<JQueryLocator> image, String classSuffix) {
        checkImageIcons(attributeInput, icon, image, classSuffix, true);
    }

    /**
     * Checks whether icon with custom URL works properly
     *
     * @param attributeInput
     * @param icon
     * @param image
     * @param classSuffix
     * @param disableIcon set to TRUE if the presence of image element causes that icon element shouldn't be present
     */
    public void checkImageIcons(ElementLocator<JQueryLocator> attributeInput, ElementLocator<JQueryLocator> icon, ElementLocator<JQueryLocator> image, String classSuffix, boolean disableIcon) {
        // option -> image
        Map<String, String> imageIcons = new HashMap<String, String>();
        imageIcons.put("nonexisting", "nonexisting");
        imageIcons.put("star", "star.png");
        for(String imageIcon : imageIcons.keySet()) {
            if (!selectOptionSilently(attributeInput, imageIcon)) {
                continue;
            }
            if (disableIcon) {
                assertFalse(selenium.isElementPresent(icon), "Icon's div (" + icon.getRawLocator() + ") should not be present when icon=" + imageIcon + ".");
            }
            assertTrue(selenium.isElementPresent(image), "Icon's image should be rendered (" + image.getRawLocator() + ") when icon=" + imageIcon + ".");
            assertTrue(selenium.getAttribute(image.getAttribute(Attribute.SRC)).contains(imageIcons.get(imageIcon)),
                "Icon's src attribute (" + image.getRawLocator() + ") should contain " + imageIcons.get(imageIcon) + " when icon=" + imageIcon + ".");
        }
    }

    public void checkNone(ElementLocator<JQueryLocator> attributeInput, ElementLocator<JQueryLocator> icon, String classSuffix) {
        if (!selectOptionSilently(attributeInput, "none")) {
            return;
        }
        assertFalse(selenium.isElementPresent(icon), "Icon should not be present when icon=none.");
    }

    public String getIconPrefix() {
        return iconPrefix;
    }

    public String getIconSuffix() {
        return iconSuffix;
    }

    private boolean selectOptionSilently(ElementLocator<?> selectLocator, String label) {
        OptionLabelLocator optionLocator = optionLabel(label);
        try {
            selenium.select(selectLocator, optionLocator);
            selenium.waitForPageToLoad();
            return true;
        } catch(RuntimeException e) {
            // silent
            return false;
        }
    }
}
