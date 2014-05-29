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
package org.richfaces.tests.metamer.ftest.richDataScroller;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.Arrays;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.richDataScroller.SimplePage.ScrollerPosition;
import org.richfaces.tests.metamer.ftest.richDataTable.DataTableAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class TestDataScrollerAttributes extends AbstractWebDriverTest {

    private final Attributes<DataScrollerAttributes> attributes = getAttributes("attributes");
    private final Attributes<DataTableAttributes> tableAttributes = getAttributes("tableAttributes");

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    private ScrollerPosition scroller;
    @Page
    private SimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataScroller/simple.xhtml");
    }

    @Test
    public void testBoundaryControls() {
        // init - show
        assertTrue(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.FIRST),
            "The first button should be present.");
        assertTrue(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.LAST),
            "The last button should be present.");
        // hide
        attributes.set(DataScrollerAttributes.boundaryControls, "hide");
        assertFalse(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.FIRST),
            "The first button shouldn't be present.");
        assertFalse(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.LAST),
            "The last button shouldn't be present.");
    }

    @Test
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                page.getScroller(scroller).switchTo(DataScrollerSwitchButton.FAST_FORWARD);
            }
        });
    }

    @Test
    public void testEvents() throws InterruptedException {
        // set event attributes
        attributes.set(DataScrollerAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        attributes.set(DataScrollerAttributes.onbegin, "metamerEvents += \"begin \"");
        attributes.set(DataScrollerAttributes.oncomplete, "metamerEvents += \"complete \"");
        // reset events
        executor.executeScript("metamerEvents = \"\";");
        // action
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_FORWARD);
        // check events
        String[] events = executor.executeScript("return metamerEvents;").toString().split(" ");
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
        attributes.set(DataScrollerAttributes.execute, "executeChecker");
        // action
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_FORWARD);
        // check
        for (WebElement element : page.getPhasesElements()) {
            if ("* executeChecker".equals(element.getText())) {
                return;
            }
        }
        fail("Attribute execute does not work");
    }

    @Test
    public void testFastControls() {
        // init - show
        assertTrue(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.FAST_FORWARD),
            "The fast forward button should be present.");
        assertTrue(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.FAST_REWIND),
            "The fast rewind button should be present.");
        // hide
        attributes.set(DataScrollerAttributes.fastControls, "hide");
        assertFalse(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.FAST_FORWARD),
            "The fast forward button shouldn't be present.");
        assertFalse(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.FAST_REWIND),
            "The fast rewind button shouldn't be present.");
    }

    @Test
    public void testFastStep() {
        attributes.set(DataScrollerAttributes.fastStep, 3);

        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_FORWARD);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 4,
            "After clicking on the fast forward button, the current page doesn't match.");

        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_REWIND);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the fast rewind button, the current page doesn't match.");
    }

    @Test
    public void testLastPageMode() {
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR)
            .switchTo(DataScrollerSwitchButton.LAST);

        // init - short
        assertEquals(getNumberOfRows(), 5,
            "Attribute lastPageMode doesn't work. The number of rows doesn't match, when the value is set to <short>");
        // full
        attributes.set(DataScrollerAttributes.lastPageMode, "full");
        assertEquals(getNumberOfRows(), 9,
            "Attribute lastPageMode doesn't work. The number of rows doesn't match, when the value is set to <full>");
    }

    @Test
    public void testLimitRender() {
        attributes.set(DataScrollerAttributes.limitRender, false);
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_FORWARD);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 2,
            "Data scroller's active page with limitRender=false");

        attributes.set(DataScrollerAttributes.limitRender, true);
        String timeBefore = page.getRequestTimeElement().getText();
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.FAST_FORWARD);
        String timeAfter = page.getRequestTimeElement().getText();
        assertEquals(timeAfter, timeBefore,
            "The panel was rerendered despite the fact that the attribute 'limitRender' is set to <true>.");
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 3,
            "Data scroller's active page with limitRender=true");
    }

    @Test
    public void testMaxPages() {
        // initial value is 10 which means that 6 pages (i.e. all) should be displayed
        assertEquals(page.getScroller(scroller).advanced().getCountOfVisiblePages(), 6 /* it means - all pages */,
            "The number of visible pages doesn't match.");

        attributes.set(DataScrollerAttributes.maxPages, 3);
        assertEquals(page.getScroller(scroller).advanced().getCountOfVisiblePages(), 3,
            "The number of visible pages doesn't match.");
    }

    @Test
    public void testPage() {
        attributes.set(DataScrollerAttributes.page, 4);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 4, "The number of current page doesn't match.");
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        attributes.set(DataScrollerAttributes.rendered, false);
        Graphene.waitGui().until().element(page.getScroller(scroller).advanced().getRootElement()).is().not().present();
    }

    @Test
    public void testRenderIfSinglePage() {
        // prepare table to display all data at once
        tableAttributes.set(DataTableAttributes.rows, 200);

        // default value - true
        Graphene.waitGui().until("The attribute 'renderIfSinglePage' doesn't work.")
            .element(page.getScroller(scroller).advanced().getRootElement()).is().present();
        assertTrue(page.getScroller(scroller).advanced().getRootElement().isDisplayed(), "Data scroller should be displayed");

        // false
        attributes.set(DataScrollerAttributes.renderIfSinglePage, false);
        assertFalse(page.getScroller(scroller).advanced().getRootElement().isDisplayed(),
            "Data scroller should not be displayed");
    }

    @Test
    public void testStatus() {
        attributes.set(DataScrollerAttributes.status, "statusChecker");

        String statusCheckerTime = page.getStatusCheckerOutputElement().getText();
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_FORWARD);
        Graphene.waitModel().until("Page was not updated").element(page.getStatusCheckerOutputElement()).text().not()
            .equalTo(statusCheckerTime);
    }

    @Test
    public void testStepControls() {
        // default value - show
        assertTrue(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.NEXT),
            "The next button should be present.");
        assertTrue(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.PREVIOUS),
            "The previous button should be present.");

        // hide
        attributes.set(DataScrollerAttributes.stepControls, "hide");
        assertFalse(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.NEXT),
            "The next button shouldn't be present.");
        assertFalse(page.getScroller(scroller).advanced().isButtonPresent(DataScrollerSwitchButton.PREVIOUS),
            "The previous button shouldn't be present.");
    }

    @Test
    public void testStep() {
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR)
            .switchTo(DataScrollerSwitchButton.NEXT);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 2,
            "After clicking on the next button, the current page doesn't match.");

        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR)
            .switchTo(DataScrollerSwitchButton.NEXT);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 3,
            "After clicking on the next button, the current page doesn't match.");

        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.PREVIOUS);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 2,
            "After clicking on the previous button, the current page doesn't match.");

        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.PREVIOUS);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the previous button, the current page doesn't match.");
    }

    /**
     * Test buttons with scroller JS API binding
     */
    @Test
    public void testJsApi() {
        verifyJsApi();
    }

    /**
     * Test buttons with scroller JS API binding, using switchToPage operation
     */
    @Test
    public void testJsApiStp() {
        verifyJsApiStp();
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        super.testStyle(page.getScroller(scroller).advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        super.testStyleClass(page.getScroller(scroller).advanced().getRootElement());
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        super.testTitle(page.getScroller(scroller).advanced().getRootElement());
    }

    private int getNumberOfRows() {
        return driver.findElements(By.cssSelector("table[id$=richDataTable].rf-dt tbody tr.rf-dt-r")).size();
    }

    private void verifyJsApi() {
        MetamerPage.waitRequest(page.getJsApiButtonLast(scroller), WaitRequestType.XHR).click();
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 6,
            "After clicking on the step last button (JS API), the current page doesn't match.");

        MetamerPage.waitRequest(page.getJsApiButtonFirst(scroller), WaitRequestType.XHR).click();
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step first button (JS API), the current page doesn't match.");

        MetamerPage.waitRequest(page.getJsApiButtonNext(scroller), WaitRequestType.XHR).click();
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 2,
            "After clicking on the step next button (JS API), the current page doesn't match.");

        MetamerPage.waitRequest(page.getJsApiButtonPrev(scroller), WaitRequestType.XHR).click();
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step previous button (JS API), the current page doesn't match.");
    }

    private void verifyJsApiStp() {
        MetamerPage.waitRequest(page.getJsApiButtonSwitchToLast(scroller), WaitRequestType.XHR).click();
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 6,
            "After clicking on the step last button (JS API), the current page doesn't match.");

        MetamerPage.waitRequest(page.getJsApiButtonSwitchToFirst(scroller), WaitRequestType.XHR).click();
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step first button (JS API), the current page doesn't match.");

        MetamerPage.waitRequest(page.getJsApiButtonSwitchToNext(scroller), WaitRequestType.XHR).click();
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 2,
            "After clicking on the step next button (JS API), the current page doesn't match.");

        MetamerPage.waitRequest(page.getJsApiButtonSwitchToPrev(scroller), WaitRequestType.XHR).click();
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the step previous button (JS API), the current page doesn't match.");
    }
}
