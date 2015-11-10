/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richColumnGroup;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractColumnAndRowClassesTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
public class TestRF13259 extends AbstractColumnAndRowClassesTest {

    private final TableAdapter adapter = new Adapter();

    @FindBy(css = "table[id$=table] tr")
    private List<WebElement> tableRows;

    @Override
    public TableAdapter getAdaptedComponent() {
        return adapter;
    }

    @Override
    public String getComponentTestPagePath() {
        return "richColumnGroup/RF-13259-2.xhtml";
    }

    @Test
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated();
    }

    @Test
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated();
    }

    @Test
    public void testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated();
    }

    @Test
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated();
    }

    @Test
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated();
    }

    @Test
    public void testColumnClasses_oneColumnClass() {
        super.testColumnClasses_oneColumnClass();
    }

    @Test
    public void testRowClasses_numberOfRowClassesEqualsToRows_commaSeparated() {
        super.testRowClasses_numberOfRowClassesEqualsToRows_commaSeparated();
    }

    @Test
    public void testRowClasses_numberOfRowClassesEqualsToRows_spaceSeparated() {
        super.testRowClasses_numberOfRowClassesEqualsToRows_spaceSeparated();
    }

    @Test
    public void testRowClasses_numberOfRowClassesGreaterThanRows_commaSeparated() {
        super.testRowClasses_numberOfRowClassesGreaterThanRows_commaSeparated();
    }

    @Test
    public void testRowClasses_numberOfRowClassesLesserThanRows_commaSeparated() {
        super.testRowClasses_numberOfRowClassesLesserThanRows_commaSeparated();
    }

    @Test
    public void testRowClasses_numberOfRowClassesLesserThanRows_spaceSeparated() {
        super.testRowClasses_numberOfRowClassesLesserThanRows_spaceSeparated();
    }

    @Test
    public void testRowClasses_oneRowClass() {
        super.testRowClasses_oneRowClass();
    }

    private class Adapter implements TableAdapter {

        private static final String CELL_CLASS = "rf-dt-c";
        private static final int DEFAULT_COLUMNS_COUNT = 3;
        private static final int DEFAULT_ROWS_COUNT = 10;

        @Override
        public WebElement getCellWithData(int r, int c) {
            return getRowWithData(r).findElements(By.className(CELL_CLASS)).get(c);
        }

        @Override
        public int getNumberOfColumns() {
            return DEFAULT_COLUMNS_COUNT;
        }

        @Override
        public int getNumberOfVisibleRows() {
            return DEFAULT_ROWS_COUNT;
        }

        @Override
        public WebElement getRowWithData(int r) {
            return tableRows.get(r);
        }
    }
}
