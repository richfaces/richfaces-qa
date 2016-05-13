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

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SortingDT;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestDataTableJSAPIFilteringAndSorting extends DataTableSortingTest {

    private final Attributes<DataTableAttributes> attributes = getAttributes();

    @FindBy(css = "[id$=clearFiltering]")
    private WebElement clearFilteringButton;
    @FindBy(css = "[id$=clearSorting]")
    private WebElement clearSortingButton;
    @FindBy(css = "[id$=filter]")
    private WebElement filterButton;
    @FindBy(css = "[id$=sortDescendingKids]")
    private WebElement sortDescendingKidsButton;
    @FindBy(css = "[id$=sortDescendingName]")
    private WebElement sortDescendingNameResetSortingButton;
    @FindBy(css = "table.rf-dt[id$=richDataTable]")
    private SortingDT table;

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/builtInFilteringAndSorting.xhtml";
    }

    @Override
    public SortingDT getTable() {
        return table;
    }

    @Test
    public void testFilterAndClearFiltering() {
        assertEquals(getTable().getAllRows().size(), 10);
        getMetamerPage().performJSClickOnButton(filterButton, WaitRequestType.XHR);
        assertEquals(getTable().getAllRows().size(), 1);
        assertEquals(getTable().getFirstRow().getNameColumnValue(), "Milan Rastislav Štefánik");

        getMetamerPage().performJSClickOnButton(clearFilteringButton, WaitRequestType.XHR);
        assertEquals(getTable().getAllRows().size(), 10);
    }

    @Test
    @Templates("plain")// similar tests in TestDataTableSortingBuiltIn
    public void testSortAndClearSorting() {
        attributes.set(DataTableAttributes.sortMode, "single");
        getMetamerPage().performJSClickOnButton(sortDescendingKidsButton, WaitRequestType.XHR);
        verifySortingByColumns("numberOfKids-");
        getMetamerPage().performJSClickOnButton(sortDescendingNameResetSortingButton, WaitRequestType.XHR);
        verifySortingByColumns("name-");
        // verify that only single sorting can be performed sith @sortMode=single
        getMetamerPage().performJSClickOnButton(sortDescendingKidsButton, WaitRequestType.XHR);
        verifySortingByColumns("numberOfKids-");

        attributes.set(DataTableAttributes.sortMode, "multi");
        getMetamerPage().performJSClickOnButton(sortDescendingNameResetSortingButton, WaitRequestType.XHR);
        getMetamerPage().performJSClickOnButton(sortDescendingKidsButton, WaitRequestType.XHR);
        verifySortingByColumns("name-", "numberOfKids-");

        getMetamerPage().performJSClickOnButton(clearSortingButton, WaitRequestType.XHR);
        verifySortingByColumns();
    }
}
