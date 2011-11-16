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
package org.richfaces.tests.showcase.notify;

import static org.jboss.arquillian.ajocado.Ajocado.elementNotPresent;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.richfaces.tests.showcase.message.AbstractTestMessage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestNotifyMessages extends AbstractTestMessage {

	/* ****************************************************************
	 * Locators****************************************************************
	 */

	protected JQueryLocator notify = jq(".rf-ntf");

	/* ***************************************************************************
	 * Tests
	 * *********************************************************************
	 * ******
	 */
	@Test
	public void testAllInputsIncorrectValuesBlurActivation() {

		checkNotifyMessages(false);
	}

	@Test
	public void testAllInputsIncorrectValuesSubmitActivation() {

		checkNotifyMessages(true);
	}

	/* ********************************************************************************************************************
	 * Help methods
	 * **************************************************************
	 * ******************************************************
	 */

	private void checkNotifyMessages(boolean submitActivation) {

		List<String> messages = new ArrayList<String>();

		fillInputWithStringOfLength(nameInput, MINIMUM_OF_NAME - 1);
		fillInputWithStringOfLength(jobInput, MINIMUM_OF_JOB - 1);
		fillInputWithStringOfLength(addressInput, MINIMUM_OF_ADDRESS - 1);
		fillInputWithStringOfLength(zipInput, MINIMUM_OF_ZIP - 1);

		if (submitActivation) {
			
			waitGui.until( elementNotPresent.locator(notify));
			
			guardXhr(selenium).click(ajaxValidateButton);
		}

		int numberOfNotifyMessages = selenium.getCount(notify);

		assertEquals(numberOfNotifyMessages, 4,
				"There should be 4 notify messages!");

		for (Iterator<JQueryLocator> i = notify.iterator(); i.hasNext();) {

			String notifyText = selenium.getText(i.next());

			messages.add(notifyText);
		}

		// checking that rich:message is there, and has correct value
		isThereErrorMessage(nameError, NAME_ERROR_LESS_THAN_MINIMUM, true);
		isThereErrorMessage(jobError, JOB_ERROR_LESS_THAN_MINIMUM, true);
		isThereErrorMessage(addressError, ADDRESS_ERROR_LESS_THAN_MINIMUM, true);
		isThereErrorMessage(zipError, ZIP_ERROR_LESS_THAN_MINIMUM, true);

		// checking the content of the rich:notify messages
		assertTrue(messages.contains(NAME_ERROR_LESS_THAN_MINIMUM),
				"The notify message for name is incorrect");
		assertTrue(messages.contains(ADDRESS_ERROR_LESS_THAN_MINIMUM),
				"The notify message for address is incorrect");
		assertTrue(messages.contains(JOB_ERROR_LESS_THAN_MINIMUM),
				"The notify message for job is incorrect");
		assertTrue(messages.contains(ZIP_ERROR_LESS_THAN_MINIMUM),
				"The notify message for zip is incorrect");
	}
}
