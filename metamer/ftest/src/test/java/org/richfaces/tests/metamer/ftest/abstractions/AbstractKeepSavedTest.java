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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.Assert;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AbstractKeepSavedTest extends AbstractWebDriverTest {

    private static final String TEXT_CORRECT = "123";
    private static final String TEXT_WRONG = "BAD VALUE";

    private final Attributes<KeepSavedAttributes> attributes = getAttributes();
    private final String componentName;

    @FindByJQuery("[id$=panel] input[type=text]:first")
    private TextInputComponentImpl firstInput;
    @FindBy(css = "input[id$=submitButton]")
    private WebElement submitButton;
    @FindBy(css = "[id$=messages]")
    private RichFacesMessages messages;

    /**
     * @param componentName used for building the URL.
     */
    public AbstractKeepSavedTest(String componentName) {
        this.componentName = componentName;
    }

    protected void changeInputValueToAndSubmit(String s) {
        firstInput.advanced().clear(ClearType.JS).sendKeys(s);
        submit();
    }

    public String getComponentSpecifier() {
        return componentName;
    }

    protected String getInputValue() {
        return firstInput.getStringValue();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + componentName + "/keepSaved.xhtml");
    }

    protected void submit() {
        MetamerPage.waitRequest(submitButton, WaitRequestType.XHR).click();
    }

    protected void checkKeepSaved(boolean keepSaved) {
        attributes.set(KeepSavedAttributes.keepSaved, keepSaved);
        // put in a correct value and submit
        changeInputValueToAndSubmit(TEXT_CORRECT);
        // check that value is saved
        Assert.assertEquals(getInputValue(), TEXT_CORRECT);
        Assert.assertFalse(messages.advanced().isVisible(), "No error message should be visible.");

        // put in a wrong value and submit
        changeInputValueToAndSubmit(TEXT_WRONG);
        // check if the value is saved depending on the @keepSaved
        Assert.assertEquals(getInputValue(), (keepSaved ? TEXT_WRONG : TEXT_CORRECT));
        Assert.assertTrue(messages.advanced().isVisible(), "An error message should be visible.");
        Assert.assertEquals(messages.size(), 1, "Only 1 error message should be visible.");
        // check if the value is saved depending on the @keepSaved after another submit
        submit();
        if (keepSaved) {
            Assert.assertEquals(getInputValue(), TEXT_WRONG);
            Assert.assertTrue(messages.advanced().isVisible(), "An error message should be visible.");
            Assert.assertEquals(messages.size(), 1, "Only 1 error message should be visible.");
        } else {
            Assert.assertEquals(getInputValue(), TEXT_CORRECT);
            Assert.assertFalse(messages.advanced().isVisible(), "No error message should be visible.");
        }
    }

    public void checkKeepSavedFalse() {
        checkKeepSaved(Boolean.FALSE);
    }

    public void checkKeepSavedTrue() {
        checkKeepSaved(Boolean.TRUE);
    }

    protected static enum KeepSavedAttributes implements AttributeEnum {

        keepSaved;
    }
}
