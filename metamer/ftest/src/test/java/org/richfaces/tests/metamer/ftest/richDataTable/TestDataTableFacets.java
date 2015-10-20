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
package org.richfaces.tests.metamer.ftest.richDataTable;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacetsTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SimpleDT;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@Templates("plain")
public class TestDataTableFacets extends DataTableFacetsTest {

    private final Attributes<DataTableFacets> dataTableFacets = getAttributes();

    private static final String HEADER_FACET_TEXT_TEMPLATE = "Header facet row 1 col1 ({0})";

    @FindBy(css = "table.rf-dt[id$=richDataTable]")
    private SimpleDT table;

    @FindBy(css = "table.rf-dt[id$=richDataTable]")
    private WebElement tableRoot;

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/facets.xhtml";
    }

    @Override
    protected SimpleDT getTable() {
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
    @CoversAttributes("captionClass")
    public void testCaptionClass() {
        testStyleClass(tableRoot.findElement(By.tagName("caption")), BasicAttributes.captionClass);
    }

    @Test
    @CoversAttributes("cellClass")
    public void testCellClass() {
        testTableStyleClass("cellClass", "rf-dt-c", table.getAllRows().size() * 2);// 2 columns
    }

    @Test
    @CoversAttributes("columnFooterCellClass")
    public void testColumnFooterCellClass() {
        testTableStyleClass("columnFooterCellClass", "rf-dt-sftr-c", 2);// 2 columns
    }

    @Test
    @CoversAttributes("columnFooterClass")
    public void testColumnFooterClass() {
        testTableStyleClass("columnFooterClass", "rf-dt-sftr", 1);// 1 row
    }

    @Test
    @CoversAttributes("columnHeaderCellClass")
    public void testColumnHeaderCellClass() {
        testTableStyleClass("columnHeaderCellClass", "rf-dt-shdr-c", 2);// 2 columns
    }

    @Test
    @CoversAttributes("columnHeaderClass")
    public void testColumnHeaderClass() {
        testTableStyleClass("columnHeaderClass", "rf-dt-shdr", 1);// 1 row
    }

    @Test
    @CoversAttributes("firstRowClass")
    public void testFirstRowClass() {
        String klass = "metamer-ftest-class";
        setAttribute("firstRowClass", "metamer-ftest-class");
        WebElement row = table.getFirstRow().getRootElement();
        assertTrue(row.getAttribute("class").contains(klass));
    }

    @Test
    @CoversAttributes("footerCellClass")
    public void testFooterCellClass() {
        testTableStyleClass("footerCellClass", "rf-dt-ftr-c", 3);// 3 cells
    }

    @Test
    @CoversAttributes("footerClass")
    public void testFooterClass() {
        testStyleClass(table.getFooter().getTableFooterElement(), BasicAttributes.footerClass);
    }

    @Test
    @CoversAttributes("headerCellClass")
    public void testHeaderCellClass() {
        testTableStyleClass("headerCellClass", "rf-dt-hdr-c", 3);// 3 cells
    }

    @Test
    @CoversAttributes("headerClass")
    public void testHeaderClass() {
        testStyleClass(table.getHeader().getTableHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testHeaderFacet() {
        dataTableFacets.set(DataTableFacets.header, SAMPLE_STRING);
        assertEquals(getTable().getHeader().getHeaderText(), format(HEADER_FACET_TEXT_TEMPLATE, SAMPLE_STRING));

        dataTableFacets.set(DataTableFacets.header, EMPTY_STRING);
        if (new WebElementConditionFactory(getTable().advanced().getHeaderElement()).isPresent().apply(driver)) {
            assertEquals(getTable().advanced().getHeaderElement().getText(), EMPTY_STRING);
        } else {
            dataTableFacets.set(DataTableFacets.header, SAMPLE_STRING);
            assertEquals(getTable().advanced().getHeaderElement().getText(), format(HEADER_FACET_TEXT_TEMPLATE, SAMPLE_STRING));
        }
    }

    @Test
    public void testNoDataFacet() {
        super.testNoDataFacet();
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
