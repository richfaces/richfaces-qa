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
package org.richfaces.tests.metamer.ftest.richPanelMenuItem;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.disabledClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.leftIconClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.rightIconClass;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.leftDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.leftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.mode;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.rightDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.rightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.selectable;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuItemSimple extends AbstractWebDriverTest {

    @Page
    private PanelMenuItemPage page;
    private final Attributes<PanelMenuItemAttributes> panelMenuItemAttributes = getAttributes();

    @Override
    public String getComponentTestPagePath() {
        return "richPanelMenuItem/simple.xhtml";
    }

    @BeforeMethod(alwaysRun = true)
    public void setupMode() {
        panelMenuItemAttributes.set(mode, PanelMenuMode.ajax);
    }

    @Test
    @CoversAttributes({ "action", "actionListener" })
    public void testActionAndActionListener() {
        guardAjax(page.getItem()).select();

        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action listener invoked");
        page.assertListener(PhaseId.INVOKE_APPLICATION, "action invoked");
    }

    @Test(groups = "smoke")
    @CoversAttributes("data")
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                page.getItem().select();
            }
        });
    }

    @Test
    @CoversAttributes("disabled")
    public void testDisabled() {
        // unselect tested item
        guardAjax(page.getPanelMenu()).selectItem(0);

        assertFalse(page.getItem().advanced().isDisabled());

        panelMenuItemAttributes.set(disabled, true);

        assertFalse(page.getItem().advanced().isSelected());
        assertTrue(page.getItem().advanced().isDisabled());

        guardNoRequest(page.getItem()).select();

        assertFalse(page.getItem().advanced().isSelected());
        assertTrue(page.getItem().advanced().isDisabled());
    }

    @Test
    @CoversAttributes("disabledClass")
    @Templates(value = "plain")
    public void testDisabledClass() {
        panelMenuItemAttributes.set(disabled, true);
        testStyleClass(page.getItem().advanced().getRootElement(), disabledClass);
    }

    @Test
    @CoversAttributes("label")
    @Templates(value = "plain")
    public void testLabel() {
        String testedVal = "completely new label";
        panelMenuItemAttributes.set(PanelMenuItemAttributes.label, testedVal);
        assertEquals(page.getItem().advanced().getRootElement().getText().trim(), testedVal);
    }

    @Test
    @CoversAttributes("leftDisabledIcon")
    @Templates(value = "plain")
    public void testLeftDisabledIcon() {
        panelMenuItemAttributes.set(disabled, true);
        verifyStandardIcons(leftDisabledIcon, page.getItem().advanced().getLeftIconElement(), page.getItem().advanced()
            .getLeftIconImgElement());
    }

    @Test
    @CoversAttributes("leftIcon")
    @Templates(value = "plain")
    public void testLeftIcon() {
        verifyStandardIcons(leftIcon, page.getItem().advanced().getLeftIconElement(), page.getItem().advanced().getLeftIconImgElement());

        panelMenuItemAttributes.set(disabled, true);
        assertTrue(page.getItem().advanced().isTransparent(page.getItem().advanced().getLeftIconElement()));
    }

    @Test
    @CoversAttributes("leftIconClass")
    @Templates(value = "plain")
    public void testLeftIconClass() {
        testStyleClass(page.getItem().advanced().getLeftIconElement(), leftIconClass);
    }

    @Test(groups = "smoke")
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        attsSetter()
            .setAttribute(PanelMenuItemAttributes.render).toValue("renderChecker")
            .setAttribute(PanelMenuItemAttributes.limitRender).toValue(true)
            .asSingleAction().perform();

        String renderChecker = page.getRenderCheckerOutputElement().getText();
        MetamerPage.requestTimeNotChangesWaiting(page.getItem()).select();
        Graphene.waitModel().until("Page was not updated").element(page.getRenderCheckerOutputElement()).text().not()
            .equalTo(renderChecker);
    }

    @Test
    @CoversAttributes("name")
    @Templates(value = "plain")
    public void testName() {
        String testedVal = "itemWithNewName";
        panelMenuItemAttributes.set(PanelMenuItemAttributes.name, testedVal);
        guardAjax(page.getItem()).select();
        assertEquals(page.getSelectedItemOutputText(), testedVal);
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        testRender(new Action() {
            @Override
            public void perform() {
                guardAjax(page.getItem()).select();
            }
        });
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        assertTrue(new WebElementConditionFactory(page.getItem().advanced().getRootElement()).isVisible().apply(driver));

        panelMenuItemAttributes.set(rendered, false);

        assertFalse(new WebElementConditionFactory(page.getItem().advanced().getRootElement()).isVisible().apply(driver));
    }

    @Test
    @CoversAttributes("rightDisabledIcon")
    @Templates(value = "plain")
    public void testRightDisabledIcon() {
        panelMenuItemAttributes.set(disabled, true);
        verifyStandardIcons(rightDisabledIcon, page.getItem().advanced().getRightIconElement(), page.getItem().advanced()
            .getRightIconImgElement());
    }

    @Test
    @CoversAttributes("rightIcon")
    @RegressionTest("https://issues.jboss.org/browse/RF-10519")
    @Templates(value = "plain")
    public void testRightIcon() {
        verifyStandardIcons(rightIcon, page.getItem().advanced().getRightIconElement(), page.getItem().advanced().getRightIconImgElement());

        panelMenuItemAttributes.set(disabled, true);
        assertTrue(page.getItem().advanced().isTransparent(page.getItem().advanced().getRightIconElement()));
    }

    @Test
    @CoversAttributes("rightIconClass")
    @RegressionTest("https://issues.jboss.org/browse/RF-10519")
    @Templates(value = "plain")
    public void testRightIconClass() {
        testStyleClass(page.getItem().advanced().getRightIconElement(), rightIconClass);
    }

    @Test
    @CoversAttributes("selectable")
    public void testSelectable() {
        // unselect tested item
        guardAjax(page.getPanelMenu()).selectItem(0);

        panelMenuItemAttributes.set(selectable, false);
        guardNoRequest(page.getItem()).select();
        assertFalse(page.getItem().advanced().isSelected());

        panelMenuItemAttributes.set(selectable, true);
        guardAjax(page.getItem()).select();
        assertTrue(page.getItem().advanced().isSelected());
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        panelMenuItemAttributes.set(status, "statusChecker");

        String statusChecker = page.getStatusCheckerOutputElement().getText();
        page.getItem().select();
        Graphene.waitAjax().until("Page was not updated").element(page.getStatusCheckerOutputElement()).text().not()
            .equalTo(statusChecker);
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getItem().advanced().getRootElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getItem().advanced().getRootElement());
    }

    private void verifyStandardIcons(PanelMenuItemAttributes attribute, WebElement icon, WebElement imgIcon) {
        new IconsChecker<PanelMenuItemAttributes>(driver, panelMenuItemAttributes).checkAll(attribute, icon, imgIcon, false, false);
    }
}
