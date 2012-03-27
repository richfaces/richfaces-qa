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
package org.richfaces.tests.metamer.ftest.a4jCommandLink;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.commandLinkAttributes;
import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jCommandLink/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestA4JCommandLink extends AbstractAjocadoTest {

    private JQueryLocator input = pjq("input[id$=input]");
    private JQueryLocator link = pjq("a[id$=a4jCommandLink]");
    private JQueryLocator output1 = pjq("span[id$=output1]");
    private JQueryLocator output2 = pjq("span[id$=output2]");
    private JQueryLocator output3 = pjq("span[id$=output3]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jCommandLink/simple.xhtml");
    }

    @Test(groups = "client-side-perf")
    public void testSimpleClick() {
        guardNoRequest(selenium).typeKeys(input, "RichFaces 4");
        guardXhr(selenium).click(link);

        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));

        String output = selenium.getText(output1);
        assertEquals(output, "RichFaces 4", "output1 when 'RichFaces 4' in input");

        output = selenium.getText(output2);
        assertEquals(output, "RichFa", "output2 when 'RichFaces 4' in input");

        output = selenium.getText(output3);
        assertEquals(output, "RICHFACES 4", "output3 when 'RichFaces 4' in input");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleClickUnicode() {
        guardNoRequest(selenium).typeKeys(input, "ľščťžýáíéňô");
        guardXhr(selenium).click(link);

        waitGui.until(textEquals.locator(output1).text("ľščťžýáíéňô"));

        String output = selenium.getText(output1);
        assertEquals(output, "ľščťžýáíéňô", "output1 when 'ľščťžýáíéňô' in input");

        output = selenium.getText(output2);
        assertEquals(output, "ľščťžý", "output2 when 'ľščťžýáíéňô' in input");

        output = selenium.getText(output3);
        assertEquals(output, "ĽŠČŤŽÝÁÍÉŇÔ", "output3 when 'ľščťžýáíéňô' in input");
    }

    @Test
    public void testAction() {
        commandLinkAttributes.set(CommandLinkAttributes.action, "doubleStringAction");
        selenium.typeKeys(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));
        assertEquals(selenium.getText(output2), "RichFaces 4RichFaces 4",
            "output2 when 'RichFaces 4' in input and doubleStringAction selected");

        commandLinkAttributes.set(CommandLinkAttributes.action, "first6CharsAction");
        selenium.typeKeys(input, "RichFaces 4ň");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ň"));
        assertEquals(selenium.getText(output2), "RichFa", "output2 when 'RichFaces 4ň' in input and first6CharsAction selected");

        commandLinkAttributes.set(CommandLinkAttributes.action, "toUpperCaseAction");
        selenium.typeKeys(input, "RichFaces 4ě");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ě"));
        assertEquals(selenium.getText(output2), "RICHFACES 4Ě",
            "output2 when 'RichFaces 4ě' in input and toUpperCaseAction selected");
    }

    @Test
    public void testActionListener() {
        commandLinkAttributes.set(CommandLinkAttributes.actionListener, "doubleStringActionListener");
        selenium.typeKeys(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));
        assertEquals(selenium.getText(output3), "RichFaces 4RichFaces 4",
            "output3 when 'RichFaces 4' in input and doubleStringActionListener selected");

        commandLinkAttributes.set(CommandLinkAttributes.actionListener, "first6CharsActionListener");
        selenium.typeKeys(input, "RichFaces 4ň");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ň"));
        assertEquals(selenium.getText(output3), "RichFa",
            "output3 when 'RichFaces 4ň' in input and first6CharsActionListener selected");

        commandLinkAttributes.set(CommandLinkAttributes.actionListener, "toUpperCaseActionListener");
        selenium.typeKeys(input, "RichFaces 4ě");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ě"));
        assertEquals(selenium.getText(output3), "RICHFACES 4Ě",
            "output3 when 'RichFaces 4ě' in input and toUpperCaseActionListener selected");
    }

    @Test
    public void testBypassUpdates() {
        commandLinkAttributes.set(CommandLinkAttributes.bypassUpdates, true);

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output 1 should not change");
        assertEquals(selenium.getText(output2), "", "Output 2 should not change");
        assertEquals(selenium.getText(output3), "", "Output 3 should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(3)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(4)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");
    }

    @Test
    public void testCharset() {
        testHtmlAttribute(link, "charset", "utf-8");
    }

    @Test
    public void testCoords() {
        testHtmlAttribute(link, "coords", "circle: 150, 60, 60");
    }

    @Test
    public void testData() {
        commandLinkAttributes.set(CommandLinkAttributes.data, "RichFaces 4");
        commandLinkAttributes.set(CommandLinkAttributes.oncomplete, "data = event.data");

        String reqTime = selenium.getText(time);

        selenium.type(input, "some input text");
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    @Test
    public void testDisabled() {
        JQueryLocator newLink = pjq("span[id$=a4jCommandLink]");

        commandLinkAttributes.set(CommandLinkAttributes.disabled, true);

        assertFalse(selenium.isElementPresent(link), link.getRawLocator() + " should not be on page when the link is disabled");
        assertTrue(selenium.isElementPresent(newLink), newLink.getRawLocator() + " should be on page when the link is disabled");
    }

    @Test
    public void testExecute() {
        commandLinkAttributes.set(CommandLinkAttributes.execute, "input executeChecker");

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChangeAndReturn("", retrieveText.locator(output1));

        JQueryLocator logItems = jq("ul.phases-list li:eq({0})");
        for (int i = 0; i < 6; i++) {
            if ("* executeChecker".equals(selenium.getText(logItems.format(i)))) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    @Test
    public void testHreflang() {
        testHtmlAttribute(link, "hreflang", "sk");
    }

    @Test
    public void testImmediate() {
        commandLinkAttributes.set(CommandLinkAttributes.immediate, true);

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output 1 should not change");
        assertEquals(selenium.getText(output2), "", "Output 2 should not change");
        assertEquals(selenium.getText(output3), "", "Output 3 should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(2)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(3)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");
    }

    @Test
    public void testLimitRender() {
        commandLinkAttributes.set(CommandLinkAttributes.limitRender, true);

        String timeValue = selenium.getText(time);

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        assertEquals(selenium.getText(time), timeValue, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    @Test
    public void testEvents() {
        commandLinkAttributes.set(CommandLinkAttributes.onbegin, "metamerEvents += \"begin \"");
        commandLinkAttributes.set(CommandLinkAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        commandLinkAttributes.set(CommandLinkAttributes.oncomplete, "metamerEvents += \"complete \"");

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, link);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, link);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, link);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, link);
    }

    @Test
    public void testOneyup() {
        testFireEvent(Event.KEYUP, link);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, link);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, link);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, link);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, link);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, link);
    }

    @Test
    public void testRel() {
        testHtmlAttribute(link, "rel", "metamer");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10555")
    public void testRender() {
        JQueryLocator renderInput = pjq("input[name$=renderInput]");

        selenium.type(renderInput, "output1");
        selenium.waitForPageToLoad();

        selenium.typeKeys(input, "aaa");
        guardXhr(selenium).click(link);

        waitGui.until(textEquals.locator(output1).text("aaa"));

        String output = selenium.getText(output1);
        assertEquals(output, "aaa", "output1 when 'aaa' in input and 'output1' set to be rerendered");

        output = selenium.getText(output2);
        assertEquals(output, "", "output2 when 'aaa' in input and 'output1' set to be rerendered");

        output = selenium.getText(output3);
        assertEquals(output, "", "output3 when 'aaa' in input and 'output1' set to be rerendered");

        selenium.type(renderInput, "output2 output3");
        selenium.waitForPageToLoad();

        selenium.typeKeys(input, "bbb");
        guardXhr(selenium).click(link);

        waitGui.until(textEquals.locator(output2).text("bbb"));

        output = selenium.getText(output1);
        assertEquals(output, "aaa", "output1 when 'bbb' in input and 'output2 output3' set to be rerendered");

        output = selenium.getText(output2);
        assertEquals(output, "bbb", "output2 when 'bbb' in input and 'output2 output3' set to be rerendered");

        output = selenium.getText(output3);
        assertEquals(output, "BBB", "output3 when 'bbb' in input and 'output2 output3' set to be rerendered");
    }

    @Test
    public void testRendered() {
        commandLinkAttributes.set(CommandLinkAttributes.rendered, false);
        assertFalse(selenium.isElementPresent(link), "Link should not be displayed");
    }

    @Test
    public void testRev() {
        testHtmlAttribute(link, "rev", "metamer");
    }

    @Test
    public void testShape() {
        testHtmlAttribute(link, "shape", "default");
    }

    @Test
    public void testStyle() {
        testStyle(link);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9307")
    public void testStyleClass() {
        testStyleClass(link);
    }

    @Test
    public void testTitle() {
        testTitle(link);
    }

    @Test
    public void testType() {
        testHtmlAttribute(link, "type", "metamer");
    }

    @Test
    public void testValue() {
        commandLinkAttributes.set(CommandLinkAttributes.value, "new label");
        assertEquals(selenium.getText(link), "new label", "Value of the button did not change");
    }
}
