package org.richfaces.tests.showcase.editor;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestAdvancedConfiguration extends AbstractAjocadoTest {

	// this is overired since the name of the sample contains dashes which can
	// not be in the name of class
	@Override
	@BeforeMethod
	public void loadPage() {

		String addition = "richfaces/component-sample.jsf?skin=blueSky&demo=editor&sample=advanced-configuration&skin=blueSky";

		this.contextRoot = getContextRoot();

		selenium.open(URLUtils.buildUrl(contextRoot, "/showcase/", addition));
	}

	/* ******************************************************************************
	 * Constants
	 * *****************************************************************
	 * ****************
	 */
	
	protected final String NEW_PAGE_ENG = "New Page";
	protected final String NEW_PAGE_FR = "Nouvelle page";
	protected final String NEW_PAGE_DE = "Neue Seite";

	/* ***************************************************************************************************
	 * Locators
	 * ******************************************************************
	 * *********************************
	 */

	protected JQueryLocator englishCheckbox = jq("input[type=radio]:eq(0)");
	protected JQueryLocator frenchCheckbox = jq("input[type=radio]:eq(1)");
	protected JQueryLocator germanCheckbox = jq("input[type=radio]:eq(2)");

	protected JQueryLocator newPageButton = jq(".cke_button_newpage");

	/* **************************************************************************
	 * Tests
	 * *********************************************************************
	 * *****
	 */

	@Test
	public void testEnglishLanguage() {
		
		selenium.check(englishCheckbox);
		guardXhr(selenium).fireEvent(englishCheckbox, Event.CLICK);
		
		String titleOfNewPageButton = 
				selenium.getAttribute(newPageButton.getAttribute(Attribute.TITLE));
		
		assertEquals( titleOfNewPageButton, NEW_PAGE_ENG, "The language was not changed to english!");
	}
	
	@Test
	public void testFrenchLanguage() {
		
		selenium.check(frenchCheckbox);
		guardXhr(selenium).fireEvent(frenchCheckbox, Event.CLICK);
		
		String titleOfNewPageButton = 
				selenium.getAttribute(newPageButton.getAttribute(Attribute.TITLE));
		
		assertEquals( titleOfNewPageButton, NEW_PAGE_FR, "The language was not changed to french!");
	}
	
	@Test
	public void testGermanLanguage() {
		
		selenium.check(germanCheckbox);
		guardXhr(selenium).fireEvent(germanCheckbox, Event.CLICK);
		
		String titleOfNewPageButton = 
				selenium.getAttribute(newPageButton.getAttribute(Attribute.TITLE));
		
		assertEquals( titleOfNewPageButton, NEW_PAGE_DE, "The language was not changed to german!");
	}
	
	@Test
	public void testUserFocusAutomaticallyOnEditor() {
		
		fail("implement me correctly!");
	}
	
}
