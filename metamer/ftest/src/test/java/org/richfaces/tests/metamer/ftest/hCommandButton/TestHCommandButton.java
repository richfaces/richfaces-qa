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
package org.richfaces.tests.metamer.ftest.hCommandButton;

import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/commandButton/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22797 $
 */
public class TestHCommandButton extends AbstractGrapheneTest {

    private JQueryLocator input = pjq("input[id$=input]");
    private JQueryLocator button = pjq("input[id$=commandButton]");
    private JQueryLocator output1 = pjq("span[id$=output1]");
    private JQueryLocator output2 = pjq("span[id$=output2]");
    private JQueryLocator output3 = pjq("span[id$=output3]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/commandButton/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("A4J", "A4J Command Button", "Simple");
    }

    @Test(groups = "client-side-perf")
    public void testSimpleClick() {
        selenium.typeKeys(input, "RichFaces 4");
        selenium.click(button);

        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));

        String output = selenium.getText(output1);
        assertEquals(output, "RichFaces 4", "output1 when 'RichFaces 4' in input");

        output = selenium.getText(output2);
        assertEquals(output, "RichFa", "output2 when 'RichFaces 4' in input");

        output = selenium.getText(output3);
        assertEquals(output, "RICHFACES 4", "output3 when 'RichFaces 4' in input");
    }

    @Test
    public void testSimpleClickUnicode() {
        selenium.typeKeys(input, "ľščťžýáíéňô");
        selenium.click(button);

        waitGui.until(textEquals.locator(output1).text("ľščťžýáíéňô"));

        String output = selenium.getText(output1);
        assertEquals(output, "ľščťžýáíéňô", "output1 when 'ľščťžýáíéňô' in input");

        output = selenium.getText(output2);
        assertEquals(output, "ľščťžý", "output2 when 'ľščťžýáíéňô' in input");

        output = selenium.getText(output3);
        assertEquals(output, "ĽŠČŤŽÝÁÍÉŇÔ", "output3 when 'ľščťžýáíéňô' in input");
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testAccesskey() {
        testHtmlAttribute(button, "accesskey", "b");
    }

    @Test(groups = { "4.Future" })
    @Templates(value = { "richPopupPanel" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11296")
    public void testAccesskeyInPopupPanel() {
        testHtmlAttribute(button, "accesskey", "b");
    }

    @Test
    public void testAction() {
        JQueryLocator doubleStringAction = pjq("input[value=doubleStringAction]");
        JQueryLocator first6CharsAction = pjq("input[value=first6CharsAction]");
        JQueryLocator toUpperCaseAction = pjq("input[value=toUpperCaseAction]");

        selenium.click(doubleStringAction);
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.typeKeys(input, "RichFaces 4");
        selenium.click(button);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));
        String output = selenium.getText(output2);
        assertEquals(output, "RichFaces 4RichFaces 4",
            "output2 when 'RichFaces 4' in input and doubleStringAction selected");

        selenium.click(first6CharsAction);
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.typeKeys(input, "RichFaces 4ň");
        selenium.click(button);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ň"));
        output = selenium.getText(output2);
        assertEquals(output, "RichFa", "output2 when 'RichFaces 4ň' in input and first6CharsAction selected");

        selenium.click(toUpperCaseAction);
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.typeKeys(input, "RichFaces 4ě");
        selenium.click(button);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ě"));
        output = selenium.getText(output2);
        assertEquals(output, "RICHFACES 4Ě", "output2 when 'RichFaces 4ě' in input and toUpperCaseAction selected");
    }

    @Test
    public void testActionListener() {
        JQueryLocator doubleStringActionListener = pjq("input[value=doubleStringActionListener]");
        JQueryLocator first6CharsActionListener = pjq("input[value=first6CharsActionListener]");
        JQueryLocator toUpperCaseActionListener = pjq("input[value=toUpperCaseActionListener]");

        selenium.click(doubleStringActionListener);
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.typeKeys(input, "RichFaces 4");
        selenium.click(button);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));
        String output = selenium.getText(output3);
        assertEquals(output, "RichFaces 4RichFaces 4",
            "output2 when 'RichFaces 4' in input and doubleStringActionListener selected");

        selenium.click(first6CharsActionListener);
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.typeKeys(input, "RichFaces 4ň");
        selenium.click(button);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ň"));
        output = selenium.getText(output3);
        assertEquals(output, "RichFa", "output2 when 'RichFaces 4ň' in input and first6CharsActionListener selected");

        selenium.click(toUpperCaseActionListener);
        selenium.waitForPageToLoad(TIMEOUT);
        selenium.typeKeys(input, "RichFaces 4ě");
        selenium.click(button);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ě"));
        output = selenium.getText(output3);
        assertEquals(output, "RICHFACES 4Ě",
            "output2 when 'RichFaces 4ě' in input and toUpperCaseActionListener selected");
    }

    @Test
    public void testAlt() {
        testHtmlAttribute(button, "alt", "metamer");
    }

    @Test
    public void testDisabled() {
        JQueryLocator disabledChecbox = pjq("input[type=radio][name$=disabledInput][value=true]");
        AttributeLocator<?> disabledAttribute = button.getAttribute(new Attribute("disabled"));

        selenium.click(disabledChecbox);
        selenium.waitForPageToLoad(TIMEOUT);

        String isDisabled = selenium.getAttribute(disabledAttribute);
        assertEquals(isDisabled.toLowerCase(), "true", "The value of attribute disabled");
    }

    @Test
    public void testOnblur() {
        testFireEvent(Event.BLUR, button);
    }

    @Test
    public void testOnchange() {
        testFireEvent(Event.CHANGE, button);
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, button);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, button);
    }

    @Test
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, button);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, button);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, button);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, button);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, button);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, button);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, button);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, button);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, button);
    }

    @Test
    public void testStyle() {
        testStyle(button);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(button);
    }

    @Test
    public void testTitle() {
        testTitle(button);
    }

    @Test
    public void testValue() {
        JQueryLocator valueInput = pjq("input[id$=valueInput]");
        final AttributeLocator<?> attribute = button.getAttribute(new Attribute("value"));
        final String value = "new label";

        selenium.type(valueInput, value);
        selenium.waitForPageToLoad(TIMEOUT);

        assertEquals(selenium.getAttribute(attribute), value, "Value of the button did not change");
    }
}
