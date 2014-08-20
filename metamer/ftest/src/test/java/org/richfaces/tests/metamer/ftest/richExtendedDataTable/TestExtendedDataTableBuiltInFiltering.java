package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFilteringTest;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.FilteringEDT;
import org.testng.annotations.Test;

public class TestExtendedDataTableBuiltInFiltering extends DataTableFilteringTest {

    @FindByJQuery("div.rf-edt[id$=richEDT]")
    private FilteringEDT table;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/builtInFiltering.xhtml");
    }

    @Override
    protected FilteringEDT getTable() {
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

}
