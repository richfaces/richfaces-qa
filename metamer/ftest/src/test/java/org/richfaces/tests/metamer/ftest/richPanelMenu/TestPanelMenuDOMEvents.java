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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.collapseEvent;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.expandEvent;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.ondblclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmousedown;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmousemove;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmouseout;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmouseover;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmouseup;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuAttributes;
import static org.richfaces.tests.page.fragments.impl.utils.Event.CLICK;
import static org.richfaces.tests.page.fragments.impl.utils.Event.DBLCLICK;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEDOWN;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEMOVE;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEOUT;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEOVER;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEUP;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.panelMenu.RichFacesPanelMenuGroup;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22749 $
 */
public class TestPanelMenuDOMEvents extends AbstractPanelMenuTest {

    @Inject
    @Use(empty = true)
    Event event = DBLCLICK;

    Event[] events = new Event[] { CLICK, DBLCLICK, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP };

    @Test
    @Use(field = "event", value = "events")
    public void testExpandEvent() {
        panelMenuAttributes.set(expandEvent, event.getEventName());
        assertFalse(page.getGroup2().advanced().isExpanded());

        fireEvent(page.getGroup2().advanced().getLabelElement(), event);
        Graphene.waitModel(driver).until(new GroupIsExpanded(page.getGroup2()));
    }

    @Test
    @Use(field = "event", value = "events")
    public void testCollapseEvent() {
        panelMenuAttributes.set(collapseEvent, event.getEventName());

        page.getPanelMenu().expandGroup(1);
        assertTrue(page.getGroup2().advanced().isExpanded());

        fireEvent(page.getGroup2().advanced().getLabelElement(), event);
        Graphene.waitModel().until(new GroupIsCollapsed(page.getGroup2()));
    }

    @Test
    public void testOnClick() {
        Action click = new Actions(driver).click(page.getPanelMenu().advanced().getRootElement()).build();
        testFireEvent(panelMenuAttributes, onclick, click);
    }

    @Test
    public void testOnDblclick() {
        Action dblClick = new Actions(driver).doubleClick(page.getPanelMenu().advanced().getRootElement()).build();
        testFireEvent(panelMenuAttributes, ondblclick, dblClick);
    }

    @Test
    public void testOnMousedown() {
        Action mousedown = new Actions(driver).clickAndHold(page.getPanelMenu().advanced().getRootElement()).build();
        testFireEvent(panelMenuAttributes, onmousedown, mousedown);
        new Actions(driver).release().build();
    }

    @Test
    public void testOnMousemove() {
        Action mousemove = new Actions(driver).moveToElement(page.getPanelMenu().advanced().getRootElement(), 3, 3).build();
        testFireEvent(panelMenuAttributes, onmousemove, mousemove);
    }

    @Test
    public void testOnMouseout() {
        Action mouseout = new Actions(driver).moveToElement(page.getPanelMenu().advanced().getRootElement())
            .moveByOffset(-5, -5).build();
        testFireEvent(panelMenuAttributes, onmouseout, mouseout);
    }

    @Test
    public void testOnMouseover() {
        Action mouseover = new Actions(driver).moveToElement(page.getPanelMenu().advanced().getRootElement(), 3, 3).build();
        testFireEvent(panelMenuAttributes, onmouseover, mouseover);
    }

    @Test
    public void testOnMouseup() {
        Action mouseup = new Actions(driver).click(page.getPanelMenu().advanced().getRootElement()).build();
        testFireEvent(panelMenuAttributes, onmouseup, mouseup);
    }

    private class GroupIsExpanded implements Predicate<WebDriver> {
        private RichFacesPanelMenuGroup group;

        public GroupIsExpanded(RichFacesPanelMenuGroup element) {
            this.group = element;
        }

        @Override
        public boolean apply(WebDriver browser) {
            Boolean present = new WebElementConditionFactory(group.advanced().getRootElement()).isPresent().apply(driver);
            return present && group.advanced().isExpanded();
        }
    }

    private class GroupIsCollapsed implements Predicate<WebDriver> {
        private RichFacesPanelMenuGroup group;

        public GroupIsCollapsed(RichFacesPanelMenuGroup element) {
            group = element;
        }

        public boolean apply(WebDriver driver) {
            Boolean present = new WebElementConditionFactory(group.advanced().getRootElement()).isPresent().apply(driver);
            return present && !group.advanced().isExpanded();
        }
    };
}
