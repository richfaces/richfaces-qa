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

import static org.jboss.arquillian.ajocado.Graphene.elementVisible;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.accordionAttributes;
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
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richAccordion/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestRichAccordion extends AbstractGrapheneTest {

    private JQueryLocator accordion = pjq("div[id$=accordion]");
    private JQueryLocator[] itemHeaders = { pjq("div[id$=item1:header]"), pjq("div[id$=item2:header]"),
            pjq("div[id$=item3:header]"), pjq("div[id$=item4:header]"), pjq("div[id$=item5:header]") };
    private JQueryLocator[] itemContents = { pjq("div[id$=item1:content]"), pjq("div[id$=item2:content]"),
            pjq("div[id$=item3:content]"), pjq("div[id$=item4:content]"), pjq("div[id$=item5:content]") };
    private JQueryLocator leftIcon = pjq("div[id$=item{0}] td.rf-ac-itm-ico");
    private JQueryLocator rightIcon = pjq("div[id$=item{0}] td.rf-ac-itm-exp-ico");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordion/simple.xhtml");
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
    public void testItemchangeEvents() {
        accordionAttributes.set(AccordionAttributes.onbeforeitemchange, "metamerEvents += \"beforeitemchange \"");
        accordionAttributes.set(AccordionAttributes.onitemchange, "metamerEvents += \"itemchange \"");

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
        accordionAttributes.set(AccordionAttributes.onbeforeitemchange, "metamerEvents += \"onbeforeitemchange \"");

        guardXhr(selenium).click(itemHeaders[1]);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        waitGui.failWith("onbeforeitemchange attribute does not work correctly").until(
            new EventFiredCondition(new Event("beforeitemchange")));
    }

    @Test
    public void testOnitemchange() {
        accordionAttributes.set(AccordionAttributes.onitemchange, "metamerEvents += \"onitemchange \"");

        guardXhr(selenium).click(itemHeaders[1]);
        waitGui.failWith("Item 2 is not displayed.").until(elementVisible.locator(itemContents[1]));

        waitGui.failWith("onitemchange attribute does not work correctly").until(
            new EventFiredCondition(new Event("itemchange")));
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, accordion);
    }

    private void verifyStandardIcons(JQueryLocator input, JQueryLocator icon, JQueryLocator image, String classSuffix) {
        IconsChecker checker = new IconsChecker(selenium, "rf-ico-", "-hdr");
        checker.checkCssImageIcons(input, icon, classSuffix);
        checker.checkCssNoImageIcons(input, icon, classSuffix);
        checker.checkImageIcons(input, icon, image, classSuffix);
        checker.checkNone(input, icon, classSuffix);
    }
}
