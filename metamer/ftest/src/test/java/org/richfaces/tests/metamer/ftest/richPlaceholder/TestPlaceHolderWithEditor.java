/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.placeholderAttributes;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPlaceHolderWithEditor extends AbstractPlaceholderTest {

    @FindBy(css = INPUT1_ID + " textarea")
    WebElement input1;
    @FindBy(css = INPUT2_ID + " textarea")
    WebElement input2;

    public TestPlaceHolderWithEditor() {
        super("editor");
    }

    @Override
    WebElement getInput1() {
        return null;
    }

    @Override
    WebElement getInput2() {
        return null;
    }

    @Override
    protected String getInput1Value() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            return activeArea.getText();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Override
    protected String getInput2Value() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(1);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            return activeArea.getText();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Override
    protected String getInput1StyleClass() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            return activeArea.getAttribute("class");
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Override
    protected String getInput2StyleClass() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(1);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            return activeArea.getAttribute("class");
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Override
    protected void sendKeysToInput1(String keys) {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            activeArea.click();
            activeArea.sendKeys(keys);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Override
    protected void clearInput1() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            activeArea.click();
            activeArea.clear();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Override
    protected void clickOnInput1() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            activeArea.click();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Override
    protected void clickOnInput2() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(1);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            activeArea.click();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Override
    protected String getInput1Color() {
        try {
            // driver.switchTo().frame(page.editorFrame);
            driver.switchTo().frame(0);// must be this way
            WebElement activeArea = driver.findElement(By.tagName("body"));
            return activeArea.getCssValue("colot");
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Test(groups = "4.Future")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-12624", "https://issues.jboss.org/browse/RF-12625" })
    @Override
    public void testAjaxSubmit() {
        super.testAjaxSubmit();
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12624")
    @Override
    public void testClickOnInputWithPlaceholder() {
        super.testClickOnInputWithPlaceholder();
    }

    @Test(groups = "4.Future")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-12624", "https://issues.jboss.org/browse/RF-12632" })
    @Override
    public void testDeleteTextFromInputWithPlaceholder() {
        super.testDeleteTextFromInputWithPlaceholder();
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12624")
    @Override
    public void testHTTPSubmit() {
        super.testHTTPSubmit();
    }

    @Test(groups = "4.Future")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-12624", "https://issues.jboss.org/browse/RF-12632" })
    @Override
    public void testRendered() {
        super.testRendered();
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12624")
    @Override
    public void testSelector() {
        super.testSelector();
    }

    @Test(groups = "4.Future")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-12621", "https://issues.jboss.org/browse/RF-12624" })
    @Override
    public void testSelectorEmpty() {
        super.testSelectorEmpty();
    }

    @Test(groups = "4.Future")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-12624", "https://issues.jboss.org/browse/RF-12632" })
    @Override
    public void testStyleClass() {
        placeholderAttributes.set(PlaceholderAttributes.styleClass, "metamer-ftest-class");
        assertTrue(getInput1StyleClass().contains("metamer-ftest-class"), "Input does not contain set class 'metamer-ftest-class'");
    }

    @Test(groups = "4.Future")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-12624", "https://issues.jboss.org/browse/RF-12632" })
    @Override
    public void testTypeToInputWithPlaceholder() {
        super.testTypeToInputWithPlaceholder();
    }
}
