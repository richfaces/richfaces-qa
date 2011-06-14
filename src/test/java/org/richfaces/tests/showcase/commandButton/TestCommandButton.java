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
package org.richfaces.tests.showcase.commandButton;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.testng.Assert.assertEquals;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestCommandButton extends AbstractTestA4jCommand {

	/* *****************************************************************************
	 * Locators
	 *******************************************************************************/
	
	protected JQueryLocator commandButton = jq("fieldset form input[type=submit]");

	/* ******************************************************************************
	 * Tests
	 ********************************************************************************/
	
	@Test
	public void testClickOnTheButtonWhileInputIsEmpty() {

		/*
		 * click on the button, the output should be empty string
		 */
		guardXhr(selenium).click(commandButton);

		String expectedOutput = "";
		assertEquals(selenium.getText(outHello).trim(), expectedOutput,
				"The output should be emtpy string");
	}

	@Test
	public void testTypeSomeCharactersAndClickOnTheButton() {

		/*
		 * type a string and click on the button, check the outHello
		 */
		String testString = "Test string";

		selenium.typeKeys(input, testString);

		guardXhr(selenium).click(commandButton);

		String expectedOutput = "Hello " + testString + " !";
		assertEquals(selenium.getText(outHello), expectedOutput,
				"The output should be: " + expectedOutput);
	}

	@Test
	public void testEraseSomeStringAndClickOnTheButton() {

		/*
		 * erases string and check the output is empty string
		 */
		String testString = "Test string";

		selenium.typeKeys(input, testString);
		
		guardXhr(selenium).click(commandButton);
		
		eraseInput(input);

		guardXhr(selenium).click(commandButton);

		String expectedOutput = "";
		waitAjax.until(textEquals.locator(outHello).text(expectedOutput));
	}

}
