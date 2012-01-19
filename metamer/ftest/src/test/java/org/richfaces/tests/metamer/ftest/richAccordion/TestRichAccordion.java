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
package org.richfaces.tests.metamer.ftest.richAccordion;

import static org.jboss.arquillian.ajocado.Ajocado.elementVisible;
import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemActiveHeaderClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemContentClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemDisabledHeaderClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemHeaderClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.itemInactiveHeaderClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/richAccordion/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestRichAccordion extends AbstractAjocadoTest {

    private JQueryLocator accordion = pjq("div[id$=accordion]");
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
        return buildUrl(contextPath, "faces/components/richAccordion/simple.xhtml");
    }

    @Test
    public void testInit() {
        boolean accordionDisplayed = selenium.isVisible(accordion);
        assertTrue(accordionDisplayed, "Accordion is not present on the page.");

        for (int i = 0; i < 5; i++) {
            accordionDisplayed = selenium.isVisible(itemHeaders[i]);
            assertTrue(accordionDisplayed, "Item" + (i + 1) + "'s header should be visible.");
        }

        accordionDisplayed = selenium.isVisible(itemContents[0]);
        assertTrue(accordionDisplayed, "Content of item1 should be visible.");

        for (int i = 1; i < 5; i++) {
            accordionDisplayed = selenium.isVisible(itemContents[i]);
            assertFalse(accordionDisplayed, "Item" + (i + 1) + "'s content should not be visible.");
        }
    }

    @Test
    public void testActiveItem() {
        selenium.type(pjq("input[type=text][id$=activeItemInput]"), "item5");
        selenium.waitForPageToLoad();

        boolean accordionDisplayed = selenium.isVisible(accordion);
        assertTrue(accordionDisplayed, "Accordion is not present on the page.");

        for (int i = 0; i < 5; i++) {
            accordionDisplayed = selenium.isVisible(itemHeaders[i]);
            assertTrue(accordionDisplayed, "Item" + (i + 1) + "'s header should be visible.");
        }

        accordionDisplayed = selenium.isVisible(itemContents[4]);
        assertTrue(accordionDisplayed, "Content of item5 should be visible.");

        for (int i = 0; i < 4; i++) {
            accordionDisplayed = selenium.isVisible(itemContents[i]);
            assertFalse(accordionDisplayed, "Item" + (i + 1) + "'s content should not be visible.");
        }

        selenium.type(pjq("input[type=text][id$=activeItemInput]"), "item4");
        selenium.waitForPageToLoad();

        for (int i = 0; i < 5; i++) {
            accordionDisplayed = selenium.isVisible(itemHeaders[i]);
            assertTrue(accordionDisplayed, "Item" + (i + 1) + "'s header should be visible.");
        }
        
        accordionDisplayed = selenium.isVisible(itemContents[0]);
        assertTrue(accordionDisplayed, "Content of item1 should be visible.");

        for (int i = 1; i < 5; i++) {
            accordionDisplayed = selenium.isVisible(itemContents[i]);
            assertFalse(accordionDisplayed, "Item" + (i + 1) + "'s content should not be visible.");
        }
    }

    @Test
    public void testCycledSwitching() {
        String accordionId = selenium.getEval(new JavaScript("window.testedComponentId"));
        String result = null;

        // RichFaces.$('form:accordion').nextItem('item4') will be null
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + accordionId + "').nextItem('item4')"));
        assertEquals(result, "null", "Result of function nextItem('item4')");

        // RichFaces.$('form:accordion').prevItem('item1') will be null
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + accordionId + "').prevItem('item1')"));
        assertEquals(result, "null", "Result of function prevItem('item1')");

        selenium.click(pjq("input[type=radio][name$=cycledSwitchingInput][value=true]"));
        selenium.waitForPageToLoad();

        // RichFaces.$('form:accordion').nextItem('item5') will be item1
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + accordionId + "').nextItem('item5')"));
        assertEquals(result, "item1", "Result of function nextItem('item5')");

        // RichFaces.$('form:accordion').prevItem('item1') will be item5
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + accordionId + "').prevItem('item1')"));
        assertEquals(result, "item5", "Result of function prevItem('item1')");
    }

    @Test
    public void testDir() {
        testDir(accordion);
    }

    @Test
    public void testHeight() {
        AttributeLocator<?> attribute = accordion.getAttribute(new Attribute("style"));

        // height = null
        assertFalse(selenium.isAttributePresent(attribute), "Attribute style should not be present.");

        // height = 300px
        selenium.type(pjq("input[type=text][id$=heightInput]"), "300px");
        selenium.waitForPageToLoad(TIMEOUT);

        assertTrue(selenium.isAttributePresent(attribute), "Attribute style should be present.");
        String value = selenium.getStyle(accordion, CssProperty.HEIGHT);
        assertEquals(value, "300px", "Attribute width");
    }

    @Test
    public void testImmediate() {
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.click(itemHeaders[2]);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(itemContents[2]));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "item changed: item1 -> item3");
    }

    @Test
    public void testItemChangeListener() {
        selenium.click(itemHeaders[2]);
        waitGui.failWith("Item 3 is not displayed.").until(elementVisible.locator(itemContents[2]));

        phaseInfo.assertListener(PhaseId.UPDATE_MODEL_VALUES, "item changed: item1 -> item3");
    }

    @Test
    public void testItemActiveHeaderClass() {
        testStyleClass(activeHeaders[0], itemActiveHeaderClass);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10352")
    public void testItemActiveLeftIcon() {
        JQueryLocator icon = leftIcon.format(1).getDescendant(jq("div.rf-ac-itm-ico-act"));
        JQueryLocator input = pjq("select[id$=itemActiveLeftIconInput]");
        JQueryLocator image = leftIcon.format(1).getChild(jq("img"));

        // icon=null
        for (int i = 1; i < 6; i++) {
            assertFalse(selenium.isElementPresent(leftIcon.format(i)), "Left icon of item" + i
                    + " should not be present on the page.");
        }

        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testItemActiveRightIcon() {
        JQueryLocator icon = rightIcon.format(1).getDescendant(jq("div.rf-ac-itm-ico-act"));
        JQueryLocator input = pjq("select[id$=itemActiveRightIconInput]");
        JQueryLocator image = rightIcon.format(1).getChild(jq("img"));

        // icon=null
        for (int i = 1; i < 6; i++) {
            assertFalse(selenium.isElementPresent(rightIcon.format(i)), "Right icon of item" + i
                    + " should not be present on the page.");
        }

        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testItemContentClass() {
        testStyleClass(itemContents[2], itemContentClass);
    }

    @Test
    public void testItemDisabledHeaderClass() {
        testStyleClass(disabledHeaders[3], itemDisabledHeaderClass);
    }

    @Test
    public void testItemDisabledLeftIcon() {
        JQueryLocator icon = leftIcon.format(4).getDescendant(jq("div.rf-ac-itm-ico-inact"));
        JQueryLocator input = pjq("select[id$=itemDisabledLeftIconInput]");
        JQueryLocator image = leftIcon.format(4).getChild(jq("img"));

        verifyStandardIcons(input, icon, image, "-dis");
    }

    @Test
    public void testItemDisabledRightIcon() {
        JQueryLocator icon = rightIcon.format(4).getDescendant(jq("div.rf-ac-itm-ico-inact"));
        JQueryLocator input = pjq("select[id$=itemDisabledRightIconInput]");
        JQueryLocator image = rightIcon.format(4).getChild(jq("img"));

        verifyStandardIcons(input, icon, image, "-dis");
    }

    @Test
    public void testItemHeaderClass() {
        testStyleClass(itemHeaders[2], itemHeaderClass);
    }

    @Test
    public void testItemInactiveHeaderClass() {
        testStyleClass(inactiveHeaders[1], itemInactiveHeaderClass);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10352")
    public void testItemInactiveLeftIcon() {
        JQueryLocator icon = leftIcon.format(3).getDescendant(jq("div.rf-ac-itm-ico-inact"));
        JQueryLocator input = pjq("select[id$=itemInactiveLeftIconInput]");
        JQueryLocator image = leftIcon.format(3).getChild(jq("img"));

        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testItemInactiveRightIcon() {
        JQueryLocator icon = rightIcon.format(3).getDescendant(jq("div.rf-ac-itm-ico-inact"));
        JQueryLocator input = pjq("select[id$=itemInactiveRightIconInput]");
        JQueryLocator image = rightIcon.format(3).getChild(jq("img"));

        verifyStandardIcons(input, icon, image, "");
    }

    @Test
    public void testLang() {
        testLang(accordion);
    }

    @Test
    public void testItemchangeEvents() {
        selenium.type(pjq("input[type=text][id$=onbeforeitemchangeInput]"), "metamerEvents += \"beforeitemchange \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onitemchangeInput]"), "metamerEvents += \"itemchange \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(itemHeaders[2]);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "beforeitemchange", "Attribute onbeforeitemchange doesn't work");
        assertEquals(events[1], "itemchange", "Attribute onbeforeitemchange doesn't work");
    }

    @Test
    public void testOnbeforeitemchange() {
        selenium.type(pjq("input[id$=onbeforeitemchangeInput]"), "metamerEvents += \"onbeforeitemchange \"");
        selenium.waitForPageToLoad(TIMEOUT);

        guardXhr(selenium).click(itemHeaders[1]);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        waitGui.failWith("onbeforeitemchange attribute does not work correctly").until(
                new EventFiredCondition(new Event("beforeitemchange")));
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, accordion);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, accordion);
    }

    @Test
    public void testOnitemchange() {
        selenium.type(pjq("input[id$=onitemchangeInput]"), "metamerEvents += \"onitemchange \"");
        selenium.waitForPageToLoad(TIMEOUT);

        guardXhr(selenium).click(itemHeaders[1]);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        waitGui.failWith("onitemchange attribute does not work correctly").until(
                new EventFiredCondition(new Event("itemchange")));
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, accordion);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, accordion);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, accordion);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, accordion);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, accordion);
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(accordion), "Accordion should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        testStyle(accordion);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(accordion);
    }

    @Test
    public void testSwitchTypeNull() {
        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardXhr(selenium).click(itemHeaders[index]);
            waitGui.failWith("Item " + index + " is not displayed.").until(elementVisible.locator(itemContents[index]));
        }
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

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardNoRequest(selenium).click(itemHeaders[index]);
            waitGui.failWith("Item " + index + " is not displayed.").until(elementVisible.locator(itemContents[index]));
        }
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10040")
    public void testSwitchTypeServer() {
        selenium.click(pjq("input[type=radio][name$=switchTypeInput][value=server]"));
        selenium.waitForPageToLoad();

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardHttp(selenium).click(itemHeaders[index]);
            waitGui.failWith("Item " + index + " is not displayed.").until(elementVisible.locator(itemContents[index]));
        }
    }

    @Test
    public void testTitle() {
        testTitle(accordion);
    }

    @Test
    public void testWidth() {
        AttributeLocator<?> attribute = accordion.getAttribute(new Attribute("style"));

        // width = null
        assertFalse(selenium.isAttributePresent(attribute), "Attribute style should not be present.");

        // width = 50%
        selenium.type(pjq("input[type=text][id$=widthInput]"), "356px");
        selenium.waitForPageToLoad(TIMEOUT);

        assertTrue(selenium.isAttributePresent(attribute), "Attribute style should be present.");
        String value = selenium.getStyle(accordion, CssProperty.WIDTH);
        assertEquals(value, "356px", "Attribute width");
    }

    private void verifyStandardIcons(JQueryLocator input, JQueryLocator icon, JQueryLocator image, String classSuffix) {
        IconsChecker checker = new IconsChecker(selenium, "rf-ico-", "-hdr");
        checker.checkCssImageIcons(input, icon, classSuffix);
        checker.checkCssNoImageIcons(input, icon, classSuffix);
        checker.checkImageIcons(input, icon, image, classSuffix);
        checker.checkNone(input, icon, classSuffix);
    }
}
