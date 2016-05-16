/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richPanelMenuItem;

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.LinkedList;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuItemMode extends AbstractWebDriverTest {

    private Boolean bypassUpdates;
    private Boolean immediate;
    private final String[] listeners = new String[] { "phases", "action invoked", "action listener invoked", "executeChecker",
        "item changed" };
    private Mode mode;

    @Page
    private PanelMenuItemPage page;

    private final Attributes<PanelMenuItemAttributes> panelMenuItemAttributes = getAttributes();
    private final Mode[] requestModes = new Mode[] { Mode.ajax, Mode.server };

    @Override
    public String getComponentTestPagePath() {
        return "richPanelMenuItem/simple.xhtml";
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

    private PhaseId getListenerInvocationPhase(String listener) {
        PhaseId[] phases = getExpectedPhases();
        PhaseId phase = phases[phases.length - 2];

        if ("executeChecker".equals(listener)) {
            if (phase.compareTo(UPDATE_MODEL_VALUES) < 0 || mode == Mode.server) {
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

    @Test
    @CoversAttributes({ "bypassUpdates", "execute", "immediate", "mode" })
    @Uses({
        @UseWithField(field = "immediate", valuesFrom = FROM_FIELD, value = "booleans"),
        @UseWithField(field = "bypassUpdates", valuesFrom = FROM_FIELD, value = "booleans"),
        @UseWithField(field = "mode", valuesFrom = FROM_FIELD, value = "requestModes")
    })
    public void testModeAjaxAndServer() {
        attsSetter()
            .setAttribute(PanelMenuItemAttributes.immediate).toValue(immediate)
            .setAttribute(PanelMenuItemAttributes.bypassUpdates).toValue(bypassUpdates)
            .setAttribute(PanelMenuItemAttributes.mode).toValue(mode)
            .setAttribute(PanelMenuItemAttributes.execute).toValue("@this executeChecker")
            .asSingleAction().perform();

        assertFalse(page.getItem().advanced().isSelected());
        switch (mode) {
            case ajax:
                guardAjax(page.getItem()).select();
                break;
            case server:
                guardHttp(page.getItem()).select();
                break;
            case client:
                page.getItem().select();
                break;
        }
        assertTrue(page.getItem().advanced().isSelected());

        for (String listener : listeners) {
            if ("phases".equals(listener)) {
                page.assertPhases(getExpectedPhases());
            } else {
                PhaseId listenerInvocationPhase = getListenerInvocationPhase(listener);
                if (listenerInvocationPhase == null) {
                    page.assertNoListener(listener);
                } else {
                    page.assertListener(listenerInvocationPhase, listener);
                }
            }
        }
    }

    @Test
    @CoversAttributes("mode")
    public void testModeClient() {
        panelMenuItemAttributes.set(PanelMenuItemAttributes.mode, Mode.client);

        assertFalse(page.getItem().advanced().isSelected());
        page.getItem().select();
        assertTrue(page.getItem().advanced().isSelected());
    }
}
