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
package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.arquillian.ajocado.Ajocado.elementPresent;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.test.selenium.javascript.JQueryScript.jqObject;
import static org.jboss.test.selenium.javascript.JQueryScript.jqScript;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.AbstractMetamerTest.pjq;

import java.awt.event.KeyEvent;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.javascript.KeyCode;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.RequestTypeModelGuard.Model;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23092 $
 */
public class Autocomplete implements Model {

    //private static final String KEY_UP = "38";
    //private static final String KEY_DOWN = "40";

AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    JQueryLocator input = pjq("input.rf-au-inp[id$=autocompleteInput]");

    JQueryLocator items = jq("div.rf-au-lst-cord[id$=autocompleteList] div[id$=autocompleteItems]");
    JQueryLocator selection = items.getDescendant(jq("div.rf-au-itm-sel"));
    JQueryLocator option = jq("div.rf-au-itm");
    JQueryLocator labeledOption = jq("div.rf-au-opt:contains({0})");

    public void typeKeys(String keys) {
        for (int i = 0; i < keys.length(); i++) {
            //final String key = String.valueOf(keys.charAt(i));
            selenium.focus(input);
            selenium.keyPress(input, keys.charAt(i));
            selenium.fireEvent(input, Event.KEYDOWN);
        }
    }

    public void type(String value) {
        selenium.type(input, value);
        selenium.fireEvent(input, Event.BLUR);
    }

    public void confirmByKeys() {
        selenium.fireEvent(input, Event.FOCUS);
        pressEnter();
    }

    public void selectByKeys(String label) {
        int labeledIndex = getLabeledOptionIndex(label);
        while (getSelectedOptionIndex() < labeledIndex) {
            pressDown();
        }
        while (getSelectedOptionIndex() > labeledIndex) {
            pressUp();
        }
    }
    
    /**
     * Method for select option from autocomplete suggestions. This method doesn't invoke/display suggestions.
     * Is should be already done by another selenium invocation.
     * 
     * For now, it is just selenium.clickAt() 
     * 
     * @param label
     */
    public void selectByMouse(String label) {
        selenium.mouseOver(labeledOption.format(label));
        selenium.clickAt(labeledOption.format(label), new Point(3, 3));
    }

    public boolean isLabeledOptionAvailable(String label) {
        return selenium.isElementPresent(getLabeledOption(label));
    }

    public int getLabeledOptionIndex(String label) {
        String index = selenium.getEval(jqScript(getLabeledOption(label), "index()"));
        return Integer.valueOf(index);
    }

    public int getSelectedOptionIndex() {
        JavaScript script = jqScript(option, "index({0})").parametrize(jqObject(selection));
        String index = selenium.getEval(script);
        return Integer.valueOf(index);
    }

    public String getSelectedOptionText() {
        return selenium.getText(selection);
    }

    public String getInputText() {
        return selenium.getValue(input);
    }

    public void clearInputValue() {
        selenium.type(input, "");
        selenium.focus(input);
    }

    private JQueryLocator getLabeledOption(String label) {
        return labeledOption.format(label);
    }

    public void pressBackspace() {
        selenium.keyPressNative(KeyEvent.VK_BACK_SPACE);
    }

    public void pressUp() {
        selenium.keyDown(input, KeyCode.UP_ARROW);
    }

    public void pressDown() {
        selenium.keyDown(input, KeyCode.DOWN_ARROW);
    }

    public void pressEnter() {
        selenium.keyPressNative(KeyEvent.VK_ENTER);
    }

    public void pressLeft() {
        selenium.keyPressNative(KeyEvent.VK_LEFT);
    }

    public void pressRight() {
        selenium.keyPressNative(KeyEvent.VK_RIGHT);
    }

    public void pressDelete() {
        selenium.keyPressNative(KeyEvent.VK_DELETE);
    }

    public void textSelectionLeft(int size) {
        selenium.keyDownNative(KeyEvent.VK_SHIFT);
        for (int i = 0; i < size; i++) {
            selenium.keyPressNative(KeyEvent.VK_LEFT);
        }
        selenium.keyUpNative(KeyEvent.VK_SHIFT);
    }

    public void textSelectionRight(int size) {
        selenium.keyDownNative(KeyEvent.VK_SHIFT);
        for (int i = 0; i < size; i++) {
            selenium.keyPressNative(KeyEvent.VK_RIGHT);
        }
        selenium.keyUpNative(KeyEvent.VK_SHIFT);
    }

    public void textSelectAll() {
        selenium.keyDownNative(KeyEvent.VK_CONTROL);
        selenium.keyPressNative(KeyEvent.VK_A);
        selenium.keyDownNative(KeyEvent.VK_CONTROL);
    }

    public boolean isCompletionVisible() {
        if (!selenium.isElementPresent(items)) {
            return false;
        }
        return selenium.isVisible(items);
    }

    public void waitForCompletionVisible() {
        waitGui.until(elementPresent.locator(option));
    }
}
