/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.bean.rich.RichColumnBean;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22970 $
 */
public class TestColumnFiltering extends AbstractColumnModelTest {

    private static final String STATE_NAME = "Maryland";
    JQueryLocator stateNameToFilter = pjq("input:text[id$=stateNameToFilter]");
    JQueryLocator tableWithFilter = pjq("table.rf-dt[id$=richDataTable1]");
    JQueryLocator tableWithFilterExpression = pjq("table.rf-dt[id$=richDataTable2]");
    RichColumnBean richColumnBean = new RichColumnBean();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richColumn/filtering.xhtml");
    }

    @Test
    public void testFilterAttribute() {
        model.setRoot(tableWithFilter);
        testFiltering();
    }

    @Test
    public void testFilterExpressionAttribute() {
        model.setRoot(tableWithFilterExpression);
        testFiltering();
    }

    public void testFiltering() {
        for (int i = 1; i <= STATE_NAME.length(); i++) {
            String namePart = STATE_NAME.substring(0, i);

            selenium.type(stateNameToFilter, namePart);
            guardXhr(selenium).fireEvent(stateNameToFilter, Event.KEYUP);

            Collection<Capital> actualCapitals = model.getCapitals();

            richColumnBean.setStateNameToFilter(namePart);
            Collection<Capital> expectedCapitals = Collections2.filter(capitals, new Predicate<Capital>() {

                @Override
                public boolean apply(Capital capital) {
                    return richColumnBean.getStateNameFilter().accept(capital);
                }
            });

            assertEqualsCapitals(actualCapitals, expectedCapitals);
        }
    }

    private void assertEqualsCapitals(Collection<Capital> actualCapitals, Collection<Capital> expectedCapitals) {
        assertEquals(actualCapitals.size(), expectedCapitals.size());

        Iterator<Capital> actualIterator = actualCapitals.iterator();
        Iterator<Capital> expectedIterator = expectedCapitals.iterator();

        while (actualIterator.hasNext()) {
            Capital actual = actualIterator.next();
            Capital expected = expectedIterator.next();

            assertEquals(actual.getName(), expected.getName());
            assertEquals(actual.getState(), actual.getState());
        }
    }
}
