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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import static java.lang.Math.max;
import static java.lang.Math.min;

import javax.xml.bind.JAXBException;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.richDataGrid.fragment.GridWithStates;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
public abstract class AbstractDataGridScrollerTest extends AbstractDataGridTest {

    @FindBy(css = ".rf-dg[id$=richDataGrid]")
    private GridWithStates dataGrid;

    private int[] testPages;

    public AbstractDataGridScrollerTest() throws JAXBException {
        super();
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDataGrid/scroller.xhtml";
    }

    @Override
    public GridWithStates getDataGrid() {
        return dataGrid;
    }

    public abstract RichFacesDataScroller getDataScroller();

    public void initializeTestedPages(int lastPage) {
        int l = lastPage;
        testPages = new int[] { 3, l, 1, l - 2, l, 2, l - 2, l - 1 };
        for (int i = 0; i < testPages.length; i++) {
            testPages[i] = min(l, max(1, testPages[i]));
        }
    }

    protected abstract boolean isDataScrollerOutsideGrid();

    public void setupDataScroller() {
        if (getDataScroller().hasPages()) {
            initializeTestedPages(getDataScroller().advanced().getLastVisiblePageNumber());
        } else {
            initializeTestedPages(0);
        }
    }

    public void testColumnsAttribute() {
        elements = 7;
        first = 0;
        attsSetter()
            .setAttribute(DataGridAttributes.elements).toValue(elements)
            .setAttribute(DataGridAttributes.first).toValue(first)
            .asSingleAction().perform();

        setupDataScroller();
        testNumberedPages();
        dataGridAttributes.set(DataGridAttributes.elements, 0);
    }

    public void testElementsAttribute() {
        first = 0;
        columns = 3;
        attsSetter()
            .setAttribute(DataGridAttributes.columns).toValue(columns)
            .setAttribute(DataGridAttributes.first).toValue(first)
            .asSingleAction().perform();
        setupDataScroller();
        testNumberedPages();
    }

    public void testFirstAttributeDoesntInfluentScroller() {
        // the attribute for component was already set, now verify that this attribute doesn't influent rendering (it
        // means dataGrid with scroller ignores this attribute, it means it is always equal to zero)
        first = 0;
        columns = 3;
        elements = 7;
        attsSetter()
            .setAttribute(DataGridAttributes.columns).toValue(columns)
            .setAttribute(DataGridAttributes.first).toValue(first)
            .setAttribute(DataGridAttributes.elements).toValue(elements)
            .asSingleAction().perform();
        setupDataScroller();
        testNumberedPages();
    }

    public void testNumberedPages() {
        if (isInPopupTemplate() && isDataScrollerOutsideGrid()) {
            popupTemplate.advanced().moveByOffset(100, 200);
        }
        Integer lastNumber = null;
        for (int pageNumber : testPages) {
            if (lastNumber == (Integer) pageNumber) {
                continue;
            }
            if (getDataScroller().hasPages()) {
                getDataScroller().switchTo(pageNumber);
            }
            verifyAfterScrolling();
            lastNumber = pageNumber;
        }

    }

    protected void verifyAfterScrolling() {
        if (getDataScroller().hasPages()) {
            page = getDataScroller().getActivePageNumber();
            lastPage = getDataScroller().advanced().getLastVisiblePageNumber();
        } else {
            page = 1;
            lastPage = 1;
        }
        verifyGrid();
    }
}
