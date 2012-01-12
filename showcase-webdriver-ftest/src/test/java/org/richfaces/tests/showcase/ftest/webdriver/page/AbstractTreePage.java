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

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractTreePage implements ShowcasePage {

    private static final String COLLAPSED_XPATH = "span[contains(@class, 'rf-trn-hnd-colps')]";
    private static final String EXPANDED_CLASS = "span[contains(@class, 'rf-trn-hnd-exp')]";
    
    private static final String FIRST_LVL_COLLAPSED_XPATH = "//*[@class='example-cnt']//div[@class='rf-tr']/div/div[contains(@class, 'rf-trn')]/" + COLLAPSED_XPATH;
    private static final String FIRST_LVL_EXPANDED_XPATH = "//*[@class='example-cnt']//div[@class='rf-tr']/div/div[contains(@class, 'rf-trn')]/" + EXPANDED_CLASS;
    
    private static final String SECOND_LVL_COLLAPSED_XPATH = "//*[@class='example-cnt']//div[@class='rf-tr']/div/div/div[contains(@class, 'rf-trn')]/" + COLLAPSED_XPATH;
    private static final String SECOND_LVL_EXPANDED_XPATH = "//*[@class='example-cnt']//div[@class='rf-tr']/div/div/div[contains(@class, 'rf-trn')]/" + EXPANDED_CLASS;    
    private static final String SECOND_LVL_VISIBLE_XPATH = FIRST_LVL_EXPANDED_XPATH + "/../../div[position()>1]";
    private static final String SECOND_LVL_VISIBLE_EXPANDED_XPATH = SECOND_LVL_VISIBLE_XPATH + "/div/" + EXPANDED_CLASS;
    
    private static final String THIRD_LVL_VISIBLE_XPATH = SECOND_LVL_VISIBLE_EXPANDED_XPATH + "/../../div[position()>1]";

    @FindBy(xpath = FIRST_LVL_COLLAPSED_XPATH)
    private WebElement firstLvlCollapsed;
    @FindBy(xpath = FIRST_LVL_EXPANDED_XPATH)
    private WebElement firstLvlExpandend;
    @FindBy(xpath = SECOND_LVL_COLLAPSED_XPATH)
    private WebElement secondLvlCollapsed;
    @FindBy(xpath = SECOND_LVL_EXPANDED_XPATH)
    private WebElement secondLvlExpandend;
    
    private WebDriver webDriver;
    
    protected AbstractTreePage(WebDriver webDriver) {
        Validate.notNull(webDriver);
        this.webDriver = webDriver;
    }
    
    public void collapseFirstLevelAll() {
        toggleAll(By.xpath(FIRST_LVL_EXPANDED_XPATH));
    }

    public void collapseFirstLevelFirstNode() {
        toggle(firstLvlExpandend);
    }
    
    public void collapseSecondLevelAll() {
        toggleAll(By.xpath(SECOND_LVL_EXPANDED_XPATH));
    }    
 
    public void collapseSecondLevelFirstNode() {
        toggle(secondLvlExpandend);
    }      
    
    public int countSecondLevelVisible() {
        return webDriver.findElements(By.xpath(SECOND_LVL_VISIBLE_XPATH)).size();
    }
    
    public int countThirdLevelVisible() {
        return webDriver.findElements(By.xpath(THIRD_LVL_VISIBLE_XPATH)).size();
    }
    
    public void expandFirstLevelAll() {
        toggleAll(By.xpath(FIRST_LVL_COLLAPSED_XPATH));
    }

    public void expandFirstLevelFirstNode() {
        toggle(firstLvlCollapsed);
    }    
    
    public void expandSecondLevel() {
        toggleAll(By.xpath(SECOND_LVL_COLLAPSED_XPATH));
    }

    public void expandSecondLevelFirstNode() {
        toggle(secondLvlCollapsed);
    }    
    
    private void toggleAll(By locator) {
        List<WebElement> toToggle = webDriver.findElements(locator);
        for(WebElement icon: toToggle) {
            toggle(icon);
        }
    }
    
    private void toggle(WebElement toToggle) {
        if (webDriver instanceof JavascriptExecutor) {
            Point location = toToggle.getLocation();
            JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
            jsExecutor.executeScript("window.moveTo(" + location.getX() + ", " + location.getY() + ")");
        }
        toToggle.click();
        new WebDriverWait(webDriver)
            .failWith("Unable to toggle the given element.")
            .until(ElementPresent.getInstance().element(toToggle));        
    }
}
