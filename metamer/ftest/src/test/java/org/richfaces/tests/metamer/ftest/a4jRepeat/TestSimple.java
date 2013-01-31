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
package org.richfaces.tests.metamer.ftest.a4jRepeat;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.repeatAttributes;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22683 $
 */
public class TestSimple extends AbstractGrapheneTest {

    protected static final int ELEMENTS_TOTAL = 20;

    SimpleModel model;

    @Inject
    @Use(empty = false)
    Integer first;

    @Inject
    @Use(empty = false)
    Integer rows;

    int expectedBegin;
    int displayedRows;
    int expectedEnd;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jRepeat/simple.xhtml");
    }

    @BeforeMethod(alwaysRun = true, dependsOnMethods={"loadPage"})
    public void prepareAttributes() {
        model = new SimpleModel();

        if (first != null) {
            repeatAttributes.set(RepeatAttributes.first, first);
        }
        if (rows != null) {
            repeatAttributes.set(RepeatAttributes.rows, rows);
        }
    }

    @Test
    public void testRenderedAttribute() {
        repeatAttributes.set(RepeatAttributes.rendered, "false");
        assertEquals(model.isRendered(), false);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10589")
    @Use(field = "first", ints = { -1, 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL, ELEMENTS_TOTAL + 1 })
    public void testFirstAttribute() {
        verifyRepeat();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10589")
    @Use(field = "rows", ints = { -2, -1, 0, 1, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL,
            ELEMENTS_TOTAL + 1 })
    public void testRowsAttribute() {
        verifyRepeat();
    }

    private void verifyRepeat() {
        countExpectedValues();
        verifyCounts();
        verifyRows();
    }

    private void verifyCounts() {
        assertEquals(model.getTotalRowCount(), displayedRows);
        if (displayedRows > 0) {
            assertEquals(model.getIndex(1), expectedBegin);
        }
    }

    private void verifyRows() {
        int rowCount = model.getTotalRowCount();
        for (int position = 1; position <= rowCount; position++) {
            assertEquals(model.getBegin(position), expectedBegin, "begin");
            assertEquals(model.getEnd(position), expectedEnd, "end");
            assertEquals(model.getIndex(position), expectedBegin + position - 1, "index");
            assertEquals(model.getCount(position), position, "count");
            assertEquals(model.isFirst(position), position == 1, "first");
            assertEquals(model.isLast(position), position == rowCount, "last");
            assertEquals(model.isEven(position), (position % 2) == 0, "even");
            assertEquals(model.getRowCount(position), ELEMENTS_TOTAL, "rowCount");
        }
    }

    private void countExpectedValues() {

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
