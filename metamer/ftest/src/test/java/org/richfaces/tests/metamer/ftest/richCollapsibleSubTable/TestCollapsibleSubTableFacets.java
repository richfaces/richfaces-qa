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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
public class TestCollapsibleSubTableFacets extends AbstractCollapsibleSubTableTest {

    private static final String EMPTY_STRING = "";
    private static final String SAMPLE_STRING = "Abc123!@#ĚščСам";

    private final Attributes<FacetsAttributes> dataTableFacets = getAttributes();

    @Override
    public String getComponentTestPagePath() {
        return "richCollapsibleSubTable/facets.xhtml";
    }

    @Test
    public void testBirthdayFooterFacet() {
        testFacet(FacetsAttributes.birthdateFooter);
    }

    @Test
    public void testBirthdayHeaderFacet() {
        testFacet(FacetsAttributes.birthdateHeader);
    }

    @Test
    @CoversAttributes("cellClass")
    public void testCellClass() {
        testTableStyleClass("cellClass", "rf-cst-c", (getSubTable(isMale).getAllRows().size()
            + getSubTable(!isMale).getAllRows().size()) * 3);// 3 columns
    }

    @Test
    @CoversAttributes("columnFooterCellClass")
    public void testColumnFooterCellClass() {
        testTableStyleClass("columnFooterCellClass", "rf-cst-sftr-c", 2 * 3);// 2 tables, 3 columns
    }

    @Test
    @CoversAttributes("columnFooterClass")
    public void testColumnFooterClass() {
        testTableStyleClass("columnFooterClass", "rf-cst-sftr", 2);// 2 tables
    }

    @Test
    @CoversAttributes("columnHeaderCellClass")
    public void testColumnHeaderCellClass() {
        testTableStyleClass("columnHeaderCellClass", "rf-cst-shdr-c", 2 * 3);// 2 tables, 3 columns
    }

    @Test
    @CoversAttributes("columnHeaderClass")
    public void testColumnHeaderClass() {
        testTableStyleClass("columnHeaderClass", "rf-cst-shdr", 2);// 2 tables
    }

    private void testFacet(FacetsAttributes attribute) {
        dataTableFacets.set(attribute, SAMPLE_STRING);
        WebElement element1, element2;
        switch (attribute) {
            case footer:
                element1 = getSubTable(isMale).advanced().getFooterElement();
                element2 = getSubTable(!isMale).advanced().getFooterElement();
                break;
            case header:
                element1 = getSubTable(isMale).advanced().getHeaderElement();
                element2 = getSubTable(!isMale).advanced().getHeaderElement();
                break;
            case birthdateFooter:
                element1 = getSubTable(isMale).advanced().getColumnFooterElement(2);
                element2 = getSubTable(!isMale).advanced().getColumnFooterElement(2);
                break;
            case birthdateHeader:
                element1 = getSubTable(isMale).advanced().getColumnHeaderElement(2);
                element2 = getSubTable(!isMale).advanced().getColumnHeaderElement(2);
                break;
            case nameFooter:
                element1 = getSubTable(isMale).advanced().getColumnFooterElement(0);
                element2 = getSubTable(!isMale).advanced().getColumnFooterElement(0);
                break;
            case nameHeader:
                element1 = getSubTable(isMale).advanced().getColumnHeaderElement(0);
                element2 = getSubTable(!isMale).advanced().getColumnHeaderElement(0);
                break;
            case titleFooter:
                element1 = getSubTable(isMale).advanced().getColumnFooterElement(1);
                element2 = getSubTable(!isMale).advanced().getColumnFooterElement(1);
                break;
            case titleHeader:
                element1 = getSubTable(isMale).advanced().getColumnHeaderElement(1);
                element2 = getSubTable(!isMale).advanced().getColumnHeaderElement(1);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported option " + attribute);
        }

        assertEquals(element1.getText(), SAMPLE_STRING);
        assertEquals(element2.getText(), SAMPLE_STRING);

        dataTableFacets.set(attribute, EMPTY_STRING);

        try {
            String s = element1.getText();// this should throw NoSuchElementException or return an empty String
            if (!s.equals(EMPTY_STRING)) {
                fail(format("The table should not have <{0}> facet element.", attribute));
            }
        } catch (NoSuchElementException ex) {
        }
        try {
            String s = element2.getText();// this should throw NoSuchElementException or return an empty String
            if (!s.equals(EMPTY_STRING)) {
                fail(format("The table should not have <{0}> facet element.", attribute));
            }
        } catch (NoSuchElementException ex) {
        }
        dataTableFacets.set(attribute, SAMPLE_STRING);

        assertEquals(element1.getText(), SAMPLE_STRING);
        assertEquals(element2.getText(), SAMPLE_STRING);
    }

    @Test
    @CoversAttributes("firstRowClass")
    public void testFirstRowClass() {
        String klass = "metamer-ftest-class";
        setAttribute("firstRowClass", "metamer-ftest-class");
        WebElement row = getSubTable(isMale).getFirstRow().getRootElement();
        WebElement row2 = getSubTable(!isMale).getFirstRow().getRootElement();
        assertTrue(row.getAttribute("class").contains(klass));
        assertTrue(row2.getAttribute("class").contains(klass));
    }

