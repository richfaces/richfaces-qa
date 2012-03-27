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
package org.richfaces.tests.metamer.ftest.richInputNumberSpinner;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;

/**
 * Abstract test case for rich:inputNumberSpinner.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22499 $
 */
public abstract class AbstractSpinnerTest extends AbstractAjocadoTest {

    protected JQueryLocator spinner = pjq("span[id$=spinner]");
    protected JQueryLocator input = pjq("span[id$=spinner] input.rf-insp-inp");
    protected JQueryLocator up = pjq("span[id$=spinner] span.rf-insp-inc");
    protected JQueryLocator down = pjq("span[id$=spinner] span.rf-insp-dec");
    protected JQueryLocator output = pjq("span[id$=output]");
    protected String[] correctNumbers = {"-10", "-5", "-1", "0", "1", "5", "10"};
    protected String[] smallNumbers = {"-11", "-15", "-100"};
    protected String[] bigNumbers = {"11", "15", "100"};
    protected String[] decimalNumbers = {"1.4999", "5.6", "7.0001", "-5.50001", "-9.9", "1.222e0", "0e0", "-5.50001e0"};
    @Inject
    @Use(empty = true)
    protected String number;

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
        assertEquals(getInputValue(), "-10", "Input was not updated.");
    }

    public void testTypeIntoInputBig() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, number);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output), "10", "Output was not updated.");
        assertEquals(getInputValue(), "10", "Input was not updated.");
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
        assertEquals(getInputValue(), "2", "Input should not be updated.");
    }

    public void testClickUp() {
        clickArrow(up, 4);
        assertEquals(selenium.getText(output), "6", "Output was not updated.");

        clickArrow(up, 4);
        assertEquals(selenium.getText(output), "10", "Output was not updated.");

        selenium.mouseDown(up);
        guardNoRequest(selenium).mouseUp(up);

        assertEquals(selenium.getText(output), "10", "Output was not updated.");
    }

    public void testClickDown() {
        clickArrow(down, 4);
        assertEquals(selenium.getText(output), "-2", "Output was not updated.");

        clickArrow(down, 8);
        assertEquals(selenium.getText(output), "-10", "Output was not updated.");

        selenium.mouseDown(down);
        guardNoRequest(selenium).mouseUp(down);

        assertEquals(selenium.getText(output), "-10", "Output was not updated.");
    }

    /**
     * Getter for value that is displayed in spinner input.
     * @return spinner input value
     */
    protected String getInputValue() {
        return selenium.getValue(input);
    }

    /**
     * Clicks on spinner's arrow.
     * @param arrow spinner's up or down arrow locator
     * @param clicks how many times should it be clicked
     */
    protected void clickArrow(ElementLocator<?> arrow, int clicks) {
        String reqTime = null;

        for (int i = 0; i < clicks; i++) {
            reqTime = selenium.getText(time);
            guardXhr(selenium).runScript(new JavaScript("jQuery(\"" + arrow.getRawLocator() + "\").mousedown().mouseup()"));

            waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        }
    }
}
