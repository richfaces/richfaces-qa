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
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class RichFacesDataScroller implements DataScroller {

    @Root
    private WebElement root;
    @FindBy(className = "rf-ds-btn-first")
    private GrapheneElement firstBtn;
    @FindBy(className = "rf-ds-btn-fastrwd")
    private GrapheneElement fastRewindBtn;
    @FindBy(className = "rf-ds-btn-prev")
    private GrapheneElement previousBtn;
    @FindBy(className = "rf-ds-btn-next")
    private GrapheneElement nextBtn;
    @FindBy(className = "rf-ds-btn-fastfwd")
    private GrapheneElement fastForwardBtn;
    @FindBy(className = "rf-ds-btn-last")
    private GrapheneElement lastBtn;
    @FindBy(className = "rf-ds-nmb-btn")
    private List<GrapheneElement> numberedPages;
    @FindBy(className = "rf-ds-act")
    private GrapheneElement activePage;

    @Drone
    private WebDriver driver;
    private static final String CSS_PAGE_SELECTOR = "[id$='ds_%d'].rf-ds-nmb-btn";
    private static final String CLASS_DISABLED = "rf-ds-dis";

    private AdvancedInteractions advancedInteractions;

    @Override
    public int getActivePageNumber() {
        return Integer.valueOf(activePage.getText());
    }

    private void switchTo(By by) {
        WebElement element = root.findElement(by);
        element.click();
        Graphene.waitModel().until().element(element).attribute("class").contains("rf-ds-act");
    }

    @Override
    public void switchTo(int pageNumber) {
        int counter = 50; // to prevent infinite loops

        while (pageNumber > advanced().getLastVisiblePageNumber() && counter > 0) {
            switchTo(DataScrollerSwitchButton.FAST_FORWARD);
            counter--;
        }
        if (counter == 0) {
            throw new RuntimeException("Scroller doesn't change pages.");
        }

        counter = 50; // to prevent inifinite loops
        while (pageNumber < advanced().getFirstVisiblePageNumber() && counter > 0) {
            switchTo(DataScrollerSwitchButton.FAST_REWIND);
            counter--;
        }
        if (counter == 0) {
            throw new RuntimeException("Scroller doesn't change pages.");
        }

        if (pageNumber == getActivePageNumber()) {
            return;
        }
        switchTo(advanced().getCssSelectorForPageNumber(pageNumber));
    }

    @Override
    public void switchTo(DataScrollerSwitchButton btn) {
        String prevPageText = activePage.getText();
        advanced().getButton(btn).click();
        Graphene.waitModel().until().element(activePage).text().not().equalTo(prevPageText);
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions {
        public WebElement getRoot() {
            return root;
        }

        public WebElement getButton(DataScrollerSwitchButton btn) {
            return getButtonGrapheneElement(btn);
        }

        private GrapheneElement getButtonGrapheneElement(DataScrollerSwitchButton btn) {
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

        public By getCssSelectorForPageNumber(int pageNumber) {
            return By.cssSelector(String.format(CSS_PAGE_SELECTOR, pageNumber));
        }

        public List<? extends WebElement> getAllPagesWebElements() {
            return numberedPages;
        }

        public WebElement getFirstVisiblePageElement() {
            return numberedPages.get(0);
        }

        public int getFirstVisiblePageNumber() {
            return Integer.valueOf(getFirstVisiblePageElement().getText());
        }

        public WebElement getLastVisiblePageElement() {
            return numberedPages.get(numberedPages.size() - 1);
        }

        public int getLastVisiblePageNumber() {
            return Integer.valueOf(getLastVisiblePageElement().getText());
        }

        public WebElement getActivePageElement() {
            return activePage;
        }

        public int getCountOfVisiblePages() {
            return numberedPages.size();
        }

        public boolean isButtonDisabled(DataScrollerSwitchButton btn) {
            return getButton(btn).getAttribute("class").contains(CLASS_DISABLED);
        }

        public boolean isButtonPresent(DataScrollerSwitchButton btn) {
            return getButtonGrapheneElement(btn).isPresent();
        }

        public boolean isFirstPage() {
            return Integer.valueOf(activePage.getText()).equals(1);
        }

        public boolean isLastPage() {
            return activePage.getText().equals(advanced().getLastVisiblePageElement().getText());
        }
    }
}
