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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;


/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 * 
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestOrderingListAttributes extends AbstractOrderingListTest {
    
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/withColumn.xhtml");
    }

    @Test
    public void testColumnClasses() {
        ATTRIBUTES.set(OrderingListAttributes.columnClasses, "some-class");
        for (int i=0; i<getOrderingList().getNumberOfColumns(); i++) {
            assertTrue(selenium.belongsClass(getOrderingList().getItemColumn(0, i), "some-class"), "The column <" + i + "> doesn't belong to set class <some-class>.");
        }
    }    
    
    @Test
    public void testDisabled() {
        ATTRIBUTES.set(OrderingListAttributes.disabled, "true");
        try {
            getOrderingList().getIndexOfSelectedItem();
            fail("The attribute <disabled> is set to true, but the ordering list is still enabled.");
        }
        catch(IllegalStateException e) {}
    }
    
    @Test
    public void testDisabledClass() {
        ATTRIBUTES.set(OrderingListAttributes.disabled, "true");
        ATTRIBUTES.set(OrderingListAttributes.disabledClass, "disabled-class");
        assertTrue(selenium.belongsClass(getOrderingList().getLocator(), "disabled-class"), "The disabled class is not set when the ordering list is disabled.");
    }
    
    @Test
    public void testHeaderClass() {
        ATTRIBUTES.set(OrderingListAttributes.headerClass, "some-class");
        assertTrue(selenium.belongsClass(getOrderingList().getHeader(), "some-class"), "The attribute <headerClass> is set to <some-class>, but the header doesn't belong to this class.");
    }
    
    @Test
    public void testItemClass() {
        ATTRIBUTES.set(OrderingListAttributes.itemClass, "some-class");
        assertTrue(selenium.belongsClass(getOrderingList().getItem(0), "some-class"), "The attribute <itemClass> is set to <some-class>, but the first item doesn't belong to this class.");
    }
    
    @Test
    public void testListHeight() {
        testSizeCssProperty(getOrderingList().getScrollableArea(), OrderingListAttributes.listHeight, CssProperty.HEIGHT);
    }

    @Test
    public void testListWidth() {
        testSizeCssProperty(getOrderingList().getScrollableArea(), OrderingListAttributes.listWidth, CssProperty.WIDTH);
    }    
    
    @Test
    public void testMaxListHeight() {
        ATTRIBUTES.set(OrderingListAttributes.listHeight, "");
        testSizeCssProperty(getOrderingList().getScrollableArea(), OrderingListAttributes.maxListHeight, new CssProperty("max-height"));
    }

    @Test
    public void testMinListHeight() {
        ATTRIBUTES.set(OrderingListAttributes.listHeight, "");
        testSizeCssProperty(getOrderingList().getScrollableArea(), OrderingListAttributes.minListHeight, new CssProperty("min-height"));
    }    
    
    @Test(enabled=false)
    public void testOnblur() {
        // TODO
    }
    
    @Test
    public void testOnchange() {
        testFireEvent(Event.CHANGE, getOrderingList().getLocator());
    }    
    
    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, getOrderingList().getLocator());
    }
    
    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, getOrderingList().getLocator());
    }
    
    @Test(enabled=false)
    public void testOnfocus() {
        // TODO
    }
    
    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, getOrderingList().getListArea());        
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, getOrderingList().getListArea());
    }    
    
    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, getOrderingList().getListArea());
    }
    
    @Test
    public void testOnlistclick() {
        testFireEvent(Event.CLICK, getOrderingList().getList(), "listclick");
    }
    
    @Test
    public void testOnlistdblclick() {
        testFireEvent(Event.DBLCLICK, getOrderingList().getList(), "listdblclick");
    }    

    @Test
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, getOrderingList().getList(), "listkeydown");
    }        
    
    @Test
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, getOrderingList().getList(), "listkeypress");
    }        
    
    @Test
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, getOrderingList().getList(), "listkeyup");
    }        
    
    @Test
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, getOrderingList().getList(), "listmousedown");
    }        
    
    @Test
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, getOrderingList().getList(), "listmousemove");
    }        
    
    @Test
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, getOrderingList().getList(), "listmouseout");
    }        
    
    @Test
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, getOrderingList().getList(), "listmouseover");
    }        
    
    @Test
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, getOrderingList().getList(), "listmouseup");
    }        
    
    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, getOrderingList().getLocator());
    }        

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, getOrderingList().getLocator());
    }            
    
    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, getOrderingList().getLocator());
    }            
    
    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, getOrderingList().getLocator());
    }            
    
    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, getOrderingList().getLocator());
    }            
    
    @Test
    public void testRendered() {
        ATTRIBUTES.set(OrderingListAttributes.rendered, false);
        assertFalse(getOrderingList().isOrderingListPresent(), "The attribute <rendered> is set to <false>, but it has no effect.");
    }
    
    @Test
    public void testSelectItemClass() {
        ATTRIBUTES.set(OrderingListAttributes.selectItemClass, "some-class");
        selectItem(0);
        assertTrue(selenium.belongsClass(getOrderingList().getItem(0), "some-class"), "The attribute <selectItemClass> is set to <some-class>, but it has no effect.");
    }
    
    @Test
    public void testStyle() {
        super.testStyle(getOrderingList().getLocator());
    }
    
    @Test
    public void testValueChangeListener() {
        getOrderingList().selectItem(0);
        getOrderingList().moveBottom();
        submit();
        assertTrue(selenium.isElementPresent(getPhaseListener(3)));
        assertTrue(selenium.getText(getPhaseListener(3)).contains("*3 value changed"));
    }
    
    private void testSizeCssProperty(JQueryLocator element, OrderingListAttributes attribute, CssProperty cssProperty) {
        Map<String, String> values = new HashMap<String, String>();
        values.put("100", "100px");
        values.put("200px", "200px");      
        for(String value : values.keySet()) {
            ATTRIBUTES.set(attribute, value);
            assertEquals(selenium.getStyle(getOrderingList().getScrollableArea(), cssProperty), values.get(value), "The attribute <" + attribute.name() +"> is set to <" + value + ">, but it has no effect.");
        }        
    }
}
