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

import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionLabel;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;

/**
 * Abstract test case for testing h:commandButton and h:commandLink with a4j:ajax.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22154 $
 */
public abstract class AbstractTestCommand extends AbstractAjocadoTest {

    private JQueryLocator input = pjq("input[type=text][id$=input]");
    private JQueryLocator output1 = pjq("div[id$=output1]");
    private JQueryLocator output2 = pjq("div[id$=output2]");

    private LocalReloadTester reloadTester = new LocalReloadTester();

    public void testClick(JQueryLocator command, String text) {
        selenium.type(input, text);
        guardXhr(selenium).click(command);
        String outputValue = waitGui.failWith("Page was not updated").waitForChangeAndReturn("",
            retrieveText.locator(output1));

        assertEquals(outputValue, text, "Wrong output1");
        assertEquals(selenium.getText(output2), text, "Wrong output2");
    }

    public void testBypassUpdates(JQueryLocator command) {
        String reqTime = selenium.getText(time);

        selenium.select(pjq("select[name$=listenerInput]"), optionLabel("doubleStringListener"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=bypassUpdatesInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(command);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "listener invoked");
    }

    public void testData(JQueryLocator command) {
        selenium.type(pjq("input[type=text][id$=dataInput]"), "RichFaces 4");
        selenium.waitForPageToLoad();

        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "data = event.data");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);

        selenium.type(input, "some input text");
        guardXhr(selenium).click(command);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    public void testDisabled(JQueryLocator command) {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.type(input, "RichFaces 4");
        guardHttp(selenium).click(command);

        assertEquals(selenium.getText(output1), "RichFaces 4", "Output1 should not change");
        assertEquals(selenium.getText(output2), "RichFaces 4", "Output2 should not change");
    }

    public void testExecute(JQueryLocator command) {
        selenium.type(pjq("input[type=text][id$=executeInput]"), "input executeChecker");
        selenium.waitForPageToLoad();

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(command);
        waitGui.failWith("Page was not updated").waitForChangeAndReturn("", retrieveText.locator(output1));

        JQueryLocator logItems = jq("ul.phases-list li:eq({0})");
        for (int i = 0; i < 6; i++) {
            if ("* executeChecker".equals(selenium.getText(logItems.format(i)))) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    public void testImmediate(JQueryLocator command) {
        String reqTime = selenium.getText(time);

        selenium.select(pjq("select[name$=listenerInput]"), optionLabel("doubleStringListener"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(command);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "RichFaces 4", "Output should change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testImmediateBypassUpdates(JQueryLocator command) {
        String reqTime = selenium.getText(time);

        selenium.select(pjq("select[name$=listenerInput]"), optionLabel("doubleStringListener"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=bypassUpdatesInput][value=true]"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(command);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testLimitRender(JQueryLocator command) {
        selenium.click(pjq("input[type=radio][name$=limitRenderInput][value=true]"));
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(command);
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        assertEquals(selenium.getText(time), reqTime, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    public void testEvents(JQueryLocator command) {
        selenium.type(pjq("input[type=text][id$=onbeforesubmitInput]"), "metamerEvents += \"beforesubmit \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onbeginInput]"), "metamerEvents += \"begin \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onbeforedomupdateInput]"), "metamerEvents += \"beforedomupdate \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "metamerEvents += \"complete \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(command);
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 4, "4 events should be fired.");
        assertEquals(events[0], "beforesubmit", "Attribute onbeforesubmit doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[3], "complete", "Attribute oncomplete doesn't work");
    }

    public void testRender(JQueryLocator command) {
        selenium.type(pjq("input[type=text][id$=renderInput]"), "output1");
        selenium.waitForPageToLoad();

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(command);
        String outputValue = waitGui.failWith("Page was not updated").waitForChangeAndReturn("",
            retrieveText.locator(output1));

        assertEquals(outputValue, "RichFaces 4", "Wrong output1");
        assertEquals(selenium.getText(output2), "", "Wrong output2");
    }

    public void testStatus(JQueryLocator command) {
        selenium.type(pjq("input[type=text][id$=statusInput]"), "statusChecker");
        selenium.waitForPageToLoad();

        String statusCheckerTime = selenium.getText(statusChecker);
        guardXhr(selenium).click(command);
        waitGui.failWith("Attribute status doesn't work").waitForChange(statusCheckerTime,
            retrieveText.locator(statusChecker));
    }

    public void testRerenderAll(JQueryLocator command) {
        reloadTester.command = command;
        reloadTester.testRerenderAll();
    }

    public void testFullPageRefresh(JQueryLocator command) {
        reloadTester.command = command;
        reloadTester.testFullPageRefresh();
    }

    private class LocalReloadTester extends ReloadTester<String> {
        JQueryLocator command;

        @Override
        public void doRequest(String inputValue) {
            selenium.type(input, inputValue);
            guardXhr(selenium).click(command);
        }

        @Override
        public void verifyResponse(String inputValue) {
            assertEquals(selenium.getText(output1), inputValue, "Wrong output1");
            assertEquals(selenium.getText(output2), inputValue, "Wrong output2");
        }

        @Override
        public String[] getInputValues() {
            return new String[] { "RichFaces 3", "RichFaces 4" };
        }
    };
}
