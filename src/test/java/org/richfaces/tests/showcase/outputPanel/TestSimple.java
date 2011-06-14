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
package org.richfaces.tests.showcase.outputPanel;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.dom.Event.KEYUP;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
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
	
	protected JQueryLocator firstWrongInput = jq("input[id$=text1]");
	protected JQueryLocator secondRightInput = jq("input[id$=text2]");
	protected JQueryLocator approvedTextWichShouldBeEmpty = jq("");
	protected JQueryLocator approvedText = jq("div[id$=out2]");
	protected JQueryLocator errorValidationLength = jq("div:contains('text2: Validation Error')");
	
	/* ********************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***********************************
	 */
	
	@Test
	public void testFirstWrongInputStringLessThan10() {
		
		String testStringLessThan10 = "foo";
		
		selenium.typeKeys(firstWrongInput, testStringLessThan10);
		
		guardXhr(selenium).fireEvent(firstWrongInput, KEYUP);
		
		assertFalse(selenium.isTextPresent(testStringLessThan10), "The string: " + testStringLessThan10 + " should not be present!");
		
		assertFalse(selenium.isTextPresent("Validation Error: Value"), "The error message about length of string should not appear!");
	}
	
	@Test
	public void testFirstWrongInputStringMoreThan10() {
		
		String testStringMoreThan10 = "This string has definitely length more than 10";
		
		selenium.typeKeys(firstWrongInput, testStringMoreThan10);
		
		guardXhr(selenium).fireEvent(firstWrongInput, KEYUP);
		
		assertFalse(selenium.isTextPresent(testStringMoreThan10), "The string: " + testStringMoreThan10 + " should not be present!");
		
		assertFalse(selenium.isTextPresent("Validation Error: Value"), "The error message about length of string should not appear!");
	}
	
	@Test
	public void testSecondRightInput() {
		
		StringBuilder sb = new StringBuilder();
		
		// try to type 10 characters and check whether the text is still approved
		// and also whether text appears in the approved text part 
		for( int i = 0; i < 10; i++ ) {
			sb.append(i);
			
			selenium.typeKeys(secondRightInput, sb.toString());
			guardXhr(selenium).fireEvent(secondRightInput, KEYUP);
			
			assertEquals(selenium.getText(approvedText), "Approved Text: " + sb.toString(), "The text still" +
					"should appear in the approved text part, since it is no longer than 10 characters!");
			
			assertFalse(selenium.isElementPresent(errorValidationLength), "The error message should not appear, there are " +
					"not more than 10 characters!");
		}
		
		// now there are 10 characters, adding one more should render error message and approved
		// text should be empty
		sb.append("X");
		selenium.typeKeys(secondRightInput, sb.toString());
		guardXhr(selenium).fireEvent(secondRightInput, KEYUP);
		
		assertEquals(selenium.getText(approvedText).trim(), "", "The text from element approved text should be" +
				"empty string, since there are more than 10 characters in the input!");
		
		assertTrue(selenium.isElementPresent(errorValidationLength), "The error message about text length should " +
				"be present and visible because there are more than 10 characters in the input!");
		
	}
}