    @Test
    @CoversAttributes("footerCellClass")
    public void testFooterCellClass() {
        testTableStyleClass("footerCellClass", "rf-cst-ftr-c", 2);// 2 tables
    }

    @Test
    @CoversAttributes("footerClass")
    public void testFooterClass() {
        dataTableFacets.set(FacetsAttributes.footer, SAMPLE_STRING);
        testStyleClass(getSubTable(isMale).advanced().getFooterElement(), BasicAttributes.footerClass);
        testStyleClass(getSubTable(!isMale).advanced().getFooterElement(), BasicAttributes.footerClass);
    }

    @Test
    public void testFooterFacet() {
        testFacet(FacetsAttributes.footer);
    }

    @Test
    @CoversAttributes("headerCellClass")
    public void testHeaderCellClass() {
        testTableStyleClass("headerCellClass", "rf-cst-hdr-c", 2);// 2 tables
    }

    @Test
    @CoversAttributes("headerClass")
    public void testHeaderClass() {
        dataTableFacets.set(FacetsAttributes.header, SAMPLE_STRING);
        testStyleClass(getSubTable(isMale).advanced().getHeaderElement(), BasicAttributes.headerClass);
        testStyleClass(getSubTable(!isMale).advanced().getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testHeaderFacet() {
        testFacet(FacetsAttributes.header);
    }

    @Test
    public void testNameFooterFacet() {
        testFacet(FacetsAttributes.nameFooter);
    }

    @Test
    public void testNameHeaderFacet() {
        testFacet(FacetsAttributes.nameHeader);
    }

    @Test
    public void testNoColumnFacets() {
        dataTableFacets.set(FacetsAttributes.birthdateFooter, EMPTY_STRING);
        dataTableFacets.set(FacetsAttributes.nameFooter, EMPTY_STRING);
        dataTableFacets.set(FacetsAttributes.titleFooter, EMPTY_STRING);
        assertEquals(getSubTable(isMale).advanced().getColumnFooterElements().size(), 0, "There should be no footer facets!");
        assertEquals(getSubTable(!isMale).advanced().getColumnFooterElements().size(), 0, "There should be no footer facets!");

        dataTableFacets.set(FacetsAttributes.birthdateHeader, EMPTY_STRING);
        dataTableFacets.set(FacetsAttributes.nameHeader, EMPTY_STRING);
        dataTableFacets.set(FacetsAttributes.titleHeader, EMPTY_STRING);
        assertEquals(getSubTable(isMale).advanced().getColumnHeaderElements().size(), 0, "There should be no header facets!");
        assertEquals(getSubTable(!isMale).advanced().getColumnHeaderElements().size(), 0, "There should be no header facets!");
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1515", "https://issues.jboss.org/browse/RF-12672" })
    public void testNoDataFacet() {
        showDataInTable(false);
        dataTableFacets.set(FacetsAttributes.noData, EMPTY_STRING);
        assertEquals(getSubTable(isMale).advanced().getNumberOfVisibleRows(), 0);
        assertEquals(getSubTable(isMale).advanced().getNumberOfVisibleRows(), 0);
        assertFalse(Utils.isVisible(getSubTable(isMale).advanced().getNoDataElement()));
        assertFalse(Utils.isVisible(getSubTable(!isMale).advanced().getNoDataElement()));

        dataTableFacets.set(FacetsAttributes.noData, SAMPLE_STRING);
        assertEquals(getSubTable(isMale).advanced().getNumberOfVisibleRows(), 0);
        assertEquals(getSubTable(!isMale).advanced().getNumberOfVisibleRows(), 0);
        assertTrue(Utils.isVisible(getSubTable(isMale).advanced().getNoDataElement()));
        assertTrue(Utils.isVisible(getSubTable(!isMale).advanced().getNoDataElement()));
        assertEquals(getSubTable(isMale).advanced().getNoDataElement().getText(), SAMPLE_STRING);
        assertEquals(getSubTable(!isMale).advanced().getNoDataElement().getText(), SAMPLE_STRING);

        showDataInTable(true);
        try {
            getSubTable(isMale).advanced().getNoDataElement();
            fail("There should be no noData element now");
        } catch (NoSuchElementException e) {
        }
        assertTrue(getSubTable(isMale).advanced().isVisible());
        assertEquals(getSubTable(isMale).advanced().getNumberOfVisibleRows(), 5);
        try {
            getSubTable(!isMale).advanced().getNoDataElement();
            fail("There should be no noData element now");
        } catch (NoSuchElementException e) {
        }
        assertTrue(getSubTable(!isMale).advanced().isVisible());
        assertEquals(getSubTable(!isMale).advanced().getNumberOfVisibleRows(), 5);
    }

    @Test
    public void testTitleFooterFacet() {
        testFacet(FacetsAttributes.titleFooter);
    }

    @Test
    public void testTitleHeaderFacet() {
        testFacet(FacetsAttributes.titleHeader);
    }

    private enum FacetsAttributes implements AttributeEnum {

        birthdateFooter, birthdateHeader, footer, header, nameFooter, nameHeader, noData, titleFooter, titleHeader
    }
}
