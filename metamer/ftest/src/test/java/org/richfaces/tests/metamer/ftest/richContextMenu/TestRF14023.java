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
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.contextMenu.PopupMenuGroup;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SimpleEDT;
import org.richfaces.tests.metamer.ftest.richSelect.TestRF14018.JSErrorStorage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14023 extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=contextMenu]")
    private RichFacesContextMenu contextMenu;
    @FindBy(css = "div.rf-edt[id$=edt]")
    private SimpleEDT edt;
    @JavaScript
    private JSErrorStorage jsErrorStorage;
    @FindBy(css = "[id$=output]")
    private WebElement output;

    private void checkNoJSErrorsArePresent() {
        assertEquals(jsErrorStorage.getMessages().size(), 0);
    }

    @Override
    public String getComponentTestPagePath() {
        return "richContextMenu/rf-14023.xhtml";
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-14023", "https://issues.jboss.org/browse/RF-14156" })
    public void testDynamicContextMenuAppearsAfterAjaxUpdateInEDT() {
        checkNoJSErrorsArePresent();
        // select first row (this should be enough to prevent context menu from showing -- RF-14023)
        WebElement menuTarget = edt.getFirstRow().getStateColumn();
        Graphene.guardAjax(menuTarget).click();
        // show context menu on first row and select an item
        Graphene.guardAjax(contextMenu.expandGroup(0, menuTarget)).selectItem(0);
        // check output
        assertEquals(output.getText(), "Montgomery (Alabama)");
        checkNoJSErrorsArePresent();

        // select third row
        Graphene.guardAjax(edt.getRow(2).getStateColumn()).click();
        checkNoJSErrorsArePresent();

        // select multiple rows (third row remains selected from previous step)
        for (int index : new Integer[] { 0, 5, 10, 13 }) {
            jsUtils.scrollToView(edt.getRow(index).getRootElement());
            edt.selectRow(index, Keys.CONTROL);
        }
        checkNoJSErrorsArePresent();
        // show context menu on third row
        jsUtils.scrollToView(edt.getRow(2).getRootElement());
        menuTarget = edt.getRow(2).getStateColumn();
        PopupMenuGroup expandedGroup = contextMenu.expandGroup(0, menuTarget);
        assertEquals(contextMenu.advanced().getItemsElements().size(), 6);// 5 items + 1 group
        // select third item
        Graphene.guardAjax(expandedGroup).selectItem(2);
        // check output
        assertEquals(output.getText(), "Denver (Colorado)");
        checkNoJSErrorsArePresent();
    }
}
