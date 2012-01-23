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
package org.richfaces.tests.metamer.ftest.richDataTable;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.model.DataTable;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22664 $
 */
public class TestDataTableSortingUsingColumn extends DataTableSortingTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/sorting-using-column.xhtml");
    }

    @BeforeClass
    public void setupModel() {
        model = new DataTable(pjq("table.rf-dt[id$=richDataTable]"));
    }

    @Test
    @Override
    public void testSortModeSingle() {
        super.testSortModeSingle();
    }

    @Test
    @Override
    public void testSortModeSingleReverse() {
        super.testSortModeSingleReverse();
    }

    @Test
    @Override
    public void testSortModeSingleDoesntRememberOrder() {
        super.testSortModeSingleDoesntRememberOrder();
    }

    @Test(groups = { "4.3" })
    @Override
    @IssueTracking({ "https://issues.jboss.org/browse/RF-9932",
        "http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790" })
    public void testSortModeSingleRerenderAll() {
        super.testSortModeSingleRerenderAll();
    }

    @Test
    @Override
    public void testSortModeSingleFullPageRefresh() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test
    @Override
    public void testSortModeMulti() {
        super.testSortModeMulti();
    }

    @Test
    @Override
    public void testSortModeMultiReverse() {
        super.testSortModeMultiReverse();
    }

    @Test
    @Override
    public void testSortModeMultiReplacingOldOccurences() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test(groups = { "4.3" })
    @Override
    @IssueTracking({ "https://issues.jboss.org/browse/RF-9932",
        "http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790" })
    public void testSortModeMultiRerenderAll() {
        super.testSortModeMultiRerenderAll();
    }

    @Test
    @Override
    public void testSortModeMultiFullPageRefresh() {
        super.testSortModeMultiFullPageRefresh();
    }
}
