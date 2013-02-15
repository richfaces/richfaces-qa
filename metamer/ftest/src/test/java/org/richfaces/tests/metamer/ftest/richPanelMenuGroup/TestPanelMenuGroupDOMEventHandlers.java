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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOUT;
import static org.richfaces.PanelMenuMode.client;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.mode;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.ondblclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onmousedown;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onmousemove;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onmouseover;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.onmouseup;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuGroupAttributes;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22750 $
 */
public class TestPanelMenuGroupDOMEventHandlers extends AbstractPanelMenuGroupTest {

    @Test
    public void testOnClick() {
        panelMenuGroupAttributes.set(mode, client);

        Action click = new Actions(driver).click(page.topGroup.getRoot()).build();
        testFireEvent(panelMenuGroupAttributes, onclick, click);
    }

    @Test
    public void testOnDblClick() {
        panelMenuGroupAttributes.set(mode, client);
        Action dblClick = new Actions(driver).doubleClick(page.topGroup.getRoot()).build();
        testFireEvent(panelMenuGroupAttributes, ondblclick, dblClick);
    }

    @Test
    public void testOnMousedown() {
        panelMenuGroupAttributes.set(mode, client);
        Action mousedown = new Actions(driver).clickAndHold(page.topGroup.getRoot()).build();
        testFireEvent(panelMenuGroupAttributes, onmousedown, mousedown);
    }

    @Test
    public void testOnMousemove() {
        panelMenuGroupAttributes.set(mode, client);
        Action mousemove = new Actions(driver).moveToElement(page.topGroup.getRoot(), 3, 3).build();
        testFireEvent(panelMenuGroupAttributes, onmousemove, mousemove);
    }

    @Test
    public void testOnMouseout() {
        panelMenuGroupAttributes.set(mode, client);
        // TODO JJa 2013-02-13: Rewrite using webdriver api when fixed (not working now)
        testFireEventWithJS(page.topGroup.getRoot(), MOUSEOUT, panelMenuGroupAttributes, PanelMenuGroupAttributes.onmouseout);
    }

    @Test
    public void testOnMouseover() {
        panelMenuGroupAttributes.set(mode, client);
        Action mouseover = new Actions(driver).moveToElement(page.topGroup.getRoot(), 3, 3).build();
        testFireEvent(panelMenuGroupAttributes, onmouseover, mouseover);
    }

    @Test
    public void testOnMouseup() {
        panelMenuGroupAttributes.set(mode, client);
        Action mouseup = new Actions(driver).clickAndHold(page.topGroup.getRoot()).release().build();
        testFireEvent(panelMenuGroupAttributes, onmouseup, mouseup);
    }

}
