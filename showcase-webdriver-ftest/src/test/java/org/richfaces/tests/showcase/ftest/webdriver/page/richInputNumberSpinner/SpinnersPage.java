/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.ftest.webdriver.page.richInputNumberSpinner;

import org.jboss.test.selenium.android.ToolKit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractAndroidPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SpinnersPage extends AbstractAndroidPage {

    private static final String FIRST_SPINNER_LOCATOR = "//*[@class='example-cnt']//span[@class='rf-insp '][1]";
    private static final String SECOND_SPINNER_LOCATOR = "//*[@class='example-cnt']//span[@class='rf-insp '][2]";
    
    @FindBy(xpath = FIRST_SPINNER_LOCATOR + "/input")
    private WebElement firstSpinnerInputElement;
    @FindBy(xpath = FIRST_SPINNER_LOCATOR + "/span/span[@class='rf-insp-inc']")
    private WebElement firstSpinnerUpArrowElement;
    @FindBy(xpath = FIRST_SPINNER_LOCATOR + "/span/span[@class='rf-insp-dec']")
    private WebElement firstSpinnerDownArrowElement;
    private Spinner firstSpinner;
    
    @FindBy(xpath = SECOND_SPINNER_LOCATOR + "/input")
    private WebElement secondSpinnerInputElement;
    @FindBy(xpath = SECOND_SPINNER_LOCATOR + "/span/span[@class='rf-insp-inc']")
    private WebElement secondSpinnerUpArrowElement;
    @FindBy(xpath = SECOND_SPINNER_LOCATOR + "/span/span[@class='rf-insp-dec']")
    private WebElement secondSpinnerDownArrowElement;
    private Spinner secondSpinner;
    
    public SpinnersPage(WebDriver webDriver, ToolKit toolKit) {
        super(webDriver, toolKit);
    }    

    @Override
    public String getDemoName() {
        return "inputNumberSpinner";
    }

    @Override
    public String getSampleName() {
        return "spinners";
    }

    public Spinner getFirstSpinner() {
        if (firstSpinner == null) {
            firstSpinner = new Spinner(getWebDriver(), getToolKit(), firstSpinnerInputElement, firstSpinnerDownArrowElement, firstSpinnerUpArrowElement, 1);
        }
        return firstSpinner;
    }

    public Spinner getSecondSpinner() {
        if (secondSpinner == null) {
            secondSpinner = new Spinner(getWebDriver(), getToolKit(), secondSpinnerInputElement, secondSpinnerDownArrowElement, secondSpinnerUpArrowElement, 10);
        }
        return secondSpinner;
    }    
}
