/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.calendar.common.editor.yearAndMonth;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Component for calendar's year and month editor.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class YearAndMonthEditorImpl implements YearAndMonthEditor {

    @Root
    private WebElement root;
    //
    private WebDriver driver = GrapheneContext.getProxy();
    //
    @FindBy(css = "div[id*='DateEditorLayoutM']")
    private List<WebElement> months;
    @FindBy(css = "div[id*='DateEditorLayoutY']")
    private List<WebElement> years;
    @FindBy(xpath = "//tr[contains(@id,'DateEditorLayoutTR')][1] //td[3] //div")
    private WebElement previousDecadeButtonElement;
    @FindBy(xpath = "//tr[contains(@id,'DateEditorLayoutTR')][1] //td[4] //div")
    private WebElement nextDecadeButtonElement;
    @FindBy(css = "div[id$=DateEditorButtonOk]")
    private WebElement okButtonElement;
    @FindBy(css = "div[id$=DateEditorButtonCancel]")
    private WebElement cancelButtonElement;
    //
    private static final String SELECTED = "rf-cal-edtr-btn-sel";

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return Graphene.element(root).isVisible();
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return Graphene.element(root).not().isVisible();
    }

    @Override
    public void confirmDate() {
        okButtonElement.click();
    }

    @Override
    public void cancelDate() {
        cancelButtonElement.click();
    }

    @Override
    public List<String> getShortMonthsLabels() {
        List<WebElement> inRightOrder = inRightOrder(months);
        List<String> result = new ArrayList<String>(12);
        for (WebElement webElement : inRightOrder) {
            result.add(webElement.getText().trim());
        }
        return result;
    }

    @Override
    public List<Integer> getDisplayedYears() {
        List<WebElement> inRightOrder = inRightOrder(years);
        List<Integer> result = new ArrayList<Integer>(10);
        for (WebElement webElement : inRightOrder) {
            result.add(Integer.parseInt(webElement.getText().trim()));
        }
        return result;
    }

    @Override
    public DateTime getDate() {
        return new DateTime().withMonthOfYear(getSelectedMonth()).withYear(getSelectedYear());
    }

    @Override
    public YearAndMonthEditorImpl selectDate(DateTime date) {
        return selectDate(date.getMonthOfYear(), date.getYear());
    }

    private YearAndMonthEditorImpl selectDate(int month, int year) {
        Validate.isTrue(month > 0 && month < 13, "Month number has to be in interval <1,12>");//this should not be necessary
        selectMonth(month);
        selectYear(year);
        return this;
    }

    @Override
    public Integer getSelectedMonth() {
        String classAttribute = "class";
        List<WebElement> inRightOrder = inRightOrder(months);
        int i = 0;
        for (WebElement webElement : inRightOrder) {
            if (webElement.getAttribute(classAttribute).contains(SELECTED)) {
                return i + 1;
            }
            i++;
        }
        return null;
    }

    @Override
    public Integer getSelectedYear() {
        String classAttribute = "class";
        for (WebElement year : years) {
            if (year.getAttribute(classAttribute).contains(SELECTED)) {
                return Integer.parseInt(year.getText().trim());
            }
        }
        return null;
    }

    private void selectMonth(int month) {
        inRightOrder(months).get(month - 1).click();
    }

    private void selectYear(int year) {
        int yearsPickerSize = years.size();
        int yearInTheFirstColumn = Integer.parseInt(years.get(0).getText());
        int diff = year - yearInTheFirstColumn;
        if (diff > 0 && diff >= yearsPickerSize) {
            while (diff > 0) {
                nextDecadeButtonElement.click();
                diff -= yearsPickerSize;
            }
        } else {
            while (diff < 0) {
                previousDecadeButtonElement.click();
                diff += yearsPickerSize;
            }
        }
        String yearString = String.valueOf(year);
        for (WebElement yearElement : years) {
            if (yearElement.getText().trim().equals(yearString)) {
                yearElement.click();
                return;
            }
        }
        throw new IllegalStateException("The year was not found");
    }

    private List<WebElement> inRightOrder(List<WebElement> list) {
        List<WebElement> result = new ArrayList<WebElement>(12);
        List<WebElement> result1 = new ArrayList<WebElement>(6);
        List<WebElement> result2 = new ArrayList<WebElement>(6);
        int x = 0;
        for (WebElement webElement : list) {
            if (x % 2 == 0) {
                result1.add(webElement);
            } else {
                result2.add(webElement);
            }
            x++;
        }
        result.addAll(result1);
        result.addAll(result2);
        return result;
    }

    @Override
    public void nextDecade() {
        nextDecadeButtonElement.click();
    }

    @Override
    public void previousDecade() {
        previousDecadeButtonElement.click();
    }

    @Override
    public WebElement getPreviousDecadeButtonElement() {
        return previousDecadeButtonElement;
    }

    @Override
    public WebElement getNextDecadeButtonElement() {
        return nextDecadeButtonElement;
    }

    @Override
    public WebElement getOkButtonElement() {
        return okButtonElement;
    }

    @Override
    public WebElement getCancelButtonElement() {
        return cancelButtonElement;
    }
}
