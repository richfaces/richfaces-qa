/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDataScroller;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.dataScrollerAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.DataScroller;
import org.richfaces.tests.metamer.ftest.model.ExtendedDataTable;
import org.testng.annotations.BeforeMethod;

/**
 * Test the functionality of switching pages using DataScroller bound to DataTable.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22740 $
 */
public abstract class AbstractScrollerTest extends AbstractGrapheneTest {

    @Inject
    @Use(ints = { 2, 3 })
    int fastStep;

    @Inject
    @Use(ints = { 3, 4 })
    int maxPages;

    @Inject
    DataScroller dataScroller;
    DataScroller dataScroller1 = PaginationTester.DATA_SCROLLER_OUTSIDE_TABLE;
    DataScroller dataScroller2 = PaginationTester.DATA_SCROLLER_IN_TABLE_FOOTER;

    PaginationTester paginationTester = new PaginationTester() {

        @Override
        protected void verifyBeforeScrolling() {
            tableText = dataTable.getTableText();
        }

        @Override
        protected void verifyAfterScrolling() {
            assertFalse(tableText.equals(dataTable.getTableText()));
            assertEquals(maxPages, dataScroller.getCountOfVisiblePages());
        }
    };

    ExtendedDataTable dataTable = new ExtendedDataTable(pjq("table.rf-dt[id$=richDataTable]"));

    String tableText;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataScroller/simple.xhtml");
    }

    @BeforeMethod
    public void prepareComponent() {
        dataScrollerAttributes.set(DataScrollerAttributes.fastStep, fastStep);
        dataScrollerAttributes.set(DataScrollerAttributes.maxPages, maxPages);

        paginationTester.setDataScroller(dataScroller);
        dataScroller.setFastStep(fastStep);

        int lastPage = dataScroller.obtainLastPage();
        dataScroller.setLastPage(lastPage);
        paginationTester.initializeTestedPages(lastPage);
    }

    public void testNumberedPages() {
        paginationTester.testNumberedPages();
    }
}
