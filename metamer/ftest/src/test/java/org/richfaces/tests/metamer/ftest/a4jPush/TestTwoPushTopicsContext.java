/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.a4jPush;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.richfaces.tests.metamer.bean.a4j.A4JPushBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

@Unstable
public class TestTwoPushTopicsContext extends AbstractWebDriverTest {

    private static final int NUMBER_OF_TESTED_UPDATES = 3;
    private static final boolean TIME_WILL_NOT_UPDATE = Boolean.FALSE;
    private static final boolean TIME_WILL_UPDATE = Boolean.TRUE;

    @Page
    private TwoPushPage page;
    private final Attributes<PushAttributes> pushAttributes = getAttributes();

    private void clickPushEnableCheckbox() {
        // Graphene.guardAjax doesn't work here
        String requestTime = page.getRequestTimeElement().getText();
        page.getPushEnabledChckBoxElement().click();
        Graphene.waitModel().until().element(page.getRequestTimeElement()).text().not().equalTo(requestTime);
    }

    @Override
    public String getComponentTestPagePath() {
        return "a4jPush/twoPush.xhtml";
    }

    private DateTime getTimeFromOutput(WebElement output) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(A4JPushBean.DATE_PATTERN);
        String text = output.getText();
        return dtf.parseDateTime(text);
    }

    @Test(groups = "smoke")
    @CoversAttributes({ "address", "ondataavailable" })
    public void testBothPushes() {
        verifyPushUpdate(NUMBER_OF_TESTED_UPDATES, TIME_WILL_UPDATE, page.getPush1BtnElement(), page.getOutput1Element());
        verifyPushUpdate(NUMBER_OF_TESTED_UPDATES, TIME_WILL_UPDATE, page.getPush2BtnElement(), page.getOutput2Element());
        verifyPushUpdate(1, TIME_WILL_NOT_UPDATE, page.getPush1BtnElement(), page.getOutput2Element());
        verifyPushUpdate(1, TIME_WILL_NOT_UPDATE, page.getPush2BtnElement(), page.getOutput1Element());
        clickPushEnableCheckbox();//disable 1st push
        verifyPushUpdate(NUMBER_OF_TESTED_UPDATES, TIME_WILL_UPDATE, page.getPush2BtnElement(), page.getOutput2Element());
        verifyPushUpdate(NUMBER_OF_TESTED_UPDATES, TIME_WILL_NOT_UPDATE, page.getPush1BtnElement(), page.getOutput1Element());
        verifyPushUpdate(1, TIME_WILL_NOT_UPDATE, page.getPush1BtnElement(), page.getOutput2Element());
        verifyPushUpdate(1, TIME_WILL_NOT_UPDATE, page.getPush2BtnElement(), page.getOutput1Element());
        clickPushEnableCheckbox();//enable 1st push
        verifyPushUpdate(NUMBER_OF_TESTED_UPDATES, TIME_WILL_UPDATE, page.getPush2BtnElement(), page.getOutput2Element());
        verifyPushUpdate(NUMBER_OF_TESTED_UPDATES, TIME_WILL_UPDATE, page.getPush1BtnElement(), page.getOutput1Element());
        verifyPushUpdate(1, TIME_WILL_NOT_UPDATE, page.getPush1BtnElement(), page.getOutput2Element());
        verifyPushUpdate(1, TIME_WILL_NOT_UPDATE, page.getPush2BtnElement(), page.getOutput1Element());
    }

    @Test
    @CoversAttributes({ "address", "ondataavailable", "onsubscribed", "rendered" })
    public void testOnSubscribed() {
        pushAttributes.set(PushAttributes.onsubscribed, "sessionStorage.setItem('metamerEvents', metamerEvents += 'onsubscribed ')");
        final String expected1 = "onsubscribed onsubscribed";
        final String expected2 = "onsubscribed onsubscribed onsubscribed";
        // first onsubscribed event receive immediatelly after form update
        String event = expectedReturnJS("return sessionStorage.getItem('metamerEvents')", expected1);
        // there are 2 push components on page (this example verify that one doesn't influence another one)
        assertEquals(event, expected1, "Attribute onsubscribed should be called 2 times on page load");
        clickPushEnableCheckbox();//disable
        clickPushEnableCheckbox();//enable
        // second onsubscribed event receive after manual re-attach by checkbox
        Graphene.waitModel()
            .withMessage("Onsubscribed should be called 3 times after push reenabled")
            .until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver arg0) {
                    // note there should be 3rd event invoked on re-attach to topic
                    return expected2.equals(expectedReturnJS("return sessionStorage.getItem('metamerEvents')", expected2));
                }
            });
        executeJS("sessionStorage.removeItem('metamerEvents')");
    }

    @Test
    @CoversAttributes({ "address", "ondataavailable", "rendered" })
    public void testPushEnable() {
        clickPushEnableCheckbox();// disable push updates
        clickPushEnableCheckbox();// enable push updates
        verifyPushUpdate(NUMBER_OF_TESTED_UPDATES, TIME_WILL_UPDATE, page.getPush1BtnElement(), page.getOutput1Element());
    }

    @Test
    @CoversAttributes({ "address", "ondataavailable" })
    public void testSimplePushEventReceive() {
        verifyPushUpdate(NUMBER_OF_TESTED_UPDATES, TIME_WILL_UPDATE, page.getPush1BtnElement(), page.getOutput1Element());
    }

    /**
     * Verifies, that push component receives or not receives updates after push
     * button is clicked.
     */
    private void verifyPushUpdate(int numberOfChecks, boolean timeWillUpdate, WebElement pushButton, WebElement outputWithTime) {
        DateTime time1;
        for (int i = 0; i < numberOfChecks; ++i) {
            time1 = getTimeFromOutput(outputWithTime);
            MetamerPage.requestTimeChangesWaiting(pushButton).click();
            Graphene.waitAjax().until(new TimeChangePredicate(time1, outputWithTime, timeWillUpdate));
        }
    }

    private class TimeChangePredicate implements Predicate<WebDriver> {

        private final WebElement outputWithTime;
        private final DateTime timeBefore;
        private final boolean timeWillChange;

        public TimeChangePredicate(DateTime timeBefore, WebElement outputWithTime, boolean timeWillChange) {
            this.timeBefore = timeBefore;
            this.outputWithTime = outputWithTime;
            this.timeWillChange = timeWillChange;
        }

        @Override
        public boolean apply(WebDriver t) {
            if (timeWillChange) {
                return getTimeFromOutput(outputWithTime).isAfter(timeBefore);
            }
            return getTimeFromOutput(outputWithTime).isEqual(timeBefore);
        }

        @Override
        public String toString() {
            return "time to " + (timeWillChange ? "" : "not ") + "change";
        }

    }
}
