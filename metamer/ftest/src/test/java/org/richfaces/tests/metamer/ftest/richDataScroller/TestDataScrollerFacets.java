/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richDataScroller;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.page.fragments.impl.dataScroller.DataScroller.DataScrollerSwitchButton;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 *
 */
public class TestDataScrollerFacets extends AbstractWebDriverTest {

    private static final String TEST_STRING_ENABLED = "TestFacetEnabled";
    private static final String TEST_STRING_DISABLED = "TestFacetDisabled";

    @Page
    private FacetPage page;

    @FindBy(name = "beforeForm:facetFirst")
    private WebElement facetFirst;

    @FindBy(name = "beforeForm:facetFastRewind")
    private WebElement facetFastRewind;

    @FindBy(name = "beforeForm:facetPrevious")
    private WebElement facetPrevious;

    @FindBy(name = "beforeForm:facetNext")
    private WebElement facetNext;

    @FindBy(name = "beforeForm:facetFastForward")
    private WebElement facetFastForward;

    @FindBy(name = "beforeForm:facetLast")
    private WebElement facetLast;

    @FindBy(name = "beforeForm:facetFirst_disabled")
    private WebElement facetFirstDisabled;

    @FindBy(name = "beforeForm:facetFastRewind_disabled")
    private WebElement facetFastRewindDisabled;

    @FindBy(name = "beforeForm:facetPrevious_disabled")
    private WebElement facetPreviousDisabled;

    @FindBy(name = "beforeForm:facetNext_disabled")
    private WebElement facetNextDisabled;

    @FindBy(name = "beforeForm:facetFastForward_disabled")
    private WebElement facetFastForwardDisabled;

