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

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFilteringTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.MultipleCoversAttributes;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.FilteringDT;
import org.testng.annotations.Test;

public class TestDataTableFilteringBuiltIn extends DataTableFilteringTest {

    private final Action ajaxAction = new Action() {
        @Override
        public void perform() {
            table.getHeader().filterNameBuiltIn("a");
        }
    };

    @FindByJQuery(value = "table.rf-dt[id$=richDataTable]")
    private FilteringDT table;

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/builtInFilteringAndSorting.xhtml";
    }

    @Override
    protected FilteringDT getTable() {
        return table;
    }

    @Test
    @MultipleCoversAttributes({
        @CoversAttributes("filterVar"),
        @CoversAttributes(value = { "filterExpression", "filterType", "filterValue" }, attributeEnumClass = ColumnAttributes.class)
    })
    public void testCombination() {
        super.testFilterCombinations(true);
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(ajaxAction);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testFilterName() {
        super.testFilterName(true);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14150")
    public void testFilterNameBuiltInAppliesAfterEnterPressed() {
        super.testFilterNameBuiltInAppliesAfterEnterPressed();
    }

    @Test
    @CoversAttributes("filterVar")
    public void testFilterTitle() {
        super.testFilterTitle(true);
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        testLimitRender(ajaxAction);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testNumberOfKids() {
        super.testFilterNumberOfKindsBuiltIn();
    }

    @Test
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        testFireEvent("onbeforedomupdate", ajaxAction);
    }

    @Test
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        testFireEvent("oncomplete", ajaxAction);
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        testRender(ajaxAction);
    }
}
