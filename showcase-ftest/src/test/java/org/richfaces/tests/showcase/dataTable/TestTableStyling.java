/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.dataTable;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTableStyling extends AbstractDataIterationWithCars {

    /*
     * Locators
     * ************************************************************************************************************
     */
    JQueryLocator firstRow = jq("tbody.rf-dt-b tr:eq(0)");
    JQueryLocator tenthRow = jq("tbody.rf-dt-b tr:eq(9)");
    JQueryLocator lastRow = jq("tbody.rf-dt-b tr:last");
    JQueryLocator tbody = jq("tbody.rf-dt-b");

    /*
     * Since the samples with the cars are created dynamically, I am testing only whether rows in the table are non
     * empty strings, and then whether the class of row is changed, when the mouse is over them
     */
    @Test
    public void testTableContainsSomeData() {

        assertFalse(testWhetherTableContainsNonEmptyStrings(tbody), "The table should contain some data!");

    }

    @Test
    public void testAllRowsHighlighting() {

        assertTrue(isAllRowsHighLightedWhenMouseIsOverThem(),
            "The rows should be highlighted when the mouse is over them!");

    }

    /*
     * help methods
     * ******************************************************************************************************
     * *************************
     */

    private boolean isAllRowsHighLightedWhenMouseIsOverThem() {

        JQueryLocator trs = jq(tbody.getRawLocator() + " > tr");

        for (Iterator<JQueryLocator> i = trs.iterator(); i.hasNext();) {

            boolean result = isRowHighlighted(i.next());

            if (!result) {

                return false;
            }

        }

        return true;
    }

    /**
     * Tests whether the row is higlighted when mouse is over it, the class of the row is changed, it then contains
     * active row string
     *
     * @param row
     * @return true if the row is highlighted, false otherwise
     */
    private boolean isRowHighlighted(JQueryLocator row) {

        selenium.mouseOver(row);

        String rowClass = (selenium.getAttribute(row.getAttribute(Attribute.CLASS)));

        selenium.mouseOut(row);

        return rowClass.contains("active-row");
    }

    @Override
    protected String getSampleLabel() {
        return "Styling";
    }
}
