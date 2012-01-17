/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richAccordionItem;

import static org.jboss.arquillian.ajocado.Ajocado.elementVisible;
import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.contentClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerActiveClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerDisabledClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerInactiveClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerStyle;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.testng.annotations.Test;


/**
 * Test case for page faces/components/richAccordionItem/simple.xhtml.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestRichAccordionItem extends AbstractAjocadoTest {

    private JQueryLocator accordion = pjq("div[id$=accordion]");
    private JQueryLocator item1 = pjq("div[id$=item1].rf-ac-itm");
    private JQueryLocator[] itemHeaders = {pjq("div[id$=item1:header]"), pjq("div[id$=item2:header]"),
        pjq("div[id$=item3:header]"), pjq("div[id$=item4:header]"), pjq("div[id$=item5:header]")};
    private JQueryLocator[] itemContents = {pjq("div[id$=item1:content]"), pjq("div[id$=item2:content]"),
        pjq("div[id$=item3:content]"), pjq("div[id$=item4:content]"), pjq("div[id$=item5:content]")};
    private JQueryLocator[] activeHeaders = {pjq("div[id$=item1:header] div.rf-ac-itm-lbl-act"),
        pjq("div[id$=item2:header] div.rf-ac-itm-lbl-act"), pjq("div[id$=item3:header] div.rf-ac-itm-lbl-act"),
        pjq("div[id$=item4:header] div.rf-ac-itm-lbl-act"), pjq("div[id$=item5:header] div.rf-ac-itm-lbl-act")};
    private JQueryLocator[] inactiveHeaders = {pjq("div[id$=item1:header] div.rf-ac-itm-lbl-inact"),
        pjq("div[id$=item2:header] div.rf-ac-itm-lbl-inact"), pjq("div[id$=item3:header] div.rf-ac-itm-lbl-inact"),
        pjq("div[id$=item4:header] div.rf-ac-itm-lbl-inact"), pjq("div[id$=item5:header] div.rf-ac-itm-lbl-inact")};
    private JQueryLocator[] disabledHeaders = {pjq("div[id$=item1:header] div.rf-ac-itm-lbl-dis"),
        pjq("div[id$=item2:header] div.rf-ac-itm-lbl-dis"), pjq("div[id$=item3:header] div.rf-ac-itm-lbl-dis"),
        pjq("div[id$=item4:header] div.rf-ac-itm-lbl-dis"), pjq("div[id$=item5:header] div.rf-ac-itm-lbl-dis")};
    private JQueryLocator leftIcon = pjq("div[id$=item{0}] td.rf-ac-itm-ico");
    private JQueryLocator rightIcon = pjq("div[id$=item{0}] td.rf-ac-itm-exp-ico");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordionItem/simple.xhtml");
    }

    @Test
    public void testInit() {
        boolean accordionDisplayed = selenium.isVisible(accordion);
        assertTrue(accordionDisplayed, "Accordion is not present on the page.");

        accordionDisplayed = selenium.isVisible(itemHeaders[2]);
        assertTrue(accordionDisplayed, "Item3's header should be visible.");


        accordionDisplayed = selenium.isVisible(itemContents[2]);
        assertTrue(accordionDisplayed, "Content of item3 should be visible.");

        accordionDisplayed = selenium.isVisible(itemContents[0]);
        assertFalse(accordionDisplayed, "Item1's content should not be visible.");
    }

    @Test
    public void testContentClass() {
        testStyleClass(itemContents[0], contentClass);
    }

    @Test
    public void testDir() {
        testDir(item1);
    }

    @Test
    public void testDisabled() {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        guardNoRequest(selenium).click(itemHeaders[0]);
        boolean accordionDisplayed = selenium.isVisible(itemContents[0]);
        assertFalse(accordionDisplayed, "Item1's content should not be visible.");
    }

    @Test
    public void testHeader() {
        selenium.type(pjq("input[id$=headerInput]"), "new header");
        selenium.waitForPageToLoad();

        String header = selenium.getText(activeHeaders[0]);
        assertEquals(header, "new header", "Header of item1 did not change.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10297")
    public void testHeaderActiveClass() {
        testStyleClass(activeHeaders[0], headerActiveClass);
        assertFalse(selenium.belongsClass(activeHeaders[1], "metamer-ftest-class"), "headerActiveClass should be set only on first item");
        assertFalse(selenium.belongsClass(activeHeaders[2], "metamer-ftest-class"), "headerActiveClass should be set only on first item");
        assertFalse(selenium.belongsClass(activeHeaders[3], "metamer-ftest-class"), "headerActiveClass should be set only on first item");
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(itemHeaders[0], headerClass);
        assertFalse(selenium.belongsClass(itemHeaders[1], "metamer-ftest-class"), "headerClass should be set only on first item");
        assertFalse(selenium.belongsClass(itemHeaders[2], "metamer-ftest-class"), "headerClass should be set only on first item");
        assertFalse(selenium.belongsClass(itemHeaders[3], "metamer-ftest-class"), "headerClass should be set only on first item");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10297")
    public void testHeaderDisabledClass() {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        testStyleClass(disabledHeaders[0], headerDisabledClass);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10297")
    public void testHeaderInactiveClass() {
        testStyleClass(inactiveHeaders[0], headerInactiveClass);
        assertFalse(selenium.belongsClass(inactiveHeaders[1], "metamer-ftest-class"), "headerInactiveClass should be set only on first item");
        assertFalse(selenium.belongsClass(inactiveHeaders[2], "metamer-ftest-class"), "headerInactiveClass should be set only on first item");
        assertFalse(selenium.belongsClass(inactiveHeaders[3], "metamer-ftest-class"), "headerInactiveClass should be set only on first item");
    }

    @Test
    public void testHeaderStyle() {
        testStyle(itemHeaders[0], headerStyle);
    }

    @Test
    public void testLang() {
        testLang(item1);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10488")
    public void testLeftActiveIcon() {
        JQueryLocator icon = leftIcon.format(1).getDescendant(jq("div.rf-ac-itm-ico-act"));
        JQueryLocator input = pjq("select[id$=leftActiveIconInput]");
        JQueryLocator image = leftIcon.format(1).getChild(jq("img"));

        // icon=null
        for (int i = 1; i < 6; i++) {
            assertFalse(selenium.isElementPresent(leftIcon.format(i)), "Left icon of item" + i + " should not be present on the page.");
        }

        guardXhr(selenium).click(itemHeaders[0]);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(itemContents[0]));

        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testLeftDisabledIcon() {
        JQueryLocator icon = leftIcon.format(1).getDescendant(jq("div.rf-ac-itm-ico-inact"));
        JQueryLocator input = pjq("select[id$=leftDisabledIconInput]");
        JQueryLocator image = leftIcon.format(1).getChild(jq("img"));

        // icon=null
        for (int i = 1; i < 6; i++) {
            assertFalse(selenium.isElementPresent(leftIcon.format(i)), "Left icon of item" + i + " should not be present on the page.");
        }

        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        verifyStandardIcons(input, icon, image, "-dis");
    }

    @Test
    public void testLeftInactiveIcon() {
        JQueryLocator icon = leftIcon.format(1).getDescendant(jq("div.rf-ac-itm-ico-inact"));
        JQueryLocator input = pjq("select[id$=leftInactiveIconInput]");
        JQueryLocator image = leftIcon.format(1).getChild(jq("img"));

        // icon=null
        for (int i = 1; i < 6; i++) {
            assertFalse(selenium.isElementPresent(leftIcon.format(i)), "Left icon of item" + i + " should not be present on the page.");
        }

        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10488")
    public void testName() {
        selenium.type(pjq("input[id$=nameInput]"), "new name");
        selenium.waitForPageToLoad();

        guardXhr(selenium).click(pjq("input[type=submit][name$=switchButtonCustom]"));
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(itemContents[0]));
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, item1);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, item1);
    }

    @Test
    public void testOnenter() {
        testFireEvent(Event.CLICK, itemHeaders[0], "enter");
    }

    @Test
    public void testOnheaderclick() {
        testFireEvent(Event.CLICK, itemHeaders[0], "headerclick");
    }

    @Test
    public void testOnheaderdblclick() {
        testFireEvent(Event.DBLCLICK, itemHeaders[0], "headerdblclick");
    }

    @Test
    public void testOnheadermousedown() {
        testFireEvent(Event.MOUSEDOWN, itemHeaders[0], "headermousedown");
    }

    @Test
    public void testOnheadermousemove() {
        testFireEvent(Event.MOUSEMOVE, itemHeaders[0], "headermousemove");
    }

    @Test
    public void testOnheadermouseup() {
        testFireEvent(Event.MOUSEUP, itemHeaders[0], "headermouseup");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9821 https://issues.jboss.org/browse/RF-10488")
    public void testOnleave() {
        selenium.type(pjq("input[type=text][id$=onleaveInput]"), "metamerEvents += \"leave \"");
        selenium.waitForPageToLoad();

        guardXhr(selenium).click(itemHeaders[0]);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(itemContents[0]));

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(itemHeaders[1]);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "leave", "Attribute onleave doesn't work");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, item1);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, item1);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, item1);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, item1);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, item1);
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(item1), "Item1 should not be rendered when rendered=false.");
    }

    @Test
    public void testRightActiveIcon() {
        JQueryLocator icon = rightIcon.format(1).getDescendant(jq("div.rf-ac-itm-ico-act"));
        JQueryLocator input = pjq("select[id$=rightActiveIconInput]");
        JQueryLocator image = rightIcon.format(1).getChild(jq("img"));

        // icon=null
        for (int i = 1; i < 6; i++) {
            assertFalse(selenium.isElementPresent(rightIcon.format(i)), "Right icon of item" + i + " should not be present on the page.");
        }

        guardXhr(selenium).click(itemHeaders[0]);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(itemContents[0]));

        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testRightDisabledIcon() {
        JQueryLocator icon = rightIcon.format(1).getDescendant(jq("div.rf-ac-itm-ico-inact"));
        JQueryLocator input = pjq("select[id$=rightDisabledIconInput]");
        JQueryLocator image = rightIcon.format(1).getChild(jq("img"));

        // icon=null
        for (int i = 1; i < 6; i++) {
            assertFalse(selenium.isElementPresent(rightIcon.format(i)), "Right icon of item" + i + " should not be present on the page.");
        }

        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        verifyStandardIcons(input, icon, image, "-dis");
    }

    @Test
    public void testRightInactiveIcon() {
        JQueryLocator icon = rightIcon.format(1).getDescendant(jq("div.rf-ac-itm-ico-inact"));
        JQueryLocator input = pjq("select[id$=rightInactiveIconInput]");
        JQueryLocator image = rightIcon.format(1).getChild(jq("img"));

        // icon=null
        for (int i = 1; i < 6; i++) {
            assertFalse(selenium.isElementPresent(rightIcon.format(i)), "Right icon of item" + i + " should not be present on the page.");
        }

        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testStyle() {
        testStyle(item1);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(item1);
    }

    @Test
    public void testSwitchTypeNull() {
        guardXhr(selenium).click(itemHeaders[0]);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(itemContents[0]));
    }

    @Test
    public void testSwitchTypeAjax() {
        selenium.click(pjq("input[type=radio][name$=switchTypeInput][value=ajax]"));
        selenium.waitForPageToLoad();

        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        selenium.click(pjq("input[type=radio][name$=switchTypeInput][value=client]"));
        selenium.waitForPageToLoad();

        guardNoRequest(selenium).click(itemHeaders[0]);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(itemContents[0]));
    }

    @Test
    public void testSwitchTypeServer() {
        selenium.click(pjq("input[type=radio][name$=switchTypeInput][value=server]"));
        selenium.waitForPageToLoad();

        guardHttp(selenium).click(itemHeaders[0]);
        waitGui.failWith("Item 1 is not displayed.").until(elementVisible.locator(itemContents[0]));
    }

    @Test
    public void testTitle() {
        testTitle(item1);
    }

    private void verifyStandardIcons(JQueryLocator input, JQueryLocator icon, JQueryLocator image, String classSuffix) {
        IconsChecker checker = new IconsChecker(selenium, "rf-ico-", "-hdr");
        checker.checkCssImageIcons(input, icon, classSuffix);
        checker.checkCssNoImageIcons(input, icon, classSuffix);
        checker.checkImageIcons(input, icon, image, classSuffix);
        checker.checkNone(input, icon, classSuffix);
    }
}
