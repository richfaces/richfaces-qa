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
package org.richfaces.tests.showcase.status;

import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.Ajocado.elementNotVisible;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.dom.Event.KEYUP;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestViewUsage extends AbstractAjocadoTest {

    /* *******************************************************************************************************
     * Locators ****************************************************************** *************************************
     */

    protected JQueryLocator userName = jq("input[type=text]:first");
    protected JQueryLocator address = jq("input[type=text]:odd");
    protected JQueryLocator submitForUserDetails = jq("input[type=button]:first");
    protected JQueryLocator submitForSearchPanel = jq("input[type=button]:last");
    protected JQueryLocator imageOfAjaxRequestProgress = jq("span[class=rf-st-start] img");

    /* ********************************************************************************************************
     * Tests ********************************************************************* ***********************************
     */

    @Test
    public void testUserNameAndImagePresentionOfAjaxProgress() {

        XHRHalter.enable();

        selenium.type(userName, "a");
        selenium.fireEvent(userName, KEYUP);

        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();

        assertTrue(selenium.isVisible(imageOfAjaxRequestProgress), "There should be an image of ajax request progress!");

        handle.complete();

        waitGui.failWith("There can not be image of ajax request, since it is completed!").until(
            elementNotVisible.locator(imageOfAjaxRequestProgress));
    }

    @Test
    public void testAddressAndImagePresentionOfAjaxProgress() {

        XHRHalter.enable();

        selenium.type(address, "a");
        selenium.fireEvent(address, KEYUP);

        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();

        assertTrue(selenium.isVisible(imageOfAjaxRequestProgress), "There should be an image of ajax request progress!");

        handle.complete();

        waitGui.failWith("There can not be image of ajax request, since it is completed!").until(
            elementNotVisible.locator(imageOfAjaxRequestProgress));
    }

    @Test
    public void testSumbitForUserDetailsAndImagePresentionOfAjaxProgress() {

        XHRHalter.enable();

        selenium.click(submitForUserDetails);
        selenium.fireEvent(submitForUserDetails, Event.SUBMIT);

        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();

        assertTrue(selenium.isVisible(imageOfAjaxRequestProgress), "There should be an image of ajax request progress!");

        handle.complete();

        waitGui.failWith("There can not be image of ajax request, since it is completed!").until(
            elementNotVisible.locator(imageOfAjaxRequestProgress));
    }

    @Test
    public void testSubmitForSearchPanelAndImagePresentionOfAjaxProgress() {

        XHRHalter.enable();

        selenium.click(submitForSearchPanel);
        selenium.fireEvent(submitForSearchPanel, Event.SUBMIT);

        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();

        assertTrue(selenium.isVisible(imageOfAjaxRequestProgress), "There should be an image of ajax request progress!");

        handle.complete();

        waitGui.failWith("There can not be image of ajax request, since it is completed!").until(
            elementNotVisible.locator(imageOfAjaxRequestProgress));
    }

}
