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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.jboss.arquillian.ajocado.Ajocado.waitModel;

import static org.jboss.arquillian.ajocado.dom.Event.CLICK;
import static org.jboss.arquillian.ajocado.dom.Event.DBLCLICK;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEMOVE;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOUT;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOVER;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEUP;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.panelMenuAttributes;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.collapseEvent;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.expandEvent;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupMode;

import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumWaiting;
import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22749 $
 */
public class TestPanelMenuDOMEvents extends AbstractPanelMenuTest {

    @Inject
    PanelMenuMode mode;

    @Inject
    @Use("events")
    Event event = DBLCLICK;
    Event[] events = new Event[] { CLICK, DBLCLICK, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP };

    SeleniumWaiting waitToggle;

    SeleniumCondition isGroup2Expanded = new SeleniumCondition() {
        public boolean isTrue() {
            return selenium.isElementPresent(group2) && group2.isExpanded();
        }
    };

    SeleniumCondition isGroup2Collapsed = new SeleniumCondition() {
        public boolean isTrue() {
            return selenium.isElementPresent(group2) && group2.isCollapsed();
        }
    };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenu/simple.xhtml");
    }

    @BeforeMethod
    public void setup() {
        waitToggle = waitModel.timeout(5000).interval(500);
        if (mode != null) {
            panelMenuAttributes.set(groupMode, mode);
            menu.setGroupMode(mode);
        }
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testExpandEvent() {
        panelMenuAttributes.set(expandEvent, event.getEventName());
        assertTrue(group2.isCollapsed());

        fireEventNatively(group2.getLabel(), event);
        waitToggle.until(isGroup2Expanded);
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testCollapseEvent() {
        panelMenuAttributes.set(collapseEvent, event.getEventName());

        group2.toggle();
        assertTrue(group2.isExpanded());

        fireEventNatively(group2.getLabel(), event);
        waitToggle.until(isGroup2Collapsed);
    }

    @Test
    @Use(field = "mode", empty = true)
    public void testDOMEvent() {
        super.testFireEvent(event, menu);
    }
}
