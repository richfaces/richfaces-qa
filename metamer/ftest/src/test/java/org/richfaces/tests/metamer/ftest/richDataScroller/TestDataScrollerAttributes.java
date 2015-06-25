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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
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

    @Page
    private SimplePage page;

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    private ScrollerPosition scroller;

    private final Attributes<DataTableAttributes> tableAttributes = getAttributes("tableAttributes");

    @Override
    public String getComponentTestPagePath() {
        return "richDataScroller/simple.xhtml";
    }

    private int getNumberOfRows() {
        return driver.findElements(By.cssSelector("table[id$=richDataTable].rf-dt tbody tr.rf-dt-r")).size();
    }

    private void movePopupTemplateWhenUsingScrollerOutsideTable() {
        if (isInPopupTemplate() && scroller.equals(ScrollerPosition.DATA_SCROLLER_OUTSIDE_TABLE)) {
            popupTemplate.advanced().moveByOffset(100, 200);
        }
    }

    @Test
    @CoversAttributes("boundaryControls")
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
    @CoversAttributes("data")
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                movePopupTemplateWhenUsingScrollerOutsideTable();
                page.getScroller(scroller).switchTo(DataScrollerSwitchButton.FAST_FORWARD);
            }
        });
    }

    @Test
    @CoversAttributes({ "onbegin", "onbeforedomupdate", "oncomplete" })
    public void testEvents() throws InterruptedException {
        eventsOrderTester()
            .testOrderOfEvents(DataScrollerAttributes.onbegin, DataScrollerAttributes.onbeforedomupdate, DataScrollerAttributes.oncomplete)
            .triggeredByAction(new Action() {
                @Override
            public void perform() {
                movePopupTemplateWhenUsingScrollerOutsideTable();
                Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.FAST_FORWARD);
                }
            }).test();
    }

    @Test
    @CoversAttributes("execute")
    public void testExecute() {
        // attributes
        attributes.set(DataScrollerAttributes.execute, "executeChecker");
        movePopupTemplateWhenUsingScrollerOutsideTable();
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
    @CoversAttributes("fastControls")
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
    @CoversAttributes("fastStep")
    public void testFastStep() {
        attributes.set(DataScrollerAttributes.fastStep, 3);

        movePopupTemplateWhenUsingScrollerOutsideTable();
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_FORWARD);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 4,
            "After clicking on the fast forward button, the current page doesn't match.");

        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_REWIND);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 1,
            "After clicking on the fast rewind button, the current page doesn't match.");
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
    @CoversAttributes("lastPageMode")
    public void testLastPageMode() {
        movePopupTemplateWhenUsingScrollerOutsideTable();
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
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        attributes.set(DataScrollerAttributes.limitRender, false);
        movePopupTemplateWhenUsingScrollerOutsideTable();
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_FORWARD);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 2,
            "Data scroller's active page with limitRender=false");

        attributes.set(DataScrollerAttributes.limitRender, true);
        movePopupTemplateWhenUsingScrollerOutsideTable();
        String timeBefore = page.getRequestTimeElement().getText();
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.FAST_FORWARD);
        String timeAfter = page.getRequestTimeElement().getText();
        assertEquals(timeAfter, timeBefore,
            "The panel was rerendered despite the fact that the attribute 'limitRender' is set to <true>.");
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 3,
            "Data scroller's active page with limitRender=true");
    }

    @Test
    @CoversAttributes("maxPages")
    public void testMaxPages() {
        // initial value is 10 which means that 6 pages (i.e. all) should be displayed
        assertEquals(page.getScroller(scroller).advanced().getCountOfVisiblePages(), 6 /*
             * it means - all pages
             */,
            "The number of visible pages doesn't match.");

        attributes.set(DataScrollerAttributes.maxPages, 3);
        assertEquals(page.getScroller(scroller).advanced().getCountOfVisiblePages(), 3,
            "The number of visible pages doesn't match.");
    }

    @Test
    @CoversAttributes("page")
    public void testPage() {
        attributes.set(DataScrollerAttributes.page, 4);
        assertEquals(page.getScroller(scroller).getActivePageNumber(), 4, "The number of current page doesn't match.");
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        attributes.set(DataScrollerAttributes.render, "@this renderChecker");
        movePopupTemplateWhenUsingScrollerOutsideTable();
        String renderCheckerText = getMetamerPage().getRenderCheckerOutputElement().getText();
        String requestTime = getMetamerPage().getRequestTimeElement().getText();
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.NEXT);
        Graphene.waitGui().until().element(getMetamerPage().getRenderCheckerOutputElement()).text().not()
            .equalTo(renderCheckerText);
        Graphene.waitGui().until().element(getMetamerPage().getRequestTimeElement()).text().not()
            .equalTo(requestTime);
    }

    @Test
    @CoversAttributes("renderIfSinglePage")
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
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        attributes.set(DataScrollerAttributes.rendered, false);
        Graphene.waitGui().until().element(page.getScroller(scroller).advanced().getRootElement()).is().not().present();
    }

    @Test
    @CoversAttributes("scrollListener")
    public void testScrollListener() {
        movePopupTemplateWhenUsingScrollerOutsideTable();
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.NEXT);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "scroll event: 1 -> next");
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.FAST_FORWARD);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "scroll event: 2 -> fastForward");
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.LAST);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "scroll event: 3 -> last");
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.PREVIOUS);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "scroll event: 6 -> previous");
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.FAST_REWIND);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "scroll event: 5 -> fastRewind");
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(DataScrollerSwitchButton.FIRST);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "scroll event: 4 -> first");
        Graphene.guardAjax(page.getScroller(scroller)).switchTo(5);
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "scroll event: 1 -> 5");
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        attributes.set(DataScrollerAttributes.status, "statusChecker");
        movePopupTemplateWhenUsingScrollerOutsideTable();

        String statusCheckerTime = page.getStatusCheckerOutputElement().getText();
        MetamerPage.waitRequest(page.getScroller(scroller), WaitRequestType.XHR).switchTo(
            DataScrollerSwitchButton.FAST_FORWARD);
        Graphene.waitModel().until("Page was not updated").element(page.getStatusCheckerOutputElement()).text().not()
            .equalTo(statusCheckerTime);
    }

    @Test
    public void testStep() {
        movePopupTemplateWhenUsingScrollerOutsideTable();
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

    @Test
    @CoversAttributes("stepControls")
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
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        super.testStyle(page.getScroller(scroller).advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates("plain")
    public void testStyleClass() {
        super.testStyleClass(page.getScroller(scroller).advanced().getRootElement());
    }

    @Test
    @CoversAttributes("title")
    @Templates("plain")
    public void testTitle() {
        htmlAttributeTester().testTitle(page.getScroller(scroller).advanced().getRootElement()).test();
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
