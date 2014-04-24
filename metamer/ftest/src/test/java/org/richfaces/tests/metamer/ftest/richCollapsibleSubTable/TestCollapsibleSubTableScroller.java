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
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.fragment.collapsibleSubTableToggler.RichFacesCollapsibleSubTableToggler;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22872 $
 */
public class TestCollapsibleSubTableScroller extends AbstractCollapsibleSubTableTest {

    @FindByJQuery(".rf-ds:eq(0)")
    private RichFacesDataScroller menScroller;
    @FindByJQuery(".rf-ds:eq(1)")
    private RichFacesDataScroller womenDataScroller;

    private static final int pagesMen = 32;
    private static final int pagesWomen = 9;

    private PaginationTester paginationTester;

    private final int rows = 5;

    @UseForAllTests(valuesFrom = FROM_FIELD, value = "booleans")
    private boolean paralelScrolling;

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
        attributes.set(CollapsibleSubTableAttributes.expandMode, expandMode);
        paginationTester.setDataScroller(isMale ? menScroller : womenDataScroller);
        paginationTester.initializeTestedPages(isMale ? pagesMen : pagesWomen);
        attributes.set(CollapsibleSubTableAttributes.rows, rows);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11301")
    @UseWithField(field = "expandMode", valuesFrom = FROM_ENUM, value = "")
    public void testScroller() {
        paginationTester.testNumberedPages();
    }

    public class BasicPaginationTester extends PaginationTester {

        private int secondScrollerPage;
        private String secondSubtableText;

        @Override
        protected void verifyBeforeScrolling() {
            secondScrollerPage = womenDataScroller.getActivePageNumber();
            secondSubtableText = getSubTable(!isMale).advanced().getTableRootElement().getText();
        }

        private RichFacesCollapsibleSubTableToggler getGuardedToggler(RichFacesCollapsibleSubTableToggler toggler) {
            switch (expandMode) {
                case ajax:
                    return Graphene.guardAjax(toggler);
                case client:
                    return Graphene.guardNoRequest(toggler);
                case none:
                case server:
                    return Graphene.guardHttp(toggler);
            }
            return null;
        }

        @Override
        protected void verifyAfterScrolling() {
            CollapsibleSubTableWithEmployees subTable = getSubTable(isMale);
            CollapsibleSubTableWithEmployees secondSubTable = getSubTable(!isMale);
            if (expandMode != ExpandMode.none) {
                RichFacesCollapsibleSubTableToggler toggler = getGuardedToggler(subTable.advanced().getTableToggler());
                assertTrue(subTable.advanced().isVisible());
                assertTrue(secondSubTable.advanced().isVisible());
                toggler.toggle();
                assertFalse(subTable.advanced().isVisible());
                assertTrue(secondSubTable.advanced().isVisible());
                toggler.toggle();
                assertTrue(subTable.advanced().isVisible());
                assertTrue(secondSubTable.advanced().isVisible());
            }

            assertEquals(womenDataScroller.getActivePageNumber(), secondScrollerPage);
            assertEquals(secondSubTable.advanced().getTableRootElement().getText(), secondSubtableText);

            int start = (dataScroller.getActivePageNumber() - 1) * rows;
            int end = Math.min(start + rows, getEmployees(isMale).size());
            int count = end - start;
            List<Employee> visibleEmployees = getEmployees(isMale).subList(start, end);

            assertEquals(subTable.advanced().getNumberOfVisibleRows(), count);

            for (int i = 0; i < count; i++) {
                assertEquals(subTable.getRow(i).getName(), visibleEmployees.get(i).getName());
                assertEquals(subTable.getRow(i).getTitle(), visibleEmployees.get(i).getTitle());
            }
        }
    }

    public class ParallelScrollingTester extends BasicPaginationTester {

        int notrandomizer = (Integer.MAX_VALUE - 17) * 13;

        @Override
        protected void verifyBeforeScrolling() {
            notrandomizer *= 177;
            int page = (Math.abs(notrandomizer) % (isMale ? pagesWomen : pagesMen)) + 1;
            womenDataScroller.switchTo(page);
            super.verifyBeforeScrolling();
        }
    }
}
