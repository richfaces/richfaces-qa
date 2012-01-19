/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest;

import static org.jboss.arquillian.ajocado.Ajocado.id;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.Retriever;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cheiron.retriever.ScriptEvaluationRetriever;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.selenium.locator.reference.LocatorReference;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Templates;

/**
 * Abstract test case used as a basis for majority of test cases.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22749 $
 */
@RunAsClient
public abstract class AbstractMetamerTest extends Arquillian {

	@ArquillianResource
	protected URL contextPath;

	/**
	 * The path to the metamer application.
	 */
	public static final String WEBAPP_SRC = "../application/src/main/webapp";

	/**
	 * timeout in miliseconds
	 */
	public static final long TIMEOUT = 5000;

	protected JQueryLocator time = jq("span[id$=requestTime]");
	protected JQueryLocator renderChecker = jq("span[id$=renderChecker]");
	protected JQueryLocator statusChecker = jq("span[id$=statusCheckerOutput]");
	protected JQueryLocator jsFunctionChecker = jq("span[id$=jsFunctionChecker]");
	protected IdLocator fullPageRefreshIcon = id("controlsForm:fullPageRefreshImage");
	protected IdLocator rerenderAllIcon = id("controlsForm:reRenderAllImage");
	protected TextRetriever retrieveRequestTime = retrieveText.locator(time);
	protected Retriever<String> retrieveWindowData = new ScriptEvaluationRetriever()
			.script(js("window.data"));
	protected TextRetriever retrieveRenderChecker = retrieveText
			.locator(jq("#renderChecker"));
	protected TextRetriever retrieveStatusChecker = retrieveText
			.locator(jq("#statusCheckerOutput"));
	protected TextRetriever retrieveJsFunctionChecker = retrieveText
			.locator(jsFunctionChecker);
	protected PhaseInfo phaseInfo = new PhaseInfo();
	protected LocatorReference<JQueryLocator> attributesRoot = new LocatorReference<JQueryLocator>(
			pjq("span[id$=:attributes:panel]"));

	@Inject
	@Templates({ "plain", "richAccordion", "richDataTable",
			"richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
			"richList", "richCollapsiblePanel", "richPanel", "richTabPanel",
			"richTogglePanel", "richPopupPanel", "a4jRegion", "a4jRepeat",
			"hDataTable", "hPanelGrid", "uiRepeat" })
	protected TemplatesList template;

	/**
	 * Returns the url to test page to be opened by Selenium
	 * 
	 * @return absolute url to the test page to be opened by Selenium
	 */
	public abstract URL getTestUrl();

	@Deployment(testable = false)
	public static WebArchive createTestArchive() {

		WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class,
				new File("target/metamer.war"));
		return war;
	}

	/**
	 * Factory method for creating instances of class JQueryLocator which
	 * locates the element using <a
	 * href="http://api.jquery.com/category/selectors/">JQuery Selector</a>
	 * syntax. It adds "div.content " in front of each selector.
	 * 
	 * @param jquerySelector
	 *            the jquery selector
	 * @return the j query locator
	 * @see JQueryLocator
	 */
	public static JQueryLocator pjq(String jquerySelector) {

		String escapedString = jq(jquerySelector).getRawLocator();

		//if (jquerySelector.trim().length() > 0 && jquerySelector.contains("[")) {
			//escapedString = escapeSelector(jquerySelector);
		//}

		return new JQueryLocator("div.content " + escapedString);
	}

	/*private static String escapeSelector(String jquerySelector) {

		// find the text between []
		int indexOpen = jquerySelector.indexOf("[");
		String escapedString = null;
		
		if (indexOpen != -1) {
			int indexClose = jquerySelector.indexOf("]");
			String onlyThisNeedToBeEscaped = jquerySelector.substring(
					indexOpen, indexClose + 1);
			
			char[] escapedChars = { ':' };
			escapedString = StringUtils.escape(onlyThisNeedToBeEscaped,
					escapedChars, '\\');

			escapedString = jquerySelector.replace(onlyThisNeedToBeEscaped,
					escapedString);
			
			String checkFurther= escapedString.substring(escapedString.indexOf("]")); 
			
			if( checkFurther.indexOf("[") != -1 ) {
				
				String furtherEscapedPart = escapeSelector(checkFurther);
				escapedString = escapedString.replace(checkFurther, furtherEscapedPart);
			}
		}

		return escapedString;
	}*/

	/**
	 * This method should be called in each test class from the method with
	 * annotation @Deployment, to ensure that deployed war will contain all
	 * environment specific files. In other words when war is deployed on Tomcat
	 * or other containers needs to have some specific files, the same apply for
	 * testing with different JSF implementations.
	 * 
	 * @param war
	 *            to be altered
	 * @return war which is altered according to the test environment
	 */
	protected static WebArchive alterWarAccordingToTestEnvironment(
			WebArchive war) {

		String tomcat = System.getProperty("TOMCAT");

		if (tomcat != null && tomcat.equals("true")) {
			war = alterAccordingToTomcat(war);
		}

		// TODO
		return (WebArchive) war;
	}

	/**
	 * Alter the war according to the Tomcat specifics
	 * 
	 * @param war
	 *            to be altered
	 * @return war to be altered
	 */
	private static WebArchive alterAccordingToTomcat(WebArchive war) {

		// TODO
		return war;
	}
}
