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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.text.MessageFormat;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.orderingList.RichFacesOrderingList;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractColumnClassesTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.utils.LazyLoadedCachedValue;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestOrderingListColumnClasses extends AbstractColumnClassesTest {

    @FindBy(css = "div[id$=orderingList]")
    private OrderingListWithColumns orderingList;

    private final TableAdapter tableAdapter = new OrderingListAdapter();

    public TableAdapter getAdaptedComponent() {
        return tableAdapter;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/moreColumns.xhtml");
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_commaSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesEqualsToColumns_spaceSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesGreaterThanColumns_commaSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_commaSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated() {
        super.testColumnClasses_numberOfColumnClassesLesserThanColumns_spaceSeparated();
    }

    @Test
    @Templates(value = "plain")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13721")
    public void testColumnClasses_oneColumnClass() {
        super.testColumnClasses_oneColumnClass();
    }

    public static class OrderingListWithColumns extends RichFacesOrderingList {

        private final AdvancedRichOrderingListWithColumnsInteractions advanced = new AdvancedRichOrderingListWithColumnsInteractions();

        @FindBy(css = "div.rf-ord-lst-scrl [id$=Items]")
        private SelectableListWithColumns list;

        @Override
        public AdvancedRichOrderingListWithColumnsInteractions advanced() {
            return advanced;
        }

        public static class SelectableListItemWithColumns extends SelectableListItemImpl {

            public WebElement getColumn(int n) {
                return getRootElement().findElement(ByJQuery.selector(MessageFormat.format("td:eq({0})", n)));
            }
        }

        public static class SelectableListWithColumns extends AbstractListComponent<SelectableListItemWithColumns> {
        }

        public class AdvancedRichOrderingListWithColumnsInteractions extends AdvancedRichOrderingListInteractions {

            @Override
            public SelectableListWithColumns getList() {
                return list;
            }
        }
    }

    private class OrderingListAdapter implements TableAdapter {

        private final LazyLoadedCachedValue<Integer> visibleRows = new LazyLoadedCachedValue<Integer>() {

            @Override
            protected Integer initValue() {
                return orderingList.advanced().getItemsElements().size();
            }

        };

        @Override
        public WebElement getColumnWithData(int r, int c) {
            return orderingList.advanced().getList().getItem(r).getColumn(c);
        }

        @Override
        public int getNumberOfColumns() {
            return 3;
        }

        @Override
        public int getNumberOfVisibleRows() {
            return visibleRows.getValue();
        }
    }
}
