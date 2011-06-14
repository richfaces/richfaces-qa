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
package org.richfaces.tests.showcase.accordion;

import static org.testng.Assert.assertFalse;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.panel.AbstractPanelTest;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */

public class TestSimple extends AbstractPanelTest {

	/* *************************************************************************************
	 * Locators
	 * ******************************************************************
	 * *******************
	 */

	protected JQueryLocator firstAccordionPanelControl = jq("div.rf-ac-itm div[class*=rf-ac-itm-hdr]:eq(0)");
	protected JQueryLocator secondAccordionPanelControl = jq("div.rf-ac-itm div[class*=rf-ac-itm-hdr]:eq(1)");
	protected String bodyOfTheFirstPanel = "div.rf-ac-itm-cnt:eq(0)";
	protected String bodyOfTheSecondPanel = "div.rf-ac-itm-cnt:eq(1)";

	/* ***************************************************************************************
	 * Tests
	 * *********************************************************************
	 * ******************
	 */

	@Test
	public void testAccordionAndContent() {

		JQueryLocator bodyOfTheFirstPanelLoc = jq(bodyOfTheFirstPanel);
		JQueryLocator bodyOfTheSecondPanelLoc = jq(bodyOfTheSecondPanel);

		if (!selenium.isVisible(bodyOfTheFirstPanelLoc)) {

			guardNoRequest(selenium).click(firstAccordionPanelControl);
		}

		checkContentOfPanel(bodyOfTheFirstPanel, RICH_FACES_INFO);

		guardNoRequest(selenium).click(secondAccordionPanelControl);

		assertFalse(selenium.isVisible(bodyOfTheFirstPanelLoc),
				"The body of the first panel should not be visible, since "
						+ "the panel is hidden!");

		checkContentOfPanel(bodyOfTheSecondPanel, RICH_FACES_JSF_INFO);

		guardNoRequest(selenium).click(firstAccordionPanelControl);

		assertFalse(selenium.isVisible(bodyOfTheSecondPanelLoc),
				"The body of the second panel should not be visible, since "
						+ "the panel is hidden!");

	}

}
