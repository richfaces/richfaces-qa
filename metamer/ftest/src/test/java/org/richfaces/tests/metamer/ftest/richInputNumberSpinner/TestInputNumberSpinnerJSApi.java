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
package org.richfaces.tests.metamer.ftest.richInputNumberSpinner;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richInputNumberSpinner/simple.xhtml
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSpinnerJSApi extends AbstractWebDriverTest {

    private final Attributes<InputNumberSpinnerAttributes> inputNumberSpinnerAttributes = getAttributes();

    private static final String DEFAULT_VALUE = "2";
    private static final String SET_VALUE = "5";

    @ArquillianResource
    private Keyboard keyboard;

    @FindBy(css = "[id$='output']")
    private WebElement output;

    @FindBy(id = "focus")
    private WebElement focusButton;
    @FindBy(css = "[id$=getValue]")
    private WebElement getValueButton;
    @FindBy(css = "[id$=setValue]")
    private WebElement setValueButton;
    @FindBy(css = "[id$=increase]")
    private WebElement increaseButton;
    @FindBy(css = "[id$=decrease]")
    private WebElement decreaseButton;
    @FindBy(css = "input[id$=':value']")
    private WebElement value;
    @FindBy(css = "span[id$=spinner]")
    private RichFacesInputNumberSpinner spinner;

    @Override
    public String getComponentTestPagePath() {
        return "richInputNumberSpinner/simple.xhtml";
    }

    private String getValueFromOutputJSField() {
        return value.getAttribute("value");
    }

    @Test(groups = "smoke")
    public void testDecrease() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.minValue, -1);
        Graphene.guardAjax(decreaseButton).click();
        Graphene.guardAjax(decreaseButton).click();
        assertEquals(spinner.advanced().getInput().getStringValue(), "0");//2 - 1 - 1
        Graphene.guardAjax(decreaseButton).click();
        Graphene.guardNoRequest(decreaseButton).click();
        assertEquals(spinner.advanced().getInput().getStringValue(), "-1");//-2, but min is -1
    }

    @Test
    public void testFocus() {
        focusButton.click();

        // default value is 2, this will set the value to 12
        keyboard.sendKeys("1");
        Graphene.guardAjax(keyboard).sendKeys(Keys.TAB);
        // the value will be set to maximum -- 10
        assertEquals(output.getText(), "10");
    }

    @Test
    public void testGetValue() {
        assertEquals(getValueFromOutputJSField(), "");
        Graphene.guardNoRequest(getValueButton).click();
        assertEquals(getValueFromOutputJSField(), DEFAULT_VALUE);
        assertEquals(spinner.advanced().getInput().getStringValue(), DEFAULT_VALUE);
    }

    @Test
    public void testIncrease() {
        inputNumberSpinnerAttributes.set(InputNumberSpinnerAttributes.maxValue, 5);

        Graphene.guardAjax(increaseButton).click();
        Graphene.guardAjax(increaseButton).click();
        assertEquals(spinner.advanced().getInput().getStringValue(), "4");//2 + 1 + 1
        Graphene.guardAjax(increaseButton).click();
        Graphene.guardNoRequest(increaseButton).click();
        assertEquals(spinner.advanced().getInput().getStringValue(), "5");//6, but max is 5

    }

    @Test
    public void testMultiple() {
        Graphene.guardAjax(setValueButton).click();//5
        Graphene.guardAjax(increaseButton).click();//6
        Graphene.guardAjax(increaseButton).click();//7
        Graphene.guardAjax(decreaseButton).click();//6
        Graphene.guardNoRequest(getValueButton).click();
        assertEquals(getValueFromOutputJSField(), "6");//5 + 1 + 1 - 1
        assertEquals(spinner.advanced().getInput().getStringValue(), "6");
    }

    @Test
    public void testSetValue() {
        Graphene.guardAjax(setValueButton).click();
        assertEquals(spinner.advanced().getInput().getStringValue(), SET_VALUE);
    }
}
