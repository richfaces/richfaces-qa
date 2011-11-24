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
package org.richfaces.tests.showcase.orderingList;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestOrderingList extends AbstractOrderingTest {
	
	/* **************************************************************************
	 * Locators
	 ****************************************************************************/
	
	private JQueryLocator optionToOrderSimpleList = jq(".rf-ord:eq(0) .rf-ord-opt{0}");
	private JQueryLocator optionToOrderWithColumns = jq(".rf-ord:eq(1) .rf-ord-opt{0}");
	
	/* ******************************************************************************
	 * Tests
	 *******************************************************************************/
	
	@Test
	public void testFirstButtonSimpleOrderingList() {
		
		checkFirstButton(optionToOrderSimpleList, firstButton.format(0));
	}
	
	@Test
	public void testUpButtonSimpleOrderingList() {
		
		checkUpButton(optionToOrderSimpleList, upButton.format(0));
	}
	
	@Test
	public void testDownButtonSimpleOrderingList() {
		
		checkDownButton(optionToOrderSimpleList, downButton.format(0));
	}
	
	@Test
	public void testLastButtonSimpleOrderingList() {
		
		checkLastButton(optionToOrderSimpleList, lastButton.format(0));
	}
	
	@Test
	public void testFirstButtonWithColumnsOrderingList() {
		
		checkFirstButton(optionToOrderWithColumns, firstButton.format(1));
	}
	
	@Test
	public void testUpButtonWithColumnsOrderingList() {
		
		checkUpButton(optionToOrderWithColumns, upButton.format(1));
	}
	
	@Test
	public void testDownButtonWithColumnsOrderingList() {
		
		checkDownButton(optionToOrderWithColumns, downButton.format(1));
	}
	
	@Test
	public void testLastButtonWithColumnsOrderingList() {
		
		checkLastButton(optionToOrderWithColumns, lastButton.format(1));
	}
	
	@Test
	public void testFirstAndUpButtonsDisabledAfterFirst() {
		
		checkFirstAndUpButtonsDisabled(optionToOrderSimpleList, 
				firstButton.format(0), firstButton.format(0), upButton.format(0));
	}
	
	@Test
	public void testFirstAndUpButtonDisabledAfterUp() {
		
		checkFirstAndUpButtonsDisabled(optionToOrderSimpleList, 
				upButton.format(0), firstButton.format(0), upButton.format(0));
	}
	
	@Test
	public void testLastAndDownButtonDisabledAfterLast() {
		
		checkDownAndLastButtonsDisabled(optionToOrderSimpleList, lastButton.format(0), lastButton.format(0), downButton.format(0));
	}
	
	@Test
	public void testLastAndDownButtonDisabledAfterDown() {
		
		checkDownAndLastButtonsDisabled(optionToOrderSimpleList, downButton.format(0), lastButton.format(0), downButton.format(0));
	}
	
}
