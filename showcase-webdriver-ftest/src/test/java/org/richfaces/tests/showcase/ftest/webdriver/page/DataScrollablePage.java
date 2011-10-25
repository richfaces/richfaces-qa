/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.ftest.webdriver.page;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.TextNotEquals;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class DataScrollablePage {
    
    // CLASSES
    private static final String CLASS_OF_BUTTON_FIRST = "rf-ds-btn rf-ds-btn-first";
    private static final String CLASS_OF_BUTTON_FAST_RWD = "rf-ds-btn rf-ds-btn-fastrwd";
    private static final String CLASS_OF_BUTTON_PREV = "rf-ds-btn rf-ds-btn-prev";
    private static final String CLASS_OF_BUTTON_NEXT = "rf-ds-btn rf-ds-btn-next";
    private static final String CLASS_OF_BUTTON_FAST_FWD = "rf-ds-btn rf-ds-btn-fastfwd";
    private static final String CLASS_OF_BUTTON_LAST = "rf-ds-btn rf-ds-btn-last";
    private static final String CLASS_OF_BUTTON_DIS = "rf-ds-dis";

    private static final String CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER = "rf-ds-nmb-btn";
    private static final String CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER = CLASS_OF_INACTIVE_BUTTON_WITH_NUMBER + " rf-ds-act";
    
    // ELEMENTS
    @FindBy(xpath="//*[@class='example-cnt']//span[contains(@class, '" + CLASS_OF_ACTIVE_BUTTON_WITH_NUMBER +"')]")
    private WebElement buttonWithNumberOfCurrentPage;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_FAST_FWD + "']")
    private WebElement fastNextButton;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_FAST_FWD + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement fastNextButtonDis;        
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_FAST_RWD + "']")
    private WebElement fastPreviousButton;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_FAST_RWD + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement fastPreviousButtonDis;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_FIRST + "']")
    private WebElement firstPageButton;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_FIRST + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement firstPageButtonDis;     
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_LAST + "']")
    private WebElement lastPageButton;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_LAST + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement lastPageButtonDis;      
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_NEXT + "']")
    private WebElement nextButton;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_NEXT + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement nextButtonDis;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_PREV + "']")
    private WebElement previousButton;
    @FindBy(xpath="//*[@class='example-cnt']//a[@class='" + CLASS_OF_BUTTON_PREV + " " + CLASS_OF_BUTTON_DIS + "']")
    private WebElement previousButtonDis;
    
    // OTHER
    private WebDriver webDriver;
    
    public DataScrollablePage(WebDriver webDriver) {
        Validate.notNull(webDriver);
        this.webDriver = webDriver;
    }
    
    public int getNumberOfCurrentPage() {
        return Integer.valueOf(buttonWithNumberOfCurrentPage.getText());
    }
    
    public void first() {
        clickAndWaitForPageChanged(firstPageButton);
    }

    public boolean isFirstPageButtonDisabled() {
        return ElementPresent.getInstance().element(firstPageButtonDis).apply(webDriver);
    }    
    
    public boolean isLastPageButtonDisabled() {
        return ElementPresent.getInstance().element(lastPageButtonDis).apply(webDriver);
    }
    
    public boolean iNextButtonDisabled() {
        return ElementPresent.getInstance().element(nextButtonDis).apply(webDriver);
    }    
    
    public boolean isNextFastButtonDisabled() {
        return ElementPresent.getInstance().element(fastNextButtonDis).apply(webDriver);
    }    
    
    public boolean isPreviousButtonDisabled() {
        return ElementPresent.getInstance().element(previousButtonDis).apply(webDriver);
    }    
    
    public boolean iPreviousFastDisabled() {
        return ElementPresent.getInstance().element(fastPreviousButtonDis).apply(webDriver);
    }    
    
    public void last() {
        clickAndWaitForPageChanged(lastPageButton);
    }    
    
    public void next() {
        clickAndWaitForPageChanged(nextButton);
    }
    
    public void nextFast() {
        clickAndWaitForPageChanged(fastNextButton);
    }
    
    public void previous() {
        clickAndWaitForPageChanged(previousButton);
    }
    
    public void previousFast() {
        clickAndWaitForPageChanged(fastPreviousButton);
    }
    
    private void clickAndWaitForPageChanged(WebElement button) {
        String currentPage = buttonWithNumberOfCurrentPage.getText();
        button.click();
        new WebDriverWait(webDriver)
            .failWith("After clicking on control button of data scroller, page doesn't change.")
            .until(TextNotEquals.getInstance().element(buttonWithNumberOfCurrentPage).text(currentPage));
    }
    
    protected WebDriver getWebDriver() {
        return webDriver;
    }
}
