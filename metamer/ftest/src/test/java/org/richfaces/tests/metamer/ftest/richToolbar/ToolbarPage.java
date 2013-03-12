/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richToolbar;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 *
 */
public class ToolbarPage {

    @FindBy(css = "table[id$=toolbar]")
    public WebElement toolbar;

    @FindBy(jquery = "td.rf-tb-sep")
    public WebElement separator;

    @FindBy(jquery = "td[id$=createDocument_itm]")
    public WebElement itemCreateDoc;

    @FindBy(jquery = "td[id$=createFolder_itm]")
    public WebElement itemCreateFolder;

    @FindBy(jquery = "td[id$=copy_itm]")
    public WebElement itemCopy;

    @FindBy(jquery = "td[id$=save_itm]")
    public WebElement itemSave;

    @FindBy(jquery = "td[id$=saveAs_itm]")
    public WebElement itemSaveAs;

    @FindBy(jquery = "td[id$=saveAll_itm]")
    public WebElement itemSaveAll;

    @FindBy(jquery = "td[id$=input_itm]")
    public WebElement itemInput;

    @FindBy(jquery = "td[id$=button_itm]")
    public WebElement itemButton;

}
