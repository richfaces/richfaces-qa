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
public class TestReferencedusage extends AbstractShowcaseTest {
	
	/* *******************************************************************************************************
	 * Locators
	 * ******************************************************************
	 * *************************************
	 */
	
	protected JQueryLocator userNameInput = jq("input[type=text]:first");
	protected JQueryLocator addressInput = jq("input[type=text]:last");
	protected JQueryLocator firstAjaxRequestProgressImage = jq("span[class=rf-st-start] img:first");
	protected JQueryLocator secondAjaxRequestProgressImage = jq("span[class=rf-st-start] img:last");
	
	/* ********************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***********************************
	 */
	
	@Test
	public void testUserNameAndAjaxRequestProgressImage() {
		
        writeSomethingToTheInputAndCheckTheImageOfAjaxProgress(userNameInput, firstAjaxRequestProgressImage);
	}
	
	@Test 
	public void testAddressAndAjaxRequestProgressImage() {
		
		writeSomethingToTheInputAndCheckTheImageOfAjaxProgress(addressInput, secondAjaxRequestProgressImage);
	}
	
	/* ********************************************************************************************************
	 * Help methods
	 * *********************************************************************
	 * ***********************************
	 */
	
	/**
	 * Writes something to the input and checks whether there is picture of Ajax request progress
	 * 
	 * @param whichInput input where something will be written
	 * @param imageOfAjaxRequestProgress image of ajax request progress which should appear when ajax request is being handled
	 */
	private void writeSomethingToTheInputAndCheckTheImageOfAjaxProgress(JQueryLocator whichInput, 
			JQueryLocator imageOfAjaxRequestProgress) {
		
		XHRHalter.enable();
		
		selenium.type(whichInput, "a");
		selenium.fireEvent(whichInput, Event.KEYUP);
		
		XHRHalter handle = XHRHalter.getHandleBlocking();
		handle.send();
        
        assertTrue(selenium.isVisible(imageOfAjaxRequestProgress), "There should be an image of ajax request progress!");
        
        handle.complete();

        waitGui.failWith("There can not be image of ajax request, since it is completed!").until(elementNotVisible.locator(imageOfAjaxRequestProgress));
	}

}
