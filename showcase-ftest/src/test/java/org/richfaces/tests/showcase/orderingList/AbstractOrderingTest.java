package org.richfaces.tests.showcase.orderingList;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;

public class AbstractOrderingTest extends AbstractAjocadoTest {

	/* **********************************************************************
	 * Locators
	 * **********************************************************************
	 */

	protected JQueryLocator firstButton = jq(".rf-ord-up-top:eq({0})");
	protected JQueryLocator upButton = jq(".rf-ord-up:eq({0})");
	protected JQueryLocator downButton = jq(".rf-ord-down:eq({0})");
	protected JQueryLocator lastButton = jq("rf-ord-down-bottom:eq({0})");

	/* ********************************************************************************************
	 * Abstract methods
	 * **************************************************************
	 * ******************************
	 */
	
	protected void checkFirstButton(JQueryLocator optionLocator, JQueryLocator firstButton ) {
		
		checkButton(optionLocator.format(2), firstButton, optionLocator.format("eq(0)"));
		
		checkButton(optionLocator.format(10), firstButton, optionLocator.format("eq(0)"));
	}

	
	protected void checkLastButton(JQueryLocator optionLocator, JQueryLocator lastButton) {
		
		checkButton(optionLocator.format(2), lastButton, optionLocator.format("last"));
		
		checkButton(optionLocator.format(10), lastButton, optionLocator.format("last"));
	}
	
	protected void checkUpButton(JQueryLocator optionLocator, JQueryLocator upButton) {
		
		checkButton(optionLocator.format(2), upButton, optionLocator.format("eq(1)"));
		
		checkButton(optionLocator.format(10), upButton, optionLocator.format("eq(9)"));
	}
	
	protected void checkDownButton(JQueryLocator optionLocator, JQueryLocator downButton) {
		
		checkButton(optionLocator.format(2), downButton, optionLocator.format("eq(3)"));
		
		checkButton(optionLocator.format(10), downButton, optionLocator.format("eq(11)"));
	}
	
	/* *********************************************************************************************************************
	 * Help methods
	 ***********************************************************************************************************************/
	private void checkButton( JQueryLocator whichOptionToMove, JQueryLocator button, JQueryLocator whereItShouldBe) {
		
		String textOptionToMove = selenium.getText( whichOptionToMove );
		selenium.click( whichOptionToMove );
		
		selenium.click(button);
	
		String textWhereItShouldBe = selenium.getText(whereItShouldBe);
		
		assertEquals( textWhereItShouldBe, textOptionToMove, 
				"The option: " + whichOptionToMove + "should be now at the position: " + whereItShouldBe);
	}
	

}
