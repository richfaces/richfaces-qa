/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.Keys.SHIFT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.IntRange;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractDataTableTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.page.SelectionPage;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestExtendedDataTableSelection extends AbstractDataTableTest {

    @Page
    private SelectionPage page;

    private final Collection<Integer> selected = new TreeSet<Integer>();
    private final Attributes<ExtendedDataTableAttributes> tableAttributes = getAttributes();

    @BeforeMethod
    public void clearSelected() {
        selected.clear();
        page.setTableAttributes(tableAttributes);
    }

    private Collection<Integer> expectedSelection(int... selection) {
        return new TreeSet<Integer>(order(selection));
    }

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/selection.xhtml";
    }

    private Collection<Integer> order(int... selection) {
        return Arrays.asList(ArrayUtils.toObject(selection));
    }

    private Collection<Integer> selection(IntRange range) {
        return expectedSelection(range.toArray());
    }

    @Test
    @CoversAttributes({ "selectionMode", // multiple (default value, same as 'null')
        "selection" })
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "a4jRegion" })
    public void testMultiSelectionRemovingUsingCtrl() {
        IntRange range1 = new IntRange(2, 14);

        page.selectRow(range1.getMaximumInteger());
        page.selectRow(range1.getMinimumInteger(), SHIFT);
        selected.addAll(selection(range1));
        verifySelected();

        page.deselectRow(4, CONTROL);
        selected.remove(4);
        verifySelected();

        page.deselectRow(12, CONTROL);
        selected.remove(12);
        verifySelected();
    }

    @Test
    @CoversAttributes("selection")
    @RegressionTest("https://issues.jboss.org/browse/RF-10256")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testMultiSelectionRemovingUsingCtrlIterationComponents() {
        testMultiSelectionRemovingUsingCtrl();
    }

    @Test
    @CoversAttributes("selection")
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-13474")
    @Templates("a4jRegion")
    public void testMultiSelectionRemovingUsingCtrlRegion() {
        // select row on second page, then select row on first page with shift
        IntRange range = new IntRange(3, 34);
        page.selectRow(range.getMaximumInteger());
        page.selectRow(range.getMinimumInteger(), SHIFT);
        selected.addAll(selection(range));
        verifySelected();

        //remove one item from selection on first page
        page.deselectRow(5, CONTROL);
        selected.remove(5);
        verifySelected();

        // switch to page 2 without any selection and assert again
        page.getDataScroller().switchTo(2);
        verifySelected();
    }

    @Test
    @CoversAttributes("selection")
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testMultiSelectionUsingControl() {
        Collection<Integer> forSelection = order(2, 5, 29, 16, 13, 21);

        for (int s : forSelection) {
            page.selectRow(s, CONTROL);

            assertEquals(page.getActualPreviousSelection(), selected);
            selected.add(s);
            assertEquals(page.getActualCurrentSelection(), selected);
        }
    }

    @Test
    @CoversAttributes("selection")
    @RegressionTest("https://issues.jboss.org/browse/RF-10256")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testMultiSelectionUsingControlIterationComponents() {
        testMultiSelectionUsingControl();
    }

    @Test
    @CoversAttributes("selection")
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-9977")
    public void testMultiSelectionUsingCtrlAndShiftCombinations() {
        IntRange range1 = new IntRange(2, 14);
        IntRange range2 = new IntRange(18, 31);

        page.selectRow(range1.getMaximumInteger());
        page.selectRow(range1.getMinimumInteger(), SHIFT);
        selected.addAll(selection(range1));
        verifySelected();

        page.selectRow(range2.getMaximumInteger(), CONTROL);
        selected.addAll(expectedSelection(range2.getMaximumInteger()));
        verifySelected();

        page.selectRow(range2.getMinimumInteger(), CONTROL, SHIFT);
        selected.addAll(selection(range2));
        verifySelected();
    }

    @Test
    @CoversAttributes("selection")
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "uiRepeat" })
    public void testMultiSelectionUsingShiftBetweenPagesInReversedOrder() {
        IntRange range = new IntRange(12, 35);

        page.selectRow(range.getMaximumInteger());
        page.selectRow(range.getMinimumInteger(), SHIFT);

        assertEquals(page.getActualPreviousSelection(), expectedSelection(range.getMaximumInteger()));
        assertEquals(page.getActualCurrentSelection(), selection(range));
    }

    @Test
    @CoversAttributes("selection")
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-13973")
    @Templates(value = "uiRepeat")
    public void testMultiSelectionUsingShiftBetweenPagesInReversedOrderInUiRepeat() {
        testMultiSelectionUsingShiftBetweenPagesInReversedOrder();
    }

    @Test
    @CoversAttributes("selection")
    @RegressionTest("https://issues.jboss.org/browse/RF-10256")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testMultiSelectionUsingShiftBetweenPagesInReversedOrderIterationComponents() {
        testMultiSelectionUsingShiftBetweenPagesInReversedOrder();
    }

    @Test
    @CoversAttributes("selection")
    public void testMultiSelectionUsingShiftOnOnePage() {
        IntRange range = new IntRange(2, 5);

        page.selectRow(range.getMinimumInteger());
        page.selectRow(range.getMaximumInteger(), SHIFT);

        assertEquals(page.getActualPreviousSelection(), expectedSelection(range.getMinimumInteger()));
        assertEquals(page.getActualCurrentSelection(), selection(range));
    }

    @Test
    @CoversAttributes("selection")
    @RegressionTest("https://issues.jboss.org/browse/RF-13941")
    public void testSelectAllWithKeyShortcut() {
        page.selectAllWithCrtlAndA();
        selected.addAll(selection(new IntRange(0, 49)));
        verifySelected();
    }

    @Test
    @CoversAttributes("selection")
    public void testSelectSomeRowAndThenSelectAllWithKeyShortcut() {
        page.selectRow(2);
        page.selectAllWithCrtlAndA();
        selected.addAll(selection(new IntRange(0, 49)));
        verifySelected();
    }

    @Test
    @CoversAttributes({ "selection", "selectionMode" })
    public void testSelectionModeMultipleKeyboardFree() {
        tableAttributes.set(ExtendedDataTableAttributes.selectionMode, "multipleKeyboardFree");

        page.selectRow(2);
        assertEquals(page.getActualPreviousSelection(), expectedSelection());
        assertEquals(page.getActualCurrentSelection(), expectedSelection(2));

        // select another row
        page.selectRow(4);
        assertEquals(page.getActualPreviousSelection(), expectedSelection(2));
        assertEquals(page.getActualCurrentSelection(), expectedSelection(2, 4));

        // select another record using CONTROL (no effect), another record is selected
        page.selectRow(7, Keys.CONTROL);
        assertEquals(page.getActualPreviousSelection(), expectedSelection(2, 4));
        assertEquals(page.getActualCurrentSelection(), expectedSelection(2, 4, 7));

        // select another record using SHIFT (no effect), another record is selected
        page.selectRow(0, Keys.SHIFT);
        assertEquals(page.getActualPreviousSelection(), expectedSelection(2, 4, 7));
        assertEquals(page.getActualCurrentSelection(), expectedSelection(0, 2, 4, 7));

        // deselect previously selected row
        page.deselectRow(4);// selecting and deselecting is performed by the same mechanism -- clicking on the row
        assertEquals(page.getActualPreviousSelection(), expectedSelection(0, 2, 4, 7));
        assertEquals(page.getActualCurrentSelection(), expectedSelection(0, 2, 7));
    }

    @Test
    @Templates("plain")
    @CoversAttributes({ "selection", "selectionMode" })
    public void testSelectionModeNone() {
        tableAttributes.set(ExtendedDataTableAttributes.selectionMode, "none");
        try {
            page.selectRow(2);
            fail("Selection should not be possible when @selectionMode=none");
        } catch (TimeoutException ok) {
        }
    }

    @Test
    @CoversAttributes({ "selection", "selectionMode" })
    public void testSelectionModeSingle() {
        tableAttributes.set(ExtendedDataTableAttributes.selectionMode, "single");

        page.selectRow(2);
        assertEquals(page.getActualPreviousSelection(), expectedSelection());
        assertEquals(page.getActualCurrentSelection(), expectedSelection(2));

        // select another record using CONTROL, only one record is selected
        page.selectRow(4, Keys.CONTROL);
        assertEquals(page.getActualPreviousSelection(), expectedSelection(2));
        assertEquals(page.getActualCurrentSelection(), expectedSelection(4));

        // select another record using SHIFT, only one record is selected
        page.selectRow(7, Keys.SHIFT);
        assertEquals(page.getActualPreviousSelection(), expectedSelection(4));
        assertEquals(page.getActualCurrentSelection(), expectedSelection(7));
    }

    @Test
    @CoversAttributes("selection")
    public void testSimpleSelection() {
        page.selectRow(2);

        assertEquals(page.getActualPreviousSelection(), expectedSelection());
        assertEquals(page.getActualCurrentSelection(), expectedSelection(2));
    }

    private void verifySelected() {
        assertEquals(page.getActualCurrentSelection(), selected);
    }
}
