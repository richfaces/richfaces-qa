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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.openqa.selenium.WebElement;
import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.checker.IconsCheckerWebdriver;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1.Final
 */
public class TestPanelMenuIcon extends AbstractPanelMenuTest {

    private final Attributes<PanelMenuAttributes> panelMenuAttributes = getAttributes();

    @BeforeMethod(alwaysRun = true)
    public void initializePage() {
        // to keep group 2 expanded after setting of the panelMenu attribute (HTTP request), so the icons are present
        // WARNING it depends on the right methods call order, super.BeforeMethods should be called firstly
        panelMenuAttributes.set(PanelMenuAttributes.groupMode, Mode.ajax);
    }

    @Test
    @Templates("plain")
    public void testGroupCollapsedLeftIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(PanelMenuAttributes.groupCollapsedLeftIcon, getPage().getGroup24().advanced().getLeftIconElement(), getPage().getGroup24().advanced()
            .getLeftIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testGroupCollapsedRightIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(PanelMenuAttributes.groupCollapsedRightIcon, getPage().getGroup24().advanced().getRightIconElement(), getPage().getGroup24().advanced()
            .getRightIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testGroupDisabledLeftIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        // for disabled icon both icons (expanded and collapsed) should be the same (state depends on implicit group settings)
        verifyStandardIcons(PanelMenuAttributes.groupDisabledLeftIcon, getPage().getGroup26().advanced().getLeftIconElement(), getPage().getGroup26().advanced()
            .getLeftIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testGroupDisabledRightIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(PanelMenuAttributes.groupDisabledRightIcon, getPage().getGroup26().advanced().getRightIconElement(), getPage().getGroup26().advanced()
            .getRightIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testGroupExpandedLeftIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2.4");
        verifyStandardIcons(PanelMenuAttributes.groupExpandedLeftIcon, getPage().getGroup24().advanced().getLeftIconElement(), getPage().getGroup24().advanced()
            .getLeftIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testGroupExpandedRightIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2.4");
        verifyStandardIcons(PanelMenuAttributes.groupExpandedRightIcon, getPage().getGroup24().advanced().getRightIconElement(), getPage().getGroup24().advanced()
            .getRightIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testItemDisabledLeftIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(PanelMenuAttributes.itemDisabledLeftIcon, getPage().getItem25().advanced().getLeftIconElement(), getPage().getItem25().advanced()
            .getLeftIconImgElement(), "");
    }

    @Test
    @Templates("plain")
    public void testItemDisabledRightIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(PanelMenuAttributes.itemDisabledRightIcon, getPage().getItem25().advanced().getRightIconElement(), getPage().getItem25().advanced()
            .getRightIconImgElement(), "");
    }

    @Test
    @Templates("plain")
    public void testItemLeftIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(PanelMenuAttributes.itemLeftIcon, getPage().getItem22().advanced().getLeftIconElement(), getPage().getItem22().advanced()
            .getLeftIconImgElement(), "");
    }

    @Test
    @Templates("plain")
    public void testItemRightIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(PanelMenuAttributes.itemRightIcon, getPage().getItem22().advanced().getRightIconElement(), getPage().getItem22().advanced()
            .getRightIconImgElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopGroupCollapsedLeftIcon() {
        verifyStandardIcons(PanelMenuAttributes.topGroupCollapsedLeftIcon, getPage().getGroup1().advanced().getLeftIconElement(), getPage().getGroup1().advanced()
            .getLeftIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopGroupCollapsedRightIcon() {
        verifyStandardIcons(PanelMenuAttributes.topGroupCollapsedRightIcon, getPage().getGroup1().advanced().getRightIconElement(), getPage().getGroup1().advanced()
            .getRightIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopGroupDisabledLeftIcon() {
        verifyStandardIcons(PanelMenuAttributes.topGroupDisabledLeftIcon, getPage().getGroup4().advanced().getLeftIconElement(), getPage().getGroup4().advanced()
            .getLeftIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopGroupDisabledRightIcon() {
        verifyStandardIcons(PanelMenuAttributes.topGroupDisabledRightIcon, getPage().getGroup4().advanced().getRightIconElement(), getPage().getGroup4().advanced()
            .getRightIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopGroupExpandedLeftIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 1");
        verifyStandardIcons(PanelMenuAttributes.topGroupExpandedLeftIcon, getPage().getGroup1().advanced().getLeftIconElement(), getPage().getGroup1().advanced()
            .getLeftIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopGroupExpandedRightIcon() {
        guardAjax(getPage().getPanelMenu()).expandGroup("Group 1");
        verifyStandardIcons(PanelMenuAttributes.topGroupExpandedRightIcon, getPage().getGroup1().advanced().getRightIconElement(), getPage().getGroup1().advanced()
            .getRightIconElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopItemDisabledLeftIcon() {
        verifyStandardIcons(PanelMenuAttributes.topItemDisabledLeftIcon, getPage().getItem4().advanced().getLeftIconElement(), getPage().getItem4().advanced()
            .getLeftIconImgElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopItemDisabledRightIcon() {
        verifyStandardIcons(PanelMenuAttributes.topItemDisabledRightIcon, getPage().getItem4().advanced().getRightIconElement(), getPage().getItem4().advanced()
            .getRightIconImgElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopItemLeftIcon() {
        verifyStandardIcons(PanelMenuAttributes.topItemLeftIcon, getPage().getItem3().advanced().getLeftIconElement(), getPage().getItem3().advanced()
            .getLeftIconImgElement(), "");
    }

    @Test
    @Templates("plain")
    public void testTopItemRightIcon() {
        verifyStandardIcons(PanelMenuAttributes.topItemRightIcon, getPage().getItem3().advanced().getRightIconElement(), getPage().getItem3().advanced()
            .getRightIconImgElement(), "");
    }

    private void verifyStandardIcons(PanelMenuAttributes attribute, WebElement icon, WebElement imgIcon, String classSuffix) {
        IconsCheckerWebdriver<PanelMenuAttributes> checker = new IconsCheckerWebdriver<PanelMenuAttributes>(driver,
            panelMenuAttributes, "rf-ico-", "");

        checker.checkCssImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkCssNoImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), imgIcon, classSuffix, false);
        checker.checkNone(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
    }
}
