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

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.guard.RequestGuardException;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionLabelLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTableFiltering extends AbstractDataIterationWithCars {

    private JQueryLocator vendorSelect = jq("select");
    private JQueryLocator mileageInput = jq("input[type=text]:first");
    private JQueryLocator vinInput = jq("input[type=text]:last");
    private JQueryLocator errorMessageLocator = jq("span[class=rf-msgs-sum]");
    private JQueryLocator tBody = jq("tbody[class=rf-dt-b]");

    /*
     * Constants *****************************************************************
     * ***********************************************
     */

    private final String MILEAGE_FILTER = "40000";
    private final String VENDOR_FILTER = "Chevrolet";
    private final String VIN_FILTER = "A";
    private final String ERROR_MESSAGE = "is not a number";

    /*
     * Tests
     *
     * tests conditions are depending on some non deterministic conditions, when are not satisfied the fail() is called
     * ************************************ **************************************
     * *****************************************
     */

    @Test
    public void testNoFilters() {

        // this solution I choose to achieve that it does not matter in which
        // order are tests running
        // because this state can be inherited from other tests and therefore
        // there is no request
        try {
            guardXhr(selenium).select(vendorSelect, new OptionLabelLocator(""));
        } catch (RequestGuardException ex) {

            guardNoRequest(selenium).select(vendorSelect, new OptionLabelLocator(""));
        }

        assertFalse(testWhetherTableContainsNonEmptyStrings(tBody), "Table should not contains empty strings");
    }

    @Test
    public void testVendorFilter() {

        // this solution I choose to achieve that it does not matter in which
        // order are tests running
        // because this state can be inherited from other tests and therefore
        // there is no request
        try {
            guardXhr(selenium).select(vendorSelect, new OptionLabelLocator(VENDOR_FILTER));
        } catch (RequestGuardException ex) {

            guardNoRequest(selenium).select(vendorSelect, new OptionLabelLocator(VENDOR_FILTER));
        }

        assertTrue(checkWhetherAllRowsHaveCorrectValue("vendor", VENDOR_FILTER),
            "There should be only rows which contains only " + VENDOR_FILTER);
    }

    @Test
    public void testMileageFilter() {

        // this solution I choose to achieve that it does not matter in which
        // order are tests running
        // because this state can be inherited from other tests and therefore
        // there is no request
        try {
            guardXhr(selenium).select(vendorSelect, new OptionLabelLocator(""));
        } catch (RequestGuardException ex) {

            guardNoRequest(selenium).select(vendorSelect, new OptionLabelLocator(""));
        }

        selenium.type(mileageInput, MILEAGE_FILTER);

        guardXhr(selenium).fireEvent(mileageInput, Event.BLUR);

        assertTrue(checkWhetherAllRowsHaveCorrectValue("mileage", MILEAGE_FILTER),
            "There should be only rows with mileage less or equal to " + MILEAGE_FILTER);

        // this is to not affect other tests
        eraseInput(mileageInput);
        guardXhr(selenium).fireEvent(mileageInput, Event.BLUR);
    }

    @Test
    public void testVinFilter() {

        // this solution I choose to achieve that it does not matter in which
        // order are tests running
        // because this state can be inherited from other tests and therefore
        // there is no request
        try {
            guardXhr(selenium).select(vendorSelect, new OptionLabelLocator(""));
        } catch (RequestGuardException ex) {

            guardNoRequest(selenium).select(vendorSelect, new OptionLabelLocator(""));
        }

        selenium.type(vinInput, VIN_FILTER);
        guardXhr(selenium).fireEvent(vinInput, Event.BLUR);

        assertTrue(checkWhetherAllRowsHaveCorrectValue("vin", VIN_FILTER), "all vins of the car should contain "
            + VIN_FILTER);

        // this is to not affect other tests
        eraseInput(vinInput);
        guardXhr(selenium).fireEvent(vinInput, Event.BLUR);
    }

    @Test
    public void testErrorMessage() {

        selenium.type(mileageInput, "foo");
        guardXhr(selenium).fireEvent(mileageInput, Event.BLUR);

        String actualErrorMessage = selenium.getText(errorMessageLocator);
        assertTrue(actualErrorMessage.contains(ERROR_MESSAGE),
            "There should be error message, since there is string in the mileage input!");

        eraseInput(mileageInput);
        guardXhr(selenium).fireEvent(vinInput, Event.BLUR);
    }

    @Test
    public void testMileageVinFilter() {

        // this solution I choose to achieve that it does not matter in which
        // order are tests running
        // because this state can be inherited from other tests and therefore
        // there is no request
        try {
            guardXhr(selenium).select(vendorSelect, new OptionLabelLocator(""));
        } catch (RequestGuardException ex) {

            guardNoRequest(selenium).select(vendorSelect, new OptionLabelLocator(""));
        }

        selenium.type(mileageInput, MILEAGE_FILTER);
        guardXhr(selenium).fireEvent(mileageInput, Event.BLUR);

        selenium.type(vinInput, VIN_FILTER);
        guardXhr(selenium).fireEvent(vinInput, Event.BLUR);

        assertTrue(checkWhetherAllRowsHaveCorrectMileageAndVin(MILEAGE_FILTER, VIN_FILTER),
            "There should be only rows with mileage" + " less or equal to " + MILEAGE_FILTER
                + " and vin which contains " + VIN_FILTER);

        eraseAllInputs();
    }

    @Test
    public void testNoRowsSatisfyConditions() {

        selenium.type(mileageInput, "1111111111111111111111");
        guardXhr(selenium).fireEvent(mileageInput, Event.BLUR);

        selenium.type(vinInput, "I hope this will not be there, but for sure I type also my name, jhuska");
        guardXhr(selenium).fireEvent(vinInput, Event.BLUR);

        boolean nothingFoundPresent = selenium
            .isElementPresent(tBody.getChild(jq("tr > td:contains('Nothing found')")));

        assertTrue(nothingFoundPresent, "No rows should satisfy the filter conditions");

        eraseAllInputs();
    }

    /* ********************************************************************************************************************
     * help methods **************************************************************
     * *****************************************************
     */
    /**
     * Check that all rows in the table have a price, which is correct according to the filter. So whether the price is
     * lower or equal than value in the filterValue parameter.
     *
     * @param filterValue
     *            which should be bigger or equal to all price values in the table
     * @return true if all rows have correct value, false otherwise
     */
    private boolean checkWhetherAllRowsHaveCorrectValue(String whatAreWeChecking, String filterValue) {

        JQueryLocator trs = jq(tBody.getRawLocator() + " > tr");

        boolean nothingFoundPresent = selenium
            .isElementPresent(tBody.getChild(jq("tr > td:contains('Nothing found')")));

        if (nothingFoundPresent) {

            fail("Test condition was not correct for this run of test, change it, or try rerun the test!");
        }

        for (Iterator<JQueryLocator> i = trs.iterator(); i.hasNext();) {

            boolean result = checkValueInTheRow(i.next(), filterValue, whatAreWeChecking);

            if (!result) {

                return false;
            }
        }

        return true;
    }

    /**
     * checks whether all rows have correct mileage and vin
     *
     * @param mileagefilter
     * @param vinFilter
     * @return true when all rows have correct mileage and von, false otherwise
     */
    private boolean checkWhetherAllRowsHaveCorrectMileageAndVin(String mileagefilter, String vinFilter) {

        JQueryLocator trs = jq(tBody.getRawLocator() + " > tr");

        boolean nothingFoundPresent = selenium
            .isElementPresent(tBody.getChild(jq("tr > td:contains('Nothing found')")));

        if (nothingFoundPresent) {

            fail("Test condition was not correct for this run of test, change it, or try rerun the test!");
        }

        for (Iterator<JQueryLocator> i = trs.iterator(); i.hasNext();) {

            JQueryLocator row = i.next();

            boolean result = checkValueInTheRow(row, mileagefilter, "mileage");

            if (!result) {

                return false;
            }

            result = checkValueInTheRow(row, vinFilter, "vin");

            if (!result) {

                return false;
            }
        }

        return true;
    }

    /**
     * Check that the row has correct price, so whether the price from row is less or equal to filterValue
     *
     * @param row
     * @param filterValue
     * @return true if the value in the row is correct, false otherwise
     */
    private boolean checkValueInTheRow(JQueryLocator row, String filterValue, String whatAreWeChecking) {

        JQueryLocator tds = jq(row.getRawLocator() + " > td");

        int i = 0;
        for (Iterator<JQueryLocator> iterator = tds.iterator(); iterator.hasNext();) {

            JQueryLocator currentTd = iterator.next();

            String valueFromTd = selenium.getText(currentTd);

            // this is what are we checking for nofilter option
            if (whatAreWeChecking.equals("nofilter")) {

                return !valueFromTd.trim().equals("");
            }

            // this is what are we checking for vendor option
            if ((i == 0) && (whatAreWeChecking.equals("vendor"))) {

                return filterValue.equals(valueFromTd);
            }

            // this is what are we checking for price option
            if ((i == 3) && (whatAreWeChecking.equals("mileage"))) {

                Double filterValueInt = new Double(filterValue);
                Double valueFromTdInt = new Double(valueFromTd);

                return (valueFromTdInt.doubleValue() <= filterValueInt.doubleValue());
            }

            // this is what are we checking for vin option
            if ((i == 4) && (whatAreWeChecking.equals("vin"))) {
                if (!valueFromTd.contains(VIN_FILTER)) {
                    System.out.println(valueFromTd);
                }
                return valueFromTd.contains(filterValue);
            }

            i++;
        }

        return false;
    }

    /**
     * erases all inputs
     */
    private void eraseAllInputs() {

        eraseInput(mileageInput);
        guardXhr(selenium).fireEvent(mileageInput, Event.BLUR);

        eraseInput(vinInput);
        guardXhr(selenium).fireEvent(vinInput, Event.BLUR);
    }
}
