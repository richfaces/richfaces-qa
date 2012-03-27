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
package org.richfaces.tests.showcase.jquery;

import static org.jboss.arquillian.ajocado.Graphene.styleEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestJquery extends AbstractAjocadoTest {

    /* *************************************************************************
     * Constants************************************************************************
     */

    private final int NUMBER_OF_IMGS = 9;
    private final String WIDTH_OF_IMG_WITHOUT_BORDERS = "180px";
    private final int WIDTH_OF_IMG_BEFORE_HOVER = 124; // these widths includes also borders
    private final int WIDTH_OF_IMG_AFTER_HOVER = 184;

    /* *************************************************************************
     * Locators*************************************************************************
     */

    protected JQueryLocator images = jq("#gallery img:eq({0})");

    /* ***************************************************************************
     * Tests***************************************************************************
     */

    @Test
    public void testImagesAreBecomingBiggerAfterHover() {

        for (int i = 0; i < NUMBER_OF_IMGS; i++) {

            JQueryLocator img = images.format(i);

            int widthBeforeHover = getWidthOfImage(img);

            selenium.mouseOver(img);

            waitModel.until(styleEquals.locator(img).property(CssProperty.WIDTH).value(WIDTH_OF_IMG_WITHOUT_BORDERS));

            int widthAfterHover = getWidthOfImage(img);

            assertEquals(widthBeforeHover, WIDTH_OF_IMG_BEFORE_HOVER, "The initial width is wrong");
            assertEquals(widthAfterHover, WIDTH_OF_IMG_AFTER_HOVER, "The width of image after hovering is wrong!");

            selenium.mouseOut(img);
        }
    }

    /* ****************************************************************************
     * Help methods****************************************************************************
     */

    /**
     * Gets the width of image element, but it also includes width of borders of image
     *
     * @param img
     *            the particular image which width will be returned
     * @return the width of image with width of image's borders
     */
    private int getWidthOfImage(JQueryLocator img) {

        return selenium.getElementWidth(img);
    }

}
