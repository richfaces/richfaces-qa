/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.abstractions;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.richfaces.fragment.dataTable.RichFacesDataTableWithHeaderAndFooter;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.capitalFooter;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.capitalHeader;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.header;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.noData;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.stateFooter;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.stateHeader;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleFooterInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleHeaderInterface;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

import static org.testng.Assert.assertEquals;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class DataTableFacetsTest extends AbstractDataTableTest {

    private static final String SAMPLE_STRING = "Abc123!@#ĚščСам";
    private static final String EMPTY_STRING = "";

    private final Attributes<DataTableFacets> dataTableFacets = getAttributes();

    public void testNoDataInstantChange(RichFacesDataTableWithHeaderAndFooter<?,?,?> table) {
        enableShowData(false);
        dataTableFacets.set(noData, SAMPLE_STRING);
        assertEquals(table.advanced().getNoDataElement().getText(), SAMPLE_STRING);
    }

    public void testNoDataEmpty(RichFacesDataTableWithHeaderAndFooter<?,?,?> table) {
        enableShowData(false);
        dataTableFacets.set(noData, EMPTY_STRING);
        assertEquals(table.advanced().getNoDataElement().getText(), EMPTY_STRING);
    }

    public void testHeaderInstantChange(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(header, SAMPLE_STRING);
        assertEquals(table.getHeader().getHeaderText(), SAMPLE_STRING);
    }

    public void testHeaderEmpty(RichFacesDataTableWithHeaderAndFooter<?,?,?> table) {
        dataTableFacets.set(header, EMPTY_STRING);

        if (new WebElementConditionFactory(table.advanced().getHeaderElement()).isPresent().apply(driver)) {
            assertEquals(table.advanced().getHeaderElement().getText(), EMPTY_STRING);
        } else {
            dataTableFacets.set(header, SAMPLE_STRING);
            assertEquals(table.advanced().getHeaderElement().getText(), SAMPLE_STRING);
        }
    }

    public void testStateHeaderInstantChange(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(stateHeader, SAMPLE_STRING);
        assertEquals(table.getHeader().getColumnHeaderText(COLUMN_STATE), SAMPLE_STRING);
    }

    public void testStateHeaderEmpty(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(stateHeader, EMPTY_STRING);
        assertEquals(table.getHeader().getColumnHeaderText(COLUMN_STATE), EMPTY_STRING);
    }

    public void testStateFooterInstantChange(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(stateFooter, SAMPLE_STRING);
        assertEquals(table.getFooter().getColumnFooterText(COLUMN_STATE), SAMPLE_STRING);
    }

    public void testStateFooterEmpty(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(stateFooter, EMPTY_STRING);
        assertEquals(table.getFooter().getColumnFooterText(COLUMN_STATE), EMPTY_STRING);
    }

    public void testCapitalHeaderInstantChange(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(capitalHeader, SAMPLE_STRING);
        assertEquals(table.getHeader().getColumnHeaderText(COLUMN_CAPITAL), SAMPLE_STRING);
    }

    public void testCapitalHeaderEmpty(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(capitalHeader, EMPTY_STRING);
        assertEquals(table.getHeader().getColumnHeaderText(COLUMN_CAPITAL), EMPTY_STRING);
    }

    public void testCapitalFooterInstantChange(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(capitalFooter, SAMPLE_STRING);
        assertEquals(table.getFooter().getColumnFooterText(COLUMN_CAPITAL), SAMPLE_STRING);
    }

    public void testCapitalFooterEmpty(RichFacesDataTableWithHeaderAndFooter<? extends SimpleHeaderInterface,?,? extends SimpleFooterInterface> table) {
        dataTableFacets.set(capitalFooter, EMPTY_STRING);
        assertEquals(table.getFooter().getColumnFooterText(COLUMN_CAPITAL), EMPTY_STRING);
    }
}