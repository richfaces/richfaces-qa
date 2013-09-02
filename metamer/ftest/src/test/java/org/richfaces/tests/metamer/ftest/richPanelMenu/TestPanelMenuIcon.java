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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
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

import org.openqa.selenium.WebElement;
import org.richfaces.component.Mode;
import org.richfaces.tests.metamer.ftest.checker.IconsCheckerWebdriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1.Final
 */
public class TestPanelMenuIcon extends AbstractPanelMenuTest {

    @BeforeMethod(alwaysRun = true)
    public void initializePage() {
        // to keep group 2 expanded after setting of the panelMenu attribute (HTTP request), so the icons are present
        // WARNING it depends on the right methods call order, super.BeforeMethods should be called firstly
        panelMenuAttributes.set(PanelMenuAttributes.groupMode, Mode.ajax);
    }

    @Test
    public void testGroupCollapsedLeftIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(groupCollapsedLeftIcon, page.getGroup24().advanced().getLeftIcon(), page.getGroup24().advanced()
            .getLeftIcon(), "");
    }

    @Test
    public void testGroupCollapsedRightIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(groupCollapsedRightIcon, page.getGroup24().advanced().getRightIcon(), page.getGroup24().advanced()
            .getRightIcon(), "");
    }

    @Test
    public void testGroupDisabledLeftIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        // for disabled icon both icons (expanded and collapsed) should be the same (state depends on implicit group settings)
        verifyStandardIcons(groupDisabledLeftIcon, page.getGroup26().advanced().getLeftIcon(), page.getGroup26().advanced()
            .getLeftIcon(), "");
    }

    @Test
    public void testGroupDisabledRightIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(groupDisabledRightIcon, page.getGroup26().advanced().getRightIcon(), page.getGroup26().advanced()
            .getRightIcon(), "");
    }

    @Test
    public void testGroupExpandedLeftIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        guardAjax(page.getPanelMenu()).expandGroup("Group 2.4");
        verifyStandardIcons(groupExpandedLeftIcon, page.getGroup24().advanced().getLeftIcon(), page.getGroup24().advanced()
            .getLeftIcon(), "");
    }

    @Test
    public void testGroupExpandedRightIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        guardAjax(page.getPanelMenu()).expandGroup("Group 2.4");
        verifyStandardIcons(groupExpandedRightIcon, page.getGroup24().advanced().getRightIcon(), page.getGroup24().advanced()
            .getRightIcon(), "");
    }

    @Test
    public void testItemDisabledLeftIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(itemDisabledLeftIcon, page.getItem25().advanced().getLeftIcon(), page.getItem25().advanced()
            .getLeftIconImg(), "");
    }

    @Test
    public void testItemDisabledRightIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(itemDisabledRightIcon, page.getItem25().advanced().getRightIcon(), page.getItem25().advanced()
            .getRightIconImg(), "");
    }

    @Test
    public void testItemLeftIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(itemLeftIcon, page.getItem22().advanced().getLeftIcon(), page.getItem22().advanced()
            .getLeftIconImg(), "");
    }

    @Test
    public void testItemRightIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 2");
        verifyStandardIcons(itemRightIcon, page.getItem22().advanced().getRightIcon(), page.getItem22().advanced()
            .getRightIconImg(), "");
    }

    @Test
    public void testTopGroupCollapsedLeftIcon() {
        verifyStandardIcons(topGroupCollapsedLeftIcon, page.getGroup1().advanced().getLeftIcon(), page.getGroup1().advanced()
            .getLeftIcon(), "");
    }

    @Test
    public void testTopGroupCollapsedRightIcon() {
        verifyStandardIcons(topGroupCollapsedRightIcon, page.getGroup1().advanced().getRightIcon(), page.getGroup1().advanced()
            .getRightIcon(), "");
    }

    @Test
    public void testTopGroupDisabledLeftIcon() {
        verifyStandardIcons(topGroupDisabledLeftIcon, page.getGroup4().advanced().getLeftIcon(), page.getGroup4().advanced()
            .getLeftIcon(), "");
    }

    @Test
    public void testTopGroupDisabledRightIcon() {
        verifyStandardIcons(topGroupDisabledRightIcon, page.getGroup4().advanced().getRightIcon(), page.getGroup4().advanced()
            .getRightIcon(), "");
    }

    @Test
    public void testTopGroupExpandedLeftIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 1");
        verifyStandardIcons(topGroupExpandedLeftIcon, page.getGroup1().advanced().getLeftIcon(), page.getGroup1().advanced()
            .getLeftIcon(), "");
    }

    @Test
    public void testTopGroupExpandedRightIcon() {
        guardAjax(page.getPanelMenu()).expandGroup("Group 1");
        verifyStandardIcons(topGroupExpandedRightIcon, page.getGroup1().advanced().getRightIcon(), page.getGroup1().advanced()
            .getRightIcon(), "");
    }

    @Test
    public void testTopItemDisabledLeftIcon() {
        verifyStandardIcons(topItemDisabledLeftIcon, page.getItem4().advanced().getLeftIcon(), page.getItem4().advanced()
            .getLeftIconImg(), "");
    }

    @Test
    public void testTopItemDisabledRightIcon() {
        verifyStandardIcons(topItemDisabledRightIcon, page.getItem4().advanced().getRightIcon(), page.getItem4().advanced()
            .getRightIconImg(), "");
    }

    @Test
    public void testTopItemLeftIcon() {
        verifyStandardIcons(topItemLeftIcon, page.getItem3().advanced().getLeftIcon(), page.getItem3().advanced()
            .getLeftIconImg(), "");
    }

    @Test
    public void testTopItemRightIcon() {
        verifyStandardIcons(topItemRightIcon, page.getItem3().advanced().getRightIcon(), page.getItem3().advanced()
            .getRightIconImg(), "");
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
