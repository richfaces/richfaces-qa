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
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimpleTable extends AbstractAjocadoTest {

    /*
     * Locators
     * ****************************************************************************************************************
     */
    // to see what does these locators represents please refer to showcase dataTable component, data table basic
    private JQueryLocator firstValueOfTransport = jq("table tbody.rf-cst tr:eq(1) td:eq(3)");
    private JQueryLocator secondValueOfTransport = jq("table tbody.rf-cst tr:eq(2) td:eq(3)");
    private JQueryLocator subTotalOfTransportsForSanJose = jq("table tbody.rf-cst tr:eq(3) td:eq(3)");
    private JQueryLocator subtotalsOfSanJose = jq("table tbody.rf-cst tr:eq(3) td:eq(4)");
    private JQueryLocator subtotalsOfSeattle = jq("table tbody.rf-cst tr:eq(8) td:eq(4)");
    private JQueryLocator totalOfSubtotals = jq("table tfoot tr td:eq(4)");

    /*
     * Constants
     * *********************************************************************************************************
     * *************
     */
    private final String FIRST_VALUE_OF_TRANSPORT = "$45.00";
    private final String SECOND_VALUE_OF_TRANSPORT = "$45.00";
    private final String SUBTOTALS_OF_TRANSPORT_FOR_SAN_JOSE = "$90.00";
    private final String SUBTOTALS_OF_SAN_JOSE = "$379.02";
    private final String SUBTOTALS_OF_SEATTLE = "$421.25";
    private final String TOTAL_OF_SUBTOTALS = "$800.27";

    @Test
    public void testTotalOfSanJoseTransports() {

        String firstValueOfTransportExpenses = selenium.getText(firstValueOfTransport);
        assertEquals(firstValueOfTransportExpenses, FIRST_VALUE_OF_TRANSPORT,
            "The first value of transport should be different");

        String secondValueOfTransportExpenses = selenium.getText(secondValueOfTransport);
        assertEquals(secondValueOfTransportExpenses, SECOND_VALUE_OF_TRANSPORT,
            "The second value of transport should be different");

        String subTotalOfTransports = selenium.getText(subTotalOfTransportsForSanJose);
        assertEquals(subTotalOfTransports, SUBTOTALS_OF_TRANSPORT_FOR_SAN_JOSE,
            "The subtotal od transports should be different");

        // control that subtotal was computed correctly
        double actualSubtotal = removeDollarAndMakeInt(firstValueOfTransportExpenses)
            + removeDollarAndMakeInt(secondValueOfTransportExpenses);

        assertEquals(actualSubtotal, removeDollarAndMakeInt(subTotalOfTransports),
            "Two values are added incorrectly, they are not equal to actual" + "counting");

        assertEquals(actualSubtotal, removeDollarAndMakeInt(SUBTOTALS_OF_TRANSPORT_FOR_SAN_JOSE),
            "Two values are added incorrectly," + "they are not equal to expected counting");
    }

    @Test
    public void testTotalOfSubtotals() {

        String subtotalOfSanJoseString = selenium.getText(subtotalsOfSanJose);
        assertEquals(subtotalOfSanJoseString, SUBTOTALS_OF_SAN_JOSE, "The subtotal of San Jose is different");

        String subtotalOfSeattleString = selenium.getText(subtotalsOfSeattle);
        assertEquals(subtotalOfSeattleString, SUBTOTALS_OF_SEATTLE, "The subtotal of Seattle is different");

        String totalOfSubtotalsString = selenium.getText(totalOfSubtotals);
        assertEquals(totalOfSubtotalsString, TOTAL_OF_SUBTOTALS, "The total of subtotals is diferent");

        // control that total of subtotals was computed correctly
        double actualTotal = removeDollarAndMakeInt(subtotalOfSanJoseString)
            + removeDollarAndMakeInt(subtotalOfSeattleString);

        assertEquals(actualTotal, removeDollarAndMakeInt(totalOfSubtotalsString), "Two values are added incorrectly, "
            + "they are not equal to the actual counting");

        assertEquals(actualTotal, removeDollarAndMakeInt(TOTAL_OF_SUBTOTALS), "Two values are added incorrectly, "
            + "they are not equal to the expected counting");
    }

    private double removeDollarAndMakeInt(String valueWithDolladAtBegining) {

        String valueWithoutDollarSign = valueWithDolladAtBegining.substring(1);

        return new Double(valueWithoutDollarSign);
    }

}
