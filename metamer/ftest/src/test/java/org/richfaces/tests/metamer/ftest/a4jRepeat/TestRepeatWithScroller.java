/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jRepeat;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRepeatWithScroller extends AbstractWebDriverTest {

    protected static final int ELEMENTS_TOTAL = 20;

    @Page
    private SimplePage page;

    private final Attributes<RepeatAttributes> repeatAttributes = getAttributes();

    @Override
    public String getComponentTestPagePath() {
        return "a4jRepeat/scroller.xhtml";
    }

    @Test
    @Templates(exclude = { "richCollapsibleSubTable", "richDataGrid", "richExtendedDataTable", "uiRepeat", "a4jRepeat" })
    @CoversAttributes({ "iterationStatusVar", "rowKeyVar", "stateVar" })
    public void testIterationVariables() {
        final int rows = 5;
        repeatAttributes.set(RepeatAttributes.rows, rows);
        int[] pages = new int[] { 2, 4, 1, 2 };
        int actPosition, position, rowCount;

        for (int testedPage : pages) {
            if (page.getDataScroller().getActivePageNumber() != testedPage) {
                page.getDataScroller().switchTo(testedPage);
                page.resetCachedTexts();
            }
            int pageIndex = testedPage - 1;
            rowCount = page.getRowsElements().size();
            assertEquals(rowCount, rows, "There should be 5 rows on each page.");
            int begin = pageIndex * rows;
            int end = pageIndex * rows + rows - 1;
            for (position = 0; position < rowCount; position++) {
                actPosition = position + pageIndex * rows;
                assertEquals(page.getBegin(position), begin, "begin");
                assertEquals(page.getEnd(position), end, "end");
                assertEquals(page.getIndex(position), actPosition, "index");
                assertEquals(page.getCount(position), position + 1, "count");
                assertEquals(page.isFirst(position), position == 0, "first");
                assertEquals(page.isLast(position), position == rowCount - 1, "last");
                assertEquals(page.isEven(position), (position % 2) != 0, "even");
                assertEquals(page.getRowCount(position), ELEMENTS_TOTAL, "rowCount");
                assertEquals(page.getRowKeyVar(position), actPosition, "rowKeyVar");
                assertEquals(page.getFirstRowFromStateVar(position), pageIndex * rows, "firstRowFromStateVar");
            }
        }
    }
}
