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

import static java.text.MessageFormat.format;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
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

    private final String phaseListenerLogFormat = "*3 value changed: <p>{0}</p> -> <p>{1}</p>";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richEditor/anotherSimple.xhtml");
    }

    @Test(groups = "smoke")
    public void testImmediate() {
        editorAttributes.set(EditorAttributes.immediate, Boolean.TRUE);
        verifyValueChangeListener(page.getHButton(), page.getValueChangeListenerAfterImmediate());
    }

    @Test(groups = "smoke")
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
    public void testValueChangeListenerWithHButton() {
        verifyValueChangeListener(page.getHButton(), page.getValueChangeListener());
    }

    @Test
    public void testValueChangeListenerWithA4jButton() {
        verifyValueChangeListener(page.getA4jButton(), page.getValueChangeListener());
    }

    /**
     * Method for retrieve text from editor. Editor lives within iFrame, so there are need some additional steps to reach
     * element containing editor text
     *
     * @return
     */
    private String getTextFromEditor() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            return activeArea.getText();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    /**
     * Provide common steps needed to verify valueChangeListener. Accepts JQueryLocator for submit button - provide ability to
     * verify JSF submit as well as Ajax submit.
     *
     * @param submitBtn
     */
    private void verifyValueChangeListener(WebElement submitBtn, final WebElement listener) {

        page.getEditor().type("text1");
        // and submit typed text
        submitBtn.submit();
        Graphene.waitModel().until().element(page.getOutput()).text().contains("text1");
        page.getEditor().type("text2");
        // and submit typed text
        submitBtn.submit();
        Graphene.waitModel().until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver webDriver) {
                return listener.getText().contains(format(phaseListenerLogFormat, "text1", "text1text2"))
                    || listener.getText().contains(format(phaseListenerLogFormat, "text1", "text2text1"));
            }
        });
    }
}
