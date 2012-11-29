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
package org.richfaces.tests.metamer.ftest.richMenuGroup;

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionLabel;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.menuGroupAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;
import org.richfaces.tests.metamer.ftest.richMenuItem.MenuItemAttributes;

/**
 * Test case for page /faces/components/richMenuGroup/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23122 $
 */
public class TestRichMenuGroup extends AbstractGrapheneTest {

    private JQueryLocator fileMenu = pjq("div[id$=menu1]");
    private JQueryLocator fileMenuLabel = pjq("div[id$=menu1_label]");
    private JQueryLocator fileMenuList = pjq("div[id$=menu1_list]");
    private JQueryLocator group = pjq("div[id$=menuGroup4]");
    private JQueryLocator groupList = pjq("div[id$=menuGroup4_list]");
    private JQueryLocator icon = group.getDescendant(jq("> span.rf-ddm-itm-ic img"));
    private JQueryLocator emptyIcon = group.getDescendant(jq("> span.rf-ddm-itm-ic > span.rf-ddm-emptyIcon"));
    private JQueryLocator label = group.getDescendant(jq("span.rf-ddm-itm-lbl"));
    private JQueryLocator menuItem41 = pjq("div[id$=menuItem41]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMenuGroup/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Menu Group", "Simple");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(fileMenu), "Drop down menu \"File\" should be present on the page.");
        assertTrue(selenium.isVisible(fileMenu), "Drop down menu \"File\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(group), "Menu group \"Save As...\" should be present on the page.");
        assertFalse(selenium.isVisible(group), "Menu group \"Save As...\" should not be visible on the page.");

        assertFalse(selenium.isVisible(fileMenuList), "Menu should not be expanded.");
        guardNoRequest(selenium).mouseOver(fileMenuLabel);
        assertTrue(selenium.isVisible(fileMenuList), "Menu should be expanded.");

        assertTrue(selenium.isElementPresent(group), "Menu group \"Save As...\" should be present on the page.");
        assertTrue(selenium.isVisible(group), "Menu group \"Save As...\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(menuItem41), "Menu item \"Save\" should be present on the page.");
        assertFalse(selenium.isVisible(menuItem41), "Menu item \"Save\" should not be visible on the page.");

        assertFalse(selenium.isVisible(groupList), "Submenu should not be expanded.");
        guardNoRequest(selenium).getEval(new JavaScript("window.jQuery('" + group.getRawLocator() + "').mouseenter()"));
        assertTrue(selenium.isVisible(groupList), "Submenu should be expanded.");

        assertTrue(selenium.isElementPresent(menuItem41), "Menu item \"Save\" should be present on the page.");
        assertTrue(selenium.isVisible(menuItem41), "Menu item \"Save\" should be visible on the page.");

        assertFalse(selenium.isElementPresent(icon), "Icon of menu group should not be present on the page.");
        assertTrue(selenium.isElementPresent(emptyIcon), "Menu group should have an empty icon.");

        assertTrue(selenium.isElementPresent(label), "Label of menu group should be present on the page.");
        assertTrue(selenium.isVisible(label), "Label of menu group should be visible on the page.");

        assertEquals(selenium.getText(label), "Save As...", "Label of menu group.");
    }

    @Test
    public void testDir() {
        testDir(group);
    }

    //    @Test
    //    @IssueTracking("https://issues.jboss.org/browse/RF-10218")
    //    public void testDirection() {
    //        fail("not implemented in RichFaces");
    //    }
    @Test
    public void testDisabled() {
        menuGroupAttributes.set(MenuGroupAttributes.disabled, true);

        assertTrue(selenium.belongsClass(group, "rf-ddm-itm-dis"), "Menu group should have class \"rf-ddm-itm-dis\".");
        assertTrue(selenium.isElementPresent(emptyIcon), "Empty icon should be present.");
        assertFalse(selenium.isElementPresent(icon), "Icon should not be present.");
    }

    //    @Test
    //    @IssueTracking("https://issues.jboss.org/browse/RF-10216")
    //    public void testHorizontalOffset() {
    //        fail("not implemented in RichFaces");
    //    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9989")
    public void testIcon() {
        AttributeLocator<?> attr = icon.getAttribute(Attribute.SRC);

        menuGroupAttributes.set(MenuGroupAttributes.icon, "star");
        assertTrue(selenium.getAttribute(attr).contains("star.png"),
            "Icon's src attribute should contain \"star.png\".");

        menuGroupAttributes.set(MenuGroupAttributes.icon, "nonexisting");
        assertTrue(selenium.getAttribute(attr).contains("nonexisting"),
            "Icon's src attribute should contain \"nonexisting\".");

        menuGroupAttributes.set(MenuGroupAttributes.icon, "null");
        assertFalse(selenium.isElementPresent(icon), "Icon should not be present.");
        assertTrue(selenium.isElementPresent(emptyIcon), "Empty icon should be present.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9989")
    public void testIconDisabled() {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();
        menuGroupAttributes.set(MenuGroupAttributes.disabled, true);

        AttributeLocator<?> attr = icon.getAttribute(Attribute.SRC);

        assertFalse(selenium.isElementPresent(icon), "Icon should not be present.");
        assertTrue(selenium.isElementPresent(emptyIcon), "Empty icon should be present.");

        menuGroupAttributes.set(MenuGroupAttributes.iconDisabled, "star");
        assertTrue(selenium.getAttribute(attr).contains("star.png"),
            "Icon's src attribute should contain \"star.png\".");

        menuGroupAttributes.set(MenuGroupAttributes.iconDisabled, "nonexisting");
        assertTrue(selenium.getAttribute(attr).contains("nonexisting"),
            "Icon's src attribute should contain \"nonexisting\".");
    }

    //    @Test
    //    @IssueTracking("https://issues.jboss.org/browse/RF-10218")
    //    public void testJointPoint() {
    //        fail("not implemented in RichFaces");
    //    }

    @Test
    public void testLabel() {
        menuGroupAttributes.set(MenuGroupAttributes.label, "new label");
        assertEquals(selenium.getText(label), "new label", "New label of the menu group.");
    }

    @Test
    public void testLang() {
        testLang(group);
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, group);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, group);
    }

    @Test
    public void testOnhide() {
        menuGroupAttributes.set(MenuGroupAttributes.onhide, "metamerEvents += \"hide \"");

        selenium.mouseOver(fileMenuLabel);
        waitGui.failWith("Menu was not open.").until(elementVisible.locator(fileMenuList));
        selenium.getEval(new JavaScript("window.jQuery('" + group.getRawLocator() + "').mouseenter()"));
        waitGui.failWith("Submenu was not open").until(elementVisible.locator(groupList));
        selenium.getEval(new JavaScript("window.jQuery('" + group.getRawLocator() + "').mouseleave()"));

        waitGui.failWith("Attribute onhide does not work correctly").until(new EventFiredCondition(new Event("hide")));
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, group);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, group);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, group);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, group);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, group);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, group);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, group);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, group);
    }

    @Test
    public void testOnshow() {
        menuGroupAttributes.set(MenuGroupAttributes.onshow, "metamerEvents += \"show \"");

        selenium.mouseOver(fileMenuLabel);
        waitGui.failWith("Menu was not open.").until(elementVisible.locator(fileMenuList));
        selenium.getEval(new JavaScript("window.jQuery('" + group.getRawLocator() + "').mouseenter()"));
        waitGui.failWith("Submenu was not open").until(elementVisible.locator(groupList));

        waitGui.failWith("Attribute onhide does not work correctly").until(new EventFiredCondition(new Event("show")));
    }

    @Test
    public void testRendered() {
        menuGroupAttributes.set(MenuGroupAttributes.rendered, false);
        assertFalse(selenium.isElementPresent(group), "Menu group should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        testStyle(group);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(group);
    }

    @Test
    public void testTitle() {
        testTitle(group);
    }

    //    @Test
    //    @IssueTracking("https://issues.jboss.org/browse/RF-10216")
    //    public void testVerticalOffset() {
    //        fail("not implemented in RichFaces");
    //    }
}
