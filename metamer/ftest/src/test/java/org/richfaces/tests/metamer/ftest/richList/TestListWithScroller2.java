/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.page.fragments.impl.list.AbstractListComponent;
import org.richfaces.tests.page.fragments.impl.list.RichFacesListItem;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestListWithScroller2 extends AbstractListTest {

    @FindBy(css = "span.rf-ds[id$=scroller2]")
    private RichFacesDataScroller scroller;
    @FindBy(css = "[id$=richList]")
    private TestedList list;

    private static final int ROWS = 20;
    private static final String ROWS_STRING = "20";
    private static final String RANGE_TEMPLATE = "[ firstRow: %s, rows: %s ]";
    private static final String ITERATION_STATUS_TEMPLATE = "[ begin= %d, end= %d, index= %d, count= %d, first= %b, last= %b, even= %b, rowCount= 200 ]";
    private static final List<Integer> pages = Arrays.asList(1, 2, 5, 8, 10);
    private static final List<Integer> items = Arrays.asList(0, 1, 10, 18, 19);

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richList/scroller2.xhtml");
    }

    @Test
    public void testIterationStatusVar() {
        TestedListItem li;
        int maxIndex;
        for (Integer page : pages) {
            if (!page.equals(scroller.getActivePageNumber())) {
                scroller.switchTo(page);
            }
            maxIndex = ROWS * (page - 1);
            for (Integer item : items) {
                li = list.getItems().get(item);
                Assert.assertEquals(li.getIterationStatusVarText(),
                    String.format(ITERATION_STATUS_TEMPLATE, maxIndex, maxIndex + ROWS - 1, item + maxIndex, item + 1, (item == 0), (item == 19), (item % 2 == 1)));
            }
        }
    }

    @Test
    public void testRowKeyVar() {
        TestedListItem li;
        String indexString;
        int index;
        for (Integer page : pages) {
            if (!page.equals(scroller.getActivePageNumber())) {
                scroller.switchTo(page);
            }
            index = ROWS * (page - 1);
            for (Integer item : items) {
                indexString = String.valueOf(index + item);
                li = list.getItems().get(item);
                Assert.assertEquals(li.getRowKeyVarText(), indexString);
            }
        }
    }

    @Test
    public void testStateVar() {
        TestedListItem li;
        String startIndexString;
        for (Integer page : pages) {
            if (!page.equals(scroller.getActivePageNumber())) {
                scroller.switchTo(page);
            }
            startIndexString = String.valueOf(ROWS * (page - 1));
            for (Integer item : items) {
                li = list.getItems().get(item);
                Assert.assertEquals(li.getFirstRowFromStateVarText(), startIndexString);
                Assert.assertEquals(li.getRowsFromStateVarText(), ROWS_STRING);
                Assert.assertEquals(li.getRangeFromStateVarText(), String.format(RANGE_TEMPLATE, startIndexString, ROWS));
            }
        }
    }

    public static class TestedList extends AbstractListComponent<TestedListItem> {
    }

    public static class TestedListItem extends RichFacesListItem {

        @FindBy(css = "[id$=rowKeyVar]")
        private WebElement rowKeyVar;
        @FindBy(css = "[id$=iterationStatusVar]")
        private WebElement iterationStatusVar;
        @FindBy(css = "[id$=rangeFromStateVar]")
        private WebElement rangeFromStateVar;
        @FindBy(css = "[id$=rowsFromStateVar]")
        private WebElement rowsFromStateVar;
        @FindBy(css = "[id$=firstRowFromStateVar]")
        private WebElement firstRowFromStateVar;

        public String getRowKeyVarText() {
            return rowKeyVar.getText();
        }

        public String getIterationStatusVarText() {
            return iterationStatusVar.getText();
        }

        public String getRangeFromStateVarText() {
            return rangeFromStateVar.getText();
        }

        public String getRowsFromStateVarText() {
            return rowsFromStateVar.getText();
        }

        public String getFirstRowFromStateVarText() {
            return firstRowFromStateVar.getText();
        }
    }
}
