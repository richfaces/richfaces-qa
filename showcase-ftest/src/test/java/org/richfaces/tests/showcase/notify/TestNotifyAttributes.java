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
package org.richfaces.tests.showcase.notify;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestNotifyAttributes extends AbstractNotifyTest {

    /* **********************************************************
     * Constants**********************************************************
     */

    // it takes approximately 1100 milisec to fully disappear notify message
    private final int NOTIFY_DISAPPEAR_DELAY = 2000;
    private final int TIMEOUT = 5000;

    /* **********************************************************************************************************************
     * Locators ******************************************************************
     * ****************************************************
     */

    private JQueryLocator inputForStayTime = jq(".rf-insl-inp:eq(0)");
    private JQueryLocator stickyCheckBox = jq("input[type=checkbox]:eq(0)");
    private JQueryLocator nonBlockingCheckBox = jq("input[type=checkbox]:eq(1)");
    private JQueryLocator showShadowCheckBox = jq("input[type=checkbox]:eq(2)");
    private JQueryLocator showCloseButtonCheckBox = jq("input[type=checkbox]:eq(3)");
    private JQueryLocator nonBlockingOpacityInput = jq(".rf-insl-inp:eq(1)");

    private JQueryLocator showNotification = jq("input[type=submit]");

    private JQueryLocator closeButton = jq(".rf-ntf-cls");

    private JQueryLocator shadow = jq(".rf-ntf-shdw");

    /* ***********************************************************************************************************************
     * Tests *********************************************************************
     * **************************************************
     */

    @Test
    public void testStayTime() {

        checkStayTime(3500);

        checkStayTime(500);

        checkStayTime(1500);
    }

    @Test
    public void testSticky() {

        waitUntilNotifyDissappeares(TIMEOUT);

        selenium.check(stickyCheckBox);

        guardXhr(selenium).click(showNotification);

        boolean ok = false;

        try {
            waitUntilNotifyDissappeares(TIMEOUT);
        } catch (RuntimeException ex) {

            ok = true;
        }

        assertTrue(ok, "The message should not dissapear");

        selenium.click(closeButton);

        waitUntilNotifyDissappeares(TIMEOUT);
    }

    @Test
    public void testNonBlockingOpacity() {

        checkNonBlockingOpacity("0.5");

        checkNonBlockingOpacity("0");

        checkNonBlockingOpacity("1");
    }

    @Test
    public void testShowShadow() {

        waitUntilNotifyDissappeares(TIMEOUT);

        selenium.check(showShadowCheckBox);

        guardXhr(selenium).click(showNotification);

        assertTrue(selenium.isElementPresent(shadow), "The shadow should be presented!");

        waitUntilNotifyDissappeares(TIMEOUT);

        selenium.uncheck(showShadowCheckBox);

        guardXhr(selenium).click(showNotification);

        assertFalse(selenium.isElementPresent(shadow), "The shadow should not be presented!");
    }

    @Test
    public void testShowCloseButton() {

        waitUntilNotifyDissappeares(TIMEOUT);

        selenium.check(showCloseButtonCheckBox);

        guardXhr(selenium).click(showNotification);

        selenium.mouseOver(closeButton);

        assertTrue(selenium.isVisible(closeButton), "The close button should be visible!");

        waitUntilNotifyDissappeares(TIMEOUT);

        selenium.uncheck(showCloseButtonCheckBox);

        guardXhr(selenium).click(showNotification);

        selenium.mouseOver(closeButton);

        assertFalse(selenium.isVisible(closeButton), "The close button should not be visible!");
    }

    /* *****************************************************************************
     * Help methods ************************************************************** ***************
     */

    private void checkNonBlockingOpacity(String opacity) {

        waitUntilNotifyDissappeares(TIMEOUT);

        selenium.check(nonBlockingCheckBox);
        guardXhr(selenium).fireEvent(nonBlockingCheckBox, Event.CLICK);

        selenium.type(nonBlockingOpacityInput, opacity);

        guardXhr(selenium).click(showNotification);

        waitUntilNotifyAppears(TIMEOUT);

        selenium.mouseOver(notify);

        waitForSomeTime(1000);

        String style = selenium.getAttribute(notify.getAttribute(Attribute.STYLE));
        String actualOpacity = style.split("opacity:")[1];

        assertTrue(actualOpacity.trim().startsWith(opacity), "The notify should has opacity " + opacity);
    }

    private void checkStayTime(long stayTime) {

        selenium.type(inputForStayTime, String.valueOf(stayTime));

        waitUntilNotifyDissappeares(TIMEOUT);

        guardXhr(selenium).click(showNotification);

        waitUntilNotifyAppears(TIMEOUT);

        long timeWhenNotifyIsRendered = System.currentTimeMillis();

        waitUntilNotifyDissappeares(TIMEOUT);

        long timeWhenNotifyDisappeared = System.currentTimeMillis();

        long delta = timeWhenNotifyDisappeared - timeWhenNotifyIsRendered;

        // the time should be measured when the notify started to disappear,
        // however
        // it is measured from the time it fully disappears, therefore there is
        // added delay

        long moreThan = stayTime;
        long lessThan = stayTime + NOTIFY_DISAPPEAR_DELAY;

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
