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
package org.richfaces.tests.showcase.ftest.webdriver.page.richOrderingList;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.ftest.webdriver.page.AbstractWebDriverPage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class OrderingListPage extends AbstractWebDriverPage {

    @FindBy(xpath = "//*[@class='example-cnt']//button[contains(@class, 'rf-ord-dn')]")
    public WebElement downButton;
    @FindBy(xpath = "//*[@class='example-cnt']//button[contains(@class, 'rf-ord-up-tp')]")
    public WebElement firstButton;
    @FindBy(xpath = "//*[@class='example-cnt']//button[contains(@class, 'rf-ord-dn-bt')]")
    public WebElement lastButton;
    @FindBy(xpath = "//*[@class='example-cnt']//button[contains(@class, 'rf-ord-up')]")
    public WebElement upButton;

    @FindBy(xpath = "//*[@class='example-cnt']//div[contains(@class, 'rf-ord-lst-scrl')]/div")
    private WebElement itemList;

    @Override
    public String getDemoName() {
        return "orderingList";
    }

    public int getNumberOfItems() {
        return 50; // FIXME: hardcoded
    }

    @Override
    public String getSampleName() {
        return "orderingList";
    }

    public boolean isBottomButtonEnabled() {
        return isButtonEnabled(lastButton);
    }

    public boolean isDownButtonEnabled() {
        return isButtonEnabled(downButton);
    }

    public boolean isTopButtonEnabled() {
        return isButtonEnabled(firstButton);
    }

    public boolean isUpButtonEnabled() {
        return isButtonEnabled(upButton);
    }

    public void select(int index) {
        final WebElement toSelect = getItemElement(index);
        toSelect.click();
        Graphene.waitAjax()
            .withMessage("The requested item hasn't been selected.")
            .until().element(toSelect).attribute("class").contains("rf-ord-sel");
    }

    private WebElement getItemElement(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("The index has to be non-negative number");
        }
        return itemList.findElement(By.xpath("div[position() = " + (index + 1) + "]"));
    }

    private boolean isButtonEnabled(WebElement button) {
        String disabled = button.getAttribute("disabled");
        return disabled == null || disabled.trim().isEmpty() || disabled.trim().equals("false");
    }
}
