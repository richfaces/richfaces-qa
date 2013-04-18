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
package org.richfaces.tests.showcase.notify;

import com.google.common.base.Predicate;
import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.graphene.Graphene;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.page.fragments.impl.notify.NotifyMessage;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.notify.page.NotifyAttributesPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class TestNotifyAttributes extends AbstractWebDriverTest {


    @Page
    private NotifyAttributesPage page;

    @ArquillianResource
    private Actions actions;

    @Test
    public void testStayTime() {
        checkStayTime(3500);
        checkStayTime(500);
        checkStayTime(1500);
    }

    @Test
    public void testSticky() {
        page.waitUntilThereIsNoNotify();
        page.setSticky(true);
        page.showNotification();

        boolean ok = false;

        try {
            page.waitUntilThereIsNoNotify();
        } catch (RuntimeException ex) {

            ok = true;
        }

        assertTrue(ok, "The message should not dissapear");

        page.getNotify().getMessageAtIndex(0).close();
        page.waitUntilThereIsNoNotify();
    }

    @Test
    public void testNonBlockingOpacity() {
        checkNonBlockingOpacity("0.5");
        checkNonBlockingOpacity("0");
        checkNonBlockingOpacity("0.9");
    }

    @Test
    public void testShowShadow() {
        page.waitUntilThereIsNoNotify();
        page.setShowShadow(true);
        page.showNotification();

        assertTrue(isElementPresent(page.getNotify().getMessageAtIndex(0).getShadowElement()), "The shadow should be presented!");

        page.waitUntilThereIsNoNotify();

        page.setShowShadow(false);
        page.showNotification();

        assertFalse(isElementPresent(page.getNotify().getMessageAtIndex(0).getShadowElement()), "The shadow should not be presented!");
    }

    @Test
    public void testShowCloseButton() {
        page.waitUntilThereIsNoNotify();
        page.setShowCloseButtion(true);
        page.showNotification();

        NotifyMessage message = page.getNotify().getMessageAtIndex(0);

        actions.moveToElement(message.getMessageSummaryElement()).build().perform();
        assertTrue(message.getCloseIconElement().isDisplayed(), "The close button should be visible!");
        page.waitUntilThereIsNoNotify();

        page.setShowCloseButtion(false);

        page.showNotification();

        message = page.getNotify().getMessageAtIndex(0);

        actions.moveToElement(message.getMessageSummaryElement());
        assertFalse(message.getCloseIconElement().isDisplayed(), "The close button should not be visible!");
    }

    /* *****************************************************************************
     * Help methods ************************************************************** ***************
     */

    private void checkNonBlockingOpacity(final String opacity) {
        loadPage();
        page.setNonBlocking(true);
        page.setNonBlockingOpacity(opacity);
        page.waitUntilThereIsNoNotify();
        page.showNotification();

        final NotifyMessage message = page.getNotify().getMessageAtIndex(0);
        actions.moveToElement(message.getMessageSummaryElement()).build().perform();

        Graphene.waitAjax()
                .withMessage("The notify should has opacity " + opacity + ".")
                .pollingEvery(50, TimeUnit.MILLISECONDS)
                .until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        double actualOpacity = Double.valueOf(message.getRoot().getCssValue("opacity"));
                        boolean succcess = Math.abs(Double.valueOf(opacity) - actualOpacity) <= 0.2;
                        if (!succcess) {
                            actions.moveToElement(message.getMessageSummaryElement()).build().perform();
                        }
                        return succcess;
                    }
                });
    }

    private void checkStayTime(long stayTime) {
        page.setStayTime(stayTime);
        page.waitUntilThereIsNoNotify();
        page.showNotification();
        long timeWhenNotifyIsRendered = System.currentTimeMillis();
        page.waitUntilThereIsNoNotify();
        long timeWhenNotifyDisappeared = System.currentTimeMillis();
        long delta = timeWhenNotifyDisappeared - timeWhenNotifyIsRendered;


        // the time should be measured when the notify started to disappear,
        // however
        // it is measured from the time it fully disappears, therefore there is
        // added delay

        long moreThan = stayTime;
        long lessThan = stayTime + NotifyAttributesPage.NOTIFY_DISAPPEAR_DELAY;

        assertTrue((delta > moreThan) && (delta < lessThan), "The notify message should stay on the screen more than: "
            + moreThan + " and less than: " + lessThan + " milisec, but was: " + delta + " milisec");
    }

    public static void waitForSomeTime(long howLongInMilis) {

        long timeout = System.currentTimeMillis() + howLongInMilis;
        long currentTime = System.currentTimeMillis();

        while (timeout > currentTime) {

            currentTime = System.currentTimeMillis();
        }
    }

}
