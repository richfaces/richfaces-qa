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

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFacetsTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SimpleEDT;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
@Templates(value = "plain")
public class TestExtendedDataTableFacets extends DataTableFacetsTest {

    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private SimpleEDT table;

    private final Attributes<ExtendedDataTableAttributes> attributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/facets.xhtml");
    }

    @Override
    protected SimpleEDT getTable() {
        return table;
    }

    @Test
    public void testCapitalFooterFacet() {
        super.testCapitalFooterFacet();
    }

    @Test
    public void testCapitalHeaderFacet() {
        super.testCapitalHeaderFacet();
    }

    @Test
    @Templates("plain")
    public void testFooterClass() {
        testStyleClass(table.getFooter().getTableFooterElement(), BasicAttributes.footerClass);
    }

    @Test
    @Templates("plain")
    public void testHeaderClass() {
        testStyleClass(table.getHeader().getTableHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testHeaderFacet() {
        super.testHeaderFacet();
    }

    @Test
    public void testNoDataFacet() {
        super.testNoDataFacet();
    }

    @Test
    public void testStateFooterFacet() {
        super.testStateFooterFacet();
    }

    @Test
    public void testStateHeaderFacet() {
        super.testStateHeaderFacet();
    }
}
