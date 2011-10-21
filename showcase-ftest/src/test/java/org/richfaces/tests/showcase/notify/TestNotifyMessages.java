package org.richfaces.tests.showcase.notify;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.richfaces.tests.showcase.message.AbstractTestMessage;
import org.testng.annotations.BeforeMethod;

public class TestNotifyMessages extends AbstractTestMessage {

	// this is overired since the name of the sample contains dashes which can
	// not be in the name of class
	@Override
	@BeforeMethod
	public void loadPage() {

		String addition = "richfaces/component-sample.jsf?skin=blueSky&demo=editor&sample=notify-messages&skin=blueSky";

		this.contextRoot = getContextRoot();

		selenium.open(URLUtils.buildUrl(contextRoot, "/showcase/", addition));
	}

	/* ****************************************************************
	 * Locators****************************************************************
	 */
	
	protected JQueryLocator notify = jq(".rf-ntf");
	
	/* ***************************************************************************
	 * Tests
	 *****************************************************************************/
	
	public void testAllInputsIncorrectValues() {
		
		List<String> messages = new ArrayList<String>();
		
		fillInputWithStringOfLength(nameInput, MINIMUM_OF_NAME - 1);
		fillInputWithStringOfLength(jobInput, MINIMUM_OF_JOB - 1);
		fillInputWithStringOfLength(addressInput, MINIMUM_OF_ADDRESS - 1);
		fillInputWithStringOfLength(zipInput, MINIMUM_OF_ZIP -1 );
		
		guardXhr(selenium).click(ajaxValidateButton);
		
		int numberOfNotifyMessages = selenium.getCount(notify);
		
		assertEquals( numberOfNotifyMessages, 4, "There should be 4 notify messages!");
		
		for(Iterator<JQueryLocator> i = notify.iterator(); i.hasNext(); ) {
			
			String notifyText = selenium.getText(i.next());
			
			messages.add(notifyText);
		}
		
		
	}
	

	
}
