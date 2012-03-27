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
package org.richfaces.tests.showcase.hashParam;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.Graphene.elementPresent;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.richfaces.tests.showcase.JQueryScriptWindowObject;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestHashOptionsPopup extends AbstractGrapheneTest {

    /* **************************************************************************
     * Constants ***************************************************************** *********
     */

    private final String WIDTH = "500px";
    private final String HEIGHT = "300px";

    /* ***************************************************************************
     * Locators ****************************************************************** *********
     */

    protected JQueryLocator callThePoppup = jq("input[type=submit]");
    protected JQueryLocator shade = jq("div.rf-pp-shade:visible");
    protected JQueryLocator poppup = jq("div.rf-pp-cntr:visible");

    /* ****************************************************************************
     * Tests ********************************************************************* *******
     */

    @Test
    public void testWhetherThePopupIsCentered() {

        selenium.click(callThePoppup);

        waitModel.until(elementPresent.locator(poppup));

        StyleAttributesOfPoppup styleAttributesOfPoppup = getStyleAttributesOfPoppup();

        assertEquals(styleAttributesOfPoppup.getHeight(), HEIGHT, "The hash param did not bring the height value");
        assertEquals(styleAttributesOfPoppup.getWidth(), WIDTH, "The hash param did not bring the width value");

        JQueryScriptWindowObject heightJQ = new JQueryScriptWindowObject("height()");
        JQueryScriptWindowObject widthJQ = new JQueryScriptWindowObject("width()");

        String screenHeight = selenium.getEval(heightJQ);
        String screenWidth = selenium.getEval(widthJQ);

        String expectedTop = String.valueOf((Integer.valueOf(screenHeight) / 2) - 150).concat("px");
        String expectedLeft = String.valueOf((Integer.valueOf(screenWidth) / 2) - 250).concat("px");

        assertEquals(styleAttributesOfPoppup.getTop(), expectedTop,
            "The hash param did not bring the top value correctly");
        assertEquals(styleAttributesOfPoppup.getLeft(), expectedLeft,
            "The hash param did not bring the left value correctly");

    }

    /* ******************************************************************************
     * Help methods ************************************************************** ****************
     */

    /**
     * Gets the width, height, top and left style attributes
     *
     * @return the width, height, top and left style attributes
     */
    private StyleAttributesOfPoppup getStyleAttributesOfPoppup() {

        String width = selenium.getStyle(poppup, CssProperty.WIDTH);
        String height = selenium.getStyle(poppup, CssProperty.HEIGHT);

        String style = selenium.getAttribute(poppup.getAttribute(Attribute.STYLE));

        int indexOfTop = style.indexOf("top");

        String topAndRest = style.substring(indexOfTop);
        String top = topAndRest.substring(5, topAndRest.indexOf(";"));

        int indexOfLeft = style.indexOf("left");

        String leftAndRest = style.substring(indexOfLeft);
        String left = leftAndRest.substring(6, leftAndRest.indexOf(";"));

        StyleAttributesOfPoppup attOfStyleOfPoppup = new StyleAttributesOfPoppup(width, height, left, top);

        return attOfStyleOfPoppup;

    }

    /* *******************************************************************************
     * Nested class ************************************************************** *****************
     */

    private class StyleAttributesOfPoppup {

        private String width;
        private String height;
        private String left;
        private String top;

        public StyleAttributesOfPoppup(String width, String height, String left, String top) {
            this.width = width;
            this.height = height;
            this.left = left;
            this.top = top;
        }

        public String getWidth() {
            return width;
        }

        public String getHeight() {
            return height;
        }

        public String getLeft() {
            return left;
        }

        public String getTop() {
            return top;
        }

    }
}
