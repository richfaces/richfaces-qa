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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import org.openqa.selenium.NoSuchElementException;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsibleSubTableFacets extends AbstractCollapsibleSubTableTest {

    private static final String SAMPLE_STRING = "Abc123!@#ĚščСам";
    private static final String EMPTY_STRING = "";

    private final Attributes<FacetsAttributes> dataTableFacets = getAttributes();

    private enum FacetsAttributes implements AttributeEnum {

        noData, header, footer
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/facets.xhtml");
    }

    @Test
    public void testFooterClass() {
        dataTableFacets.set(FacetsAttributes.footer, SAMPLE_STRING);
        testStyleClass(getSubTable(isMale).advanced().getFooterElement(), BasicAttributes.footerClass);
        testStyleClass(getSubTable(!isMale).advanced().getFooterElement(), BasicAttributes.footerClass);
    }

    @Test
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    public void testFooterFacet() {
        dataTableFacets.set(FacetsAttributes.footer, SAMPLE_STRING);
        assertEquals(getSubTable(isMale).advanced().getFooterElement().getText(), SAMPLE_STRING);

        dataTableFacets.set(FacetsAttributes.footer, EMPTY_STRING);
        try {
            getSubTable(isMale).advanced().getFooterElement();
            fail("The table should not have any footer elements");
        } catch (NoSuchElementException ex) {
        }
        dataTableFacets.set(FacetsAttributes.footer, SAMPLE_STRING);
        assertEquals(getSubTable(isMale).advanced().getFooterElement().getText(), SAMPLE_STRING);
    }

    @Test
    public void testHeaderClass() {
        dataTableFacets.set(FacetsAttributes.header, SAMPLE_STRING);

        testStyleClass(getSubTable(isMale).advanced().getHeaderElement(), BasicAttributes.headerClass);
        testStyleClass(getSubTable(!isMale).advanced().getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    public void testHeaderFacet() {
        dataTableFacets.set(FacetsAttributes.header, SAMPLE_STRING);
        assertEquals(getSubTable(isMale).advanced().getHeaderElement().getText(), SAMPLE_STRING);

        dataTableFacets.set(FacetsAttributes.header, EMPTY_STRING);

        try {
            getSubTable(isMale).advanced().getHeaderElement();
            fail("The table should not have any header elements");
        } catch (NoSuchElementException ex) {
        }

        dataTableFacets.set(FacetsAttributes.header, SAMPLE_STRING);
        assertEquals(getSubTable(isMale).advanced().getHeaderElement().getText(), SAMPLE_STRING);
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RFPL-1515", "https://issues.jboss.org/browse/RF-12672" })
    @UseWithField(field = "isMale", valuesFrom = FROM_FIELD, value = "booleans")
    public void testNoDataFacet() {
        showDataInTable(false);
        assertFalse(getSubTable(isMale).advanced().isVisible());
        assertFalse(Utils.isVisible(getSubTable(isMale).advanced().getNoDataElement()));

        dataTableFacets.set(FacetsAttributes.noData, SAMPLE_STRING);
        assertEquals(getSubTable(isMale).advanced().getNumberOfVisibleRows(), 0);
        assertTrue(Utils.isVisible(getSubTable(isMale).advanced().getNoDataElement()));
        assertEquals(getSubTable(isMale).advanced().getNoDataElement().getText(), SAMPLE_STRING);

        showDataInTable(true);
        try {
            getSubTable(isMale).advanced().getNoDataElement();
            fail("There should be no noData element now");
        } catch (NoSuchElementException e) {
        }
        assertTrue(getSubTable(isMale).advanced().isVisible());
        assertEquals(getSubTable(isMale).advanced().getNumberOfVisibleRows(), 5);
    }
}
