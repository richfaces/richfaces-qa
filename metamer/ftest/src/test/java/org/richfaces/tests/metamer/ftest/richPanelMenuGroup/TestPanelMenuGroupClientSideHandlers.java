/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.STRINGS;

import java.net.URL;

import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.BecauseOf;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuGroupClientSideHandlers extends AbstractPanelMenuGroupTest {

    private final Attributes<PanelMenuGroupAttributes> panelMenuGroupAttributes = getAttributes();

    private String event;
    private final String[] ajaxCollapsionEvents = new String[] { "onbeforeselect", "onbeforeswitch", "onbeforecollapse", "onbegin",
        "onbeforedomupdate", "onselect", "oncollapse", "onswitch", "oncomplete" };
    private final String[] ajaxExpansionEvents = new String[] { "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" };
    private final String[] clientCollapsionEvents = new String[] { "onbeforeselect", "onbeforeswitch", "onbeforecollapse", "onselect", "oncollapse",
        "oncollapse", "onswitch" };
    private final String[] clientExpansionEvents = new String[] { "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onselect", "onexpand",
        "onswitch" };
    private final String[] serverExpansionEvents1 = new String[] { "onbeforeswitch" };
    private final String[] serverExpansionEvents2 = new String[] { "onbeforeexpand" };
    private final String[] serverCollapsionEvents = new String[] { "onbeforeswitch", "onbeforecollapse" };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuGroup/simple.xhtml");
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "ajaxCollapsionEvents")
    public void testClientSideCollapsionEvent() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.ajax);
        testRequestEventsBefore(event);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        testRequestEventsAfter(event);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @RegressionTest("https://issues.jboss.org/browse/RF-12549")
    public void testClientSideCollapsionEventsOrderAjax() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.ajax);
        testRequestEventsBefore(ajaxCollapsionEvents);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        testRequestEventsAfter(ajaxCollapsionEvents);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforecollapse", "onselect", "oncollapse",
        "oncollapse", "onswitch" })
    @RegressionTest("https://issues.jboss.org/browse/RF-10564")
    public void testClientSideCollapsionEventsOrderClient() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.client);
        //first expand group 2 because you want to test collapse
        getPage().getMenu().expandGroup(1);
        testRequestEventsBefore(clientCollapsionEvents);
        //collapse and verify event order
        getPage().getMenu().collapseGroup(1);
        testRequestEventsAfter(clientCollapsionEvents);
    }

    @Test
    @CoversAttributes({ "onbeforeswitch", "onbeforecollapse" })
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "serverCollapsionEvents")
    public void testClientSideCollapsionEventsServer() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.server);
        // testRequestEventsBeforeByAlert(event);
        testRequestEventsBefore(event);
        guardHttp(getPage().getMenu()).collapseGroup(1);
        // testRequestEventsAfterByAlert(event);
        testRequestEventsAfter(event);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "ajaxExpansionEvents")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
        "richList", "uiRepeat" })
    public void testClientSideExpansionEvent() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.ajax);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        testRequestEventsBefore(event);
        guardAjax(getPage().getMenu()).expandGroup(1);
        testRequestEventsAfter(event);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbegin", "onbeforedomupdate", "onselect", "onswitch",
        "oncomplete" })
    @UseWithField(field = "event", valuesFrom = STRINGS, value = { "onbeforeselect", "onbeforeswitch", "onbegin", "onbeforedomupdate", "onselect", "onswitch",
        "oncomplete" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
        "richList" })
    public void testClientSideExpansionEventInIterationComponents() {
        testClientSideExpansionEvent();
    }

    @Test
    @CoversAttributes({ "onbeforeexpand", "onexpand" })
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @UseWithField(field = "event", valuesFrom = STRINGS, value = { "onbeforeexpand", "onexpand" })
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
        "richList" })
    public void testClientSideExpansionEventInIterationComponentsExpand() {
        testClientSideExpansionEvent();
    }

    @Test // fails with JSF 2.2
    @Skip(BecauseOf.UIRepeatSetIndexIssue.class)
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbegin", "onbeforedomupdate", "onselect", "onswitch",
        "oncomplete" })
    @IssueTracking("https://issues.jboss.org/browse/RF-13727")
    @UseWithField(field = "event", valuesFrom = STRINGS, value = { "onbeforeselect", "onbeforeswitch", "onbegin", "onbeforedomupdate", "onselect", "onswitch",
        "oncomplete" })
    @Templates(value = { "uiRepeat" })
    public void testClientSideExpansionEventInUiRepeat() {
        testClientSideExpansionEvent();
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @RegressionTest("https://issues.jboss.org/browse/RF-12549")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
        "richList", "uiRepeat" })
    public void testClientSideExpansionEventsOrderAjax() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.ajax);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        testRequestEventsBefore(ajaxExpansionEvents);
        cleanMetamerEventsVariable();
        guardAjax(getPage().getMenu()).expandGroup(1);
        testRequestEventsAfter(ajaxExpansionEvents);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
        "richList" })
    public void testClientSideExpansionEventsOrderAjaxInIterationComponents() {
        testClientSideExpansionEventsOrderAjax();
    }

    @Test // fails with JSF 2.2
    @Skip(BecauseOf.UIRepeatSetIndexIssue.class)
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @IssueTracking("https://issues.jboss.org/browse/RF-13727")
    @Templates(value = { "uiRepeat" })
    public void testClientSideExpansionEventsOrderAjaxInUiRepeat() {
        testClientSideExpansionEventsOrderAjax();
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onselect", "onexpand",
        "onswitch" })
    public void testClientSideExpansionEventsOrderClient() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.client);
        testRequestEventsBefore(clientExpansionEvents);
        getPage().getMenu().collapseGroup(1);
        getPage().getMenu().expandGroup(0);
        cleanMetamerEventsVariable();
        getPage().getMenu().expandGroup(1);
        testRequestEventsAfter(clientExpansionEvents);
    }

    @Test
    @CoversAttributes("onbeforeexpand")
    @IssueTracking("https://issues.jboss.org/browse/RF-11547")
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "serverExpansionEvents2")
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
        "richList" })
    public void testClientSideExpansionEventsServerBeforeExpand() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.server);
        guardHttp(getPage().getMenu()).collapseGroup(1);
        // testRequestEventsBeforeByAlert(event);
        testRequestEventsBefore(event);
        guardHttp(getPage().getMenu()).expandGroup(1);
        // testRequestEventsAfterByAlert(event);
        testRequestEventsAfter(event);
    }

    @Test
    @CoversAttributes("onbeforeexpand")
    @RegressionTest("https://issues.jboss.org/browse/RF-11547")
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "serverExpansionEvents2")
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richDataGrid", "richDataTable", "richExtendedDataTable",
        "richList" })
    public void testClientSideExpansionEventsServerBeforeExpandIterationComponents() {
        testClientSideExpansionEventsServerBeforeExpand();
    }

    @Test
    @CoversAttributes("onbeforeswitch")
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "serverExpansionEvents1")
    public void testClientSideExpansionEventsServerBeforeSwitch() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.server);
        guardHttp(getPage().getMenu()).collapseGroup(1);
        // testRequestEventsBeforeByAlert(event);
        testRequestEventsBefore(event);
        guardHttp(getPage().getMenu()).expandGroup(1);
        // testRequestEventsAfterByAlert(event);
        testRequestEventsAfter(event);
    }
}
