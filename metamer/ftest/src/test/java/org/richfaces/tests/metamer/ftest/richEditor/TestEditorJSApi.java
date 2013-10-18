/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richEditor;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.editorAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

public class TestEditorJSApi extends AbstractWebDriverTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richEditor/simple.xhtml");
    }

    @Page
    private EditorSimplePage page;

    @FindBy(xpath = "//div[contains(@class,'rf-ed') and contains (@id, 'editor')]")
    private WebElement editor;

    private final String editorTextAreaTag = "textarea";

    private String getEditorId() {
        return editor.getAttribute("id");
    }

    private WebElement getEditorTextArea() {
        return driver.findElement(By.tagName(editorTextAreaTag));
    }

    private void jsSetFocus() {
        executeJS("RichFaces.component('" + getEditorId() + "').focus()");
    }

    private void jsBlur() {
        executeJS("RichFaces.component('" + getEditorId() + "').blur()");
    }

    private Boolean jsIsFocused() {
        Boolean jsResult = (Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isFocused()");
        return jsResult;
    }

    private String jsGetValue() {
        return (String) executeJS("return RichFaces.component('" + getEditorId() + "').getValue()");
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsGetValue() {
        String testText = "Some nice and cool text";
        page.getEditor().type(testText);
        String jsResult = (String) jsGetValue();
        assertTrue(jsResult.contains(testText));
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsSetValue() {
        // should be empty
        assertEquals(page.getEditor().getText(), "");
        String testText = "NEW VALUE SET BY JS!";
        executeJS("RichFaces.component('" + getEditorId() + "').setValue('" + testText + "')");
        assertEquals(page.getEditor().getText(), testText);
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsGetInput() {
        RemoteWebElement textArea;
        textArea = (RemoteWebElement) executeJS("return RichFaces.component('" + getEditorId() + "').getInput()");
        assertEquals(textArea.getAttribute("id"), getEditorTextArea().getAttribute("id"));
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
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                driver.findElement(By.xpath("//input[@name='blur']")).click();
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
    public void testJsIsDirty() {
        page.getEditor().type("Some text");
        assertTrue((Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isDirty()"));

        page.getEditor().type("even more text!");
        guardAjax(page.getA4jButton()).click();
        assertFalse((Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isDirty()"));
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsIsValueChanged() {
        // edit, submit changes and assert
        page.getEditor().type("Hello");
        assertTrue((Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isValueChanged()"));

        // click submit again with no changes
        page.fullPageRefresh();
        assertFalse((Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isValueChanged()"));
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsIsReadOnly() {
        // initially false
        assertFalse((Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isReadOnly()"));

        // set read only and assert true
        editorAttributes.set(EditorAttributes.readonly, true);
        assertTrue((Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isReadOnly()"));
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsSetReadOnly() {
        String testText = "Some random text";
        // initially editable
        page.getEditor().type(testText);
        guardAjax(page.getA4jButton()).click();
        assertEquals(page.getEditor().getText(), testText);
        page.getEditor().clear();

        executeJS("RichFaces.component('" + getEditorId() + "').setReadOnly(true)");
        try {
            page.getEditor().type(testText);
        } catch (Exception e) {
            // OK exception should be thrown as you cannot edit read only editor
        }
        // additional check with JS function
        assertTrue((Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isReadOnly()"));

        // revert back to editable and assert
        executeJS("RichFaces.component('" + getEditorId() + "').setReadOnly(false)");
        page.getEditor().type(testText);
        guardAjax(page.getA4jButton()).click();
        assertEquals(page.getEditor().getText(), testText);
    }

    /**
     * Idea is to get editor Object and use several its functions to assure the object works correctly
     */
    @Test
    @Templates(value = { "plain" })
    public void testJsGetEditor() {
        String someText = "some text";
        executeJS("RichFaces.component('" + getEditorId() + "').getEditor().setReadOnly()");
        try {
            page.getEditor().type("Nice text");
        } catch (Exception e) {
            // OK exception should be thrown as you cannot edit read only editor
        }
        // additional check with JS function
        assertTrue((Boolean) executeJS("return RichFaces.component('" + getEditorId() + "').isReadOnly()"));
        // set back to editable
        executeJS("RichFaces.component('" + getEditorId() + "').getEditor().setReadOnly(false)");

        executeJS("RichFaces.component('" + getEditorId() + "').getEditor().setData('" + someText + "')");
        assertEquals(page.getEditor().getText(), someText);
    }
}
