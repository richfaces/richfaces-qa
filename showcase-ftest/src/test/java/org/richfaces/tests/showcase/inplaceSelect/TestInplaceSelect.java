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
package org.richfaces.tests.showcase.inplaceSelect;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestInplaceSelect extends AbstractGrapheneTest {

    /* ********************************************************************************************
     * Locators ****************************************************************** **************************
     */

    protected JQueryLocator selectSimple = jq("input[id$=Input]:eq(0)");
    protected JQueryLocator selectCustomization = jq("input[id$=Input]:eq(1)");

    protected JQueryLocator selectSimpleLabel = jq("span[id$=Label]:eq(0)");
    protected JQueryLocator selectCustomizationLabel = jq("span[id$=Label]:eq(1)");

    protected JQueryLocator optionLocator = jq("span.rf-is-opt:contains('{0}')");

    protected JQueryLocator declineButton = jq("input.rf-is-btn:eq(1)");

    /* *********************************************************************************************
     * Tests ********************************************************************* ************************
     */

    @Test
    public void testSimpleSelect() {
        for (int i = 1; i <= 5; i++) {

            checkSelect(selectSimple, selectSimpleLabel, "Option " + i, false);
        }
    }

    @Test
    public void testCustomizationSelect() {
        checkSelect(selectCustomization, selectCustomizationLabel, "Alabama", true);

        checkSelect(selectCustomization, selectCustomizationLabel, "Florida", true);

        checkSelect(selectCustomization, selectCustomizationLabel, "California", true);
    }

    /* *********************************************************************************************************
     * Help methods **************************************************************
     * *******************************************
     */

    /**
     * Checks the select, when it is select which is activated by double click, then also there is need for click on
     * accept button.
     */

    private void checkSelect(JQueryLocator select, JQueryLocator labelWhenSomethingIsSelected,
        String optionLabelFromPoppup, boolean doubleClick) {

        JQueryLocator acceptButton = jq("input[class*=rf-is-btn]:eq(0)");

        if (doubleClick) {

            selenium.doubleClick(select);
        } else {

            selenium.click(select);
        }

        selenium.click(optionLocator.format(optionLabelFromPoppup));

        if (doubleClick) {

            selenium.mouseDown(acceptButton);
        }

        String optionFromLabel = selenium.getText(labelWhenSomethingIsSelected);

        assertEquals(optionFromLabel, optionFromLabel, "The selected option is different as the select shows!");
    }

}
