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

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.ajaxAttributes;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jAjax/hSelectManyCheckbox.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22054 $
 */
public class TestHSelectManyCheckbox extends AbstractAjocadoTest {

    private JQueryLocator input = pjq("table[id$=selectManyCheckbox] input[value=Audi]");
    private JQueryLocator output1 = pjq("span[id$=output1]");
    private JQueryLocator output2 = pjq("span[id$=output2]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jAjax/hSelectManyCheckbox.xhtml");
    }

    @Test
    public void testSimpleClick() {
        guardXhr(selenium).click(input);

        String outputValue = waitGui.failWith("Page was not updated").waitForChangeAndReturn("[Ferrari, Lexus]",
            retrieveText.locator(output1));

        assertEquals(outputValue, "[Audi, Ferrari, Lexus]", "Wrong output1");
        assertEquals(selenium.getText(output2), "[Audi, Ferrari, Lexus]", "Wrong output2");
    }

    @Test
    public void testBypassUpdates() {
        String reqTime = selenium.getText(time);
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.bypassUpdates, true);

        guardXhr(selenium).click(input);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "[Ferrari, Lexus]", "Output should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "listener invoked");
    }

    @Test
    public void testData() {
        ajaxAttributes.set(AjaxAttributes.data, "RichFaces 4 data");
        ajaxAttributes.set(AjaxAttributes.oncomplete, "data = event.data");

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(input);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4 data", "Data sent with ajax request");
    }

    @Test
    public void testDisabled() {
        ajaxAttributes.set(AjaxAttributes.disabled, true);

        guardNoRequest(selenium).click(input);
    }

    @Test
    public void testExecute() {
        ajaxAttributes.set(AjaxAttributes.execute, "input executeChecker");

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(input);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        JQueryLocator logItems = jq("ul.phases-list li:eq({0})");
        for (int i = 0; i < 6; i++) {
            if ("* executeChecker".equals(selenium.getText(logItems.format(i)))) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    @Test
    public void testImmediate() {
        String reqTime = selenium.getText(time);
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.immediate, true);

        guardXhr(selenium).click(input);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "[Audi, Ferrari, Lexus]", "Output should change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    @Test
    public void testImmediateBypassUpdates() {
        String reqTime = selenium.getText(time);

        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.bypassUpdates, true);
        ajaxAttributes.set(AjaxAttributes.immediate, true);

        guardXhr(selenium).click(input);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "[Ferrari, Lexus]", "Output should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    @Test
    public void testLimitRender() {
        ajaxAttributes.set(AjaxAttributes.limitRender, true);

        String reqTime = selenium.getText(time);

        guardXhr(selenium).click(input);
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        assertEquals(selenium.getText(time), reqTime, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    @Test
    public void testEvents() {
        ajaxAttributes.set(AjaxAttributes.onbeforesubmit, "metamerEvents += \"beforesubmit \"");
        ajaxAttributes.set(AjaxAttributes.onbegin, "metamerEvents += \"begin \"");
        ajaxAttributes.set(AjaxAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        ajaxAttributes.set(AjaxAttributes.oncomplete, "metamerEvents += \"complete \"");

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        guardXhr(selenium).click(input);
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 4, "4 events should be fired.");
        assertEquals(events[0], "beforesubmit", "Attribute onbeforesubmit doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[3], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testRender() {
        ajaxAttributes.set(AjaxAttributes.render, "output1");

        guardXhr(selenium).click(input);
        String outputValue = waitGui.failWith("Page was not updated").waitForChangeAndReturn("", retrieveText.locator(output1));

        assertEquals(outputValue, "[Audi, Ferrari, Lexus]", "Wrong output1");
        assertEquals(selenium.getText(output2), "[Ferrari, Lexus]", "Wrong output2");
    }

    @Test
    public void testStatus() {
        ajaxAttributes.set(AjaxAttributes.status, "statusChecker");

        String statusCheckerTime = selenium.getText(statusChecker);
        guardXhr(selenium).click(input);
        waitGui.failWith("Attribute status doesn't work").waitForChange(statusCheckerTime, retrieveText.locator(statusChecker));
    }
}
