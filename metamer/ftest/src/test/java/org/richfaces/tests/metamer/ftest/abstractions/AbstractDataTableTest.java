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
package org.richfaces.tests.metamer.ftest.abstractions;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.metamer.model.Employee;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision: 22688 $
 */
public abstract class AbstractDataTableTest extends AbstractWebDriverTest {
    protected static final List<Capital> CAPITALS = Model.unmarshallCapitals();
    protected static final List<Employee> EMPLOYEES = Model.unmarshallEmployees();
    protected static final int ELEMENTS_TOTAL = 50;

    protected static final int COLUMN_STATE = 0;
    protected static final int COLUMN_CAPITAL = 1;

    protected static final int COLUMN_SEX = 0;
    protected static final int COLUMN_NAME = 1;
    protected static final int COLUMN_TITLE = 2;
    protected static final int COLUMN_NUMBER_OF_KIDS1 = 3;
    protected static final int COLUMN_NUMBER_OF_KIDS2 = 4;

    protected static final Integer[] COUNTS = new Integer[] { 1, 3, 11, ELEMENTS_TOTAL / 2, ELEMENTS_TOTAL - 1, ELEMENTS_TOTAL,
            ELEMENTS_TOTAL + 1 };

    @FindByJQuery("input[id$=noDataCheckbox]")
    protected WebElement showDataLocator;

    @FindByJQuery("span.rf-ds[id$=scroller1]")
    protected RichFacesDataScroller dataScroller1;

    @FindByJQuery("span.rf-ds[id$=scroller2]")
    protected RichFacesDataScroller dataScroller2;

    protected void enableShowData(final boolean showData) {
        if(showData) {
            if (!showDataLocator.isEnabled()) {
                Graphene.guardAjax(showDataLocator).click();
            }
        } else {
            if(showDataLocator.isEnabled()) {
                Graphene.guardAjax(showDataLocator).click();
            }
        }
    }
}