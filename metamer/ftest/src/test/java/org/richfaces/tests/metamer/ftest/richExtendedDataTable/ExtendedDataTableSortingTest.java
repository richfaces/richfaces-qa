package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.dataScroller.DataScroller;
import org.richfaces.model.SortMode;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableSortingTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SortingEDT;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

public abstract class ExtendedDataTableSortingTest extends DataTableSortingTest {

    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private SortingEDT table;

    @Override
    protected SortingEDT getTable() {
        return table;
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-7872")
    public void testShowColumnControlHideAllColumnsAndScroll() {
        setAttribute("showColumnControl", true);
        getTable().getHeader().openColumnControl().hideAllColumns();
        // check
        assertNotVisible(getTable().advanced().getTableBodyElement(), "Table body should not be visible");
        assertTrue(getTable().getHeader().openColumnControl().areAllColumnsHidden());

        // do some scrolling
        Graphene.guardAjax(dataScroller2).switchTo(DataScroller.DataScrollerSwitchButton.NEXT);
        // check
        assertNotVisible(getTable().advanced().getTableBodyElement(), "Table body should not be visible");
        assertTrue(getTable().getHeader().openColumnControl().areAllColumnsHidden());

        // do some scrolling
        Graphene.guardAjax(dataScroller2).switchTo(DataScroller.DataScrollerSwitchButton.LAST);
        Graphene.guardAjax(dataScroller2).switchTo(DataScroller.DataScrollerSwitchButton.PREVIOUS);
        // check
        assertNotVisible(getTable().advanced().getTableBodyElement(), "Table body should not be visible");
        assertTrue(getTable().getHeader().openColumnControl().areAllColumnsHidden());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-7872")
    public void testShowColumnControlWithSorting() {
        setAttribute("showColumnControl", true);
        setAttribute("style", "z-index: 50;");
        setAttribute("sortMode", SortMode.single);

        // hide column with title
        getTable().getHeader().openColumnControl().hideColumn(ChoicePickerHelper.byVisibleText().contains("title"));

        sortByColumn(COLUMN_SEX);
        verifySortingByColumns(Sets.newHashSet(2), "sex");// 2 = column with title
        // check column visibility stays unchecked
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("title")));

        // hide column with sex
        getTable().getHeader().openColumnControl().hideColumn(ChoicePickerHelper.byVisibleText().contains("sex"));

        sortByColumn(COLUMN_NUMBER_OF_KIDS1);
        verifySortingByColumns(Sets.newHashSet(0, 2), "numberOfKids");// 0 = column with sex, 2 = column with title
        // check column visibility stays unchecked
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("sex")));
        assertFalse(getTable().getHeader().openColumnControl().isColumnChecked(ChoicePickerHelper.byVisibleText().contains("title")));
    }
}
