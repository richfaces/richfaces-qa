/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.richfaces.fragment.common.Icon;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

import com.google.common.collect.Lists;

/**
 * Checker for icon attributes (e.g. accordion, panelMenu)
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class IconsChecker<A extends AttributeEnum> {

    private static final Map<String, String> cssImageIcons = new HashMap<String, String>();
    private static final List<String> cssNoImageIcons = Lists.newArrayList("transparent");
    private static final Map<String, String> imageIcons = new HashMap<String, String>();

    static {
        cssImageIcons.put("chevron", "chevron");
        cssImageIcons.put("chevronDown", "chevron-down");
        cssImageIcons.put("chevronUp", "chevron-up");
        cssImageIcons.put("disc", "disc");
        cssImageIcons.put("grid", "grid");
        cssImageIcons.put("triangle", "triangle");
        cssImageIcons.put("triangleDown", "triangle-down");
        cssImageIcons.put("triangleUp", "triangle-up");

        imageIcons.put("nonexisting", "nonexisting");
        imageIcons.put("star", "star.png");
    }

    private final Attributes<A> attributes;
    private final String iconPrefix;
    private final String iconSuffix;

    /**
     * Create a new instance of icons checker
     *
     * @param attributes Attributes instance used to set component attributes
     * @param iconPrefix prefix used for icon elements
     * @param iconSuffix suffix used for icon elements
     */
    public IconsChecker(Attributes<A> attributes, String iconPrefix, String iconSuffix) {
        Validate.notNull(attributes);
        Validate.notNull(iconPrefix);
        Validate.notNull(iconSuffix);
        this.attributes = attributes;
        this.iconPrefix = iconPrefix;
        this.iconSuffix = iconSuffix;
    }

    public IconsChecker(Attributes<A> attributes) {
        this(attributes, "", "");
    }

    /**
     * checkCssImageIcons, checkCssNoImageIcons, checkImageIcons, checkNone
     *
     * @param attribute
     * @param icon
     * @param classSuffix
     */
    public void checkAll(A attribute, Icon icon, String classSuffix) {
        checkCssImageIcons(attribute, icon, classSuffix);
        checkCssNoImageIcons(attribute, icon, classSuffix);
        checkImageIcons(attribute, icon, classSuffix);
        checkNone(attribute, icon, classSuffix);
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which
     * produce an image)
     *
     * @param attribute icon attribute
     * @param icon
     * @param classSuffix
     */
    public void checkCssImageIcons(A attribute, Icon icon, String classSuffix) {
        String imageNameSuffix = "";
        if (classSuffix.contains("dis")) {
            imageNameSuffix = "Disabled";
        }
        // option -> css class
        for (String cssIcon : cssImageIcons.keySet()) {
            if (!setAttributeSilently(attribute, cssIcon)) {
                continue;
            }
            assertTrue(
                icon.getIconElement().getAttribute("class").contains(iconPrefix + cssImageIcons.get(cssIcon) + iconSuffix + classSuffix),
                "Div should have set class " + iconPrefix + cssImageIcons.get(cssIcon) + iconSuffix + classSuffix + ".");
            assertTrue(icon.getIconElement().getCssValue("background-image").contains(cssIcon + imageNameSuffix),
                "Icon should contain a " + cssIcon + ".");
        }
    }

    /**
     * Checks whether icons controlled by CSS work properly (only icons which
     * don't produce any image)
     *
     * @param attribute icon attribute
     * @param icon
     * @param classSuffix
     */
    public void checkCssNoImageIcons(A attribute, Icon icon, String classSuffix) {
        for (String cssIcon : cssNoImageIcons) {
            if (!setAttributeSilently(attribute, cssIcon)) {
                continue;
            }
            assertTrue(
                icon.getIconDivElement().getAttribute("class").contains(iconPrefix + cssIcon + iconSuffix + classSuffix),
                "Div should have set class " + iconPrefix + cssIcon + iconSuffix + classSuffix + ".");
            assertTrue(icon.getIconElement().getCssValue("background-image").equals("none"),
                "Icon should not contain any image.");
        }
    }

    /**
     * Checks whether icon with custom URL works properly
     *
     * @param attribute icon attribute
     * @param icon
     * @param classSuffix
     */
    public void checkImageIcons(A attribute, Icon icon, String classSuffix) {
        // option -> image
        for (String imageIcon : imageIcons.keySet()) {
            if (!setAttributeSilently(attribute, imageIcon)) {
                continue;
            }
            assertFalse(icon.getIconDivElement().isPresent(), "Icon's div should not be present when icon=" + imageIcon + ".");
            assertTrue(icon.getIconImageElement().isPresent(), "Icon's image should be rendered when icon=" + imageIcon + ".");
            assertTrue(icon.getIconElement().getAttribute("src").contains(imageIcons.get(imageIcon)),
                "Icon's src attribute should contain " + imageIcons.get(imageIcon) + " when icon=" + imageIcon + ".");
        }
    }

    public void checkNone(A attribute, Icon icon, String classSuffix) {
        if (!setAttributeSilently(attribute, "none")) {
            return;
        }
        assertFalse(icon.getIconElement().isPresent(), "Icon should not be present when icon=none.");
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
