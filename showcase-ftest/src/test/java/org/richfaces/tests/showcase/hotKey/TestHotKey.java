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
package org.richfaces.tests.showcase.hotKey;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.testng.Assert.assertTrue;

import java.awt.event.KeyEvent;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.pickList.AbstractPickListTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 */
public class TestHotKey extends AbstractPickListTest {

    @Test
    public void testTargetHasToBeFocusedToProccessHotKey() {
        String[] availableCities = selenium.getText(sourceItemsSimple).split("\n");

        JQueryLocator oneCity = jq(sourceItemsSimple.format(0).getRawLocator() + " " + optionToPick + ":eq(0)");

        selenium.focus(oneCity);
        selenium.click(oneCity);

        selenium.keyPressNative(KeyEvent.VK_END);

        checkThatListContains(availableCities, targetItemsSimple);
    }

    @Test
    public void testHotKeyIsNotProccessedWhenTargetIsNotFocused() {
        selenium.keyPressNative(KeyEvent.VK_END);

        String citiesFromTargetItemsSimple = selenium.getText(targetItemsSimple).trim();

        System.out.println(citiesFromTargetItemsSimple);

        assertTrue(citiesFromTargetItemsSimple.length() == 0,
            "There should be no cities, since the pick list was not focused while hoKey was invoked");
    }

    @Test
    public void testAddHotkey() {
        checkAddButton(sourceItemsSimple, addButton, targetItemsSimple, true);
    }

    @Test
    public void testRemoveHotKey() {
        super.testRemoveButton(true);
    }

    @Test
    public void testAddAllHotKey() {
        super.testAddAllButtonSimplePickList(true);
    }

    @Test
    public void testRemoveAllHotKey() {
        super.testRemoveAllButtonSimplePickList(true);
    }

    @Test
    public void testAddAllButtonSimplePickList() {
        super.testAddAllButtonSimplePickList();
    }

    @Test
    public void testRemoveAllButtonSimplePickList() {
        super.testRemoveAllButtonSimplePickList();
    }

    @Test
    public void testAddButtonSimplePickList() {
        super.testAddButtonSimplePickList();
    }

    @Test
    public void testFirstButtonOrdering() {
        super.testFirstButtonOrdering();
    }

    @Test
    public void testUpButtonOrdering() {
        super.testUpButtonOrdering();
    }

    @Test
    public void testDownButtonOrdering() {
        super.testDownButtonOrdering();
    }

    @Test
    public void testLastButtonOrdering() {
        super.testLastButtonOrdering();
    }
}
