package org.richfaces.tests.showcase.notify;

import static org.jboss.arquillian.ajocado.Ajocado.elementNotPresent;
import static org.jboss.arquillian.ajocado.Ajocado.elementPresent;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;

public class AbstractNotifyTest extends AbstractAjocadoTest {

	/* *********************************************************************
	 * Locators
	 ***********************************************************************/
	protected JQueryLocator notify = jq(".rf-ntf");

	
	/* **********************************************************************
	 * Methods
	 ************************************************************************/
	protected void waitUntilNotifyDissappeares(int timeout) {

		waitGui.failWith(
				new RuntimeException("The message should once dissapear!"))
				.interval(100).timeout(timeout)
				.until(elementNotPresent.locator(notify));
	}

	protected void waitUntilNotifyAppears(int timeout) {

		waitGui.failWith(new RuntimeException("The message should appear!"))
				.interval(50).timeout(timeout)
				.until(elementPresent.locator(notify));

	}

}
