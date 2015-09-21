/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richEditor;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.remote.RemoteWebElement;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestEditorJSApi extends AbstractWebDriverTest {

    private final Attributes<EditorAttributes> editorAttributes = getAttributes();

    private final String editorTextAreaTag = "textarea";

    @Page
    private EditorSimplePage page;

    @Override
    public String getComponentTestPagePath() {
        return "richEditor/simple.xhtml";
    }

    private String getEditorId() {
        return getEditorRootElement().getAttribute("id");
    }

    private WebElement getEditorRootElement() {
        return page.getEditor().advanced().getRootElement();
    }

    private WebElement getEditorTextArea() {
        return driver.findElement(By.tagName(editorTextAreaTag));
    }

    private void jsBlur() {
        executeJS("RichFaces.component('" + getEditorId() + "').blur()");
    }

    private String jsGetValue() {
        return Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "getValue()");
    }

    private Boolean jsIsFocused() {
        return Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "isFocused()");
    }

    private void jsSetFocus() {
        Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "focus()");
    }

    /**
     * Test through fire JS event
     */
    @Test
    @Templates(value = { "plain" })
    public void testJsBlur() {
        testFireEvent(editorAttributes, EditorAttributes.onblur, new Action() {
            @Override
            public void perform() {
                // focus first, then blur
                driver.findElement(By.xpath("//input[@name='focus']")).click();
                waiting(2000);
                driver.findElement(By.xpath("//input[@name='blur']")).click();
                waiting(2000);
            }
        });
    }

    /**
     * Test through fire JS event
     */
    @Test
    @Templates(value = { "plain" })
    public void testJsFocus() {
        testFireEvent(editorAttributes, EditorAttributes.onfocus, new Action() {
            @Override
            public void perform() {
                driver.findElement(By.xpath("//input[@name='focus']")).click();
                waiting(2000);
            }
        });
    }

    /**
     * Idea is to get editor Object and use several its functions to assure the object works correctly
     */
    @Test
    @Templates(value = { "plain" })
    public void testJsGetEditor() {
        String someText = "some text";
        Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "getEditor().setReadOnly(true)");
        try {
            page.getEditor().type("Nice text");
        } catch (Exception e) {
            // OK exception should be thrown as you cannot edit read only editor
        }
        // additional check with JS function

        assertTrue(Utils.<Boolean>invokeRichFacesJSAPIFunction(getEditorRootElement(), "isReadOnly()"));
        // set back to editable
        Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "getEditor().setReadOnly(false)");
        Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "getEditor().setData('" + someText + "')");
        assertEquals(page.getEditor().getText(), someText);
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsGetInput() {
        RemoteWebElement textArea = Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "getInput()");
        assertEquals(textArea.getAttribute("id"), getEditorTextArea().getAttribute("id"));
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsGetValue() {
        String testText = "Some nice and cool text";
        page.getEditor().type(testText);
        assertTrue(jsGetValue().contains(testText));
    }

    @Test
    @Unstable
    @Templates(value = { "plain" })
    public void testJsIsDirty() {
        page.getEditor().type("Some text");
        assertTrue(Utils.<Boolean>invokeRichFacesJSAPIFunction(getEditorRootElement(), "isDirty()"));

        page.getEditor().type("even more text!");
        guardAjax(page.getA4jButton()).click();
        waitAjax(driver).until().element(page.getOutput()).text().contains("even more text!");
        assertFalse(Utils.<Boolean>invokeRichFacesJSAPIFunction(getEditorRootElement(), "isDirty()"));
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsIsFocused() {
        // refresh page to make sure focus is lost and assert initially false
        page.fullPageRefresh();
        assertFalse(jsIsFocused());

        // focus and assert true
        jsSetFocus();
        assertTrue(jsIsFocused());

        // lose focus and assert false
        jsBlur();
        assertFalse(jsIsFocused());
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsIsReadOnly() {
        // initially false

        assertFalse(Utils.<Boolean>invokeRichFacesJSAPIFunction(getEditorRootElement(), "isReadOnly()"));

        // set read only and assert true
        editorAttributes.set(EditorAttributes.readonly, true);
        assertTrue(Utils.<Boolean>invokeRichFacesJSAPIFunction(getEditorRootElement(), "isReadOnly()"));
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsIsValueChanged() {
        // edit, submit changes and assert
        page.getEditor().type("Hello");
        assertTrue(Utils.<Boolean>invokeRichFacesJSAPIFunction(getEditorRootElement(), "isValueChanged()"));

        // click submit again with no changes
        page.fullPageRefresh();
        assertFalse(Utils.<Boolean>invokeRichFacesJSAPIFunction(getEditorRootElement(), "isValueChanged()"));
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14120")
    @Templates(value = { "plain" })
    public void testJsSetReadOnly() {
        String testText = "Some random text";
        // initially editable
        page.getEditor().type(testText);
        guardAjax(page.getA4jButton()).click();
        assertEquals(page.getEditor().getText(), testText);
        page.getEditor().clear();

        Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "setReadOnly(true)");
        page.getEditor().type(testText);
        assertEquals(page.getEditor().getText(), "");

        // additional check with JS function
        assertTrue(Utils.<Boolean>invokeRichFacesJSAPIFunction(getEditorRootElement(), "isReadOnly()"));

        // revert back to editable and assert
        Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "setReadOnly(false)");
        page.getEditor().type(testText);
        guardAjax(page.getA4jButton()).click();
        assertEquals(page.getEditor().getText(), testText);
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsSetValue() {
        // should be empty
        assertEquals(page.getEditor().getText(), "");
        String testText = "NEW VALUE SET BY JS!";

        Utils.invokeRichFacesJSAPIFunction(getEditorRootElement(), "setValue('" + testText + "')");
        assertEquals(page.getEditor().getText(), testText);
    }
}
