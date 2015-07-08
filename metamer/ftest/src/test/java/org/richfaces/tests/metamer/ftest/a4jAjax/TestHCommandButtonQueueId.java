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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestHCommandButtonQueueId extends AbstractWebDriverTest {

    private final Attributes<AjaxAttributes> attributes = getAttributes();

    @FindBy(css = "span#delay")
    private WebElement delayOutput;

    @Page
    private AjaxPage page;

    @Override
    public String getComponentTestPagePath() {
        return "a4jAjax/hCommandButtonWithQueues.xhtml";
    }

    private long getDelayInMillis() {
        return Long.parseLong(delayOutput.getText());
    }

    private void submitText(String text) {
        page.getInputElement().clear();
        page.getInputElement().sendKeys(text);
        Graphene.guardAjax(page.getButtonElement()).click();
    }

    @Test
    @CoversAttributes("queueId")
    public void testQueueId() {
        submitText("some text");
        assertEquals(getDelayInMillis(), 0, 500, "The delay should be between 0 and 500 ms without using any queue.");

        attributes.set(AjaxAttributes.queueId, "q1");
        submitText("using queue 1 with @requestDelay=1500 ms");
        assertEquals(getDelayInMillis(), 1500, 700, "The delay should be between 1500 and 2200 ms using queue #1.");

        attributes.set(AjaxAttributes.queueId, "q2");
        submitText("using queue 2 with @requestDelay=500 ms");
        assertEquals(getDelayInMillis(), 500, 700, "The delay should be between 500 and 1200 ms using queue #2.");
    }
}
