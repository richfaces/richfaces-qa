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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.page.SelectionPage;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RegressionTest("https://issues.jboss.org/browse/RF-14277")
public class TestExtendedDataTableJSAPISelectAndDeselect extends AbstractWebDriverTest {

    @Page
    private SelectionPage page;

    private final Attributes<ExtendedDataTableAttributes> tableAttributes = getAttributes();

    private List<Integer> arrayToList(int... indexes) {
        List<Integer> r = Lists.newArrayList();
        for (int i : indexes) {
            r.add(i);
        }
        return r;
    }

    @BeforeMethod
    public void clearSelected() {
        page.setTableAttributes(tableAttributes);
    }

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/selection.xhtml";
    }

    @Test
    public void testOnselectionEventsAreTriggered() {
        testRequestEventsBefore("onbeforeselectionchange", "onselectionchange");
        Graphene.guardAjax(page.getSelectRowsUsingIndexJSAPIButton()).click();
        testRequestEventsAfter("onbeforeselectionchange", "onselectionchange");
        verifySelectedBefore();
        verifySelectedNow(2);

        testRequestEventsBefore("onbeforeselectionchange", "onselectionchange");
        Graphene.guardAjax(page.getDeselectRowJSAPIButton()).click();
        testRequestEventsAfter("onbeforeselectionchange", "onselectionchange");
        verifySelectedBefore(2);
        verifySelectedNow();
    }

    @Test
    public void testSelectRowsAndDeselectRow() {
        final int rows = Integer.parseInt(tableAttributes.get(ExtendedDataTableAttributes.rows));
        for (int p : new int[] { 1, 2 }) {
            if (p == 2) {
                Graphene.guardAjax(page.getDataScroller()).switchTo(p);
            }
            int pageConstant = rows * (p - 1);
            Graphene.guardAjax(page.getSelectRowsUsingIndexJSAPIButton()).click();
            verifySelectedBefore();
            verifySelectedNow(2 + pageConstant);

            Graphene.guardAjax(page.getDeselectRowJSAPIButton()).click();
            verifySelectedBefore(2 + pageConstant);
            verifySelectedNow();

            Graphene.guardAjax(page.getSelectRowsUsingRangeJSAPIButton()).click();
            verifySelectedBefore();
            verifySelectedNow(1 + pageConstant, 2 + pageConstant, 3 + pageConstant, 4 + pageConstant, 5 + pageConstant);

            Graphene.guardAjax(page.getDeselectRowJSAPIButton()).click();
            verifySelectedBefore(1 + pageConstant, 2 + pageConstant, 3 + pageConstant, 4 + pageConstant, 5 + pageConstant);
            verifySelectedNow(1 + pageConstant, 3 + pageConstant, 4 + pageConstant, 5 + pageConstant);

            Graphene.guardAjax(page.getSelectRowsUsingIndexJSAPIButton()).click();
            verifySelectedBefore(1 + pageConstant, 3 + pageConstant, 4 + pageConstant, 5 + pageConstant);
            verifySelectedNow(2 + pageConstant);

            Graphene.guardAjax(page.getDeselectRowJSAPIButton()).click();
            verifySelectedBefore(2 + pageConstant);
            verifySelectedNow();
        }
    }

    private void verifySelectedBefore(int... indexes) {
        assertEquals(page.getActualPreviousSelection(), arrayToList(indexes));
    }

    private void verifySelectedNow(int... indexes) {
        assertEquals(page.getActualCurrentSelection(), arrayToList(indexes));
        for (int i : indexes) {
            assertTrue(page.getTable().getRow(page.getRowForIndex(i)).getRootElement().getAttribute("class").contains("rf-edt-r-sel"));
        }
    }
}
