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
package org.richfaces.tests.metamer.ftest.richCollapsiblePanel;

import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.guardNoRequest;
import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.locator.option.OptionLocatorFactory.optionLabel;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.collapsiblePanelAttributes;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.bodyClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerClass;

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
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richCollapsiblePanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23138 $
 */
public class TestRichCollapsiblePanel extends AbstractGrapheneTest {

    private JQueryLocator panel = pjq("div[id$=collapsiblePanel]");
    private JQueryLocator header = pjq("div[id$=collapsiblePanel:header]");
    private JQueryLocator headerExp = pjq("div[id$=collapsiblePanel:header] div.rf-cp-lbl-exp");
    private JQueryLocator headerColps = pjq("div[id$=collapsiblePanel:header] div.rf-cp-lbl-colps");
    private JQueryLocator content = pjq("div[id$=collapsiblePanel:content]");
    private JQueryLocator leftIcon = pjq("div[id$=collapsiblePanel] td.rf-cp-ico");
    private JQueryLocator rightIcon = pjq("td.rf-cp-exp-ico");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsiblePanel/simple.xhtml");
    }

    @Test
    public void testInit() {
        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Collapsible panel is not present on the page.");

        verifyBeforeClick();
    }

    @Test
    public void testBodyClass() {
        testStyleClass(content, bodyClass);
    }

    @Test
    public void testDir() {
        testDir(panel);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10312")
    public void testExpanded() {
        verifyBeforeClick();
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.expanded, false);
        verifyAfterClick();
    }

    @Test
    public void testHeader() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.header, "new header");

        assertEquals(selenium.getText(headerExp), "new header", "Header of the panel did not change.");

        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.header, "ľščťťžžôúňď ацущьмщфзщйцу");

        assertEquals(selenium.getText(headerExp), "ľščťťžžôúňď ацущьмщфзщйцу", "Header of the panel did not change.");
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(header, headerClass);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.immediate, true);

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(header);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "panel collapsed");
    }

    @Test
    public void testLang() {
        testLang(panel);
    }

    @Test
    public void testLeftCollapsedIcon() {
        JQueryLocator icon = leftIcon.getDescendant(jq("div.rf-cp-ico-colps"));
        JQueryLocator input = pjq("select[id$=leftCollapsedIconInput]");
        JQueryLocator image = leftIcon.getChild(jq("img:nth-child(1)"));

        // icon=null
        assertTrue(selenium.belongsClass(icon, "rf-ico-chevron-up-hdr"), "Div should have set class rf-ico-chevron-up-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("chevronUp.png"),
            "Icon should contain a chevron up.");

        verifyStandardIcons(input, icon, image);
    }

    @Test
    public void testLeftExpandedIcon() {
        JQueryLocator icon = leftIcon.getDescendant(jq("div.rf-cp-ico-exp"));
        JQueryLocator input = pjq("select[id$=leftExpandedIconInput]");
        JQueryLocator image = leftIcon.getChild(jq("img:nth-child(2)"));

        // icon=null
        assertTrue(selenium.belongsClass(icon, "rf-ico-chevron-down-hdr"), "Div should have set class rf-ico-chevron-down-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("chevronDown.png"),
            "Icon should contain a chevron down.");

        verifyStandardIcons(input, icon, image);
    }

    @Test
    public void testOnbeforeswitchOnswitch() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.onbeforeswitch, "metamerEvents += \"beforeswitch \"");
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.onswitch, "metamerEvents += \"switch \"");

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        guardXhr(selenium).click(header);

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 2, "2 events should be fired.");
        assertEquals(events[0], "beforeswitch", "Attribute onbeforeswitch doesn't work");
        assertEquals(events[1], "switch", "Attribute onswitch doesn't work");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, panel);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, panel);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, panel);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, panel);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, panel);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, panel);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, panel);
    }

    @Test
    public void testRendered() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.rendered, false);

        assertFalse(selenium.isElementPresent(panel), "Panel should not be rendered when rendered=false.");
    }

    @Test
    public void testRightCollapsedIcon() {
        JQueryLocator icon = rightIcon.getDescendant(jq("div.rf-cp-ico-colps"));
        JQueryLocator input = pjq("select[id$=rightCollapsedIconInput]");
        JQueryLocator image = rightIcon.getChild(jq("img:nth-child(1)"));

        // icon=null
        assertFalse(selenium.isElementPresent(rightIcon), "Right icon should not be present on the page.");

        verifyStandardIcons(input, icon, image);
    }

    @Test
    public void testRightExpandedIcon() {
        JQueryLocator icon = rightIcon.getDescendant(jq("div.rf-cp-ico-exp"));
        JQueryLocator input = pjq("select[id$=rightExpandedIconInput]");
        JQueryLocator image = rightIcon.getChild(jq("img:nth-child(1)"));

        // icon=null
        assertFalse(selenium.isElementPresent(rightIcon), "Right icon should not be present on the page.");

        verifyStandardIcons(input, icon, image);
    }

    @Test
    public void testStyle() {
        testStyle(panel);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(panel);
    }

    @Test
    public void testSwitchTypeNull() {
        // click to collapse
        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(header);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        verifyAfterClick();

        // click to expand
        reqTime = selenium.getText(time);
        guardXhr(selenium).click(header);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        verifyBeforeClick();
    }

    @Test
    public void testSwitchTypeAjax() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.switchType, "ajax");

        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.switchType, "client");

        // click to collapse
        guardNoRequest(selenium).click(header);
        verifyAfterClick();

        // click to expand
        guardNoRequest(selenium).click(header);
        verifyBeforeClick();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10368")
    public void testSwitchTypeServer() {
        collapsiblePanelAttributes.set(CollapsiblePanelAttributes.switchType, "server");

        // click to collapse
        guardHttp(selenium).click(header);
        verifyAfterClick();

        // click to expand
        guardHttp(selenium).click(header);
        verifyBeforeClick();
    }

    @Test
    public void testTitle() {
        testTitle(panel);
    }

    @Test
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "richList",
            "a4jRepeat" })
    public void testToggleListener() {
        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(header);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        phaseInfo.assertListener(PhaseId.INVOKE_APPLICATION, "panel collapsed");

        reqTime = selenium.getText(time);
        guardXhr(selenium).click(header);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        phaseInfo.assertListener(PhaseId.INVOKE_APPLICATION, "panel expanded");
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11568")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "richList",
            "a4jRepeat" })
    public void testToggleListenerInIterationComponents() {
        testToggleListener();
    }

    private void verifyBeforeClick() {
        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Collapsible panel is not present on the page.");

        displayed = selenium.isVisible(headerExp);
        assertTrue(displayed, "Expanded header should be visible.");

        displayed = selenium.isVisible(headerColps);
        assertFalse(displayed, "Collapsed header should not be visible.");

        displayed = selenium.isVisible(content);
        assertTrue(displayed, "Panel's content should be visible.");

        String text = selenium.getText(headerExp);
        assertEquals(text, "collapsible panel header", "Header of the panel.");

        text = selenium.getText(content);
        assertTrue(text.startsWith("Lorem ipsum"), "Panel doesn't contain Lorem ipsum in its content.");
    }

    private void verifyAfterClick() {
        boolean displayed = selenium.isVisible(panel);
        assertTrue(displayed, "Collapsible panel is not present on the page.");

        displayed = selenium.isVisible(headerExp);
        assertFalse(displayed, "Expanded header should not be visible.");

        displayed = selenium.isVisible(headerColps);
        assertTrue(displayed, "Collapsed header should be visible.");

        if (selenium.isElementPresent(content)) {
            displayed = selenium.isVisible(content);
            assertFalse(displayed, "Panel's content should not be visible.");
        }

        String text = selenium.getText(headerColps);
        assertEquals(text, "collapsible panel header", "Header of the panel.");
    }

    private void verifyStandardIcons(JQueryLocator input, JQueryLocator icon, JQueryLocator image) {
        selenium.select(input, optionLabel("chevron"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-chevron-hdr"), "Div should have set class rf-ico-chevron-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("chevron.png"),
            "Icon should contain a chevron.");

        selenium.select(input, optionLabel("chevronDown"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-chevron-down-hdr"), "Div should have set class rf-ico-chevron-down-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("chevronDown.png"),
            "Icon should contain a chevron down.");

        selenium.select(input, optionLabel("chevronUp"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-chevron-up-hdr"), "Div should have set class rf-ico-chevron-up-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("chevronUp.png"),
            "Icon should contain a chevron up.");

        selenium.select(input, optionLabel("transparent"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-transparent-hdr"), "Div should have set class rf-ico-transparent-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).equals("none"), "Icon should not contain any image.");

        selenium.select(input, optionLabel("disc"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-disc-hdr"), "Div should have set class rf-ico-disc-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("disc.png"), "Icon should contain a disc.");

        selenium.select(input, optionLabel("grid"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-grid-hdr"), "Div should have set class rf-ico-grid-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("grid.png"), "Icon should contain a grid.");

        selenium.select(input, optionLabel("triangle"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-triangle-hdr"), "Div should have set class rf-ico-triangle-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("triangle.png"),
            "Icon should contain a triangle.");

        selenium.select(input, optionLabel("triangleDown"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-triangle-down-hdr"),
            "Div should have set class rf-ico-triangle-down-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("triangleDown.png"),
            "Icon should contain a triangle down.");

        selenium.select(input, optionLabel("triangleUp"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.belongsClass(icon, "rf-ico-triangle-up-hdr"), "Div should have set class rf-ico-triangle-up-hdr.");
        assertTrue(selenium.getStyle(icon, CssProperty.BACKGROUND_IMAGE).contains("triangleUp.png"),
            "Icon should contain a triangle up.");

        selenium.select(input, optionLabel("none"));
        selenium.waitForPageToLoad();
        assertFalse(selenium.isElementPresent(icon), "Icon should not be present when icon=none.");

        selenium.select(input, optionLabel("star"));
        selenium.waitForPageToLoad();
        assertFalse(selenium.isElementPresent(icon), "Icon's div should not be present when icon=star.");
        assertTrue(selenium.isElementPresent(image), "Icon's image should be rendered.");
        assertTrue(selenium.getAttribute(image.getAttribute(Attribute.SRC)).contains("star.png"),
            "Icon's src attribute should contain star.png.");

        selenium.select(input, optionLabel("nonexisting"));
        selenium.waitForPageToLoad();
        assertFalse(selenium.isElementPresent(icon), "Icon's div should not be present when icon=nonexisting.");
        assertTrue(selenium.isElementPresent(image), "Icon's image should be rendered.");
        assertTrue(selenium.getAttribute(image.getAttribute(Attribute.SRC)).contains("nonexisting"),
            "Icon's src attribute should contain nonexisting.");
    }
}
