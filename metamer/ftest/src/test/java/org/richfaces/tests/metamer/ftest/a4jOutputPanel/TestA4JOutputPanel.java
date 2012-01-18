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
package org.richfaces.tests.metamer.ftest.a4jOutputPanel;

import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.dom.Event.CLICK;
import static org.jboss.arquillian.ajocado.dom.Event.DBLCLICK;
import static org.jboss.arquillian.ajocado.dom.Event.KEYDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.KEYPRESS;
import static org.jboss.arquillian.ajocado.dom.Event.KEYUP;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEMOVE;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOUT;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOVER;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEUP;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/a4jOutputPanel/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestA4JOutputPanel extends AbstractAjocadoTest {

    Event[] events = new Event[]{CLICK, DBLCLICK, KEYDOWN, KEYPRESS, KEYUP, MOUSEDOWN, MOUSEMOVE, MOUSEOUT,
        MOUSEOVER, MOUSEUP};
    String[] layouts = new String[]{"block", "inline"};
    @Inject
    @Use(empty = true)
    Event event;
    @Inject
    @Use(empty = true)
    String layout;
    private JQueryLocator increaseCounterButton = pjq("input[id$=button]");
    private JQueryLocator outputDiv = pjq("div[id$=outputPanel]");
    private JQueryLocator outputSpan = pjq("span[id$=outputPanel]");
    private JQueryLocator optionBlue = pjq("input[name$=styleClassInput][value=blue-background]");
    private JQueryLocator optionGray = pjq("input[name$=styleClassInput][value=gray-background]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jOutputPanel/simple.xhtml");
    }

    @Test
    @Uses({
        @Use(field = "event", value = "events"),
        @Use(field = "layout", value = "layouts")})
    public void testEvent() {
        JQueryLocator element = null;

        if ("inline".equals(layout)) {
            guardHttp(selenium).click(pjq("input[name$=layoutInput][value=inline]"));
            element = outputSpan;
        } else {
            element = outputDiv;
        }

        testFireEvent(event, element);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11312")
    public void testClick() {
        selenium.click(increaseCounterButton);
        waitGui.until(textEquals.locator(outputDiv).text("1"));

        selenium.click(increaseCounterButton);
        waitGui.until(textEquals.locator(outputDiv).text("2"));
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10555")
    public void testAjaxRendered() {
        JQueryLocator ajaxRenderedInput = pjq("input[type=radio][name$=ajaxRenderedInput][value=false]");
        JQueryLocator reRenderAllImage = jq("div.header img[id$=reRenderAllImage]");

        guardHttp(selenium).click(ajaxRenderedInput);

        selenium.click(increaseCounterButton);
        selenium.click(increaseCounterButton);

        String output = selenium.getText(outputDiv);
        assertEquals(output, "0", "Output after two clicks when ajaxRendered is set to false.");

        selenium.click(reRenderAllImage);
        waitGui.until(textEquals.locator(outputDiv).text("2"));
    }

    @Test
    public void testDir() {
        testDir(outputDiv);
    }

    @Test
    public void testLang() {
        testLang(outputDiv);
    }

    @Test
    public void testLayout() {
        JQueryLocator optionBlock = pjq("input[name$=layoutInput][value=block]");
        JQueryLocator optionInline = pjq("input[name$=layoutInput][value=inline]");
        JQueryLocator optionNone = pjq("input[name$=layoutInput][value=none]");

        assertTrue(selenium.isElementPresent(outputDiv), "Div should be rendered on the beginning.");
        assertFalse(selenium.isElementPresent(outputSpan), "Div should be rendered on the beginning.");

        guardHttp(selenium).click(optionInline);
        assertFalse(selenium.isElementPresent(outputDiv), "Span should be rendered when inline is set.");
        assertTrue(selenium.isElementPresent(outputSpan), "Span should be rendered when inline is set.");

        guardHttp(selenium).click(optionBlock);
        assertTrue(selenium.isElementPresent(outputDiv), "Div should be rendered when block is set.");
        assertFalse(selenium.isElementPresent(outputSpan), "Div should be rendered when block is set.");

        // TODO uncomment as soon as implemented https://issues.jboss.org/browse/RF-7819
        // selenium.click(optionNone);
        // selenium.waitForPageToLoad(TIMEOUT);
        // assertFalse(selenium.isElementPresent(outputDiv), "Span should be rendered when none is set.");
        // assertTrue(selenium.isElementPresent(outputSpan), "Span should be rendered when none is set.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11312")
    public void testRendered() {
        JQueryLocator renderedInputFalse = pjq("input[type=radio][name$=renderedInput][value=false]");
        JQueryLocator renderedInputTrue = pjq("input[type=radio][name$=renderedInput][value=true]");

        guardHttp(selenium).click(renderedInputFalse);
        assertFalse(selenium.isElementPresent(outputDiv), "Panel should not be rendered.");

        String timeValue = selenium.getText(time);
        guardXhr(selenium).click(increaseCounterButton);
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));
        timeValue = selenium.getText(time);
        guardXhr(selenium).click(increaseCounterButton);
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));

        guardHttp(selenium).click(renderedInputTrue);
        assertTrue(selenium.isElementPresent(outputDiv), "Panel should be rendered.");

        String counter = selenium.getText(outputDiv);
        assertEquals(counter, "2", "Counter after two clicks on button.");
    }

    @Test
    public void testStyle() {
        testStyle(outputDiv);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(outputDiv);
    }

    @Test
    public void testTitle() {
        testTitle(outputDiv);
    }
}
