/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.disabledClass;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.panelMenuGroupAttributes;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.data;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.limitRender;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.render;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.selectable;
import static org.richfaces.tests.metamer.ftest.richPanelMenuGroup.PanelMenuGroupAttributes.status;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.test.selenium.GuardRequest;
import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22750 $
 */
public class TestPanelMenuGroupSimple extends AbstractPanelMenuGroupTest {

    private static String sampleImage = "/resources/images/loading.gif";
    private static String chevronDown = "chevronDown";
    private static String chevronDownClass = "rf-ico-chevron-down";

    @Test
    public void testData() {
        panelMenuGroupAttributes.set(data, "RichFaces 4");
        panelMenuGroupAttributes.set(oncomplete, "data = event.data");

        retrieveRequestTime.initializeValue();
        topGroup.toggle();
        waitGui.waitForChange(retrieveRequestTime);

        assertEquals(retrieveWindowData.retrieve(), "RichFaces 4");
    }

    @Test
    public void testDisabled() {
        menu.setGroupMode(null);
        assertFalse(topGroup.isDisabled());

        panelMenuGroupAttributes.set(disabled, true);

        assertFalse(topGroup.isSelected());
        assertTrue(topGroup.isDisabled());

        new GuardRequest(RequestType.NONE) {
            public void command() {
                topGroup.toggle();
            }
        }.waitRequest();

        assertFalse(topGroup.isSelected());
        assertTrue(topGroup.isDisabled());
    }

    @Test
    public void testDisabledClass() {
        panelMenuGroupAttributes.set(disabled, true);
        super.testStyleClass(topGroup, disabledClass);
    }

    @Test
    public void testLeftDisabledIcon() {
        panelMenuGroupAttributes.set(disabled, true);
        JQueryLocator input = pjq("select[id$=lefttDisabledIcon]");
        ElementLocator<JQueryLocator> icon = leftIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(leftIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testLeftCollapsedIcon() {
        topGroup.toggle();
        JQueryLocator input = pjq("select[id$=leftCollapsedIcon]");
        ElementLocator<JQueryLocator> icon = leftIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(leftIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(leftIcon.isTransparent());
    }

    @Test
    public void testLeftExpandedIcon() {
        JQueryLocator input = pjq("select[id$=leftExpandedIcon]");
        ElementLocator<JQueryLocator> icon = leftIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(leftIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(rightIcon.isTransparent());
    }

    @Test
    public void testLimitRender() {
        panelMenuGroupAttributes.set(render, "renderChecker");
        panelMenuGroupAttributes.set(limitRender, true);

        retrieveRequestTime.initializeValue();
        retrieveRenderChecker.initializeValue();
        topGroup.toggle();
        waitAjax.waitForChange(retrieveRenderChecker);
        assertFalse(retrieveRequestTime.isValueChanged());
    }

    @Test
    public void testRendered() {
        assertTrue(topGroup.isVisible());

        panelMenuGroupAttributes.set(rendered, false);

        assertFalse(topGroup.isVisible());
    }

    @Test
    public void testRightDisabledIcon() {
        panelMenuGroupAttributes.set(disabled, true);
        JQueryLocator input = pjq("select[id$=rightDisabledIcon]");
        ElementLocator<JQueryLocator> icon = rightIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(rightIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testRightExpandedIcon() {
        JQueryLocator input = pjq("select[id$=rightExpandedIcon]");
        ElementLocator<JQueryLocator> icon = rightIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(rightIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(rightIcon.isTransparent());
    }

    @Test
    public void testRightCollapsedIcon() {
        topGroup.toggle();
        JQueryLocator input = pjq("select[id$=rightCollapsedIcon]");
        ElementLocator<JQueryLocator> icon = rightIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(rightIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");

        panelMenuGroupAttributes.set(disabled, true);
        assertTrue(rightIcon.isTransparent());
    }

    @Test
    public void testSelectable() {
        menu.setGroupMode(PanelMenuMode.ajax);

        panelMenuGroupAttributes.set(selectable, false);
        topGroup.toggle();
        assertFalse(topGroup.isSelected());

        panelMenuGroupAttributes.set(selectable, true);
        topGroup.toggle();
        assertTrue(topGroup.isSelected());
    }

    @Test
    public void testStatus() {
        panelMenuGroupAttributes.set(status, "statusChecker");

        retrieveStatusChecker.initializeValue();
        topGroup.toggle();
        waitAjax.waitForChange(retrieveStatusChecker);
    }

    @Test
    public void testStyle() {
        super.testStyle(topGroup);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10485")
    public void testStyleClass() {
        super.testStyleClass(topGroup);
    }

    private void verifyStandardIcons(ElementLocator<JQueryLocator> input, ElementLocator<JQueryLocator> icon, ElementLocator<JQueryLocator> image, String classSuffix) {
        IconsChecker checker = new IconsChecker(selenium, "rf-ico-", "");
        checker.checkCssImageIcons(input, icon, classSuffix);
        checker.checkCssNoImageIcons(input, icon, classSuffix);
        checker.checkImageIcons(input, icon, image, classSuffix, false);
        checker.checkNone(input, icon, classSuffix);
    }
}
