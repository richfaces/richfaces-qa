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
package org.richfaces.tests.showcase.param;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestClientParam extends AbstractGrapheneTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator buttonShowScreenSize = jq("input[type=submit]");
    protected JQueryLocator widthValueLocator = jq("fieldset table:first tr:first td:last");
    protected JQueryLocator heightValueLocator = jq("fieldset table:first tr:last td:last");

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testShowScreenSizeAtInitialState() {

        String actualString = selenium.getText(widthValueLocator).trim();
        assertEquals(actualString, "", "The value of width should be empty string!");

        actualString = selenium.getText(heightValueLocator).trim();
        assertEquals(actualString, "", "The value of height should be empty string!");
    }

    @Test
    public void testShowScreenSizeAfterClickingOnButton() {

        guardXhr(selenium).click(buttonShowScreenSize);

        String widthActual = selenium.getText(widthValueLocator).trim();
        String heightActual = selenium.getText(heightValueLocator).trim();

        String widthExpected = selenium.getEval(new JavaScript("window.screen.width"));
        String heightExpected = selenium.getEval(new JavaScript("window.screen.height"));

        assertEquals(widthActual, widthExpected, "The width returned from website can not be "
            + "different from width returned from this code");
        assertEquals(heightActual, heightExpected, "The height returned from website can not be "
            + "different from height returned from this code");
    }

}
