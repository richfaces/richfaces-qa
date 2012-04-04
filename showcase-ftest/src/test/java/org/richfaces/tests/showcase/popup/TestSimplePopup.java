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
package org.richfaces.tests.showcase.popup;

import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimplePopup extends AbstractPoppupPanel {

    /* *************************************************************************************
     * Constants*************************************************************************************
     */
    protected final String BODY_OF_THE_POPPUP = "Any content might be inside this panel.\n"
        + " The popup panel is open and closed from the javascript function of component client side object. "
        + "The following code hide this panel: #{rich:component('popup')}.hide()";

    /* *************************************************************************************
     * Locators*************************************************************************************
     */

    JQueryLocator poppupPanelHideAnchor = jq("div.rf-pp-cnt:visible a");

    /* ***************************************************************************************
     * Tests***************************************************************************************
     */

    @Test
    public void testCallTheNonModalPopupAndHideIt() {

        guardNoRequest(selenium).click(callthePoppupButton);

        assertTrue(selenium.isElementPresent(poppupPanelContent), "The panel should be visible!");

        checkContentOfPanel(poppupPanelContent, BODY_OF_THE_POPPUP);

        guardNoRequest(selenium).click(anchorOfSource);

        assertTrue(selenium.isElementPresent(sourceOfPage), "The source of the page should be visible, since "
            + "the poppup panel is not modal, and therefore clicking on the source should show the source");

        assertTrue(selenium.isElementPresent(poppupPanelContent),
            "The panel should not disappear when clicking somewhere else!");

        guardNoRequest(selenium).click(poppupPanelHideAnchor);

        assertFalse(selenium.isElementPresent(poppupPanelContent),
            "The poppup panel should not be visible, since there was a click " + "on the hide anchor!");
    }

}
