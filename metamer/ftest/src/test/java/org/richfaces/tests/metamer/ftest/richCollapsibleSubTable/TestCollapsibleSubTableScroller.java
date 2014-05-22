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
import static org.richfaces.tests.metamer.ftest.richCollapsibleSubTable.ExpandMode.ajax;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.fragment.collapsibleSubTableToggler.RichFacesCollapsibleSubTableToggler;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsibleSubTableScroller extends AbstractCollapsibleSubTableTest {

    @FindByJQuery(".rf-ds:eq(0)")
    private RichFacesDataScroller menScroller;
    @FindByJQuery(".rf-ds:eq(1)")
    private RichFacesDataScroller womenScroller;
    private CollapsibleSubTableWithEmployees menSubTable;
    private CollapsibleSubTableWithEmployees womenSubTable;

    private static final int pagesMen = 32;
    private static final int pagesWomen = 9;

    private PaginationTester paginationTester;

    private final int rows = 5;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/scroller.xhtml");
    }

    @BeforeMethod
    public void prepareComponent() {
        paginationTester = new ParallelScrollingTester();
        attributes.set(CollapsibleSubTableAttributes.expandMode, expandMode);
        paginationTester.setDataScroller(menScroller);
        paginationTester.initializeTestedPages(pagesMen);
        attributes.set(CollapsibleSubTableAttributes.rows, rows);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11301")
    @UseWithField(field = "expandMode", valuesFrom = FROM_FIELD, value = "expandModeAjax")
    public void testScrollerExpandModeAjax() {
        menSubTable = getSubTable(Boolean.TRUE);
        womenSubTable = getSubTable(Boolean.FALSE);
        paginationTester.testNumberedPages();
    }

    @Test(groups = "extended")
    @IssueTracking("https://issues.jboss.org/browse/RF-11301")
    @UseWithField(field = "expandMode", valuesFrom = FROM_FIELD, value = "expandModesOtherThanAjax")
    public void testScrollerExpandModeOtherThanAjax() {
        testScrollerExpandModeAjax();
    }

    public class ParallelScrollingTester extends PaginationTester {

        private int womenScrollerPage;
        private int actPage;

        private List<EmployeeRecord> lastVisibleWomenEmployees;
        private List<EmployeeRecord> lastVisibleMenEmployees;

        @Override
        protected void verifyAfterScrolling() {
            verifyScrollingAfterMenScrollerUsed();
            verifySubTablesToggling();
            verifyScrollingWithWomenScroller();
        }

        @Override
        protected void verifyBeforeScrolling() {
            womenScrollerPage = womenScroller.getActivePageNumber();
            lastVisibleWomenEmployees = getSubTable(Boolean.FALSE).getAllRows();
        }

        private void checkEmployeesEquals(List<Employee> expected, List<EmployeeRecord> actualEmployees) {
            assertEquals(expected.size(), actualEmployees.size());
            EmployeeRecord actualE;
            Employee expectedE;
            for (int i = 0; i < expected.size(); i++) {
                actualE = actualEmployees.get(i);
                expectedE = expected.get(i);
                assertEquals(expectedE.getName(), actualE.getName());
                assertEquals(expectedE.getTitle(), actualE.getTitle());
            }
        }

        private List<Employee> getExpectedEmployeesForScrolledView(boolean isMaleTable) {
            int start = ((isMaleTable ? menScroller : womenScroller).getActivePageNumber() - 1) * rows;
            int end = Math.min(start + rows, getEmployees(isMaleTable).size());
            List<Employee> visibleEmployees = getEmployees(isMaleTable).subList(start, end);
            return visibleEmployees;
        }

        private RichFacesCollapsibleSubTableToggler getGuardedToggler(RichFacesCollapsibleSubTableToggler toggler) {
            switch (expandMode) {
                case ajax:
                    return Graphene.guardAjax(toggler);
                case none:
                case client:
                    return Graphene.guardNoRequest(toggler);
                case server:
                    return Graphene.guardHttp(toggler);
            }
            return null;
        }

        private void scrollWithWomenScroller() {
            womenScroller.switchTo((womenScroller.getActivePageNumber() + 3) % pagesWomen + 1);
        }

        private void verifyScrollingAfterMenScrollerUsed() {
            assertEquals(womenScroller.getActivePageNumber(), womenScrollerPage);
            checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.FALSE), lastVisibleWomenEmployees);

            lastVisibleMenEmployees = getSubTable(Boolean.TRUE).getAllRows();
            checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.TRUE), lastVisibleMenEmployees);
        }

        private void verifyScrollingWithWomenScroller() {
            scrollWithWomenScroller();

            checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.TRUE), lastVisibleMenEmployees);

            lastVisibleWomenEmployees = getSubTable(Boolean.FALSE).getAllRows();
            checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.FALSE), lastVisibleWomenEmployees);
        }

        private void verifySubTablesToggling() {
            actPage = menScroller.getActivePageNumber();
            RichFacesCollapsibleSubTableToggler togglerMen = getGuardedToggler(menSubTable.advanced().getTableToggler());
            RichFacesCollapsibleSubTableToggler togglerWomen = getGuardedToggler(womenSubTable.advanced().getTableToggler());
            assertTrue(menSubTable.advanced().isVisible());
            assertTrue(womenSubTable.advanced().isVisible());
            if (expandMode != ExpandMode.none) {
                togglerMen.toggle();
                assertFalse(Utils.isVisible(menScroller.advanced().getRootElement()));
                assertEquals(womenScroller.getActivePageNumber(), womenScrollerPage);
                assertFalse(menSubTable.advanced().isVisible());
                assertTrue(womenSubTable.advanced().isVisible());
                assertFalse(menSubTable.advanced().isExpanded());
                assertTrue(womenSubTable.advanced().isExpanded());

                togglerMen.toggle();
                assertEquals(menScroller.getActivePageNumber(), actPage);
                assertEquals(womenScroller.getActivePageNumber(), womenScrollerPage);
                assertTrue(menSubTable.advanced().isVisible());
                assertTrue(womenSubTable.advanced().isVisible());
                assertTrue(menSubTable.advanced().isExpanded());
                assertTrue(womenSubTable.advanced().isExpanded());
                checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.TRUE), lastVisibleMenEmployees);
                checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.FALSE), lastVisibleWomenEmployees);

                togglerWomen.toggle();
                assertEquals(menScroller.getActivePageNumber(), actPage);
                assertFalse(Utils.isVisible(womenScroller.advanced().getRootElement()));
                assertTrue(menSubTable.advanced().isVisible());
                assertFalse(womenSubTable.advanced().isVisible());
                assertTrue(menSubTable.advanced().isExpanded());
                assertFalse(womenSubTable.advanced().isExpanded());
            } else {
                togglerMen.toggle();
                assertEquals(menScroller.getActivePageNumber(), actPage);
                assertEquals(womenScroller.getActivePageNumber(), womenScrollerPage);
                assertTrue(menSubTable.advanced().isVisible());
                assertTrue(womenSubTable.advanced().isVisible());
                assertTrue(menSubTable.advanced().isExpanded());
                assertTrue(womenSubTable.advanced().isExpanded());
                checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.TRUE), lastVisibleMenEmployees);
                checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.FALSE), lastVisibleWomenEmployees);
            }
            togglerWomen.toggle();
            assertEquals(menScroller.getActivePageNumber(), actPage);
            assertEquals(womenScroller.getActivePageNumber(), womenScrollerPage);
            assertTrue(menSubTable.advanced().isVisible());
            assertTrue(womenSubTable.advanced().isVisible());
            assertTrue(menSubTable.advanced().isExpanded());
            assertTrue(womenSubTable.advanced().isExpanded());
            checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.TRUE), lastVisibleMenEmployees);
            checkEmployeesEquals(getExpectedEmployeesForScrolledView(Boolean.FALSE), lastVisibleWomenEmployees);
        }
    }
}
