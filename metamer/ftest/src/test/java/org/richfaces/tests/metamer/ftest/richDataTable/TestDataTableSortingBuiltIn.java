package org.richfaces.tests.metamer.ftest.richDataTable;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SortingDT;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestDataTableSortingBuiltIn extends DataTableSortingTest {

    @FindByJQuery("table.rf-dt[id$=richDataTable]")
    private SortingDT table;

    @Override
    public SortingDT getTable() {
        return table;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/builtInFilteringAndSorting.xhtml";
    }

    @BeforeTest
    public void setUp() {
        super.setBuiltIn(true);
    }

    @Test
    @CoversAttributes("sortMode")
    @Override
    public void testSortModeMulti() {
        super.testSortModeMulti();
    }

    @Test
    @CoversAttributes("sortMode")
    @Override
    public void testSortModeMultiFullPageRefresh() {
        super.testSortModeMultiFullPageRefresh();
    }

    @Test
    @CoversAttributes("sortMode")
    @Override
    public void testSortModeMultiReplacingOldOccurences() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test
    @CoversAttributes("sortMode")
    @Override
    public void testSortModeMultiReverse() {
        super.testSortModeMultiReverse();
    }

    @Test
    @CoversAttributes("sortMode")
    @Override
    public void testSortModeSingle() {
        super.testSortModeSingle();
    }

    @Test
    @CoversAttributes("sortMode")
    @Override
    public void testSortModeSingleFullPageRefresh() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test
    @CoversAttributes("sortMode")
    @Override
    public void testSortModeSingleReverse() {
        super.testSortModeSingleReverse();
    }
}
