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
package org.richfaces.tests.metamer.ftest.richPanelMenuItem;

import static org.jboss.arquillian.ajocado.Graphene.waitAjax;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.disabledClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.leftIconClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.rightIconClass;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.panelMenuItemAttributes;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.data;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.limitRender;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.mode;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.render;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.selectable;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.status;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.test.selenium.GuardRequest;
import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.richfaces.tests.metamer.ftest.model.PanelMenu;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22751 $
 */
public class TestPanelMenuItemSimple extends AbstractGrapheneTest {

    private static String sampleImage = "/resources/images/loading.gif";
    private static String chevronDown = "chevronDown";
    private static String chevronDownClass = "rf-ico-chevron-down";

    PanelMenu menu = new PanelMenu(pjq("div.rf-pm[id$=panelMenu]"));
    PanelMenu.Item item = menu.getGroup(1).getItemContains("Item 1.2");
    PanelMenu.Icon leftIcon = item.getLeftIcon();
    PanelMenu.Icon rightIcon = item.getRightIcon();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuItem/simple.xhtml");
    }

    @BeforeMethod
    public void setupMode() {
        panelMenuItemAttributes.set(mode, PanelMenuMode.ajax);
        menu.setItemMode(PanelMenuMode.ajax);
    }

    @Test
    public void testData() {
        panelMenuItemAttributes.set(data, "RichFaces 4");
        panelMenuItemAttributes.set(oncomplete, "data = event.data");

        retrieveRequestTime.initializeValue();
        item.select();
        waitGui.waitForChange(retrieveRequestTime);

        assertEquals(retrieveWindowData.retrieve(), "RichFaces 4");
    }

    @Test
    public void testDisabled() {
        menu.setItemMode(null);
        assertFalse(item.isDisabled());

        panelMenuItemAttributes.set(disabled, true);

        assertFalse(item.isSelected());
        assertTrue(item.isDisabled());

        new GuardRequest(RequestType.NONE) {
            public void command() {
                item.select();
            }
        }.waitRequest();

        assertFalse(item.isSelected());
        assertTrue(item.isDisabled());
    }

    @Test
    public void testDisabledClass() {
        panelMenuItemAttributes.set(disabled, true);
        super.testStyleClass(item, disabledClass);
    }

    @Test
    public void testLeftDisabledIcon() {
        panelMenuItemAttributes.set(disabled, true);
        JQueryLocator input = pjq("select[id$=leftDisabledIcon]");
        ElementLocator<JQueryLocator> icon = leftIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(leftIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testLeftIcon() {
        JQueryLocator input = pjq("select[id$=leftIcon]");
        ElementLocator<JQueryLocator> icon = leftIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(leftIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");

        panelMenuItemAttributes.set(disabled, true);
        assertTrue(leftIcon.isTransparent());
    }

    @Test
    public void testLeftIconClass() {
        super.testStyleClass(leftIcon, leftIconClass);
    }

    @Test
    public void testLimitRender() {
        panelMenuItemAttributes.set(render, "renderChecker");
        panelMenuItemAttributes.set(limitRender, true);

        retrieveRequestTime.initializeValue();
        retrieveRenderChecker.initializeValue();
        item.select();
        waitAjax.waitForChange(retrieveRenderChecker);
        assertFalse(retrieveRequestTime.isValueChanged());
    }

    @Test
    public void testRendered() {
        assertTrue(item.isVisible());

        panelMenuItemAttributes.set(rendered, false);

        assertFalse(item.isVisible());
    }

    @Test
    public void testRightDisabledIcon() {
        panelMenuItemAttributes.set(disabled, true);
        JQueryLocator input = pjq("select[id$=rightDisabledIcon]");
        ElementLocator<JQueryLocator> icon = rightIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(rightIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10519")
    public void testRightIcon() {
        JQueryLocator input = pjq("select[id$=rightIcon]");
        ElementLocator<JQueryLocator> icon = rightIcon.getIcon();
        ElementLocator<JQueryLocator> image = jq(rightIcon.getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");

        panelMenuItemAttributes.set(disabled, true);
        assertTrue(rightIcon.isTransparent());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10519")
    public void testRightIconClass() {
        super.testStyleClass(rightIcon, rightIconClass);
    }

    @Test
    public void testSelectable() {
        menu.setItemMode(null);
        panelMenuItemAttributes.set(selectable, false);

        new GuardRequest(RequestType.NONE) {
            public void command() {
                item.select();
            }
        }.waitRequest();

        assertFalse(item.isSelected());

        panelMenuItemAttributes.set(selectable, true);

        new GuardRequest(RequestType.XHR) {
            public void command() {
                item.select();
            }
        }.waitRequest();

        assertTrue(item.isSelected());
    }

    @Test
    public void testStatus() {
        panelMenuItemAttributes.set(status, "statusChecker");

        retrieveStatusChecker.initializeValue();
        item.select();
        waitAjax.waitForChange(retrieveStatusChecker);
    }

    @Test
    public void testStyle() {
        super.testStyle(item);
    }

    @Test
    public void testStyleClass() {
        super.testStyleClass(item);
    }

    private void verifyStandardIcons(ElementLocator<JQueryLocator> input, ElementLocator<JQueryLocator> icon, ElementLocator<JQueryLocator> image, String classSuffix) {
        IconsChecker checker = new IconsChecker(selenium, "rf-ico-", "");
        checker.checkCssImageIcons(input, icon, classSuffix);
        checker.checkCssNoImageIcons(input, icon, classSuffix);
        checker.checkImageIcons(input, icon, image, classSuffix, false);
        checker.checkNone(input, icon, classSuffix);
    }
}
