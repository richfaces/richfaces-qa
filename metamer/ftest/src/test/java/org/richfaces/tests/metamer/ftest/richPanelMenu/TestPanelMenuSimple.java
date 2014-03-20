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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupMode;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.itemMode;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.width;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22749 $
 */
public class TestPanelMenuSimple extends AbstractPanelMenuTest {

    private final Attributes<PanelMenuAttributes> panelMenuAttributes = getAttributes();

    private Boolean expandSingle = true;

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10158")
    public void testDisabled() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(disabled, false);

        assertEquals(page.getPanelMenu().advanced().getAllDisabledGroups().size(), 2);
        assertEquals(page.getPanelMenu().advanced().getAllDisabledItems().size(), 3);

        panelMenuAttributes.set(disabled, true);

        assertEquals(page.getPanelMenu().advanced().getAllDisabledGroups().size(), 6);
        assertEquals(page.getPanelMenu().advanced().getAllDisabledItems().size(), 24);
    }

    @Test
    @UseWithField(field = "expandSingle", valuesFrom = FROM_FIELD, value = "booleans")
    @IssueTracking("https://issues.jboss.org/browse/RF-10626")
    public void testExpandSingle() {
        panelMenuAttributes.set(PanelMenuAttributes.expandSingle, expandSingle);

        page.getPanelMenu().expandGroup("Group 2");
        assertEquals(getExpandedGroupsCount(), expanded(1));

        page.getPanelMenu().expandGroup("Group 1");
        assertEquals(getExpandedGroupsCount(), expanded(2));
    }

    @Test
    @Templates(value = "plain")
    public void testGroupClass() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        testStyleClass(page.getGroup24().advanced().getRootElement(), BasicAttributes.groupClass);
    }

    @Test
    @Templates(value = "plain")
    public void testGroupDisabledClass() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        super.testStyleClass(page.getGroup26().advanced().getRootElement(), BasicAttributes.groupDisabledClass);
    }

    @Test
    @Templates(value = "plain")
    public void testItemClass() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        super.testStyleClass(page.getItem22().advanced().getRootElement(), BasicAttributes.itemClass);
    }

    @Test
    @Templates(value = "plain")
    public void testItemDisabledClass() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        super.testStyleClass(page.getItem25().advanced().getRootElement(), BasicAttributes.itemDisabledClass);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        panelMenuAttributes.set(rendered, false);
        assertFalse(new WebElementConditionFactory(page.getPanelMenu().advanced().getRootElement()).isPresent().apply(driver));
        panelMenuAttributes.set(rendered, true);
        assertTrue(new WebElementConditionFactory(page.getPanelMenu().advanced().getRootElement()).isPresent().apply(driver));
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getPanelMenu().advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getPanelMenu().advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testTopGroupClass() {
        testStyleClass(page.getGroup1().advanced().getRootElement(), BasicAttributes.topGroupClass);
    }

    @Test
    @Templates(value = "plain")
    public void testTopItemClass() {
        testStyleClass(page.getItem3().advanced().getRootElement(), BasicAttributes.topItemClass);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10302")
    public void testWidth() {
        panelMenuAttributes.set(PanelMenuAttributes.style, "");
        panelMenuAttributes.set(width, "300px");
        assertTrue(page.getPanelMenu().advanced().getRootElement().getCssValue("width").contains("300px"));
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiExpandAll() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        page.getExpandAll().click();

        assertEquals(page.getPanelMenu().advanced().getAllExpandedGroups().size(), 4);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiExpandAll1() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        page.getExpandAllBtn1().click();

        assertEquals(page.getPanelMenu().advanced().getAllExpandedGroups().size(), 4);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiCollapseAll() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        // expand all group manually
        page.getPanelMenu().expandGroup("Group 1");
        page.getPanelMenu().expandGroup("Group 2");
        page.getPanelMenu().expandGroup("Group 2.4");
        page.getPanelMenu().expandGroup("Group 3");
        page.getCollapseAll().click();

        assertEquals(page.getPanelMenu().advanced().getAllExpandedGroups().size(), 0);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiCollapseAll1() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        // expand all group manually
        page.getPanelMenu().expandGroup("Group 1");
        page.getPanelMenu().expandGroup("Group 2");
        page.getPanelMenu().expandGroup("Group 2.4");
        page.getPanelMenu().expandGroup("Group 3");
        page.getCollapseAllBtn1().click();

        assertEquals(page.getPanelMenu().advanced().getAllExpandedGroups().size(), 0);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiSelectItem() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        page.getSelecItem().click();

        assertEquals(page.getSelectedItem().getText(), "item23");
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12778")
    public void testJsApiSelectItem1() {
        panelMenuAttributes.set(groupMode, PanelMenuMode.client);
        panelMenuAttributes.set(itemMode, PanelMenuMode.client);

        page.getSelectItemBtn1().click();

        assertEquals(page.getSelectedItem().getText(), "item23");
    }

    private int getExpandedGroupsCount() {
        return page.getPanelMenu().advanced().getAllExpandedGroups().size();
    }

    private int expanded(int expanded) {
        return (expandSingle && expanded >= 1) ? 1 : expanded;
    }
}
