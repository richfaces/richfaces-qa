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
package org.richfaces.tests.showcase.log;


import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestLog extends AbstractAjocadoTest {
	
	/* *******************************************************************************************************
	 * Locators
	 * ******************************************************************
	 * *************************************
	 */
	
	protected JQueryLocator input = jq("input[type=text]");
	protected JQueryLocator submitButton = jq("input[type=submit]");
	protected JQueryLocator outputText = jq("span[id$=out]");
	protected JQueryLocator clearButton = jq("button");
	protected JQueryLocator selectMenu = jq("select");
	protected JQueryLocator rfLogContents = jq("div[class=rf-log-contents]");
	
	/* *****************************************************************************************************
	 * Constants
	 *******************************************************************************************************/

	private final int INDEX_OF_DEBUG = 0;
	private final int INDEX_OF_INFO = 1;
	
	/* ********************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***********************************
	 */
	
	@Test
	public void testInitialStateNothingToInputAndCheckTheOutput() {
		
		guardXhr(selenium).click(submitButton);
		
		assertEquals(selenium.getText(outputText).trim(), "", "The ouput string should be empty!");
	}
	
	@Test
	public void testTypeSomethingToInputAndCheckTheOutput() {
		
		String testString = "Test String";
		
		selenium.type(input, testString);
		
		guardXhr(selenium).click(submitButton);
		
		assertEquals(selenium.getText(outputText).trim(), "Hello " + testString + "!", "The ouput string is incorrect!");
	}
	
	@Test
	public void testTypeSomethingToTheInputSelectDebugAndCheckTheLog() {
		
		selenium.type(input, "Test String");
		selenium.select(selectMenu, OptionLocatorFactory.optionIndex(INDEX_OF_DEBUG));
		guardXhr(selenium).click(submitButton);
		
		assertTrue(selenium.getText(rfLogContents).contains("debug"), "The log should contain debug informations.");
		assertTrue(selenium.getText(rfLogContents).contains("info"), "The log should contain info informations.");		
	}
	
	@Test 
	public void testTypeSomethingToTheInputSelectInfoAndCheckTheLog() {
		
		selenium.type(input, "Test string");
		selenium.select(selectMenu, OptionLocatorFactory.optionIndex(INDEX_OF_INFO));
		guardXhr(selenium).click(submitButton);
		
		assertFalse(selenium.getText(rfLogContents).contains("debug"), "The log should not contain debug informations");
		assertTrue(selenium.getText(rfLogContents).contains("info"), "The log should contain info informations");
		
	}
	
	@Test
	public void testClearButton() {
		
		selenium.type(input, "test String");
		selenium.select(selectMenu, OptionLocatorFactory.optionIndex(INDEX_OF_INFO));
		guardXhr(selenium).click(submitButton);
		
		assertTrue(selenium.getText(rfLogContents).contains("info"), "The log should contain info information");
		
		selenium.click(clearButton);
		assertEquals(selenium.getText(rfLogContents).trim(), "", "The log info should be empty!");
	}
	
}
