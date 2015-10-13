package org.richfaces.tests.metamer.ftest.richDataTable;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.MultipleCoversAttributes;
import org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.SortingDT;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestDataTableSortingBuiltIn extends DataTableSortingTest {

    private final Action ajaxAction = new Action() {
        @Override
        public void perform() {
            getTable().getHeader().sortByName(true);
        }
    };

    @FindByJQuery(value = "table.rf-dt[id$=richDataTable]")
    private SortingDT table;

    @Override
    public String getComponentTestPagePath() {
        return "richDataTable/builtInFilteringAndSorting.xhtml";
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
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        testLimitRender(ajaxAction);
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        testRender(ajaxAction);
    }

    @Test
    @MultipleCoversAttributes({
        @CoversAttributes("sortMode"),
        @CoversAttributes(value = { "sortBy", "sortOrder", "sortType" }, attributeEnumClass = ColumnAttributes.class)
    })
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
