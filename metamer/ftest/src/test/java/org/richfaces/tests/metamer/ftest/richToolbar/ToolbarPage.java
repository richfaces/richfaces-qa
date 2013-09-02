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
package org.richfaces.tests.metamer.ftest.richToolbar;

import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class ToolbarPage {

    @FindBy(css = "table[id$=toolbar]")
    private WebElement toolbar;
    @FindBy(jquery = "td.rf-tb-sep")
    private RichFacesToolbarGroupSeparator separator;
    @FindBy(jquery = "td.rf-tb-sep")
    private List<RichFacesToolbarGroupSeparator> separators;
    @FindBy(jquery = "td.rf-tb-sep > img")
    private List<WebElement> separatorsImages;
    /** toolbar items with no matter if tool or empty cell */
    @FindBy(jquery = "tr.rf-tb-cntr > td")
    private List<WebElement> items;
    @FindBy(jquery = "td[id$=createDocument_itm]")
    private WebElement itemCreateDoc;
    @FindBy(jquery = "td[id$=createFolder_itm]")
    private WebElement itemCreateFolder;
    @FindBy(jquery = "td[id$=copy_itm]")
    private WebElement itemCopy;
    @FindBy(jquery = "td[id$=save_itm]")
    private WebElement itemSave;
    @FindBy(jquery = "td[id$=saveAs_itm]")
    private WebElement itemSaveAs;
    @FindBy(jquery = "td[id$=saveAll_itm]")
    private WebElement itemSaveAll;
    @FindBy(jquery = "td[id$=input_itm]")
    private WebElement itemInput;
    @FindBy(jquery = "td[id$=button_itm]")
    private WebElement itemButton;

    public WebElement getItemButton() {
        return itemButton;
    }

    public WebElement getItemCopy() {
        return itemCopy;
    }

    public WebElement getItemCreateDoc() {
        return itemCreateDoc;
    }

    public WebElement getItemCreateFolder() {
        return itemCreateFolder;
    }

    public WebElement getItemInput() {
        return itemInput;
    }

    public WebElement getItemSave() {
        return itemSave;
    }

    public WebElement getItemSaveAll() {
        return itemSaveAll;
    }

    public WebElement getItemSaveAs() {
        return itemSaveAs;
    }

    public List<WebElement> getItems() {
        return items;
    }

    public RichFacesToolbarGroupSeparator getSeparator() {
        return separator;
    }

    public List<RichFacesToolbarGroupSeparator> getSeparators() {
        return separators;
    }

    public List<WebElement> getSeparatorsImages() {
        return separatorsImages;
    }

    public WebElement getToolbar() {
        return toolbar;
    }
}
