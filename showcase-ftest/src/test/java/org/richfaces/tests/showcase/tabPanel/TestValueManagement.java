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
package org.richfaces.tests.showcase.tabPanel;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestValueManagement extends AbstractAjocadoTest {

    /* **********************************************************************
     * Constants **********************************************************************
     */

    private final String BODY_OF_PANEL = "Here is tab #";
    private final String BODY_OF_PANEL_1 = BODY_OF_PANEL + 1;
    private final String BODY_OF_PANEL_2 = BODY_OF_PANEL + 2;

    /* **********************************************************************
     * Locators*********************************************************************
     */

    JQueryLocator previousTabButton = jq("a:contains('Previous')");
    JQueryLocator nextTabButton = jq("a:contains('Next')");
    JQueryLocator firstTabButton = jq("span:contains('First'):visible");
    JQueryLocator bodyOfPanel = jq("div.rf-tab div[id$=content]:visible");

    /* *********************************************************************
     * Tests **********************************************************************
     */

    @Test
    public void testExternalControlsForSwitchingThePanel() {
        guardXhr(selenium).click(firstTabButton);

        guardXhr(selenium).click(nextTabButton);

        String actualContent = selenium.getText(bodyOfPanel);

        assertEquals(actualContent, BODY_OF_PANEL_2, "The next button does not work!");

        guardXhr(selenium).click(previousTabButton);

        actualContent = selenium.getText(bodyOfPanel);

        assertEquals(actualContent, BODY_OF_PANEL_1, "The previous button does not work!");
    }
}
