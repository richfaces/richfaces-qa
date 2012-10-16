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
package org.richfaces.tests.metamer.ftest.richDataScroller;

import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.Arrays;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.attributes.Attributes;
import org.richfaces.tests.metamer.ftest.model.DataScroller;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;
import org.testng.annotations.Test;


/**
 * Test the functionality of switching pages using DataScroller bound to DataTable.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@Use(field = "scroller", value = { "scrollerOutsideTable", "scrollerInsideTable" })
public class TestDataScrollerAttributes extends AbstractGrapheneTest {

    private final Attributes<DataScrollerAttributes> attributes = new Attributes<DataScrollerAttributes>(
        pjq("table[id$='attributes']"));
    private final Attributes<DataTableAttributes> tableAttributes = new Attributes<DataTableAttributes>(
        pjq("table[id$='tableAttributes']"));

    @Inject
    private DataScroller scroller;

    private DataScroller scrollerOutsideTable = PaginationTester.DATA_SCROLLER_OUTSIDE_TABLE;
    private DataScroller scrollerInsideTable = PaginationTester.DATA_SCROLLER_IN_TABLE_FOOTER;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataScroller/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Data Scroller", "Simple");
    }

    @Test
    public void testBoundaryControls() {
        // init - show
        assertTrue(getScroller().isFirstPageButtonPresent(), "The first button should be present.");
        assertTrue(getScroller().isLastPageButtonPresent(), "The last button should be present.");
        // hide
        getAttributes().set(DataScrollerAttributes.boundaryControls, "hide");
        assertFalse(getScroller().isFirstPageButtonPresent(), "The first button shouldn't be present.");
        assertFalse(getScroller().isLastPageButtonPresent(), "The last button shouldn't be present.");
    }

    @Test
    public void testData() {
        // attributes
        getAttributes().set(DataScrollerAttributes.data, "RichFaces");
        getAttributes().set(DataScrollerAttributes.oncomplete, "data = event.data");
        // action
        retrieveRequestTime.initializeValue();
        getScroller().clickFastForward();
        waitGui.waitForChange(retrieveRequestTime);
        // check
        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces", "Data sent with ajax request");
    }

    @Test
    public void testEvents() throws InterruptedException {
        // set event attributes
        getAttributes().set(DataScrollerAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        getAttributes().set(DataScrollerAttributes.onbegin, "metamerEvents += \"begin \"");
        getAttributes().set(DataScrollerAttributes.oncomplete, "metamerEvents += \"complete \"");
        // reset events
        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        // action
        retrieveRequestTime.initializeValue();
        getScroller().clickFastForward();
        waitGui.waitForChange(retrieveRequestTime);
        // check events
        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");
        assertEquals(events.length, 3, "3 events should be fired, found events are " + Arrays.toString(events) + ".");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work, found events are " + Arrays.toString(events)
            + ".");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work, found events are "
            + Arrays.toString(events) + ".");
        assertEquals(events[2], "complete",
            "Attribute oncomplete doesn't work, found events are " + Arrays.toString(events) + ".");
    }

    @Test
    public void testExecute() {
        // attributes
        getAttributes().set(DataScrollerAttributes.execute, "executeChecker");
        // action
        retrieveRequestTime.initializeValue();
        getScroller().clickFastForward();
        waitGui.waitForChange(retrieveRequestTime);
        // check
        JQueryLocator logItems = jq("ul.phases-list li:eq({0})");
        for (int i = 0; i < 6; i++) {
            if ("* executeChecker".equals(selenium.getText(logItems.format(i)))) {
                return;
            }
        }
        fail("Attribute execute does not work");
    }

    @Test
    public void testFastControls() {
        // init - show
        assertTrue(getScroller().isFastForwardButtonPresent(), "The fast forward button should be present.");
        assertTrue(getScroller().isFastRewindButtonPresent(), "The fast rewind button should be present.");
        // hide
        getAttributes().set(DataScrollerAttributes.fastControls, "hide");
        assertFalse(getScroller().isFastForwardButtonPresent(), "The fast forward button shouldn't be present.");
        assertFalse(getScroller().isFastRewindButtonPresent(), "The fast rewind button shouldn't be present.");
    }

    @Test
    public void testFastStep() {
        getAttributes().set(DataScrollerAttributes.fastStep, 3);
        retrieveRequestTime.initializeValue();
        getScroller().clickFastForward();
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 4,
            "After clicking on the fast forward button, the current page doesn't match.");
        retrieveRequestTime.initializeValue();
        getScroller().clickFastRewind();
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 1,
            "After clicking on the fast rewind button, the current page doesn't match.");
    }

    @Test
    public void testLastPageMode() {
        retrieveRequestTime.initializeValue();
        getScroller().clickLastPageButton();
        waitGui.waitForChange(retrieveRequestTime);
        // init - short
        assertEquals(getNumberOfRows(), 5,
            "Attribute lastPageMode doesn't work. The number of rows doesn't match, when the value is set to <short>");
        // full
        getAttributes().set(DataScrollerAttributes.lastPageMode, "full");
        assertEquals(getNumberOfRows(), 9,
            "Attribute lastPageMode doesn't work. The number of rows doesn't match, when the value is set to <full>");
    }

    @Test
    public void testLimitRender() {
        // false
        getAttributes().set(DataScrollerAttributes.limitRender, false);
        retrieveRequestTime.initializeValue();
        getScroller().clickFastForward();
        waitGui.failWith(
            "The panel hasn't been rerendered despite of the fact the attribute 'limitRender' is set to <false>.")
            .waitForChange(retrieveRequestTime);
        // true
        getAttributes().set(DataScrollerAttributes.limitRender, false);
        String timeBefore = retrieveRequestTime.getValue();
        getScroller().clickFastRewind();
        waitModel.until(new SeleniumCondition() {
            @Override
            public boolean isTrue() {
                return getScroller().getCurrentPage() == 1;
            }
        });
        String timeAfter = retrieveRequestTime.getValue();
        assertEquals(timeAfter, timeBefore,
            "The panel hasn been rerendered despite of the fact the attribute 'limitRender' is set to <true>.");

    }

    @Test
    public void testMaxPages() {
        // init - 10
        assertEquals(getScroller().getCountOfVisiblePages(), 6 /* it means - all pages */,
            "The number of visible pages doesn't match.");
        // smaller number
        getAttributes().set(DataScrollerAttributes.maxPages, 3);
        assertEquals(getScroller().getCountOfVisiblePages(), 3, "The number of visible pages doesn't match.");
    }

    @Test
    public void testPage() {
        getAttributes().set(DataScrollerAttributes.page, 4);
        assertEquals(getScroller().getCurrentPage(), 4, "The number of current page doesn't match.");
    }

    @Test
    public void testRendered() {
        getAttributes().set(DataScrollerAttributes.rendered, false);
        assertFalse(getScroller().isPresent(), "The data scroller shouldn't be present.");
    }

    @Test
    public void testRenderIfSinglePage() {
        // prepare
        getTableAttributes().set(DataTableAttributes.rows, 200);
        // init - true
        assertTrue(getScroller().isPresent(), "The attribute 'renderIfSinglePage' doesn't work.");
        // false
        getAttributes().set(DataScrollerAttributes.renderIfSinglePage, false);
        assertFalse(getScroller().isPresent(), "The attribute 'renderIfSinglePage' doesn't work.");
    }

    @Test
    public void testStatus() {
        // prepare
        getAttributes().set(DataScrollerAttributes.status, "statusChecker");
        String statusBefore = selenium.getText(statusChecker);
        // action
        retrieveRequestTime.initializeValue();
        getScroller().clickFastForward();
        waitGui.waitForChange(retrieveRequestTime);
        // check
        String statusAfter = selenium.getText(statusChecker);
assertFalse(statusAfter.equals(statusBefore), "The status attribute doesn't work.");
    }

    @Test
    public void testStepControls() {
        // init - show
        assertTrue(getScroller().isNextButtonPresent(), "The next button should be present.");
        assertTrue(getScroller().isPreviousButtonPresent(), "The previous button should be present.");
        // hide
        getAttributes().set(DataScrollerAttributes.stepControls, "hide");
        assertFalse(getScroller().isNextButtonPresent(), "The next button shouldn't be present.");
        assertFalse(getScroller().isPreviousButtonPresent(), "The previous button shouldn't be present.");
    }

    /**
     * Test simple step forward and step back buttons on dataScroller
     */
    @Test
    public void testStep() {
        retrieveRequestTime.initializeValue();
        // 2 times move forward by basic step
        getScroller().clickStepForward();
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 2,
            "After clicking on the step next button, the current page doesn't match.");
        getScroller().clickStepForward();
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 3,
            "After clicking on the step next button, the current page doesn't match.");
        // then move backward by basic step back
        retrieveRequestTime.initializeValue();
        getScroller().clickStepPrevious();
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 2,
            "After clicking on the step previous button, the current page doesn't match.");
        getScroller().clickStepPrevious();
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 1,
            "After clicking on the step previous button, the current page doesn't match.");
    }

    /**
     * Test buttons with scroller JS API binding
     */
    @Test
    public void testJsApi() {
        verifyJsApi(1);
        verifyJsApi(2);
    }

    /**
     * Test buttons with scroller JS API binding, using switchToPage operation
     */
    @Test
    public void testJsApiStp() {
        verifyJsApiStp(1);
        verifyJsApiStp(2);
    }

    @Test
    public void testStyle() {
        super.testStyle(getScroller());
    }

    @Test
    public void testStyleClass() {
        super.testStyleClass(getScroller());
    }

    @Test
    public void testTitle() {
        super.testTitle(getScroller());
    }

    private Attributes<DataScrollerAttributes> getAttributes() {
        return attributes;
    }

    private int getNumberOfRows() {
        return selenium.getCount(pjq("table[id$=richDataTable].rf-dt tbody tr.rf-dt-r"));
    }

    private DataScroller getScroller() {
        return scroller;
    }

    private Attributes<DataTableAttributes> getTableAttributes() {
        return tableAttributes;
    }

    private void verifyJsApi(int scrollerNo) {
        retrieveRequestTime.initializeValue();
        getScroller().clickJsApiLast(scrollerNo);
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 6,
            "After clicking on the step last button (JS API), the current page doesn't match.");

        getScroller().clickJsApiFirst(scrollerNo);
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 1,
            "After clicking on the step first button (JS API), the current page doesn't match.");

        getScroller().clickJsApiNext(scrollerNo);
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 2,
            "After clicking on the step next button (JS API), the current page doesn't match.");

        getScroller().clickJsApiNext(scrollerNo);
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 3,
            "After clicking on the step next button (JS API), the current page doesn't match.");
    }

    private void verifyJsApiStp(int scrollerNo) {
        retrieveRequestTime.initializeValue();
        getScroller().clickJsApiStpLast(scrollerNo);
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 6,
            "After clicking on the step last button (JS API), the current page doesn't match.");

        getScroller().clickJsApiStpFirst(scrollerNo);
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 1,
            "After clicking on the step first button (JS API), the current page doesn't match.");

        getScroller().clickJsApiStpNext(scrollerNo);
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 2,
            "After clicking on the step next button (JS API), the current page doesn't match.");

        getScroller().clickJsApiStpNext(scrollerNo);
        waitGui.waitForChange(retrieveRequestTime);
        assertEquals(getScroller().getCurrentPage(), 3,
            "After clicking on the step next button (JS API), the current page doesn't match.");
    }
}
