/**
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
 */
package org.richfaces.tests.metamer.ftest.richPlaceholder;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.placeholderAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.awt.Color;
import java.net.URL;
import org.jboss.arquillian.ajocado.utils.ColorUtils;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
//FIXME should not be generic
public abstract class AbstractPlaceholderNonJSFTest extends AbstractWebDriverTest {

    private static final String INPUT1_ID = "[id$=input1]";
    private static final String INPUT2_ID = "[id$=input2]";
    private static final String INPUT3_ID = "[id$=input3]";
    private static final String DEFAULT_PLACEHOLDER_TEXT = "Watermark text";
    private static final String DEFAULT_PLACEHOLDER_CLASS = "rf-plhdr";
    //
    private final String componentName;
    //
    @FindBy(css = "[id$=placeholder]")
    private WebElement placeholder;
    @FindBy(css = INPUT1_ID)
    private WebElement input1;
    @FindBy(css = INPUT2_ID)
    private WebElement input2;
    @FindBy(css = INPUT3_ID)
    private WebElement input3;
    @FindBy(css = "[id$=a4jButton]")
    private WebElement a4jSubmitBtn;
    @FindBy(css = "[id$=hButton]")
    private WebElement hSubmitBtn;
    @Page
    private MetamerPage page;

    public AbstractPlaceholderNonJSFTest(String componentName) {
        this.componentName = componentName;
    }

    private void clearInput(WebElement input) {
        input.clear();
    }

    private void clickOnInput(WebElement input) {
        input.click();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPlaceholder/" + componentName + ".xhtml");
    }

    private String getInputStyleClass(WebElement input) {
        return input.getAttribute("class");
    }

    private Color getInputTextColor(WebElement input) {
        return ColorUtils.convertToAWTColor(input.getCssValue("color"));
    }

    private String getInputValue(WebElement input) {
        return input.getAttribute("value");
    }

    private void sendKeysToInput(WebElement input, String keys) {
        input.sendKeys(keys);
    }

    public void testClickOnInputWithPlaceholder() {
        placeholderAttributes.set(PlaceholderAttributes.selector, INPUT1_ID);
        assertEquals(getInputValue(input1), DEFAULT_PLACEHOLDER_TEXT, "Input 1 value");
        assertEquals(getInputValue(input2), "", "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertTrue(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertFalse(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.blue, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.black, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");

        clickOnInput(input1);
        Graphene.waitGui().until().element(input1).attribute("value").equalTo("");

        assertEquals(getInputValue(input1), "", "Input 1 value");
        assertEquals(getInputValue(input2), "", "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertFalse(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertFalse(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.black, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.black, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");
    }

    public void testDeleteTextFromInputWithPlaceholder() {
        placeholderAttributes.set(PlaceholderAttributes.selector, INPUT1_ID);
        String text = "abcd";

        sendKeysToInput(input1, text);
        Graphene.waitGui().until().element(input1).attribute("value").equalTo(text);

        assertEquals(getInputValue(input1), text, "Input 1 value");
        assertEquals(getInputValue(input2), "", "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertFalse(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertFalse(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.black, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.black, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");

        clearInput(input1);
        page.getRequestTimeElement().click();
        Graphene.waitGui().until().element(input1).attribute("value").equalTo(DEFAULT_PLACEHOLDER_TEXT);

        assertEquals(getInputValue(input1), DEFAULT_PLACEHOLDER_TEXT, "Input 1 value");
        assertEquals(getInputValue(input2), "", "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertTrue(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertFalse(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.blue, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.black, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");
    }

    public void testRendered() {
        placeholderAttributes.set(PlaceholderAttributes.selector, INPUT1_ID);
        placeholderAttributes.set(PlaceholderAttributes.rendered, Boolean.FALSE);

        assertFalse(Graphene.element(placeholder).isVisible().apply(driver), "Placeholder should not be visible");

        assertEquals(getInputValue(input1), "", "Input 1 value");
        assertEquals(getInputValue(input2), "", "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertFalse(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertFalse(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.black, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.black, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");
    }

    public void testSelectorSingle() {
        placeholderAttributes.set(PlaceholderAttributes.selector, INPUT1_ID);

        assertEquals(getInputValue(input1), DEFAULT_PLACEHOLDER_TEXT, "Input 1 value");
        assertEquals(getInputValue(input2), "", "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertTrue(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertFalse(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.blue, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.black, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");

        placeholderAttributes.set(PlaceholderAttributes.selector, INPUT2_ID);
        assertEquals(getInputValue(input2), DEFAULT_PLACEHOLDER_TEXT, "Input 1 value");
        assertEquals(getInputValue(input1), "", "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertTrue(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertFalse(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input2), Color.blue, "Input 1 text color");
        assertEquals(getInputTextColor(input1), Color.black, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");
    }

    public void testSelectorMultiple() {
        placeholderAttributes.set(PlaceholderAttributes.selector, INPUT1_ID + ", " + INPUT2_ID);

        assertEquals(getInputValue(input1), DEFAULT_PLACEHOLDER_TEXT, "Input 1 value");
        assertEquals(getInputValue(input2), DEFAULT_PLACEHOLDER_TEXT, "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertTrue(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertTrue(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.blue, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.blue, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");
    }

    public void testSelectorEmpty() {
        placeholderAttributes.set(PlaceholderAttributes.selector, "");

        assertEquals(getInputValue(input1), DEFAULT_PLACEHOLDER_TEXT, "Input 1 value");
        assertEquals(getInputValue(input2), DEFAULT_PLACEHOLDER_TEXT, "Input 2 value");
        assertEquals(getInputValue(input3), DEFAULT_PLACEHOLDER_TEXT, "Input 3 value");
        assertTrue(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertTrue(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertTrue(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.blue, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.blue, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.blue, "Input 3 text color");
    }

    public void testStyleClass() {
        placeholderAttributes.set(PlaceholderAttributes.selector, INPUT1_ID);
        testStyleClass(input1);
    }

    public void testTypeToInputWithPlaceholder() {
        placeholderAttributes.set(PlaceholderAttributes.selector, INPUT1_ID);
        String text = "abcd";

        sendKeysToInput(input1, text);
        Graphene.waitGui().until().element(input1).attribute("value").equalTo(text);

        assertEquals(getInputValue(input1), text, "Input 1 value");
        assertEquals(getInputValue(input2), "", "Input 2 value");
        assertEquals(getInputValue(input3), "", "Input 3 value");
        assertFalse(getInputStyleClass(input1).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 1 styleClass");
        assertFalse(getInputStyleClass(input2).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 2 styleClass");
        assertFalse(getInputStyleClass(input3).contains(DEFAULT_PLACEHOLDER_CLASS), "Input 3 styleClass");
        assertEquals(getInputTextColor(input1), Color.black, "Input 1 text color");
        assertEquals(getInputTextColor(input2), Color.black, "Input 2 text color");
        assertEquals(getInputTextColor(input3), Color.black, "Input 3 text color");
    }
}
