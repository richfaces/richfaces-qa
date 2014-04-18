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
import org.richfaces.fragment.dataTable.RichFacesDataTable;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.capitalFooter;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.capitalHeader;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.header;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.noData;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.stateFooter;
import static org.richfaces.tests.metamer.ftest.abstractions.DataTableFacets.stateHeader;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleFooterInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleHeaderInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleRowInterface;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

import static org.testng.Assert.assertEquals;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public abstract class DataTableFacetsTest extends AbstractDataTableTest {

    private static final String SAMPLE_STRING = "Abc123!@#ĚščСам";
    private static final String EMPTY_STRING = "";

    private final Attributes<DataTableFacets> dataTableFacets = getAttributes();

    protected abstract RichFacesDataTable<? extends SimpleHeaderInterface, ? extends SimpleRowInterface, ? extends SimpleFooterInterface> getTable();

    public void testNoDataInstantChange() {
        enableShowData(false);
        dataTableFacets.set(noData, SAMPLE_STRING);
        assertEquals(getTable().advanced().getNoDataElement().getText(), SAMPLE_STRING);
    }

    public void testNoDataEmpty() {
        enableShowData(false);
        dataTableFacets.set(noData, EMPTY_STRING);
        assertEquals(getTable().advanced().getNoDataElement().getText(), EMPTY_STRING);
    }

    public void testHeaderInstantChange() {
        dataTableFacets.set(header, SAMPLE_STRING);
        assertEquals(getTable().getHeader().getHeaderText(), SAMPLE_STRING);
    }

    public void testHeaderEmpty() {
        dataTableFacets.set(header, EMPTY_STRING);

        if (new WebElementConditionFactory(getTable().advanced().getHeaderElement()).isPresent().apply(driver)) {
            assertEquals(getTable().advanced().getHeaderElement().getText(), EMPTY_STRING);
        } else {
            dataTableFacets.set(header, SAMPLE_STRING);
            assertEquals(getTable().advanced().getHeaderElement().getText(), SAMPLE_STRING);
        }
    }

    public void testStateHeaderInstantChange() {
        dataTableFacets.set(stateHeader, SAMPLE_STRING);
        assertEquals(getTable().getHeader().getColumnHeaderText(COLUMN_STATE), SAMPLE_STRING);
    }

    public void testStateHeaderEmpty() {
        dataTableFacets.set(stateHeader, EMPTY_STRING);
        assertEquals(getTable().getHeader().getColumnHeaderText(COLUMN_STATE), EMPTY_STRING);
    }

    public void testStateFooterInstantChange() {
        dataTableFacets.set(stateFooter, SAMPLE_STRING);
        assertEquals(getTable().getFooter().getColumnFooterText(COLUMN_STATE), SAMPLE_STRING);
    }

    public void testStateFooterEmpty() {
        dataTableFacets.set(stateFooter, EMPTY_STRING);
        assertEquals(getTable().getFooter().getColumnFooterText(COLUMN_STATE), EMPTY_STRING);
    }

    public void testCapitalHeaderInstantChange() {
        dataTableFacets.set(capitalHeader, SAMPLE_STRING);
        assertEquals(getTable().getHeader().getColumnHeaderText(COLUMN_CAPITAL), SAMPLE_STRING);
    }

    public void testCapitalHeaderEmpty() {
        dataTableFacets.set(capitalHeader, EMPTY_STRING);
        assertEquals(getTable().getHeader().getColumnHeaderText(COLUMN_CAPITAL), EMPTY_STRING);
    }

    public void testCapitalFooterInstantChange() {
        dataTableFacets.set(capitalFooter, SAMPLE_STRING);
        assertEquals(getTable().getFooter().getColumnFooterText(COLUMN_CAPITAL), SAMPLE_STRING);
    }

    public void testCapitalFooterEmpty() {
        dataTableFacets.set(capitalFooter, EMPTY_STRING);
        assertEquals(getTable().getFooter().getColumnFooterText(COLUMN_CAPITAL), EMPTY_STRING);
    }
}