package org.richfaces.tests.metamer.ftest.richDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SortingDT;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestDataTableSortingBuiltIn extends DataTableSortingTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private SortingDT table;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/builtInFiltering.xhtml");
    }

    @Override
    public SortingDT getTable() {
        return table;
    }

    @BeforeTest
    public void setUp() {
        super.setBuiltIn(true);
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

    @Test
    @Override
    public void testSortModeMultiFullPageRefresh() {
        super.testSortModeMultiFullPageRefresh();
    }
}
