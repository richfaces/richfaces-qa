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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import javax.xml.bind.JAXBException;

import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.DataScroller;
import org.richfaces.tests.metamer.ftest.richDataScroller.PaginationTester;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
@Use(field = "elements", ints = 7)
public abstract class AbstractScrollerTest extends AbstractDataGridTest {

    @Inject
    DataScroller dataScroller;
    DataScroller dataScroller1 = PaginationTester.DATA_SCROLLER_OUTSIDE_TABLE;
    DataScroller dataScroller2 = PaginationTester.DATA_SCROLLER_IN_TABLE_FOOTER;

    PaginationTester paginationTester = new PaginationTester() {

        @Override
        protected void verifyBeforeScrolling() {
        }

        @Override
        protected void verifyAfterScrolling() {
            page = dataScroller.getCurrentPage();
            lastPage = dataScroller.getLastPage();
            verifyGrid();
        }
    };

    public AbstractScrollerTest() throws JAXBException {
        super();
    }

    @BeforeMethod
    public void setupDataScroller() {
        paginationTester.setDataScroller(dataScroller);

        int lastPage = dataScroller.obtainLastPage();
        dataScroller.setLastPage(lastPage);
        paginationTester.initializeTestedPages(lastPage);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataGrid/scroller.xhtml");
    }

    @Use(field = "columns", ints = { 1, 3, 11, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL,
            ELEMENTS_TOTAL + 1 })
    public void testColumnsAttribute() {
        paginationTester.testNumberedPages();
    }

    @Use(field = "elements", ints = { 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 })
    public void testElementsAttribute() {
        paginationTester.testNumberedPages();
    }

    @Use(field = "first", ints = { 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 })
    public void testFirstAttributeDoesntInfluentScroller() {
        // the attribute for component was already set, now verify that this attribute doesn't influent rendering (it
        // means dataGrid with scroller ignores this attribute, it means it is always equal to zero)
        first = 0;
        paginationTester.testNumberedPages();
    }
}
