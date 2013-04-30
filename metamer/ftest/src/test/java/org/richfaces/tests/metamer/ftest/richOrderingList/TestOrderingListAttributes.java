/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.orderingListAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestOrderingListAttributes extends AbstractOrderingListTest {

    @Page
    private MetamerPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/withColumn.xhtml");
    }

    @Test
    public void testCaption() {
        String testedValue = "New Caption";
        orderingListAttributes.set(OrderingListAttributes.caption, testedValue);
        assertEquals(twoColumnOrderingList.getCaption(), testedValue);
    }

    @Test
    public void testColumnClasses() {
        String testedClass = "metamer-ftest-class";
        orderingListAttributes.set(OrderingListAttributes.columnClasses, "metamer-ftest-class");
        for (TwoColumnListItem li : twoColumnOrderingList.getItems()) {
            for (WebElement e : li.getItemElement().findElements(By.tagName("td"))) {
                assertTrue(e.getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
            }
        }
    }

    @Test
    public void testDisabled() {
        orderingListAttributes.set(OrderingListAttributes.disabled, Boolean.TRUE);
        for (TwoColumnListItem li : twoColumnOrderingList.getItems()) {
            assertTrue(li.getItemElement().getAttribute("class").contains("rf-ord-opt-dis"), "Item @class should contain " + "rf-ord-opt-dis");
        }
        try {
            twoColumnOrderingList.getItems().get(0).select();
            fail("The attribute <disabled> is set to true, but the ordering list is still enabled.");
        } catch (TimeoutException e) {
        }
    }

    @Test
    public void testDisabledClass() {
        orderingListAttributes.set(OrderingListAttributes.disabled, Boolean.TRUE);
        testStyleClass(twoColumnOrderingList.getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    public void testDownBottomText() {
        String testedValue = "New text";
        orderingListAttributes.set(OrderingListAttributes.downBottomText, testedValue);
        assertEquals(twoColumnOrderingList.getBottomButtonElement().getText(), testedValue);
    }

    @Test
    public void testDownText() {
        String testedValue = "New text";
        orderingListAttributes.set(OrderingListAttributes.downText, testedValue);
        assertEquals(twoColumnOrderingList.getDownButtonElement().getText(), testedValue);
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(twoColumnOrderingList.getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testImmediate() {
        orderingListAttributes.set(OrderingListAttributes.immediate, Boolean.FALSE);
        twoColumnOrderingList.selectItemsByIndex(1).top();
        submit();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");

        orderingListAttributes.set(OrderingListAttributes.immediate, Boolean.TRUE);
        twoColumnOrderingList.selectItemsByIndex(1).top();
        submit();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed");
    }

    @Test
    public void testItemClass() {
        String testedClass = "metamer-ftest-class";
        orderingListAttributes.set(OrderingListAttributes.itemClass, "metamer-ftest-class");
        for (TwoColumnListItem li : twoColumnOrderingList.getItems()) {
            assertTrue(li.getItemElement().getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
        }
    }

    @Test
    public void testListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        orderingListAttributes.set(OrderingListAttributes.listHeight, testedValue);
        assertEquals(Integer.valueOf(twoColumnOrderingList.getListAreaElement().getCssValue("height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testListWidth() {
        int testedValue = 600;
        int tolerance = 10;
        orderingListAttributes.set(OrderingListAttributes.listWidth, testedValue);
        assertEquals(Integer.valueOf(twoColumnOrderingList.getListAreaElement().getCssValue("width").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testMaxListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        orderingListAttributes.set(OrderingListAttributes.maxListHeight, testedValue);
        orderingListAttributes.set(OrderingListAttributes.listHeight, "");
        assertEquals(Integer.valueOf(twoColumnOrderingList.getListAreaElement().getCssValue("max-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testMinListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        orderingListAttributes.set(OrderingListAttributes.listHeight, "");
        orderingListAttributes.set(OrderingListAttributes.minListHeight, testedValue);
        assertEquals(Integer.valueOf(twoColumnOrderingList.getListAreaElement().getCssValue("min-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testOnblur() {
        testFireEvent(orderingListAttributes, OrderingListAttributes.onblur, new Action() {
            @Override
            public void perform() {
                //have to be this way or the blur will not be triggered
                twoColumnOrderingList.getListAreaElement().click();
                twoColumnOrderingList.getRootElement().click();
                waiting(500);
                Utils.triggerJQ("blur", twoColumnOrderingList.getItems().get(0).getItemElement());
            }
        });
    }

    @Test
    public void testOnchange() {
        testFireEvent(Event.CHANGE, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnclick() {
        testFireEvent(orderingListAttributes, OrderingListAttributes.onlistclick,
                new Actions(driver).click(twoColumnOrderingList.getListAreaElement()).build());
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnfocus() {
        testFireEvent(orderingListAttributes, OrderingListAttributes.onfocus,
                new Actions(driver).click(twoColumnOrderingList.getItems().get(0).getItemElement()).build());
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnlistclick() {
        testFireEvent(orderingListAttributes, OrderingListAttributes.onlistclick, new Actions(driver).click(twoColumnOrderingList.getItems().get(0).getItemElement()).build());
    }

    @Test
    public void testOnlistdblclick() {
        testFireEvent(Event.DBLCLICK, twoColumnOrderingList.getItems().get(0).getItemElement(), "listdblclick");
    }

    @Test
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, twoColumnOrderingList.getItems().get(0).getItemElement(), "listkeydown");
    }

    @Test
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, twoColumnOrderingList.getItems().get(0).getItemElement(), "listkeypress");
    }

    @Test
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, twoColumnOrderingList.getItems().get(0).getItemElement(), "listkeyup");
    }

    @Test
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, twoColumnOrderingList.getItems().get(0).getItemElement(), "listmousedown");
    }

    @Test
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, twoColumnOrderingList.getItems().get(0).getItemElement(), "listmousemove");
    }

    @Test
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, twoColumnOrderingList.getItems().get(0).getItemElement(), "listmouseout");
    }

    @Test
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, twoColumnOrderingList.getItems().get(0).getItemElement(), "listmouseover");
    }

    @Test
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, twoColumnOrderingList.getItems().get(0).getItemElement(), "listmouseup");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, twoColumnOrderingList.getListAreaElement());
    }

    @Test
    public void testRendered() {
        orderingListAttributes.set(OrderingListAttributes.rendered, false);
        assertNotPresent(twoColumnOrderingList.getRootElement(), "The attribute <rendered> is set to <false>, but it has no effect.");
    }

    @Test
    public void testSelectItemClass() {
        orderingListAttributes.set(OrderingListAttributes.selectItemClass, "metamer-ftest-class");
        twoColumnOrderingList.selectItemsByIndex(0);
        assertTrue(twoColumnOrderingList.getSelectedItems().get(0).getItemElement().getAttribute("class").contains("metamer-ftest-class"), "The attribute <selectItemClass> is set to <metamer-ftest-class>, but it has no effect.");
    }

    @Test
    public void testStyle() {
        testStyle(twoColumnOrderingList.getRootElement());
    }

    @Test
    public void testStyleClass() {
        testStyle(twoColumnOrderingList.getRootElement());
    }

    @Test
    public void testUpText() {
        String testedValue = "New text";
        orderingListAttributes.set(OrderingListAttributes.upText, testedValue);
        assertEquals(twoColumnOrderingList.getUpButtonElement().getText(), testedValue);
    }

    @Test
    public void testUpTopText() {
        String testedValue = "New text";
        orderingListAttributes.set(OrderingListAttributes.upTopText, testedValue);
        assertEquals(twoColumnOrderingList.getTopButtonElement().getText(), testedValue);
    }

    @Test
    public void testValueChangeListener() {
        twoColumnOrderingList.selectItemsByIndex(0);
        twoColumnOrderingList.bottom();
        submit();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");
    }
}
