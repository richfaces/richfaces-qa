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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22727 $
 */
public class TestPanelMenuIcon extends AbstractPanelMenuTest {

    @Test
    public void testGroupCollapsedLeftIcon() {
        group2.toggle();
        JQueryLocator input = pjq("select[id$=groupCollapsedLeftIcon]");
        ElementLocator<JQueryLocator> icon = group24.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group24.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testGroupCollapsedRightIcon() {
        group2.toggle();
        JQueryLocator input = pjq("select[id$=groupCollapsedRightIcon]");
        ElementLocator<JQueryLocator> icon = group24.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group24.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testGroupDisabledLeftIcon() {
        group2.toggle();
        JQueryLocator input = pjq("select[id$=groupDisabledLeftIcon]");
        ElementLocator<JQueryLocator> icon = group26.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group26.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testDisabledRightIcon() {
        group2.toggle();
        JQueryLocator input = pjq("select[id$=disabledRightIcon]");
        ElementLocator<JQueryLocator> icon = group26.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group26.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testGroupExpandedLeftIcon() {
        group2.toggle();
        group24.toggle();
        JQueryLocator input = pjq("select[id$=groupExpandedLeftIcon]");
        ElementLocator<JQueryLocator> icon = group24.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group24.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testGroupExpandedRightIcon() {
        group2.toggle();
        group24.toggle();
        JQueryLocator input = pjq("select[id$=groupExpandedRightIcon]");
        ElementLocator<JQueryLocator> icon = group24.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group24.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testItemDisabledLeftIcon() {
        group2.toggle();
        JQueryLocator input = pjq("select[id$=itemDisabledLeftIcon]");
        ElementLocator<JQueryLocator> icon = item25.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(item25.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testItemDisabledRightIcon() {
        group2.toggle();
        JQueryLocator input = pjq("select[id$=itemDisabledRightIcon]");
        ElementLocator<JQueryLocator> icon = item25.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(item25.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testItemLeftIcon() {
        group2.toggle();
        JQueryLocator input = pjq("select[id$=itemLeftIcon]");
        ElementLocator<JQueryLocator> icon = item22.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(item22.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testItemRightIcon() {
        group2.toggle();
        JQueryLocator input = pjq("select[id$=itemRightIcon]");
        ElementLocator<JQueryLocator> icon = item22.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(item22.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopGroupCollapsedLeftIcon() {
        JQueryLocator input = pjq("select[id$=topGroupCollapsedLeftIcon]");
        ElementLocator<JQueryLocator> icon = group1.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group1.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopGroupCollapsedRightIcon() {
        JQueryLocator input = pjq("select[id$=topGroupCollapsedRightIcon]");
        ElementLocator<JQueryLocator> icon = group1.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group1.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopGroupDisabledLeftIcon() {
        JQueryLocator input = pjq("select[id$=topGroupDisabledLeftIcon]");
        ElementLocator<JQueryLocator> icon = group4.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group4.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopGroupDisabledRightIcon() {
        JQueryLocator input = pjq("select[id$=topGroupDisabledRightIcon]");
        ElementLocator<JQueryLocator> icon = group4.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group4.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopGroupExpandedLeftIcon() {
        group1.toggle();
        JQueryLocator input = pjq("select[id$=topGroupExpandedLeftIcon]");
        ElementLocator<JQueryLocator> icon = group1.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group1.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopGroupExpandedRightIcon() {
        group1.toggle();
        JQueryLocator input = pjq("select[id$=topGroupExpandedRightIcon]");
        ElementLocator<JQueryLocator> icon = group1.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(group1.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopItemDisabledLeftIcon() {
        JQueryLocator input = pjq("select[id$=topItemDisabledLeftIcon]");
        ElementLocator<JQueryLocator> icon = item4.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(item4.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopItemDisabledRightIcon() {
        JQueryLocator input = pjq("select[id$=topItemDisabledRightIcon]");
        ElementLocator<JQueryLocator> icon = item4.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(item4.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopItemLeftIcon() {
        JQueryLocator input = pjq("select[id$=topItemLefttIconInput]");
        ElementLocator<JQueryLocator> icon = item3.getLeftIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(item3.getLeftIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testTopItemRightIcon() {
        JQueryLocator input = pjq("select[id$=topItemRightIconInput]");
        ElementLocator<JQueryLocator> icon = item3.getRightIcon().getIcon();
        ElementLocator<JQueryLocator> image = jq(item3.getRightIcon().getIcon().getRawLocator()).getChild(jq("img"));
        verifyStandardIcons(input, icon, image, "");
    }

    private void verifyStandardIcons(ElementLocator<JQueryLocator> input, ElementLocator<JQueryLocator> icon, ElementLocator<JQueryLocator> image, String classSuffix) {
        IconsChecker checker = new IconsChecker(selenium, "rf-ico-", "");
        checker.checkCssImageIcons(input, icon, classSuffix);
        checker.checkCssNoImageIcons(input, icon, classSuffix);
        checker.checkImageIcons(input, icon, image, classSuffix, false);
        checker.checkNone(input, icon, classSuffix);
    }
}
