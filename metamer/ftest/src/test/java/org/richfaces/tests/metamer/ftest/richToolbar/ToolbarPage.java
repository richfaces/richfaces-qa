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

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class ToolbarPage {

    @FindBy(css = "table[id$=toolbar]")
    private WebElement toolbar;
    @FindByJQuery("td.rf-tb-sep")
    private RichFacesToolbarGroupSeparator separator;
    @FindByJQuery("td.rf-tb-sep")
    private List<RichFacesToolbarGroupSeparator> separators;
    @FindByJQuery("td.rf-tb-sep > img")
    private List<WebElement> separatorsImages;
    /** toolbar items with no matter if tool or empty cell */
    @FindByJQuery("tr.rf-tb-cntr > td")
    private List<GrapheneElement> items;
    @FindByJQuery("td[id$=createDocument_itm]")
    private GrapheneElement itemCreateDoc;
    @FindByJQuery("td[id$=createFolder_itm]")
    private GrapheneElement itemCreateFolder;
    @FindByJQuery("td[id$=copy_itm]")
    private GrapheneElement itemCopy;
    @FindByJQuery("td[id$=save_itm]")
    private GrapheneElement itemSave;
    @FindByJQuery("td[id$=saveAs_itm]")
    private GrapheneElement itemSaveAs;
    @FindByJQuery("td[id$=saveAll_itm]")
    private GrapheneElement itemSaveAll;
    @FindByJQuery("td[id$=input_itm]")
    private GrapheneElement itemInput;
    @FindByJQuery("td[id$=button_itm]")
    private GrapheneElement itemButton;

    public GrapheneElement getItemButton() {
        return itemButton;
    }

    public GrapheneElement getItemCopy() {
        return itemCopy;
    }

    public GrapheneElement getItemCreateDoc() {
        return itemCreateDoc;
    }

    public GrapheneElement getItemCreateFolder() {
        return itemCreateFolder;
    }

    public GrapheneElement getItemInput() {
        return itemInput;
    }

    public GrapheneElement getItemSave() {
        return itemSave;
    }

    public GrapheneElement getItemSaveAll() {
        return itemSaveAll;
    }

    public GrapheneElement getItemSaveAs() {
        return itemSaveAs;
    }

    public List<GrapheneElement> getItems() {
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
