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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.status.RichFacesStatus;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestReferencedUsage extends AbstractStatusTest {

    @FindBy(css = "span[id$=status1]")
    private RichFacesStatus status1;
    @FindBy(css = "span[id$=status2]")
    private RichFacesStatus status2;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/referencedUsage.xhtml");
    }

    @Test
    public void testClickBothButtonsImmediately() {
        getButton1().click();
        getButton2().click();

        // first request started
        status1.advanced().waitUntilStatusTextChanges("START").perform();
        status2.advanced().waitUntilStatusTextChanges("STOP").perform();

        // first request completed, second request started
        status1.advanced().waitUntilStatusTextChanges("STOP").withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        status2.advanced().waitUntilStatusTextChanges("START").perform();

        // second request completed
        status2.advanced().waitUntilStatusTextChanges("STOP").withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        status1.advanced().waitUntilStatusTextChanges("STOP").perform();
    }

    @Test
    public void testClickBothButtonsInSequence() {
        getButton1().click();

        // first request started
        status1.advanced().waitUntilStatusTextChanges("START").perform();
        status2.advanced().waitUntilStatusTextChanges("STOP").perform();

        // first request completed
        status1.advanced().waitUntilStatusTextChanges("STOP").withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        status2.advanced().waitUntilStatusTextChanges("STOP").perform();

        getButton2().click();

        // second request started
        status1.advanced().waitUntilStatusTextChanges("STOP").perform();
        status2.advanced().waitUntilStatusTextChanges("START").perform();

        // second request completed
        status2.advanced().waitUntilStatusTextChanges("STOP").withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        status1.advanced().waitUntilStatusTextChanges("STOP").perform();
    }

    @Test
    public void testClickFirstButtonThenSecondButtonThenAgainFirstButtonImmediately() {
        getButton1().click();
        getButton2().click();
        getButton1().click();

        // first request started
        status1.advanced().waitUntilStatusTextChanges("START").perform();
        status2.advanced().waitUntilStatusTextChanges("STOP").perform();

        // first request completed, second request started
        status1.advanced().waitUntilStatusTextChanges("STOP").withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        status2.advanced().waitUntilStatusTextChanges("START").perform();

        // second request completed, third request started
        status2.advanced().waitUntilStatusTextChanges("STOP").withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        status1.advanced().waitUntilStatusTextChanges("START").perform();

        //third request completed
        status1.advanced().waitUntilStatusTextChanges("STOP").withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        status2.advanced().waitUntilStatusTextChanges("STOP").perform();
    }

    @Test
    public void testDoubleClick() {
        getButton1().click();
        getButton1().click();

        // first request started
        status1.advanced().waitUntilStatusTextChanges("START").perform();
        status2.advanced().waitUntilStatusTextChanges("STOP").perform();

        // check that status is still at 'START' after first request
        waiting(WAIT_TIME_STATUS_CHANGES);
        status1.advanced().waitUntilStatusTextChanges("START").perform();
        status2.advanced().waitUntilStatusTextChanges("STOP").perform();

        // both requests completed
        status1.advanced().waitUntilStatusTextChanges("STOP").withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        status2.advanced().waitUntilStatusTextChanges("STOP").perform();
    }
}
