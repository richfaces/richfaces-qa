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
package org.richfaces.tests.metamer.ftest.richPickList;

import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.Graphene.countEquals;
import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.textEquals;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.pickListAttributes;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.addAllText;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.addText;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.downBottomText;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.downText;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.immediate;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.itemClass;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.listHeight;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.listWidth;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.maxListHeight;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.minListHeight;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.onadditems;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.onremoveitems;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.orderable;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.removeAllText;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.removeText;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.required;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.requiredMessage;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.selectItemClass;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.sourceCaption;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.switchByClick;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.switchByDblClick;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.targetCaption;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.upText;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.upTopText;
import static org.richfaces.tests.metamer.ftest.richPickList.PickListAttributes.validatorMessage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test for rich:pickList on page faces/components/richPickList/simple.xhtml.
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 */
public class TestPickList extends AbstractGrapheneTest {

    private JQueryLocator phaseListenerFormat = jq("div#phasesPanel li:eq({0})");

    private JQueryLocator hSubmit = pjq("input[id$=hButton]");
    private JQueryLocator a4jSubmit = pjq("input[id$=a4jButton]");

    private JQueryLocator pickListTop = pjq("div.rf-pick[id$=pickList]");
    private JQueryLocator pickListSource = pjq("div.rf-pick-src div.rf-pick-lst-dcrtn"); // this locator works for
                                                                                         // sourcemouseover event
    private JQueryLocator pickListTarget = pjq("div.rf-pick-tgt div.rf-pick-lst-dcrtn");
    private JQueryLocator output = pjq("span[id$=output]");
    private JQueryLocator pickListMsgBox = pjq("span.rf-msg-err[id$=pickList] > span.rf-msg-det");
    private JQueryLocator targetCaptionLocator = pjq("div[id$=pickListTargetList] > div.rf-pick-tgt-cptn");
    private JQueryLocator sourceCaptionLocator = pjq("div[id$=pickListSourceList] > div.rf-pick-src-cptn");

    private JQueryLocator pickListCtrl = pjq("div.rf-pick-lst-scrl");
    private JQueryLocator pickListOrderingControlsContainer = pjq("table[id$=pickListTarget] td:eq(1)");
    private JQueryLocator pickListOrderingCtrlFormat = pickListOrderingControlsContainer
        .getDescendant(jq("tr td button:contains({0})"));

    private JQueryLocator pickListSrcItems = pjq("div[id$=pickListSourceItems]");
    private JQueryLocator pickListSrcItem = pickListSrcItems.getChild(jq("div[id$=pickListItem{0}]"));
    private JQueryLocator pickListSrcItemByText = pickListSrcItems.getChild(jq("div.rf-pick-opt:contains({0})"));

    private JQueryLocator pickListTargetItems = pjq("div[id$=pickListTargetItems]");
    private JQueryLocator pickListTargetItem = pickListTargetItems.getChild(jq("div[id$=pickListItem{0}]"));
    private JQueryLocator pickListTargetItemByText = pickListTargetItems.getChild(jq("div.rf-pick-opt:contains({0})"));

    // after click on 'add' button, item is moved to 'staging' container (not child of #pickListTargetItems)
    private JQueryLocator pickListTrgtItemsStage = pjq("div[id$=pickListTargetList] div.rf-pick-lst-scrl");
    private JQueryLocator pickListTrgtItemStage = pickListTrgtItemsStage.getDescendant(jq("div[id$=pickListItem{0}]"));
    private JQueryLocator pickListTrgtItemStageByText = pickListTrgtItemsStage
        .getDescendant(jq("div.rf-pick-opt:contains({0})"));

    private JQueryLocator addBtn = pjq("button.rf-pick-add");
    private JQueryLocator addAllBtn = pjq("button.rf-pick-add-all");
    private JQueryLocator removeBtn = pjq("button.rf-pick-rem");
    private JQueryLocator removeAllBtn = pjq("button.rf-pick-rem-all");

    private Attribute classAttr = new Attribute("class");
    private Attribute disabledAttr = new Attribute("disabled");
    private Attribute styleAttr = new Attribute("style");

    private String STYLE_CLASS_ITEM_DISABLED = "rf-pick-opt-dis";
    private String STYLE_CLASS_BTN_DISABLED = "rf-pick-btn-dis";
    private String STYLE_CLASS_ITEM_SELECTED = "rf-pick-sel";
    private String STYLE_CLASS_ORD_BTN_DISABLED = "rf-ord-btn-dis";

