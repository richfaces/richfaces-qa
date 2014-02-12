/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.ftest.webdriver.page.richPanelMenu;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.test.selenium.android.support.ui.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class PanelMenu extends AbstractComponent{

    public PanelMenu(WebDriver webDriver, WebElement webElement) {
        super(webDriver, webElement);
    }

    public int getNumberOfFirstLevelGroups() {
        return getWebElement().findElements(By.xpath("div[@class='rf-pm-top-gr']")).size();
    }

    public boolean isFirstLevelGroupExpanded(int index) {
        try {
            getWebElement().findElement(By.xpath("div[@class='rf-pm-top-gr'][" + (index+1) + "]/div[contains(@class,'rf-pm-hdr-exp')]"));
            return true;
        }
        catch(NoSuchElementException ignored) {}
        catch(StaleElementReferenceException ignored) {}
        return false;
    }

    public void toggleFirstLevelGroup(final int index) {
        boolean expanded = isFirstLevelGroupExpanded(index);
        getWebElement().findElement(By.xpath("div[@class='rf-pm-top-gr'][" + (index+1) + "]/div[1]")).click();
        ExpectedCondition<Boolean> condition;
        if (expanded) {
            condition = new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver arg0) {
                    return !isFirstLevelGroupExpanded(index);
                }
            };
        } else {
            condition = new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver arg0) {
                    return isFirstLevelGroupExpanded(index);
                }
            };
        }
        Graphene.waitAjax()
            .withMessage("The group <" + index + "> in panel menu can't be " + (expanded ? "collapsed" : "expanded") + ".")
            .until(condition);
    }

    public void selectSecondLevelItem(int groupIndex, int itemIndex) {
        final String itemLocator = "div[@class='rf-pm-top-gr'][" + (groupIndex+1) + "]//div[contains(@class, 'rf-pm-itm')][" + (itemIndex+1) + "]";
        getWebElement().findElement(By.xpath(itemLocator + "//td[@class='rf-pm-itm-lbl']")).click();
        Graphene.waitAjax()
            .withMessage("The option <" + groupIndex + "><" + itemIndex +"> can't be selected.")
            .until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver webDriver) {
                    try {
                        return getWebElement().findElement(By.xpath(itemLocator)).getAttribute("class").contains("rf-pm-itm-sel");
                    } catch(Exception ignored) {
                        return false;
                    }
                }
            });
    }

}
