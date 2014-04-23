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
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSimpleTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SimpleDT;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDataTableSimple extends DataTableSimpleTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private SimpleDT table;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/simple.xhtml");
    }

    @Override
    protected SimpleDT getTable() {
        return table;
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    public void testNoDataLabel() {
        super.testNoDataLabel();
    }

    @Test
    @UseWithField(field = "first", valuesFrom = FROM_FIELD, value = "COUNTS")
    public void testFirst() {
        super.testFirst();
    }

    @Test
    @UseWithField(field = "rows", valuesFrom = FROM_FIELD, value = "COUNTS")
    public void testRows() {
        super.testRows();
    }
}
