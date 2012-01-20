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
package org.richfaces.tests.metamer.ftest.richList;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.DataScroller;
import org.richfaces.tests.metamer.ftest.richDataScroller.PaginationTester;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TestScroller extends AbstractListTest {

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
            int currentPage = dataScroller.getCurrentPage();
            first = rows * (currentPage - 1);
            verifyList();
        }
    };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richList/scroller.xhtml");
    }

    @BeforeMethod
    public void prepareComponent() {
        paginationTester.setDataScroller(dataScroller);

        int lastPage = dataScroller.obtainLastPage();
        dataScroller.setLastPage(lastPage);
        paginationTester.initializeTestedPages(lastPage);

        dataScroller.setFastStep(1);
    }

    @Test
    @Use(field = "dataScroller", value = "dataScroller1")
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testScrollerWithRowsAttributeOut() {
        paginationTester.testNumberedPages();
    }

    @Test(groups = "4.Future")
    @Use(field = "dataScroller", value = "dataScroller1")
    @IssueTracking("https://issues.jboss.org/browse/RF-11787")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testScrollerWithRowsAttributeOutIterationComponents() {
        paginationTester.testNumberedPages();
    }

    @Test
    @Use(field = "dataScroller", value = "dataScroller2")
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testScrollerWithRowsAttributeIn() {
        paginationTester.testNumberedPages();
    }

    @Test(groups = "4.Future")
    @Use(field = "dataScroller", value = "dataScroller2")
    @IssueTracking("https://issues.jboss.org/browse/RF-11787")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testScrollerWithRowsAttributeInIterationComponents() {
        paginationTester.testNumberedPages();
    }
}
