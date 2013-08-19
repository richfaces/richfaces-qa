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

import static org.richfaces.tests.page.fragments.impl.utils.Event.CLICK;
import static org.richfaces.tests.page.fragments.impl.utils.Event.DBLCLICK;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEDOWN;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEMOVE;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEOUT;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEOVER;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEUP;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.collapseEvent;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.expandEvent;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupMode;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.ondblclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmousedown;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmousemove;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmouseover;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.onmouseup;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuAttributes;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.panelMenuGroup.RichFacesPanelMenuGroup;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22749 $
 */
public class TestPanelMenuDOMEvents extends AbstractPanelMenuTest {

    @Inject
    Mode mode;

    @Inject
    @Use("events")
    Event event = DBLCLICK;
    Event[] events = new Event[] { CLICK, DBLCLICK, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenu/simple.xhtml");
    }

    @BeforeMethod
    public void setup() {
        if (mode != null) {
            panelMenuAttributes.set(groupMode, mode);
            page.setGroupMode(mode);
        }
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testExpandEvent() {
        panelMenuAttributes.set(expandEvent, event.getEventName());
        assertTrue(page.group2.isCollapsed());

        fireEvent(page.group2.getLabel(), event);
        Graphene.waitModel(driver).until(new GroupIsExpanded(page.group2));
    }

    @Test
    @Use(field = "mode", enumeration = true)
    public void testCollapseEvent() {
        panelMenuAttributes.set(collapseEvent, event.getEventName());

        page.group2.toggle();
        assertTrue(page.group2.isExpanded());

        fireEvent(page.group2.getLabel(), event);
        Graphene.waitModel().until(new GroupIsCollapsed(page.group2));
    }

    @Test
    @Use(field = "mode", enumeration = false)
    public void testOnClick() {
        Action click = new Actions(driver).click(page.panelMenu.getRoot()).build();
        testFireEvent(panelMenuAttributes, onclick, click);
    }

    @Test
    @Use(field = "mode", enumeration = false)
    public void testOnDblclick() {
        Action dblClick = new Actions(driver).doubleClick(page.panelMenu.getRoot()).build();
        testFireEvent(panelMenuAttributes, ondblclick, dblClick);
    }

    @Test
    @Use(field = "mode", enumeration = false)
    public void testOnMousedown() {
        Action mousedown = new Actions(driver).clickAndHold(page.panelMenu.getRoot()).build();
        testFireEvent(panelMenuAttributes, onmousedown, mousedown);
    }

    @Test
    @Use(field = "mode", enumeration = false)
    public void testOnMousemove() {
        Action mousemove = new Actions(driver).moveToElement(page.panelMenu.getRoot(), 3, 3).build();
        testFireEvent(panelMenuAttributes, onmousemove, mousemove);
    }

    @Test
    @Use(field = "mode", enumeration = false)
    public void testOnMouseout() {
        // TODO 2013-02-07 JJa: implement using WebDriver API (doesn't work for now)
        // Action mouseout = new Actions(driver).moveToElement(page.panelMenu.getRoot()).moveByOffset(-5, -5).build();
        // testFireEvent(panelMenuAttributes, onmouseout, mouseout);
        testFireEventWithJS(page.panelMenu.getRoot(), Event.MOUSEOUT, panelMenuAttributes, PanelMenuAttributes.onmouseout);
    }

    @Test
    @Use(field = "mode", enumeration = false)
    public void testOnMouseover() {
        Action mouseover = new Actions(driver).moveToElement(page.panelMenu.getRoot(), 3, 3).build();
        testFireEvent(panelMenuAttributes, onmouseover, mouseover);
    }

    @Test
    @Use(field = "mode", enumeration = false)
    public void testOnMouseup() {
        Action mouseup = new Actions(driver).clickAndHold(page.panelMenu.getRoot()).release().build();
        testFireEvent(panelMenuAttributes, onmouseup, mouseup);
    }

    private class GroupIsExpanded implements Predicate<WebDriver> {
        private RichFacesPanelMenuGroup group;

        public GroupIsExpanded(RichFacesPanelMenuGroup element) {
            this.group = element;
        }

        @Override
        public boolean apply(WebDriver browser) {
            Boolean present = Graphene.element(group.getRoot()).isPresent().apply(driver);
            return present && group.isExpanded();
        }
    }

    private class GroupIsCollapsed implements Predicate<WebDriver> {
        private RichFacesPanelMenuGroup group;

        public GroupIsCollapsed(RichFacesPanelMenuGroup element) {
            group = element;
        }

        public boolean apply(WebDriver driver) {
            Boolean present = Graphene.element(group.getRoot()).isPresent().apply(driver);
            return present && group.isCollapsed();
        }
    };
}
