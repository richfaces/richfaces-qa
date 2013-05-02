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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.arquillian.ajocado.guard.RequestGuardFactory.guard;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.ajocado.request.RequestType;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.DataScroller;
import org.richfaces.tests.metamer.ftest.richDataScroller.PaginationTester;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22872 $
 */
public class TestCollapsibleSubTableScroller extends AbstractCollapsibleSubTableTest {

    DataScroller dataScroller;
    DataScroller secondDataScroller;
    PaginationTester paginationTester;

    int rows = 5;

    @Inject
    @Use(booleans = { true, false })
    boolean paralelScrolling;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/scroller.xhtml");
    }

    @BeforeMethod
    public void prepareComponent() {
        if (paralelScrolling) {
            paginationTester = new ParallelScrollingTester();
        } else {
            paginationTester = new BasicPaginationTester();
        }

        collapsibleSubTabAttributes.set(CollapsibleSubTableAttributes.expandMode, expandMode);

        dataScroller = new DataScroller(subtable.getFooter().getChild(jq("span.rf-ds")));
        secondDataScroller = new DataScroller(secondSubtable.getFooter().getChild(jq("span.rf-ds")));

        paginationTester.setDataScroller(dataScroller);

        collapsibleSubTabAttributes.set(CollapsibleSubTableAttributes.rows, rows);

        int lastPage = dataScroller.obtainLastPage();
        dataScroller.setLastPage(lastPage);
        paginationTester.initializeTestedPages(lastPage);

        secondDataScroller.setLastPage(secondDataScroller.obtainLastPage());
    }

    @Test(groups = { "4.Future" })
    @Use(field = "expandMode", enumeration = true)
    @IssueTracking("https://issues.jboss.org/browse/RF-11301")
    public void testScroller() {
        paginationTester.testNumberedPages();
    }

    public class BasicPaginationTester extends PaginationTester {

        int secondScrollerPage;
        String secondSubtableText;

        @Override
        protected void verifyBeforeScrolling() {
            secondScrollerPage = secondDataScroller.getCurrentPage();
            secondSubtableText = selenium.getText(secondSubtable);
        }

        @Override
        protected void verifyAfterScrolling() {
            if (expandMode != ExpandMode.none) {
                final RequestType requestType = getRequestTypeForExpandMode();
                guard(selenium, requestType).click(toggler);
                assertFalse(subtable.isVisible());
                assertTrue(secondSubtable.isVisible());
                guard(selenium, requestType).click(toggler);
                assertTrue(subtable.isVisible());
                assertTrue(secondSubtable.isVisible());
            }

            assertEquals(secondDataScroller.getCurrentPage(), secondScrollerPage);
            assertEquals(selenium.getText(secondSubtable), secondSubtableText);

            int start = (dataScroller.getCurrentPage() - 1) * rows;
            int end = Math.min(start + rows, employees.size());
            int count = end - start;
            List<Employee> visibleEmployees = employees.subList(start, end);

            assertEquals(subtable.getRowCount(), count);

            for (int i = 0; i < count; i++) {
                String name = selenium.getText(subtable.getCell(1, i + 1));
                String title = selenium.getText(subtable.getCell(2, i + 1));

                assertEquals(name, visibleEmployees.get(i).getName());
                assertEquals(title, visibleEmployees.get(i).getTitle());
            }
        }
    }

    public class ParallelScrollingTester extends BasicPaginationTester {

        int notrandomizer = (Integer.MAX_VALUE - 17) * 13;

        @Override
        protected void verifyBeforeScrolling() {
            notrandomizer *= 177;
            int page = (Math.abs(notrandomizer) % secondDataScroller.getLastPage()) + 1;
            secondDataScroller.gotoPage(page);
            super.verifyBeforeScrolling();
        }
    }
}
