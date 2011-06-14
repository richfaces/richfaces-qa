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
package org.richfaces.tests.showcase.status;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.Ajocado.elementNotVisible;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.tests.showcase.AbstractShowcaseTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimple extends AbstractShowcaseTest {

	/* *******************************************************************************************************
	 * Locators
	 * ******************************************************************
	 * *************************************
	 */
	
	protected JQueryLocator userNameInput = jq("input[type=text]:first");
	protected JQueryLocator address = jq("input[type=text]:last");
	protected JQueryLocator submitButton = jq("input[type=button]");
	protected JQueryLocator userStoredSuccessfully = jq("span[id$=out]");
	protected JQueryLocator imageOfAjaxRequestProgress = jq("span[class=rf-st-start] img");
	
	/* ********************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***********************************
	 */
	
	@Test
	public void testUserNameAndAjaxRequestProgress() {
		
		XHRHalter.enable();
		
		selenium.type(userNameInput, "a");
		selenium.fireEvent(userNameInput, Event.KEYUP);
		
        handleSendAssertPictureIsVisibleHandleComplete();
	}
	
	@Test
	public void testAddressAndAjaxRequestProgress() {
		
		XHRHalter.enable();
		
		selenium.type(address, "a");
		selenium.fireEvent(address, Event.KEYUP);
		
		handleSendAssertPictureIsVisibleHandleComplete();
	}
	
	@Test
	public void testSubmitButtonAndAjaxRequestProgress() {
		
		selenium.typeKeys(userNameInput, "a");
		selenium.fireEvent(userNameInput, Event.KEYUP);
		
		XHRHalter.enable();
		
		selenium.click(submitButton);
		selenium.fireEvent(submitButton, Event.SUBMIT);
		
		handleSendAssertPictureIsVisibleHandleComplete();
        
        assertEquals(selenium.getText(userStoredSuccessfully).trim(), "User a stored succesfully", "There should appear " +
        		"notification that user stored successfully!");
	}
	
	/* ********************************************************************************************************
	 * Help methods
	 * *********************************************************************
	 * ***********************************
	 */
	
	/**
	 * Catches the ajax request, send it, checks whether there is a picture of Ajax request progress visible,
	 * completes the request and checks whether the picture disappeared
	 */
	private void handleSendAssertPictureIsVisibleHandleComplete() {
		
		XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();
        
        assertTrue(selenium.isVisible(imageOfAjaxRequestProgress), "There should be an image of ajax request progress!");
        
        handle.complete();

        waitGui.failWith("There can not be image of ajax request, since it is completed!").until(elementNotVisible.locator(imageOfAjaxRequestProgress));
	}
}
