/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.EnumMap;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * Abstract test case for testing h:selectManyMenu and h:selectManyListbox with a4j:ajax.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public abstract class AbstractAjaxTest extends AbstractWebDriverTest {

    private static final int LENGTH_6 = 6;

    private final Attributes<AjaxAttributes> ajaxAttributes = getAttributes();

    private final String causeErrorListener = "causeErrorListener";
    private EnumMap<Listener, Action> enumMap = new EnumMap<Listener, Action>(Listener.class);
    protected Listener listener;

    @Page
    protected AjaxPage page;

    public void assertOutput1Changed() {
        assertOutput1Equals(getExpectedOutput());
    }

    public void assertOutput1Equals(String value) {
        assertEquals(page.getOutput1Element().getText(), value, "Output1");
    }

    public void assertOutput1NotChanged() {
        assertOutput1Equals(getDefaultOutput());
    }

    public void assertOutput2Changed() {
        assertOutput2Equals(getExpectedOutput());
    }

    public void assertOutput2Equals(String value) {
        assertEquals(page.getOutput2Element().getText(), value, "Output2");
    }

    public void assertOutput2NotChanged() {
        assertOutput2Equals(getDefaultOutput());
    }

    public EnumMap<Listener, Action> getActionMapForListeners() {
        if (enumMap.isEmpty()) {
            enumMap.put(Listener.doubleStringListener, new Action() {
                @Override
                public void perform() {
                    performAction();
                    String val = getExpectedOutput();
                    val = val.concat(val);
                    assertOutput1Equals(val);
                    assertOutput2Equals(val);
                }
            });
            enumMap.put(Listener.first6CharsListener, new Action() {
                @Override
                public void perform() {
                    performAction();
                    String val = getExpectedOutput();
                    int endIndex = val.length() > LENGTH_6 ? LENGTH_6 : val.length();
                    val = val.substring(0, endIndex);
                    assertOutput1Equals(val);
                    assertOutput2Equals(val);
                }
            });
            enumMap.put(Listener.toUpperCaseListener, new Action() {
                @Override
                public void perform() {
                    performAction();
                    String val = getExpectedOutput().toUpperCase();
                    assertOutput1Equals(val);
                    assertOutput2Equals(val);
                }
            });
        }
        return enumMap;
    }

    public abstract String getDefaultOutput();

    public abstract String getExpectedOutput();

    public abstract void performAction(String input);

    public abstract void performAction();

    public void testBypassUpdates() {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.bypassUpdates, true);

        String reqTime = page.getRequestTimeElement().getText();
        performAction();
        waiting(500);
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        String output = getDefaultOutput();
        output = output.concat(output);// doubleStringListener
        assertOutput1Equals(output);
        assertOutput2Equals(output);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "listener invoked");
    }

    public void testClick() {
        String reqTime = page.getRequestTimeElement().getText();
        performAction();
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        assertOutput1Changed();
        assertOutput2Changed();
    }

    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                performAction();
            }
        });
    }

    public void testDisabledForTextInputs() {
        ajaxAttributes.set(AjaxAttributes.disabled, true);

        typeKeys("RichFaces 4");

        assertOutput1NotChanged();
        assertOutput2NotChanged();
    }

    public void testEvents() {
        ajaxAttributes.set(AjaxAttributes.onbeforesubmit, "metamerEvents += \"beforesubmit \"");
        ajaxAttributes.set(AjaxAttributes.onbegin, "metamerEvents += \"begin \"");
        ajaxAttributes.set(AjaxAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        ajaxAttributes.set(AjaxAttributes.oncomplete, "metamerEvents += \"complete \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");
        String reqTime = page.getRequestTimeElement().getText();
        performAction();
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        String[] events = ((JavascriptExecutor) driver).executeScript("return metamerEvents").toString().split(" ");

        assertEquals(events.length, 4, "4 events should be fired.");
        assertEquals(events[0], "beforesubmit", "Attribute onbeforesubmit doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[3], "complete", "Attribute oncomplete doesn't work");
    }

    public void testEventsForTextInputs() {
        ajaxAttributes.set(AjaxAttributes.onbeforesubmit, "metamerEvents += \"beforesubmit \"");
        ajaxAttributes.set(AjaxAttributes.onbegin, "metamerEvents += \"begin \"");
        ajaxAttributes.set(AjaxAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        ajaxAttributes.set(AjaxAttributes.oncomplete, "metamerEvents += \"complete \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");
        final String text = "RichFaces";

        for (int i = 1; i <= text.length(); i++) {
            ((JavascriptExecutor) driver).executeScript("$(\"[id$=input]\").val('" + text.substring(0, i) + "');");
            ((JavascriptExecutor) driver).executeScript("$(\"[id$=input]\").trigger('keyup');");
            Graphene.waitModel().until("Page was not updated").element(page.getOutput1Element()).text().equalTo(text.substring(0, i));
        }

        String[] events = ((JavascriptExecutor) driver).executeScript("return metamerEvents").toString().split(" ");

        assertEquals(events.length, 36, "36 events should be fired.");
        assertEquals(events[0], "beforesubmit", "Attribute onbeforesubmit doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[3], "complete", "Attribute oncomplete doesn't work");
    }

    public void testExecute() {
        ajaxAttributes.set(AjaxAttributes.execute, "[input, executeChecker]");

        String reqTime = page.getRequestTimeElement().getText();
        performAction();
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        for (WebElement element : page.getPhasesElements()) {
            if ("* executeChecker".equals(element.getText())) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    public void testFullPageRefresh() {
        new LocalReloadTester().testFullPageRefresh();

    }

    public void testImmediate() {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.immediate, true);

        String reqTime = page.getRequestTimeElement().getText();
        performAction();
        waiting(500);
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        String output = getExpectedOutput();
        output = output.concat(output);// doubleStringListener
        assertOutput1Equals(output);
        assertOutput2Equals(output);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testImmediateBypassUpdates() {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.bypassUpdates, true);
        ajaxAttributes.set(AjaxAttributes.immediate, true);

        String reqTime = page.getRequestTimeElement().getText();
        performAction();
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        String output = getDefaultOutput();
        output = output.concat(output);// doubleStringListener
        assertOutput1Equals(output);
        assertOutput2Equals(output);
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testLimitRender(String expectedOutput) {
        ajaxAttributes.set(AjaxAttributes.limitRender, true);

        String reqTime = page.getRequestTimeElement().getText();
        performAction();
        Graphene.waitModel().until("Page was not updated").element(page.getOutput1Element()).text().equalTo(expectedOutput);

        assertEquals(page.getRequestTimeElement().getText(), reqTime, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    public void testListener(EnumMap<Listener, Action> changeValueAndCheckActionsMap) {
        ajaxAttributes.set(AjaxAttributes.listener, listener);
        changeValueAndCheckActionsMap.get(listener).perform();
    }

    public void testRender() {
        ajaxAttributes.set(AjaxAttributes.render, "[output1]");

        String reqTime = page.getRequestTimeElement().getText();
        performAction();
        waiting(500);
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        assertOutput1Changed();
        assertOutput2NotChanged();
    }

    public void testRerenderAll() {
        new LocalReloadTester().testRerenderAll();
    }

    public void testStatus() {
        ajaxAttributes.set(AjaxAttributes.status, "statusChecker");

        String statusCheckerTime = page.getStatusCheckerOutputElement().getText();
        performAction();
        Graphene.waitModel().until("Page was not updated").element(page.getStatusCheckerOutputElement()).text().not().equalTo(statusCheckerTime);
    }

    public void testType() {
        String reqTime = page.getRequestTimeElement().getText();
        typeKeys("RichFaces 4");
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        assertOutput1Changed();
        assertOutput2Changed();
    }

    public void testTypeUnicode() {
        String reqTime = page.getRequestTimeElement().getText();
        typeKeys("ľščťžýáíéúôň фывацукйешгщь");
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        assertEquals(page.getOutput1Element().getText(), "ľščťžýáíéúôň фывацукйешгщь", "Output1 should change");
        assertEquals(page.getOutput2Element().getText(), "ľščťžýáíéúôň фывацукйешгщь", "Output2 should change");
    }

    protected void typeKeys(String text) {
        for (int i = 1; i <= text.length(); i++) {
            Utils.jQ(executor, "val('" + text.subSequence(0, i) + "')", page.getInputElement());
            Utils.jQ(executor, "trigger('keyup')", page.getInputElement());
            waiting(200);
        }
    }

    public enum Listener {

        doubleStringListener, first6CharsListener, toUpperCaseListener
    }

    private class LocalReloadTester extends ReloadTester<String> {

        @Override
        public void doRequest(String inputValue) {
            String reqTime = page.getRequestTimeElement().getText();
            performAction(inputValue);
            Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);
        }

        @Override
        public void verifyResponse(String inputValue) {
            assertEquals(page.getOutput1Element().getText(), inputValue, "Wrong output1");
            assertEquals(page.getOutput2Element().getText(), inputValue, "Wrong output2");
        }

        @Override
        public String[] getInputValues() {
            return new String[] { "RichFaces 3", "RichFaces 4" };
        }
    }
}
