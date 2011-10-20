package org.richfaces.tests.showcase.orderingList;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;

public class TestOrderingList extends AbstractAjocadoTest {
	
	/* **************************************************************************
	 * Locators
	 ****************************************************************************/
	
	private JQueryLocator optionToOrderSimpleList = jq(".rf-ord-list:eq(0) .rf-ord-opt:eq({0})");
	private JQueryLocator optionToOrderWithColumns = jq(".rf-ord-list:eq(0) .rf-ord-opt:eq({0})");
	
	/* ******************************************************************************
	 * Tests
	 *******************************************************************************/
	
	public void testFirstButtonSimplePickList() {
		
		
	}
	
	
	
}
