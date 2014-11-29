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
package org.richfaces.tests.metamer.ftest.richAccordion;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.accordion.RichFacesAccordionItem;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.switchable.SwitchType;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestAccordion extends AbstractWebDriverTest {

    private final Attributes<AccordionAttributes> accordionAttributes = getAttributes();

    @Page
    private AccordionPage page;

    @Override
    public String getComponentTestPagePath() {
        return "richAccordion/simple.xhtml";
    }

    private final Action switchToFirstItemAction = new Action() {
        @Override
        public void perform() {
            page.getAccordion().switchTo(1);
        }
    };

    @Test
    public void testInit() {
        assertTrue(page.isAccordionVisible(), "Accordion is not present on the page.");
        assertEquals(page.getAccordion().advanced().getAccordionItems().size(), 5, "number of visible headers");
        assertTrue(page.getAccordion().advanced().getAccordionItems().get(0).advanced().isActive(),
            "The first accordion item should be active.");
        List<RichFacesAccordionItem> items = page.getAccordion().advanced().getAccordionItems();
        for (int i = 1; i < items.size(); i++) {
            assertTrue(!items.get(i).advanced().isActive(), "Item " + (i + 1) + " shouldn't be active.");
        }
    }

    @Test(groups = "smoke")
    @CoversAttributes("activeItem")
    public void testActiveItem() {
        accordionAttributes.set(AccordionAttributes.activeItem, "item5");

        assertTrue(page.isAccordionVisible(), "Accordion is not present on the page.");
        List<RichFacesAccordionItem> items = page.getAccordion().advanced().getAccordionItems();
        for (RichFacesAccordionItem item : items) {
            assertTrue(item.advanced().isActive() || !item.advanced().isActive() || !item.advanced().isEnabled(), "Item "
                + item.advanced().getHeader() + "'s header should be visible.");
        }

        assertTrue(items.get(4).advanced().isActive(), "Content of item5 should be visible.");
        for (int i = 0; i < 4; i++) {
            assertTrue(!items.get(i).advanced().isActive(), "Item " + (i + 1) + " shouldn't be active.");
        }

        accordionAttributes.set(AccordionAttributes.activeItem, "item4");
        for (RichFacesAccordionItem item : items) {
            assertTrue(item.advanced().isActive() || !item.advanced().isActive() || !item.advanced().isEnabled(), "Item "
                + item.advanced().getHeader() + "'s header should be visible.");
        }

        assertTrue(items.get(0).advanced().isActive(), "Item 1 should be active.");
        for (int i = 1; i < 4; i++) {
            assertTrue(!items.get(i).advanced().isActive(), "Item " + (i + 1) + " shouldn't be active.");
        }
    }

    @Test(groups = "smoke")
    @CoversAttributes("cycledSwitching")
    public void testCycledSwitching() {
        WebElement rootElement = page.getAccordion().advanced().getRootElement();
        // RichFaces.$('form:accordion').nextItem('item5') will be null
        assertEquals(Utils.invokeRichFacesJSAPIFunction(rootElement, "nextItem('item5')"), null, "Result of function nextItem('item5')");

        // RichFaces.$('form:accordion').prevItem('item1') will be null
        assertEquals(Utils.invokeRichFacesJSAPIFunction(rootElement, "prevItem('item1')"), null, "Result of function prevItem('item1')");

        accordionAttributes.set(AccordionAttributes.cycledSwitching, true);

        // RichFaces.$('form:accordion').nextItem('item5') will be item1
        assertEquals(Utils.invokeRichFacesJSAPIFunction(rootElement, "nextItem('item5')"), "item1", "Result of function nextItem('item5')");

        // RichFaces.$('form:accordion').prevItem('item1') will be item5
        assertEquals(Utils.invokeRichFacesJSAPIFunction(rootElement, "prevItem('item1')"), "item5", "Result of function prevItem('item1')");
    }

    @Test
    @CoversAttributes("dir")
    @Templates(value = "plain")
    public void testDir() {
        testDir(page.getAccordionRootElement());
    }

    @Test
    @CoversAttributes("height")
    @Templates(value = "plain")
    public void testHeight() {
        WebElement accordionRoot = page.getAccordionRootElement();
        // height = null
        assertEquals(accordionRoot.getAttribute("style"), "", "Attribute style should not be present.");

        // height = 300px
        accordionAttributes.set(AccordionAttributes.height, "300px");
        assertEquals(accordionRoot.getCssValue("height"), "300px", "Attribute height");
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        accordionAttributes.set(AccordionAttributes.immediate, true);

        page.getAccordion().switchTo(2);

        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: item1 -> item3");
    }

    @Test
    @CoversAttributes("itemActiveHeaderClass")
    @Templates(value = "plain")
    public void testItemActiveHeaderClass() {
        testStyleClass(page.getAccordion().advanced().getActiveItem().advanced().getHeaderElement(),
            BasicAttributes.itemActiveHeaderClass);
    }

    @Test
    @CoversAttributes("itemChangeListener")
    public void testItemChangeListener() {
        page.getAccordion().switchTo(2);
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: item1 -> item3");
    }

    @Test
    @CoversAttributes("itemContentClass")
    @Templates(value = "plain")
    public void testItemContentClass() {
        for (RichFacesAccordionItem item : page.getAccordion().advanced().getAccordionItems()) {
            testStyleClass(item.advanced().getContentElement(), BasicAttributes.itemContentClass);
        }
    }

    @Test
    @CoversAttributes("itemDisabledHeaderClass")
    @Templates(value = "plain")
    public void testItemDisabledHeaderClass() {
        for (RichFacesAccordionItem item : page.getAccordion().advanced().getAccordionItems()) {
            if (!item.advanced().isEnabled()) {
                testStyleClass(item.advanced().getHeaderElement(), BasicAttributes.itemDisabledHeaderClass);
            }
        }
    }

    @Test
    @CoversAttributes("itemHeaderClass")
    @Templates(value = "plain")
    public void testItemHeaderClass() {
        for (RichFacesAccordionItem item : page.getAccordion().advanced().getAccordionItems()) {
            testStyleClass(item.advanced().getToActivateElement(), BasicAttributes.itemHeaderClass);
        }
    }

    @Test
    @CoversAttributes({ "onbeforeitemchange", "onitemchange" })
    public void testItemchangeEvents() {
        eventsOrderTester().testOrderOfEvents("onbeforeitemchange", "onitemchange").triggeredByAction(switchToFirstItemAction)
            .test();
    }

    @Test
    @CoversAttributes("itemInactiveHeaderClass")
    @Templates(value = "plain")
    public void testItemInactiveHeaderClass() {
        for (RichFacesAccordionItem item : page.getAccordion().advanced().getAccordionItems()) {
            if (!item.advanced().isActive() && item.advanced().isEnabled()) {
                testStyleClass(item.advanced().getHeaderElement(), BasicAttributes.itemInactiveHeaderClass);
            }
        }
    }

    @Test
    @CoversAttributes("lang")
    @Templates(value = "plain")
    public void testLang() {
        testLang(page.getAccordionRootElement());
    }

    @Test
    @CoversAttributes("onbeforeitemchange")
    public void testOnbeforeitemchange() {
        eventTester().testEvent("beforeitemchange").withCustomAction(switchToFirstItemAction).test();
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        eventTester().testEvent(Event.CLICK).onElement(page.getAccordionRootElement()).test();
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        eventTester().testEvent(Event.DBLCLICK).onElement(page.getAccordionRootElement()).test();
    }

    @Test
    @CoversAttributes("onitemchange")
    public void testOnitemchange() {
        eventTester().testEvent("itemchange").withCustomAction(switchToFirstItemAction).test();
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        eventTester().testEvent(Event.MOUSEDOWN).onElement(page.getAccordionRootElement()).test();
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        eventTester().testEvent(Event.MOUSEMOVE).onElement(page.getAccordionRootElement()).test();
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        eventTester().testEvent(Event.MOUSEOUT).onElement(page.getAccordionRootElement()).test();
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        eventTester().testEvent(Event.MOUSEOVER).onElement(page.getAccordionRootElement()).test();
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        eventTester().testEvent(Event.MOUSEUP).onElement(page.getAccordionRootElement()).test();
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        accordionAttributes.set(AccordionAttributes.rendered, false);
        assertFalse(page.isAccordionVisible());
    }

    @Test
    public void testSimple() {
        List<RichFacesAccordionItem> items = page.getAccordion().advanced().getAccordionItems();
        for (int i = 0; i < items.size(); i++) {
            RichFacesAccordionItem item = items.get(i);
            if (item.advanced().isEnabled()) {
                page.getAccordion().switchTo(i);
                Assert.assertTrue(item.advanced().isActive());
            }
        }
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getAccordionRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getAccordionRootElement());
    }

    @Test
    @CoversAttributes("switchType")
    @RegressionTest("https://issues.jboss.org/browse/RF-12532")
    public void testSwitchTypeNull() {
        for (int i = 2; i >= 0; i--) {
            Graphene.guardAjax(page.getAccordion()).switchTo(i);
        }
    }

    @Test
    @CoversAttributes("switchType")
    @RegressionTest("https://issues.jboss.org/browse/RF-12532")
    @Templates(value = "plain")
    public void testSwitchTypeAjax() {
        accordionAttributes.set(AccordionAttributes.switchType, "ajax");
        testSwitchTypeNull();
    }

    @Test
    @CoversAttributes("switchType")
    @RegressionTest("https://issues.jboss.org/browse/RF-12532")
    public void testSwitchTypeClient() {
        accordionAttributes.set(AccordionAttributes.switchType, "client");
        page.getAccordion().advanced().setSwitchType(SwitchType.CLIENT);
        for (int i = 2; i >= 0; i--) {
            Graphene.guardNoRequest(page.getAccordion()).switchTo(i);
        }
    }

    @Test
    @CoversAttributes("switchType")
    @RegressionTest({ "https://issues.jboss.org/browse/RF-10040", "https://issues.jboss.org/browse/RF-12532" })
    public void testSwitchTypeServer() {
        accordionAttributes.set(AccordionAttributes.switchType, "server");
        page.getAccordion().advanced().setSwitchType(SwitchType.SERVER);
        for (int i = 2; i >= 0; i--) {
            Graphene.guardHttp(page.getAccordion()).switchTo(i);
        }
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        testHTMLAttribute(page.getAccordionRootElement(), accordionAttributes, AccordionAttributes.title, "new title");
    }

    @Test
    @CoversAttributes("width")
    @Templates(value = "plain")
    public void testWidth() {
        String width = "400px";
        accordionAttributes.set(AccordionAttributes.width, width);
        assertEquals(page.getAccordionRootElement().getCssValue("width"), width);
        width = "150px";
        accordionAttributes.set(AccordionAttributes.width, width);
        assertEquals(page.getAccordionRootElement().getCssValue("width"), width);
    }
}
