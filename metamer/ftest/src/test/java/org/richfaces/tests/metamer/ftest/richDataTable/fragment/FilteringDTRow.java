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
package org.richfaces.tests.metamer.ftest.richDataTable.fragment;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.FilteringRowInterface;
import org.richfaces.tests.metamer.model.Employee;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class FilteringDTRow implements FilteringRowInterface {

    @FindBy(css = "td:nth-of-type(2)")
    private WebElement nameColumnElement;
    @FindBy(css = "td:nth-of-type(4)")
    private WebElement numberOfKids1ColumnElement;
    @FindBy(css = "td:nth-of-type(5)")
    private WebElement numberOfKids2ColumnElement;
    @FindBy(css = "td:nth-of-type(1)")
    private WebElement sexColumnElement;
    @FindBy(css = "td:nth-of-type(1) img")
    private WebElement sexColumnImageElement;
    @FindBy(css = "td:nth-of-type(3)")
    private WebElement titleColumnElement;

    @Override
    public WebElement getNameColumnElement() {
        return nameColumnElement;
    }

    @Override
    public String getNameColumnValue() {
        return getNameColumnElement().getText();
    }

    @Override
    public WebElement getNumberOfKids1ColumnElement() {
        return numberOfKids1ColumnElement;
    }

    @Override
    public int getNumberOfKids1ColumnValue() {
        return Integer.parseInt(getNumberOfKids1ColumnElement().getText());
    }

    @Override
    public WebElement getNumberOfKids2ColumnElement() {
        return numberOfKids2ColumnElement;
    }

    @Override
    public int getNumberOfKids2ColumnValue() {
        return Integer.parseInt(getNumberOfKids2ColumnElement().getText());
    }

    @Override
    public WebElement getSexColumnElement() {
        return sexColumnElement;
    }

    @Override
    public Employee.Sex getSexColumnValue() {
        return Employee.Sex.valueOf(sexColumnImageElement.getAttribute("alt"));
    }

    @Override
    public WebElement getTitleColumnElement() {
        return titleColumnElement;
    }

    @Override
    public String getTitleColumnValue() {
        return getTitleColumnElement().getText();
    }
}
