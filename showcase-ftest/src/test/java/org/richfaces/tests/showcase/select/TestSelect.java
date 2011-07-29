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
package org.richfaces.tests.showcase.select;


import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertTrue;
import org.jboss.arquillian.ajocado.dom.Event;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSelect extends AbstractAjocadoTest {

	/* *************************************************************************************
	 * Constants
	 * *****************************************************************
	 * ********************
	 */

	protected final String CLASS_OF_SELECTED_OTPION = "rf-sel-sel";

	/* ***********************************************************************************
	 * Locators
	 * ******************************************************************
	 * *****************
	 */

	protected JQueryLocator selectOpenButton = jq("span.rf-sel-btn:eq({0})");
	protected JQueryLocator option = jq("div.rf-sel-opt:contains('{0}')");
	protected JQueryLocator manualInput = jq("input[type=text]:eq(1)");

	/* ***********************************************************************************
	 * Locators
	 * ******************************************************************
	 * *****************
	 */

	@Test
	public void testSimpleSelectMouseSelect() {

		for (int i = 1; i <= 5; i++) {

			selenium.mouseDown(selectOpenButton.format(0));

			JQueryLocator particularOption = option.format("Option " + i);

			selenium.click(particularOption);

			String classOfSelectedOption = selenium
					.getAttribute(particularOption
							.getAttribute(Attribute.CLASS));

			assertTrue(
					classOfSelectedOption.contains(CLASS_OF_SELECTED_OTPION),
					"The option " + particularOption.getRawLocator()
							+ " should be selected");
		}

	}

	@Test
	public void testSelectManualInputByMouse() {

		selectSomethingFromCapitalsSelectAndCheck("Arizona");
		
		selectSomethingFromCapitalsSelectAndCheck("Florida");
		
		selectSomethingFromCapitalsSelectAndCheck("California");
	}
	
	/* *******************************************************************************
	 * Help methods
	 *********************************************************************************/
	
	/**
	 * Types the beginning of capital and then selects from poppup and check whether right
	 * option was selected
	 */
	private void selectSomethingFromCapitalsSelectAndCheck( String capital ) {
		
		eraseInput(manualInput);
		
		JQueryLocator particularOption = option.format(capital);

		selenium.type(manualInput, capital.substring(0, 2));
		selenium.fireEvent(manualInput, Event.KEYPRESS);
		
		selenium.click(particularOption);

		String classOfSelectedOption = selenium.getAttribute(particularOption
				.getAttribute(Attribute.CLASS));

		assertTrue(classOfSelectedOption.contains(CLASS_OF_SELECTED_OTPION),
				"The option " + particularOption.getRawLocator()
						+ " should be selected");

		
	}

}
