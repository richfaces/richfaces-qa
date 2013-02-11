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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupCollapsedLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupCollapsedRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupDisabledLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupDisabledRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupExpandedLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.groupExpandedRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.itemDisabledLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.itemDisabledRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.itemLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.itemRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topGroupCollapsedLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topGroupCollapsedRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topGroupDisabledLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topGroupDisabledRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topGroupExpandedLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topGroupExpandedRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topItemDisabledLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topItemDisabledRightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topItemLeftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenu.PanelMenuAttributes.topItemRightIcon;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuAttributes;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.IMG_BY_LOC;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.checker.IconsCheckerWebdriver;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1.Final
 */
public class TestPanelMenuIcon extends AbstractPanelMenuTest {

    @Test
    public void testGroupCollapsedLeftIcon() {
        page.group2.toggle();
        verifyStandardIcons(groupCollapsedLeftIcon, page.group24.leftIcon.iconCollapsed, IMG_BY_LOC, "");
    }

    @Test
    public void testGroupCollapsedRightIcon() {
        page.group2.toggle();
        verifyStandardIcons(groupCollapsedRightIcon, page.group24.rightIcon.iconCollapsed, IMG_BY_LOC, "");
    }

    @Test
    public void testGroupDisabledLeftIcon() {
        page.group2.toggle();
        // for disabled icon both icons (expanded and collapsed) should be the same (state depends on implicit group settings)
        verifyStandardIcons(groupDisabledLeftIcon, page.group26.leftIcon.iconCollapsed, IMG_BY_LOC, "");
        verifyStandardIcons(groupDisabledLeftIcon, page.group26.leftIcon.iconExpanded, IMG_BY_LOC, "");
    }

    @Test
    public void testGroupDisabledRightIcon() {
        // TODO JJa: have a look: there were something wrong with original version
        page.group2.toggle();
        verifyStandardIcons(groupDisabledRightIcon, page.group26.rightIcon.iconCollapsed, IMG_BY_LOC, "");
        verifyStandardIcons(groupDisabledRightIcon, page.group26.rightIcon.iconExpanded, IMG_BY_LOC, "");
    }

    @Test
    public void testGroupExpandedLeftIcon() {
        page.group2.toggle();
        page.group24.toggle();
        verifyStandardIcons(groupExpandedLeftIcon, page.group24.leftIcon.iconExpanded, IMG_BY_LOC, "");
    }

    @Test
    public void testGroupExpandedRightIcon() {
        page.group2.toggle();
        page.group24.toggle();
        verifyStandardIcons(groupExpandedRightIcon, page.group24.rightIcon.iconExpanded, IMG_BY_LOC, "");
    }

    @Test
    public void testItemDisabledLeftIcon() {
        page.group2.toggle();
        verifyStandardIcons(itemDisabledLeftIcon, page.item25.leftIcon.icon, IMG_BY_LOC, "");
    }

    @Test
    public void testItemDisabledRightIcon() {
        page.group2.toggle();
        verifyStandardIcons(itemDisabledRightIcon, page.item25.rightIcon.icon, IMG_BY_LOC, "");
    }

    @Test
    public void testItemLeftIcon() {
        page.group2.toggle();
        verifyStandardIcons(itemLeftIcon, page.item22.leftIcon.icon, IMG_BY_LOC, "");
    }

    @Test
    public void testItemRightIcon() {
        page.group2.toggle();
        verifyStandardIcons(itemRightIcon, page.item22.rightIcon.icon, IMG_BY_LOC, "");
    }

    @Test
    public void testTopGroupCollapsedLeftIcon() {
        verifyStandardIcons(topGroupCollapsedLeftIcon, page.group1.leftIcon.iconCollapsed, IMG_BY_LOC, "");
    }

    @Test
    public void testTopGroupCollapsedRightIcon() {
        verifyStandardIcons(topGroupCollapsedRightIcon, page.group1.rightIcon.iconCollapsed, IMG_BY_LOC, "");
    }

    @Test
    public void testTopGroupDisabledLeftIcon() {
        verifyStandardIcons(topGroupDisabledLeftIcon, page.group4.leftIcon.iconCollapsed, IMG_BY_LOC, "");
        verifyStandardIcons(topGroupDisabledLeftIcon, page.group4.leftIcon.iconExpanded, IMG_BY_LOC, "");
    }

    @Test
    public void testTopGroupDisabledRightIcon() {
        verifyStandardIcons(topGroupDisabledRightIcon, page.group4.rightIcon.iconCollapsed, IMG_BY_LOC, "");
        verifyStandardIcons(topGroupDisabledRightIcon, page.group4.rightIcon.iconExpanded, IMG_BY_LOC, "");
    }

    @Test
    public void testTopGroupExpandedLeftIcon() {
        page.group1.toggle();
        verifyStandardIcons(topGroupExpandedLeftIcon, page.group1.leftIcon.iconExpanded, IMG_BY_LOC, "");
    }

    @Test
    public void testTopGroupExpandedRightIcon() {
        page.group1.toggle();
        verifyStandardIcons(topGroupExpandedRightIcon, page.group1.rightIcon.iconExpanded, IMG_BY_LOC, "");
    }

    @Test
    public void testTopItemDisabledLeftIcon() {
        verifyStandardIcons(topItemDisabledLeftIcon, page.item4.leftIcon.icon, IMG_BY_LOC, "");
    }

    @Test
    public void testTopItemDisabledRightIcon() {
        verifyStandardIcons(topItemDisabledRightIcon, page.item4.rightIcon.icon, IMG_BY_LOC, "");
    }

    @Test
    public void testTopItemLeftIcon() {
        verifyStandardIcons(topItemLeftIcon, page.item3.leftIcon.icon, IMG_BY_LOC, "");
    }

    @Test
    public void testTopItemRightIcon() {
        verifyStandardIcons(topItemRightIcon, page.item3.rightIcon.icon, IMG_BY_LOC, "");
    }

    private void verifyStandardIcons(PanelMenuAttributes attribute, WebElement icon, By image, String classSuffix) {
        IconsCheckerWebdriver<PanelMenuAttributes> checker = new IconsCheckerWebdriver<PanelMenuAttributes>(
            driver, panelMenuAttributes, "rf-ico-", "");

        checker.checkCssImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkCssNoImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkImageIconsAsRelative(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), image, classSuffix, false);
        checker.checkNone(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);

    }
}
