/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jPush;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.pushAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.bean.a4j.A4JPushBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

public class TestTwoPush extends AbstractWebDriverTest {

    @Page
    private TwoPushPage page;

    private void clickPushEnableCheckbox(boolean waitForReinitialization) {
        // Graphene.guardAjax doesn't work here
        String requestTime = page.getRequestTimeElement().getText();
        page.pushEnabledChckBox.click();
        Graphene.waitAjax().until().element(page.getRequestTimeElement()).text().not().equalTo(requestTime);
        if (waitForReinitialization) {
            waitUntilPushReinits();
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jPush/twoPush.xhtml");
    }

    private DateTime getTimeFromOutput(WebElement output) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(A4JPushBean.DATE_PATTERN);
        String text = output.getText();
        return dtf.parseDateTime(text);
    }

    @Test
    public void testBothPushes() {
        verifyPushUpdate(3, false, page.push1Btn, page.output1);
        verifyPushUpdate(3, false, page.push2Btn, page.output2);
        verifyPushUpdate(1, true, page.push1Btn, page.output2);
        verifyPushUpdate(1, true, page.push2Btn, page.output1);
        clickPushEnableCheckbox(true);//disable 1st push
        verifyPushUpdate(3, false, page.push2Btn, page.output2);
        verifyPushUpdate(3, true, page.push1Btn, page.output1);
        verifyPushUpdate(1, true, page.push1Btn, page.output2);
        verifyPushUpdate(1, true, page.push2Btn, page.output1);
        clickPushEnableCheckbox(true);//enable 1st push
        verifyPushUpdate(3, false, page.push2Btn, page.output2);
        verifyPushUpdate(3, false, page.push1Btn, page.output1);
        verifyPushUpdate(1, true, page.push1Btn, page.output2);
        verifyPushUpdate(1, true, page.push2Btn, page.output1);
    }

    @Test
    public void testOnSubscribed() {
        pushAttributes.set(PushAttributes.onsubscribed, "sessionStorage.setItem('metamerEvents', metamerEvents += 'onsubscribed ')");
        final String expected1 = "onsubscribed onsubscribed";
        final String expected2 = "onsubscribed onsubscribed onsubscribed";
        // first onsubscribed event receive immediatelly after form update
        String event = expectedReturnJS("return sessionStorage.getItem('metamerEvents')", expected1);
        // there are 2 push components on page (this example verify that one doesn't influence another one)
        assertEquals(event, expected1, "Attribute onsubscribed should be called 2 times on page load");
        clickPushEnableCheckbox(false);//disable
        clickPushEnableCheckbox(true);//enable
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
    public void testPushEnable() {
        clickPushEnableCheckbox(false);// disable push updates
        clickPushEnableCheckbox(true);// enable push updates
        verifyPushUpdate(5, false, page.push1Btn, page.output1);
    }

    @Test
    public void testSimplePushEventReceive() {
        verifyPushUpdate(5, false, page.push1Btn, page.output1);
    }

    /**
     * Verifies, that push component receives or not receives updates after push
     * button is clicked.
     */
    private void verifyPushUpdate(int numberOfChecks, boolean shouldNotReceiveUpdate, WebElement pushButton, WebElement outputWithTime) {
        long waitTime = 1200L;
        DateTime time1, time2;
        for (int i = 0; i < numberOfChecks; ++i) {
            time1 = getTimeFromOutput(outputWithTime);
            waiting(waitTime);//wait, so the seconds of output (received date) should increase after button clicked
            MetamerPage.requestTimeChangesWaiting(pushButton).click();
            time2 = getTimeFromOutput(outputWithTime);
            if (shouldNotReceiveUpdate) {
                assertEquals(time2, time1, "Time in output should not change when the push is disabled.");
            } else {
                assertTrue(time2.isAfter(time1), "Time should be increasing.");
            }
        }
    }

    /**
     * When push component on page is disabled/re-enabled the same and even
     * other
     * push components don't receive updates for some time. This method should
     * wait until push receives updates. It continously clicks the second push
     * button (second push is always enabled) and checks if it received an
     * update.
     * Because of https://issues.jboss.org/browse/RF-12096.
     */
    private void waitUntilPushReinits() {
        new WebDriverWait(driver, 70, 2000)
            .withMessage("Waiting for push to reinitialize")
            .until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    DateTime time1 = getTimeFromOutput(page.output2);
                    MetamerPage.requestTimeChangesWaiting(page.push2Btn).click();
                    DateTime time2 = getTimeFromOutput(page.output2);
                    return time2.isAfter(time1);
                }
            });
    }
}
