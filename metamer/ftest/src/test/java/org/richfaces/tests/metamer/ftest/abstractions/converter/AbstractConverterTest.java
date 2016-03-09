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
package org.richfaces.tests.metamer.ftest.abstractions.converter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.bean.ConverterBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;

/**
 * Base for testing of @converter and @converterMessage of input components.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractConverterTest extends AbstractWebDriverTest {

    private static final String CUSTOM_CONVERTER_MESSAGE = "Custom converter message";

    public static final String DEFAULT_VALUE_DATE = "Jan 1, 1970 12:00:00 AM";
    public static final String DEFAULT_VALUE_LIST = "[Montgomery (Alabama)]";
    public static final String DEFAULT_VALUE_NUMBER = "5";
    public static final String DEFAULT_VALUE_SINGLE = "Montgomery (Alabama)";

    @FindBy(css = "[id$=a4jButton]")
    private WebElement ajaxSubmitButton;
    @FindBy(css = "[id$='failingConverterInput:1']")
    private WebElement failingConverterFalseButton;
    @FindBy(css = "[id$='failingConverterInput:0']")
    private WebElement failingConverterTrueButton;
    @FindBy(css = "[id$=msg]")
    private RichFacesMessage message;
    @FindBy(css = "[id$=output]")
    private WebElement output;

    protected abstract String getComponentName();

    @Override
    public String getComponentTestPagePath() {
        return getComponentName() + "/converter.xhtml";
    }

    protected abstract String getDefaultValue();

    private String getOutputText() {
        return output.getText().trim();
    }

    private void setConverterMessage(String message) {
        setAttribute("converterMessage", message);
    }

    private void setFailing(boolean willFail) {
        getMetamerPage().performJSClickOnButton(willFail ? failingConverterTrueButton : failingConverterFalseButton, WaitRequestType.XHR);
    }

    private void submitAjax() {
        jsUtils.scrollToView(ajaxSubmitButton);
        Graphene.guardAjax(ajaxSubmitButton).click();
    }

    @CoversAttributes({ "converter", "converterMessage" })
    public void testConverterAndConverterMessage() {
        assertNotVisible(message.advanced().getRootElement(), "Message should not be visible.");
        assertEquals(getOutputText(), getDefaultValue());

        submitAjax();
        assertFalse(message.advanced().isVisible(), "Message should not be visible.");
        assertEquals(getOutputText(), getDefaultValue());

        setFailing(true);
        submitAjax();
        assertTrue(message.advanced().isVisible(), "Message should be visible.");
        assertEquals(message.getDetail(), ConverterBean.ERROR_MSG);
        assertEquals(getOutputText(), getDefaultValue());

        //check converterMessage
        setFailing(false);

        setConverterMessage(CUSTOM_CONVERTER_MESSAGE);
        submitAjax();
        assertFalse(message.advanced().isVisible(), "Message should not be visible.");
        assertEquals(getOutputText(), getDefaultValue());

        setFailing(true);
        submitAjax();
        assertTrue(message.advanced().isVisible(), "Message should be visible.");
        assertEquals(message.getDetail(), CUSTOM_CONVERTER_MESSAGE);
        assertEquals(getOutputText(), getDefaultValue());
    }
}
