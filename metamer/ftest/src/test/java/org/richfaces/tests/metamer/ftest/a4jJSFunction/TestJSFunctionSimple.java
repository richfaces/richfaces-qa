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
package org.richfaces.tests.metamer.ftest.a4jJSFunction;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jJSFunction/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class TestJSFunctionSimple extends AbstractWebDriverTest {

    private final Attributes<JSFunctionAttributes> jsFunctionAttributes = getAttributes();

    @Page
    private JSFunctionPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jJSFunction/simple.xhtml");
    }

    @Test
    public void testSimpleClick() {
        String time1Value = page.time1.getText();
        String time2Value = page.time2.getText();
        String yearValue = page.year.getText();
        String ajaxRenderedTimeValue = page.ajaxRenderedTime.getText();

        Graphene.guardAjax(page.link).click();

        Graphene.waitAjax().withMessage("Time1 did not change.").until().element(page.time1).text().not()
            .equalTo(time1Value);
        assertNotSame(page.time2.getText(), time2Value, "Time2 did not change");
        assertEquals(page.year.getText(), yearValue, "Year should not change");
        assertNotSame(page.ajaxRenderedTime.getText(), ajaxRenderedTimeValue, "Ajax rendered time did not change");
    }

    @Test(groups = "smoke")
    public void testAction() {
        jsFunctionAttributes.set(JSFunctionAttributes.action, "increaseYearAction");

        int yearValue = Integer.parseInt(page.year.getText());
        String time1Value = page.time1.getText();

        Graphene.guardAjax(page.link).click();
        Graphene.waitAjax().withMessage("Time1 did not change.").until().element(page.time1).text().not()
            .equalTo(time1Value);
        assertEquals(Integer.parseInt(page.year.getText()), yearValue + 1, "Action was not invoked in first request.");

        Graphene.guardAjax(page.link).click();
        Graphene.waitAjax().withMessage("Time1 did not change.").until().element(page.time1).text().not()
            .equalTo(time1Value);
        assertEquals(Integer.parseInt(page.year.getText()), yearValue + 2, "Action was not invoked in second request.");

        page.assertListener(PhaseId.INVOKE_APPLICATION, MetamerPage.STRING_ACTION_MSG);
    }

    @Test
    public void testActionListener() {
        jsFunctionAttributes.set(JSFunctionAttributes.actionListener, "increaseYearActionListener");

        int yearValue = Integer.parseInt(page.year.getText());
        String time1Value = page.time1.getText();

        Graphene.guardAjax(page.link).click();
        Graphene.waitAjax().withMessage("Time1 did not change.").until().element(page.time1).text().not()
            .equalTo(time1Value);
        assertEquals(Integer.parseInt(page.year.getText()), yearValue + 1,
            "Action listener was not invoked in first request.");

        Graphene.guardAjax(page.link).click();
        Graphene.waitAjax().withMessage("Time1 did not change.").until().element(page.time1).text().not()
            .equalTo(time1Value);
        assertEquals(Integer.parseInt(page.year.getText()), yearValue + 2,
            "Action listener was not invoked in second request.");

        page.assertListener(PhaseId.INVOKE_APPLICATION, MetamerPage.STRING_ACTIONLISTENER_MSG);
    }

    @Test
    public void testBypassUpdates() {
        jsFunctionAttributes.set(JSFunctionAttributes.action, "decreaseYearAction");
        jsFunctionAttributes.set(JSFunctionAttributes.bypassUpdates, true);

        String time1Value = page.time1.getText();
        Graphene.guardAjax(page.link).click();
        Graphene.waitAjax().withMessage("Time1 did not change.").until().element(page.time1).text().not()
            .equalTo(time1Value);

        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, MetamerPage.STRING_ACTION_MSG);
    }

    @Test
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                MetamerPage.waitRequest(page.link, WaitRequestType.XHR).click();
            }
        });
    }

    @Test
    public void testExecute() {
        jsFunctionAttributes.set(JSFunctionAttributes.execute, "input executeChecker");

        MetamerPage.waitRequest(page.link, WaitRequestType.XHR).click();

        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, MetamerPage.STRING_EXECUTE_CHECKER_MSG);
    }

    @Test
    public void testImmediate() {
        jsFunctionAttributes.set(JSFunctionAttributes.actionListener, "decreaseYearActionListener");
        jsFunctionAttributes.set(JSFunctionAttributes.immediate, true);

        MetamerPage.waitRequest(page.link, WaitRequestType.XHR).click();

        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, MetamerPage.STRING_ACTIONLISTENER_MSG);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10011")
    public void testLimitRender() {
        jsFunctionAttributes.set(JSFunctionAttributes.limitRender, true);

        String time1Value = page.time1.getText();
        String time2Value = page.time2.getText();
        String yearValue = page.year.getText();
        String ajaxRenderedTimeValue = page.ajaxRenderedTime.getText();

        Graphene.guardAjax(page.link).click();

        Graphene.waitAjax().withMessage("Time1 did not change.").until().element(page.time1).text().not()
            .equalTo(time1Value);
        assertNotSame(page.time2.getText(), time2Value, "Time2 did not change");
        assertEquals(page.year.getText(), yearValue, "Year should not change");
        assertEquals(page.ajaxRenderedTime.getText(), ajaxRenderedTimeValue, "Ajax rendered time should not change");
    }

    @Test
    public void testName() {
        jsFunctionAttributes.set(JSFunctionAttributes.name, "metamer");

        testSimpleClick();
    }

    @Test
    public void testEvents() {
        jsFunctionAttributes.set(JSFunctionAttributes.onbegin, "metamerEvents += \"begin \"");
        jsFunctionAttributes.set(JSFunctionAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        jsFunctionAttributes.set(JSFunctionAttributes.oncomplete, "metamerEvents += \"complete \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");
        MetamerPage.waitRequest(page.link, WaitRequestType.XHR).click();

        String[] events = ((JavascriptExecutor) driver).executeScript("return metamerEvents").toString().split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testRender() {
        jsFunctionAttributes.set(JSFunctionAttributes.render, "time1");

        String time1Value = page.time1.getText();
        String time2Value = page.time2.getText();
        String yearValue = page.year.getText();
        String ajaxRenderedTimeValue = page.ajaxRenderedTime.getText();

        MetamerPage.waitRequest(page.link, WaitRequestType.XHR).click();

        assertNotSame(page.time1.getText(), time1Value, "Time1 should not change");
        assertEquals(page.time2.getText(), time2Value, "Time2 did not change");
        assertEquals(page.year.getText(), yearValue, "Year should not change");
        assertNotSame(page.ajaxRenderedTime.getText(), ajaxRenderedTimeValue, "Ajax rendered time should change");
    }

    @Test
    public void testRendered() {
        jsFunctionAttributes.set(JSFunctionAttributes.rendered, false);

        String time1Value = page.time1.getText();

        MetamerPage.waitRequest(page.link, WaitRequestType.NONE).click();

        assertEquals(page.time1.getText(), time1Value, "Time1 should not change");
    }

    @Test
    public void testStatus() {
        jsFunctionAttributes.set(JSFunctionAttributes.status, "statusChecker");

        String statusCheckerTime = page.getStatusCheckerOutputElement().getText();

        MetamerPage.waitRequest(page.link, WaitRequestType.XHR).click();

        assertNotSame(page.getStatusCheckerOutputElement().getText(), statusCheckerTime, "Attribute status doesn't work");
    }
}
