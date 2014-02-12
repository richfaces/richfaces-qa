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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.mode;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.ondblclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onmousedown;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onmousemove;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onmouseover;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onmouseup;
import static org.richfaces.ui.common.Mode.client;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
@Templates("plain")
public class TestPanelMenuGroupDOMEventHandlers extends AbstractPanelMenuGroupTest {

    private final Attributes<PanelMenuGroupAttributes> panelMenuGroupAttributes = getAttributes();

    @Test
    public void testOnclick() {
        panelMenuGroupAttributes.set(mode, client);

        Action click = new Actions(driver).click(page.getTopGroup().advanced().getHeaderElement()).build();
        testFireEvent(panelMenuGroupAttributes, onclick, click);
    }

    @Test
    public void testOndblclick() {
        panelMenuGroupAttributes.set(mode, client);
        Action dblClick = new Actions(driver).doubleClick(page.getTopGroup().advanced().getHeaderElement()).build();
        testFireEvent(panelMenuGroupAttributes, ondblclick, dblClick);
    }

    @Test
    public void testOnmousedown() {
        panelMenuGroupAttributes.set(mode, client);
        Action mousedown = new Actions(driver).clickAndHold(page.getTopGroup().advanced().getHeaderElement()).build();
        testFireEvent(panelMenuGroupAttributes, onmousedown, mousedown);
        new Actions(driver).release(page.getTopGroup().advanced().getHeaderElement()).perform();
    }

    @Test
    public void testOnmousemove() {
        panelMenuGroupAttributes.set(mode, client);
        Action mousemove = new Actions(driver).moveToElement(page.getTopGroup().advanced().getHeaderElement(), 3, 3).build();
        testFireEvent(panelMenuGroupAttributes, onmousemove, mousemove);
    }

    @Test
    public void testOnmouseout() {
        panelMenuGroupAttributes.set(mode, client);
        // TODO JJa 2013-02-13: Rewrite using webdriver api when fixed (not working now)
        testFireEventWithJS(page.getTopGroup().advanced().getHeaderElement(), Event.MOUSEOUT, panelMenuGroupAttributes, PanelMenuGroupAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        panelMenuGroupAttributes.set(mode, client);
        Action mouseover = new Actions(driver).moveToElement(page.getTopGroup().advanced().getHeaderElement(), 3, 3).build();
        testFireEvent(panelMenuGroupAttributes, onmouseover, mouseover);
    }

    @Test
    public void testOnmouseup() {
        panelMenuGroupAttributes.set(mode, client);
        Action mouseup = new Actions(driver).clickAndHold(page.getTopGroup().advanced().getHeaderElement()).release().build();
        testFireEvent(panelMenuGroupAttributes, onmouseup, mouseup);
    }

}
