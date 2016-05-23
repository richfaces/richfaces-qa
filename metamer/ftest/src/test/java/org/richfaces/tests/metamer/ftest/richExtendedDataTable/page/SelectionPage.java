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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable.page;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.ExtendedDataTableAttributes;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.SimpleEDT;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.utils.MetamerJavascriptUtils;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class SelectionPage {

    @FindBy(css = "span.currentSelection")
    private WebElement currentSelection;
    @FindBy(css = "span.rf-ds[id$=scroller2]")
    private RichFacesDataScroller dataScroller2;
    @FindBy(css = "[id$=deselectRow]")
    private WebElement deselectRowJSAPIButton;
    @JavaScript
    private MetamerJavascriptUtils jsUtils;
    @FindBy(css = "span.previousSelection")
    private WebElement previousSelection;
    @FindBy(css = "[id$=selectRowsUsingIndex]")
    private WebElement selectRowsUsingIndexJSAPIButton;
    @FindBy(css = "[id$=selectRowsUsingRange]")
    private WebElement selectRowsUsingRangeJSAPIButton;
    @FindBy(css = "div.rf-edt[id$=richEDT]")
    private SimpleEDT table;

    private Attributes<ExtendedDataTableAttributes> tableAttributes;

    public void deselectRow(int rowIndex, Keys... keys) {
        scrollToPage(rowIndex);
        String currentSelectionBefore = currentSelection.getText();
        Graphene.guardAjax(table).deselectRow(getRowForIndex(rowIndex), keys);
        Graphene.waitAjax().until().element(currentSelection).text().not().equalTo(currentSelectionBefore);
    }

    public Collection<Integer> getActualCurrentSelection() {
        return getSelection(currentSelection.getText());
    }

    public Collection<Integer> getActualPreviousSelection() {
        return getSelection(previousSelection.getText());
    }

    public RichFacesDataScroller getDataScroller() {
        return dataScroller2;
    }

    public WebElement getDeselectRowJSAPIButton() {
        return deselectRowJSAPIButton;
    }

    private int getPageForIndex(int index) {
        return (index / Integer.parseInt(tableAttributes.get(ExtendedDataTableAttributes.rows)))
            + 1;
    }

    public int getRowForIndex(int index) {
        return index
            - ((dataScroller2.getActivePageNumber() - 1)
            * Integer.parseInt(tableAttributes.get(ExtendedDataTableAttributes.rows)));
    }

    public WebElement getSelectRowsUsingIndexJSAPIButton() {
        return selectRowsUsingIndexJSAPIButton;
    }

    public WebElement getSelectRowsUsingRangeJSAPIButton() {
        return selectRowsUsingRangeJSAPIButton;
    }

    private Collection<Integer> getSelection(String selectionString) {
        String[] splitted = StringUtils.split(selectionString, "[], ");
        SortedSet<Integer> result = new TreeSet<Integer>();
        for (String selectedRow : splitted) {
            result.add(Integer.valueOf(selectedRow));
        }
        return result;
    }

    public SimpleEDT getTable() {
        return table;
    }

    private void scrollRowElementToView(int rowIndex) {
        jsUtils.scrollToView(table.advanced().getCellElement(0, Math.max(0, rowIndex - 1)));
    }

    private void scrollToPage(int rowIndex) {
        int page = getPageForIndex(rowIndex);
        if (page != dataScroller2.getActivePageNumber()) {
            Graphene.guardAjax(dataScroller2).switchTo(page);
        }
        scrollRowElementToView(getRowForIndex(rowIndex));
    }

    public void selectAllWithCrtlAndA() {
        String currentSelectionBefore = currentSelection.getText();
        Graphene.guardAjax(table).selectAllRowsWithKeyShortcut();
        Graphene.waitAjax().until().element(currentSelection).text().not().equalTo(currentSelectionBefore);
    }

    public void selectRow(int rowIndex, Keys... keys) {
        scrollToPage(rowIndex);
        String currentSelectionBefore = currentSelection.getText();
        Graphene.guardAjax(table).selectRow(getRowForIndex(rowIndex), keys);
        Graphene.waitAjax().until().element(currentSelection).text().not().equalTo(currentSelectionBefore);
    }

    public void setTableAttributes(Attributes<ExtendedDataTableAttributes> tableAttributes) {
        this.tableAttributes = tableAttributes;
    }
}
