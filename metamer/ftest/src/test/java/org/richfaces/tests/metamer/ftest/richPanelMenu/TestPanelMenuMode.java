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

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22749 $
 */
public class TestPanelMenuMode extends AbstractPanelMenuTest {

    private final Attributes<PanelMenuAttributes> panelMenuAttributes = getAttributes();

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    private Mode mode;

    private PhaseId[] expectedPhases = new PhaseId[]{ RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, UPDATE_MODEL_VALUES,
        INVOKE_APPLICATION, RENDER_RESPONSE };

    @Test
    public void testGroupMode() {
        checkGroupMode(mode);
    }

    @Test
    public void testItemMode() {
        checkItemMode(mode);
    }

    private void checkGroupMode(Mode mode) {
        panelMenuAttributes.set(PanelMenuAttributes.groupMode, mode);

        assertFalse(getPage().getGroup1().advanced().isExpanded());
        switch (mode) {
            case ajax:
                guardAjax(getPage().getPanelMenu()).expandGroup("Group 1");
                break;
            case server:
                guardHttp(getPage().getPanelMenu()).expandGroup("Group 1");
                break;
            case client:
                getPage().getPanelMenu().expandGroup("Group 1");
                break;
        }
        assertTrue(getPage().getGroup1().advanced().isExpanded());

        if (mode != Mode.client) {
            getPage().assertPhases(expectedPhases);
        }

        switch (mode) {
            case ajax:
                guardAjax(getPage().getPanelMenu()).collapseGroup("Group 1");
                break;
            case server:
                guardHttp(getPage().getPanelMenu()).collapseGroup("Group 1");
                break;
            case client:
                getPage().getPanelMenu().collapseGroup("Group 1");
                break;
        }
        assertFalse(getPage().getGroup1().advanced().isExpanded());

        if (mode != Mode.client) {
            getPage().assertPhases(expectedPhases);
        }
    }

    private void checkItemMode(Mode mode) {
        panelMenuAttributes.set(PanelMenuAttributes.itemMode, mode);

        assertFalse(getPage().getItem3().advanced().isSelected());
        switch (mode) {
            case ajax:
                guardAjax(getPage().getItem3()).select();
                break;
            case server:
                guardHttp(getPage().getItem3()).select();
                break;
            case client:
                getPage().getItem3().select();
                break;
        }
        assertTrue(getPage().getItem3().advanced().isSelected());

        if (mode != Mode.client) {
            getPage().assertPhases(expectedPhases);
            getPage().assertListener(UPDATE_MODEL_VALUES, "item changed");
        }
    }
}
