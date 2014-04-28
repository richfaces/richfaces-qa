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

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.testng.Assert.assertEquals;

import java.util.Collections;

import org.richfaces.model.SortOrder;
import org.richfaces.tests.metamer.bean.rich.RichColumnBean;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.metamer.model.Capital;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractColumnSortingTest extends AbstractColumnTest {

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    protected SortOrder sortOrder;

    public void checkSortingWithSortOrder() {
        columnAttributes.setRequestType(WaitRequestType.XHR);
        columnAttributes.set(ColumnAttributes.sortOrder, sortOrder);

        switch (sortOrder) {
            case ascending:
                Collections.sort(capitals, RichColumnBean.STATE_NAME_LENGTH_COMPARATOR);
                break;
            case descending:
                Collections.sort(capitals, Collections.reverseOrder(RichColumnBean.STATE_NAME_LENGTH_COMPARATOR));
                break;
            default:
                break;
        }
        Capital actualCapital, expectedCapital;
        for (int i = 0; i < capitals.size(); i++) {
            actualCapital = getTable().getRow(i);
            expectedCapital = capitals.get(i);

            assertEquals(actualCapital.getName(), expectedCapital.getName());
            assertEquals(actualCapital.getState(), expectedCapital.getState());
        }
    }
}
