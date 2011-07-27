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
package org.richfaces.tests.showcase.dropDownMenu;

import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTopMenu extends AbstractAjocadoTest {
	
	/* *********************************************************************************
	 * Constants
	 ***********************************************************************************/
	
	protected final String TOP_LVL_MENU = "td[class*=rf-tb-itm]";
	protected final String LABEL = "div[id*=label]";
	protected String  fileMenuItems = "div.rf-ddm-pos:eq(0) div[class*=rf-ddm-itm]";
	
	/* **********************************************************************************
	 * Locators 
	 ************************************************************************************/
	
	JQueryLocator fileMenu = jq( TOP_LVL_MENU + ":eq(0) " + LABEL );
	JQueryLocator linksMenu = jq( TOP_LVL_MENU + ":eq(1) " + LABEL );
	JQueryLocator currentSelection = jq("span:contains('Current Selection') > span");
	
	/* ************************************************************************************
	 * Tests
	 **************************************************************************************/
	
	@Test
	public void testSelectFromFileMenu() {
		
		int numberOfFileMenuItems = selenium.getCount( jq( fileMenuItems ) );
		
		for( int i = 0; i < numberOfFileMenuItems; i++) {
			
			if( (i == 3) || (i == 4) ) {
				continue;
			}
			
			JQueryLocator item = jq( fileMenuItems + ":eq(" + i +")");
			
			if( (i != 2) && (i != 3) && (i != 4) ) {
				
				guardNoRequest(selenium).fireEvent( fileMenu, Event.MOUSEOVER );
				
				clickSomewhereAndCheckCurrentSelection(item);
				
			} 
			
		}
	}
	
	/* **************************************************************************************************
	 * Help methods
	 ****************************************************************************************************/
	
	/**
	 * Click somewhere in the menu and check the current selection info
	 * 
	 * @param whereToClick the item from menu, where it will be clicked
	 */
	private void clickSomewhereAndCheckCurrentSelection( JQueryLocator whereToClick ) {
		
		String textFromMenu = selenium.getText(whereToClick);
		
		guardXhr(selenium).click( whereToClick );
		
		String textFromCurrentSelectionInfo = selenium.getText(currentSelection);
		
		assertEquals( textFromCurrentSelectionInfo, textFromMenu, "Something else was selected than the current selection info says");
	}
	
	
}
