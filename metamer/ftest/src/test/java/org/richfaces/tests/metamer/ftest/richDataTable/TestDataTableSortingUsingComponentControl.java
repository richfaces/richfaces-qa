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
package org.richfaces.tests.metamer.ftest.richDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import org.jboss.arquillian.graphene.findby.FindByJQuery;

import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SortingDT;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDataTableSortingUsingComponentControl extends DataTableSortingTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private SortingDT table;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/sorting-using-component-control.xhtml");
    }

    @Override
    public SortingDT getTable() {
        return table;
    }

    @Test
    public void testSortModeSingle() {
        super.testSortModeSingle();
    }

    @Test
    public void testSortModeSingleReverse() {
        super.testSortModeSingleReverse();
    }

    @Test(groups = {"Future"})
    @IssueTracking({"https://issues.jboss.org/browse/RF-9932",
        "http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790"})
    public void testSortModeSingleRerenderAll() {
        super.testSortModeSingleRerenderAll();
    }

    @Test
    public void testSortModeSingleFullPageRefresh() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test
    public void testSortModeMulti() {
        super.testSortModeMulti();
    }

    @Test
    public void testSortModeMultiReverse() {
        super.testSortModeMultiReverse();
    }

    @Test
    public void testSortModeMultiReplacingOldOccurences() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test(groups = {"Future"})
    @IssueTracking({"https://issues.jboss.org/browse/RF-9932",
        "http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790"})
    public void testSortModeMultiRerenderAll() {
        super.testSortModeMultiRerenderAll();
    }

    @Test
    public void testSortModeMultiFullPageRefresh() {
        super.testSortModeMultiFullPageRefresh();
    }
}
