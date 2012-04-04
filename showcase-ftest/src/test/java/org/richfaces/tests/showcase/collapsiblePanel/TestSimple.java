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
package org.richfaces.tests.showcase.collapsiblePanel;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.testng.Assert.fail;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;

import org.richfaces.tests.showcase.panel.AbstractPanelTest;
import org.testng.annotations.Test;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimple extends AbstractPanelTest {

    /* *************************************************************************************
     * Locators*************************************************************************************
     */

    JQueryLocator firstCollapsiblePanelControl = jq("div[class*=rf-cp-hdr]:eq(0)");
    JQueryLocator secondCollapsiblePanelControl = jq("div[class*=rf-cp-hdr]:eq(1)");
    String bodyOfTheFirstPanel = "div.rf-cp-b:eq(0):visible";
    String bodyOfTheSecondPanel = "div.rf-cp-b:eq(1)";

    /* **************************************************************************************
     * Tests***************************************************************************************
     */

    @Test
    public void testCollapsAndShowPanels() {

        JQueryLocator bodyOfTheFirstPanelLoc = jq(bodyOfTheFirstPanel);
        JQueryLocator bodyOfTheSecondPanelLoc = jq(bodyOfTheSecondPanel);

        if (!selenium.isElementPresent(bodyOfTheFirstPanelLoc)) {

            guardNoRequest(selenium).click(firstCollapsiblePanelControl);
        }

        checkContentOfPanel(bodyOfTheFirstPanel, RICH_FACES_INFO);

        guardNoRequest(selenium).click(firstCollapsiblePanelControl);

        if (selenium.isElementPresent(bodyOfTheFirstPanelLoc)) {

            fail("The content of the first panel should not be visible, since the panel is collapsed!");
        }

        if (!selenium.isElementPresent(bodyOfTheSecondPanelLoc)) {

            guardXhr(selenium).click(secondCollapsiblePanelControl);
        }

        checkContentOfPanel(bodyOfTheSecondPanel, RICH_FACES_JSF_INFO);

        guardXhr(selenium).click(secondCollapsiblePanelControl);

        if (selenium.isElementPresent(bodyOfTheSecondPanelLoc)) {

            fail("The content of the second panel should not be visible, since the panel is collapsed!");
        }

    }
}
