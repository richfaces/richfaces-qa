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
package org.richfaces.tests.showcase.editor;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractGrapheneTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 */
public class TestAdvancedConfiguration extends AbstractGrapheneTest {

    /* ******************************************************************************
     * Constants ***************************************************************** ****************
     */

    protected final String NEW_PAGE_ENG = "New Page";
    protected final String NEW_PAGE_FR = "Nouvelle page";
    protected final String NEW_PAGE_DE = "Neue Seite";

    /* ***************************************************************************************************
     * Locators ****************************************************************** *********************************
     */

    protected JQueryLocator englishCheckbox = jq("input[type=radio]:eq(0)");
    protected JQueryLocator frenchCheckbox = jq("input[type=radio]:eq(1)");
    protected JQueryLocator germanCheckbox = jq("input[type=radio]:eq(2)");

    protected JQueryLocator newPageButton = jq(".cke_button_newpage");

    /* **************************************************************************
     * Tests ********************************************************************* *****
     */

    @Test
    public void testEnglishLanguage() {

        selenium.check(englishCheckbox);
        guardXhr(selenium).fireEvent(englishCheckbox, Event.CLICK);

        String titleOfNewPageButton = selenium.getAttribute(newPageButton.getAttribute(Attribute.TITLE));

        assertEquals(titleOfNewPageButton, NEW_PAGE_ENG, "The language was not changed to english!");
    }

    @Test
    public void testFrenchLanguage() {

        selenium.check(frenchCheckbox);
        guardXhr(selenium).fireEvent(frenchCheckbox, Event.CLICK);

        String titleOfNewPageButton = selenium.getAttribute(newPageButton.getAttribute(Attribute.TITLE));

        assertEquals(titleOfNewPageButton, NEW_PAGE_FR, "The language was not changed to french!");
    }

    @Test
    public void testGermanLanguage() {

        selenium.check(germanCheckbox);
        guardXhr(selenium).fireEvent(germanCheckbox, Event.CLICK);

        String titleOfNewPageButton = selenium.getAttribute(newPageButton.getAttribute(Attribute.TITLE));

        assertEquals(titleOfNewPageButton, NEW_PAGE_DE, "The language was not changed to german!");
    }

    @Test
    public void testUserFocusAutomaticallyOnEditor() {

        fail("implement me correctly!");
    }

}
