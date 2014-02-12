/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.tests.metamer.ftest.richCollapsiblePanel;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Icon;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richCollapsiblePanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestCollapsiblePanel extends TestFacets {

    @Override
    protected String getExpectedHeaderAfterCollapsion() {
        return collapsiblePanelAttributes.get(CollapsiblePanelAttributes.header);
    }

    @Override
    protected String getExpectedHeaderAfterExpansion() {
        return getExpectedHeaderAfterCollapsion();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsiblePanel/simple.xhtml");
    }

    @Test
    @Templates(value = "plain")
    public void testBodyClass() {
        testStyleClass(panel.advanced().getBodyElement(), BasicAttributes.bodyClass);
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        testDir(panel.advanced().getRootElement());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10312")
    public void testExpanded() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.expanded, Boolean.TRUE);
        verifyStateAfterExpansion();
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.expanded, Boolean.FALSE);
        verifyStateAfterCollapsion();
    }

    @Test
    public void testHeader() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.header, "new header");

        assertEquals(panel.getHeaderText(), "new header", "Header of the panel did not change.");

        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.header, "ľščťťžžôúňď ацущьмщфзщйцу");

        assertEquals(panel.getHeaderText(), "ľščťťžžôúňď ацущьмщфзщйцу", "Header of the panel did not change.");
    }

    @Test
    @Templates(value = "plain")
    public void testHeaderClass() {
        testStyleClass(panel.advanced().getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test(groups = "smoke")
    @RegressionTest("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.immediate, Boolean.TRUE);

        Graphene.guardAjax(panel).collapse();

        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "panel collapsed");
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testAttributeLang(panel.advanced().getRootElement());
    }

    @Test
    public void testLeftCollapsedIcon() {
        Graphene.guardAjax(panel).collapse();
        verifyStandardIcons(panel.advanced().getLeftIcon(), CollapsiblePanelAttributes.leftCollapsedIcon);
    }

    @Test
    public void testLeftExpandedIcon() {
        verifyStandardIcons(panel.advanced().getLeftIcon(), CollapsiblePanelAttributes.leftExpandedIcon);
    }

    @Test
    public void testOnbeforeswitchOnswitch() {
        testRequestEventsBefore("beforeswitch", "switch");
        Graphene.guardAjax(panel).collapse();
        testRequestEventsAfter("beforeswitch", "switch");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, panel.advanced().getRootElement());
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, panel.advanced().getRootElement());
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, panel.advanced().getRootElement());
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, panel.advanced().getRootElement());
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, panel.advanced().getRootElement());
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, panel.advanced().getRootElement());
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, panel.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.rendered, Boolean.FALSE);
        assertFalse(panel.advanced().getRootElement().isPresent(), "Panel should not be rendered when rendered=false.");
    }

    @Test
    public void testRightCollapsedIcon() {
        Graphene.guardAjax(panel).collapse();
        verifyStandardIcons(panel.advanced().getRightIcon(), CollapsiblePanelAttributes.rightCollapsedIcon);
    }

    @Test
    public void testRightExpandedIcon() {
        verifyStandardIcons(panel.advanced().getRightIcon(), CollapsiblePanelAttributes.rightExpandedIcon);
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(panel.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(panel.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    @Use(field = "switchType", strings = { "null", "ajax", "client", "server" })
    @RegressionTest("https://issues.jboss.org/browse/RF-10368")
    public void testSwitchType() {
        super.testSwitchType();
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(panel.advanced().getRootElement());
    }

    @Test
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "richList",
        "a4jRepeat" })
    public void testToggleListener() {
        Graphene.guardAjax(panel).collapse();
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "panel collapsed");

        Graphene.guardAjax(panel).expand();
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "panel expanded");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11568")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "richList",
        "a4jRepeat" })
    public void testToggleListenerInIterationComponents() {
        testToggleListener();
    }

    private void verifyStandardIcons(Icon icon, CollapsiblePanelAttributes attribute) {
        new IconsChecker<CollapsiblePanelAttributes>(collapsiblePanelAttributes, "rf-ico-", "-hdr").checkAll(attribute, icon, "");
    }
}
