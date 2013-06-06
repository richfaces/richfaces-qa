/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richToggleControl;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.toggleControlAttributes;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.toggleControl.RichFacesToggleControl;

/**
 * Abstract test case for rich:toggleControl.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21115 $
 */
public abstract class AbstractTestToggleControl extends AbstractWebDriverTest {

    @Page
    ToggleControlPage page;

    private RichFacesToggleControl[] firstPanelBtns;

    private RichFacesToggleControl[] secondPanelBtns;

    public void testSwitchFirstPanel(WebElement[] items) {
        testSwitching(getFirstPanelButtons(), items);
    }

    public void testSwitchSecondPanel(WebElement[] items) {
        testSwitching(getSecondPanelButtons(), items);
    }

    public void testTargetItem(WebElement[] items) {
        toggleControlAttributes.set(ToggleControlAttributes.targetItem, "item2");

        Graphene.guardAjax(page.tcPanel1Item3).toggle();
        Graphene.waitGui().until().element(items[2]).is().visible();
        assertFalse(items[0].isDisplayed(), "Item 1 should not be visible.");
        assertFalse(items[1].isDisplayed(), "Item 2 should not be visible.");

        Graphene.guardAjax(page.tcCustom).toggle();
        Graphene.waitGui().until().element(items[1]).is().visible();
        assertFalse(items[0].isDisplayed(), "Item 1 should not be visible.");
        assertFalse(items[2].isDisplayed(), "Item 3 should not be visible.");
    }

    public void testTargetPanel(WebElement[] items) {
        toggleControlAttributes.set(ToggleControlAttributes.targetPanel, "panel2");

        Graphene.guardAjax(page.tcPanel2Item3).toggle();
        Graphene.waitGui().until().element(items[2]).is().visible();
        assertFalse(items[0].isDisplayed(), "Item 1 should not be visible.");
        assertFalse(items[1].isDisplayed(), "Item 2 should not be visible.");

        Graphene.guardAjax(page.tcCustom).toggle();
        // waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(items[0]));
        Graphene.waitGui().until().element(items[0]).is().visible();
        assertFalse(items[1].isDisplayed(), "Item 2 should not be visible.");
        assertFalse(items[2].isDisplayed(), "Item 3 should not be visible.");
    }

    private void testSwitching(RichFacesToggleControl[] buttons, WebElement[] items) {
        Graphene.guardAjax(buttons[2]).toggle();
        Graphene.waitGui().until().element(items[2]).is().visible();
        assertFalse(items[0].isDisplayed(), "Item 1 should not be visible.");
        assertFalse(items[1].isDisplayed(), "Item 2 should not be visible.");

        Graphene.guardAjax(buttons[1]).toggle();
        Graphene.waitGui().until().element(items[1]).is().visible();
        assertFalse(items[0].isDisplayed(), "Item 1 should not be visible.");
        assertFalse(items[2].isDisplayed(), "Item 3 should not be visible.");

        Graphene.guardAjax(buttons[0]).toggle();
        Graphene.waitGui().until().element(items[0]).is().visible();
        assertFalse(items[1].isDisplayed(), "Item 2 should not be visible.");
        assertFalse(items[2].isDisplayed(), "Item 3 should not be visible.");
    }

    public RichFacesToggleControl[] getFirstPanelButtons() {
        if ( firstPanelBtns == null ) {
            firstPanelBtns = new RichFacesToggleControl[] { page.tcPanel1Item1, page.tcPanel1Item2, page.tcPanel1Item3 };
        }
        return firstPanelBtns;
    }

    public RichFacesToggleControl[] getSecondPanelButtons() {
        if ( secondPanelBtns == null ) {
            secondPanelBtns = new RichFacesToggleControl[] { page.tcPanel2Item1, page.tcPanel2Item2, page.tcPanel2Item3 };
        }
        return secondPanelBtns;
    }
}
