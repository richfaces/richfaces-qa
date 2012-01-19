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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.attributes.Attributes;
import org.richfaces.tests.metamer.ftest.model.OrderingList;

/**
 * Abstract test case for pages faces/components/richOrderingList/
 * 
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractOrderingListTest extends AbstractAjocadoTest {

    private OrderingList orderingList = new OrderingList();
    
    private JQueryLocator requestTime = jq("span#requestTime");
    
    private JQueryLocator phaseListenerFormat = jq("div#phasesPanel li:eq({0})");
    
    private JQueryLocator submitButton = pjq("input[id$=submitButton]");
    
    protected static final Attributes<OrderingListAttributes> ATTRIBUTES = new Attributes<OrderingListAttributes>();
    
    protected void checkButtonsBottom() {
        assertFalse(orderingList.isButtonBottomEnabled(), "The button [bottom] should be disabled.");
        assertFalse(orderingList.isButtonDownEnabled(), "The button [down] should be disabled.");
        assertTrue(orderingList.isButtonTopEnabled(), "The button [top] should be enabled.");
        assertTrue(orderingList.isButtonUpEnabled(), "The button [up] should be enabled.");        
    }
    
    protected void checkButtonsMiddle() {
        assertTrue(orderingList.isButtonBottomEnabled(), "The button [bottom] should be enabled.");
        assertTrue(orderingList.isButtonDownEnabled(), "The button [down] should be enabled.");        
        assertTrue(orderingList.isButtonTopEnabled(), "The button [top] should be enabled.");
        assertTrue(orderingList.isButtonUpEnabled(), "The button [up] should be enabled.");        
    }
    
    protected void checkButtonsTop() {
        assertTrue(orderingList.isButtonBottomEnabled(), "The button [bottom] should be enabled.");
        assertTrue(orderingList.isButtonDownEnabled(), "The button [down] should be enabled.");
        assertFalse(orderingList.isButtonTopEnabled(), "The button [top] should be disabled.");
        assertFalse(orderingList.isButtonUpEnabled(), "The button [up] should be disabled.");        
    }    
    
    protected OrderingList getOrderingList() {
        return orderingList;
    }
    
    protected JQueryLocator getPhaseListener(int index) {
        return phaseListenerFormat.format(index);
    }
    
    protected void moveBottom() {
        int beforeIndex = orderingList.getIndexOfSelectedItem();
        String stateA = orderingList.getItemColumnValue(beforeIndex, 0);
        String cityA = orderingList.getItemColumnValue(beforeIndex, 1);
        String stateB = orderingList.getItemColumnValue(orderingList.getNumberOfItems() - 1, 0);
        String cityB = orderingList.getItemColumnValue(orderingList.getNumberOfItems() - 1, 1);             
        orderingList.moveBottom();
        int after = orderingList.getIndexOfSelectedItem();
        assertEquals(after, orderingList.getNumberOfItems() - 1, "The index of selected item doesn't match.");
        checkColumnValuesMoved(beforeIndex, stateA, cityA, orderingList.getNumberOfItems() - 1, stateB, cityB);        
        checkButtonsBottom();
    }    
    
    protected void moveDown() {
        int beforeIndex = orderingList.getIndexOfSelectedItem();
        String stateA = orderingList.getItemColumnValue(beforeIndex, 0);
        String cityA = orderingList.getItemColumnValue(beforeIndex, 1);
        String stateB = orderingList.getItemColumnValue(beforeIndex + 1, 0);
        String cityB = orderingList.getItemColumnValue(beforeIndex + 1, 1);             
        orderingList.moveDown();
        int afterIndex = orderingList.getIndexOfSelectedItem();   
        assertEquals(afterIndex, beforeIndex + 1, "The index of selected item doesn't match.");
        checkColumnValuesSwapped(beforeIndex, stateA, cityA, afterIndex, stateB, cityB);
        if (afterIndex < orderingList.getNumberOfItems() - 1) {
            checkButtonsMiddle();
        }
        else {
            checkButtonsBottom();
        }
    }

    protected void moveTop() {
        int beforeIndex = orderingList.getIndexOfSelectedItem();
        String stateA = orderingList.getItemColumnValue(beforeIndex, 0);
        String cityA = orderingList.getItemColumnValue(beforeIndex, 1);
        String stateB = orderingList.getItemColumnValue(0, 0);
        String cityB = orderingList.getItemColumnValue(0, 1);        
        orderingList.moveTop();
        int afterIndex = orderingList.getIndexOfSelectedItem();
        assertEquals(afterIndex, 0, "The index of selected item doesn't match.");
        checkColumnValuesMoved(beforeIndex, stateA, cityA, afterIndex, stateB, cityB);
        checkButtonsTop();
    }    
 
    protected void moveUp() {
        int beforeIndex = orderingList.getIndexOfSelectedItem();
        String stateA = orderingList.getItemColumnValue(beforeIndex, 0);
        String cityA = orderingList.getItemColumnValue(beforeIndex, 1);
        String stateB = orderingList.getItemColumnValue(beforeIndex - 1, 0);
        String cityB = orderingList.getItemColumnValue(beforeIndex - 1, 1);             
        orderingList.moveUp();
        int afterIndex = orderingList.getIndexOfSelectedItem();
        assertEquals(afterIndex, beforeIndex - 1, "The index of selected item doesn't match.");
        checkColumnValuesSwapped(beforeIndex, stateA, cityA, afterIndex, stateB, cityB);
        if (afterIndex > 0) {
            checkButtonsMiddle();
        }
        else {
            checkButtonsTop();
        }
    }       
    
    protected void selectItem(int index) {
        getOrderingList().selectItem(index);
        assertTrue(selenium.belongsClass(getOrderingList().getItem(index), "rf-ord-sel"), "After selecting an item, the item should belong to the class <rf-ord-sel>."); 
    }
    
    protected void submit() {
        selenium.click(submitButton);
        selenium.waitForPageToLoad();
    }
    
    private void checkColumnValuesSwapped(int indexA, String stateA, String cityA, int indexB, String stateB, String cityB) {
        assertEquals(orderingList.getItemColumnValue(indexA, 0), stateB, "The rows weren't swapped succesfully after moving.");
        assertEquals(orderingList.getItemColumnValue(indexA, 1), cityB, "The rows weren't swapped succesfully after moving.");
        assertEquals(orderingList.getItemColumnValue(indexB, 0), stateA, "The rows weren't swapped succesfully after moving.");
        assertEquals(orderingList.getItemColumnValue(indexB, 1), cityA, "The rows weren't swapped succesfully after moving.");        
    }
    
    private void checkColumnValuesMoved(int indexA, String stateA, String cityA, int indexB, String stateB, String cityB) {
        assertEquals(orderingList.getItemColumnValue(indexB + (int) Math.signum(indexA - indexB), 0), stateB, "The rows weren't moved succesfully after moving.");
        assertEquals(orderingList.getItemColumnValue(indexB + (int) Math.signum(indexA - indexB), 1), cityB, "The rows weren't moved succesfully after moving.");        
        assertEquals(orderingList.getItemColumnValue(indexB, 0), stateA, "The rows weren't moved succesfully after moving.");
        assertEquals(orderingList.getItemColumnValue(indexB, 1), cityA, "The rows weren't moved succesfully after moving.");        
    }    
    
}
