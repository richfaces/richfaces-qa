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
package org.richfaces.tests.metamer.ftest.richCalendar;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.fragment.log.Log.LogEntryLevel.ERROR;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.log.Log.LogEntryLevel;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestRF12566 extends AbstractWebDriverTest {

    @Page
    private RF12566Page page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCalendar/rf-12566.xhtml");
    }

    @Test(groups = "Future")
    public void testClickOnTheNextMonthAndJSErrorIsThrown() {
        page.getLog().changeLevel(ERROR);
        page.getShowCalendarButton().click();
        Graphene.waitAjax().until().element(page.getNextMonthScroller()).is().visible();
        guardAjax(page.getNextMonthScroller()).click();
        assertTrue(page.getLog().getLogEntries().isEmpty(), "There should be no errors on the log screen!");
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
