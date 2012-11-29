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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.PanelMenuMode.ajax;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.panelMenuAttributes;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupMode;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.itemMode;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.model.PanelMenu;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22749 $
 */
public class AbstractPanelMenuTest extends AbstractGrapheneTest {

    PanelMenu menu = new PanelMenu(pjq("div.rf-pm[id$=panelMenu]"));
    PanelMenu.Item item3 = menu.getItemContains("Item 3");
    PanelMenu.Item item4 = menu.getItemContains("Item 4");
    PanelMenu.Group group1 = menu.getGroupContains("Group 1");
    PanelMenu.Group group2 = menu.getGroupContains("Group 2");
    PanelMenu.Item item22 = group2.getItemContains("Item 2.2");
    PanelMenu.Item item25 = group2.getItemContains("Item 2.5");
    PanelMenu.Group group24 = group2.getGroupContains("Group 2.4");
    PanelMenu.Group group26 = group2.getGroupContains("Group 2.6");
    PanelMenu.Item item242 = group24.getItemContains("Item 2.4.2");
    PanelMenu.Group group4 = menu.getGroupContains("Group 4");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenu/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Panel Menu", "Simple");
    }

    @BeforeMethod
    public void setupModes() {
        panelMenuAttributes.set(itemMode, ajax);
        panelMenuAttributes.set(groupMode, ajax);
        menu.setItemMode(ajax);
        menu.setGroupMode(ajax);
    }
}
