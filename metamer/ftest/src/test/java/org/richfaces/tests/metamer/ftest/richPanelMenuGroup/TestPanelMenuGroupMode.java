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

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.richfaces.component.Mode.ajax;
import static org.richfaces.component.Mode.client;
import static org.richfaces.component.Mode.server;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.LinkedList;

import javax.faces.event.PhaseId;

import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuGroupMode extends AbstractPanelMenuGroupTest {

    private final Attributes<PanelMenuGroupAttributes> panelMenuGroupAttributes = getAttributes();

    @Inject
    @Use(booleans = { true, false })
    private Boolean immediate;

    @Inject
    @Use(booleans = { true, false })
    private Boolean bypassUpdates;

    @Inject
    @Use("requestModes")
    private Mode mode;
    private Mode[] requestModes = new Mode[]{ ajax, server };

    @Inject
    @Use("listeners")
    private String listener;
    private String[] listeners = new String[] { "phases", "action invoked", "action listener invoked", "executeChecker", "item changed" };

    @Test
    public void testRequestMode() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.immediate, immediate);
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.bypassUpdates, bypassUpdates);
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, mode);

        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.execute, "@this executeChecker");

        assertTrue(page.getTopGroup().advanced().isExpanded());
        switch (mode) {
            case ajax:
                guardAjax(page.getMenu()).collapseGroup(1);
                break;
            case server:
                guardHttp(page.getMenu()).collapseGroup(1);
                break;
            case client:
                page.getMenu().collapseGroup(1);
        }
        assertFalse(page.getTopGroup().advanced().isExpanded());

        if (mode != client) {
            if ("phases".equals(listener)) {
                page.assertPhases(getExpectedPhases());
            } else {
                PhaseId listenerInvocationPhase = getListenerInvocationPhase();
                if (listenerInvocationPhase == null) {
                    page.assertNoListener(listener);
                } else {
                    page.assertListener(listenerInvocationPhase, listener);
                }
            }
        }
    }

    @Test
    @Uses({ @Use(field = "immediate", empty = true), @Use(field = "bypassUpdates", empty = true),
            @Use(field = "mode", empty = true), @Use(field = "listener", empty = true) })
    public void testClientMode() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, client);

        assertTrue(page.getTopGroup().advanced().isExpanded());
        page.getMenu().collapseGroup(1);
        assertFalse(page.getTopGroup().advanced().isExpanded());
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
            if (phase.compareTo(UPDATE_MODEL_VALUES) < 0 || mode == server) {
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
