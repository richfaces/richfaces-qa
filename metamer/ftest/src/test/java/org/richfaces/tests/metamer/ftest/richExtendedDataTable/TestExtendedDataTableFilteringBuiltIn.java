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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.abstractions.DataTableFilteringTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment.FilteringEDT;
import org.testng.annotations.Test;

public class TestExtendedDataTableFilteringBuiltIn extends DataTableFilteringTest {

    private final Action ajaxAction = new Action() {
        @Override
        public void perform() {
            table.getHeader().filterNameBuiltIn("a");
        }
    };

    @FindByJQuery("div.rf-edt[id$=richEDT]")
    private FilteringEDT table;

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/builtInFilteringAndSorting.xhtml";
    }

    @Override
    protected FilteringEDT getTable() {
        return table;
    }

    @Test
    @CoversAttributes("filterVar")
    public void testCombination() {
        super.testFilterCombinations(true);
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(ajaxAction);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testFilterName() {
        super.testFilterName(true);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14150")
    public void testFilterNameBuiltInAppliesAfterEnterPressed() {
        super.testFilterNameBuiltInAppliesAfterEnterPressed();
    }

    @Test
    @CoversAttributes("filterVar")
    public void testFilterTitle() {
        super.testFilterTitle(true);
    }

    @Test
    @CoversAttributes("filterVar")
    public void testNumberOfKids() {
        super.testFilterNumberOfKindsBuiltIn();
    }

    @Test
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        testFireEvent("onbeforedomupdate", ajaxAction);
    }

    @Test
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        testFireEvent("oncomplete", ajaxAction);
    }

    @Test
    @CoversAttributes("onready")
    public void testOnready() {
        // after page re-render
        testFireEvent("onready", new Action() {
            @Override
            public void perform() {
                getMetamerPage().rerenderAll();
            }
        });
        // after full page refresh
        testFireEvent("onready", new Action() {
            @Override
            public void perform() {
                getMetamerPage().fullPageRefresh();
            }
        });
        // after ajax request
        testFireEvent("onready", ajaxAction);
    }
}
