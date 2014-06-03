/*
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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable.page;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.ExtendedDataTableAttributes;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SimpleEDT;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class SelectionPage {

    @FindBy(css = "span.currentSelection")
    private WebElement currentSelection;

    @FindBy(css = "span.previousSelection")
    private WebElement previousSelection;

    @FindBy(css = "span.rf-ds[id$=scroller2]")
    protected RichFacesDataScroller dataScroller2;

    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private SimpleEDT table;

    @Drone
    private WebDriver browser;

    private final Attributes<ExtendedDataTableAttributes> tableAttributes
        = Attributes.<ExtendedDataTableAttributes>getAttributesFor(getFutureDriver());

    public void selectRow(int rowIndex, Keys... keys) {
        scrollToPage(rowIndex);
        Graphene.guardAjax(table).selectRow(getRowForIndex(rowIndex), keys);
    }

    public void deselectRow(int rowIndex, Keys... keys) {
        scrollToPage(rowIndex);
        Graphene.guardAjax(table).deselectRow(getRowForIndex(rowIndex), keys);
    }

    private void scrollToPage(int rowIndex) {
        int page = getPageForIndex(rowIndex);
        if (page != dataScroller2.getActivePageNumber()) {
            Graphene.guardAjax(dataScroller2).switchTo(page);
        }
    }

    public Collection<Integer> getActualCurrentSelection() {
        return getSelection(currentSelection.getText());
    }

    public Collection<Integer> getActualPreviousSelection() {
        return getSelection(previousSelection.getText());
    }

    private Collection<Integer> getSelection(String selectionString) {
        String[] splitted = StringUtils.split(selectionString, "[], ");
        SortedSet<Integer> result = new TreeSet<Integer>();
        for (String selectedRow : splitted) {
            result.add(Integer.valueOf(selectedRow));
        }
        return result;
    }

    private int getPageForIndex(int index) {
        return (index / Integer.parseInt(tableAttributes.get(ExtendedDataTableAttributes.rows)))
            + 1;
    }

    private int getRowForIndex(int index) {
        return index
            - ((dataScroller2.getActivePageNumber() - 1)
            * Integer.parseInt(tableAttributes.get(ExtendedDataTableAttributes.rows)));
    }

    private AbstractWebDriverTest.FutureTarget<WebDriver> getFutureDriver() {
        return new AbstractWebDriverTest.FutureTarget<WebDriver>() {

            @Override
            public WebDriver getTarget() {
                return browser;
            }
        };
    }
}
