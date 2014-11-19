/**
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
 */
package org.richfaces.tests.metamer.ftest.a4jAjax;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class AjaxPage extends MetamerPage {

    @FindBy(css = "input[type=submit][id$=commandButton]")
    private WebElement buttonElement;
    @FindBy(css = "img[id$=image]")
    private WebElement imageElement;
    @FindBy(css = "[id$=input]")
    private WebElement inputElement;
    @FindBy(css = "a[id$=commandLink]")
    private WebElement linkElement;
    @FindBy(css = "[id$=output1]")
    private WebElement output1Element;
    @FindBy(css = "[id$=output2]")
    private WebElement output2Element;
    @FindBy(css = "input[type=checkbox][id$=selectBooleanCheckbox]")
    private WebElement selectBooleanCheckboxElement;
    @FindBy(css = "table[id$=selectManyCheckbox] input[value=Audi]")
    private WebElement selectManyCheckboxElement;
    @FindBy(css = "select[id$=selectManyListbox]")
    private Select selectManyListbox;
    @FindBy(css = "select[id$=selectManyMenu]")
    private Select selectManyMenu;
    @FindBy(css = "select[id$=selectOneListbox]")
    private Select selectOneListbox;
    @FindBy(css = "select[id$=selectOneMenu]")
    private Select selectOneMenu;
    @FindBy(css = "table[id$=selectOneRadio] input[type=radio][value=Audi]")
    private WebElement selectOneRadioElement;

    /**
     * @return the buttonElement
     */
    public WebElement getButtonElement() {
        return buttonElement;
    }

    /**
     * @return the imageElement
     */
    public WebElement getImageElement() {
        return imageElement;
    }

    /**
     * @return the inputElement
     */
    public WebElement getInputElement() {
        return inputElement;
    }

    /**
     * @return the linkElement
     */
    public WebElement getLinkElement() {
        return linkElement;
    }

    /**
     * @return the output1Element
     */
    public WebElement getOutput1Element() {
        return output1Element;
    }

    /**
     * @return the output2Element
     */
    public WebElement getOutput2Element() {
        return output2Element;
    }

    /**
     * @return the selectBooleanCheckboxElement
     */
    public WebElement getSelectBooleanCheckboxElement() {
        return selectBooleanCheckboxElement;
    }

    /**
     * @return the selectManyCheckboxElement
     */
    public WebElement getSelectManyCheckboxElement() {
        return selectManyCheckboxElement;
    }

    /**
     * @return the selectManyListbox
     */
    public Select getSelectManyListbox() {
        return selectManyListbox;
    }

    /**
     * @return the selectManyMenu
     */
    public Select getSelectManyMenu() {
        return selectManyMenu;
    }

    /**
     * @return the selectOneListbox
     */
    public Select getSelectOneListbox() {
        return selectOneListbox;
    }

    /**
     * @return the selectOneMenu
     */
    public Select getSelectOneMenu() {
        return selectOneMenu;
    }

    /**
     * @return the selectOneRadioElement
     */
    public WebElement getSelectOneRadioElement() {
        return selectOneRadioElement;
    }

}
