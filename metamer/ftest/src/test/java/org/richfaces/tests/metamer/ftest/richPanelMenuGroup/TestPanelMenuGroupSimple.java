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

import static org.richfaces.component.Mode.ajax;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.disabledClass;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.data;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.leftCollapsedIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.leftDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.leftExpandedIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.limitRender;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.render;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rightCollapsedIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rightDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rightExpandedIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.selectable;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.status;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuGroupAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.checker.IconsCheckerWebdriver;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuGroupSimple extends AbstractPanelMenuGroupTest {

    @Test
    public void testData() {
        final String RF_STRING = "RichFaces 4";
        panelMenuGroupAttributes.set(data, RF_STRING);
        panelMenuGroupAttributes.set(oncomplete, "data = event.data");

        String requestTime = page.getRequestTimeElement().getText();
        page.topGroup.toggle();
        Graphene.waitModel().until().element(page.getRequestTimeElement()).text().not().equals(requestTime);

        assertEquals(expectedReturnJS("return window.data", RF_STRING), RF_STRING);
    }

    @Test
    public void testDisabled() {
        page.topGroup.setMode(null);
        assertFalse(page.topGroup.isDisabled());

        panelMenuGroupAttributes.set(disabled, true);

        assertFalse(page.topGroup.isSelected());
        assertTrue(page.topGroup.isDisabled());

        Graphene.guardNoRequest(page.topGroup).toggle();

        assertFalse(page.topGroup.isSelected());
        assertTrue(page.topGroup.isDisabled());
    }

    @Test
    @Templates(value = "plain")
    public void testDisabledClass() {
        panelMenuGroupAttributes.set(disabled, true);
        testStyleClass(page.topGroup.getRoot(), disabledClass);
    }

    @Test
    @Templates(value = "plain")
    public void testLeftDisabledIcon() {
        panelMenuGroupAttributes.set(disabled, true);

        verifyStandardIcons(leftDisabledIcon, page.topGroup.getLeftIcon().getIcon(), page.topGroup.getLeftIcon().getIcon(), "");
    }

    @Test
    @Templates(value = "plain")
    public void testLeftCollapsedIcon() {
        page.topGroup.toggle();

        verifyStandardIcons(leftCollapsedIcon, page.topGroup.getLeftIcon().getIcon(), page.topGroup.getLeftIcon().getIcon(), "");

        panelMenuGroupAttributes.set(disabled, true);
        // both icon should be "transparent" - invisible
        assertTrue(page.topGroup.getLeftIcon().isTransparent(page.topGroup.getLeftIcon().getIcon()));
    }

    @Test
    @Templates(value = "plain")
    public void testLeftExpandedIcon() {

        verifyStandardIcons(leftExpandedIcon, page.topGroup.getLeftIcon().getIcon(), page.topGroup.getLeftIcon().getIcon(), "");

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(page.topGroup.getRightIcon().isTransparent(page.topGroup.getRightIcon().getIcon()));
    }

    @Test
    public void testLimitRender() {
        panelMenuGroupAttributes.set(render, "renderChecker");
        panelMenuGroupAttributes.set(limitRender, true);

        String requestTime = page.getRequestTimeElement().getText();
        String renderCheckerTime = page.getRenderCheckerOutputElement().getText();

        page.topGroup.toggle();

        Graphene.waitModel().until("Page was not updated").element(page.getRenderCheckerOutputElement()).text().not().equalTo(renderCheckerTime);
        assertEquals(page.getRequestTimeElement().getText(), requestTime);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        assertTrue(page.topGroup.isVisible());

        panelMenuGroupAttributes.set(rendered, false);

        assertFalse(page.topGroup.isVisible());
    }

    @Test
    @Templates(value = "plain")
    public void testRightDisabledIcon() {
        panelMenuGroupAttributes.set(disabled, true);

        verifyStandardIcons(rightDisabledIcon, page.topGroup.getRightIcon().getIcon(), page.topGroup.getRightIcon().getIcon(), "");
    }

    @Test
    @Templates(value = "plain")
    public void testRightExpandedIcon() {
        verifyStandardIcons(rightExpandedIcon, page.topGroup.getRightIcon().getIcon(), page.topGroup.getRightIcon().getIcon(), "");

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(page.topGroup.getRightIcon().isTransparent(page.topGroup.getRightIcon().getIcon()));
    }

    @Test
    @Templates(value = "plain")
    public void testRightCollapsedIcon() {
        page.topGroup.toggle();

        verifyStandardIcons(rightCollapsedIcon, page.topGroup.getRightIcon().getIcon(), page.topGroup.getRightIcon().getIcon(), "");

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(page.topGroup.getRightIcon().isTransparent(page.topGroup.getRightIcon().getIcon()));
    }

    @Test
    public void testSelectable() {
        page.topGroup.setMode(ajax);

        panelMenuGroupAttributes.set(selectable, false);
        page.topGroup.toggle();
        assertFalse(page.topGroup.isSelected());

        panelMenuGroupAttributes.set(selectable, true);
        page.topGroup.toggle();
        assertTrue(page.topGroup.isSelected());
    }

    @Test
    public void testStatus() {
        panelMenuGroupAttributes.set(status, "statusChecker");

        String statusCheckerTime = page.getStatusCheckerOutputElement().getText();
        page.topGroup.toggle();
        Graphene.waitModel().until("Page was not updated").element(page.getStatusCheckerOutputElement()).text().not().equalTo(statusCheckerTime);
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.topGroup.getRoot());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10485")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.topGroup.getRoot());
    }

    private void verifyStandardIcons(PanelMenuGroupAttributes attribute, WebElement icon, WebElement imgIcon, String classSuffix) {
        IconsCheckerWebdriver<PanelMenuGroupAttributes> checker = new IconsCheckerWebdriver<PanelMenuGroupAttributes>(
            driver, panelMenuGroupAttributes, "rf-ico-", "");

        checker.checkCssImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkCssNoImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), imgIcon, classSuffix, false);
        checker.checkNone(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);

    }
}
