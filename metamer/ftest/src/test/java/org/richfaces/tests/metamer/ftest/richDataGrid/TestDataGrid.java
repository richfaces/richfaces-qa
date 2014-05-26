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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.xml.bind.JAXBException;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.richDataGrid.fragment.GridWithStates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
public class TestDataGrid extends AbstractDataGridTest {

    public TestDataGrid() throws JAXBException {
        super();
    }

    @FindBy(css = "table.rf-dg[id$=richDataGrid]")
    private GridWithStates dataGrid;

    @FindBy(css = "table.rf-dg[id$=richDataGrid]")
    private WebElement dataGridRoot;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataGrid/simple.xhtml");
    }

    @Test
    @UseWithField(field = "columns", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS1")
    public void testColumnsAttribute() {
        verifyGrid();
    }

    @Test
    @UseWithField(field = "elements", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS2")
    public void testElementsAttribute() {
        verifyGrid();
    }

    @Test
    @UseWithField(field = "first", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS2")
    public void testFirstAttribute() {
        verifyGrid();
    }

    @Test
    public void testNoDataFacet() {
        Graphene.guardAjax(attributeShowData).click();

        assertEquals(dataGrid.getNumberOfColumns(), 0);
        assertEquals(dataGrid.getNumberOfRecords(), 0);
        assertTrue(dataGrid.advanced().isNoData());
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        dataGridAttributes.set(DataGridAttributes.rendered, false);
        assertNotVisible(dataGridRoot, "DataGrid should not be visible.");
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        testStyle(dataGridRoot);
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(dataGridRoot);
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        testTitle(dataGridRoot);
    }

    @Override
    public GridWithStates getDataGrid() {
        return dataGrid;
    }
}
