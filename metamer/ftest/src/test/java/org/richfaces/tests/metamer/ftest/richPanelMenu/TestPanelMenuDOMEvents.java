/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.richfaces.fragment.common.Event.CLICK;
import static org.richfaces.fragment.common.Event.DBLCLICK;
import static org.richfaces.fragment.common.Event.MOUSEOVER;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.collapseEvent;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.expandEvent;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.ondblclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmousedown;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmousemove;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmouseover;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmouseup;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @since 4.3.4
 */
public class TestPanelMenuDOMEvents extends AbstractPanelMenuTest {

    private final Attributes<PanelMenuAttributes> panelMenuAttributes = getAttributes();

    private Event event = DBLCLICK;

    private final Event[] events = new Event[]{ CLICK, DBLCLICK, MOUSEOVER };

    @Test
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "events")
    public void testExpandEvent() {
        panelMenuAttributes.set(expandEvent, event.getEventName());
        assertFalse(page.getGroup2().advanced().isExpanded());

        fireEvent(page.getGroup2().advanced().getLabelElement(), event);
        page.getGroup2().advanced().waitUntilMenuGroupExpanded(page.getGroup2().advanced().getHeaderElement());
    }

    @Test
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "events")
    public void testCollapseEvent() {
        panelMenuAttributes.set(collapseEvent, event.getEventName());

        page.getPanelMenu().expandGroup(1);
        assertTrue(page.getGroup2().advanced().isExpanded());

        fireEvent(page.getGroup2().advanced().getLabelElement(), event);
        page.getGroup2().advanced().waitUntilMenuGroupExpanded(page.getGroup2().advanced().getHeaderElement());
    }

    @Test
    @Templates("plain")
    public void testOnclick() {
        Action click = new Actions(driver).click(page.getPanelMenu().advanced().getRootElement()).build();
        testFireEvent(panelMenuAttributes, onclick, click);
    }

    @Test
    @Templates("plain")
    public void testOndblclick() {
        Action dblClick = new Actions(driver).doubleClick(page.getPanelMenu().advanced().getRootElement()).build();
        testFireEvent(panelMenuAttributes, ondblclick, dblClick);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmousedown() {
        Action mousedown = new Actions(driver).clickAndHold(page.getPanelMenu().advanced().getRootElement()).build();
        testFireEvent(panelMenuAttributes, onmousedown, mousedown);
        new Actions(driver).release().perform();
    }

    @Test
    @Templates("plain")
    public void testOnmousemove() {
        Action mousemove = new Actions(driver).moveToElement(page.getPanelMenu().advanced().getRootElement(), 3, 3).build();
        testFireEvent(panelMenuAttributes, onmousemove, mousemove);
    }

    @Test
    @Templates("plain")
    public void testOnmouseout() {
        testFireEventWithJS(page.getPanelMenu().advanced().getRootElement(),panelMenuAttributes, PanelMenuAttributes.onmouseout);
    }

    @Test(priority=1)
    @Templates("plain")
    public void testOnmouseover() {
        //This test should be done first, because use static x and y variables
        Action mouseover = new Actions(driver).moveToElement(page.getPanelMenu().advanced().getRootElement(), 3, 3).build();
        testFireEvent(panelMenuAttributes, onmouseover, mouseover);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseup() {
        Action mouseup = new Actions(driver).click(page.getPanelMenu().advanced().getRootElement()).build();
        testFireEvent(panelMenuAttributes, onmouseup, mouseup);
    }
}
