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
package org.richfaces.tests.showcase.autocomplete;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.awt.event.KeyEvent;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractShowcaseTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestCachedAjax  extends AbstractShowcaseTest {
	
	/* **************************************************************************************
	 * Locators
	 ****************************************************************************************/
	
	protected JQueryLocator minCharInput = jq("input[type=text]:eq(0)");
	protected JQueryLocator multipleSelectionInput = jq("input[type=text]:eq(1)");
	protected JQueryLocator selectFirstFalseInput = jq("input[type=text]:eq(2)");
	protected JQueryLocator selection = jq("div.rf-au-itm");
	
	/* **************************************************************************************
	 * Tests
	 ****************************************************************************************/
	
	@Test
	public void testMinCharInput() {
		
		selenium.typeKeys(minCharInput, "a");
		selenium.fireEvent(minCharInput, Event.KEYPRESS);
		
		assertFalse( selenium.isElementPresent(selection), "The selection should not be visible, since there is only one char!");
		
		selenium.typeKeys(minCharInput, "ar");
		guardXhr(selenium).fireEvent(minCharInput, Event.KEYPRESS);
		
		assertTrue( selenium.isVisible(selection), "The selection should be visible, since there are two chars!");
		
		String actualArizona = selenium.getText( jq(selection.getRawLocator() + ":eq(0)") );
		assertEquals( actualArizona, "Arizona", "The provided option should be Arizona");
		
		String actualArkansas = selenium.getText( jq(selection.getRawLocator() + ":eq(1)") );
		assertEquals( actualArkansas, "Arkansas", "The provided option should be Arkansas");
		
	}
	
	@Test
	public void testMultipleSelectionInput() {
		
		selenium.focus(multipleSelectionInput);
		selenium.typeKeys( multipleSelectionInput, "a");
		guardXhr(selenium).fireEvent(multipleSelectionInput, Event.KEYPRESS);
		
		assertTrue( selenium.isVisible(selection), "The selection should be visible, since there is correct starting char!");
		
		selenium.keyPressNative( KeyEvent.VK_ENTER );
		
		selenium.typeKeys( multipleSelectionInput, "Alabama w");
		guardXhr(selenium).fireEvent(multipleSelectionInput, Event.KEYPRESS);
		
		assertTrue( selenium.isVisible(selection), "The selection should be visible, since there is correct starting char!");
		
		selenium.keyPressNative( KeyEvent.VK_ENTER );
		
		waitModel.waitForChange(retrieveText.locator(multipleSelectionInput));
		String actualContentOfInput = selenium.getValue(multipleSelectionInput);
		assertEquals(actualContentOfInput, "Alabama Washington", "The input should contain something else!");
	}
	
	@Test
	public void testSelectFirstFalseInput() {
		
		selenium.typeKeys( selectFirstFalseInput, "a");
		guardXhr(selenium).fireEvent(selectFirstFalseInput, Event.KEYPRESS);
		
		assertTrue( selenium.isVisible(selection), "The selection should be visible, since there is correct starting char!");
		
		selenium.keyPressNative( KeyEvent.VK_ENTER );
		
		String actualValueOfInput = selenium.getValue(selectFirstFalseInput);
		assertEquals(actualValueOfInput, "a", "The content should be the letter which was types and not something else, since selectFirst is false!");
	}

}
