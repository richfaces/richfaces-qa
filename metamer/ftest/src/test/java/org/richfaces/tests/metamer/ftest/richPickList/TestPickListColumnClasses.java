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
package org.richfaces.tests.metamer.ftest.richPickList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.text.MessageFormat;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.list.AbstractListComponent;
import org.richfaces.fragment.pickList.RichFacesPickList;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractColumnClassesTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.utils.LazyLoadedCachedValue;
import org.testng.annotations.Test;

import com.google.common.collect.BoundType;
import com.google.common.collect.Ranges;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickListColumnClasses extends AbstractColumnClassesTest {

    @FindBy(css = "[id$=pickList]")
    private PickListWithColumns pickList;

    private final TableAdapter tableAdapter = new PickListAdapter();

    @Override
    public TableAdapter getAdaptedComponent() {
        return tableAdapter;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/moreColumns.xhtml");
    }

    @Override
    public void performAfterSettingOfAttributes() {
        // move some of the items to target list, so the @columnClasses can be also checked there
        pickList.addMultiple(ChoicePickerHelper.byIndex().fromRange(Ranges.range(0, BoundType.CLOSED, pickList.advanced().getSourceListItemsElements().size() / 2, BoundType.CLOSED)));
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

    public static class PickListWithColumns extends RichFacesPickList {

        private final AdvancedPickListWithColumnsInteractions advanced = new AdvancedPickListWithColumnsInteractions();

        @FindBy(css = "[id$='SourceItems']")
        private SelectableListWithColumns sourceList;
        @FindBy(css = "[id$='TargetItems']")
        private SelectableListWithColumns targetList;

        @Override
        public AdvancedPickListWithColumnsInteractions advanced() {
            return advanced;
        }

        public static class SelectableListItemWithColumns extends SelectableListItemImpl {

            public WebElement getColumn(int n) {
                return getRootElement().findElement(ByJQuery.selector(MessageFormat.format("td:eq({0})", n)));
            }

        }

        public static class SelectableListWithColumns extends AbstractListComponent<SelectableListItemWithColumns> {
        }

        public class AdvancedPickListWithColumnsInteractions extends AdvancedPickListInteractions {

            @Override
            public SelectableListWithColumns getSourceList() {
                return sourceList;
            }

            @Override
            public SelectableListWithColumns getTargetList() {
                return targetList;
            }
        }
    }

    private class PickListAdapter implements TableAdapter {

        private final LazyLoadedCachedValue<Integer> visibleRows = new LazyLoadedCachedValue<Integer>() {

            @Override
            protected Integer initValue() {
                // some of the rows are in target list
                return pickList.advanced().getSourceList().size() + pickList.advanced().getTargetList().size();
            }

        };
        private final LazyLoadedCachedValue<Integer> visibleSourceListRows = new LazyLoadedCachedValue<Integer>() {

            @Override
            protected Integer initValue() {
                return pickList.advanced().getSourceList().size();
            }

        };

        @Override
        public WebElement getColumnWithData(int r, int c) {
            // some of the rows are in target list
            return r < visibleSourceListRows.getValue()
                ? pickList.advanced().getSourceList().getItem(r).getColumn(c)
                : pickList.advanced().getTargetList().getItem(r - visibleSourceListRows.getValue()).getColumn(c);
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
