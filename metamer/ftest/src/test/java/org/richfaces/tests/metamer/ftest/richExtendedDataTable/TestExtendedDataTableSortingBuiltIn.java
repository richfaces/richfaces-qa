package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SortingEDT;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestExtendedDataTableSortingBuiltIn extends DataTableSortingTest {

    private final Action ajaxAction = new Action() {
        @Override
        public void perform() {
            getTable().getHeader().sortByName(true);
        }
    };

    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private SortingEDT table;

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/builtInFilteringAndSorting.xhtml";
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
    @Override
    public void testSortModeMulti() {
        super.testSortModeMulti();
    }

    @Test
    @Override
    public void testSortModeMultiFullPageRefresh() {
        super.testSortModeMultiFullPageRefresh();
    }

    @Test
    @Override
    public void testSortModeMultiReplacingOldOccurences() {
        super.testSortModeMultiReplacingOldOccurences();
    }

    @Test
    @Skip
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testSortModeMultiRerenderAll() {
        super.testSortModeMultiRerenderAll();
    }

    @Test
    @Override
    public void testSortModeMultiReverse() {
        super.testSortModeMultiReverse();
    }

    @Test
    @Override
    public void testSortModeSingle() {
        super.testSortModeSingle();
    }

    @Test
    @Override
    public void testSortModeSingleFullPageRefresh() {
        super.testSortModeSingleFullPageRefresh();
    }

    @Test
    @Skip
    @Override
    @IssueTracking("https://issues.jboss.org/browse/RF-9932 http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testSortModeSingleRerenderAll() {
        super.testSortModeSingleRerenderAll();
    }

    @Test
    @Override
    public void testSortModeSingleReverse() {
        super.testSortModeSingleReverse();
    }
}
