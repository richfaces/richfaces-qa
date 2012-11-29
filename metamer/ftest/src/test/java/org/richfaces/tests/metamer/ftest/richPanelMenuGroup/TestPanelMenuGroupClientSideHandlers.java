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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.PanelMenuMode.ajax;
import static org.richfaces.PanelMenuMode.client;
import static org.richfaces.PanelMenuMode.server;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.panelMenuGroupAttributes;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.mode;

import java.net.URL;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.PanelMenu;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23138 $
 */
public class TestPanelMenuGroupClientSideHandlers extends AbstractPanelMenuGroupTest {

    PanelMenu.Group group1 = menu.getGroupContains("Group 1");

    @Inject
    @Use(empty = true)
    String event;
    String[] ajaxExpansionEvents = new String[] { "beforeselect", "beforeswitch", "beforeexpand", "begin",
        "beforedomupdate", "select", "expand", "switch", "complete" };
    String[] ajaxCollapsionEvents = new String[] { "beforeselect", "beforeswitch", "beforecollapse", "begin",
        "beforedomupdate", "select", "collapse", "switch", "complete" };
    String[] clientExpansionEvents = new String[] { "beforeselect", "beforeswitch", "beforeexpand", "select", "expand",
        "switch" };
    String[] clientCollapsionEvents = new String[] { "beforeselect", "beforeswitch", "beforecollapse", "select",
        "collapse", "collapse", "switch" };
    String[] serverExpansionEvents1 = new String[] { "beforeswitch" };
    String[] serverExpansionEvents2 = new String[] { "beforeexpand" };
    String[] serverCollapsionEvents = new String[] { "beforeswitch", "beforecollapse" };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuGroup/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Panel Menu Group", "Simple");
    }

    @Test
    @Use(field = "event", value = "ajaxCollapsionEvents")
    public void testClientSideCollapsionEvent() {
        panelMenuGroupAttributes.set(mode, ajax);
        menu.setGroupMode(ajax);
        super.testRequestEventsBefore(event);
        topGroup.toggle();
        super.testRequestEventsAfter(event);
    }

    @Test
    @Use(field = "event", value = "ajaxExpansionEvents")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEvent() {
        panelMenuGroupAttributes.set(mode, ajax);
        menu.setGroupMode(ajax);
        topGroup.toggle();
        super.testRequestEventsBefore(event);
        topGroup.toggle();
        super.testRequestEventsAfter(event);
    }

    @Test
    @Use(field = "event", strings = { "beforeselect", "beforeswitch", "begin", "beforedomupdate", "select", "switch",
        "complete" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventInIterationComponents() {
        testClientSideExpansionEvent();
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", strings = { "beforeexpand", "expand" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventInIterationComponentsExpand() {
        testClientSideExpansionEvent();
    }

    @Test
    public void testClientSideExpansionEventsOrderClient() {
        panelMenuGroupAttributes.set(mode, client);
        menu.setGroupMode(client);
        super.testRequestEventsBefore(clientExpansionEvents);
        topGroup.toggle();
        group1.toggle();
        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        topGroup.toggle();
        super.testRequestEventsAfter(clientExpansionEvents);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10564")
    public void testClientSideCollapsionEventsOrderClient() {
        panelMenuGroupAttributes.set(mode, client);
        menu.setGroupMode(client);
        topGroup.toggle();
        super.testRequestEventsBefore(clientCollapsionEvents);
        topGroup.toggle();
        super.testRequestEventsAfter(clientCollapsionEvents);
    }

    @Test
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventsOrderAjax() {
        panelMenuGroupAttributes.set(mode, ajax);
        menu.setGroupMode(ajax);
        topGroup.toggle();
        super.testRequestEventsBefore(ajaxExpansionEvents);
        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        topGroup.toggle();
        super.testRequestEventsAfter(ajaxExpansionEvents);
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11547")
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventsOrderAjaxInIterationComponents() {
        testClientSideExpansionEventsOrderAjax();
    }

    @Test
    public void testClientSideCollapsionEventsOrderAjax() {
        panelMenuGroupAttributes.set(mode, ajax);
        menu.setGroupMode(ajax);
        super.testRequestEventsBefore(ajaxCollapsionEvents);
        topGroup.toggle();
        super.testRequestEventsAfter(ajaxCollapsionEvents);
    }

    @Test
    @Use(field = "event", value = "serverExpansionEvents1")
    public void testClientSideExpansionEventsServerBeforeSwitch() {
        panelMenuGroupAttributes.set(mode, server);
        menu.setGroupMode(server);
        topGroup.toggle();
        menu.setGroupMode(null);
        testRequestEventsBeforeByAlert(event);
        topGroup.toggle();
        testRequestEventsAfterByAlert(event);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", value = "serverExpansionEvents2")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventsServerBeforeExpand() {
        panelMenuGroupAttributes.set(mode, server);
        menu.setGroupMode(server);
        topGroup.toggle();
        menu.setGroupMode(null);
        testRequestEventsBeforeByAlert(event);
        topGroup.toggle();
        testRequestEventsAfterByAlert(event);
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", value = "serverExpansionEvents2")
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable",
        "richExtendedDataTable", "richList" })
    public void testClientSideExpansionEventsServerBeforeExpandIterationComponents() {
        testClientSideExpansionEventsServerBeforeExpand();
    }

    @Test
    @Use(field = "event", value = "serverCollapsionEvents")
    public void testClientSideCollapsionEventsServer() {
        panelMenuGroupAttributes.set(mode, server);
        menu.setGroupMode(null);
        testRequestEventsBeforeByAlert(event);
        topGroup.toggle();
        testRequestEventsAfterByAlert(event);
    }
}
