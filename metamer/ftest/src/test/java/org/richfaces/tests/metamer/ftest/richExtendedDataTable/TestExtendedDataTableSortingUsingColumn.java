/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SortingEDT;
import org.testng.annotations.Test;

/**
 * Extracted EDT template into separate future tests as atm there is a Graphene but preventing from locating the correct element
 * of EDT within another EDT.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22664 $
 */
public class TestExtendedDataTableSortingUsingColumn extends DataTableSortingTest {

    @FindByJQuery("div.rf-edt[id$=richEDT]")
    private SortingEDT table;

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/sorting-using-column.xhtml";
    }

    @Override
    protected SortingEDT getTable() {
        return table;
    }

    @Test
    @CoversAttributes("sortMode")
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeMulti() {
        super.testSortModeMulti();
    }

    @Test
    @CoversAttributes("sortMode")
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeMultiFullPageRefresh() {
        super.testSortModeMultiFullPageRefresh();
    }

    @Test
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-13881")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeMultiFullPageRefreshInEDT() {
        super.testSortModeMultiFullPageRefresh();
    }

    @Test
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-13881")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeMultiInEDT() {
        super.testSortModeMulti();
    }

    @Test
    @CoversAttributes("sortMode")
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeMultiReplacingOldOccurences() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-13881")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeMultiReplacingOldOccurencesInEDT() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test
    @Skip
    @CoversAttributes("sortMode")
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testSortModeMultiRerenderAll() {
        super.testSortModeMultiRerenderAll();
    }

    @Test
    @CoversAttributes("sortMode")
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeMultiReverse() {
        super.testSortModeMultiReverse();
    }

    @Test
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-13881")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeMultiReverseInEDT() {
        super.testSortModeMultiReverse();
    }

    @Test
    @CoversAttributes("sortMode")
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeSingle() {
        super.testSortModeSingle();
    }

    @Test
    @CoversAttributes("sortMode")
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeSingleFullPageRefresh() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-13881")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeSingleFullPageRefreshInEDT() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test
    @CoversAttributes("sortMode")
    @RegressionTest("https://issues.jboss.org/browse/RF-13881")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeSingleInEDT() {
        super.testSortModeSingle();
    }

    @Test
    @Skip
    @CoversAttributes("sortMode")
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testSortModeSingleRerenderAll() {
        super.testSortModeSingleRerenderAll();
    }

    @Test
    @CoversAttributes("sortMode")
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeSingleReverse() {
        super.testSortModeSingleReverse();
    }

    @Test
    @CoversAttributes("sortMode")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13881")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeSingleReverseInEDT() {
        super.testSortModeSingleReverse();
    }
}
