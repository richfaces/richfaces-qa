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

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.richfaces.fragment.dataTable.AbstractTable;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleFooterInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleHeaderInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleRowInterface;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class DataTableFacetsTest extends AbstractDataTableTest {

    private static final String SAMPLE_STRING = "Abc123!@#ĚščСам";
    private static final String EMPTY_STRING = "";

    private final Attributes<DataTableFacets> dataTableFacets = getAttributes();

    protected abstract AbstractTable<? extends SimpleHeaderInterface, ? extends SimpleRowInterface, ? extends SimpleFooterInterface> getTable();

    public void testNoDataFacet() {
        enableShowData(false);
        dataTableFacets.set(DataTableFacets.noData, SAMPLE_STRING);
        assertEquals(getTable().advanced().getNoDataElement().getText(), SAMPLE_STRING);

        dataTableFacets.set(DataTableFacets.noData, EMPTY_STRING);
        assertEquals(getTable().advanced().getNoDataElement().getText(), EMPTY_STRING);
    }

    public void testHeaderFacet() {
        dataTableFacets.set(DataTableFacets.header, SAMPLE_STRING);
        assertEquals(getTable().getHeader().getHeaderText(), SAMPLE_STRING);

        dataTableFacets.set(DataTableFacets.header, EMPTY_STRING);
        if (new WebElementConditionFactory(getTable().advanced().getHeaderElement()).isPresent().apply(driver)) {
            assertEquals(getTable().advanced().getHeaderElement().getText(), EMPTY_STRING);
        } else {
            dataTableFacets.set(DataTableFacets.header, SAMPLE_STRING);
            assertEquals(getTable().advanced().getHeaderElement().getText(), SAMPLE_STRING);
        }
    }

    public void testStateHeaderFacet() {
        dataTableFacets.set(DataTableFacets.stateHeader, SAMPLE_STRING);
        assertEquals(getTable().getHeader().getColumnHeaderText(COLUMN_STATE), SAMPLE_STRING);

        dataTableFacets.set(DataTableFacets.stateHeader, EMPTY_STRING);
        assertEquals(getTable().getHeader().getColumnHeaderText(COLUMN_STATE), EMPTY_STRING);
    }

    public void testStateFooterFacet() {
        dataTableFacets.set(DataTableFacets.stateFooter, SAMPLE_STRING);
        assertEquals(getTable().getFooter().getColumnFooterText(COLUMN_STATE), SAMPLE_STRING);

        dataTableFacets.set(DataTableFacets.stateFooter, EMPTY_STRING);
        assertEquals(getTable().getFooter().getColumnFooterText(COLUMN_STATE), EMPTY_STRING);
    }

    public void testCapitalHeaderFacet() {
        dataTableFacets.set(DataTableFacets.capitalHeader, SAMPLE_STRING);
        assertEquals(getTable().getHeader().getColumnHeaderText(COLUMN_CAPITAL), SAMPLE_STRING);

        dataTableFacets.set(DataTableFacets.capitalHeader, EMPTY_STRING);
        assertEquals(getTable().getHeader().getColumnHeaderText(COLUMN_CAPITAL), EMPTY_STRING);
    }

    public void testCapitalFooterFacet() {
        dataTableFacets.set(DataTableFacets.capitalFooter, SAMPLE_STRING);
        assertEquals(getTable().getFooter().getColumnFooterText(COLUMN_CAPITAL), SAMPLE_STRING);

        dataTableFacets.set(DataTableFacets.capitalFooter, EMPTY_STRING);
        assertEquals(getTable().getFooter().getColumnFooterText(COLUMN_CAPITAL), EMPTY_STRING);
    }
}
