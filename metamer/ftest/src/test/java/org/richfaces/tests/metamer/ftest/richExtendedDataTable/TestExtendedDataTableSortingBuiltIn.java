package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SortingEDT;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestExtendedDataTableSortingBuiltIn extends DataTableSortingTest {

    @FindByJQuery("div.rf-edt[id$=richEDT]")
    private SortingEDT table;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/builtInFiltering.xhtml");
    }

    @Override
    protected SortingEDT getTable() {
        return table;
    }

    @BeforeTest
    public void setUp() {
        super.setBuiltIn(true);
    }

    @Test
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeSingle() {
        super.testSortModeSingle();
    }

    @Test(groups = "Future")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeSingleInEDT() {
        super.testSortModeSingle();
    }

    @Test
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeSingleReverse() {
        super.testSortModeSingleReverse();
    }

    @Test(groups = "Future")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeSingleReverseInEDT() {
        super.testSortModeSingleReverse();
    }

    @Test
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeSingleDoesntRememberOrder() {
        super.testSortModeSingleDoesntRememberOrder();
    }

    @Test(groups = "Future")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeSingleDoesntRememberOrderInEDT() {
        super.testSortModeSingleDoesntRememberOrder();
    }

    @Test(groups = { "Future" })
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testSortModeSingleRerenderAll() {
        super.testSortModeSingleRerenderAll();
    }

    @Test
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeSingleFullPageRefresh() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test(groups = "Future")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeSingleFullPageRefreshInEDT() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test
    @Templates(exclude="richExtendedDataTable")
    @Override
    public void testSortModeMulti() {
        super.testSortModeMulti();
    }

    @Test(groups = "Future")
    @Templates(value="richExtendedDataTable")
    public void testSortModeMultiInEDT() {
        super.testSortModeMulti();
    }

    @Test
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeMultiReverse() {
        super.testSortModeMultiReverse();
    }

    @Test(groups = "Future")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeMultiReverseInEDT() {
        super.testSortModeMultiReverse();
    }

    @Test
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeMultiReplacingOldOccurences() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test(groups = "Future")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeMultiReplacingOldOccurencesInEDT() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test(groups = { "Future" })
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testSortModeMultiRerenderAll() {
        super.testSortModeMultiRerenderAll();
    }

    @Test
    @Templates(exclude = "richExtendedDataTable")
    @Override
    public void testSortModeMultiFullPageRefresh() {
        super.testSortModeMultiFullPageRefresh();
    }

    @Test(groups = "Future")
    @Templates(value = "richExtendedDataTable")
    public void testSortModeMultiFullPageRefreshInEDT() {
        super.testSortModeMultiFullPageRefresh();
    }

}
