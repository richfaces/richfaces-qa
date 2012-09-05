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

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.ajaxAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;

/**
 * Abstract test case for testing h:commandButton and h:commandLink with a4j:ajax.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public abstract class AbstractTestCommand extends AbstractWebDriverTest<AjaxPage> {

    private LocalReloadTester reloadTester = new LocalReloadTester();

    @Override
    protected AjaxPage createPage() {
        return new AjaxPage();
    }

    public void testClick(WebElement command, String text) {
        page.input.sendKeys(text);

        waitRequest(command, WaitRequestType.XHR).click();

        assertEquals(page.output1.getText(), text, "Wrong output1");
        assertEquals(page.output2.getText(), text, "Wrong output2");
    }

    public void testBypassUpdates(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.bypassUpdates, true);

        page.input.sendKeys("RichFaces 4");
        waitRequest(command, WaitRequestType.XHR).click();

        assertEquals(page.output1.getText(), "", "Output should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "listener invoked");
    }

    public void testData(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.data, "RichFaces 4");
        ajaxAttributes.set(AjaxAttributes.oncomplete, "data = event.data");

        page.input.sendKeys("some input text");
        waitRequest(command, WaitRequestType.XHR).click();

        String data = ((JavascriptExecutor) driver).executeScript("return data").toString();
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    public void testDisabled(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.disabled, true);

        page.input.sendKeys("RichFaces 4");
        Graphene.guardHttp(command).click();

        assertEquals(page.output1.getText(), "RichFaces 4", "Output1 did not change");
        assertEquals(page.output2.getText(), "RichFaces 4", "Output2 did not change");
    }

    public void testExecute(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.execute, "[input, executeChecker]");

        page.input.sendKeys("RichFaces 4");
        waitRequest(command, WaitRequestType.XHR).click();

        for (WebElement element : page.phases) {
            if ("* executeChecker".equals(element.getText())) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    public void testImmediate(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.immediate, true);

        page.input.sendKeys("RichFaces 4");
        waitRequest(command, WaitRequestType.XHR).click();

        assertEquals(page.output1.getText(), "RichFaces 4", "Output1 did not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testImmediateBypassUpdates(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.bypassUpdates, true);
        ajaxAttributes.set(AjaxAttributes.immediate, true);

        page.input.sendKeys("RichFaces 4");
        waitRequest(command, WaitRequestType.XHR).click();

        assertEquals(page.output1.getText(), "", "Output 1 should not change");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testLimitRender(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.limitRender, true);

        String reqTime = page.requestTime.getText();
        page.input.sendKeys("RichFaces 4");
        Graphene.guardXhr(command).click();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.output1).textEquals("RichFaces 4"));

        assertEquals(page.requestTime.getText(), reqTime, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    public void testEvents(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.onbeforesubmit, "metamerEvents += \"beforesubmit \"");
        ajaxAttributes.set(AjaxAttributes.onbegin, "metamerEvents += \"begin \"");
        ajaxAttributes.set(AjaxAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        ajaxAttributes.set(AjaxAttributes.oncomplete, "metamerEvents += \"complete \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");

        page.input.sendKeys("RichFaces 4");
        waitRequest(command, WaitRequestType.XHR).click();

        String[] events = ((JavascriptExecutor) driver).executeScript("return metamerEvents").toString().split(" ");

        assertEquals(events.length, 4, "4 events should be fired.");
        assertEquals(events[0], "beforesubmit", "Attribute onbeforesubmit doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[3], "complete", "Attribute oncomplete doesn't work");
    }

    public void testRender(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.render, "[output1]");

        page.input.sendKeys("RichFaces 4");
        waitRequest(command, WaitRequestType.XHR).click();

        assertEquals(page.output1.getText(), "RichFaces 4", "Output1 should change");
        assertEquals(page.output2.getText(), "", "Output2 should not change");
    }

    public void testStatus(WebElement command) {
        ajaxAttributes.set(AjaxAttributes.status, "statusChecker");

        String statusCheckerTime = page.statusCheckerOutput.getText();
        Graphene.guardXhr(command).click();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.statusCheckerOutput).not().textEquals(statusCheckerTime));
    }

    public void testRerenderAll(WebElement command) {
        reloadTester.command = command;
        reloadTester.testRerenderAll();
    }

    public void testFullPageRefresh(WebElement command) {
        reloadTester.command = command;
        reloadTester.testFullPageRefresh();
    }

    private class LocalReloadTester extends ReloadTester<String> {
        WebElement command;

        @Override
        public void doRequest(String inputValue) {
            String reqTime = page.requestTime.getText();
            page.input.clear();
            page.input.sendKeys(inputValue);
            Graphene.guardXhr(command).click();
            Graphene.waitModel().withMessage("Page was not updated")
                .until(Graphene.element(page.requestTime).not().textEquals(reqTime));
        }

        @Override
        public void verifyResponse(String inputValue) {
            assertEquals(page.output1.getText(), inputValue, "Wrong output1");
            assertEquals(page.output2.getText(), inputValue, "Wrong output2");
        }

        @Override
        public String[] getInputValues() {
            return new String[] { "RichFaces 3", "RichFaces 4" };
        }
    };
}
