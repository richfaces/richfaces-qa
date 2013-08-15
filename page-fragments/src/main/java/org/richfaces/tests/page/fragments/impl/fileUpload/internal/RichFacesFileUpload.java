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
package org.richfaces.tests.page.fragments.impl.fileUpload.internal;

import com.google.common.base.Predicate;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.list.internal.ListItems;
import org.richfaces.tests.page.fragments.impl.list.internal.ListItemsFilterBuilderImpl;

public class RichFacesFileUpload implements FileUpload {

    @Drone
    private WebDriver driver;
    @Root
    private WebElement rootElement;
    @FindBy(className = "rf-fu-lst")
    private RichFacesFileUploadList items;
    @FindBy(className = "rf-fu-btn-cnt-add")
    private WebElement addButtonElement;
    @FindBy(className = "rf-fu-btn-cnt-add-dis")
    private WebElement disabledAddButtonElement;
    @FindBy(className = "rf-fu-btn-clr")
    private WebElement clearAllButtonElement;
    @FindBy(className = "rf-fu-btn-cnt-upl")
    private WebElement uploadButtonElement;
    @FindBy(jquery = "input[type=file].rf-fu-inp:last")
    private WebElement fileInputElement;
    @FindBy(css = "input[type=file].rf-fu-inp")
    private List<WebElement> fileInputElements;

    @Override
    public FileUpload addFile(File file) {
        final int expectedSize = fileInputElements.size() + 1;
        fileInputElement.sendKeys(file.getAbsolutePath());
        try {
            Graphene.waitGui().withTimeout(1, TimeUnit.SECONDS).until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return fileInputElements.size() == expectedSize;
                }
            });
        } catch (TimeoutException ignored) {
        }
        return this;
    }

    @Override
    public FileUpload clearAll() {
        getClearAllButtonElement().click();
        Graphene.waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return getItems().isEmpty();
            }
        });
        return this;
    }

    @Override
    public WebElement getAddButtonElement() {
        return addButtonElement;
    }

    @Override
    public WebElement getClearAllButtonElement() {
        return clearAllButtonElement;
    }

    @Override
    public WebElement getFileInputElement() {
        return fileInputElement;
    }

    @Override
    public ListItems<RichFacesFileUploadItem> getItems() {
        return items.getItems();
    }

    @Override
    public RichFacesFileUploadList getItemsList() {
        return items;
    }

    @Override
    public WebElement getRootElement() {
        return rootElement;
    }

    @Override
    public WebElement getUploadButtonElement() {
        return uploadButtonElement;
    }

    @Override
    public boolean isDisabled() {
        return Graphene.element(disabledAddButtonElement).isVisible().apply(driver)
                && Graphene.element(addButtonElement).not().isVisible().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(rootElement).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(rootElement).isVisible();
    }

    @Override
    public FileUpload removeFile(final String filename) {
        ListItems<RichFacesFileUploadItem> items = getItems().filter(new ListItemsFilterBuilderImpl<RichFacesFileUploadItem>().addFilter(new Predicate<RichFacesFileUploadItem>() {
            @Override
            public boolean apply(RichFacesFileUploadItem input) {
                return input.getFilename().equals(filename);
            }
        }));
        for (RichFacesFileUploadItem item : items) {
            item.remove();
        }
        return this;
    }

    @Override
    public FileUpload upload() {
        getUploadButtonElement().click();
        return this;
    }
}
