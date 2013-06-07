/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.richDataScroller.PaginationTesterWebDriver;
import org.richfaces.tests.metamer.ftest.richDataScroller.PaginationTesterWebDriver.AssertingAndWaitingDataScroller;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestListWithScroller extends AbstractListTest {

    @FindBy(css = "span.rf-ds[id$=scroller1]")
    protected AssertingAndWaitingDataScroller scrollerOutsideTable;
    @FindBy(css = "span.rf-ds[id$=scroller2]")
    protected AssertingAndWaitingDataScroller scrollerInTableFooter;
    PaginationTesterWebDriver paginationTester = new PaginationTesterWebDriver() {
        final int DEFAULT_ROWS = 20;

        @Override
        protected void verifyBeforeScrolling() {
        }

        @Override
        protected void verifyAfterScrolling() {
            rows = DEFAULT_ROWS;
            int currentPage = dataScroller.getActPageNumber();
            first = rows * (currentPage - 1);
            verifyList();
        }
    };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richList/scroller.xhtml");
    }

    public void prepareTester(AssertingAndWaitingDataScroller dataScroller) {
        paginationTester.setDataScroller(dataScroller);
        int lastPage = dataScroller.obtainLastPage();
        dataScroller.setLastPage(lastPage);
        paginationTester.initializeTestedPages(lastPage);
        dataScroller.setFastStep(1);
    }

    @Test
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testScrollerWithRowsAttributeOut() {
        prepareTester(scrollerOutsideTable);
        paginationTester.testNumberedPages();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11787")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testScrollerWithRowsAttributeOutIterationComponents() {
        testScrollerWithRowsAttributeOut();
    }

    @Test
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testScrollerWithRowsAttributeIn() {
        prepareTester(scrollerInTableFooter);
        paginationTester.testNumberedPages();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11787")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testScrollerWithRowsAttributeInIterationComponents() {
        testScrollerWithRowsAttributeIn();
    }
}
