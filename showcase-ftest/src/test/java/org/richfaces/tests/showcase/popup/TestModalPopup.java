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

import java.io.IOException;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.panel.AbstractPanelTest;
import org.richfaces.tests.showcase.popup.page.PopupPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestModalPopup extends AbstractPanelTest {

    @Page
    private PopupPage page;

    @FindBy(css = "div[id$='popup_shade']")
    private WebElement hidePopup;

    protected final String BODY_OF_POPUP = "You can also check and trigger events if the use clicks outside of the panel.\n"
        + " In this example clicking outside closes the panel.";

    @Test
    public void testModalPopupPanel() throws IOException {
        page.callthePopupButton.click();
        assertTrue(page.popupPanelContent.isDisplayed(), "The poppup panel should be visible now!");
        checkContentOfPanel(page.popupPanelContent.getText(), BODY_OF_POPUP);
        hidePopup.click();
        assertFalse(page.popupPanelContent.isDisplayed(), "The poppup panel should not be visible now!");
    }

}
