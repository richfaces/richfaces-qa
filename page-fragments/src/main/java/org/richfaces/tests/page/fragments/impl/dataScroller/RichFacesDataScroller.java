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
package org.richfaces.tests.page.fragments.impl.dataScroller;

import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class RichFacesDataScroller implements DataScroller {

    @Root
    protected WebElement root;
    @FindBy(className = "rf-ds-btn-first")
    protected WebElement firstBtn;
    @FindBy(className = "rf-ds-btn-fastrwd")
    protected WebElement fastRewindBtn;
    @FindBy(className = "rf-ds-btn-prev")
    protected WebElement previousBtn;
    @FindBy(className = "rf-ds-btn-next")
    protected WebElement nextBtn;
    @FindBy(className = "rf-ds-btn-fastfwd")
    protected WebElement fastForwardBtn;
    @FindBy(className = "rf-ds-btn-last")
    protected WebElement lastBtn;
    @FindBy(className = "rf-ds-nmb-btn")
    protected List<WebElement> numberedPages;
    @FindBy(className = "rf-ds-act")
    protected WebElement actPage;

    @Drone
    private WebDriver driver;
    private static final String CSS_PAGE_SELECTOR = "[id$='ds_%d'].rf-ds-nmb-btn";
    private static final String CLASS_DISABLED = "rf-ds-dis";

    @Override
    public int getActPageNumber() {
        return Integer.valueOf(actPage.getText());
    }

    private WebElement getButton(DataScrollerSwitchButton btn) {
        switch (btn) {
            case FAST_FORWARD:
                return fastForwardBtn;
            case FAST_REWIND:
                return fastRewindBtn;
            case FIRST:
                return firstBtn;
            case LAST:
                return lastBtn;
            case NEXT:
                return nextBtn;
            case PREVIOUS:
                return previousBtn;
            default:
                throw new UnsupportedOperationException("Unknown button " + btn);
        }
    }

    private By getCssSelectorForPageNumber(int pageNumber) {
        return By.cssSelector(String.format(CSS_PAGE_SELECTOR, pageNumber));
    }

    private WebElement getFirstVisiblePageElement() {
        return numberedPages.get(0);
    }

    private int getFirstVisiblePageNumber() {
        return Integer.valueOf(getFirstVisiblePageElement().getText());
    }

    private WebElement getLastVisiblePageElement() {
        return numberedPages.get(numberedPages.size() - 1);
    }

    private int getLastVisiblePageNumber() {
        return Integer.valueOf(getLastVisiblePageElement().getText());
    }

    @Override
    public boolean isButtonDisabled(DataScrollerSwitchButton btn) {
        return getButton(btn).getAttribute("class").contains(CLASS_DISABLED);
    }

    @Override
    public boolean isFirstPage() {
        return Integer.valueOf(actPage.getText()).equals(1);
    }

    @Override
    public boolean isLastPage() {
        return actPage.getText().equals(getLastVisiblePageElement().getText());
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    private void switchTo(By by) {
        root.findElement(by).click();
    }

    @Override
    public void switchTo(int pageNumber) {
        int counter = 50; // to prevent infinite loops
        String prevPageText = actPage.getText();
        while (pageNumber > getLastVisiblePageNumber() && counter > 0) {
            switchTo(DataScrollerSwitchButton.FAST_FORWARD);
            Graphene.waitModel().until().element(actPage).text().not().equalTo(prevPageText);
            prevPageText = actPage.getText();
            counter--;
        }
        if (counter == 0) {
            throw new RuntimeException("Scroller doesn't change pages.");
        }
        counter = 50; // to prevent inifinite loops
        while (pageNumber < getFirstVisiblePageNumber() && counter > 0) {
            switchTo(DataScrollerSwitchButton.FAST_REWIND);
            Graphene.waitModel().until().element(actPage).text().not().equalTo(prevPageText);
            prevPageText = actPage.getText();
            counter--;
        }
        if (counter == 0) {
            throw new RuntimeException("Scroller doesn't change pages.");
        }
        if (pageNumber == getActPageNumber()) {
            return;
        }
        switchTo(getCssSelectorForPageNumber(pageNumber));
    }

    @Override
    public void switchTo(DataScrollerSwitchButton btn) {
        getButton(btn).click();
    }
}
