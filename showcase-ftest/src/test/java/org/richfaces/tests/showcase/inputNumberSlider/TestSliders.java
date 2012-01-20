/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.inputNumberSlider;

import static org.testng.Assert.assertEquals;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSliders extends AbstractAjocadoTest {

    /* *****************************************************************************************************
     * Constants*****************************************************************************************************
     */

    protected final int PADDING_WHEN_CLICK_ON_200 = 100;
    protected final int PADDING_WHEN_CLICK_ON_30 = 14;

    protected final int PADDING_WHEN_CLICK_ON_30_ANOTHER = 15;

    protected final int DEFAULT_PADDING_OF_DISABLED_SLIDER = 50;

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator defaultNumberSliderHandle = jq("span.rf-insl-hnd-cntr:eq(0)");
    protected JQueryLocator defalutInputOfNumberSlider = jq("input[type=text]:eq(0)");
    protected JQueryLocator defaultBodyNumberSlider = jq("span.rf-insl-trc:eq(0)");

    protected JQueryLocator minimalisticNumberSliderHandle = jq("span.rf-insl-hnd-cntr:eq(1)");
    protected JQueryLocator minimalisticBodyNumberSlider = jq("span.rf-insl-trc:eq(1)");

    protected JQueryLocator anotherNumberSliderHandle = jq("span.rf-insl-hnd-cntr:eq(2)");
    protected JQueryLocator anotherInputOfNumberSlider = jq("input[type=text]:eq(2)");
    protected JQueryLocator anotherBodyNumberSlider = jq("span.rf-insl-trc:eq(2)");

    protected JQueryLocator disabledNumberSliderHandle = jq("span.rf-insl-hnd-cntr:eq(3)");
    protected JQueryLocator disabledInputOfNumberSlider = jq("input[type=text]:eq(3)");
    protected JQueryLocator disabledBodyNumberSlider = jq("span.rf-insl-trc:eq(3)");

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testDefaultInputNumberSliderClickOnBody() {

        int padding = clickOnSliderAndCheckThePadding(defaultBodyNumberSlider, defaultNumberSliderHandle,
            PADDING_WHEN_CLICK_ON_200, 200);

        checkTheInputAccordingToPositionOfHandler(defalutInputOfNumberSlider, padding);

        padding = clickOnSliderAndCheckThePadding(defaultBodyNumberSlider, defaultNumberSliderHandle,
            PADDING_WHEN_CLICK_ON_30, 30);

        checkTheInputAccordingToPositionOfHandler(defalutInputOfNumberSlider, padding);
    }

    @Test
    public void testMinimalisticInputNumberSliderClickOnBody() {

        clickOnSliderAndCheckThePadding(minimalisticBodyNumberSlider, minimalisticNumberSliderHandle,
            PADDING_WHEN_CLICK_ON_200, 200);

        clickOnSliderAndCheckThePadding(minimalisticBodyNumberSlider, minimalisticNumberSliderHandle,
            PADDING_WHEN_CLICK_ON_30, 30);

    }

    @Test
    public void testAnotherInputNumberSliderClickOnTheBody() {

        int padding = clickOnSliderAndCheckThePadding(anotherBodyNumberSlider, anotherNumberSliderHandle,
            PADDING_WHEN_CLICK_ON_200, 200);

        checkTheInputAccordingToPositionOfHandler(anotherInputOfNumberSlider, padding * 10);

        padding = clickOnSliderAndCheckThePadding(anotherBodyNumberSlider, anotherNumberSliderHandle,
            PADDING_WHEN_CLICK_ON_30_ANOTHER, 30);

        checkTheInputAccordingToPositionOfHandler(anotherInputOfNumberSlider, padding * 10);
    }

    @Test
    public void testDisabledControlInputNumberSlider() {

        int padding = clickOnSliderAndCheckThePadding(disabledBodyNumberSlider, disabledNumberSliderHandle,
            DEFAULT_PADDING_OF_DISABLED_SLIDER, 200);

        checkTheInputAccordingToPositionOfHandler(disabledInputOfNumberSlider, padding * 10);
    }

    /* *********************************************************************************************************
     * Help methods **************************************************************
     * *******************************************
     */

    /**
     * Clicks on the particular place on the slider and check the padding left style property
     *
     * @param sliderBody
     *            the body of The slider where selenium will click
     * @param sliderHandler
     *            the handler which padding left will be checked
     * @param expectedPadding
     *            the expected padding after click
     * @param whereToClick
     *            the pixel where to click on the body, it is on the X axis
     */
    private int clickOnSliderAndCheckThePadding(JQueryLocator sliderBody, JQueryLocator sliderHandler,
        int expectedPadding, int whereToClick) {

        selenium.mouseDownAt(sliderBody, new Point(whereToClick, 0));

        int paddingAfterClick = getPaddingLeftOfInputNumberSlider(sliderHandler);

        assertEquals(paddingAfterClick, expectedPadding, "The padding now should be " + expectedPadding);

        return paddingAfterClick;
    }

    /**
     * Checks the value in the input and compare it to the real position of handler
     *
     * @param input
     *            the input from which the value will be checked
     * @param currentPaddingOfHandler
     *            the current padding of the handler
     */
    private void checkTheInputAccordingToPositionOfHandler(JQueryLocator input, int currentPaddingOfHandler) {

        assertEquals(Integer.valueOf(selenium.getValue(input)).intValue(), currentPaddingOfHandler,
            "The value in input does not relflect the real value of position of handler!");

    }

    /**
     * Gets the padding left value, this can achieved via calling method getStyle(locator, cssProperty), but it returns
     * padding in pixels.
     */
    private int getPaddingLeftOfInputNumberSlider(JQueryLocator handler) {

        String style = selenium.getAttribute(handler.getAttribute(Attribute.STYLE));

        int indexOfPaddingLeft = style.indexOf("padding-left");

        String paddingLeftAndOther = style.substring(indexOfPaddingLeft);

        int indexOfFirstSemicolon = paddingLeftAndOther.indexOf(';');

        String paddingLeft = paddingLeftAndOther.substring(14, indexOfFirstSemicolon - 1);

        return Integer.valueOf(paddingLeft);
    }
}
