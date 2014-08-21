package org.richfaces.tests.metamer.ftest.richDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFilteringTest;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.FilteringDT;
import org.testng.annotations.Test;

public class TestDataTableBuiltInFiltering extends DataTableFilteringTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private FilteringDT table;

    @Override
    protected FilteringDT getTable() {
        return table;
    }

    @Test
    public void testFilterName() {
        super.testFilterName(true);
    }

    @Test
    public void testFilterTitle() {
        super.testFilterTitle(true);
    }

    @Test
    public void testNumberOfKids() {
        super.testFilterNumberOfKindsBuiltIn();
    }

    @Test
    public void testCombination() {
        super.testFilterCombinations(true);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/builtInFiltering.xhtml");
    }

}
