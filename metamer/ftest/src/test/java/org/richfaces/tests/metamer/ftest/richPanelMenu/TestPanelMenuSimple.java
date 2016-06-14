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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupMode;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.rendered;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebElement;
import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
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
    @CoversAttributes("activeItem")
    public void testActiveItem() {
        // https://issues.jboss.org/browse/RF-13104
        // the activeItem is a String, so it cannot change dynamically to actual selected item
        // TODO: refactor the test after issue RF-13104 is resolved
        List<WebElement> selectedItems = getPage().getPanelMenu().advanced().getAllSelectedItems();
        assertEquals(selectedItems.size(), 0);

        panelMenuAttributes.set(PanelMenuAttributes.activeItem, "item3");
        selectedItems = getPage().getPanelMenu().advanced().getAllSelectedItems();
        assertEquals(selectedItems.size(), 1);
        assertEquals(selectedItems.get(0).getText(), "Item 3");

        panelMenuAttributes.set(PanelMenuAttributes.activeItem, "item1");
        selectedItems = getPage().getPanelMenu().advanced().getAllSelectedItems();
        assertEquals(selectedItems.size(), 1);
        assertEquals(selectedItems.get(0).getText(), "Item 1");

        panelMenuAttributes.set(PanelMenuAttributes.activeItem, "item241");
        getPage().getPanelMenu().expandGroup("Group 2").expandGroup("Group 2.4");
        selectedItems = getPage().getPanelMenu().advanced().getAllSelectedItems();
        assertEquals(selectedItems.size(), 1);
        assertEquals(selectedItems.get(0).getText(), "Item 2.4.1");
    }

    @Test
    @CoversAttributes("disabled")
    @IssueTracking("https://issues.jboss.org/browse/RF-10158")
    public void testDisabled() {
        attsSetter()
            .setAttribute(PanelMenuAttributes.groupMode).toValue("client")
            .setAttribute(PanelMenuAttributes.disabled).toValue(false)
            .asSingleAction().perform();

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
    @Templates(exclude = "uiRepeat")
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
    @Templates("uiRepeat")
    @Skip(On.JSF.VersionMojarraLowerThan2212.class)
    public void testImmediateInUiRepeat() {
        testImmediate();
    }

    @Test
    @CoversAttributes("itemChangeListener")
    @Templates(exclude = "uiRepeat")
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
    @Templates("uiRepeat")
    @Skip(On.JSF.VersionMojarraLowerThan2212.class)
    public void testItemChangeListenerInUiRepeat() {
        testItemChangeListener();
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
    @IssueTracking("https://issues.jboss.org/browse/RF-10302")
    @Templates(value = "plain")
    public void testWidth() {
        attsSetter()
            .setAttribute(PanelMenuAttributes.style).toValue("")
            .setAttribute(PanelMenuAttributes.width).toValue("300px")
            .asSingleAction().perform();
        assertTrue(getPage().getPanelMenu().advanced().getRootElement().getCssValue("width").contains("300px"));
    }
}
