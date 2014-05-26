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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSimpleTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SimpleEDT;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class TestExtendedDataTableSimple extends DataTableSimpleTest {

    private final Attributes<ExtendedDataTableAttributes> attributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/simple.xhtml");
    }

    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private SimpleEDT table;

    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private WebElement tableRoot;

    @Override
    protected SimpleEDT getTable() {
        return table;
    }

    @Test
    @UseWithField(field = "first", valuesFrom = FROM_FIELD, value = "COUNTS")
    public void testFirst() {
        super.testFirst();
    }

    @Test
    @Templates("plain")
    public void testNoDataLabel() {
        super.testNoDataLabel();
    }

    @Test
    @Templates("plain")
    public void testOnbeforeselectionchange() {
        testFireEvent("onbeforeselectionchange", new Action() {
            @Override
            public void perform() {
                table.getFirstRow().getRootElement().click();
            }
        });
    }

    @Test
    @Templates("plain")
    public void testOnrowclick() {
        super.testOnrowclick();
    }

    @Test
    @Templates("plain")
    public void testOnrowdblclick() {
        super.testOnrowdblclick();
    }

    @Test
    @Templates("plain")
    public void testOnrowkeydown() {
        super.testOnrowkeydown();
    }

    @Test
    @Templates("plain")
    public void testOnrowkeypress() {
        super.testOnrowkeypress();
    }

    @Test
    @Templates("plain")
    public void testOnrowkeyup() {
        super.testOnrowkeyup();
    }

    @Test
    @Templates("plain")
    public void testOnrowmousedown() {
        super.testOnrowmousedown();
    }

    @Test
    @Templates("plain")
    public void testOnrowmousemove() {
        super.testOnrowmousemove();
    }

    @Test
    @Templates("plain")
    public void testOnrowmouseout() {
        super.testOnrowmouseout();
    }

    @Test
    @Templates("plain")
    public void testOnrowmouseover() {
        super.testOnrowmouseover();
    }

    @Test
    @Templates("plain")
    public void testOnrowmouseup() {
        super.testOnrowmouseup();
    }

    @Test
    @Templates("plain")
    public void testOnselectionchange() {
        testFireEvent("onbeforeselectionchange", new Action() {
            @Override
            public void perform() {
                table.getFirstRow().getRootElement().click();
            }
        });
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @Templates("plain")
    public void testRowClass() {
        super.testRowClass();
    }

    @Test
    @Templates("plain")
    public void testRowClasses() {
        super.testRowClasses();
    }

    @Test
    @UseWithField(field = "rows", valuesFrom = FROM_FIELD, value = "COUNTS")
    public void testRows() {
        super.testRows();
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        testStyle(tableRoot);
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(tableRoot);
    }
}
