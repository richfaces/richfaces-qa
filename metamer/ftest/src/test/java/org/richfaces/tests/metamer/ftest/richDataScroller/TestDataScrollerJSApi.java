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
package org.richfaces.tests.metamer.ftest.richDataScroller;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.richDataScroller.SimplePage.ScrollerPosition;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class TestDataScrollerJSApi extends AbstractWebDriverTest {

    private final Attributes<DataScrollerAttributes> attributes = getAttributes("attributes");

    @Page
    private SimplePage page;

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    private ScrollerPosition scroller;

    @Override
    public String getComponentTestPagePath() {
        return "richDataScroller/simple.xhtml";
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-14275")
    public void testFastRewindFastForward() {
        attributes.set(DataScrollerAttributes.fastStep, 2);

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonFastForward(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 3,
            "After clicking on the step fast forward button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonFastForward(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 5,
            "After clicking on the step fast forward button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonFastRewind(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 3,
            "After clicking on the step fast rewind button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonFastRewind(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step fast rewind button (JS API), the current page doesn't match.");
    }

    @Test
    public void testFirstLastNextPrevious() {
        getMetamerPage().performJSClickOnButton(page.getJsApiButtonLast(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 6,
            "After clicking on the step last button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonFirst(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step first button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonNext(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 2,
            "After clicking on the step next button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonPrev(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step previous button (JS API), the current page doesn't match.");
    }

    @Test
    public void testSwitchToFastRewindFastForward() {
        attributes.set(DataScrollerAttributes.fastStep, 2);

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonSwitchToFastForward(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 3,
            "After clicking on the step fast forward button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonSwitchToFastForward(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 5,
            "After clicking on the step fast forward button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonSwitchToFastRewind(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 3,
            "After clicking on the step fast rewind button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonSwitchToFastRewind(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step fast rewind button (JS API), the current page doesn't match.");
    }

    /**
     * Test buttons with scroller JS API binding, using switchToPage operation
     */
    @Test
    public void testSwitchToPage() {
        getMetamerPage().performJSClickOnButton(page.getJsApiButtonSwitchToLast(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 6,
            "After clicking on the step last button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonSwitchToFirst(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step first button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonSwitchToNext(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 2,
            "After clicking on the step next button (JS API), the current page doesn't match.");

        getMetamerPage().performJSClickOnButton(page.getJsApiButtonSwitchToPrev(scroller), WaitRequestType.XHR);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step previous button (JS API), the current page doesn't match.");
    }
}
