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
package org.richfaces.tests.showcase.ftest.webdriver.page.richList;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ListsPage implements ShowcasePage {

    @FindBy(xpath = "//*[@class='example-cnt']//dl")
    private WebElement definitionList;
    @FindBy(xpath = "//*[@class='example-cnt']//ol")
    private WebElement orderedList;
    @FindBy(xpath = "//*[@class='example-cnt']//ul")
    private WebElement unorderedList;
    @FindBy(xpath = "//*[@class='example-cnt']//a[text()='definitions']")
    private WebElement setDefinitionLink;
    @FindBy(xpath = "//*[@class='example-cnt']//a[text()='ordered']")
    private WebElement setOrderedLink;
    @FindBy(xpath = "//*[@class='example-cnt']//a[text()='unordered']")
    private WebElement setUnorderedLink;    
    
    private WebDriver webDriver;
    
    public ListsPage(WebDriver webDriver) {
        Validate.notNull(webDriver);
        this.webDriver = webDriver;
    }
    
    @Override
    public String getDemoName() {
        return "list";
    }

    @Override
    public String getSampleName() {
        return "lists";
    }

    public int getNumberOfItems() {
        if (isDefinition()) {
            return webDriver.findElements(By.xpath("//*[@class='example-cnt']//dd")).size();
        } else {
            return webDriver.findElements(By.xpath("//*[@class='example-cnt']//li")).size();
        }
    }
    
    public boolean isDefinition() {
        return isListPresent(definitionList);
    }    
    
    public boolean isOrdered() {
        return isListPresent(orderedList);
    }

    public boolean isUnordered() {
        return isListPresent(unorderedList);
    }    
    
    public void setDefinition() {
        setDefinitionLink.click();
        waitUntilListPresent(definitionList);
    }

    public void setOrdered() {
        setOrderedLink.click();
        waitUntilListPresent(orderedList);
    }    
    
    public void setUnordered() {
        setUnorderedLink.click();
        waitUntilListPresent(unorderedList);
    }    
    
    private boolean isListPresent(WebElement list) {
        try {
            list.isDisplayed();
            return true;
        } catch(NoSuchElementException ignored) {
            return false;
        }
    }
    
    private void waitUntilListPresent(WebElement list) {
        new WebDriverWait(webDriver)
            .failWith("Unable to change the list type.")
            .until(ElementPresent.getInstance().element(list));
    }
    
}
