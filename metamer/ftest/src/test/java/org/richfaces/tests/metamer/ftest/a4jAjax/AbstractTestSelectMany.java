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
 * Abstract test case for testing h:selectManyMenu and h:selectManyListbox with a4j:ajax.
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public abstract class AbstractTestSelectMany extends AbstractWebDriverTest<AjaxPage> {

    @Override
    protected AjaxPage createPage() {
        return new AjaxPage();
    }

    public void testClick() {
        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        assertOutput1Changed();
        assertOutput2Changed();
    }

    public void testBypassUpdates() {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.bypassUpdates, true);

        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        assertOutput1NotChanged();
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "listener invoked");
    }

    public void testData() {
        ajaxAttributes.set(AjaxAttributes.data, "RichFaces 4 data");
        ajaxAttributes.set(AjaxAttributes.oncomplete, "data = event.data");

        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        String data = ((JavascriptExecutor) driver).executeScript("return data").toString();
        assertEquals(data, "RichFaces 4 data", "Data sent with ajax request");
    }

    public void testExecute() {
        ajaxAttributes.set(AjaxAttributes.execute, "[input, executeChecker]");

        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        for (WebElement element : page.phases) {
            if ("* executeChecker".equals(element.getText())) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    public void testImmediate() {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.immediate, true);

        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        assertOutput1Changed();
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testImmediateBypassUpdates() {
        ajaxAttributes.set(AjaxAttributes.listener, "doubleStringListener");
        ajaxAttributes.set(AjaxAttributes.bypassUpdates, true);
        ajaxAttributes.set(AjaxAttributes.immediate, true);

        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        assertOutput1NotChanged();
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "listener invoked");
    }

    public void testLimitRender(String expectedOutput) {
        ajaxAttributes.set(AjaxAttributes.limitRender, true);

        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.output1).textEquals(expectedOutput));

        assertEquals(page.requestTime.getText(), reqTime, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    public void testEvents() {
        ajaxAttributes.set(AjaxAttributes.onbeforesubmit, "metamerEvents += \"beforesubmit \"");
        ajaxAttributes.set(AjaxAttributes.onbegin, "metamerEvents += \"begin \"");
        ajaxAttributes.set(AjaxAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        ajaxAttributes.set(AjaxAttributes.oncomplete, "metamerEvents += \"complete \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");
        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        String[] events = ((JavascriptExecutor) driver).executeScript("return metamerEvents").toString().split(" ");

        assertEquals(events.length, 4, "4 events should be fired.");
        assertEquals(events[0], "beforesubmit", "Attribute onbeforesubmit doesn't work");
        assertEquals(events[1], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[2], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[3], "complete", "Attribute oncomplete doesn't work");
    }

    public void testRender() {
        ajaxAttributes.set(AjaxAttributes.render, "[output1]");

        String reqTime = page.requestTime.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        assertOutput1Changed();
        assertOutput2NotChanged();
    }

    public void testStatus() {
        ajaxAttributes.set(AjaxAttributes.status, "statusChecker");

        String statusCheckerTime = page.statusCheckerOutput.getText();
        performAction();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.statusCheckerOutput).not().textEquals(statusCheckerTime));
    }

    public abstract void performAction();
    public abstract void assertOutput1Changed();
    public abstract void assertOutput1NotChanged();
    public abstract void assertOutput2Changed();
    public abstract void assertOutput2NotChanged();
}
