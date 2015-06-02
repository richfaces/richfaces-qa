/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richList;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.list.RichFacesListItem;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestListAttributes extends AbstractListTest {

    private final Attributes<ListAttributes> listAttributes = getAttributes();

    private ListType type;

    private WebElement getTestedItem() {
        return list.getItems().get(0).getRootElement();
    }

    @Override
    public String getComponentTestPagePath() {
        return "richList/simple.xhtml";
    }

    @Test
    @CoversAttributes("dir")
    @Templates(value = "plain")
    public void testDir() {
        testDir(list.getRootElement());
    }

    @Test(groups = "smoke")
    @CoversAttributes("first")
    @UseWithField(field = "first", valuesFrom = FROM_FIELD, value = "INTS")
    public void testFirst() {
        listAttributes.set(ListAttributes.first, first);
        rows = 20;
        listAttributes.set(ListAttributes.rows, rows);
        verifyList();
    }

    @Test
    @CoversAttributes("lang")
    @Templates("plain")
    public void testLang() {
        testLang(list.getRootElement());
    }

    @Test
    @CoversAttributes("onclick")
    @Templates("plain")
    public void testOnclick() {
        testFireEvent(listAttributes, ListAttributes.onclick,
            new Actions(driver).click(list.getRootElement()).build());
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(listAttributes, ListAttributes.ondblclick,
            new Actions(driver).doubleClick(list.getRootElement()).build());
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEventWithJS(list.getRootElement(), listAttributes,
            ListAttributes.onkeydown);
    }

    @Test(groups = "smoke")
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEventWithJS(list.getRootElement(), listAttributes,
            ListAttributes.onkeypress);
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        testFireEventWithJS(list.getRootElement(), listAttributes,
            ListAttributes.onkeyup);

    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEventWithJS(list.getRootElement(), listAttributes,
            ListAttributes.onmousedown);
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEventWithJS(list.getRootElement(), listAttributes,
            ListAttributes.onmousemove);

    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEventWithJS(list.getRootElement(), listAttributes,
            ListAttributes.onmouseout);
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEventWithJS(list.getRootElement(), listAttributes,
            ListAttributes.onmouseover);
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEventWithJS(list.getRootElement(), listAttributes,
            ListAttributes.onmouseup);
    }

    @Test
    @CoversAttributes("onrowclick")
    @Templates(value = "plain")
    public void testOnrowclick() {
        testFireEvent(listAttributes, ListAttributes.onrowclick,
            new Actions(driver).click(getTestedItem()).build());
    }

    @Test
    @CoversAttributes("onrowdblclick")
    @Templates(value = "plain")
    public void testOnrowdblclick() {
        testFireEvent(listAttributes, ListAttributes.onrowdblclick,
            new Actions(driver).doubleClick(getTestedItem()).build());
    }

    @Test
    @CoversAttributes("onrowkeydown")
    @Templates(value = "plain")
    public void testOnrowkeydown() {
        testFireEventWithJS(getTestedItem(), Event.KEYDOWN, listAttributes,
            ListAttributes.onrowkeydown);
    }

    @Test
    @CoversAttributes("onrowkeypress")
    @Templates(value = "plain")
    public void testOnrowkeypress() {
        testFireEventWithJS(getTestedItem(), Event.KEYPRESS, listAttributes,
            ListAttributes.onrowkeypress);
    }

    @Test
    @CoversAttributes("onrowkeyup")
    @Templates(value = "plain")
    public void testOnrowkeyup() {
        testFireEventWithJS(getTestedItem(), Event.KEYUP, listAttributes,
            ListAttributes.onrowkeyup);
    }

    @Test
    @CoversAttributes("onrowmousedown")
    @Templates(value = "plain")
    public void testOnrowmousedown() {
        testFireEventWithJS(getTestedItem(), Event.MOUSEDOWN, listAttributes,
            ListAttributes.onrowmousedown);
    }

    @Test
    @CoversAttributes("onrowmousemove")
    @Templates(value = "plain")
    public void testOnrowmousemove() {
        testFireEventWithJS(getTestedItem(), Event.MOUSEMOVE, listAttributes,
            ListAttributes.onrowmousemove);

    }

    @Test
    @CoversAttributes("onrowmouseout")
    @Templates(value = "plain")
    public void testOnrowmouseout() {
        testFireEventWithJS(getTestedItem(), Event.MOUSEOUT, listAttributes,
            ListAttributes.onrowmouseout);
    }

    @Test
    @CoversAttributes("onrowmouseover")
    @Templates(value = "plain")
    public void testOnrowmouseover() {
        testFireEventWithJS(getTestedItem(), Event.MOUSEOVER, listAttributes,
            ListAttributes.onrowmouseover);
    }

    @Test
    @CoversAttributes("onrowmouseup")
    @Templates(value = "plain")
    public void testOnrowmouseup() {
        testFireEventWithJS(getTestedItem(), Event.MOUSEUP, listAttributes,
            ListAttributes.onrowmouseup);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        listAttributes.set(ListAttributes.rendered, Boolean.FALSE);
        assertNotVisible(list.getRootElement(), "List should not be visible");
    }

    @Test
    @CoversAttributes("rowClass")
    @Templates(value = "plain")
    public void testRowClass() {
        List<RichFacesListItem> items = list.getItems();
        testStyleClass(items.get(0).getRootElement(), BasicAttributes.rowClass);
        testStyleClass(items.get(items.size() - 1).getRootElement(), BasicAttributes.rowClass);
    }

    @Test
    @CoversAttributes("rowClasses")
    @Templates(value = "plain")
    public void testRowClasses() {
        List<RichFacesListItem> items = list.getItems();
        testStyleClass(items.get(0).getRootElement(), BasicAttributes.rowClasses);
        testStyleClass(items.get(items.size() - 1).getRootElement(), BasicAttributes.rowClasses);
    }

    @Test(groups = "smoke")
    @CoversAttributes("rows")
    @UseWithField(field = "rows", valuesFrom = FROM_FIELD, value = "INTS")
    public void testRows() {
        listAttributes.set(ListAttributes.rows, rows);
        verifyList();
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(list.getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(list.getRootElement());
    }

    @Test
    @CoversAttributes("title")
    @Templates("plain")
    public void testTitle() {
        testTitle(list.getRootElement());
    }

    @Test(groups = "smoke")
    @CoversAttributes("type")
    @UseWithField(field = "type", valuesFrom = FROM_ENUM, value = "")
    public void testType() {
        listAttributes.set(ListAttributes.type, type.getValue());
    }

    enum ListType {

        ORDERED("ordered"), UNORDERED("unordered"), DEFINITIONS("definitions");
        private final String value;

        private ListType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
