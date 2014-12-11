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
package org.richfaces.tests.metamer.ftest.abstractions.fragments;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.tests.metamer.model.Employee;

import com.google.common.base.Strings;

public abstract class AbstractFilteringHeader implements FilteringHeaderInterface {

    @Drone
    private WebDriver browser;

    private void fillInAndBlur(TextInputComponentImpl input, String text) {
        if (Strings.isNullOrEmpty(text)) {
            input.advanced().clear(ClearType.BACKSPACE);
        } else {
            input.clear().sendKeys(text);
        }
        makeBlur();
    }

    @Override
    public void filterName(String name, boolean isBuiltIn) {
        if (isBuiltIn) {
            filterNameBuiltIn(name);
        } else {
            filterName(name);
        }
    }

    @Override
    public void filterName(String name) {
        fillInAndBlur(getNameInput(), name);
    }

    @Override
    public void filterNameBuiltIn(String name) {
        fillInAndBlur(getNameBuiltInInput(), name);
    }

    @Override
    public void filterNumberOfKidsBuiltIn(int numberOfKids) {
        fillInAndBlur(getNumberOfKidsInput(), String.valueOf(numberOfKids));
    }

    @Override
    public void filterNumberOfKidsWithSpinner(int numberOfKids) {
        getNumberOfKidsSpinner().setValue(numberOfKids);
        makeBlur();
    }

    @Override
    public void filterSex(Employee.Sex sex) {
        String option = sex == null ? "ALL" : sex.toString();
        guardAjax(getSexSelect()).selectByValue(option);
    }

    @Override
    public void filterTitle(String title, boolean isBuiltIn) {
        if (isBuiltIn) {
            filterTitleBuiltIn(title);
        } else {
            filterTitle(title);
        }
    }

    @Override
    public void filterTitle(String title) {
        fillInAndBlur(getTitleInput(), title);
    }

    @Override
    public void filterTitleBuiltIn(String title) {
        fillInAndBlur(getTitleBuildInInput(), title);
    }

    public void makeBlur() {
        try {
            guardAjax(browser.findElement(By.cssSelector("input[id$=metamerResponseDelayInput]"))).click();
        } catch (Exception ex) {
            // if no ajax then it has been already done
        }
    }
}