    private String phaseListenerLogFormat = "*3 value changed: {0} -> {1}";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/simple.xhtml");
    }

    private void addItem(int id) {
        selenium.click(pickListSrcItem.format(id));
        selenium.click(addBtn);
    }

    private void addItem(String value) {
        selenium.click(pickListSrcItemByText.format(value));
        selenium.click(addBtn);
    }

    private void removeItem(int id) {

        if (selenium.isElementPresent(pickListTrgtItemStage.format(id))) {
            selenium.click(pickListTrgtItemStage.format(id));
        } else {
            // if doesn't exist at this point, there we don't know what happens
            selenium.click(pickListTargetItem.format(id));
        }

        selenium.click(removeBtn);
    }

    private void removeItem(String value) {
        selenium.click(pickListTargetItemByText.format(value));
        selenium.click(removeBtn);
    }

    private void addAllItems() {
        selenium.click(addAllBtn);
    }

    private void removeAllItems() {
        selenium.click(removeAllBtn);
    }

    private String getOrderBtnClass(String label) {
        return selenium.getAttribute(pickListOrderingCtrlFormat.format(label).getAttribute(classAttr));
    }

    @Test
    public void testAddAllText() {
        String label = "xxx";
        pickListAttributes.set(addAllText, label);
        assertTrue(label.equals(selenium.getText(addAllBtn)));
    }

    @Test
    public void testAddText() {
        String label = "xxx";
        pickListAttributes.set(addText, label);
        assertTrue(label.equals(selenium.getText(addBtn)));
    }

    // @Test
    public void testColumnClasses() {
        // move to columns layout test
    }

    // @Test
    public void testVar() {
        // move to columns layout test
    }

    @Test
    public void testDisabled() {
        // add first item [idx=0], then submit and disable
        addItem(0);
        waitGui.until(elementPresent.locator(pickListTargetItem.format(0)));
        selenium.click(hSubmit);
        selenium.waitForPageToLoad();

        // add all and remove all buttons should be enabled, the other two disabled
        assertTrue(selenium.isAttributePresent(addBtn.getAttribute(disabledAttr)));
        assertFalse(selenium.isAttributePresent(addAllBtn.getAttribute(disabledAttr)));
        assertTrue(selenium.isAttributePresent(removeBtn.getAttribute(disabledAttr)));
        assertFalse(selenium.isAttributePresent(removeAllBtn.getAttribute(disabledAttr)));

        pickListAttributes.set(disabled, Boolean.TRUE);

        // check if source items are disabled (check second - first item after 0 item added)
        String pickListSourceItemClass = selenium.getAttribute(pickListSrcItem.format(1).getAttribute(classAttr));
        assertTrue(pickListSourceItemClass.contains(STYLE_CLASS_ITEM_DISABLED));

        // all buttons should be disabled
        assertTrue(selenium.getAttribute(addBtn.getAttribute(classAttr)).contains(STYLE_CLASS_BTN_DISABLED));
        assertTrue(selenium.getAttribute(addAllBtn.getAttribute(classAttr)).contains(STYLE_CLASS_BTN_DISABLED));
        assertTrue(selenium.getAttribute(removeBtn.getAttribute(classAttr)).contains(STYLE_CLASS_BTN_DISABLED));
        assertTrue(selenium.getAttribute(removeAllBtn.getAttribute(classAttr)).contains(STYLE_CLASS_BTN_DISABLED));

        assertTrue(selenium.isAttributePresent(addBtn.getAttribute(disabledAttr)));
        assertTrue(selenium.isAttributePresent(addAllBtn.getAttribute(disabledAttr)));
        assertTrue(selenium.isAttributePresent(removeBtn.getAttribute(disabledAttr)));
        assertTrue(selenium.isAttributePresent(removeAllBtn.getAttribute(disabledAttr)));

        // check item added to target list if disabled (remember - item index in id didn't change)
        assertTrue(selenium.getAttribute(pickListTargetItem.format(0).getAttribute(classAttr)).contains(
            STYLE_CLASS_ITEM_DISABLED));
    }

    @Test
    public void testDisabledClass() {
        String disabledClass = "my-cool-disabled-class";
        pickListAttributes.set(PickListAttributes.disabledClass, disabledClass);
        pickListAttributes.set(disabled, Boolean.TRUE);

        String found = selenium.getAttribute(pickListTop.getAttribute(classAttr));
        assertTrue(found != null);
        assertTrue(found.contains(disabledClass), "Following class " + found + " doesn't coitains " + disabledClass);
    }

    @Test
    public void testDownBottomText() {
        pickListAttributes.set(downBottomText, "xxx");
        pickListAttributes.set(orderable, Boolean.TRUE);
        assertEquals(selenium.getText(pickListOrderingControlsContainer.getDescendant(jq("button:eq(3)"))), "xxx",
            "Button's text did not change.");
    }

    @Test
    public void testDownText() {
        pickListAttributes.set(downText, "xxx");
        pickListAttributes.set(orderable, Boolean.TRUE);
        assertEquals(selenium.getText(pickListOrderingControlsContainer.getDescendant(jq("button:eq(2)"))), "xxx",
            "Button's text did not change.");
    }

    /**
     * This attribute is supposed to be used with column layout
     */
    @Test
    public void testHeaderClass() {
        // move to columns layout test
        /*
         * String headerClass = "my-cool-header-class"; pickListAttributes.setHeaderClass(headerClass);
         *
         * String found = selenium.getAttribute(pickListTop.getAttribute(classAttr)); assertTrue(found != null);
         * assertTrue(found.contains(headerClass));
         */
    }

    @Test
    public void testImmediate() {
        pickListAttributes.set(immediate, Boolean.TRUE);
        addItem("richfaces");
        selenium.click(a4jSubmit);

        // for immediate submit is record in 3rd line
        waitModel.until(textEquals.locator(phaseListenerFormat.format(2)).text(
            format(phaseListenerLogFormat, "[]", "[richfaces]")));

    }

    @Test
    public void testItemClass() {
        String value = "xxx";
        pickListAttributes.set(itemClass, value);

        String classVal = selenium.getAttribute(pickListSrcItemByText.format("richfaces").getAttribute(classAttr));
        assertTrue(classVal != null);
        assertTrue(classVal.contains(value));
    }

    @Test
    public void testListHeight() {
        String value = "333";
        pickListAttributes.set(listHeight, value);

        String found = selenium.getAttribute(pickListCtrl.getAttribute(styleAttr));

        assertTrue(found != null);
        assertTrue(found.contains(value));
    }

    @Test
    public void testListWidth() {
        String value = "222";
        pickListAttributes.set(listWidth, value);

        String found = selenium.getAttribute(pickListCtrl.getAttribute(styleAttr));

        assertTrue(found != null);
        assertTrue(found.contains(value));
    }

    @Test
    public void testMaxListHeight() {
        String value = "345";
        pickListAttributes.set(maxListHeight, value);

        String found = selenium.getAttribute(pickListCtrl.getAttribute(styleAttr));

        assertTrue(found != null);
        assertTrue(found.contains(value));
    }

    @Test
    public void testMinListHeight() {
        String value = "234";
        pickListAttributes.set(minListHeight, value);

        String found = selenium.getAttribute(pickListCtrl.getAttribute(styleAttr));

        assertTrue(found != null);
        assertTrue(found.contains(value));
    }

    @Test
    public void testOnadditems() {
        pickListAttributes.set(onadditems, "metamerEvents += \"additems \"");
        addItem("North Carolina");
        waitGui.failWith("Attribute onadditems does not work correctly").until(
            new EventFiredCondition(new Event("additems")));
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnblur() {
        ElementLocator<?> eventInput = pjq("input[id$=onblurInput]");
        String value = "metamerEvents += \"blur \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.fireEvent(addAllBtn, Event.FOCUS);
        selenium.fireEvent(addAllBtn, Event.BLUR);

        waitGui.failWith("Attribute onblur does not work correctly").until(new EventFiredCondition(Event.BLUR));
    }

    @Test
    public void testOnchange() {
        testFireEvent(Event.CHANGE, pickListTop, "change");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, pickListTop);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, pickListTop);
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, pickListTop);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, pickListTop);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, pickListTop);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, pickListTop);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, pickListTop);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, pickListTop);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, pickListTop);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, pickListTop);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, pickListTop);
    }

    @Test
    public void testOnremoveitems() {
        pickListAttributes.set(onremoveitems, "metamerEvents += \"removeitems \"");
        addItem("North Carolina");
        addItem("Hawaii");
        addItem("Arizona");
        removeItem("Hawaii");
        waitGui.failWith("Attribute onremoveitems does not work correctly").until(
            new EventFiredCondition(new Event("removeitems")));

    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11322")
    public void testOnsourceblur() {
        ElementLocator<?> eventInput = pjq("input[id$=onsourceblurInput]");
        String value = "metamerEvents += \"blur \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.click(pickListSrcItems);
        selenium.click(pickListTargetItems);

        waitGui.failWith("Attribute onsourceblur does not work correctly").until(new EventFiredCondition(Event.BLUR));
    }

    @Test
    public void testOnsourceclick() {
        testFireEvent(Event.CLICK, pickListSource, "sourceclick");
    }

    @Test
    public void testOnsourcedblclick() {
        testFireEvent(Event.DBLCLICK, pickListSource, "sourcedblclick");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11322")
    public void testOnsourcefocus() {
        ElementLocator<?> eventInput = pjq("input[id$=onsourcefocusInput]");
        String value = "metamerEvents += \"focus \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.click(pickListSrcItems);

        waitGui.failWith("Attribute onsourcefocus does not work correctly").until(new EventFiredCondition(Event.FOCUS));
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnsourcekeydown() {
        ElementLocator<?> eventInput = pjq("input[id$=onsourcekeydownInput]");
        String value = "metamerEvents += \"keydown \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.fireEvent(pickListSrcItems, Event.KEYDOWN);
        JavaScript script = new JavaScript("window.jQuery('div[id$=pickListSourceItems]').keydown()");
        selenium.getEval(script);

        waitGui.failWith("Attribute onsourcekeydown does not work correctly").until(
            new EventFiredCondition(Event.KEYDOWN));
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnsourcekeypress() {
        ElementLocator<?> eventInput = pjq("input[id$=onsourcekeypressInput]");
        String value = "metamerEvents += \"keypress \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.fireEvent(pickListSrcItems, Event.KEYPRESS);
        JavaScript script = new JavaScript("window.jQuery('div[id$=pickListSourceItems]').keypress()");
        selenium.getEval(script);

        waitGui.failWith("Attribute onsourcekeypress does not work correctly").until(
            new EventFiredCondition(Event.KEYPRESS));
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOnsourcekeyup() {
        ElementLocator<?> eventInput = pjq("input[id$=onsourcekeyupInput]");
        String value = "metamerEvents += \"keyup \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.fireEvent(pickListSrcItems, Event.KEYUP);
        JavaScript script = new JavaScript("window.jQuery('div[id$=pickListSourceItems]').keyup()");
        selenium.getEval(script);

        waitGui.failWith("Attribute onsourcekeyup does not work correctly").until(new EventFiredCondition(Event.KEYUP));
    }

    @Test
    public void testOnsourcemousedown() {
        testFireEvent(Event.MOUSEDOWN, pickListSource, "sourcemousedown");
    }

    @Test
    public void testOnsourcemousemove() {
        testFireEvent(Event.MOUSEMOVE, pickListSource, "sourcemousemove");
    }

    @Test
    public void testOnsourcemouseout() {
        testFireEvent(Event.MOUSEOUT, pickListSource, "sourcemouseout");
    }

    @Test
    public void testOnsourcemouseover() {
        testFireEvent(Event.MOUSEOVER, pickListSource, "sourcemouseover");
    }

    @Test
    public void testOnsourcemouseup() {
        testFireEvent(Event.MOUSEUP, pickListSource, "sourcemouseup");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11322")
    public void testOntargetblur() {
        ElementLocator<?> eventInput = pjq("input[id$=ontargetblurInput]");
        String value = "metamerEvents += \"blur \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.click(pickListTargetItems);
        selenium.click(pickListSrcItems);

        waitGui.failWith("Attribute ontargetblur does not work correctly").until(new EventFiredCondition(Event.BLUR));
    }

    @Test
    public void testOntargetclick() {
        testFireEvent(Event.CLICK, pickListTarget, "targetclick");
    }

    @Test
    public void testOntargetdblclick() {
        testFireEvent(Event.DBLCLICK, pickListTarget, "targetdblclick");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11322")
    public void testOntargetfocus() {
        ElementLocator<?> eventInput = pjq("input[id$=ontargetfocusInput]");
        String value = "metamerEvents += \"focus \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.click(pickListTargetItems);

        waitGui.failWith("Attribute ontargetfocus does not work correctly").until(new EventFiredCondition(Event.FOCUS));
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOntargetkeydown() {
        ElementLocator<?> eventInput = pjq("input[id$=ontargetkeydownInput]");
        String value = "metamerEvents += \"keydown \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.fireEvent(pickListTargetItems, Event.KEYDOWN);
        JavaScript script = new JavaScript("window.jQuery('div[id$=pickListTargetItems]').keydown()");
        selenium.getEval(script);

        waitGui.failWith("Attribute ontargetkeydown does not work correctly").until(
            new EventFiredCondition(Event.KEYDOWN));
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOntargetkeypress() {
        ElementLocator<?> eventInput = pjq("input[id$=ontargetkeypressInput]");
        String value = "metamerEvents += \"keypress \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.fireEvent(pickListTargetItems, Event.KEYPRESS);
        JavaScript script = new JavaScript("window.jQuery('div[id$=pickListTargetItems]').keypress()");
        selenium.getEval(script);

        waitGui.failWith("Attribute ontargetkeypress does not work correctly").until(
            new EventFiredCondition(Event.KEYPRESS));
    }

    @Test
    @IssueTracking({ "https://issues.jboss.org/browse/RFPL-1659", "https://issues.jboss.org/browse/RF-11322" })
    public void testOntargetkeyup() {
        ElementLocator<?> eventInput = pjq("input[id$=ontargetkeyupInput]");
        String value = "metamerEvents += \"keyup \"";

        guardHttp(selenium).type(eventInput, value);
        selenium.fireEvent(pickListTargetItems, Event.KEYUP);
        JavaScript script = new JavaScript("window.jQuery('div[id$=pickListTargetItems]').keyup()");
        selenium.getEval(script);

        waitGui.failWith("Attribute ontargetkeyup does not work correctly").until(new EventFiredCondition(Event.KEYUP));
    }

    @Test
    public void testOntargetmousedown() {
        testFireEvent(Event.MOUSEDOWN, pickListTarget, "targetmousedown");
    }

    @Test
    public void testOntargetmousemove() {
        testFireEvent(Event.MOUSEMOVE, pickListTarget, "targetmousemove");
    }

    @Test
    public void testOntargetmouseout() {
        testFireEvent(Event.MOUSEOUT, pickListTarget, "targetmouseout");
    }

    @Test
    public void testOntargetmouseover() {
        testFireEvent(Event.MOUSEOVER, pickListTarget, "targetmouseover");
    }

    @Test
    public void testOntargetmouseup() {
        testFireEvent(Event.MOUSEUP, pickListTarget, "targetmouseup");
    }

    @Test
    public void testOrderable() {

        // firstly check ordering controls doesn't appear near pickList if not "orderable"
        pickListAttributes.set(orderable, Boolean.FALSE);
        assertFalse(selenium.isElementPresent(pickListOrderingControlsContainer));

        // then make sure that controls appear near pickList when allow ordering behavior
        pickListAttributes.set(orderable, Boolean.TRUE);
        assertTrue(selenium.isElementPresent(pickListOrderingControlsContainer));

        // then add some items to target list
        addItem("Alaska");
        addItem("California");
        addItem("Delaware");

        // all items should remain selected and in this case ordering controls should be disabled
        String classAttrVal = selenium.getAttribute(pickListTrgtItemStageByText.format("Alaska")
            .getAttribute(classAttr));

        assertTrue(classAttrVal != null);
        assertTrue(classAttrVal.contains(STYLE_CLASS_ITEM_SELECTED));

        classAttrVal = selenium.getAttribute(pickListTrgtItemStageByText.format("California").getAttribute(classAttr));

        assertTrue(classAttrVal != null);
        assertTrue(classAttrVal.contains(STYLE_CLASS_ITEM_SELECTED));

        classAttrVal = selenium.getAttribute(pickListTrgtItemStageByText.format("Delaware").getAttribute(classAttr));

        assertTrue(classAttrVal != null);
        assertTrue(classAttrVal.contains(STYLE_CLASS_ITEM_SELECTED));

        // so check ordering controls if they are disabled
        assertTrue(getOrderBtnClass("First").contains(STYLE_CLASS_ORD_BTN_DISABLED));
        assertTrue(getOrderBtnClass("Up").contains(STYLE_CLASS_ORD_BTN_DISABLED));
        assertTrue(getOrderBtnClass("Down").contains(STYLE_CLASS_ORD_BTN_DISABLED));
        assertTrue(getOrderBtnClass("Last").contains(STYLE_CLASS_ORD_BTN_DISABLED));

        // now is time to select one item. This should cause ordering controls enable
        selenium.click(pickListTrgtItemStageByText.format("Alaska"));

        // since it was first item, "Down" and "Last" buttons should be enabled
        assertTrue(getOrderBtnClass("First").contains(STYLE_CLASS_ORD_BTN_DISABLED));
        assertTrue(getOrderBtnClass("Up").contains(STYLE_CLASS_ORD_BTN_DISABLED));
        assertFalse(getOrderBtnClass("Down").contains(STYLE_CLASS_ORD_BTN_DISABLED));
        assertFalse(getOrderBtnClass("Last").contains(STYLE_CLASS_ORD_BTN_DISABLED));

        // move "Alaska" to last
        selenium.click(pickListOrderingCtrlFormat.format("Last"));

        // verify that "Alaska" is last item (select 3rd item, and verify text)
        assertTrue("Alaska".equals(selenium.getText(pickListTrgtItemsStage.getDescendant(jq("div.rf-pick-opt:eq(2)")))));

        // then move "Alaska" one step "up"
        selenium.click(pickListOrderingCtrlFormat.format("Up"));
        assertTrue("Alaska".equals(selenium.getText(pickListTrgtItemsStage.getDescendant(jq("div.rf-pick-opt:eq(1)")))));

        // and then verify if all items are submitted in user defined order as well
        selenium.click(a4jSubmit);
        waitModel.until(textEquals.locator(output).text("[California, Alaska, Delaware]"));

    }

    @Test
    public void testRemoveAllText() {
        String label = "xxx";
        pickListAttributes.set(removeAllText, label);
        assertTrue(label.equals(selenium.getText(removeAllBtn)));
    }

    @Test
    public void testRemoveText() {
        String label = "xxx";
        pickListAttributes.set(removeText, label);
        assertTrue(label.equals(selenium.getText(removeBtn)));
    }

    @Test
    public void testRendered() {
        pickListAttributes.set(rendered, Boolean.FALSE);
        assertFalse(selenium.isElementPresent(pickListTop));
    }

    @Test
    public void testRequired() {
        pickListAttributes.set(required, Boolean.TRUE);
        addItem("richfaces");
        selenium.click(a4jSubmit);

        assertFalse(selenium.isElementPresent(pickListMsgBox));

        removeAllItems();
        selenium.click(a4jSubmit);

        waitModel.until(elementPresent.locator(pickListMsgBox));
        assertTrue(selenium.isElementPresent(pickListMsgBox));
    }

    @Test
    public void testRequiredMessage() {
        String value = "Test validation message";
        pickListAttributes.set(requiredMessage, value);

        pickListAttributes.set(required, Boolean.TRUE);
        selenium.click(a4jSubmit);

        assertEquals(selenium.getText(pickListMsgBox), value);
    }

    @Test
    public void testSelectItemClass() {
        String value = "my-select-item-class";
        pickListAttributes.set(selectItemClass, value);

        selenium.click(pickListSrcItemByText.format("richfaces"));

        String classVal = selenium.getAttribute(pickListSrcItemByText.format("richfaces").getAttribute(classAttr));
        assertTrue(classVal.contains(value));
    }

    @Test
    public void testSourceCaption() {
        String caption = "This is source";
        pickListAttributes.set(sourceCaption, caption);

        waitModel.until(textEquals.locator(sourceCaptionLocator).text(caption));
    }

    @Test
    public void testStyle() {
        testStyle(pickListTop);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(pickListTop);
    }

    @Test
    public void testTargetCaption() {
        String caption = "This is target";
        pickListAttributes.set(targetCaption, caption);

        waitModel.until(elementPresent.locator(targetCaptionLocator));
        assertTrue(caption.equals(selenium.getText(targetCaptionLocator)));
    }

    @Test
    public void testUpText() {
        pickListAttributes.set(upText, "xxx");
        pickListAttributes.set(orderable, Boolean.TRUE);
        assertEquals(selenium.getText(pickListOrderingControlsContainer.getDescendant(jq("button:eq(1)"))), "xxx",
            "Button's text did not change.");
    }

    @Test
    public void testUpTopText() {
        pickListAttributes.set(upTopText, "xxx");
        pickListAttributes.set(orderable, Boolean.TRUE);
        assertEquals(selenium.getText(pickListOrderingControlsContainer.getDescendant(jq("button:eq(0)"))), "xxx",
            "Button's text did not change.");
    }

    @Test
    public void testValidator() {
        addAllItems();
        selenium.click(a4jSubmit);

        // following message is predefined by validator
        waitModel.until(textEquals.locator(pickListMsgBox).text("We are sorry, but @ is not allowed to join us!"));
    }

    @Test
    public void testValidatorMessage() {
        String message = "We are sorry, but @ is not allowed to join us! Custom validatorMessage.";
        pickListAttributes.set(validatorMessage, message);
        addAllItems();
        selenium.click(a4jSubmit);

        // verify our own validation message
        waitModel.until(textEquals.locator(pickListMsgBox).text(message));
    }

    @Test
    public void testValue() {
        addItem("richfaces"); // "richfaces"
        selenium.click(a4jSubmit);

        waitModel.until(textEquals.locator(output).text("[richfaces]"));
    }

    @Test
    public void testValueChangeListener() {
        addItem("richfaces"); //

        selenium.click(a4jSubmit);

        // valueChangeListener output as 4th record
        waitModel.until(textEquals.locator(phaseListenerFormat.format(3)).text(
            format(phaseListenerLogFormat, "[]", "[richfaces]")));

        addItem("Alabama");
        selenium.click(a4jSubmit);

        // valueChangeListener output as 4th record
        waitModel.until(textEquals.locator(phaseListenerFormat.format(3)).text(
            format(phaseListenerLogFormat, "[richfaces]", "[richfaces, Alabama]")));
    }

    /**
     * Verify that addBtn is disabled until item from source items picked
     */
    @Test
    public void testDisableAddBtn() {
        int itemIdx = 0;

        String addBtnClass = selenium.getAttribute(addBtn.getAttribute(classAttr));

        // assert that button is visually disabled
        assertTrue(addBtnClass != null);
        assertTrue(addBtnClass.contains(STYLE_CLASS_BTN_DISABLED));

        selenium.click(pickListSrcItem.format(itemIdx));

        addBtnClass = selenium.getAttribute(addBtn.getAttribute(classAttr));

        assertFalse(addBtnClass.contains(STYLE_CLASS_BTN_DISABLED));

    }

    /**
     * Verify that removeBtn is disabled until item from target items picked
     */
    @Test
    public void testDisableRemoveBtn() {
        int itemIdx = 0;

        addAllItems();

        String removeBtnClass = selenium.getAttribute(removeBtn.getAttribute(classAttr));

        // assert that button is visually disabled
        assertTrue(removeBtnClass != null);
        assertTrue(removeBtnClass.contains(STYLE_CLASS_BTN_DISABLED));

        selenium.click(pickListTrgtItemStage.format(itemIdx));

        removeBtnClass = selenium.getAttribute(removeBtn.getAttribute(classAttr));

        assertFalse(removeBtnClass.contains(STYLE_CLASS_BTN_DISABLED));
    }

    /**
     * Verify addAll button working correctly
     */
    @Test
    public void testAddAllBtn() {
        addAllItems();

        waitModel.until(countEquals.count(54).locator(pickListTrgtItemsStage.getDescendant(jq("div.rf-pick-opt"))));
    }

    /**
     * Verify removeAll button working correctly
     */
    @Test
    public void testRemoveAllBtn() {
        int itemAtIdx = 50;
        addAllItems();

        // validator doesn't allow '@', so remove it before save
        removeItem(itemAtIdx);

        selenium.click(hSubmit);
        waitModel.until(countEquals.count(53).locator(pickListTargetItems.getChild(jq("div.rf-pick-opt"))));

        removeAllItems();
        // nothing should remain in target container
        waitModel.until(countEquals.count(0).locator(pickListTargetItems.getChild(jq("div.rf-pick-opt"))));

    }

    /**
     * Verify submit by JSF submit button
     */
    @Test
    public void testSaveJSF() {
        int itemIdx = 0;
        addItem(itemIdx);

        // verify that item "added" to target list really appears in target (staging) container.
        waitModel.until(elementPresent.locator(pickListTrgtItemStage.format(itemIdx)));

        // submit by JSF submit btn:
        selenium.click(hSubmit);

        // now item moved to target and submitted should appear in target (not staging)
        waitModel.until(elementPresent.locator(pickListTargetItem.format(itemIdx)));

        // todo check output!
    }

    /**
     * Verify submit by ajax button
     */
    @Test
    public void testSaveAjax() {
        int itemIdx = 0;
        addItem(itemIdx);

        // verify that item "added" to target list really appears in target (staging) container.
        waitModel.until(elementPresent.locator(pickListTrgtItemStage.format(itemIdx)));

        // submit by ajax btn:
        selenium.click(a4jSubmit);

        // and here is difference between jsf and ajax submit.
        // With ajax, submitted item doesn'e move from stage to target
        waitModel.until(elementPresent.locator(pickListTrgtItemStage.format(itemIdx)));

        // now call rerenderAll on whole page
        selenium.click(rerenderAllIcon);

        // now item moved to target and submitted should appear in target (not staging)
        waitModel.until(elementPresent.locator(pickListTargetItem.format(itemIdx)));

        // todo check output!
    }

    /**
     * Verify that item keep selected even moved from source to target, or back. If selected Alaska from sources, and
     * then added to target, it should remain selected in target list
     */
    @Test
    public void testKeepSelected() {
        int itemIdx = 0;

        String classBeforeSelect = selenium.getAttribute(pickListSrcItem.format(itemIdx).getAttribute(classAttr));
        assertFalse(classBeforeSelect.contains(STYLE_CLASS_ITEM_SELECTED),
            "pickList item shouldn't be selected before any click made! Current class(es) found: " + classBeforeSelect);

        selenium.click(pickListSrcItem.format(itemIdx));

        String pickedItemText = selenium.getText(pickListSrcItem.format(itemIdx));

        String classAfterSelect = selenium.getAttribute(pickListSrcItem.format(itemIdx).getAttribute(classAttr));
        assertTrue(classAfterSelect.contains(STYLE_CLASS_ITEM_SELECTED),
            "pickList item should be selected after click on item was made. Current class(es) found: "
                + classAfterSelect);

        // now move selected item to target list
        selenium.click(addBtn);

        waitGui.until(elementPresent.locator(pickListTrgtItemStage.format(0)));

        // and check if item remain selected
        String classAfterMove = selenium.getAttribute(pickListTrgtItemStage.format(itemIdx).getAttribute(classAttr));
        assertTrue(classAfterMove.contains(STYLE_CLASS_ITEM_SELECTED),
            "pickList item should keep selected after move to target list. Current class(es) found: " + classAfterMove);

        // verify that the same text is within item with the same ID index
        assertEquals(selenium.getText(pickListTrgtItemStage.format(itemIdx)), pickedItemText);
    }

    /**
     * Verify that selected item is moved to appropriate list after click on add/remove button
     */
    @Test
    public void testMoveItem() {
        int itemIdx = 5;

        // select item
        selenium.click(pickListSrcItem.format(itemIdx));

        String pickedItemText = selenium.getText(pickListSrcItem.format(itemIdx));

        // add item to target list
        selenium.click(addBtn);

        // item index doesn't change when moved to target list
        assertTrue(selenium.isElementPresent(pickListTrgtItemStage.format(itemIdx)));
        // verify that the same text is within item with the same ID index
        assertEquals(selenium.getText(pickListTrgtItemStage.format(itemIdx)), pickedItemText);
    }

    /**
     * Verify that item get selected when click on it
     */
    @Test
    public void testGetSelected() {
        // this item will be selected and verified appropriate class change/add
        int itemIdx = 0;

        String classBeforeSelect = selenium.getAttribute(pickListSrcItem.format(itemIdx).getAttribute(classAttr));
        assertFalse(classBeforeSelect.contains(STYLE_CLASS_ITEM_SELECTED),
            "pickList item shouldn't be selected before any click made! Current class(es) found: " + classBeforeSelect);

        selenium.click(pickListSrcItem.format(itemIdx));

        String classAfterSelect = selenium.getAttribute(pickListSrcItem.format(itemIdx).getAttribute(classAttr));
        assertTrue(classAfterSelect.contains(STYLE_CLASS_ITEM_SELECTED),
            "pickList item should be selected after click on item was made. Current class(es) found: "
                + classAfterSelect);

    }

    /**
     * Verify switchByClick attribute
     */
    @Test
    public void testSwitchByClick() {
        String itemLabel = "Colorado";

        pickListAttributes.set(switchByClick, Boolean.TRUE);
        selenium.click(pickListSrcItemByText.format(itemLabel));

        // verify that item "added" to target list really appears in target (staging) container.
        waitModel.until(elementPresent.locator(pickListTrgtItemStageByText.format(itemLabel)));
    }

    /**
     * Verify switchByDblClick attribute
     */
    @Test
    public void testSwitchByDblClick() {
        String itemLabel = "Delaware";

        pickListAttributes.set(switchByDblClick, Boolean.TRUE);
        selenium.doubleClick(pickListSrcItemByText.format(itemLabel));

        // verify that item "added" to target list really appears in target (staging) container.
        waitModel.until(elementPresent.locator(pickListTrgtItemStageByText.format(itemLabel)));
    }

}
