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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.panelMenuGroupAttributes;

import static org.testng.Assert.assertTrue;

import java.util.LinkedList;

import javax.faces.event.PhaseId;

import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22750 $
 */
public class TestPanelMenuGroupMode extends AbstractPanelMenuGroupTest {

    @Inject
    @Use(booleans = { true, false })
    Boolean immediate;

    @Inject
    @Use(booleans = { true, false })
    Boolean bypassUpdates;

    @Inject
    @Use("requestModes")
    PanelMenuMode mode;
    PanelMenuMode[] requestModes = new PanelMenuMode[] { PanelMenuMode.ajax, PanelMenuMode.server };

    @Inject
    @Use("listeners")
    String listener;
    String[] listeners = new String[] { "phases", "action invoked", "action listener invoked", "executeChecker",
        "item changed" };

    @Test
    public void testRequestMode() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.immediate, immediate);
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.bypassUpdates, bypassUpdates);
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, mode);
        menu.setGroupMode(mode);

        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.execute, "@this executeChecker");

        assertTrue(topGroup.isExpanded());
        topGroup.toggle();
        assertTrue(topGroup.isCollapsed());

        if (mode != PanelMenuMode.client) {
            if ("phases".equals(listener)) {
                phaseInfo.assertPhases(getExpectedPhases());
            } else {
                PhaseId listenerInvocationPhase = getListenerInvocationPhase();
                if (listenerInvocationPhase == null) {
                    phaseInfo.assertNoListener(listener);
                } else {
                    phaseInfo.assertListener(listenerInvocationPhase, listener);
                }
            }
        }
    }

    @Test
    @Uses({ @Use(field = "immediate", empty = true), @Use(field = "bypassUpdates", empty = true),
        @Use(field = "mode", empty = true), @Use(field = "listener", empty = true) })
    public void testClientMode() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, PanelMenuMode.client);
        menu.setGroupMode(PanelMenuMode.client);

        assertTrue(topGroup.isExpanded());
        topGroup.toggle();
        assertTrue(topGroup.isCollapsed());
    }

    private PhaseId[] getExpectedPhases() {
        LinkedList<PhaseId> list = new LinkedList<PhaseId>();
        list.add(RESTORE_VIEW);
        list.add(APPLY_REQUEST_VALUES);
        if (!immediate) {
            list.add(PROCESS_VALIDATIONS);
        }
        if (!immediate && !bypassUpdates) {
            list.add(UPDATE_MODEL_VALUES);
            list.add(INVOKE_APPLICATION);
        }
        list.add(RENDER_RESPONSE);
        return list.toArray(new PhaseId[list.size()]);
    }

    private PhaseId getListenerInvocationPhase() {
        PhaseId[] phases = getExpectedPhases();
        PhaseId phase = phases[phases.length - 2];

        if ("executeChecker".equals(listener)) {
            if (phase.compareTo(UPDATE_MODEL_VALUES) < 0 || mode == PanelMenuMode.server) {
                return null;
            } else {
                return UPDATE_MODEL_VALUES;
            }
        }
        
        if ("item changed".equals(listener)) {
            if (phases.length == 6) {
                return UPDATE_MODEL_VALUES;
            }
        }

        return phase;
    }
}
