/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.panelMenuAttributes;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupMode;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.itemMode;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22749 $
 */
public class TestPanelMenuMode extends AbstractPanelMenuTest {

    @Inject
    @Use(enumeration = true)
    PanelMenuMode mode;

    PhaseId[] expectedPhases = new PhaseId[] { RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS,
        UPDATE_MODEL_VALUES, INVOKE_APPLICATION, RENDER_RESPONSE };

    @Test
    public void testGroupMode() {
        panelMenuAttributes.set(groupMode, mode);
        menu.setGroupMode(mode);

        assertTrue(group1.isCollapsed());
        group1.toggle();
        assertTrue(group1.isExpanded());

        if (mode != PanelMenuMode.client) {
            phaseInfo.assertPhases(expectedPhases);
        }

        group1.toggle();

        assertTrue(group1.isCollapsed());

        if (mode != PanelMenuMode.client) {
            phaseInfo.assertPhases(expectedPhases);
        }
    }

    @Test
    public void testItemMode() {
        panelMenuAttributes.set(itemMode, mode);
        menu.setItemMode(mode);

        assertFalse(item3.isSelected());
        item3.select();
        assertTrue(item3.isSelected());

        if (mode != PanelMenuMode.client) {
            phaseInfo.assertPhases(expectedPhases);
            phaseInfo.assertListener(UPDATE_MODEL_VALUES, "item changed");
        }
    }
}
