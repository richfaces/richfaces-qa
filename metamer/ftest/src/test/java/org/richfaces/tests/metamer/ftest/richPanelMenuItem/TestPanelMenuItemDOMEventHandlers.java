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
package org.richfaces.tests.metamer.ftest.richPanelMenuItem;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.PanelMenuMode.client;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.mode;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.onclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.ondblclick;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.onmousedown;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.onmousemove;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.onmouseover;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.onmouseup;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
@Templates("plain")
public class TestPanelMenuItemDOMEventHandlers extends AbstractWebDriverTest {

    private final Attributes<PanelMenuItemAttributes> panelMenuItemAttributes = getAttributes();

    @Page
    private PanelMenuItemPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuItem/simple.xhtml");
    }

    @Test
    public void testOnclick() {
        panelMenuItemAttributes.set(mode, client);

        Action click = new Actions(driver).click(page.getItem().advanced().getRootElement()).build();
        testFireEvent(panelMenuItemAttributes, onclick, click);
    }

    @Test
    @Templates("plain")
    public void testOndblclick() {
        panelMenuItemAttributes.set(mode, client);
        Action dblClick = new Actions(driver).doubleClick(page.getItem().advanced().getRootElement()).build();
        testFireEvent(panelMenuItemAttributes, ondblclick, dblClick);
    }

    @Test
    public void testOnmousedown() {
        panelMenuItemAttributes.set(mode, client);
        Action mousedown = new Actions(driver).clickAndHold(page.getItem().advanced().getRootElement()).build();
        testFireEvent(panelMenuItemAttributes, onmousedown, mousedown);
        new Actions(driver).release().perform();
    }

    @Test
    public void testOnmousemove() {
        panelMenuItemAttributes.set(mode, client);
        Action mousemove = new Actions(driver).moveToElement(page.getItem().advanced().getRootElement(), 3, 3).build();
        testFireEvent(panelMenuItemAttributes, onmousemove, mousemove);
    }

    @Test
    public void testOnmouseout() {
        panelMenuItemAttributes.set(mode, client);
        // TODO JJa 2013-02-25: Rewrite using webdriver api when fixed (not working now)
        testFireEventWithJS(page.getItem().advanced().getRootElement(), Event.MOUSEOUT, panelMenuItemAttributes, PanelMenuItemAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        panelMenuItemAttributes.set(mode, client);
        Action mouseover = new Actions(driver).moveToElement(page.getItem().advanced().getRootElement(), 3, 3).build();
        testFireEvent(panelMenuItemAttributes, onmouseover, mouseover);
    }

    @Test
    public void testOnmouseup() {
        panelMenuItemAttributes.set(mode, client);
        Action mouseup = new Actions(driver).clickAndHold(page.getItem().advanced().getRootElement()).release().build();
        testFireEvent(panelMenuItemAttributes, onmouseup, mouseup);
    }
}
