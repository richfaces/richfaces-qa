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
package org.richfaces.tests.showcase.dataScroller;


import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractShowcaseTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestDataScrollerAPI extends AbstractShowcaseTest {
	
	/* **********************************************************************************************************************
	 * Constants
	 ************************************************************************************************************************/
	private final String CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER = "rf-ds-nmb-btn";
	private final String CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER = CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + " rf-ds-act";
	
	/* ************************************************************************************************************************
	 * Locators
	 **************************************************************************************************************************/
	private JQueryLocator buttonWithNumberOfPageActive = jq("span[class*='" + CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER + "']");
	
	private JQueryLocator firstImgOnThePage = jq("table[id$=repeat] tbody tr:first td:eq(1) img:eq(0)");
	
	private JQueryLocator previousButton = jq("table[id$=repeat] tbody tr:first td:first img");
	private JQueryLocator nextButton = jq("table[id$=repeat] tbody tr:first td:last img");
	
	/* *************************************************************************************************************************
	 * Tests
	 ****************************************************************************************************************************/
	
	@Test
	public void testNumberOfPagesButtons() {
		
		checkNumberOfPagesButtons(1);
		
		checkNumberOfPagesButtons(2);
		
		checkNumberOfPagesButtons(3);
		
	}
	
	@Test
	public void testAPINextPrevious() {
		
		int currentNumberOfThePage = getNumberOfCurrentPage();
		
		if ( currentNumberOfThePage > 1 ) {
			
			guardXhr(selenium).click( previousButton );
			guardXhr(selenium).click( previousButton );
		}
		
		String srcBeforeClicking = getSrcOfFirstImage();
		
		guardXhr(selenium).click(nextButton);
		
		String srcAfterClicking = getSrcOfFirstImage();
		
		assertFalse( srcBeforeClicking.equals(srcAfterClicking), "The data should be different on he different pages!");
		
		int numberOfThePageAfterClicking = getNumberOfCurrentPage();
		
		assertEquals(numberOfThePageAfterClicking, currentNumberOfThePage + 1, "The current number of the page should be higher");
		
		guardXhr(selenium).click(previousButton);
		
		numberOfThePageAfterClicking = getNumberOfCurrentPage();
		
		assertEquals(numberOfThePageAfterClicking, currentNumberOfThePage, "The current number of the page should be less");
	}
	
	/* ***************************************************************************************************************************************
	 * Help methods
	 ******************************************************************************************************************************************/
	
	/**
	 * Checking the buttons which have number of pages
	 */
	private void checkNumberOfPagesButtons( int numberOfPage ) {
		
		JQueryLocator checkingButton = jq("a[class*='" + CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:contains('" + numberOfPage +"')");
		
		String imgSrcBeforeClick = null;
		
		if( selenium.isElementPresent(checkingButton) ) {
			
			imgSrcBeforeClick = getSrcOfFirstImage();
			guardXhr(selenium).click( checkingButton );
		} else {
			
			JQueryLocator inactiveButton = jq("a[class*='" + CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + "']:first");
			
			imgSrcBeforeClick = getSrcOfFirstImage();
			guardXhr(selenium).click( inactiveButton );
			numberOfPage = getNumberOfCurrentPage();
		}
		
		String imgSrcAfterClick = getSrcOfFirstImage();
		
		assertFalse( imgSrcAfterClick.equals(imgSrcBeforeClick) , "The data should be different on the different pages!");
		
		int actualCurrentNumberOfPage = getNumberOfCurrentPage();
		
		assertEquals(actualCurrentNumberOfPage, numberOfPage, "We should be on the " + numberOfPage +". page");
	}
	
	/**
	 * Gets the number of the current page
	 * @return number of the current page
	 */
	private int getNumberOfCurrentPage() {
		
		String currentPage = selenium.getText( buttonWithNumberOfPageActive ).trim();
		int currentPageInt = Integer.valueOf(currentPage).intValue();
		
		return currentPageInt;
	}
	
	/**
	 * Gets the src attribute of the first image on the page
	 */
	private String getSrcOfFirstImage() {
	
		String src = selenium.getAttribute( firstImgOnThePage.getAttribute(Attribute.SRC) );
		
		return src;
	}
	

}
