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
package org.richfaces.tests.showcase.editor;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestToolbarAndSkinning extends AbstractAjocadoTest {
	
	/* ***************************************************************************************
	 * Locators
	 *****************************************************************************************/

	protected JQueryLocator buttonOfEditor = jq(".cke_button"); 
	
	protected JQueryLocator basicEditorCheckbox = jq("input[id*='toolbarSelection:0']");
	protected JQueryLocator fullEditorCheckbox = jq("input[id*='toolbarSelection:1']");
	protected JQueryLocator customEditorCheckbox = jq("input[id*='toolbarSelection:2']");
	
	
	/* **********************************************************************************************************************
	 * Constants
	 * *****************************************************************
	 * *****************************************************
	 */
	
	protected final int BASIC_NUMBERS = 28; //there are four skins therefore 4 * 7 buttons
	protected final int FULL_NUMBERS = 83; // the same
	protected final int CUSTOM_NUMBERS = 56; // the same

	/* ***********************************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***************************************************
	 */
	
	@Test
	public void testNumberOfButtonsBasicEditor() {
		
		selenium.check(basicEditorCheckbox);
		guardXhr(selenium).fireEvent(basicEditorCheckbox, Event.CLICK);
		
		int numberOfButtonsActual = selenium.getCount(buttonOfEditor);
		
		assertEquals(numberOfButtonsActual, BASIC_NUMBERS, "The number of buttons in basic mode is incorrect!");
	}
	
	@Test
	public void testNumberOfButtonsFullEditor() {
		
		selenium.check(fullEditorCheckbox);
		guardXhr(selenium).fireEvent(fullEditorCheckbox, Event.CLICK);
		
		int numberOfButtonsActual = selenium.getCount(buttonOfEditor);
		
		assertEquals(numberOfButtonsActual, FULL_NUMBERS, "The number of buttons in full mode is incorrect!");
	}
	
	@Test 
	public void testNumberOfButtonsCustomEditor() {
		
		selenium.check(customEditorCheckbox);
		guardXhr(selenium).fireEvent(customEditorCheckbox, Event.CLICK);
		
		int numberOfButtonsActual = selenium.getCount(buttonOfEditor);
		
		assertEquals(numberOfButtonsActual, CUSTOM_NUMBERS, "The number of buttons in custom mode is incorrect!");
	}

}
