/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import org.jboss.arquillian.graphene.Graphene;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.panel.AbstractPanelTest;
import org.richfaces.tests.showcase.popup.page.LoginPage;
import org.richfaces.tests.showcase.popup.page.PopupPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestLogin extends AbstractPanelTest {

    @Page
    private LoginPage page;

    @Page
    private PopupPage popupPage;

    @Test
    public void testLoginPoppup() {
        checkPopupPanel(page.loginOnToolbar, page.loginOnPopup);
    }

    @Test
    public void testSearchPoppup() {
        checkPopupPanel(page.searchOnToolbar, page.searchOnPopup);
    }

    /**
     * Call the poppup panel, and then hides it, check for presence
     *
     * @param callPopupButton
     *            the button by which the poppup is called
     * @param closingPopupButton
     *            the button by which the poppup is closed
     */
    private void checkPopupPanel(WebElement callPopupButton, WebElement closingPopupButton) {
        Graphene.waitGui(webDriver).until().element(callPopupButton).is().visible();
        callPopupButton.click();
        Graphene.waitAjax(webDriver).until().element(popupPage.popupPanelContent).is().visible();
        assertTrue(popupPage.popupPanelContent.isDisplayed(), "The popup panel should be visible!");
        closingPopupButton.click();
        assertFalse(popupPage.popupPanelContent.isDisplayed(), "The popup panel should not be visible!");
    }
}
