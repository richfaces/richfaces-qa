/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestEditorWithTyping extends AbstractWebDriverTest {

    private final Attributes<EditorAttributes> editorAttributes = getAttributes();

    @Page
    private EditorSimplePage page;

    private final String phaseListenerLogFormat = "* 3 value changed: <p>{0}</p> -> <p>{1}</p>";

    @Override
    public String getComponentTestPagePath() {
        return "richEditor/anotherSimple.xhtml";
    }

    /**
     * Method for retrieve text from editor.
     */
    private String getTextFromEditor() {
        return page.getEditor().getText();
    }

    private void performGuardedSubmit(WebElement submitBtn) {
        (submitBtn == page.getA4jButton() ? Graphene.guardAjax(submitBtn) : Graphene.guardHttp(submitBtn)).click();
    }

    @Test(groups = "smoke")
    @CoversAttributes("immediate")
    public void testImmediate() {
        editorAttributes.set(EditorAttributes.immediate, Boolean.TRUE);
        verifyValueChangeListener(page.getHButton(), page.getValueChangeListenerAfterImmediate());
    }

    @Test(groups = "smoke")
    @CoversAttributes("ondirty")
    public void testOnDirty() {
        String testedValue = "dirty";
        editorAttributes.set(EditorAttributes.ondirty, "metamerEvents += \"" + testedValue + " \"");
        executeJS("window.metamerEvents = \"\";");
        page.getEditor().type("x");
        String event = expectedReturnJS("return window.metamerEvents", testedValue);
        assertEquals(event, "dirty", "Attribute ondirty doesn't work");
    }

    @Test(groups = "smoke")
    public void testTypeAndSubmit() throws InterruptedException {
        page.getEditor().type("SOMETHING");
        page.getHButton().submit();
        Graphene.waitModel().until().element(page.getOutput()).text().contains("SOMETHING");
    }

    @Test
    @CoversAttributes("value")
    @Templates("plain")
    public void testValue() {
        // write some value in editor and submit by normal way
        page.getEditor().type("text1");
        page.getHButton().submit();

        // then set value from outside, and check this value in editor
        editorAttributes.set(EditorAttributes.value, "text2");
        String found = getTextFromEditor();
        assertTrue(found != null && found.contains("text2"));
    }

    @Test
    @CoversAttributes("valueChangeListener")
    public void testValueChangeListenerWithA4jButton() {
        verifyValueChangeListener(page.getA4jButton(), page.getValueChangeListener());
    }

    @Test
    @CoversAttributes("valueChangeListener")
    public void testValueChangeListenerWithHButton() {
        verifyValueChangeListener(page.getHButton(), page.getValueChangeListener());
    }

    /**
     * Provide common steps needed to verify valueChangeListener. Accepts JQueryLocator for submit button - provide ability to
     * verify JSF submit as well as Ajax submit.
     *
     * @param submitBtn
     */
    private void verifyValueChangeListener(WebElement submitBtn, final WebElement listener) {
        page.getEditor().type("text1");
        performGuardedSubmit(submitBtn);
        assertEquals(page.getOutput().getText(), "<p>text1</p>");
        page.getEditor().type("text2");
        performGuardedSubmit(submitBtn);
        Graphene.waitAjax().until(new Predicate<WebDriver>() {

            private String actual;
            private final String val1 = format(phaseListenerLogFormat, "text1", "text1text2");
            private final String val2 = format(phaseListenerLogFormat, "text1", "text2text1");

            @Override
            public boolean apply(WebDriver webDriver) {
                actual = listener.getText();
                return actual.contains(val1) || actual.contains(val2);
            }

            @Override
            public String toString() {
                return format("text from listener contains <{0}> or <{1}>. Got: <{2}>.", val1, val2, actual);
            }

        });
    }
}
