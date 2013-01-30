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
package org.richfaces.tests.showcase.popup;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestLogin extends AbstractPoppupPanel {

    /* ********************************************************************************
     * Locators********************************************************************************
     */

    protected JQueryLocator loginAnchorOnTheToolbar = jq("a:contains('Login'):eq(0)");
    protected JQueryLocator loginAnchorOnThePoppup = jq("a:contains('Login'):eq(1)");
    protected JQueryLocator searchAnchorOnTheToolbar = jq("a:contains('Search'):eq(0)");
    protected JQueryLocator searchAnchorOnThePoppup = jq("a:contains('Search'):eq(1)");

    /* **********************************************************************************
     * Tests**********************************************************************************
     */

    @Test
    public void testLoginPoppup() {
        checkPoppupPanel(loginAnchorOnTheToolbar, loginAnchorOnThePoppup);
    }

    @Test
    public void testSearchPoppup() {
        checkPoppupPanel(searchAnchorOnTheToolbar, searchAnchorOnThePoppup);
    }

    /* *****************************************************************************************************
     * Help methods*****************************************************************************************************
     */

    /**
     * Call the poppup panel, and then hides it, check for presence
     *
     * @param callPoppupButton
     *            the button by which the poppup is called
     * @param closingPoppupButton
     *            the button by which the poppup is closed
     */
    private void checkPoppupPanel(JQueryLocator callPoppupButton, JQueryLocator closingPoppupButton) {
        guardNoRequest(selenium).click(callPoppupButton);

        assertTrue(selenium.isElementPresent(poppupPanelContent), "The poppup panel should be visible!");

        guardNoRequest(selenium).click(closingPoppupButton);

        assertFalse(selenium.isVisible(poppupPanelContent), "The poppup panel should not be visible!");
    }
}
