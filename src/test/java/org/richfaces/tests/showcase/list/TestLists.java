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
package org.richfaces.tests.showcase.list;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractShowcaseTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestLists extends AbstractShowcaseTest {
	
	/* *****************************************************************************
	 * Locators
	 *******************************************************************************/
	
	private JQueryLocator orderedList = jq("a:contains('ordered')");
	private JQueryLocator unordered = jq("a:contains('unordered')");
	private JQueryLocator definitions = jq("a:contains('definitions')");
	
	/* ***********************************************************************************************
	 * Tests
	 *************************************************************************************************/
	
	@Test
	public void testOrderedList() {
	
		checkList(orderedList, "ol#list");
	}
	
	@Test
	public void testUnorderedList() {
		
		checkList(unordered, "ul#list");
	}
	
	@Test
	public void testDefinitionsList() {
		
		checkList(definitions, "dl#list");
	}
	
	/* ********************************************************************************************************************
	 * Help methods
	 **********************************************************************************************************************/
	
	private void checkList( JQueryLocator typeOfList, String listString) {
		
		guardXhr(selenium).click(typeOfList);
		
		JQueryLocator list = jq(listString);
		
		assertTrue( selenium.isElementPresent(list), "There should list " + listString );
		
		JQueryLocator liElement = jq( listString + " > li");
		
		for( Iterator<JQueryLocator> i = liElement.iterator(); i.hasNext(); ) {
			
			String textOfLi = selenium.getText(i.next()).trim();
			assertFalse( textOfLi.equals(""), "The list should not contain empty strings");
		}
	}
}
