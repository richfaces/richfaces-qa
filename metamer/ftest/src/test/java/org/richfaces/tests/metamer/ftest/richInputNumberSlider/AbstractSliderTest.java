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
package org.richfaces.tests.metamer.ftest.richInputNumberSlider;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;

/**
 * Abstract test case for rich:inputNumberSlider.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22499 $
 */
public abstract class AbstractSliderTest extends AbstractAjocadoTest {

    protected JQueryLocator slider = pjq("span[id$=slider]");
    protected JQueryLocator input = pjq("input.rf-insl-inp");
    protected JQueryLocator left = pjq("span.rf-insl-dec");
    protected JQueryLocator right = pjq("span.rf-insl-inc");
    protected JQueryLocator minBoundary = pjq("span.rf-insl-mn");
    protected JQueryLocator maxBoundary = pjq("span.rf-insl-mx");
    protected JQueryLocator track = pjq("span.rf-insl-trc");
    protected JQueryLocator handle = pjq("span.rf-insl-hnd");
    protected JQueryLocator tooltip = pjq("span.rf-insl-tt");
    protected JQueryLocator output = pjq("span[id$=output]");
    protected String[] correctNumbers = {"-10", "-5", "-1", "0", "1", "5", "10"};
    protected String[] smallNumbers = {"-11", "-15", "-100"};
    protected String[] bigNumbers = {"11", "15", "100"};
    protected String[] decimalNumbers = {"1.4999", "5.6", "7.0001", "-5.50001", "-9.9", "1.222e0", "0e0", "-5.50001e0"};
    @Inject
    @Use(empty = true)
    protected String number;
    @Inject
    @Use(empty = true)
    protected Integer delay;
    protected JavaScript clickLeft = new JavaScript("jQuery(\"" + left.getRawLocator() + "\").mousedown().mouseup()");
    protected JavaScript clickRight = new JavaScript("jQuery(\"" + right.getRawLocator() + "\").mousedown().mouseup()");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSlider/simple.xhtml");
    }

    public void testTypeIntoInputCorrect() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, number);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output), number, "Output was not updated.");
    }

    public void testTypeIntoInputSmall() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, number);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output), "-10", "Output was not updated.");
        assertEquals(selenium.getValue(input), "-10", "Input was not updated.");
    }

    public void testTypeIntoInputBig() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, number);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output), "10", "Output was not updated.");
        assertEquals(selenium.getValue(input), "10", "Input was not updated.");
    }

    public void testTypeIntoInputDecimal() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, number);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        Double newNumber = new Double(number);

        assertEquals(selenium.getText(output), newNumber == 0 ? "0" : newNumber.toString(), "Output was not updated.");
        assertEquals(selenium.getValue(input), newNumber == 0 ? "0" : newNumber.toString(), "Input was not updated.");
    }

    public void testTypeIntoInputNotNumber() {
        guardNoRequest(selenium).type(input, "aaa");
        assertEquals(selenium.getText(output), "2", "Output should not be updated.");
        assertEquals(selenium.getValue(input), "2", "Input should not be updated.");
    }

    public void testClickLeft() {
        selenium.type(pjq("input[type=text][id$=delayInput]"), "500");
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=showArrowsInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.runScript(clickLeft);
        selenium.runScript(clickLeft);
        selenium.runScript(clickLeft);
        selenium.runScript(clickLeft);
        waitGui.failWith("Output was not updated.").until(textEquals.locator(output).text("-2"));
    }

    public void testClickRight() {
        selenium.type(pjq("input[type=text][id$=delayInput]"), "500");
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=showArrowsInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.runScript(clickRight);
        selenium.runScript(clickRight);
        selenium.runScript(clickRight);
        selenium.runScript(clickRight);
        waitGui.failWith("Output was not updated.").until(textEquals.locator(output).text("6"));
    }

    public void testClick() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).mouseDownAt(track, new Point(0, 0));

        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "-10", "Output was not updated.");
        int margin = selenium.getElementPositionLeft(handle) - selenium.getElementPositionLeft(track);
        assertTrue(margin <= 3, "Left margin of handle should be 0 (was " + margin + ").");

        reqTime = selenium.getText(time);
        guardXhr(selenium).mouseDownAt(track, new Point(30, 0));
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output), "-7", "Output was not updated.");
        margin = selenium.getElementPositionLeft(handle) - selenium.getElementPositionLeft(track);
        assertTrue(margin >= 27 && margin <= 33, "Left margin of handle should be between 27 and 33 (was " + margin + ").");

        reqTime = selenium.getText(time);
        guardXhr(selenium).mouseDownAt(track, new Point(195, 0));
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "10", "Output was not updated.");
        margin = selenium.getElementPositionLeft(handle) - selenium.getElementPositionLeft(track);
        assertTrue(margin >= 192 && margin <= 198, "Left margin of handle should be between 192 and 198 (was " + margin + ").");
    }
}
