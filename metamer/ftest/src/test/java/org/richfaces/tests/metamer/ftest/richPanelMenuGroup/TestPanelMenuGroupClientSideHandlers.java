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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.richfaces.component.Mode.ajax;
import static org.richfaces.component.Mode.client;
import static org.richfaces.component.Mode.server;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.mode;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuGroupClientSideHandlers extends AbstractPanelMenuGroupTest {

    private final Attributes<PanelMenuGroupAttributes> panelMenuGroupAttributes = getAttributes();

    @Inject
    @Use(empty = true)
    private String event;

    private String[] ajaxExpansionEvents = new String[]{ "beforeselect", "beforeswitch", "beforeexpand", "begin", "beforedomupdate",
        "select", "expand", "switch", "complete" };
    private String[] ajaxCollapsionEvents = new String[]{ "beforeselect", "beforeswitch", "beforecollapse", "begin",
        "beforedomupdate", "select", "collapse", "switch", "complete" };
    private String[] clientExpansionEvents = new String[]{ "beforeselect", "beforeswitch", "beforeexpand", "select", "expand",
        "switch" };
    private String[] clientCollapsionEvents = new String[]{ "beforeselect", "beforeswitch", "beforecollapse", "select", "collapse",
        "collapse", "switch" };
    private String[] serverExpansionEvents1 = new String[]{ "beforeswitch" };
    private String[] serverExpansionEvents2 = new String[]{ "beforeexpand" };
    private String[] serverCollapsionEvents = new String[]{ "beforeswitch", "beforecollapse" };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuGroup/simple.xhtml");
    }

    public MetamerPage getPage() {
        return page;
    }

    @Test
    @Use(field = "event", value = "ajaxCollapsionEvents")
    public void testClientSideCollapsionEvent() {
        panelMenuGroupAttributes.set(mode, ajax);
        testRequestEventsBefore(event);
        guardAjax(page.getMenu()).collapseGroup(1);
        testRequestEventsAfter(event);
    }

    @Test
    @Use(field = "event", value = "ajaxExpansionEvents")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
            "richList" })
    public void testClientSideExpansionEvent() {
        panelMenuGroupAttributes.set(mode, ajax);
        guardAjax(page.getMenu()).collapseGroup(1);
        testRequestEventsBefore(event);
        guardAjax(page.getMenu()).expandGroup(1);
        testRequestEventsAfter(event);
    }

    @Test
    @Use(field = "event", strings = { "beforeselect", "beforeswitch", "begin", "beforedomupdate", "select", "switch",
            "complete" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
            "richList" })
    public void testClientSideExpansionEventInIterationComponents() {
        testClientSideExpansionEvent();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", strings = { "beforeexpand", "expand" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
            "richList" })
    public void testClientSideExpansionEventInIterationComponentsExpand() {
        testClientSideExpansionEvent();
    }

    @Test
    public void testClientSideExpansionEventsOrderClient() {
        panelMenuGroupAttributes.set(mode, client);
        testRequestEventsBefore(clientExpansionEvents);
        page.getMenu().collapseGroup(1);
        page.getMenu().expandGroup(0);
        cleanMetamerEventsVariable();
        page.getMenu().expandGroup(1);
        testRequestEventsAfter(clientExpansionEvents);
    }

    @Test(groups = { "Future" }) //false negative, need to fix
    @RegressionTest("https://issues.jboss.org/browse/RF-10564")
    public void testClientSideCollapsionEventsOrderClient() {
        panelMenuGroupAttributes.set(mode, client);
        page.getMenu().collapseGroup(1);
        testRequestEventsBefore(clientCollapsionEvents);
        page.getMenu().expandGroup(1);
        testRequestEventsAfter(clientCollapsionEvents);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12549")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
            "richList" })
    public void testClientSideExpansionEventsOrderAjax() {
        panelMenuGroupAttributes.set(mode, ajax);
        guardAjax(page.getMenu()).collapseGroup(1);
        testRequestEventsBefore(ajaxExpansionEvents);
        cleanMetamerEventsVariable();
        guardAjax(page.getMenu()).expandGroup(1);
        testRequestEventsAfter(ajaxExpansionEvents);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
            "richList" })
    public void testClientSideExpansionEventsOrderAjaxInIterationComponents() {
        testClientSideExpansionEventsOrderAjax();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12549")
    public void testClientSideCollapsionEventsOrderAjax() {
        panelMenuGroupAttributes.set(mode, ajax);
        testRequestEventsBefore(ajaxCollapsionEvents);
        guardAjax(page.getMenu()).collapseGroup(1);
        testRequestEventsAfter(ajaxCollapsionEvents);
    }

    @Test
    @Use(field = "event", value = "serverExpansionEvents1")
    public void testClientSideExpansionEventsServerBeforeSwitch() {
        panelMenuGroupAttributes.set(mode, server);
        guardHttp(page.getMenu()).collapseGroup(1);
        // testRequestEventsBeforeByAlert(event);
        testRequestEventsBefore(event);
        guardHttp(page.getMenu()).expandGroup(1);
        // testRequestEventsAfterByAlert(event);
        testRequestEventsAfter(event);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", value = "serverExpansionEvents2")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
            "richList" })
    public void testClientSideExpansionEventsServerBeforeExpand() {
        panelMenuGroupAttributes.set(mode, server);
        guardHttp(page.getMenu()).collapseGroup(1);
        // testRequestEventsBeforeByAlert(event);
        testRequestEventsBefore(event);
        guardHttp(page.getMenu()).expandGroup(1);
        // testRequestEventsAfterByAlert(event);
        testRequestEventsAfter(event);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @Use(field = "event", value = "serverExpansionEvents2")
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
            "richList" })
    public void testClientSideExpansionEventsServerBeforeExpandIterationComponents() {
        testClientSideExpansionEventsServerBeforeExpand();
    }

    @Test
    @Use(field = "event", value = "serverCollapsionEvents")
    public void testClientSideCollapsionEventsServer() {
        panelMenuGroupAttributes.set(mode, server);
        // testRequestEventsBeforeByAlert(event);
        testRequestEventsBefore(event);
        guardHttp(page.getMenu()).collapseGroup(1);
        // testRequestEventsAfterByAlert(event);
        testRequestEventsAfter(event);
    }
}
