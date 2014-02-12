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
package org.richfaces.tests.metamer.ftest.richComponentControl;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.richDataScroller.PaginationTester;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22797 $
 */
public class TestDataScroller extends AbstractGrapheneTest {

    ComponentControlDataScroller dataScroller = new ComponentControlDataScroller();

    PaginationTester paginationTester = new PaginationTester() {

        @Override
        protected void verifyBeforeScrolling() {

        }

        @Override
        protected void verifyAfterScrolling() {

        }
    };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richComponentControl/simple.xhtml");
    }

    @BeforeMethod
    public void setup() {
        paginationTester.setDataScroller(dataScroller);
        int lastPage = dataScroller.obtainLastPage();
        dataScroller.setLastPage(lastPage);
        paginationTester.initializeTestedPages(lastPage);
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1187" })
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testScrollerPagination() {
        paginationTester.testNumberedPages();
    }

    @Test(groups = { "Future" })
    @IssueTracking({ "https://issues.jboss.org/browse/RF-9306" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testScrollerPaginationInIterationComponents() {
        paginationTester.testNumberedPages();
    }
}
