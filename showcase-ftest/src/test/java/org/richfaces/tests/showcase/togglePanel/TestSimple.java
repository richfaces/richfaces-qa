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
package org.richfaces.tests.showcase.togglePanel;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimple extends AbstractAjocadoTest {

    /* ****************************************************************************
     * Constants****************************************************************************
     */
    protected final String CONTENT_OF_TAB1 = "For now you are at Panel 1";
    protected final String CONTENT_OF_TAB2 = "For now you are at Panel 2";

    /* ****************************************************************************
     * Locators****************************************************************************
     */

    JQueryLocator toogleTab1 = jq("div[id$=tabs] > div:eq(0)");
    JQueryLocator toogleTab2 = jq("div[id$=tabs] > div:eq(1)");
    JQueryLocator bodyOfPanel = jq("div.rf-tgp-itm:visible");

    /* ****************************************************************************
     * Tests****************************************************************************
     */

    @Test
    public void testTooglePanelItem1() {

        checkToogleTab(toogleTab1, CONTENT_OF_TAB1);
    }

    @Test
    public void testTooglePanelItem2() {

        checkToogleTab(toogleTab2, CONTENT_OF_TAB2);
    }

    /* ********************************************************************************
     * Help methods********************************************************************************
     */

    private void checkToogleTab(JQueryLocator button, String expectedContent) {

        guardXhr(selenium).click(button);

        String actualContent = selenium.getText(bodyOfPanel);

        assertTrue(actualContent.contains(expectedContent), "The content of " + button.getRawLocator()
            + " is diferent!");
    }
}
