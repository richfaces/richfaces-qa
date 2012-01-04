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
package org.richfaces.tests.showcase.pickList;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestPickList extends AbstractPickListTest {

	private JQueryLocator sourceItemsWithColumns = jq("[id$='SourceItems']:eq(1)");

	private JQueryLocator targetItemsWithColumns = jq("[id$='TargetItems']:eq(1)");

	/* *************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***************************
	 */

	@Test
	public void testAddAllButtonSimplePickList() {

		super.testAddAllButtonSimplePickList();
	}

	@Test
	public void testRemoveAllButtonSimplePickList() {

		super.testRemoveAllButtonSimplePickList();
	}
	
	@Test
	public void testRemoveButton() {
		
		super.testRemoveButton();
	}

	@Test
	public void testAddAllButtonWithColumnsPickList() {

		String[] availableCities = selenium.getText(sourceItemsWithColumns)
				.split("\n");
		int numberOfFlagsInAvailableCities = selenium
				.getCount(jq(sourceItemsWithColumns.getRawLocator() + " img"));

		selenium.click(addAllButton.format(1));

		areThereSomeSelectedCities(targetItemsWithColumns, true);

		checkThatListContains(availableCities, targetItemsWithColumns);

		int numberOfFlagsInSelectedCities = selenium
				.getCount(jq(targetItemsWithColumns.getRawLocator() + " img"));

		assertEquals(
				numberOfFlagsInAvailableCities,
				numberOfFlagsInSelectedCities,
				"the number of flags should be the same in the avaiable cities and selected cities!");
	}

	@Test
	public void testRemoveAllButtonWithColumnsPickList() {

		checkRemoveAllButton(1, targetItemsWithColumns, 1);
	}

	@Test
	public void testAddButtonSimplePickList() {

		super.testAddButtonSimplePickList();
	}

	@Test
	public void testAddButtonWithColumnsPickList() {

		checkAddButton(sourceItemsWithColumns, addButton.format(1),
				targetItemsWithColumns);
	}

	@Test
	public void testFirstButtonOrdering() {

		super.testFirstButtonOrdering();
	}

	@Test
	public void testUpButtonOrdering() {

		super.testUpButtonOrdering();
	}

	@Test
	public void testDownButtonOrdering() {

		super.testDownButtonOrdering();
	}

	@Test
	public void testLastButtonOrdering() {

		super.testLastButtonOrdering();
	}
}
