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

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.testng.annotations.Test;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestCalendar extends AbstractCalendarTest {

    /* ************************************************************************************
     * Locators ****************************************************************** ******************
     */

    protected JQueryLocator disabledCheckBox = jq("input[type=checkbox]:eq(0)");
    protected JQueryLocator poppupModeCheckBox = jq("input[type=checkbox]:eq(1)");
    protected JQueryLocator applyButtonCheckBox = jq("input[type=checkbox]:eq(2)");
    protected JQueryLocator dataPetternSelect = jq("select");
    protected JQueryLocator USLocale = jq("input[type=radio]:eq(0)");
    protected JQueryLocator DELocale = jq("input[type=radio]:eq(1)");
    protected JQueryLocator FRLocale = jq("input[type=radio]:eq(2)");
    protected JQueryLocator RULocale = jq("input[type=radio]:eq(3)");

    /* **************************************************************************************
     * Tests ********************************************************************* *****************
     */

    @Test
    public void testThereAreAllRequiredComponents() {

        abstractTestThereAreAllRequiredComponents();

        checkTheTimeSetterAndCleanButtonPresent();
    }

    @Test
    public void testDisabledCheckBox() {

        selenium.check(disabledCheckBox);
        guardXhr(selenium).fireEvent(disabledCheckBox, Event.CLICK);

        guardNoRequest(selenium).click(imgWhichCallsTheCalendar);

        checkWhetherTheseComponentsArePresent(addAllComponentsToTheSet(), false);
    }
}
