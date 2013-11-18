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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
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

    private void checkHeightAttribute(OrderingListAttributes heightAtt, boolean withUnit) {
        int testedValue = 600;
        int tolerance = 10;

        attributes.set(heightAtt, String.format((withUnit ? "%spx" : "%s"), testedValue));
        switch (heightAtt) {
            case listHeight:
                assertEquals(Integer.valueOf(orderingList.advanced().getScrollBoxElement().getCssValue("height").replace("px", "")), testedValue, tolerance);
                break;
            case maxListHeight:
                assertEquals(Integer.valueOf(orderingList.advanced().getScrollBoxElement().getCssValue("max-height").replace("px", "")), testedValue, tolerance);
                break;
            case minListHeight:
                assertEquals(Integer.valueOf(orderingList.advanced().getScrollBoxElement().getCssValue("min-height").replace("px", "")), testedValue, tolerance);
                break;
            default:
                throw new UnsupportedOperationException("Unknown height attribute " + heightAtt);
        }
    }

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
    @IssueTracking("https://github.com/richwidgets/richwidgets/issues/124")
    public void testColumnClasses() {
        // test single class applies only to the first column
        String testedClass = "metamer-ftest-class";
        attributes.set(OrderingListAttributes.columnClasses, testedClass);

        for (WebElement li : orderingList.advanced().getItemsElements()) {
            List<WebElement> columns = li.findElements(By.tagName("td"));
            assertEquals(columns.size(), 2);
            assertTrue(columns.get(0).getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
            assertFalse(columns.get(1).getAttribute("class").contains(testedClass), "Item @class should not contain " + testedClass);
        }

        // test more classes than columns, redundant classes are ignored
        testedClass = "class1, class2, class3";
        attributes.set(OrderingListAttributes.columnClasses, testedClass);

        for (WebElement li : orderingList.advanced().getItemsElements()) {
            List<WebElement> columns = li.findElements(By.tagName("td"));
            assertEquals(columns.size(), 2);
            assertTrue(columns.get(0).getAttribute("class").contains("class1"), "Item @class should contain class1");
            assertFalse(columns.get(0).getAttribute("class").contains("class2"), "Item @class should not contain class2");
            assertFalse(columns.get(0).getAttribute("class").contains("class3"), "Item @class should not contain class3");
            assertFalse(columns.get(1).getAttribute("class").contains("class1"), "Item @class should not contain class1");
            assertTrue(columns.get(1).getAttribute("class").contains("class2"), "Item @class should contain class2");
            assertFalse(columns.get(1).getAttribute("class").contains("class3"), "Item @class should not contain class3");
        }
    }

    @Test
    @Templates(value = "plain")
    @IssueTracking("https://github.com/richwidgets/richwidgets/issues/135")
    public void testColumnClasses_repeating() {
        // test single class repeating
        String testedClass = "metamer-ftest-class";
        String testedClassRepeating = testedClass + ", *";
        attributes.set(OrderingListAttributes.columnClasses, testedClassRepeating);

        for (WebElement li : orderingList.advanced().getItemsElements()) {
            List<WebElement> columns = li.findElements(By.tagName("td"));
            assertEquals(columns.size(), 2);
            assertTrue(columns.get(0).getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
            assertTrue(columns.get(1).getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
        }
    }

    @Test
    @Templates(value = "plain")
    public void testDisabled() {
        attributes.set(OrderingListAttributes.disabled, Boolean.TRUE);
        List<WebElement> items = orderingList.advanced().getItemsElements();
        assertEquals(items.size(), 50, "List should contain 50 items");
        for (WebElement li : items) {
            assertTrue(li.getAttribute("class").contains("ui-disabled"), "Item @class should contain ui-disabled");
            assertFalse(li.getAttribute("class").contains("ui-selectee"), "Item @class should not contain ui-selectee");
        }
        try {
            orderingList.select(0);
            fail("The attribute <disabled> is set to true, but the ordering list is still enabled.");
        } catch (TimeoutException e) {
        }

        attributes.set(OrderingListAttributes.disabled, Boolean.FALSE);
        items = orderingList.advanced().getItemsElements();
        assertEquals(items.size(), 50, "List should contain 50 items");
        for (WebElement li : items) {
            assertFalse(li.getAttribute("class").contains("ui-disabled"), "Item @class should not contain ui-disabled");
            assertTrue(li.getAttribute("class").contains("ui-selectee"), "Item @class should contain ui-selectee");
        }
        orderingList.select(0);
        assertEquals(orderingList.advanced().getSelectedItemsElements().size(), 1);
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
    @IssueTracking("https://issues.jboss.org/browse/RF-13339")
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
    public void testListHeight() {
        checkHeightAttribute(OrderingListAttributes.listHeight, Boolean.FALSE);
        checkHeightAttribute(OrderingListAttributes.listHeight, Boolean.TRUE);
    }

    @Test
    public void testMaxListHeight_withUnit() {
        checkHeightAttribute(OrderingListAttributes.maxListHeight, Boolean.TRUE);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-13346")
    public void testMaxListHeight_withoutUnit() {
        checkHeightAttribute(OrderingListAttributes.maxListHeight, Boolean.FALSE);
    }

    @Test
    public void testMinListHeight_withUnit() {
        checkHeightAttribute(OrderingListAttributes.minListHeight, Boolean.TRUE);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-13346")
    public void testMinListHeight_withoutUnit() {
        checkHeightAttribute(OrderingListAttributes.minListHeight, Boolean.FALSE);
    }

    @Test
    public void testOnblur() {
        testFireEvent(attributes, OrderingListAttributes.onblur, new Action() {
            @Override
            public void perform() {
                Utils.triggerJQ(executor, "blur", orderingList.advanced().getItemsElements().get(0));
            }
        });
    }

    @Test
    public void testOnchange() {
        testFireEvent(attributes, OrderingListAttributes.onchange, new Action() {
            @Override
            public void perform() {
                orderingList.select(1).putItBefore(0);
            }
        });
    }

    @Test
    public void testOnclick() {
        testFireEvent(attributes, OrderingListAttributes.onclick,
            new Actions(driver).click(orderingList.advanced().getListAreaElement()).build());
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnfocus() {
        testFireEvent(attributes, OrderingListAttributes.onfocus,
            new Actions(driver).click(orderingList.advanced().getItemsElements().get(0)).build());
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, orderingList.advanced().getListAreaElement());
    }

    @Test
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
    @IssueTracking("https://issues.jboss.org/browse/RF-13340")
    public void testStyle() {
        testStyle(orderingList.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(orderingList.advanced().getRootElement());
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
    @IssueTracking({ "https://issues.jboss.org/browse/RF-13339", "https://issues.jboss.org/browse/RF-13340" })
    public void testValueChangeListener() {
        orderingList.select(0).putItAfter(ChoicePickerHelper.byIndex().last());
        submit();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");
    }
}
