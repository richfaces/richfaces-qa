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
package org.richfaces.tests.metamer.ftest.richDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import org.jboss.arquillian.graphene.findby.FindByJQuery;

import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacetsTest;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SimpleTable;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDataTableFacets extends DataTableFacetsTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private SimpleTable table;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/facets.xhtml");
    }

    @Test
    public void testNoDataInstantChange() {
        super.testNoDataInstantChange(table);
    }

    @Test
    public void testNoDataEmpty() {
        super.testNoDataEmpty(table);
    }

    @Test
    public void testHeaderInstantChange() {
        super.testHeaderInstantChange(table);
    }

    @Test
    public void testHeaderEmpty() {
        super.testHeaderEmpty(table);
    }

    @Test
    public void testStateHeaderInstantChange() {
        super.testStateHeaderInstantChange(table);
    }

    @Test
    public void testStateHeaderEmpty() {
        super.testStateHeaderEmpty(table);
    }

    @Test
    public void testStateFooterInstantChange() {
        super.testStateFooterInstantChange(table);
    }

    @Test
    public void testStateFooterEmpty() {
        super.testStateFooterEmpty(table);
    }

    @Test
    public void testCapitalHeaderInstantChange() {
        super.testCapitalHeaderInstantChange(table);
    }

    @Test
    public void testCapitalHeaderEmpty() {
        super.testCapitalHeaderEmpty(table);
    }

    @Test
    public void testCapitalFooterInstantChange() {
        super.testCapitalFooterInstantChange(table);
    }

    @Test
    public void testCapitalFooterEmpty() {
        super.testCapitalFooterEmpty(table);
    }
}