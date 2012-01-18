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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionLabel;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;

/**
 * Abstract test case for testing h:inputText, h:inputSecret and h:inputTextarea with a4j:ajax.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22054 $
 */
public abstract class AbstractTestTextInput extends AbstractAjocadoTest {

    private JQueryLocator output1 = pjq("div[id$=output1]");
    private JQueryLocator output2 = pjq("div[id$=output2]");

    private void typeToInput(JQueryLocator input, String text) {
        for (int i = 1; i <= text.length(); i++) {
            selenium.type(input, text.substring(0, i));
            guardXhr(selenium).fireEvent(input, Event.KEYUP);
        }
    }

    public void testType(JQueryLocator input, String text) {
        typeToInput(input, text);
        String outputValue = waitGui.failWith("Page was not updated").waitForChangeAndReturn("",
            retrieveText.locator(output1));

        assertEquals(outputValue, text, "Wrong output1");
        assertEquals(selenium.getText(output2), text, "Wrong output2");
    }

    public void testBypassUpdates(JQueryLocator input) {
        String reqTime = selenium.getText(time);

        selenium.select(pjq("select[name$=listenerInput]"), optionLabel("doubleStringListener"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=bypassUpdatesInput][value=true]"));
        selenium.waitForPageToLoad();

        typeToInput(input, "RichFaces 4");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "listener invoked");
    }

    public void testData(JQueryLocator input) {
        selenium.type(pjq("input[type=text][id$=dataInput]"), "RichFaces 4 data");
        selenium.waitForPageToLoad();

        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "data = event.data");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);

        typeToInput(input, "RichFaces 4");

        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4 data", "Data sent with ajax request");
    }

    public void testDisabled(JQueryLocator input) {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        guardNoRequest(selenium).type(input, "RichFaces 4");
    }

    public void testExecute(JQueryLocator input) {
        selenium.type(pjq("input[type=text][id$=executeInput]"), "@this executeChecker");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        typeToInput(input, "RichFaces 4");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        JQueryLocator logItems = jq("ul.phases-list li:eq({0})");
        for (int i = 0; i < 6; i++) {
            if ("* executeChecker".equals(selenium.getText(logItems.format(i)))) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    public void testImmediate(JQueryLocator input) {
        String reqTime = selenium.getText(time);

        selenium.select(pjq("select[name$=listenerInput]"), optionLabel("doubleStringListener"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        typeToInput(input, "RichFaces 4");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "RichFaces 4", "Output should change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testImmediateBypassUpdates(JQueryLocator input) {
        String reqTime = selenium.getText(time);

        selenium.select(pjq("select[name$=listenerInput]"), optionLabel("doubleStringListener"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=bypassUpdatesInput][value=true]"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        typeToInput(input, "RichFaces 4");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testLimitRender(JQueryLocator input) {
        selenium.click(pjq("input[type=radio][name$=limitRenderInput][value=true]"));
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);

        typeToInput(input, "RichFaces 4");
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        assertEquals(selenium.getText(time), reqTime, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    public void testEvents(JQueryLocator input) {
        selenium.type(pjq("input[type=text][id$=onbeforesubmitInput]"), "metamerEvents += \"beforesubmit \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onbeginInput]"), "metamerEvents += \"begin \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onbeforedomupdateInput]"), "metamerEvents += \"beforedomupdate \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "metamerEvents += \"complete \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        typeToInput(input, "RichFaces 4");
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 44, "4 events should be fired.");
        assertEquals(events[0], "beforesubmit", "Attribute onbeforesubmit doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[3], "complete", "Attribute oncomplete doesn't work");
    }

    public void testRender(JQueryLocator input) {
        selenium.type(pjq("input[type=text][id$=renderInput]"), "output1");
        selenium.waitForPageToLoad();

        typeToInput(input, "RichFaces 4");
        String outputValue = waitGui.failWith("Page was not updated").waitForChangeAndReturn("",
            retrieveText.locator(output1));

        assertEquals(outputValue, "RichFaces 4", "Wrong output1");
        assertEquals(selenium.getText(output2), "", "Wrong output2");
    }

    public void testStatus(JQueryLocator input) {
        selenium.type(pjq("input[type=text][id$=statusInput]"), "statusChecker");
        selenium.waitForPageToLoad();

        String statusCheckerTime = selenium.getText(statusChecker);
        typeToInput(input, "RichFaces 4");
        waitGui.failWith("Attribute status doesn't work").waitForChange(statusCheckerTime,
            retrieveText.locator(statusChecker));
    }
}
