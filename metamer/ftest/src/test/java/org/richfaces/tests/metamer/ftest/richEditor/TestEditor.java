/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richEditor;

import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richEditor.EditorAttributes.toolbar;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for basic functionality of rich:editor on page faces/components/richEditor/simple.xhtml.
 *
 * @author <<a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 * @version $Revision$
 */
public class TestEditor extends AbstractWebDriverTest {

    private final Attributes<EditorAttributes> editorAttributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richEditor/simple.xhtml");
    }

    @Page
    private EditorSimplePage page;

    private final String editorTextAreaTag = "textarea";

    private WebElement getEditorTextArea() {
        return driver.findElement(By.tagName(editorTextAreaTag));
    }

    @Test
    @Templates(value = "plain")
    public void testHeight() {
        String height = "500px";
        editorAttributes.set(EditorAttributes.height, height);
        assertEquals(page.getEditorFrame().getCssValue("height"), height);

    }

    @Test
    @Templates("plain")
    public void testLanguage() {
        String language = "testLanguage";
        editorAttributes.set(EditorAttributes.lang, language);
        assertEquals(getEditorTextArea().getAttribute("lang").toString(), language);
    }

    @Test
    public void testReadonly() {
        // set readable only
        editorAttributes.set(EditorAttributes.readonly, Boolean.TRUE);
        assertEquals(getEditorTextArea().getAttribute("readonly"), "true");
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        editorAttributes.set(EditorAttributes.rendered, Boolean.FALSE);
        assertNotPresent(page.getEditorFrame(), "Editor should not be rendered.");
    }

    @Test
    public void testRequired() {

        editorAttributes.set(EditorAttributes.required, Boolean.TRUE);
        page.getHButton().click();
        waitGui().until().element(page.getErrorMsg()).is().present();

        page.fullPageRefresh();

        page.getA4jButton().click();
        waitGui().until().element(page.getErrorMsg()).is().present();
    }

    @Test
    public void testRequiredMessage() {
        String testMessage1 = "Click hButton";
        String testMessage2 = "Click a4jButton";
        editorAttributes.set(EditorAttributes.required, Boolean.TRUE);
        editorAttributes.set(EditorAttributes.requiredMessage, testMessage1);
        page.getHButton().click();
        waitAjax().until().element(page.getErrorMsg()).is().present();
        assertEquals(page.getErrorMsg().getText(), testMessage1);

        editorAttributes.set(EditorAttributes.requiredMessage, testMessage2);
        page.getA4jButton().click();
        waitAjax().until().element(page.getErrorMsg()).is().present();
        assertEquals(page.getErrorMsg().getText(), testMessage2);
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        testStyle(getEditorTextArea());
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(getEditorTextArea());
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        testTitle(getEditorTextArea());
    }

    @Test
    public void testToolbar() {

        editorAttributes.set(toolbar, "basic");

        assertTrue(page.getEditor().advanced().getToolbar().isBasic());

        editorAttributes.set(toolbar, "full");
        assertTrue(page.getEditor().advanced().getToolbar().isAdvanced());

        // since config facet has been introduced...
        editorAttributes.set(toolbar, "custom");
        assertTrue(page.getEditor().advanced().getToolbar().numberOfToolbarItems() == 9);
    }

    @Test
    @Templates(value = "plain")
    public void testWidth() {

        String STYLE_WIDTH = "400px";
        editorAttributes.set(EditorAttributes.width, STYLE_WIDTH);
        assertTrue(getEditorTextArea().getAttribute("style").contains("width: " + STYLE_WIDTH));
    }

    @Test
    public void testOnBlur() {
        testFireEvent(editorAttributes, EditorAttributes.onblur, new Action() {
            @Override
            public void perform() {
                guardNoRequest(page.getEditor()).type("some text");
                // click on some different component to lose focus
                driver.findElement(By.xpath("//input[contains(@name, 'a4jButton')]")).click();
            }
        });
    }

    @Test
    public void testOnChange() {
        testFireEvent(editorAttributes, EditorAttributes.onchange, new Action() {
            @Override
            public void perform() {
                guardNoRequest(page.getEditor()).type("some text");
                // click on some different component to lose focus
                driver.findElement(By.xpath("//input[contains(@name, 'a4jButton')]")).click();
            }
        });
    }

    @Test
    public void testOnFocus() {
        testFireEvent(editorAttributes, EditorAttributes.onfocus, new Action() {
            @Override
            public void perform() {
                guardNoRequest(page.getEditor()).type("some text");
            }
        });
    }

    @Test
    public void testOnInit() {
        testFireEvent(editorAttributes, EditorAttributes.oninit, new Action() {
            @Override
            public void perform() {
                // should react immediately after rendering
                page.fullPageRefresh();
            }
        });
    }
}
