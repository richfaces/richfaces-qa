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
package org.richfaces.tests.metamer.ftest.richColumn;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.tests.metamer.bean.rich.RichColumnBean;
import org.richfaces.tests.metamer.ftest.richDataTable.fragment.ColumnGroupDT;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestColumnFiltering extends AbstractColumnTest {

    @FindBy(css = ".rf-dt[id$=richDataTable]")
    private ColumnGroupDT table;

    private static final String STATE_NAME = "Maryland";
    @FindBy(css = "input[id$=stateNameToFilter]")
    private TextInputComponentImpl stateNameFilterInput;

    @FindBy(css = ".rf-dt[id$=richDataTable1]")
    private ColumnGroupDT tableWithFilter;
    @FindBy(css = ".rf-dt[id$=richDataTable2]")
    private ColumnGroupDT tableWithFilterExpression;

    private RichColumnBean richColumnBean = new RichColumnBean();

    private ColumnGroupDT actualTable;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richColumn/filtering.xhtml");
    }

    @Test
    public void testFilterAttribute() {
        actualTable = tableWithFilter;
        testFiltering();
    }

    @Test
    public void testFilterExpressionAttribute() {
        actualTable = tableWithFilterExpression;
        testFiltering();
    }

    public void testFiltering() {
        for (int i = 1; i <= STATE_NAME.length(); i++) {
            String namePart = STATE_NAME.substring(0, i);

            Graphene.guardAjax(stateNameFilterInput.clear()).sendKeys(namePart);

            List<? extends Capital> actualCapitals = actualTable.getAllRows();

            richColumnBean.setStateNameToFilter(namePart);
            List<? extends Capital> expectedCapitals = Lists.newArrayList(Iterables.filter(capitals, new Predicate<Capital>() {

                @Override
                public boolean apply(Capital capital) {
                    return richColumnBean.getStateNameFilter().accept(capital);
                }
            }));
            assertEquals(actualCapitals.size(), expectedCapitals.size(), "Number of capitals does not match.");
            for (int j = 0; j < expectedCapitals.size(); j++) {
                assertEquals(actualCapitals.get(j).toString(), expectedCapitals.get(j).toString());
            }
        }
    }

    @Override
    protected ColumnGroupDT getTable() {
       return table;
    }
}
