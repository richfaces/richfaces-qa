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
package org.richfaces.tests.metamer.ftest.richValidator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestValidatorQueueId extends AbstractWebDriverTest {

    private final Attributes<ValidatorAttributes> attributes = getAttributes();

    @FindBy(css = "span#delay")
    private WebElement delayOutput;
    @FindBy(css = "input[id$=input]")
    private WebElement inputElement;
    @FindBy(css = "[id$=inputMsg]")
    private RichFacesMessage message;

    @Override
    public String getComponentTestPagePath() {
        return "richValidator/singleWithQueues.xhtml";
    }

    private long getDelayInMillis() {
        return Long.parseLong(delayOutput.getText());
    }

    private void submitText(String text) {
        inputElement.clear();
        inputElement.sendKeys(text);
        // blur
        Graphene.guardAjax(getMetamerPage().getResponseDelayElement()).click();
    }

    @Test
    @CoversAttributes("queueId")
    @Unstable
    public void testQueueId() {
        assertFalse(message.advanced().isVisible());

        submitText("not using any queue");
        assertTrue(message.advanced().isVisible());
        assertEquals(getDelayInMillis(), 0, 500, "The delay should be between 0 and 500 ms without using any queue.");

        attributes.set(ValidatorAttributes.queueId, "q1");
        assertFalse(message.advanced().isVisible());
        submitText("using queue 1 with @requestDelay=1500 ms");
        assertTrue(message.advanced().isVisible());
        assertEquals(getDelayInMillis(), 1500, 700, "The delay should be between 1500 and 2200 ms using queue #1.");

        attributes.set(ValidatorAttributes.queueId, "q2");
        assertFalse(message.advanced().isVisible());
        submitText("using queue 2 with @requestDelay=500 ms");
        assertTrue(message.advanced().isVisible());
        assertEquals(getDelayInMillis(), 500, 700, "The delay should be between 500 and 1200 ms using queue #2.");
    }
}
