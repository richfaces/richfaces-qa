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
package org.richfaces.tests.showcase.repeat;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestRepeat extends AbstractDataIterationWithStates {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    private JQueryLocator firstStateHeader = jq("div.rf-p:first div[id$=header]");
    private JQueryLocator firstStateBody = jq("div.rf-p:first div[id$=body]");
    private JQueryLocator lastStateHeader = jq("div.rf-p:last div[id$=header]");
    private JQueryLocator lastStateBody = jq("div.rf-p:last div[id$=body]");
    private JQueryLocator anchorForSecondPage = jq("a.rf-ds-nmb-btn:first");
    private JQueryLocator anchorForThirdPage = jq("a.rf-ds-nmb-btn:last");

    /* *****************************************************************************************************
     * Constants*****************************************************************************************************
     */

    private final StateWithCapitalAndTimeZone FIRST_STATE_FIRST_PAGE = new StateWithCapitalAndTimeZone("Alabama",
        "Montgomery", "GMT-6");

    private final StateWithCapitalAndTimeZone LAST_STATE_FIRST_PAGE = new StateWithCapitalAndTimeZone("Maryland",
        "Annapolis", "GMT-5");

    private final StateWithCapitalAndTimeZone FIRST_STATE_SECOND_PAGE = new StateWithCapitalAndTimeZone(
        "Massachusetts", "Boston", "GMT-5");

    private final StateWithCapitalAndTimeZone LAST_STATE_SECOND_PAGE = new StateWithCapitalAndTimeZone(
        "South Carolina", "Columbia", "GMT-5");

    private final StateWithCapitalAndTimeZone FIRST_STATE_THIRD_PAGE = new StateWithCapitalAndTimeZone("South Dakota",
        "Pierre", "GMT-6");

    private final StateWithCapitalAndTimeZone LAST_STATE_THIRD_PAGE = new StateWithCapitalAndTimeZone("Wyoming",
        "Cheyenne", "GMT-7");

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testFirstStateFirstPage() {

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(firstStateHeader, firstStateBody);
        assertEquals(actual, FIRST_STATE_FIRST_PAGE, "The first state on first page is not correct");
    }

    @Test
    public void testLastStateFirstPage() {

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(lastStateHeader, lastStateBody);
        assertEquals(actual, LAST_STATE_FIRST_PAGE, "The last state on first page is not correct");
    }

    @Test
    public void testFirstStateSecondPage() {

        guardXhr(selenium).click(anchorForSecondPage);

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(firstStateHeader, firstStateBody);
        assertEquals(actual, FIRST_STATE_SECOND_PAGE, "The first state on second page is not correct");
    }

    @Test
    public void testLastStateSecondPage() {

        guardXhr(selenium).click(anchorForSecondPage);

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(lastStateHeader, lastStateBody);
        assertEquals(actual, LAST_STATE_SECOND_PAGE, "The last state on second page is not correct");
    }

    @Test
    public void testFirstStateThirdPage() {

        guardXhr(selenium).click(anchorForThirdPage);

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(firstStateHeader, firstStateBody);
        assertEquals(actual, FIRST_STATE_THIRD_PAGE, "The first state on third page is not correct");
    }

    @Test
    public void testLastStateThirdPage() {

        guardXhr(selenium).click(anchorForThirdPage);

        StateWithCapitalAndTimeZone actual = retrieveDataAboutState(lastStateHeader, lastStateBody);
        assertEquals(actual, LAST_STATE_THIRD_PAGE, "The last state on third page is not correct");
    }

    /* ********************************************************************************************************
     * Help methods *********************************************************************
     * ***********************************
     */

    /**
     * retrieves data about state from header and body elements of particular grid which is created via rich:repeat
     *
     * @param header
     *            there is name of State
     * @param body
     *            there is capital and timezone with particular format
     * @return
     */
    private StateWithCapitalAndTimeZone retrieveDataAboutState(JQueryLocator header, JQueryLocator body) {

        StateWithCapitalAndTimeZone state = new StateWithCapitalAndTimeZone();

        state.setState(selenium.getText(header));

        String capitalAndTimeZone = selenium.getText(body);

        // since capitalAndTimeZone is in format State Capital (particular capital) State TimeZone (particular time
        // zone)
        // for example State Capital Annapolis State TimeZone GMT-5
        String[] capitalAndTimeZoneSplitted = capitalAndTimeZone.split(" ");

        // capital and timezone will be always at these indices, since the samples I am testing do have capitals
        // and timezones which consist from one word only
        // NOTE it has to be changed when testing of other samples will come, samples which will have capitals or
        // timezones consisted from more than one word
        state.setCapital(capitalAndTimeZoneSplitted[2]);
        state.setTimeZone(capitalAndTimeZoneSplitted[5]);

        return state;
    }

}
