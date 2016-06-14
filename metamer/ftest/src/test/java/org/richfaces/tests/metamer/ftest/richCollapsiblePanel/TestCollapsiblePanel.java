/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2016, Red Hat, Inc., and individual contributors
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

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.STRINGS;
import static org.testng.Assert.assertEquals;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Icon;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
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
    public String getComponentTestPagePath() {
        return "richCollapsiblePanel/simple.xhtml";
    }

    @Test
    @CoversAttributes("bodyClass")
    @Templates(value = "plain")
    public void testBodyClass() {
        testStyleClass(panel.advanced().getBodyElement(), BasicAttributes.bodyClass);
    }

    @Test
    @CoversAttributes("dir")
    @Templates(value = "plain")
    public void testDir() {
        testDir(panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("expanded")
    @IssueTracking("https://issues.jboss.org/browse/RF-10312")
    public void testExpanded() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.expanded, Boolean.TRUE);
        verifyStateAfterExpansion();
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.expanded, Boolean.FALSE);
        verifyStateAfterCollapsion();
    }

    @Test
    @CoversAttributes("header")
    public void testHeader() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.header, "new header");

        assertEquals(panel.getHeaderText(), "new header", "Header of the panel did not change.");

        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.header, "ľščťťžžôúňď ацущьмщфзщйцу");

        assertEquals(panel.getHeaderText(), "ľščťťžžôúňď ацущьмщфзщйцу", "Header of the panel did not change.");
    }

    @Test
    @CoversAttributes("headerClass")
    @Templates(value = "plain")
    public void testHeaderClass() {
        testStyleClass(panel.advanced().getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test(groups = "smoke")
    @CoversAttributes("immediate")
    @IssueTracking("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.immediate, Boolean.TRUE);

        Graphene.guardAjax(panel).collapse();

        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "panel collapsed");
    }

    @Test
    @CoversAttributes("lang")
    @Templates(value = "plain")
    public void testLang() {
        testLang(panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("leftCollapsedIcon")
    @Templates(value = "plain")
    public void testLeftCollapsedIcon() {
        Graphene.guardAjax(panel).collapse();
        verifyStandardIcons(panel.advanced().getLeftIcon(), CollapsiblePanelAttributes.leftCollapsedIcon);
    }

    @Test
    @CoversAttributes("leftExpandedIcon")
    @Templates(value = "plain")
    public void testLeftExpandedIcon() {
        verifyStandardIcons(panel.advanced().getLeftIcon(), CollapsiblePanelAttributes.leftExpandedIcon);
    }

    @Test
    @CoversAttributes({ "onbeforeswitch", "onswitch" })
    public void testOnbeforeswitchOnswitch() {
        testRequestEventsBefore("beforeswitch", "switch");
        Graphene.guardAjax(panel).collapse();
        testRequestEventsAfter("beforeswitch", "switch");
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEvent(Event.CLICK, panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.rendered, Boolean.FALSE);
        assertNotVisible(panel, "Panel should not be rendered when rendered=false.");
    }

    @Test
    @CoversAttributes("rightCollapsedIcon")
    @Templates(value = "plain")
    public void testRightCollapsedIcon() {
        Graphene.guardAjax(panel).collapse();
        verifyStandardIcons(panel.advanced().getRightIcon(), CollapsiblePanelAttributes.rightCollapsedIcon);
    }

    @Test
    @CoversAttributes("rightExpandedIcon")
    @Templates(value = "plain")
    public void testRightExpandedIcon() {
        verifyStandardIcons(panel.advanced().getRightIcon(), CollapsiblePanelAttributes.rightExpandedIcon);
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("switchType")
    @Templates(value = "plain")
    @UseWithField(field = "switchType", valuesFrom = STRINGS, value = { "null", "ajax", "client", "server" })
    @IssueTracking("https://issues.jboss.org/browse/RF-10368")
    public void testSwitchType() {
        super.testSwitchType();
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(panel.advanced().getRootElement());
    }

    @Test
    @CoversAttributes("toggleListener")
    @IssueTracking("https://issues.jboss.org/browse/RF-11568")
    public void testToggleListener() {
        Graphene.guardAjax(panel).collapse();
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "panel collapsed");

        Graphene.guardAjax(panel).expand();
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "panel expanded");
    }

    private void verifyStandardIcons(Icon icon, CollapsiblePanelAttributes attribute) {
        new IconsChecker<CollapsiblePanelAttributes>(driver, collapsiblePanelAttributes).checkAll(attribute,
            icon.getIconDivElement(), icon.getIconImageElement(), true, true);
    }
}
