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
package org.richfaces.tests.metamer.ftest.richDataTable;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.richCollapsibleSubTable.AbstractCollapsibleSubTableTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestDataTableJSAPICollapseExpand extends AbstractCollapsibleSubTableTest {

    private static final boolean FEMALE = Boolean.FALSE;
    private static final boolean MALE = Boolean.TRUE;

    @FindBy(css = "[id$=collapseAllSubTables]")
    private WebElement collapseAllSubTablesButton;
    @FindBy(css = "[id$=expandAllSubTables]")
    private WebElement expandAllSubTablesButton;
    @FindBy(css = "[id$=switchSubTableMen]")
    private WebElement switchSubTableMenButton;
    @FindBy(css = "[id$=switchSubTableWomen]")
    private WebElement switchSubTableWomenButton;

    @Override
    public String getComponentTestPagePath() {
        return "richCollapsibleSubTable/simple.xhtml";
    }

    @Test
    public void testCollapseAllExpandAll() {
        assertTrue(getSubTable(MALE).advanced().isExpanded());
        assertTrue(getSubTable(FEMALE).advanced().isExpanded());
        getMetamerPage().performJSClickOnButton(collapseAllSubTablesButton, WaitRequestType.NONE);
        assertFalse(getSubTable(MALE).advanced().isExpanded());
        assertFalse(getSubTable(FEMALE).advanced().isExpanded());
        getMetamerPage().performJSClickOnButton(expandAllSubTablesButton, WaitRequestType.NONE);
        assertTrue(getSubTable(MALE).advanced().isExpanded());
        assertTrue(getSubTable(FEMALE).advanced().isExpanded());
    }

    @Test
    public void testSwitchSubTable() {
        assertTrue(getSubTable(MALE).advanced().isExpanded());
        assertTrue(getSubTable(FEMALE).advanced().isExpanded());
        getMetamerPage().performJSClickOnButton(switchSubTableMenButton, WaitRequestType.NONE);
        assertFalse(getSubTable(MALE).advanced().isExpanded());
        assertTrue(getSubTable(FEMALE).advanced().isExpanded());
        getMetamerPage().performJSClickOnButton(switchSubTableMenButton, WaitRequestType.NONE);
        assertTrue(getSubTable(MALE).advanced().isExpanded());
        assertTrue(getSubTable(FEMALE).advanced().isExpanded());
        getMetamerPage().performJSClickOnButton(switchSubTableMenButton, WaitRequestType.NONE);
        getMetamerPage().performJSClickOnButton(switchSubTableWomenButton, WaitRequestType.NONE);
        assertFalse(getSubTable(MALE).advanced().isExpanded());
        assertFalse(getSubTable(FEMALE).advanced().isExpanded());
    }
}
