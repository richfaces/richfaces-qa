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

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.MetamerAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.page.fragments.impl.status.RichFacesStatus;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractStatusTest extends AbstractWebDriverTest {

    @FindBy(css = "input[id$=button1]")
    private WebElement button1;
    @FindBy(css = "input[id$=button2]")
    private WebElement button2;
    @FindBy(css = "input[id$=button3]")
    private WebElement buttonError;
    @FindBy(css = "span[id$=status]")
    private RichFacesStatus status;
    @FindBy(css = "span[id$=a4jStatus]")
    private RichFacesStatus defaultStatus;

    private Attributes<StatusAttributes> statusAttributes = getAttributes();

    protected static final long DELAY = 2000;
    protected static final long WAIT_TIME_STATUS_CHANGES = DELAY + 1000;

    public WebElement getButton1() {
        return button1;
    }

    public WebElement getButton2() {
        return button2;
    }

    public WebElement getButtonError() {
        return buttonError;
    }

    public RichFacesStatus getMetamerStatus() {
        return defaultStatus;
    }

    public RichFacesStatus getStatus() {
        return status;
    }

    public Attributes<StatusAttributes> getStatusAttributes() {
        return statusAttributes;
    }

    @BeforeMethod(alwaysRun = true)
    public void setupDelay() {
        getMetamerAttributes().set(MetamerAttributes.metamerResponseDelay, DELAY);
    }

    protected void checkStatus(WebElement button, String startStatusText, String stopStatusText) {
        button.click();
        getStatus().advanced().waitUntilStatusTextChanges(startStatusText)
            .withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
        getStatus().advanced().waitUntilStatusTextChanges(stopStatusText)
            .withTimeout(WAIT_TIME_STATUS_CHANGES, TimeUnit.MILLISECONDS).perform();
    }
}
