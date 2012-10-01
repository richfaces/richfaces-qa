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
package org.richfaces.tests.metamer.ftest.richAccordion;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.accordionAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richAccordion/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class TestRichAccordionWebDriver extends AbstractWebDriverTest<AccordionPage> {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordion/simple.xhtml");
    }

    @Override
    protected AccordionPage createPage() {
        return new AccordionPage();
    }

    @Test
    public void testInit() {

        assertTrue(page.accordion.isDisplayed(), "Accordion is not present on the page.");

        assertEquals(page.headers.size(), 5, "number of visible headers");
        for (int i = 0; i < 5; i++) {
            assertTrue(page.headers.get(i).isDisplayed(), "Item" + (i + 1) + "'s header should be visible.");
        }

        assertTrue(page.itemContents.get(0).isDisplayed(), "Content of item1 should be visible.");

        for (int i = 1; i < 5; i++) {
            assertFalse(page.itemContents.get(i).isDisplayed(), "Item" + (i + 1) + "'s content should not be visible.");
        }
    }

    @Test
    public void testActiveItem() {
        accordionAttributes.set(AccordionAttributes.activeItem, "item5");

        assertTrue(page.accordion.isDisplayed(), "Accordion is not present on the page.");
        for (int i = 0; i < 5; i++) {
            assertTrue(page.headers.get(i).isDisplayed(), "Item" + (i + 1) + "'s header should be visible.");
        }

        assertTrue(page.itemContents.get(4).isDisplayed(), "Content of item5 should be visible.");
        for (int i = 0; i < 4; i++) {
            assertFalse(page.itemContents.get(i).isDisplayed(), "Item" + (i + 1) + "'s content should not be visible.");
        }

        accordionAttributes.set(AccordionAttributes.activeItem, "item4");
        for (int i = 0; i < 5; i++) {
            assertTrue(page.headers.get(i).isDisplayed(), "Item" + (i + 1) + "'s header should be visible.");
        }

        for (int i = 0; i < 5; i++) {
            assertFalse(page.itemContents.get(i).isDisplayed(), "Item" + (i + 1) + "'s content should not be visible.");
        }
    }

    @Test
    public void testCycledSwitching() {
        String accordionId = ((JavascriptExecutor) driver).executeScript("return testedComponentId").toString();
        Object result = null;

        // RichFaces.$('form:accordion').nextItem('item4') will be null
        result = ((JavascriptExecutor) driver).executeScript("return RichFaces.$('" + accordionId
            + "').nextItem('item4')");
        assertEquals(result, null, "Result of function nextItem('item4')");

        // RichFaces.$('form:accordion').prevItem('item1') will be null
        result = ((JavascriptExecutor) driver).executeScript("return RichFaces.$('" + accordionId
            + "').prevItem('item1')");
        assertEquals(result, null, "Result of function prevItem('item1')");

        accordionAttributes.set(AccordionAttributes.cycledSwitching, true);

        // RichFaces.$('form:accordion').nextItem('item5') will be item1
        result = ((JavascriptExecutor) driver).executeScript("return RichFaces.$('" + accordionId
            + "').nextItem('item5')");
        assertEquals(result.toString(), "item1", "Result of function nextItem('item5')");

        // RichFaces.$('form:accordion').prevItem('item1') will be item5
        result = ((JavascriptExecutor) driver).executeScript("return RichFaces.$('" + accordionId
            + "').prevItem('item1')");
        assertEquals(result.toString(), "item5", "Result of function prevItem('item1')");
    }

    @Test
    public void testDir() {
        testDir(page.accordion);
    }

    @Test
    public void testHeight() {
        // height = null
        assertEquals(page.accordion.getAttribute("style"), "", "Attribute style should not be present.");

        // height = 300px
        accordionAttributes.set(AccordionAttributes.height, "300px");
        assertEquals(page.accordion.getCssValue("height"), "300px", "Attribute height");
    }

    @Test
    public void testImmediate() {
        accordionAttributes.set(AccordionAttributes.immediate, true);

        page.headers.get(2).click();
        Graphene.waitModel().withMessage("Item 3 is not displayed.")
            .until(Graphene.element(page.itemContents.get(2)).isVisible());

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: item1 -> item3");
    }

    @Test
    public void testItemActiveHeaderClass() {
        testStyleClass(page.activeHeaders.get(0), BasicAttributes.itemActiveHeaderClass);
    }

    @Test
    public void testItemChangeListener() {
        page.headers.get(2).click();
        Graphene.waitModel().withMessage("Item 3 is not displayed.")
            .until(Graphene.element(page.itemContents.get(2)).isVisible());

        phaseInfo.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: item1 -> item3");
    }

    @Test
    public void testItemContentClass() {
        testStyleClass(page.itemContents.get(2), BasicAttributes.itemContentClass);
    }

    @Test
    public void testItemDisabledHeaderClass() {
        testStyleClass(page.disabledHeaders.get(0), BasicAttributes.itemDisabledHeaderClass);
    }

    @Test
    public void testItemHeaderClass() {
        testStyleClass(page.headers.get(2), BasicAttributes.itemHeaderClass);
    }

    @Test
    public void testItemInactiveHeaderClass() {
        testStyleClass(page.inactiveHeaders.get(2), BasicAttributes.itemInactiveHeaderClass);
    }

    @Test
    public void testLang() {
        testHTMLAttribute(page.accordion, accordionAttributes, AccordionAttributes.lang, "sk");
    }

    @Test
    public void testOnclick() {
        Action action = new Actions(driver).click(page.accordion).build();
        testFireEvent(accordionAttributes, AccordionAttributes.onclick, action);
    }

    @Test
    public void testOndblclick() {
        Action action = new Actions(driver).doubleClick(page.accordion).build();
        testFireEvent(accordionAttributes, AccordionAttributes.ondblclick, action);
    }

    @Test
    public void testOnmousedown() {
        Action action = new Actions(driver).clickAndHold(page.accordion).build();
        testFireEvent(accordionAttributes, AccordionAttributes.onmousedown, action);
    }

    @Test
    public void testOnmousemove() {
        Action action = new Actions(driver).moveToElement(page.accordion).build();
        testFireEvent(accordionAttributes, AccordionAttributes.onmousemove, action);
    }

    @Test
    public void testOnmouseover() {
        Action action = new Actions(driver).moveToElement(page.accordion).build();
        testFireEvent(accordionAttributes, AccordionAttributes.onmouseover, action);
    }

    @Test
    public void testOnmouseup() {
        Action action = new Actions(driver).click(page.accordion).build();
        testFireEvent(accordionAttributes, AccordionAttributes.onmouseup, action);
    }

    @Test
    public void testRendered() {
        accordionAttributes.set(AccordionAttributes.rendered, false);

        try {
            page.accordion.isDisplayed();
            fail("Accordion should not be rendered when rendered=false.");
        } catch (NoSuchElementException ex) {
            // expected
        }
    }

    @Test
    public void testStyle() {
        testStyle(page.accordion);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(page.accordion);
    }

    @Test
    public void testSwitchTypeNull() {
        for (int i = 2; i >= 0; i--) {
            final int index = i;
            Graphene.guardXhr(page.headers.get(index)).click();
            Graphene.waitGui().withMessage("Item " + index + " is not displayed.")
                .until(Graphene.element(page.itemContents.get(index)).isVisible());
        }
    }

    @Test
    public void testSwitchTypeAjax() {
        accordionAttributes.set(AccordionAttributes.switchType, "ajax");

        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        accordionAttributes.set(AccordionAttributes.switchType, "client");

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            Graphene.guardNoRequest(page.headers.get(index)).click();
            Graphene.waitGui().withMessage("Item " + index + " is not displayed.")
                .until(Graphene.element(page.itemContents.get(index)).isVisible());
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10040")
    public void testSwitchTypeServer() {
        accordionAttributes.set(AccordionAttributes.switchType, "server");

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            Graphene.guardHttp(page.headers.get(index)).click();
            Graphene.waitGui().withMessage("Item " + index + " is not displayed.")
                .until(Graphene.element(page.itemContents.get(index)).isVisible());
        }
    }

    @Test
    public void testTitle() {
        testTitle(page.accordion);
    }

    @Test
    public void testWidth() {
        // width = null
        assertEquals(page.accordion.getAttribute("style"), "", "Attribute style should not be present.");

        // width = 356px
        accordionAttributes.set(AccordionAttributes.width, "356px");
        assertEquals(page.accordion.getCssValue("width"), "356px", "Attribute width");
    }
}
