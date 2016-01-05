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
package org.richfaces.tests.metamer.ftest.richDataTable;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSimpleTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SimpleDT;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDataTableSimple extends DataTableSimpleTest {

    private final Attributes<DataTableAttributes> attributes = getAttributes();

    @FindBy(css = "table.rf-dt[id$=richDataTable]")
    private SimpleDT table;
    @FindBy(css = "table.rf-dt[id$=richDataTable]")
    private WebElement tableRoot;

    @Override
    protected SimpleDT getTable() {
        return table;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/simple.xhtml";
    }

    @Test
    @CoversAttributes("first")
    @UseWithField(field = "first", valuesFrom = FROM_FIELD, value = "COUNTS")
    public void testFirst() {
        super.testFirst();
    }

    @Test
    @CoversAttributes("noDataLabel")
    @Templates("plain")
    public void testNoDataLabel() {
        super.testNoDataLabel();
    }

    @Test
    @CoversAttributes("onrowclick")
    @Templates("plain")
    public void testOnrowclick() {
        super.testOnrowclick();
    }

    @Test
    @CoversAttributes("onrowdblclick")
    @Templates("plain")
    public void testOnrowdblclick() {
        super.testOnrowdblclick();
    }

    @Test
    @CoversAttributes("onrowkeydown")
    @Templates("plain")
    public void testOnrowkeydown() {
        super.testOnrowkeydown();
    }

    @Test
    @CoversAttributes("onrowkeypress")
    @Templates("plain")
    public void testOnrowkeypress() {
        super.testOnrowkeypress();
    }

    @Test
    @CoversAttributes("onrowkeyup")
    @Templates("plain")
    public void testOnrowkeyup() {
        super.testOnrowkeyup();
    }

    @Test
    @CoversAttributes("onrowmousedown")
    @Templates("plain")
    public void testOnrowmousedown() {
        super.testOnrowmousedown();
    }

    @Test
    @CoversAttributes("onrowmousemove")
    @Templates("plain")
    public void testOnrowmousemove() {
        super.testOnrowmousemove();
    }

    @Test
    @CoversAttributes("onrowmouseout")
    @Templates("plain")
    public void testOnrowmouseout() {
        super.testOnrowmouseout();
    }

    @Test
    @CoversAttributes("onrowmouseover")
    @Templates("plain")
    public void testOnrowmouseover() {
        super.testOnrowmouseover();
    }

    @Test
    @CoversAttributes("onrowmouseup")
    @Templates("plain")
    public void testOnrowmouseup() {
        super.testOnrowmouseup();
    }

    @Test
    @CoversAttributes("rendered")
    @Templates("plain")
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @CoversAttributes("rowClass")
    @Templates("plain")
    public void testRowClass() {
        super.testRowClass();
    }

    @Test
    @CoversAttributes("rows")
    @UseWithField(field = "rows", valuesFrom = FROM_FIELD, value = "COUNTS")
    public void testRows() {
        super.testRows();
    }

    @Test
    @CoversAttributes("style")
    @Templates("plain")
    public void testStyle() {
        testStyle(tableRoot);
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(tableRoot);
    }
}
