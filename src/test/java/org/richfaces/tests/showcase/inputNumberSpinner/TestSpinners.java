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
package org.richfaces.tests.showcase.inputNumberSpinner;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractShowcaseTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSpinners extends AbstractShowcaseTest {

	/* *********************************************************************************
	 * Constants
	 * *****************************************************************
	 * ****************
	 */

	protected final int MAX_VALUE = 100;
	protected final int MIN_VALUE = 0;

	protected final int FIRST_INPUT_STEP = 1;
	protected final int SECOND_INPUT_STEP = 10;

	/* *********************************************************************************
	 * Locators
	 * ******************************************************************
	 * ***************
	 */

	protected JQueryLocator inputChangingByOne = jq("input[type=text]:eq(0)");
	protected JQueryLocator inputChangingByTen = jq("input[type=text]:eq(1)");

	protected JQueryLocator increaseByOne = jq("span.rf-insp-inc:eq(0)");
	protected JQueryLocator increaseByTen = jq("span.rf-insp-inc:eq(1)");

	protected JQueryLocator decreaseByOne = jq("span.rf-insp-dec:eq(0)");
	protected JQueryLocator decreaseByTen = jq("span.rf-insp-dec:eq(1)");

	/* *********************************************************************************
	 * Tests
	 * *********************************************************************
	 * ************
	 */

	@Test
	public void testInputIncreasedByOne() {

		increaseByStep(inputChangingByOne, increaseByOne, FIRST_INPUT_STEP);

	}

	@Test
	public void testInputIcreaseByTen() {

		increaseByStep(inputChangingByTen, increaseByTen, SECOND_INPUT_STEP);

	}
	
	@Test
	public void testInputDecreaseByOne() {
		
		decreaseByStep(inputChangingByOne, decreaseByOne, FIRST_INPUT_STEP);
	}
	
	@Test
	public void testInputDecreaseByTen() {
		
		decreaseByStep(inputChangingByTen, decreaseByTen, SECOND_INPUT_STEP);
	}

	/* **************************************************************************************************************************
	 * Help methods
	 * **************************************************************
	 * ************************************************************
	 */

	/**
	 * Decrease by step via decrease spinner
	 * 
	 * @param input
	 *            which value will be decreased
	 * @param increaseSpinner
	 *            the decrease spinner which will be clicked on
	 * @param step
	 *            the step by which will be the value decreased
	 */
	private void decreaseByStep(JQueryLocator input,
			JQueryLocator decreaseSpinner, int step) {

		int currentValueOfInput = Integer.valueOf(selenium.getValue(input));

		for (int i = currentValueOfInput; i > MIN_VALUE; i -= step) {

			selenium.clickAt(decreaseSpinner, new Point(1, 1));

			assertEquals(Integer.valueOf(selenium.getValue(input)).intValue(),
					currentValueOfInput - step,
					"The value should be decreased by " + step);

			currentValueOfInput = Integer.valueOf(selenium.getValue(input));
		}

		selenium.clickAt(decreaseSpinner, new Point(1, 1));

		currentValueOfInput = Integer.valueOf(selenium.getValue(input));

		assertEquals(currentValueOfInput, MAX_VALUE,
				"The current value in the input should be minimal, so "
						+ MAX_VALUE);
	}

	/**
	 * Increase by step via increase spinner
	 * 
	 * @param input
	 *            which value will be increased
	 * @param increaseSpinner
	 *            the increase spinner which will be clicked on
	 * @param step
	 *            the step by which will be the value increased
	 * 
	 */
	private void increaseByStep(JQueryLocator input,
			JQueryLocator increaseSpinner, int step) {

		int currentValueOfInput = Integer.valueOf(selenium.getValue(input));

		for (int i = currentValueOfInput; i < MAX_VALUE; i += step) {

			selenium.clickAt(increaseSpinner, new Point(1, 1));

			assertEquals(Integer.valueOf(selenium.getValue(input)).intValue(),
					currentValueOfInput + step,
					"The value should be increased by " + step);

			currentValueOfInput = Integer.valueOf(selenium.getValue(input));
		}

		selenium.clickAt(increaseSpinner, new Point(1, 1));

		currentValueOfInput = Integer.valueOf(selenium.getValue(input));

		assertEquals(currentValueOfInput, MIN_VALUE,
				"The current value in the input should be minimal, so "
						+ MIN_VALUE);
	}

}
