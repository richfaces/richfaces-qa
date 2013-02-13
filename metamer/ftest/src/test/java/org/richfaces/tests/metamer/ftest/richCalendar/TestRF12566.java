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
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.log.Log.LogEntryLevel;
import org.richfaces.tests.page.fragments.impl.log.LogEntry;
import org.richfaces.tests.page.fragments.impl.log.RichFacesLog;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestRF12566 extends AbstractWebDriverTest {

    @Page
    private RF12566Page page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/RF-12566.xhtml");
    }

    @Test(groups = "4.Future")
    public void testClickOnTheNextMonthAndJSErrorIsThrown() {
        page.getShowCalendarButton().click();

        Graphene.waitAjax().until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return page.getNextMonthScroller().isDisplayed();
            }
        });

        guardXhr(page.getNextMonthScroller()).click();

        List<LogEntry> errorEntries = page.getLog().getLogEntries(LogEntryLevel.ERROR);
        assertEquals(errorEntries.size(), 0, "There should be no errors on the log screen!");
    }

    public static class RF12566Page extends MetamerPage {

        @FindBy(id = "richfaces.log")
        private RichFacesLog log;

        @FindBy(css = "img[id$='cal1PopupButton']")
        private WebElement showCalendarButton;

        @FindBy(css = "td[class='rf-cal-tl'] > div")
        private WebElement nextMonthScroller;

        public RichFacesLog getLog() {
            return log;
        }

        public void setLog(RichFacesLog log) {
            this.log = log;
        }

        public WebElement getShowCalendarButton() {
            return showCalendarButton;
        }

        public void setShowCalendarButton(WebElement showCalendarButton) {
            this.showCalendarButton = showCalendarButton;
        }

        public WebElement getNextMonthScroller() {
            return nextMonthScroller;
        }

        public void setNextMonthScroller(WebElement nextMonthScroller) {
            this.nextMonthScroller = nextMonthScroller;
        }
    }
}
