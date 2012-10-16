/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.footer;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.header;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.noData;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.dataTableFacets;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22937 $
 */
public class TestCollapsibleSubTableFacets extends AbstractCollapsibleSubTableTest {

    private static final String SAMPLE_STRING = "Abc123!@#ĚščСам";
    private static final String EMPTY_STRING = "";

    @Override
    public URL getTestUrl() {

        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/facets.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Collapsible Sub Table", "Facets");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RFPL-1515")
    public void testNoDataFacet() {
        waitModel.until(new SeleniumCondition() {
            public boolean isTrue() {
                return subtable.hasVisibleRows();
            }
        });
        enableShowData(false);
        assertFalse(subtable.hasVisibleRows());

        JQueryLocator noDataFacet = pjq("table[id$=richDataTable] > tbody.rf-cst:eq(0) > tr.rf-cst-nd > td.rf-cst-nd-c");
        assertTrue(selenium.isElementPresent(noDataFacet));
        assertTrue(selenium.getText(noDataFacet).isEmpty());

        dataTableFacets.set(noData, SAMPLE_STRING);

        enableShowData(true);
        assertTrue(subtable.hasVisibleRows());

        enableShowData(false);
        assertFalse(subtable.hasVisibleRows());

        assertTrue(subtable.isNoData());
        assertEquals(selenium.getText(noDataFacet), SAMPLE_STRING);
    }

    @Test
    public void testHeaderInstantChange() {
        dataTableFacets.set(header, SAMPLE_STRING);
        assertEquals(selenium.getText(subtable.getHeader()), SAMPLE_STRING);

        dataTableFacets.set(header, EMPTY_STRING);
        if (selenium.isElementPresent(subtable.getHeader())) {
            assertEquals(selenium.getText(subtable.getHeader()), EMPTY_STRING);
        }

        dataTableFacets.set(header, SAMPLE_STRING);
        assertEquals(selenium.getText(subtable.getHeader()), SAMPLE_STRING);
    }

    @Test
    public void testFooterInstantChange() {
        dataTableFacets.set(footer, SAMPLE_STRING);
        assertEquals(selenium.getText(subtable.getFooter()), SAMPLE_STRING);

        dataTableFacets.set(footer, EMPTY_STRING);
        if (selenium.isElementPresent(subtable.getFooter())) {
            assertEquals(selenium.getText(subtable.getFooter()), EMPTY_STRING);
        }

        dataTableFacets.set(footer, SAMPLE_STRING);
        assertEquals(selenium.getText(subtable.getFooter()), SAMPLE_STRING);
    }
}
