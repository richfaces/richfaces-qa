/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class TestSelectsUpdates extends AbstractWebDriverTest {

    /* *************************************************************************************
     * Locators*************************************************************************************
     */

    @FindByJQuery("fieldset form select:eq(0)")
    protected WebElement firstSelect;
    @FindByJQuery("div[id$='second'] select")
    protected WebElement secondSelect;

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

        assertTrue(firstSelect.isDisplayed(), "First select should be displayed!");

//        assertFalse(secondSelect.isDisplayed(), "Second select should be dispayed");

        Graphene.guardAjax(new Select(firstSelect)).selectByVisibleText("Fruits");

        List<String> fruitsActual = new ArrayList<String>();
        List<WebElement> fruitsOptions = new Select(secondSelect).getOptions();
        for (WebElement option: fruitsOptions) {
            fruitsActual.add(option.getText().trim());
        }

        assertTrue(secondSelect.isDisplayed(), "Second select should be dispayed");

        assertEquals(fruitsActual, fruitsExpected, "When selected fruits in first select, in the second "
            + "should be some examples of Fruits");

        Graphene.guardAjax(new Select(firstSelect)).selectByVisibleText("Vegetables");

        List<String> vegetablesActual = new ArrayList<String>();
        List<WebElement> vegetablesOptions = new Select(secondSelect).getOptions();
        for (WebElement option: vegetablesOptions) {
            vegetablesActual.add(option.getText().trim());
        }

        assertEquals(vegetablesActual, vegetablesExpected, "When selected vegetables in first select, in the second "
            + "should be some examples of vegetables");

    }

}
