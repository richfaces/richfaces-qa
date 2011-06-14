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
package org.richfaces.tests.showcase.dataGrid;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.dataScroller.AbstractDataScrollerTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestGrid extends AbstractDataScrollerTest {
	
	/* *****************************************************************************************************
	 * Locators
	 *******************************************************************************************************/
	JQueryLocator firstPanelGridOnThePage = jq("td.rf-dg-c:first");
	JQueryLocator lastPanelGridOnThePage = jq("td.rf-dg-c:contains('Price'):last");
	
	/* ********************************************************************************************************
	 * Tests
	 **********************************************************************************************************/
	
	@Test
	public void testDataGridIsNotEmpty() {
		
		@SuppressWarnings("unused")
		Car pageFirstCar = retrieveCarFromThePanelGrid(firstPanelGridOnThePage);
		@SuppressWarnings("unused")
		Car pageLastCar = retrieveCarFromThePanelGrid(lastPanelGridOnThePage);
		
		guardXhr(selenium).click(lastPageButton);
		
		pageFirstCar = retrieveCarFromThePanelGrid(firstPanelGridOnThePage);
		pageLastCar = retrieveCarFromThePanelGrid(lastPanelGridOnThePage);
	}
	
	@Test
	public void testDataGridIsScrollable() {
		
		int numberOfThePageBeforeClick;
		Car carBeforeClicking; 
		
		if( !selenium.isElementPresent(firstPageButton) ) {
			
			numberOfThePageBeforeClick = getNumberOfCurrentPage();
			carBeforeClicking = retrieveCarFromThePanelGrid(firstPanelGridOnThePage);
			
			guardXhr(selenium).click(nextButton);
			
		} else {
		
			guardXhr(selenium).click(firstPageButton);
			
			numberOfThePageBeforeClick = getNumberOfCurrentPage();
			carBeforeClicking = retrieveCarFromThePanelGrid(firstPanelGridOnThePage);
			
			guardXhr(selenium).click(nextButton);
			
		}
		
		int numberOfThePageAfterClick = getNumberOfCurrentPage();
		Car carAfterClicking = retrieveCarFromThePanelGrid(firstPanelGridOnThePage);
		
		assertEquals(numberOfThePageBeforeClick, numberOfThePageAfterClick - 1, 
				"The next button does not works!");
		
		assertFalse( carBeforeClicking.equals(carAfterClicking), "Data should be different on the different pages!");
	}
	
	/* *********************************************************************************************************
	 * Help methods
	 ************************************************************************************************************/
	private Car retrieveCarFromThePanelGrid( JQueryLocator panelGrid ) {
		
		Car car = new Car();
		
		String panelGridText = selenium.getText(panelGrid);

		String[] partsOfPanelGrid = panelGridText.split(" ");
		
		try {
		
			car.setVendor(partsOfPanelGrid[0].trim());
			car.setModel(partsOfPanelGrid[1].trim());
			car.setPrice(partsOfPanelGrid[3].trim());
			car.setMileage(partsOfPanelGrid[5].trim());
			car.setVin(partsOfPanelGrid[7].trim());
			car.setStock(partsOfPanelGrid[9]);
			
		} catch (IllegalArgumentException ex) {
			
			fail("The table should not be empty");
		}
		
		return car;
	}

}
