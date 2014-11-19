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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;

import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22749 $
 */
public class TestPanelMenuSelection extends AbstractPanelMenuTest {

    private final Attributes<PanelMenuAttributes> panelMenuAttributes = getAttributes();

    @UseForAllTests(valuesFrom = FROM_FIELD, value = "booleans")
    private Boolean bubbleSelection;

    @Test(groups = "smoke")
    public void testBubbleSelection() {
        panelMenuAttributes.set(PanelMenuAttributes.bubbleSelection, bubbleSelection);

        assertEquals(getSelectedItems(), 0);
        assertEquals(getSelectedGroups(), 0);
        getPage().getPanelMenu().expandGroup("Group 2");
        assertEquals(getSelectedItems(), 0);
        assertEquals(getSelectedGroups(), 0);
        guardAjax(getPage().getItem22()).select();
        assertEquals(getSelectedItems(), 1);
        assertEquals(getSelectedGroups(), bubbledGroups(1));
        getPage().getPanelMenu().expandGroup("Group 2.4");
        assertEquals(getSelectedItems(), 1);
        assertEquals(getSelectedGroups(), bubbledGroups(1));
        guardAjax(getPage().getPanelMenu()).selectItem("Item 2.4.2");
        assertEquals(getSelectedItems(), 1);
        assertEquals(getSelectedGroups(), bubbledGroups(2));
    }

    private int bubbledGroups(int bubbledGroups) {
        return bubbleSelection ? bubbledGroups : 0;
    }

    private int getSelectedItems() {
        return getPage().getPanelMenu().advanced().getAllSelectedItems().size();
    }

    private int getSelectedGroups() {
        return getPage().getPanelMenu().advanced().getAllSelectedGroups().size();
    }
}
