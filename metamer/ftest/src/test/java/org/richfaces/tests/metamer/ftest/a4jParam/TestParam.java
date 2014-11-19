/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************
 */
package org.richfaces.tests.metamer.ftest.a4jParam;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jParam/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 5.0.0.Alpha1
 */
public class TestParam extends AbstractWebDriverTest {

    private final Attributes<ParamAttributes> paramAttributes = getAttributes();

    @Page
    private SimpleParamPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jParam/simple.xhtml");
    }

    @Test(groups = "smoke")
    public void testParameter() {
        MetamerPage.waitRequest(page.getButton1Element(), WaitRequestType.XHR).click();

        String screenWidth = ((JavascriptExecutor) driver).executeScript("return screen.width").toString();

        assertEquals(page.getOutput1Element().getText(), screenWidth, "Output 1 after clicking on first button.");
        assertEquals(page.getOutput2Element().getText(), screenWidth, "Output 2 after clicking on first button.");

        MetamerPage.waitRequest(page.getResetButtonElement(), WaitRequestType.XHR).click();

        assertEquals(page.getOutput1Element().getText(), "", "Output 1 after clicking on reset button.");
        assertEquals(page.getOutput2Element().getText(), "", "Output 2 after clicking on reset button.");

        MetamerPage.waitRequest(page.getButton2Element(), WaitRequestType.HTTP).click();

        assertEquals(page.getOutput1Element().getText(), "screen.width", "Output 1 after clicking on second button.");
        assertEquals(page.getOutput2Element().getText(), "screen.width", "Output 2 after clicking on second button.");
    }

    @Test
    public void testName() {
        paramAttributes.set(ParamAttributes.name, "metamer");

        MetamerPage.waitRequest(page.getButton1Element(), WaitRequestType.XHR).click();

        String screenWidth = ((JavascriptExecutor) driver).executeScript("return screen.width").toString();

        assertEquals(page.getOutput1Element().getText(), screenWidth, "Output 1 after clicking on first button.");
        assertEquals(page.getOutput2Element().getText(), screenWidth, "Output 2 after clicking on first button.");
    }

    @Test
    public void testNoEscape() {
        paramAttributes.set(ParamAttributes.noEscape, false);

        MetamerPage.waitRequest(page.getButton1Element(), WaitRequestType.XHR).click();

        assertEquals(page.getOutput1Element().getText(), "screen.width", "Output 1 after clicking on first button.");
        assertEquals(page.getOutput2Element().getText(), "screen.width", "Output 2 after clicking on first button.");
    }

    @Test
    public void testValue() {
        paramAttributes.set(ParamAttributes.value, "4+5");

        MetamerPage.waitRequest(page.getButton1Element(), WaitRequestType.XHR).click();

        assertEquals(page.getOutput1Element().getText(), "9", "Output 1 after clicking on first button.");
        assertEquals(page.getOutput2Element().getText(), "9", "Output 2 after clicking on first button.");

        MetamerPage.waitRequest(page.getResetButtonElement(), WaitRequestType.XHR).click();

        assertEquals(page.getOutput1Element().getText(), "", "Output 1 after clicking on reset button.");
        assertEquals(page.getOutput2Element().getText(), "", "Output 2 after clicking on reset button.");

        MetamerPage.waitRequest(page.getButton2Element(), WaitRequestType.HTTP).click();

        assertEquals(page.getOutput1Element().getText(), "4+5", "Output 1 after clicking on second button.");
        assertEquals(page.getOutput2Element().getText(), "4+5", "Output 2 after clicking on second button.");
    }

    public class SimpleParamPage extends MetamerPage {

        @FindBy(css = "input[id$=button1]")
        private WebElement button1Element;
        @FindBy(css = "input[id$=button2]")
        private WebElement button2Element;
        @FindBy(css = "input[id$=resetButton]")
        private WebElement resetButtonElement;
        @FindBy(css = "span[id$=output1]")
        private WebElement output1Element;
        @FindBy(css = "span[id$=output2]")
        private WebElement output2Element;

        /**
         * @return the button1Element
         */
        public WebElement getButton1Element() {
            return button1Element;
        }

        /**
         * @return the button2Element
         */
        public WebElement getButton2Element() {
            return button2Element;
        }

        /**
         * @return the output1Element
         */
        public WebElement getOutput1Element() {
            return output1Element;
        }

        /**
         * @return the output2Element
         */
        public WebElement getOutput2Element() {
            return output2Element;
        }

        /**
         * @return the resetButtonElement
         */
        public WebElement getResetButtonElement() {
            return resetButtonElement;
        }

    }
}
