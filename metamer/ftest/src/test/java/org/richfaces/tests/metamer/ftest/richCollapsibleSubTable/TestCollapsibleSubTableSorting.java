/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.model.SortMode;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.AndExpression;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.model.Employee;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsibleSubTableSorting extends AbstractCollapsibleSubTableTest {

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTableWithCSTWithBuiltInSortingHeader dataTableWithCSTWithBuiltInSortingHeader;
    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private DataTableWithCSTWithSortingHeader dataTableWithCSTWithSortingHeader;

    private final int rows = 15;
    @UseForAllTests(valuesFrom = ValuesFrom.FROM_FIELD, value = "samples")
    private String sample;
    private final String[] samples = { "builtInFilteringAndSorting", "sorting-using-column" };

    private SortMode sortMode = SortMode.multi;

    private SortingState sortingStateMen;
    private SortingState sortingStateWomen;

    @Override
    public String getComponentTestPagePath() {
        return format("richCollapsibleSubTable/{0}.xhtml", sample);
    }

    public DataTableWithCSTWithBuiltInSortingHeader getDataTable() {
        return sample.equals("builtInFilteringAndSorting") ? dataTableWithCSTWithBuiltInSortingHeader : dataTableWithCSTWithSortingHeader;
    }

    private void testSorting(boolean isSingle) {
        if (isSingle) {
            sortMode = SortMode.single;
        } else {
            sortMode = SortMode.multi;
        }
        attsSetter()
            .setAttribute(CollapsibleSubTableAttributes.rows).toValue(rows)
            .setAttribute(CollapsibleSubTableAttributes.sortMode).toValue(sortMode)
            .asSingleAction().perform();

        sortingStateMen = new SortingState(getEmployees(Boolean.TRUE));
        sortingStateWomen = new SortingState(getEmployees(Boolean.FALSE));

        verifySortingInBothSubTables(SortBy.NAME);
        verifySortingInBothSubTables(SortBy.TITLE);
        verifySortingInBothSubTables(SortBy.NAME);
        verifySortingInBothSubTables(SortBy.TITLE);
    }

    @Test
    @Templates(exclude = "uiRepeat")
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-11302")
    public void testSortingMulti() {
        testSorting(false);
    }

    @Test
    @Templates("uiRepeat")// RFPL-4156
    @Skip(expressions = {
        @AndExpression({ On.Container.Tomcat7.class, On.JSF.Mojarra.class }),
        @AndExpression({ On.Container.Tomcat8.class, On.JSF.Mojarra.class })
    })
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-11302")
    public void testSortingMultiInUiRepeat() {
        testSortingMulti();
    }

    @Test
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-11302")
    public void testSortingSingle() {
        testSorting(true);
    }

    private void verifySortingInBothSubTables(final SortBy by) {
        getDataTable().sortBy(by);

        sortingStateMen.sortEmployees(sortMode, by);
        sortingStateWomen.sortEmployees(sortMode, by);
        verifySortingInSubTable(sortingStateMen, Boolean.TRUE);
        verifySortingInSubTable(sortingStateWomen, Boolean.FALSE);
    }

    private void verifySortingInSubTable(final SortingState state, boolean isMaleTable) {
        List<Employee> expectedSortedEmployees = state.getSortedEmployees().subList(0, rows);

        CollapsibleSubTableWithEmployees table = getSubTable(isMaleTable);
        int rowCount = table.advanced().getNumberOfVisibleRows();
        assertEquals(rowCount, expectedSortedEmployees.size());

        EmployeeRecord entry;
        for (int i = 0; i < rowCount; i++) {
            entry = table.getRow(i);
            assertEquals(entry.getName(), expectedSortedEmployees.get(i).getName());
            assertEquals(entry.getTitle(), expectedSortedEmployees.get(i).getTitle());
        }
    }

    public static enum SortBy {

        NAME, TITLE, BIRTHDAY;
    }

    public static class DataTableWithCSTWithBuiltInSortingHeader extends DataTableWithCST {

        @FindByJQuery(".rf-cst-shdr .rf-dt-srt-btn:eq(2)")
        private WebElement sortByDate;
        @FindByJQuery(value = ".rf-cst-shdr:eq(1) .rf-dt-srt-btn:eq(2)")
        private WebElement sortByDate2;
        @FindByJQuery(value = ".rf-cst-shdr .rf-dt-srt-btn:eq(0)")
        private WebElement sortByName;
        @FindByJQuery(".rf-cst-shdr:eq(1) .rf-dt-srt-btn:eq(0)")
        private WebElement sortByName2;
        @FindByJQuery(value = ".rf-cst-shdr .rf-dt-srt-btn:eq(1)")
        private WebElement sortByTitle;
        @FindByJQuery(".rf-cst-shdr:eq(1) .rf-dt-srt-btn:eq(1)")
        private WebElement sortByTitle2;

        public void sortBy(SortBy by) {
            switch (by) {
                case NAME:
                    Graphene.guardAjax(sortByName).click();
                    Graphene.guardAjax(sortByName2).click();
                    break;
                case TITLE:
                    Graphene.guardAjax(sortByTitle).click();
                    Graphene.guardAjax(sortByTitle2).click();
                    break;
                case BIRTHDAY:
                    Graphene.guardAjax(sortByDate).click();
                    Graphene.guardAjax(sortByDate2).click();
                    break;
                default:
                    throw new UnsupportedOperationException("unknown sortBy " + by);
            }
        }
    }

    public static class DataTableWithCSTWithSortingHeader extends DataTableWithCSTWithBuiltInSortingHeader {

        @FindByJQuery(".rf-dt-hdr a:eq(0)")
        private WebElement sortByName;
        @FindByJQuery(".rf-dt-hdr a:eq(1)")
        private WebElement sortByTitle;
        @FindByJQuery(".rf-dt-hdr a:eq(2)")
        private WebElement sortByDate;

        @Override
        public void sortBy(SortBy by) {
            switch (by) {
                case NAME:
                    Graphene.guardAjax(sortByName).click();
                    break;
                case TITLE:
                    Graphene.guardAjax(sortByTitle).click();
                    break;
                case BIRTHDAY:
                    Graphene.guardAjax(sortByDate).click();
                    break;
                default:
                    throw new UnsupportedOperationException("unknown sortBy " + by);
            }
        }
    }

    public static class SortingState {

        private final List<Employee> initialEmployees;
        private List<Employee> sortedEmployees;
        private final LinkedHashMap<SortBy, Boolean> sortPriority = new LinkedHashMap<SortBy, Boolean>();

        public SortingState(List<Employee> initialEmployees) {
            this.initialEmployees = initialEmployees;
        }

        public List<Employee> sortEmployees(final SortMode sortMode, final SortBy by) {
            sortedEmployees = Lists.newArrayList(initialEmployees);
            if (sortMode.equals(SortMode.single) && !sortPriority.containsKey(by)) {
                sortPriority.clear();
            }

            if (sortPriority.containsKey(by)) {
                Boolean val = sortPriority.get(by);
                if (sortPriority.size() > 1) {// need to add it to the end of the collection if the value is there already
                    sortPriority.remove(by);
                    sortPriority.put(by, !val);
                }
            } else {
                sortPriority.put(by, !sortPriority.containsKey(by));
            }
            sort(sortedEmployees, sortMode);
            return sortedEmployees;
        }

        public List<Employee> getSortedEmployees() {
            return sortedEmployees;
        }

        private void sort(List<Employee> list, final SortMode sortMode) {
            Collections.sort(list, new Comparator<Employee>() {
                @Override
                public int compare(Employee e1, Employee e2) {
                    if (sortMode == SortMode.single) {
                        SortBy sortBy = (SortBy) sortPriority.keySet().toArray()[0];
                        return _compare(sortPriority.get(sortBy), sortBy, e1, e2);
                    } else {
                        int result;
                        for (SortBy sortBy : sortPriority.keySet()) {
                            result = _compare(sortPriority.get(sortBy), sortBy, e1, e2);
                            if (result != 0) {
                                return result;
                            }
                        }
                        return 0;
                    }
                }
            });
        }

        private int _compare(boolean isAscending, final SortBy by, Employee e1, Employee e2) {
            int result;
            switch (by) {
                case BIRTHDAY:
                    Date d1 = e1.getBirthdate();
                    Date d2 = e2.getBirthdate();
                    d1 = (d1 == null ? new Date(0) : d1);
                    d2 = (d2 == null ? new Date(0) : d2);
                    result = d1.compareTo(d2);
                    break;
                case NAME:
                    result = e1.getName().compareToIgnoreCase(e2.getName());
                    break;
                case TITLE:
                    result = e1.getTitle().compareToIgnoreCase(e2.getTitle());
                    break;
                default:
                    throw new UnsupportedOperationException("unknown sortBy " + by);
            }
            return isAscending ? result : -result;
        }
    }
}
