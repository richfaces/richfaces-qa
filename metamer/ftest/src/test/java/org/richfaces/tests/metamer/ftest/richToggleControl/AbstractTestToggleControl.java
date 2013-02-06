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

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.toggleControlAttributes;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;

/**
 * Abstract test case for rich:toggleControl.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21115 $
 */
public abstract class AbstractTestToggleControl extends AbstractGrapheneTest {

    private JQueryLocator[] buttons1 = { pjq("input[id$=tc11]"), pjq("input[id$=tc12]"), pjq("input[id$=tc13]") };
    private JQueryLocator[] buttons2 = { pjq("input[id$=tc21]"), pjq("input[id$=tc22]"), pjq("input[id$=tc23]") };
    private JQueryLocator customButton = pjq("input[id$=tcCustom]");

    public void testSwitchFirstPanel(JQueryLocator[] items) {
        testSwitching(buttons1, items);
    }

    public void testSwitchSecondPanel(JQueryLocator[] items) {
        testSwitching(buttons2, items);
    }

    public void testTargetItem(JQueryLocator[] items) {
        toggleControlAttributes.set(ToggleControlAttributes.targetItem, "item2");

        guardXhr(selenium).click(buttons1[2]);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(items[2]));
        assertFalse(selenium.isVisible(items[0]), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(items[1]), "Item 2 should not be visible.");

        guardXhr(selenium).click(customButton);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(items[1]));
        assertFalse(selenium.isVisible(items[0]), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(items[2]), "Item 3 should not be visible.");
    }

    public void testTargetPanel(JQueryLocator[] items) {
        toggleControlAttributes.set(ToggleControlAttributes.targetPanel, "panel2");

        guardXhr(selenium).click(buttons2[2]);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(items[2]));
        assertFalse(selenium.isVisible(items[0]), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(items[1]), "Item 2 should not be visible.");

        guardXhr(selenium).click(customButton);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(items[0]));
        assertFalse(selenium.isVisible(items[1]), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(items[2]), "Item 3 should not be visible.");
    }

    private void testSwitching(JQueryLocator[] buttons, JQueryLocator[] items) {
        guardXhr(selenium).click(buttons[2]);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(items[2]));
        assertFalse(selenium.isVisible(items[0]), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(items[1]), "Item 2 should not be visible.");

        guardXhr(selenium).click(buttons[1]);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(items[1]));
        assertFalse(selenium.isVisible(items[0]), "Item 1 should not be visible.");
        assertFalse(selenium.isVisible(items[2]), "Item 3 should not be visible.");

        guardXhr(selenium).click(buttons[0]);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(items[0]));
        assertFalse(selenium.isVisible(items[1]), "Item 2 should not be visible.");
        assertFalse(selenium.isVisible(items[2]), "Item 3 should not be visible.");
    }
}
