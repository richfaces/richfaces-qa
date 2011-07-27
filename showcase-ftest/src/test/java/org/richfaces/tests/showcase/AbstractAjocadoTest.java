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
package org.richfaces.tests.showcase;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.awt.event.KeyEvent;
import java.util.Iterator;

import org.jboss.arquillian.ajocado.ajaxaware.AjaxAwareInterceptor;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 * @version $Revision$
 */
@RunAsClient
public abstract class AbstractAjocadoTest extends AbstractShowcaseTest {

	@Drone
	protected AjaxSelenium selenium;

	@BeforeMethod
	public void loadPage() {

		// this is beacuse of permission denied failures, this interceptor
		// intercept this type of exception and tries to
		// repeat that command several times
		selenium.getCommandInterceptionProxy().registerInterceptor(
				new AjaxAwareInterceptor());

		// workaround for jboss as 7, since it throws error when is looking up
		// for contextRoot
		/*
		 * try { contextRoot = new URL("http://localhost:8080"); } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		String addition = getAdditionToContextRoot();

		selenium.open(URLUtils.buildUrl(contextRoot, "/showcase/", addition));
	}

	/* ***********************************************************************************************************************
	 * ajocado specific methods *********************************
	 * *****************************************
	 * **********************************************
	 */

	/**
	 * Erases the content of input
	 * 
	 * @param input
	 */
	public void eraseInput(JQueryLocator input) {

		selenium.focus(input);

		selenium.keyDownNative(KeyEvent.VK_CONTROL);
		selenium.keyPressNative(KeyEvent.VK_A);
		selenium.keyUpNative(KeyEvent.VK_CONTROL);

		selenium.keyPressNative(KeyEvent.VK_BACK_SPACE);
	}

	/**
	 * Fills input with string
	 * 
	 * @param input
	 * @param value
	 */
	public void fillAnyInput(JQueryLocator input, String value) {

		selenium.type(input, value);
	}

	/**
	 * Checks whether there is particular message and checks whether the message
	 * is correct
	 * 
	 * @param errorMessageLocator
	 * @param errorMessage
	 * @param shouldErrorMessagePresented
	 */
	public void isThereErrorMessage(JQueryLocator errorMessageLocator,
			String errorMessage, boolean shouldErrorMessagePresented) {

		if (shouldErrorMessagePresented) {
			assertTrue(
					selenium.getText(errorMessageLocator)
							.contains(errorMessage), errorMessage
							+ " /// should be presented!");
		} else {
			assertFalse(selenium.isElementPresent(errorMessageLocator),
					errorMessage + " /// should not be presented!");

		}
	}

	/**
	 * Checks whether there is particular message and checks whether the message
	 * is correct
	 * 
	 * @param infoMessageLocator
	 * @param infoMessage
	 * @param shouldBeInfoMessagePresented
	 */
	public void isThereInfoMessage(JQueryLocator infoMessageLocator,
			String infoMessage, boolean shouldBeInfoMessagePresented) {

		isThereErrorMessage(infoMessageLocator, infoMessage,
				shouldBeInfoMessagePresented);
	}

	/**
	 * Fills input with string of length defined via parameter, the string is
	 * always the same
	 * 
	 * @param input
	 * @param lengthOfString
	 */
	public void fillInputWithStringOfLength(JQueryLocator input,
			int lengthOfString) {

		StringBuilder sb = new StringBuilder();

		for (int i = 1; i <= lengthOfString; i++) {

			sb.append("x");
		}

		selenium.type(input, sb.toString());
	}

	/**
	 * test whether all rows in the table contains empty strings
	 * 
	 * @return true if there is a row in the table with empty string, false
	 *         otherwise
	 */
	public boolean testWhetherTableContainsNonEmptyStrings(JQueryLocator table) {

		JQueryLocator tr = jq(table.getRawLocator() + " > tr");

		for (Iterator<JQueryLocator> i = tr.iterator(); i.hasNext();) {

			boolean result = testWhetherRowContainsNonEmptyStrings(i.next());

			if (result) {

				return true;
			}

		}

		return false;

	}

	/* ***************************************************************************************************
	 * help methods
	 * **************************************************************
	 * *************************************
	 */

	/**
	 * tests whether the rows tds contains some non empty strings
	 * 
	 * @param row
	 * @return true if contains empty strings, false otherwise
	 */
	private boolean testWhetherRowContainsNonEmptyStrings(JQueryLocator row) {

		JQueryLocator td = jq(row.getRawLocator() + " > td");

		for (Iterator<JQueryLocator> i = td.iterator(); i.hasNext();) {

			String tdInTable = selenium.getText(i.next()).trim();

			if (tdInTable.isEmpty()) {

				return true;
			}
		}

		return false;
	}
}
