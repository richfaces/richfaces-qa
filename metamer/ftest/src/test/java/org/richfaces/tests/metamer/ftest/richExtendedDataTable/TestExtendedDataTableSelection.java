/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.Keys.SHIFT;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.IntRange;
import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractDataTableTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.page.SelectionPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestExtendedDataTableSelection extends AbstractDataTableTest {

    private final Collection<Integer> selected = new TreeSet<Integer>();

    @Page
    private SelectionPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/selection.xhtml");
    }

    @BeforeMethod
    public void clearSelected() {
        selected.clear();
    }

    @Test
    public void testSimpleSelection() {
        page.selectRow(2);

        assertEquals(page.getActualPreviousSelection(), expectedSelection());
        assertEquals(page.getActualCurrentSelection(), expectedSelection(2));
    }

    @Test
    @Templates(exclude = {"richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "a4jRepeat"})
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
    @RegressionTest("https://issues.jboss.org/browse/RF-10256")
    @Templates(value = {"richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat"})
    public void testMultiSelectionUsingControlIterationComponents() {
        testMultiSelectionUsingControl();
    }

    @Test
    public void testMultiSelectionUsingShiftOnOnePage() {
        IntRange range = new IntRange(2, 5);

        page.selectRow(range.getMinimumInteger());
        page.selectRow(range.getMaximumInteger(), SHIFT);

        assertEquals(page.getActualPreviousSelection(), expectedSelection(range.getMinimumInteger()));
        assertEquals(page.getActualCurrentSelection(), selection(range));
    }

    @Test
    @Templates(exclude = {"richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "a4jRepeat"})
    public void testMultiSelectionUsingShiftBetweenPagesInReversedOrder() {
        IntRange range = new IntRange(12, 35);

        page.selectRow(range.getMaximumInteger());
        page.selectRow(range.getMinimumInteger(), SHIFT);

        assertEquals(page.getActualPreviousSelection(), expectedSelection(range.getMaximumInteger()));
        assertEquals(page.getActualCurrentSelection(), selection(range));
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10256")
    @Templates(value = {"richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat"})
    public void testMultiSelectionUsingShiftBetweenPagesInReversedOrderIterationComponents() {
        testMultiSelectionUsingShiftBetweenPagesInReversedOrder();
    }

    @Test(groups = "Future")
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
    @Templates(exclude = {"richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "a4jRegion", "a4jRepeat"})
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
    @RegressionTest("https://issues.jboss.org/browse/RF-10256")
    @Templates(value = {"richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat"})
    public void testMultiSelectionRemovingUsingCtrlIterationComponents() {
        testMultiSelectionRemovingUsingCtrl();
    }

    @Test(groups = "Future")
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
    public void testSelectSomeRowAndThenSelectAllWithKeyShortcut() {
        page.selectRow(2);
        page.selectAllWithCrtlAndA();
        selected.addAll(selection(new IntRange(0, 49)));
        verifySelected();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13941")
    public void testSelectAllWithKeyShortcut() {
        page.selectAllWithCrtlAndA();
        selected.addAll(selection(new IntRange(0, 49)));
        verifySelected();
    }

    private Collection<Integer> order(int... selection) {
        return Arrays.asList(ArrayUtils.toObject(selection));
    }

    private Collection<Integer> expectedSelection(int... selection) {
        return new TreeSet<Integer>(order(selection));
    }

    private Collection<Integer> selection(IntRange range) {
        return expectedSelection(range.toArray());
    }

    private void verifySelected() {
        assertEquals(page.getActualCurrentSelection(), selected);
    }
}
