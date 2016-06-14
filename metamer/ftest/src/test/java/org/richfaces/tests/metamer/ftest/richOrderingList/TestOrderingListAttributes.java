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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import javax.faces.event.PhaseId;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestOrderingListAttributes extends AbstractOrderingListTest {

    @Override
    public String getComponentTestPagePath() {
        return "richOrderingList/withColumn.xhtml";
    }

    @Test
    @CoversAttributes("caption")
    public void testCaption() {
        String testedValue = "New Caption";
        attributes.set(OrderingListAttributes.caption, testedValue);
        assertEquals(orderingList.advanced().getCaptionElement().getText(), testedValue);
    }

    @Test
    @CoversAttributes("collectionType")
    public void testCollectionType() {
        int indexInMiddle1 = 22;
        int indexInMiddle2 = 7;
        // the @collectionType attribute accepts both String and Class values, which are resolved in bean by according prefix
        for (String testedValue : new String[] { "class-ArrayList", "string-LinkedList", "class-Stack", "string-Vector" }) {
            attributes.set(OrderingListAttributes.collectionType, testedValue);
            orderingList.select(indexInMiddle1).putItBefore(0);
            submitAndCheckElementsOrderPersists();
            orderingList.select(ChoicePickerHelper.byIndex().last()).putItAfter(indexInMiddle2);
            submitAndCheckElementsOrderPersists();
        }
    }

    @Test
    @Templates("plain")
    @CoversAttributes("collectionType")
    public void testCollectionType_unsupportedTypeThrowsException() {
        try {
            attributes.set(OrderingListAttributes.collectionType, "invalid-LinkedHashMap");
            submit();
            String exceptionText = driver.findElement(By.tagName("body")).getText();
            assertTrue(exceptionText.contains("java.util.LinkedHashMap cannot be cast to java.util.Collection"));

            loadPage();
            attributes.set(OrderingListAttributes.collectionType, "invalid-LinkedHashSet");
            submit();
            exceptionText = driver.findElement(By.tagName("body")).getText();
            assertTrue(exceptionText.contains("Ordered List Components must be backed by a List or Array"));
        } finally {
            loadPage();
            attributes.set(OrderingListAttributes.collectionType, "string-LinkedList");
        }
    }

    @Test
    @CoversAttributes("disabled")
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
    @CoversAttributes("disabledClass")
    @Templates(value = "plain")
    public void testDisabledClass() {
        attributes.set(OrderingListAttributes.disabled, Boolean.TRUE);
        testStyleClass(orderingList.advanced().getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    @CoversAttributes("downBottomText")
    @Templates(value = "plain")
    public void testDownBottomText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.downBottomText, testedValue);
        assertEquals(orderingList.advanced().getBottomButtonElement().getText(), testedValue);
    }

    @Test
    @CoversAttributes("downText")
    @Templates(value = "plain")
    public void testDownText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.downText, testedValue);
        assertEquals(orderingList.advanced().getDownButtonElement().getText(), testedValue);
    }

    @Test
    @CoversAttributes("headerClass")
    @Templates(value = "plain")
    public void testHeaderClass() {
        testStyleClass(orderingList.advanced().getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        attributes.set(OrderingListAttributes.immediate, Boolean.FALSE);
        orderingList.select(1).putItBefore(0);
        submitAndCheckElementsOrderPersists();
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");

        attributes.set(OrderingListAttributes.immediate, Boolean.TRUE);
        orderingList.select(1).putItBefore(0);
        submitAndCheckElementsOrderPersists();
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed");
    }

    @Test
    @CoversAttributes("itemClass")
    @Templates(value = "plain")
    public void testItemClass() {
        String testedClass = "metamer-ftest-class";
        attributes.set(OrderingListAttributes.itemClass, "metamer-ftest-class");
        for (WebElement element : orderingList.advanced().getItemsElements()) {
            assertTrue(element.getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
        }
    }

    @Test
    @CoversAttributes("listHeight")
    @Templates(value = "plain")
    public void testListHeight() {
        int testedValue = 600;
        int tolerance = 20;
        attributes.set(OrderingListAttributes.listHeight, testedValue);
        assertEquals(Integer.valueOf(orderingList.advanced().getContentAreaElement().getCssValue("height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    @CoversAttributes("listWidth")
    @Templates(value = "plain")
    public void testListWidth() {
        int testedValue = 600;
        int tolerance = 20;
        attributes.set(OrderingListAttributes.listWidth, testedValue);
        assertEquals(Integer.valueOf(orderingList.advanced().getContentAreaElement().getCssValue("width").replace("px", "")), testedValue, tolerance);
    }

    @Test
    @CoversAttributes("maxListHeight")
    @Templates(value = "plain")
    public void testMaxListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        attsSetter()
            .setAttribute(OrderingListAttributes.maxListHeight).toValue(testedValue)
            .setAttribute(OrderingListAttributes.listHeight).toValue("")
            .asSingleAction().perform();
        assertEquals(Integer.valueOf(orderingList.advanced().getContentAreaElement().getCssValue("max-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    @CoversAttributes("minListHeight")
    @Templates(value = "plain")
    public void testMinListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        attsSetter()
            .setAttribute(OrderingListAttributes.minListHeight).toValue(testedValue)
            .setAttribute(OrderingListAttributes.listHeight).toValue("")
            .asSingleAction().perform();
        assertEquals(Integer.valueOf(orderingList.advanced().getContentAreaElement().getCssValue("min-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    @CoversAttributes("onblur")
    @Templates(value = "plain")
    public void testOnblur() {
        WebElement focusKeeper = driver.findElement(By.cssSelector(".rf-ord input[id$=FocusKeeper]"));
        testFireEventWithJS(focusKeeper, attributes, OrderingListAttributes.onblur);
    }

    @Test
    @CoversAttributes("onchange")
    @Templates(value = "plain")
    public void testOnchange() {
        testFireEvent(Event.CHANGE, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(attributes, OrderingListAttributes.onclick,
            new Actions(driver).click(orderingList.advanced().getContentAreaElement()).build());
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onfocus")
    @Templates(value = "plain")
    public void testOnfocus() {
        testFireEvent(attributes, OrderingListAttributes.onfocus,
            new Actions(driver).click(orderingList.advanced().getItemsElements().get(0)).build());
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onlistclick")
    @Templates(value = "plain")
    public void testOnlistclick() {
        testFireEvent(attributes, OrderingListAttributes.onlistclick, new Actions(driver).click(orderingList.advanced().getItemsElements().get(0)).build());
    }

    @Test
    @CoversAttributes("onlistdblclick")
    @Templates(value = "plain")
    public void testOnlistdblclick() {
        testFireEvent(Event.DBLCLICK, orderingList.advanced().getItemsElements().get(0), "listdblclick");
    }

    @Test
    @CoversAttributes("onlistkeydown")
    @Templates(value = "plain")
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, orderingList.advanced().getItemsElements().get(0), "onlistkeydown");
    }

    @Test
    @CoversAttributes("onlistkeypress")
    @Templates(value = "plain")
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, orderingList.advanced().getItemsElements().get(0), "listkeypress");
    }

    @Test
    @CoversAttributes("onlistkeyup")
    @Templates(value = "plain")
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, orderingList.advanced().getItemsElements().get(0), "listkeyup");
    }

    @Test
    @CoversAttributes("onlistmousedown")
    @Templates(value = "plain")
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, orderingList.advanced().getItemsElements().get(0), "listmousedown");
    }

    @Test
    @CoversAttributes("onlistmousemove")
    @Templates(value = "plain")
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, orderingList.advanced().getItemsElements().get(0), "listmousemove");
    }

    @Test
    @CoversAttributes("onlistmouseout")
    @Templates(value = "plain")
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, orderingList.advanced().getItemsElements().get(0), "listmouseout");
    }

    @Test
    @CoversAttributes("onlistmouseover")
    @Templates(value = "plain")
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, orderingList.advanced().getItemsElements().get(0), "listmouseover");
    }

    @Test
    @CoversAttributes("onlistmouseup")
    @Templates(value = "plain")
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, orderingList.advanced().getItemsElements().get(0), "listmouseup");
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, orderingList.advanced().getContentAreaElement());
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        attributes.set(OrderingListAttributes.rendered, false);
        assertNotPresent(orderingList.advanced().getRootElement(), "The attribute <rendered> is set to <false>, but it has no effect.");
    }

    @Test
    @CoversAttributes("selectItemClass")
    @Templates(value = "plain")
    public void testSelectItemClass() {
        attributes.set(OrderingListAttributes.selectItemClass, "metamer-ftest-class");
        orderingList.select(0);
        assertTrue(orderingList.advanced().getSelectedItemsElements().get(0).getAttribute("class").contains("metamer-ftest-class"), "The attribute <selectItemClass> is set to <metamer-ftest-class>, but it has no effect.");
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(orderingList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(orderingList.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("upText")
    @Templates(value = "plain")
    public void testUpText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.upText, testedValue);
        assertEquals(orderingList.advanced().getUpButtonElement().getText(), testedValue);
    }

    @Test
    @CoversAttributes("upTopText")
    @Templates(value = "plain")
    public void testUpTopText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.upTopText, testedValue);
        assertEquals(orderingList.advanced().getTopButtonElement().getText(), testedValue);
    }

    @Test
    @CoversAttributes("valueChangeListener")
    public void testValueChangeListener() {
        orderingList.select(0).putItAfter(ChoicePickerHelper.byIndex().last());
        submitAndCheckElementsOrderPersists();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");
    }
}
