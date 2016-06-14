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

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacetsTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.MultipleCoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SimpleEDTWithColumnControl;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates(value = "plain")
public class TestExtendedDataTableFacets extends DataTableFacetsTest {

    private static final int NUMBER_OF_COMBINATIONS = 32;

    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private SimpleEDTWithColumnControl table;

    private void checkColumnControlsLabels(String firstLabel, String secondLabel) {
        assertEquals(getTable().getHeader().openColumnControl().getOptionsLabels(), Lists.newArrayList(firstLabel, secondLabel));
    }

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/facets.xhtml";
    }

    @Override
    protected SimpleEDTWithColumnControl getTable() {
        return table;
    }

    @Test
    public void testCapitalFooterFacet() {
        super.testCapitalFooterFacet();
    }

    @Test
    public void testCapitalHeaderFacet() {
        super.testCapitalHeaderFacet();
    }

    @Test
    @CoversAttributes("footerClass")
    public void testFooterClass() {
        testStyleClass(table.getFooter().getTableFooterElement(), BasicAttributes.footerClass);
    }

    @Test
    @CoversAttributes("headerClass")
    public void testHeaderClass() {
        testStyleClass(table.getHeader().getTableHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testHeaderFacet() {
        super.testHeaderFacet();
    }

    @Test
    public void testNoDataFacet() {
        super.testNoDataFacet();
    }

    @Test
    @MultipleCoversAttributes({
        @CoversAttributes("showColumnControl"),
        @CoversAttributes(value = "name", attributeEnumClass = ColumnAttributes.class)
    })
    @IssueTracking("https://issues.jboss.org/browse/RF-7872")
    public void testShowColumnControlsLabels() {
        setAttribute("showColumnControl", true);
        boolean hasColumnName = false, hasStateHeader = false, hasStateFooter = false, hasCapitalHeader = false,
            hasCapitalFooter = false;
        String columnName, stateHeader, stateFooter, capitalHeader, capitalFooter;
        for (int i = 0; i < NUMBER_OF_COMBINATIONS; i++) {
            hasColumnName = !hasColumnName;
            hasStateHeader = i % 2 == 0 ? !hasStateHeader : hasStateHeader;
            hasStateFooter = i % 4 == 0 ? !hasStateFooter : hasStateFooter;
            hasCapitalHeader = i % 8 == 0 ? !hasCapitalHeader : hasCapitalHeader;
            hasCapitalFooter = i % 16 == 0 ? !hasCapitalFooter : hasCapitalFooter;
            columnName = hasColumnName ? "name-" + SAMPLE_STRING : "";
            stateHeader = hasStateHeader ? "header1-" + SAMPLE_STRING : "";
            stateFooter = hasStateFooter ? "footer1-" + SAMPLE_STRING : "";
            capitalHeader = hasCapitalHeader ? "header2-" + SAMPLE_STRING : "";
            capitalFooter = hasCapitalFooter ? "footer2-" + SAMPLE_STRING : "";

            attsSetter()
                .setAttribute("stateColumnName").toValue(columnName)
                .setAttribute("stateHeader").toValue(stateHeader)
                .setAttribute("stateFooter").toValue(stateFooter)
                .setAttribute("capitalHeader").toValue(capitalHeader)
                .setAttribute("capitalFooter").toValue(capitalFooter)
                .asSingleAction().perform();

            checkColumnControlsLabels(
                hasColumnName ? columnName : hasStateHeader ? stateHeader : (hasStateFooter && !hasCapitalHeader) ? stateFooter : "#columnState",
                hasCapitalHeader ? capitalHeader : (hasCapitalFooter && !hasStateHeader) ? capitalFooter : "#columnCapital"
            );
        }
    }

    @Test
    public void testStateFooterFacet() {
        super.testStateFooterFacet();
    }

    @Test
    public void testStateHeaderFacet() {
        super.testStateHeaderFacet();
    }
}
