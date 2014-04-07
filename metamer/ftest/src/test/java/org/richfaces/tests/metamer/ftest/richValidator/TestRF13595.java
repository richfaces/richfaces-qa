/*******************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc. and individual contributors by the @authors tag.
 * See the copyright.txt in the distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richValidator;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.joda.time.DateTime;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.calendar.RichFacesAdvancedPopupCalendar;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * This is a reproducer test for RF-13595
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public class TestRF13595 extends AbstractWebDriverTest {

    @FindByJQuery("input[id$=setInputVisibleButton]")
    private WebElement toggleButton;

    @FindByJQuery("div[id$=inputField]")
    private RichFacesAdvancedPopupCalendar calendar;

    @FindByJQuery("div[id$=inputField]")
    private WebElement calendarAsWebElement;

    @FindByJQuery("span[class$=rf-msg-det]")
    private WebElement errorMsg;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richValidator/rf-13595.xhtml");
    }

    @Test(groups = "Future")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-13595" })
    public void testToggledInputValidator() {
        // toggle calendar and wait until it is present
        toggleButton.click();
        waitGui(driver).until().element(calendarAsWebElement).is().present();

        // set today date
        calendar.openPopup().getDayPicker().selectDayInMonth(new DateTime());

        // blur
        fireEvent(calendarAsWebElement, Event.BLUR);

        // expect error msg
        waitGui(driver).until().element(errorMsg).is().present();
    }
}