    @FindBy(name = "beforeForm:facetLast_disabled")
    private WebElement facetLastDisabled;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataScroller/facets.xhtml");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13081")
    public void testFirstFacetDisabled() {
        // both facets are disabled
        assertTrue(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FIRST));
        assertTrue(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FIRST));

        // change title of first facet when disabled
        setStringToFacet(facetFirstDisabled, TEST_STRING_DISABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.FIRST).getText(), TEST_STRING_DISABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.FIRST).getText(),
            TEST_STRING_DISABLED);
    }

    @Test
    public void testFirstFacetEnabled() {
        // change to different page
        page.getTopScroller().switchTo(DataScrollerSwitchButton.LAST);
        assertFalse(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FIRST));
        assertFalse(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FIRST));

        // change text of first facet when enabled
        setStringToFacet(facetFirst, TEST_STRING_ENABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.FIRST).getText(), TEST_STRING_ENABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.FIRST).getText(),
            TEST_STRING_ENABLED);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13081")
    public void testFastRewindFacetDisabled() {
        // both facets should be disabled
        assertTrue(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FAST_REWIND));
        assertTrue(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FAST_REWIND));

        // change title of disabled facet
        setStringToFacet(facetFastRewindDisabled, TEST_STRING_DISABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.FAST_REWIND).getText(),
            TEST_STRING_DISABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.FAST_REWIND).getText(),
            TEST_STRING_DISABLED);
    }

    @Test
    public void testFastRewindFacetEnabled() {
        // switch to another page
        page.getTopScroller().switchTo(DataScrollerSwitchButton.LAST);
        assertFalse(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FAST_REWIND));
        assertFalse(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FAST_REWIND));

        // change text of enabled facet
        setStringToFacet(facetFastRewind, TEST_STRING_ENABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.FAST_REWIND).getText(),
            TEST_STRING_ENABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.FAST_REWIND).getText(),
            TEST_STRING_ENABLED);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13081")
    public void testPreviousFacetDisabled() {
        // both facets should be disabled initially
        assertTrue(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.PREVIOUS));
        assertTrue(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.PREVIOUS));

        // change text of disabled
        setStringToFacet(facetPreviousDisabled, TEST_STRING_DISABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.PREVIOUS).getText(),
            TEST_STRING_DISABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.PREVIOUS).getText(),
            TEST_STRING_DISABLED);
    }

    @Test
    public void testPreviousFacetEnabled() {
        // switch to another page
        page.getTopScroller().switchTo(DataScrollerSwitchButton.LAST);
        assertFalse(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.PREVIOUS));
        assertFalse(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.PREVIOUS));

        // change text of enabled facet
        setStringToFacet(facetPrevious, TEST_STRING_ENABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.PREVIOUS).getText(),
            TEST_STRING_ENABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.PREVIOUS).getText(),
            TEST_STRING_ENABLED);
    }

    @Test
    public void testNextFacetEnabled() {
        // both facets should be enabled initially
        assertFalse(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.NEXT));
        assertFalse(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.NEXT));

        // change text when enabled
        setStringToFacet(facetNext, TEST_STRING_ENABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.NEXT).getText(), TEST_STRING_ENABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.NEXT).getText(),
            TEST_STRING_ENABLED);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13081")
    public void testNextFacetDisabled() {
        // switch to another page
        page.getTopScroller().switchTo(DataScrollerSwitchButton.LAST);
        assertTrue(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.NEXT));
        assertTrue(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.NEXT));

        // change text when disabled
        setStringToFacet(facetNextDisabled, TEST_STRING_DISABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.NEXT).getText(), TEST_STRING_DISABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.NEXT).getText(),
            TEST_STRING_DISABLED);
    }

    @Test
    public void testFastForwardFacetEnabled() {
        // both facets should be enabled initially
        assertFalse(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FAST_FORWARD));
        assertFalse(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FAST_FORWARD));

        // change text when enabled
        setStringToFacet(facetFastForward, TEST_STRING_ENABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.FAST_FORWARD).getText(),
            TEST_STRING_ENABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.FAST_FORWARD).getText(),
            TEST_STRING_ENABLED);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13081")
    public void testfacetForwardFacetDisabled() {
        // switch to another page
        page.getTopScroller().switchTo(DataScrollerSwitchButton.LAST);
        assertTrue(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FAST_FORWARD));
        assertTrue(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.FAST_FORWARD));

        // change text when disabled
        setStringToFacet(facetFastForwardDisabled, TEST_STRING_DISABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.FAST_FORWARD).getText(),
            TEST_STRING_DISABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.FAST_FORWARD).getText(),
            TEST_STRING_DISABLED);
    }

    @Test
    public void testLastFacetEnabled() {
        // both facets should be enabled initially
        assertFalse(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.LAST));
        assertFalse(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.LAST));

        // change text when enabled
        setStringToFacet(facetLast, TEST_STRING_ENABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.LAST).getText(), TEST_STRING_ENABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.LAST).getText(),
            TEST_STRING_ENABLED);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13081")
    public void testLastFacetDisabled() {
        // switch to another page
        page.getTopScroller().switchTo(DataScrollerSwitchButton.LAST);
        assertTrue(page.getTopScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.LAST));
        assertTrue(page.getBottomScroller().advanced().isButtonDisabled(DataScrollerSwitchButton.LAST));

        // change text when disabled
        setStringToFacet(facetLastDisabled, TEST_STRING_DISABLED);
        assertEquals(page.getTopScroller().advanced().getButton(DataScrollerSwitchButton.LAST).getText(), TEST_STRING_DISABLED);
        assertEquals(page.getBottomScroller().advanced().getButton(DataScrollerSwitchButton.LAST).getText(),
            TEST_STRING_DISABLED);
    }

    @Test
    public void testTopAndBottomScrollersCooperation() {
        // check if both scrollers react together on table page change
        page.getTopScroller().switchTo(2);
        assertEquals(page.getTopScroller().getActivePageNumber(), page.getBottomScroller().getActivePageNumber());
        assertEquals(page.getBottomScroller().getActivePageNumber(), 2);
        assertEquals(page.getTopScroller().getActivePageNumber(), 2);
        page.getBottomScroller().switchTo(4);
        assertEquals(page.getTopScroller().getActivePageNumber(), page.getBottomScroller().getActivePageNumber());
        assertEquals(page.getBottomScroller().getActivePageNumber(), 4);
        assertEquals(page.getTopScroller().getActivePageNumber(), 4);
    }

    private void setStringToFacet(WebElement facet, String text) {
        facet.clear();
        facet.sendKeys(text);
        facet.submit();
    }
}
