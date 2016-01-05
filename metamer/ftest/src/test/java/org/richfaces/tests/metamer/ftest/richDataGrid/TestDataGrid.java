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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.richDataGrid.fragment.GridWithStates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestDataGrid extends AbstractDataGridTest {

    @FindBy(css = "table.rf-dg[id$=richDataGrid]")
    private GridWithStates dataGrid;
    @FindBy(css = "table.rf-dg[id$=richDataGrid]")
    private WebElement dataGridRoot;

    public TestDataGrid() throws JAXBException {
        super();
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDataGrid/simple.xhtml";
    }

    @Override
    public GridWithStates getDataGrid() {
        return dataGrid;
    }

    @Test
    @CoversAttributes("columns")
    @UseWithField(field = "columns", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS1")
    public void testColumnsAttribute() {
        verifyGrid();
    }

    @Test
    @CoversAttributes("elements")
    @UseWithField(field = "elements", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS2")
    public void testElementsAttribute() {
        verifyGrid();
    }

    @Test
    @CoversAttributes("first")
    @UseWithField(field = "first", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS2")
    public void testFirstAttribute() {
        verifyGrid();
    }

    @Test
    @CoversAttributes("footerClass")
    @Templates("plain")
    public void testFooterClass() {
        testStyleClass(dataGridRoot.findElement(By.className("rf-dg-f")), BasicAttributes.footerClass);
    }

    @Test
    @CoversAttributes("headerClass")
    @Templates("plain")
    public void testHeaderClass() {
        testStyleClass(dataGridRoot.findElement(By.className("rf-dg-h")), BasicAttributes.headerClass);
    }

    @Test
    public void testNoDataFacet() {
        Graphene.guardAjax(attributeShowData).click();

        assertEquals(dataGrid.getNumberOfColumns(), 0);
        assertEquals(dataGrid.getNumberOfRecords(), 0);
        assertTrue(dataGrid.advanced().isNoData());
    }

    @Test
    @CoversAttributes("rendered")
    @Templates("plain")
    public void testRendered() {
        dataGridAttributes.set(DataGridAttributes.rendered, false);
        assertNotVisible(dataGridRoot, "DataGrid should not be visible.");
    }

    @Test
    @CoversAttributes("style")
    @Templates("plain")
    public void testStyle() {
        testStyle(dataGridRoot);
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(dataGridRoot);
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(dataGridRoot);
    }
}
