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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment;

import org.jboss.arquillian.drone.api.annotation.Drone;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.FilteringHeaderInterface;
import org.richfaces.tests.metamer.model.Employee;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class FilteringEDTHeader implements FilteringHeaderInterface {

    @FindByJQuery("[id$='columnHeaderSexInput']")
    private Select sexSelect;

    @FindByJQuery("[id$='columnHeaderNameInput']")
    private WebElement nameInput;

    @FindByJQuery("[id$='columnHeaderTitleInput']")
    private WebElement titleInput;

    @FindByJQuery("[id$='spinnerFilter']")
    private RichFacesInputNumberSpinner numberOfKidsSpinner;

    @FindByJQuery("[id$='columnName:flt']")
    private WebElement nameInputBuiltIn;

    @FindByJQuery("[id$='columnTitle:flt']")
    private WebElement titleInputBuiltIn;

    @FindByJQuery("[id$='columnNumberOfKids1:flt']")
    private WebElement numberOfKids1BuiltInInput;

    @FindByJQuery("[id$='columnNumberOfKids2:flt']")
    private WebElement numberOfKids2BuiltInInput;

    @Drone
    private WebDriver browser;

    @Override
    public void filterSex(Employee.Sex sex) {
        String option = sex == null ? "ALL" : sex.toString();
        guardAjax(sexSelect).selectByValue(option);
    }

    @Override
    public void filterName(String name, boolean isBuiltIn) {
        if(isBuiltIn){
            filterNameBuiltIn(name);
        }
        else{
            nameInput.click();
            nameInput.sendKeys(name == null ? "" : name);
            makeBlur();
        }
    }

    @Override
    public void filterTitle(String title, boolean isBuiltIn) {
        if(isBuiltIn){
            filterTitleBuiltIn(title);
        }
        else{
            titleInput.click();
            titleInput.sendKeys(title == null ? "" : title);
            makeBlur();
        }
    }

    @Override
    public void filterNumberOfKidsWithSpinner(int numberOfKids) {
        numberOfKidsSpinner.advanced().getRootElement().click();
        numberOfKidsSpinner.setValue(numberOfKids);
        makeBlur();
    }

    @Override
    public WebElement getFilterNameInput() {
        return nameInput;
    }

    @Override
    public WebElement getFilterTitleInput() {
        return titleInput;
    }

    private void makeBlur() {
        try {
            guardAjax(browser.findElement(ByJQuery.selector("span[id$=requestTime]"))).click();
        } catch (Exception ex) {
            // if no ajax then it has been already done
        }
    }

    @Override
    public void filterNameBuiltIn(String name) {
        nameInputBuiltIn.click();
        nameInputBuiltIn.sendKeys(name);
        makeBlur();
    }

    @Override
    public void filterTitleBuiltIn(String title) {
        titleInputBuiltIn.click();
        titleInputBuiltIn.sendKeys(title);
        makeBlur();

    }

    @Override
    public void filterNumberOfKidsBuiltIn(int numberOfKids) {
        numberOfKids1BuiltInInput.click();
        numberOfKids1BuiltInInput.sendKeys(Integer.toString(numberOfKids));
        makeBlur();
    }

    @Override
    public WebElement getFilterNameBuiltInInput() {
        return nameInputBuiltIn;
    }

    @Override
    public WebElement getFilterTitleBuiltInInput() {
        return titleInputBuiltIn;
    }

    @Override
    public WebElement getFilterKidsGreaterBuiltInInput() {
        return numberOfKids1BuiltInInput;
    }

    @Override
    public WebElement getFilterKidsLesserBuiltInInput() {
        return numberOfKids2BuiltInInput;
    }
}
