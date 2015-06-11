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
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;

import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
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

    private final String[] ajaxCollapsionEvents = new String[] { "onbeforeselect", "onbeforeswitch", "onbeforecollapse", "onbegin",
        "onbeforedomupdate", "onselect", "oncollapse", "onswitch", "oncomplete" };
    private final String[] ajaxExpansionEvents = new String[] { "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" };
    private final String[] clientCollapsionEvents = new String[] { "onbeforeselect", "onbeforeswitch", "onbeforecollapse", "onselect", "oncollapse",
        "oncollapse", "onswitch" };
    private final String[] clientExpansionEvents = new String[] { "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onselect", "onexpand",
        "onswitch" };
    private String event;
    private final Attributes<PanelMenuGroupAttributes> panelMenuGroupAttributes = getAttributes();

    @Override
    public String getComponentTestPagePath() {
        return "richPanelMenuGroup/simple.xhtml";
    }

    @Test
    @Templates("plain")
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "ajaxCollapsionEvents")
    public void testCollapsionEvent() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.ajax);
        testRequestEventsBefore(event);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        testRequestEventsAfter(event);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @RegressionTest("https://issues.jboss.org/browse/RF-12549")
    public void testCollapsionEventsOrderAjax() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.ajax);
        testRequestEventsBefore(ajaxCollapsionEvents);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        testRequestEventsAfter(ajaxCollapsionEvents);
    }

    @Test
    @Templates("plain")
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforecollapse", "onselect", "oncollapse",
        "oncollapse", "onswitch" })
    @RegressionTest("https://issues.jboss.org/browse/RF-10564")
    public void testCollapsionEventsOrderClient() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.client);
        //first expand group 2 because you want to test collapse
        getPage().getMenu().expandGroup(1);
        testRequestEventsBefore(clientCollapsionEvents);
        //collapse and verify event order
        getPage().getMenu().collapseGroup(1);
        testRequestEventsAfter(clientCollapsionEvents);
    }

    @Test
    @Templates("plain")
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @UseWithField(field = "event", valuesFrom = FROM_FIELD, value = "ajaxExpansionEvents")
    @RegressionTest({ "https://issues.jboss.org/browse/RF-11547" })
    public void testExpansionEvent() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.ajax);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        testRequestEventsBefore(event);
        guardAjax(getPage().getMenu()).expandGroup(1);
        testRequestEventsAfter(event);
    }

    @Test
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onbegin", "onbeforedomupdate",
        "onselect", "onexpand", "onswitch", "oncomplete" })
    @RegressionTest({ "https://issues.jboss.org/browse/RF-12549", "https://issues.jboss.org/browse/RF-11547", "https://issues.jboss.org/browse/RF-13727" })
    public void testExpansionEventsOrderAjax() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.ajax);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        testRequestEventsBefore(ajaxExpansionEvents);
        guardAjax(getPage().getMenu()).expandGroup(1);
        testRequestEventsAfter(ajaxExpansionEvents);
    }

    @Test
    @Templates("plain")
    @CoversAttributes({ "onbeforeselect", "onbeforeswitch", "onbeforeexpand", "onselect", "onexpand",
        "onswitch" })
    public void testExpansionEventsOrderClient() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.client);
        testRequestEventsBefore(clientExpansionEvents);
        getPage().getMenu().collapseGroup(1);
        getPage().getMenu().expandGroup(0);
        cleanMetamerEventsVariable();
        getPage().getMenu().expandGroup(1);
        testRequestEventsAfter(clientExpansionEvents);
    }

    @Test
    @Templates("plain")
    @CoversAttributes("onbeforeexpand")
    public void testOnbeforeexpandServer() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.server);
        guardHttp(getPage().getMenu()).collapseGroup(1);
        // testRequestEventsBeforeByAlert(event);
        testRequestEventsBefore("onbeforeexpand");
        guardHttp(getPage().getMenu()).expandGroup(1);
        // testRequestEventsAfterByAlert(event);
        testRequestEventsAfter("onbeforeexpand");
    }

    @Test
    @Templates("plain")
    @CoversAttributes({ "onbeforeswitch", "onbeforecollapse" })
    public void testOnbeforeswitchOnbeforecollapseServer() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.server);
        testRequestEventsBefore("onbeforeswitch", "onbeforecollapse");
        guardHttp(getPage().getMenu()).collapseGroup(1);
        testRequestEventsAfter("onbeforeswitch", "onbeforecollapse");
    }

    @Test
    @Templates("plain")
    @CoversAttributes("onbeforeswitch")
    public void testOnbeforeswitchServer() {
        panelMenuGroupAttributes.set(PanelMenuGroupAttributes.mode, Mode.server);
        guardHttp(getPage().getMenu()).collapseGroup(1);
        // testRequestEventsBeforeByAlert(event);
        testRequestEventsBefore("onbeforeswitch");
        guardHttp(getPage().getMenu()).expandGroup(1);
        // testRequestEventsAfterByAlert(event);
        testRequestEventsAfter("onbeforeswitch");
    }
}
