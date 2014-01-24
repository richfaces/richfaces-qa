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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class TestMatrix extends AbstractWebDriverTest {

    @Page
    private MatrixPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jRepeat/matrix.xhtml");
    }

    @BeforeMethod(groups = "smoke")
    public void initializeTest() {
        page.initializeMatrix();
    }

    @Test
    public void testInitialStateOfMatrix() {
        assertEquals(page.inputRows.size(), MatrixPage.ROWS_COUNT, "number of rows in input matrix");
        assertEquals(page.outputRows.size(), MatrixPage.ROWS_COUNT, "number of rows in output matrix");

        assertEquals(page.inputRows.get(1).findElements(MatrixPage.BY_CELL).size(), MatrixPage.COLUMNS_COUNT,
                "number of columns in the second row of input matrix");
        assertEquals(page.inputRows.get(3).findElements(MatrixPage.BY_CELL).size(), MatrixPage.COLUMNS_COUNT,
                "number of columns in the fourth row of input matrix");

        assertEquals(page.outputRows.get(1).findElements(MatrixPage.BY_CELL).size(), MatrixPage.COLUMNS_COUNT,
                "number of columns in the second row of output matrix");
        assertEquals(page.outputRows.get(3).findElements(MatrixPage.BY_CELL).size(), MatrixPage.COLUMNS_COUNT,
                "number of columns in the fourth row of output matrix");
    }

    @Test(groups = "smoke")
    public void testIncrementing() {
        page.incrementValue(0, 3);
        page.incrementValue(0, 3);

        page.incrementValue(3, 2);
        page.incrementValue(3, 2);
        page.incrementValue(3, 2);

        page.checkMatrix();
    }

    @Test
    public void testDecrementing() {
        page.decrementValue(0, 3);
        page.decrementValue(0, 3);

        page.decrementValue(3, 2);
        page.decrementValue(3, 2);
        page.decrementValue(3, 2);

        page.checkMatrix();
    }

    @Test
    public void testManualInput() {
        page.changeValue(0, 3, 42);
        page.changeValue(3, 2, -127);
        page.changeValue(1, 1, 89);

        page.checkMatrix();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12512")
    public void testClear() {
        page.changeValue(0, 3, 42);
        page.changeValue(3, 2, -127);
        page.changeValue(1, 1, 89);

        page.clearValue(0, 3);
        page.clearValue(3, 2);
        page.clearValue(1, 1);

        page.checkMatrix();
    }

    @Test
    public void testMatrixAfterRerender() {
        new MatrixReloadTester().testRerenderAll();
    }

    @Test
    public void testMatrixAfterRefresh() {
        new MatrixReloadTester().testFullPageRefresh();
    }

    private class MatrixReloadTester extends ReloadTester<int[]> {

        @Override
        public void doRequest(int[] coords) {
            page.changeValue(coords[0], coords[1], 12);
        }

        @Override
        public void verifyResponse(final int[] coords) {
            page.checkMatrix();
        }

        @Override
        public int[][] getInputValues() {
            return new int[][]{ { 0, 3 } };
        }
    }
}
