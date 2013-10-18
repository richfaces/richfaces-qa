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
package org.richfaces.tests.metamer.ftest.richInputNumberSlider;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.inputNumberSliderAttributes;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richInputNumberSlider/simple.xhtml
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSliderJSApi extends AbstractSliderTest {

    private static final String DEFAULT_VALUE = "2";
    private static final String SET_VALUE = "5";
    //
    @FindBy(css = "[id$=getValue]")
    WebElement getValueButton;
    @FindBy(css = "[id$=setValue]")
    WebElement setValueButton;
    @FindBy(css = "[id$=increase]")
    WebElement increaseButton;
    @FindBy(css = "[id$=decrease]")
    WebElement decreaseButton;
    @FindBy(css = "input[id$=':value']")
    WebElement value;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSlider/simple.xhtml");
    }

    private String getValueFromOutputJSField() {
        return value.getAttribute("value");
    }

    @Test
    public void testDecrease() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.minValue, -1);
        MetamerPage.waitRequest(decreaseButton, WaitRequestType.XHR).click();
        MetamerPage.waitRequest(decreaseButton, WaitRequestType.XHR).click();
        assertEquals(slider.advanced().getInput().getStringValue(), "0");//2 - 1 - 1
        MetamerPage.waitRequest(decreaseButton, WaitRequestType.XHR).click();
        MetamerPage.waitRequest(decreaseButton, WaitRequestType.XHR).click();
        assertEquals(slider.advanced().getInput().getStringValue(), "-1");//-2, but min is -1

    }

    @Test
    public void testGetValue() {
        assertEquals(getValueFromOutputJSField(), "");
        MetamerPage.waitRequest(getValueButton, WaitRequestType.NONE).click();
        assertEquals(getValueFromOutputJSField(), DEFAULT_VALUE);
        assertEquals(slider.advanced().getInput().getStringValue(), DEFAULT_VALUE);
    }

    @Test
    public void testIncrease() {
        inputNumberSliderAttributes.set(InputNumberSliderAttributes.maxValue, 5);

        MetamerPage.waitRequest(increaseButton, WaitRequestType.XHR).click();
        MetamerPage.waitRequest(increaseButton, WaitRequestType.XHR).click();
        assertEquals(slider.advanced().getInput().getStringValue(), "4");//2 + 1 + 1
        MetamerPage.waitRequest(increaseButton, WaitRequestType.XHR).click();
        MetamerPage.waitRequest(increaseButton, WaitRequestType.XHR).click();
        assertEquals(slider.advanced().getInput().getStringValue(), "5");//6, but max is 5
    }

    @Test
    public void testMultiple() {
        MetamerPage.waitRequest(setValueButton, WaitRequestType.XHR).click();
        MetamerPage.waitRequest(increaseButton, WaitRequestType.XHR).click();
        MetamerPage.waitRequest(increaseButton, WaitRequestType.XHR).click();
        MetamerPage.waitRequest(decreaseButton, WaitRequestType.XHR).click();
        MetamerPage.waitRequest(getValueButton, WaitRequestType.NONE).click();
        assertEquals(getValueFromOutputJSField(), "6");//5 + 1 + 1 - 1
        assertEquals(slider.advanced().getInput().getStringValue(), "6");
    }

    @Test
    public void testSetValue() {
        MetamerPage.waitRequest(setValueButton, WaitRequestType.XHR).click();
        assertEquals(slider.advanced().getInput().getStringValue(), SET_VALUE);
    }
}
