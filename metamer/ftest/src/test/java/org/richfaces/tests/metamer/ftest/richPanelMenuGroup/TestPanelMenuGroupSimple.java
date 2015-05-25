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
import static org.richfaces.tests.metamer.ftest.BasicAttributes.disabledClass;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.leftCollapsedIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.leftDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.leftExpandedIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rightCollapsedIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rightDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rightExpandedIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.selectable;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.BecauseOf;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuGroupSimple extends AbstractPanelMenuGroupTest {

    private final Attributes<PanelMenuGroupAttributes> panelMenuGroupAttributes = getAttributes();

    final Action collapseFirstGroupAction = new Action() {
        @Override
        public void perform() {
            Graphene.guardAjax(getPage().getMenu()).collapseGroup(1);
        }
    };

    @Test
    @CoversAttributes("action")
    public void testAction() {
        collapseFirstGroupAction.perform();
        getPage().assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
    }

    @Test
    @CoversAttributes("actionListener")
    public void testActionListener() {
        collapseFirstGroupAction.perform();
        getPage().assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(collapseFirstGroupAction);
    }

    @Test
    @CoversAttributes("disabled")
    public void testDisabled() {
        assertFalse(getPage().getTopGroup().advanced().isDisabled());

        panelMenuGroupAttributes.set(disabled, true);

        assertFalse(getPage().getTopGroup().advanced().isSelected());
        assertTrue(getPage().getTopGroup().advanced().isDisabled());

        Graphene.guardNoRequest(getPage().getTopGroup().advanced().getHeaderElement()).click();

        assertFalse(getPage().getTopGroup().advanced().isSelected());
        assertTrue(getPage().getTopGroup().advanced().isDisabled());
    }

    @Test
    @CoversAttributes("disabledClass")
    @Templates(value = "plain")
    public void testDisabledClass() {
        panelMenuGroupAttributes.set(disabled, true);
        testStyleClass(getPage().getTopGroup().advanced().getRootElement(), disabledClass);
    }

    @Test
    @CoversAttributes("leftCollapsedIcon")
    @Templates(value = "plain")
    public void testLeftCollapsedIcon() {
        Graphene.guardAjax(getPage().getMenu()).collapseGroup(1);

        verifyStandardIcons(leftCollapsedIcon, getPage().getTopGroup().advanced().getLeftIconElement(), getPage().getTopGroup().advanced().getLeftIconElement());

        panelMenuGroupAttributes.set(disabled, true);
        // both icon should be "transparent" - invisible
        assertTrue(getPage().getTopGroup().advanced().isTransparent(getPage().getTopGroup().advanced().getLeftIconElement()));
    }

    @Test
    @CoversAttributes("leftDisabledIcon")
    @Templates(value = "plain")
    public void testLeftDisabledIcon() {
        panelMenuGroupAttributes.set(disabled, true);

        verifyStandardIcons(leftDisabledIcon, getPage().getTopGroup().advanced().getLeftIconElement(), getPage().getTopGroup().advanced().getLeftIconElement());
    }

    @Test
    @CoversAttributes("leftExpandedIcon")
    @Templates(value = "plain")
    public void testLeftExpandedIcon() {

        verifyStandardIcons(leftExpandedIcon, getPage().getTopGroup().advanced().getLeftIconElement(), getPage().getTopGroup().advanced().getLeftIconElement());

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(getPage().getTopGroup().advanced().isTransparent(getPage().getTopGroup().advanced().getRightIconElement()));
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        testLimitRender(collapseFirstGroupAction);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        assertTrue(new WebElementConditionFactory(getPage().getTopGroup().advanced().getRootElement()).isVisible().apply(driver));

        panelMenuGroupAttributes.set(rendered, false);

        assertFalse(new WebElementConditionFactory(getPage().getTopGroup().advanced().getRootElement()).isVisible().apply(driver));
    }

    @Test
    @CoversAttributes("rightCollapsedIcon")
    @Templates(value = "plain")
    public void testRightCollapsedIcon() {
        Graphene.guardAjax(getPage().getMenu()).collapseGroup(1);

        verifyStandardIcons(rightCollapsedIcon, getPage().getTopGroup().advanced().getRightIconElement(), getPage().getTopGroup().advanced().getRightIconElement());

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(getPage().getTopGroup().advanced().isTransparent(getPage().getTopGroup().advanced().getRightIconElement()));
    }

    @Test
    @CoversAttributes("rightDisabledIcon")
    @Templates(value = "plain")
    public void testRightDisabledIcon() {
        panelMenuGroupAttributes.set(disabled, true);

        verifyStandardIcons(rightDisabledIcon, getPage().getTopGroup().advanced().getRightIconElement(), getPage().getTopGroup().advanced().getRightIconElement());
    }

    @Test
    @CoversAttributes("rightExpandedIcon")
    @Templates(value = "plain")
    public void testRightExpandedIcon() {
        verifyStandardIcons(rightExpandedIcon, getPage().getTopGroup().advanced().getRightIconElement(), getPage().getTopGroup().advanced().getRightIconElement());

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(getPage().getTopGroup().advanced().isTransparent(getPage().getTopGroup().advanced().getRightIconElement()));
    }

    @Test
    @CoversAttributes("selectable")
    @Templates(exclude = "uiRepeat")
    public void testSelectable() {
        panelMenuGroupAttributes.set(selectable, false);
        guardAjax(getPage().getMenu()).collapseGroup(1);
        assertFalse(getPage().getTopGroup().advanced().isSelected());

        panelMenuGroupAttributes.set(selectable, true);
        guardAjax(getPage().getMenu()).expandGroup(1);
        assertTrue(getPage().getTopGroup().advanced().isSelected());
    }

    @Test // fails with JSF 2.2
    @Skip(BecauseOf.UIRepeatSetIndexIssue.class)
    @CoversAttributes("selectable")
    @Templates(value = "uiRepeat")
    @IssueTracking("https://issues.jboss.org/browse/RF-13727")
    public void testSelectableInUirepeat() {
        testSelectable();
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        testStatus(collapseFirstGroupAction);
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(getPage().getTopGroup().advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @RegressionTest("https://issues.jboss.org/browse/RF-10485")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(getPage().getTopGroup().advanced().getRootElement());
    }

    private void verifyStandardIcons(PanelMenuGroupAttributes attribute, WebElement icon, WebElement imgIcon) {
        new IconsChecker<PanelMenuGroupAttributes>(driver, panelMenuGroupAttributes).checkAll(attribute, icon, imgIcon, false, true);
    }
}
