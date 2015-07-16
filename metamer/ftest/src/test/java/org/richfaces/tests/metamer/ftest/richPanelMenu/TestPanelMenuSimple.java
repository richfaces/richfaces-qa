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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupMode;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.itemMode;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.width;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestPanelMenuSimple extends AbstractPanelMenuTest {

    private final Attributes<PanelMenuAttributes> panelMenuAttributes = getAttributes();

    private Boolean expandSingle = true;

    private int expanded(int expanded) {
        return (expandSingle && expanded >= 1) ? 1 : expanded;
    }

    private int getExpandedGroupsCount() {
        return getPage().getPanelMenu().advanced().getAllExpandedGroups().size();
    }

    @Test
    @CoversAttributes("disabled")
    @RegressionTest("https://issues.jboss.org/browse/RF-10158")
    public void testDisabled() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(disabled, false);

        assertEquals(getPage().getPanelMenu().advanced().getAllDisabledGroups().size(), 2);
        assertEquals(getPage().getPanelMenu().advanced().getAllDisabledItems().size(), 3);

        panelMenuAttributes.set(disabled, true);

        assertEquals(getPage().getPanelMenu().advanced().getAllDisabledGroups().size(), 6);
        assertEquals(getPage().getPanelMenu().advanced().getAllDisabledItems().size(), 24);
    }

    @Test
    @CoversAttributes("expandSingle")
    @UseWithField(field = "expandSingle", valuesFrom = FROM_FIELD, value = "booleans")
    @IssueTracking("https://issues.jboss.org/browse/RF-10626")
    public void testExpandSingle() {
        panelMenuAttributes.set(PanelMenuAttributes.expandSingle, expandSingle);

        getPage().getPanelMenu().expandGroup("Group 2");
        assertEquals(getExpandedGroupsCount(), expanded(1));

        getPage().getPanelMenu().expandGroup("Group 1");
        assertEquals(getExpandedGroupsCount(), expanded(2));
    }

    @Test
    @CoversAttributes("groupClass")
    @Templates(value = "plain")
    public void testGroupClass() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        testStyleClass(getPage().getGroup24().advanced().getRootElement(), BasicAttributes.groupClass);
    }

    @Test
    @CoversAttributes("groupDisabledClass")
    @Templates(value = "plain")
    public void testGroupDisabledClass() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        super.testStyleClass(getPage().getGroup26().advanced().getRootElement(), BasicAttributes.groupDisabledClass);
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        panelMenuAttributes.set(PanelMenuAttributes.immediate, true);
        Graphene.guardAjax(getPage().getPanelMenu()).selectItem("Item 1");
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: null -> item1");
        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);

        // select item in sub group
        guardAjax(getPage().getPanelMenu().expandGroup(0)).selectItem(2);
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: item1 -> item13");
        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
    }

    @Test
    @CoversAttributes("itemChangeListener")
    public void testItemChangeListener() {
        guardAjax(getPage().getPanelMenu()).selectItem("Item 1");
        getMetamerPage().assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: null -> item1");
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);

        // select item in sub group
        guardAjax(getPage().getPanelMenu().expandGroup(0)).selectItem(2);
        getMetamerPage().assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: item1 -> item13");
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
    }

    @Test
    @CoversAttributes("itemClass")
    @Templates(value = "plain")
    public void testItemClass() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        super.testStyleClass(getPage().getItem22().advanced().getRootElement(), BasicAttributes.itemClass);
    }

    @Test
    @CoversAttributes("itemDisabledClass")
    @Templates(value = "plain")
    public void testItemDisabledClass() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        super.testStyleClass(getPage().getItem25().advanced().getRootElement(), BasicAttributes.itemDisabledClass);
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiCollapseAll() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        // expand all group manually
        getPage().getPanelMenu().expandGroup("Group 1");
        getPage().getPanelMenu().expandGroup("Group 2");
        getPage().getPanelMenu().expandGroup("Group 2.4");
        getPage().getPanelMenu().expandGroup("Group 3");
        getPage().getCollapseAll().click();

        assertEquals(getPage().getPanelMenu().advanced().getAllExpandedGroups().size(), 0);
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiCollapseAll1() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        // expand all group manually
        getPage().getPanelMenu().expandGroup("Group 1");
        getPage().getPanelMenu().expandGroup("Group 2");
        getPage().getPanelMenu().expandGroup("Group 2.4");
        getPage().getPanelMenu().expandGroup("Group 3");
        getPage().getCollapseAllBtn1().click();

        assertEquals(getPage().getPanelMenu().advanced().getAllExpandedGroups().size(), 0);
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiExpandAll() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        getPage().getExpandAll().click();

        assertEquals(getPage().getPanelMenu().advanced().getAllExpandedGroups().size(), 4);
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiExpandAll1() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        getPage().getExpandAllBtn1().click();

        assertEquals(getPage().getPanelMenu().advanced().getAllExpandedGroups().size(), 4);
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiSelectItem() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        getPage().getSelecItem().click();

        assertEquals(getPage().getSelectedItem().getText(), "item23");
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiSelectItem1() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        getPage().getSelectItemBtn1().click();

        assertEquals(getPage().getSelectedItem().getText(), "item23");
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        panelMenuAttributes.set(rendered, false);
        assertFalse(new WebElementConditionFactory(getPage().getPanelMenu().advanced().getRootElement()).isPresent().apply(driver));
        panelMenuAttributes.set(rendered, true);
        assertTrue(new WebElementConditionFactory(getPage().getPanelMenu().advanced().getRootElement()).isPresent().apply(driver));
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(getPage().getPanelMenu().advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(getPage().getPanelMenu().advanced().getRootElement());
    }

    @Test
    @CoversAttributes("topGroupClass")
    @Templates(value = "plain")
    public void testTopGroupClass() {
        testStyleClass(getPage().getGroup1().advanced().getRootElement(), BasicAttributes.topGroupClass);
    }

    @Test
    @CoversAttributes("topGroupDisabledClass")
    @Templates(value = "plain")
    public void testTopGroupDisabledClass() {
        testStyleClass(getPage().getGroup4().advanced().getRootElement(), BasicAttributes.topGroupDisabledClass);
    }

    @Test
    @CoversAttributes("topItemClass")
    @Templates(value = "plain")
    public void testTopItemClass() {
        testStyleClass(getPage().getItem3().advanced().getRootElement(), BasicAttributes.topItemClass);
    }

    @Test
    @CoversAttributes("topItemDisabledClass")
    @Templates(value = "plain")
    public void testTopItemDisabledClass() {
        testStyleClass(getPage().getItem2().advanced().getRootElement(), BasicAttributes.topItemDisabledClass);
        testStyleClass(getPage().getItem4().advanced().getRootElement(), BasicAttributes.topItemDisabledClass);
    }

    @Test
    @CoversAttributes("width")
    @RegressionTest("https://issues.jboss.org/browse/RF-10302")
    @Templates(value = "plain")
    public void testWidth() {
        panelMenuAttributes.set(PanelMenuAttributes.style, "");
        panelMenuAttributes.set(width, "300px");
        assertTrue(getPage().getPanelMenu().advanced().getRootElement().getCssValue("width").contains("300px"));
    }
}
