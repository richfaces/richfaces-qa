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
package org.richfaces.tests.showcase.poll;

import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestPoll extends AbstractPoolTest {

	/* *******************************************************************************************************
	 * Locators
	 * ******************************************************************
	 * *************************************
	 */

	protected JQueryLocator serverDate = jq("span[id$=serverDate]");
	protected JQueryLocator button = jq("input[type=submit]");

	/* ********************************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ***********************************
	 */

	@Test
	public void testPooling() {

		List<Integer> deviations = new ArrayList<Integer>();

		TextRetriever dateRetriever = retrieveText.locator(serverDate);
		dateRetriever.initializeValue();

		Integer deviation = null;
		
		for( int i = 0; i < 5; i++ ) {
			
			deviation = waitForServerActionAndReturnDeviation(dateRetriever, "poll");
			
			deviations.add(deviation);
		}
		
		Collections.sort(deviations);
		assertFalse(deviations.get(2) > 3,
				"The median of deviations is bigger than 3");
	}	
}
