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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.dataScroller.DataScroller.DataScrollerSwitchButton;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFilteringTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.MultipleCoversAttributes;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.FilteringEDT;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class ExtendedDataTableFilteringTest extends DataTableFilteringTest {

    @FindByJQuery("div.rf-edt[id$=richEDT]")
    private FilteringEDT table;

    @Override
    protected FilteringEDT getTable() {
        return table;
    }

    @RegressionTest("https://issues.jboss.org/browse/RF-7872")
    @MultipleCoversAttributes({
        @CoversAttributes("showColumnControl"),
        @CoversAttributes(value = "name", attributeEnumClass = ColumnAttributes.class)
    })
    public void testShowColumnControlHideAllColumnsAndScroll() {
        setAttribute("showColumnControl", true);
        getTable().getHeader().openColumnControl().hideAllColumns();
        // check
        assertNotVisible(getTable().advanced().getTableBodyElement(), "Table body should not be visible");
        assertTrue(getTable().getHeader().openColumnControl().areAllColumnsHidden());

        // do some scrolling
        Graphene.guardAjax(dataScroller2).switchTo(DataScrollerSwitchButton.NEXT);
        // check
        assertNotVisible(getTable().advanced().getTableBodyElement(), "Table body should not be visible");
        assertTrue(getTable().getHeader().openColumnControl().areAllColumnsHidden());

        // do some scrolling
        Graphene.guardAjax(dataScroller2).switchTo(DataScrollerSwitchButton.LAST);
        Graphene.guardAjax(dataScroller2).switchTo(DataScrollerSwitchButton.PREVIOUS);
        // check
        assertNotVisible(getTable().advanced().getTableBodyElement(), "Table body should not be visible");
        assertTrue(getTable().getHeader().openColumnControl().areAllColumnsHidden());
    }

    @RegressionTest("https://issues.jboss.org/browse/RF-7872")
    @MultipleCoversAttributes({
        @CoversAttributes("showColumnControl"),
        @CoversAttributes(value = "name", attributeEnumClass = ColumnAttributes.class)
    })
    public void testShowColumnControlWithFiltering(boolean isBuiltIn) {
        // enable column control
        setAttribute("showColumnControl", true);
        // hide column with job title
        getTable().getHeader().openColumnControl().hideColumn(ChoicePickerHelper.byVisibleText().contains("title"));

        // filter by name
        getTable().getHeader().filterName("ivan", isBuiltIn);
        getFilteredEmployeeModel().setName("ivan");

        // performs verification across multiple pages, with checking for column visibility
        verifyFiltering(Sets.newHashSet(2));// 2 = column with title
        // check column visibility stays unchecked
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("title")));

        // clear filtering
        getTable().getHeader().filterName("", isBuiltIn);
        getFilteredEmployeeModel().setName("");
        // check column visibility stays unchecked
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("title")));

        // performs verification across multiple pages, with checking for column visibility
        verifyFiltering(Sets.newHashSet(2));// 2 = column with title
        // check column visibility stays unchecked
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("title")));

        // filter by # of kids >=2
        if (isBuiltIn) {
            getTable().getHeader().filterNumberOfKidsBuiltIn(2);
        } else {
            getTable().getHeader().filterNumberOfKidsWithSpinner(2);
        }
        getFilteredEmployeeModel().setNumberOfKids1(2);
        // check column visibility stays unchecked
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("title")));

        // performs verification across multiple pages, with checking for column visibility
        verifyFiltering(Sets.newHashSet(2));// 2 = column with title
        // check column visibility stays unchecked
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("title")));

        // hide column with name
        getTable().getHeader().openColumnControl().hideColumn(ChoicePickerHelper.byVisibleText().contains("name"));
        // performs verification across multiple pages, with checking for column visibility
        verifyFiltering(Sets.newHashSet(1, 2));// 1 = column with name, 2 = column with title
        // check column visibility stays unchecked
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("name")));
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("title")));
    }
}
