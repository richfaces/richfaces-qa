/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.fragments.SortingHeaderInterface;
import org.richfaces.tests.metamer.ftest.webdriver.utils.MetamerJavascriptUtils;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class SortingEDTHeader implements SortingHeaderInterface {

    @ArquillianResource
    private WebDriver browser;

    private ColumnControl cc;
    @FindBy(className = "rf-edt-colctrl-btn")
    private WebElement columnControlButton;
    @JavaScript
    protected MetamerJavascriptUtils jsUtils;
    @FindByJQuery(".rf-edt-hdr-c-cnt:eq(1) a")
    private WebElement nameSortingAnchor;
    @FindByJQuery(".rf-edt-c-srt:eq(1)")
    private WebElement nameSortingBuiltIn;
    @FindByJQuery(".rf-edt-hdr-c-cnt:eq(3) a")
    private WebElement numberOfKidsSortingAnchor;
    @FindByJQuery(value = ".rf-edt-c-srt:eq(3)")
    private WebElement numberOfKidsSortingBuiltIn;
    @FindByJQuery(value = ".rf-edt-hdr-c-cnt:eq(0) a")
    private WebElement sexSortingAnchor;
    @FindByJQuery(".rf-edt-c-srt:eq(0)")
    private WebElement sexSortingBuiltIn;
    @FindByJQuery(".rf-edt-hdr-c-cnt:eq(2) a")
    private WebElement titleSortingAnchor;
    @FindByJQuery(".rf-edt-c-srt:eq(2)")
    private WebElement titleSortingBuiltIn;

    public ColumnControl openColumnControl() {
        if (cc == null) {
            cc = Graphene.createPageFragment(ColumnControl.class, browser.findElement(By.className("rf-edt-colctrl")));
        }
        if (!cc.isVisible()) {
            jsUtils.scrollToView(columnControlButton);
            columnControlButton.click();
            cc.waitUntilIsVisible();
        }
        return cc;
    }

    private void sortByColumn(WebElement sortingTrigger) {
        Graphene.guardAjax(sortingTrigger).click();
    }

    @Override
    public void sortByName(boolean isBuiltIn) {
        if (isBuiltIn) {
            sortByColumn(nameSortingBuiltIn);
        } else {
            sortByColumn(nameSortingAnchor);
        }
    }

    @Override
    public void sortByNumberOfKids(boolean isBuiltIn) {
        if (isBuiltIn) {
            sortByColumn(numberOfKidsSortingBuiltIn);
        } else {
            sortByColumn(numberOfKidsSortingAnchor);
        }
    }

    @Override
    public void sortBySex(boolean isBuiltIn) {
        if (isBuiltIn) {
            sortByColumn(sexSortingBuiltIn);
        } else {
            sortByColumn(sexSortingAnchor);
        }
    }

    @Override
    public void sortByTitle(boolean isBuiltIn) {
        if (isBuiltIn) {
            sortByColumn(titleSortingBuiltIn);
        } else {
            sortByColumn(titleSortingAnchor);
        }
    }
}
