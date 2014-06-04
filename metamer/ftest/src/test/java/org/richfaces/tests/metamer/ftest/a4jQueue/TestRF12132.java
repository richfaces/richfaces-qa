/*
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
 */
package org.richfaces.tests.metamer.ftest.a4jQueue;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Random;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.log.Log.LogEntryLevel;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12132 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=input]")
    private TextInputComponentImpl input;
    @FindBy(css = "[id$=log]")
    private RichFacesLog log;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=action]")
    private WebElement submitButton;
    @FindBy(css = "[id$=actionWithNotExistingOncompleteFunction]")
    private WebElement submitButtonWithNotExistingOncompleteFunction;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jQueue/rf-12132.xhtml");
    }

    @Test
    public void testQueueIsClearedAfterJSErrorInOncompleteFunction() {
        typeRandomTextSubmitAndCheck(submitButton);
        typeRandomTextSubmitAndCheck(submitButtonWithNotExistingOncompleteFunction);
        typeRandomTextSubmitAndCheck(submitButton);
        typeRandomTextSubmitAndCheck(submitButton);
        typeRandomTextSubmitAndCheck(submitButtonWithNotExistingOncompleteFunction);
        typeRandomTextSubmitAndCheck(submitButtonWithNotExistingOncompleteFunction);
        typeRandomTextSubmitAndCheck(submitButton);
    }

    private void typeRandomTextSubmitAndCheck(WebElement submitButton) {
        log.changeLevel(LogEntryLevel.ERROR);

        String text = "random text " + new Random().nextLong();
        input.clear().sendKeys(text);
        Graphene.guardAjax(submitButton).click();
        if (submitButton == this.submitButtonWithNotExistingOncompleteFunction) {
            assertEquals(log.getLogEntries().size(), 1);
            assertEquals(log.getLogEntries().getItem(0).getContent(), "Error in method execution: notExistingFunction is not defined");
            log.clear();
        }
        assertEquals(log.getLogEntries().size(), 0);
        assertEquals(output.getText(), text.toUpperCase());
    }
}
