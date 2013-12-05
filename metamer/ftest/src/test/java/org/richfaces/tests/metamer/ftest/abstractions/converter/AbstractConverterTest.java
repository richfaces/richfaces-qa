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
package org.richfaces.tests.metamer.ftest.abstractions.converter;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.RichFacesMessage;
import org.richfaces.tests.metamer.bean.ConverterBean;
import org.richfaces.tests.metamer.converter.SwitchableFailingConverter;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * Base for testing of @converter and @converterMessage of input components.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractConverterTest extends AbstractWebDriverTest {

    private static final String CUSTOM_CONVERTER_MESSAGE = "Custom converter message";
    private final Attributes<ConverterAttributes> attributes = getAttributes();

    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=a4jButton]")
    private WebElement ajaxSubmitButton;
    @FindBy(css = "[id$=msg]")
    private RichFacesMessage message;

    protected abstract String badValue();

    protected abstract String outputForEmptyValue();

    public void checkConverter() {
        assertNotVisible(message.advanced().getRootElement(), "Message should not be visible.");
        assertEquals(getOutputText(), ConverterBean.DEFAULT_VALUE, "Output");

        submitAjax();
        assertFalse(message.advanced().isVisible(), "Message should not be visible.");
        assertEquals(getOutputText(), outputForEmptyValue(), "Output");

        setFailing(true);
        setBadValue();
        submitAjax();
        assertTrue(message.advanced().isVisible(), "Message should be visible.");
        assertEquals(message.getDetail(), String.format(SwitchableFailingConverter.MESSAGE_TEMPLATE, badValue()), "Output");
        assertEquals(getOutputText(), ConverterBean.DEFAULT_VALUE, "Output");
    }

    public void checkConverterMessage() {
        setConverterMessage(CUSTOM_CONVERTER_MESSAGE);
        submitAjax();
        assertFalse(message.advanced().isVisible(), "Message should not be visible.");
        assertEquals(getOutputText(), outputForEmptyValue(), "Output");

        setFailing(true);
        setBadValue();
        submitAjax();
        assertTrue(message.advanced().isVisible(), "Message should be visible.");
        assertEquals(message.getDetail(), CUSTOM_CONVERTER_MESSAGE);
        assertEquals(getOutputText(), ConverterBean.DEFAULT_VALUE, "Output");
    }

    protected abstract String getComponentName();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + getComponentName() + "/converter.xhtml");
    }

    private String getOutputText() {
        return output.getText();
    }

    private void setConverterMessage(String message) {
        attributes.set(ConverterAttributes.converterMessage, message);
    }

    private void setFailing(boolean willFail) {
        attributes.set(ConverterAttributes.failingConverter, willFail);
    }

    protected abstract void setBadValue();

    private void submitAjax() {
        guardAjax(ajaxSubmitButton).click();
//        MetamerPage.waitRequest(ajaxSubmitButton, WaitRequestType.XHR).click();
    }

    private enum ConverterAttributes implements AttributeEnum {

        converterMessage, failingConverter;
    }
}
