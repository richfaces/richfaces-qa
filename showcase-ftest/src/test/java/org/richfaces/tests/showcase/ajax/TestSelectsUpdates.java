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
package org.richfaces.tests.showcase.ajax;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionLabel;

import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSelectsUpdates extends AbstractGrapheneTest {

    /* *************************************************************************************
     * Locators*************************************************************************************
     */

    protected JQueryLocator firstSelect = jq("fieldset form select:eq(0)");
    protected JQueryLocator secondSelect = jq("fieldset form select:eq(1)");

    /* ***************************************************************************************
     * Tests***************************************************************************************
     */

    @Test
    public void testDynamicSelects() {

        List<String> fruitsExpected = new ArrayList<String>();
        fruitsExpected.add("");
        fruitsExpected.add("Banana");
        fruitsExpected.add("Cranberry");
        fruitsExpected.add("Blueberry");
        fruitsExpected.add("Orange");

        List<String> vegetablesExpected = new ArrayList<String>();
        vegetablesExpected.add("");
        vegetablesExpected.add("Potatoes");
        vegetablesExpected.add("Broccoli");
        vegetablesExpected.add("Garlic");
        vegetablesExpected.add("Carrot");

        boolean isFirstSelectDispalyed = selenium.isElementPresent(firstSelect);
        assertTrue(isFirstSelectDispalyed, "First select should be displayed!");

        boolean isSecondSelectDisplayed = selenium.isElementPresent(secondSelect);
        assertFalse(isSecondSelectDisplayed, "Second select should be dispayed");

        guardXhr(selenium).select(firstSelect, optionLabel("Fruits"));

        List<String> fruitsActual = selenium.getSelectOptions(secondSelect);

        isSecondSelectDisplayed = selenium.isElementPresent(secondSelect);
        assertTrue(isSecondSelectDisplayed, "Second select should be dispayed");

        assertEquals(fruitsActual, fruitsExpected, "When selected fruits in first select, in the second "
            + "should be some examples of Fruits");

        guardXhr(selenium).select(firstSelect, optionLabel("Vegetables"));

        List<String> vegetablesActual = selenium.getSelectOptions(secondSelect);

        assertEquals(vegetablesActual, vegetablesExpected, "When selected vegetables in first select, in the second "
            + "should be some examples of vegetables");

    }
}
