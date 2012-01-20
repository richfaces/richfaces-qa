/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.jsFunction;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestJsFunction extends AbstractAjocadoTest {

    /* *****************************************************************************
     * Locators*****************************************************************************
     */

    protected JQueryLocator firstTd = jq("fieldset table tr:first td:first span");
    protected JQueryLocator secondTd = jq("fieldset table tr:first td:odd span");
    protected JQueryLocator lastTd = jq("fieldset table tr:first td:last span");
    protected JQueryLocator showName = jq("span#showname");

    /* ******************************************************************************
     * Tests******************************************************************************
     */

    @Test
    public void testInitialState() {

        String textInShowName = selenium.getText(showName).trim();
        assertEquals("", textInShowName, "The text in shownName should be empty");
    }

    @Test
    public void testMouseOverSpecificTdElement() {
        /*
         * Move mouse over all td elements and check whether the showName is same as text of particular td element then
         * move mouse out of td element and check whether the showName is empty
         */
        // first td
        String textInFirstTd = selenium.getText(firstTd).trim();

        guardXhr(selenium).mouseOver(firstTd);

        String textInShowName = selenium.getText(showName).trim();

        assertEquals(textInShowName, textInFirstTd, "The text in shownName should be same as in the first td!");

        guardXhr(selenium).mouseOut(firstTd);

        textInShowName = selenium.getText(showName).trim();

        assertEquals(textInShowName, "", "The text in shownName should be empty");

        // second td
        String textInSecondTd = selenium.getText(secondTd).trim();

        guardXhr(selenium).mouseOver(secondTd);

        textInShowName = selenium.getText(showName).trim();

        assertEquals(textInShowName, textInSecondTd, "The text in shownName should be same as in the second td!");

        guardXhr(selenium).mouseOut(secondTd);

        textInShowName = selenium.getText(showName).trim();

        assertEquals(textInShowName, "", "The text in shownName should be empty");

        // last td
        String textLastTd = selenium.getText(lastTd).trim();

        guardXhr(selenium).mouseOver(lastTd);

        textInShowName = selenium.getText(showName).trim();

        assertEquals(textInShowName, textLastTd, "The text in shownName should be same as in the last td!");

        guardXhr(selenium).mouseOut(lastTd);

        textInShowName = selenium.getText(showName).trim();

        assertEquals(textInShowName, "", "The text in shownName should be empty");

    }
}
