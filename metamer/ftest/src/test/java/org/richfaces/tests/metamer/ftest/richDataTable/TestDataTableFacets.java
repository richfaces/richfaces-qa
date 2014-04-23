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
import org.richfaces.fragment.dataTable.RichFacesDataTable;

import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacetsTest;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleFooterInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleHeaderInterface;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SimpleRowInterface;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SimpleDT;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDataTableFacets extends DataTableFacetsTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private SimpleDT table;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/facets.xhtml");
    }


    @Override
    protected SimpleDT getTable() {
        return table;
    }

    @Test
    public void testNoDataInstantChange() {
        super.testNoDataInstantChange();
    }

    @Test
    public void testNoDataEmpty() {
        super.testNoDataEmpty();
    }

    @Test
    public void testHeaderInstantChange() {
        super.testHeaderInstantChange();
    }

    @Test
    public void testHeaderEmpty() {
        super.testHeaderEmpty();
    }

    @Test
    public void testStateHeaderInstantChange() {
        super.testStateHeaderInstantChange();
    }

    @Test
    public void testStateHeaderEmpty() {
        super.testStateHeaderEmpty();
    }

    @Test
    public void testStateFooterInstantChange() {
        super.testStateFooterInstantChange();
    }

    @Test
    public void testStateFooterEmpty() {
        super.testStateFooterEmpty();
    }

    @Test
    public void testCapitalHeaderInstantChange() {
        super.testCapitalHeaderInstantChange();
    }

    @Test
    public void testCapitalHeaderEmpty() {
        super.testCapitalHeaderEmpty();
    }

    @Test
    public void testCapitalFooterInstantChange() {
        super.testCapitalFooterInstantChange();
    }

    @Test
    public void testCapitalFooterEmpty() {
        super.testCapitalFooterEmpty();
    }

}