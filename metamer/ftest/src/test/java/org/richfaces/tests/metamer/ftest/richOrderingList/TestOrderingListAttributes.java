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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestOrderingListAttributes extends AbstractOrderingListTest {

    @Page
    private MetamerPage page;

    @ArquillianResource
    private JavascriptExecutor executor;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/withColumn.xhtml");
    }

    @Test
    public void testCaption() {
        String testedValue = "New Caption";
        attributes.set(OrderingListAttributes.caption, testedValue);
        assertEquals(orderingList.advanced().getCaptionElement().getText(), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testColumnClasses() {
        String testedClass = "metamer-ftest-class";
        attributes.set(OrderingListAttributes.columnClasses, testedClass);
        for (WebElement li : orderingList.advanced().getItemsElements()) {
            for (WebElement e : li.findElements(By.tagName("td"))) {
                assertTrue(e.getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
            }
        }
    }

    @Test
    public void testDisabled() {
        attributes.set(OrderingListAttributes.disabled, Boolean.TRUE);
        for (WebElement li : orderingList.advanced().getItemsElements()) {
            assertTrue(li.getAttribute("class").contains("rf-ord-opt-dis"), "Item @class should contain " + "rf-ord-opt-dis");
        }
        try {
            orderingList.select(0);
            fail("The attribute <disabled> is set to true, but the ordering list is still enabled.");
        } catch (TimeoutException e) {
        }
    }

    @Test
    @Templates(value = "plain")
    public void testDisabledClass() {
        attributes.set(OrderingListAttributes.disabled, Boolean.TRUE);
        testStyleClass(orderingList.advanced().getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    @Templates(value = "plain")
    public void testDownBottomText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.downBottomText, testedValue);
        assertEquals(orderingList.advanced().getBottomButtonElement().getText(), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testDownText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.downText, testedValue);
        assertEquals(orderingList.advanced().getDownButtonElement().getText(), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testHeaderClass() {
        testStyleClass(orderingList.advanced().getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testImmediate() {
        attributes.set(OrderingListAttributes.immediate, Boolean.FALSE);
        orderingList.select(1).putItBefore(0);
        submit();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");

        attributes.set(OrderingListAttributes.immediate, Boolean.TRUE);
        orderingList.select(1).putItBefore(0);
        submit();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed");
    }

    @Test
    @Templates(value = "plain")
    public void testItemClass() {
        String testedClass = "metamer-ftest-class";
        attributes.set(OrderingListAttributes.itemClass, "metamer-ftest-class");
        for (WebElement element : orderingList.advanced().getItemsElements()) {
            assertTrue(element.getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testListHeight() {
        int testedValue = 600;
        int tolerance = 20;
        attributes.set(OrderingListAttributes.listHeight, testedValue);
        assertEquals(Integer.valueOf(orderingList.advanced().getListAreaElement().getCssValue("height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    @Templates(value = "plain")
    public void testListWidth() {
        int testedValue = 600;
        int tolerance = 20;
        attributes.set(OrderingListAttributes.listWidth, testedValue);
        assertEquals(Integer.valueOf(orderingList.advanced().getListAreaElement().getCssValue("width").replace("px", "")), testedValue, tolerance);
    }

    @Test
    @Templates(value = "plain")
    public void testMaxListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        attributes.set(OrderingListAttributes.maxListHeight, testedValue);
        attributes.set(OrderingListAttributes.listHeight, "");
        assertEquals(Integer.valueOf(orderingList.advanced().getListAreaElement().getCssValue("max-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    @Templates(value = "plain")
    public void testMinListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        attributes.set(OrderingListAttributes.listHeight, "");
        attributes.set(OrderingListAttributes.minListHeight, testedValue);
        assertEquals(Integer.valueOf(orderingList.advanced().getListAreaElement().getCssValue("min-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    @Templates(value = "plain")
    public void testOnblur() {
        WebElement focusKeeper = driver.findElement(By.cssSelector(".rf-ord input[id$=FocusKeeper]"));
        testFireEventWithJS(focusKeeper, attributes, OrderingListAttributes.onblur);
    }

    @Test
    @Templates(value = "plain")
    public void testOnchange() {
        testFireEvent(Event.CHANGE, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(attributes, OrderingListAttributes.onlistclick,
            new Actions(driver).click(orderingList.advanced().getListAreaElement()).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(attributes, OrderingListAttributes.onfocus,
            new Actions(driver).click(orderingList.advanced().getItemsElements().get(0)).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistclick() {
        testFireEvent(attributes, OrderingListAttributes.onlistclick, new Actions(driver).click(orderingList.advanced().getItemsElements().get(0)).build());
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistdblclick() {
        testFireEvent(Event.DBLCLICK, orderingList.advanced().getItemsElements().get(0), "listdblclick");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, orderingList.advanced().getItemsElements().get(0), "listkeydown");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, orderingList.advanced().getItemsElements().get(0), "listkeypress");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, orderingList.advanced().getItemsElements().get(0), "listkeyup");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, orderingList.advanced().getItemsElements().get(0), "listmousedown");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, orderingList.advanced().getItemsElements().get(0), "listmousemove");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, orderingList.advanced().getItemsElements().get(0), "listmouseout");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, orderingList.advanced().getItemsElements().get(0), "listmouseover");
    }

    @Test
    @Templates(value = "plain")
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, orderingList.advanced().getItemsElements().get(0), "listmouseup");
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        attributes.set(OrderingListAttributes.rendered, false);
        assertNotPresent(orderingList.advanced().getRootElement(), "The attribute <rendered> is set to <false>, but it has no effect.");
    }

    @Test
    @Templates(value = "plain")
    public void testSelectItemClass() {
        attributes.set(OrderingListAttributes.selectItemClass, "metamer-ftest-class");
        orderingList.select(0);
        assertTrue(orderingList.advanced().getSelectedItemsElements().get(0).getAttribute("class").contains("metamer-ftest-class"), "The attribute <selectItemClass> is set to <metamer-ftest-class>, but it has no effect.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(orderingList.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyle(orderingList.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testUpText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.upText, testedValue);
        assertEquals(orderingList.advanced().getUpButtonElement().getText(), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testUpTopText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.upTopText, testedValue);
        assertEquals(orderingList.advanced().getTopButtonElement().getText(), testedValue);
    }

    @Test
    public void testValueChangeListener() {
        orderingList.select(0).putItAfter(ChoicePickerHelper.byIndex().last());
        submit();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");
    }
}
