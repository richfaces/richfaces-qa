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
package org.richfaces.tests.showcase.calendar;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.Graphene.elementPresent;

import java.util.HashSet;
import java.util.Set;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractCalendarTest extends AbstractAjocadoTest {

    /* ******************************************************************
     * Locators******************************************************************
     */

    protected JQueryLocator prevYear = jq("div[onclick*=prevYear]:visible");
    protected JQueryLocator prevMonth = jq("div[onclick*=prevMonth]:visible");
    protected JQueryLocator nextMonth = jq("div[onclick*=nextMonth]:visible");
    protected JQueryLocator nextYear = jq("div[onclick*=nextYear]:visible");

    protected JQueryLocator currentMonthAndYear = jq("div[onclick*=showDateEditor]:visible");
    protected JQueryLocator todayButton = jq("div[onclick*=today]:visible");

    protected JQueryLocator day15 = jq("td[onclick*=eventCellOnClick]:contains('15'):visible");
    protected JQueryLocator cleanButton = jq("div.rf-cal-tl-btn:contains('Clean'):visible");
    protected JQueryLocator timeSetter = jq("div[onclick*=showTimeEditor]:visible");
    protected JQueryLocator closeButton = jq("div[onclick*=close]");

    protected JQueryLocator imgWhichCallsTheCalendar = jq("img.rf-cal-btn");

    protected JQueryLocator holiday = jq("td[class*=holiday]:visible");

    /* *********************************************************************************
     * Methods*********************************************************************************
     */

    /**
     * Checks whether that all components in the given set are visible
     *
     * @param setOfComponents
     *            the set of components
     *
     * @param shouldBeVisible
     *            whether the components in the set should be visible or not
     */
    protected void checkWhetherTheseComponentsArePresent(Set<JQueryLocator> setOfComponents, boolean shouldBeVisible) {

        for (JQueryLocator i : setOfComponents) {

            if (!selenium.isElementPresent(i) && shouldBeVisible) {

                fail("The element with locator " + i.getRawLocator() + " should be visible");
            } else if (selenium.isElementPresent(i) && !shouldBeVisible) {

                fail("The element with locator " + i.getRawLocator() + " should not be visible");
            }
        }
    }

    /**
     * Adds all components to the set all returns it
     *
     * @return the set with all components
     */
    protected Set<JQueryLocator> addAllComponentsToTheSet() {

        Set<JQueryLocator> allComponents = new HashSet<JQueryLocator>();

        allComponents.add(prevYear);
        allComponents.add(prevMonth);
        allComponents.add(nextMonth);
        allComponents.add(nextYear);
        allComponents.add(currentMonthAndYear);
        allComponents.add(todayButton);
        allComponents.add(day15);
        allComponents.add(closeButton);

        return allComponents;
    }

    /**
     * Checks the presence if time setter and clean button, since they will appear after click on the day
     */
    protected void checkTheTimeSetterAndCleanButtonPresent() {

        selenium.click(day15);

        if (!selenium.isElementPresent(cleanButton)) {

            fail("The clean button should be visible since there was a click on the day!");
        } else if (!selenium.isElementPresent(timeSetter)) {

            fail("The time setter should be visible since there was a click on the day!");
        }
    }

    /**
     * Checks whether there are all components when the calendar is visible, and that there are non when it is not
     * visible
     */
    protected void abstractTestThereAreAllRequiredComponents() {

        Set<JQueryLocator> allComponents = addAllComponentsToTheSet();

        checkWhetherTheseComponentsArePresent(allComponents, false);

        selenium.click(imgWhichCallsTheCalendar);

        checkWhetherTheseComponentsArePresent(allComponents, true);
    }

    /**
     * Checks whether when clicking on the disabled component does not close the calendar
     *
     * @param componentType
     *            the type of the disabled component
     */
    protected void abstractTestClickOnDisabledComponents(JQueryLocator componentType) {

        Set<JQueryLocator> setOfComponents = new HashSet<JQueryLocator>();

        int numberOfDisabledComponents = selenium.getCount(componentType);

        for (int i = 0; i < numberOfDisabledComponents; i++) {

            setOfComponents.add(jq(componentType.getRawLocator() + ":eq(" + i + ")"));

        }

        selenium.click(imgWhichCallsTheCalendar);
        waitModel.until(elementPresent.locator(currentMonthAndYear));

        for (JQueryLocator i : setOfComponents) {

            selenium.click(i);

            assertTrue(selenium.isElementPresent(currentMonthAndYear),
                "The calendar should not be closed when clicking on the disabled component");

            selenium.click(jq(componentType.getRawLocator() + ":eq(0)"));
        }

        selenium.click(imgWhichCallsTheCalendar);
    }

}
