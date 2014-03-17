/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.a4jRepeat;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class TestRepeat extends AbstractWebDriverTest {

    private final Attributes<RepeatAttributes> repeatAttributes = getAttributes();

    protected static final int ELEMENTS_TOTAL = 20;

    @Page
    private SimplePage page;

    private Integer first;

    private Integer rows;

    int expectedBegin;
    int displayedRows;
    int expectedEnd;

    private Integer[] intsFirst = { -1, 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 };
    private Integer[] intsRows = { -2, -1, 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jRepeat/simple.xhtml");
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        repeatAttributes.set(RepeatAttributes.rendered, "false");
        assertEquals(page.rows.size(), 0);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10589")
    @UseWithField(field = "first", valuesFrom = FROM_FIELD, value = "intsFirst")
    public void testFirst() {
        repeatAttributes.set(RepeatAttributes.first, first);
        verifyRepeat();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10589")
    @UseWithField(field = "rows", valuesFrom = FROM_FIELD, value = "intsRows")
    public void testRows() {
        repeatAttributes.set(RepeatAttributes.rows, rows);
        verifyRepeat();
    }

    private void verifyRepeat() {
        countExpectedValues();
        verifyCounts();
        verifyRows();
    }

    private void verifyCounts() {
        assertEquals(page.rows.size(), displayedRows);
        if (displayedRows > 0) {
            assertEquals(page.getIndex(0), expectedBegin);
        }
    }

    private void verifyRows() {
        int rowCount = page.rows.size();
        for (int position = 0; position < rowCount; position++) {
            assertEquals(page.getBegin(position), expectedBegin, "begin");
            assertEquals(page.getEnd(position), expectedEnd, "end");
            assertEquals(page.getIndex(position), expectedBegin + position, "index");
            assertEquals(page.getCount(position), position + 1, "count");
            assertEquals(page.isFirst(position), position == 0, "first");
            assertEquals(page.isLast(position), position == rowCount - 1, "last");
            assertEquals(page.isEven(position), (position % 2) != 0, "even");
            assertEquals(page.getRowCount(position), ELEMENTS_TOTAL, "rowCount");
        }
    }

    private void countExpectedValues() {
        String firstAtt = repeatAttributes.get(RepeatAttributes.first);
        if (firstAtt != null && !firstAtt.isEmpty()) {
            first = Integer.valueOf(firstAtt);
        } else {
            first = null;
        }
        String rowsAtt = repeatAttributes.get(RepeatAttributes.rows);
        if (rowsAtt != null && !rowsAtt.isEmpty()) {
            rows = Integer.valueOf(rowsAtt);
        } else {
            rows = null;
        }
        // expected begin
        if (first == null || first < 0) {
            expectedBegin = 0;
        } else {
            expectedBegin = first;
        }

        expectedBegin = minMax(0, expectedBegin, ELEMENTS_TOTAL);

        // expected displayed rows
        if (rows == null || rows < 1 || rows > ELEMENTS_TOTAL) {
            displayedRows = ELEMENTS_TOTAL;
        } else {
            displayedRows = rows;
        }

        if (first != null && first < 0) {
            displayedRows = 0;
        }

        displayedRows = min(displayedRows, ELEMENTS_TOTAL - expectedBegin);

        // expected end
        if (rows == null || rows < 1) {
            expectedEnd = ELEMENTS_TOTAL - 1;
        } else {
            expectedEnd = rows - 1;
        }
    }

    private int minMax(int min, int value, int max) {
        return max(0, min(max, value));
    }
}
