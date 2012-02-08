/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richDataTable;

import static org.testng.Assert.assertEquals;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richDataTable.SimpleTablePage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichDataTableSimple extends AbstractWebDriverTest<SimpleTablePage> {

    @Test
    public void testInit() {
        assertEquals(getPage().getSanJoseHotels(), "$224.00");
        assertEquals(getPage().getSanJoseMeals(), "$65.02");
        assertEquals(getPage().getSanJoseSubtotals(), "$379.02");
        assertEquals(getPage().getSanJoseTransport(), "$90.00");

        assertEquals(getPage().getSeattleHotels(), "$218.00");
        assertEquals(getPage().getSeattleMeals(), "$131.25");
        assertEquals(getPage().getSeattleTransport(), "$72.00");
        assertEquals(getPage().getSeattleSubtotals(), "$421.25");

        assertEquals(getPage().getTotalsOfHotels(), "$442.00");
        assertEquals(getPage().getTotalsOfMeals(), "$196.27");
        assertEquals(getPage().getTotalsOfTransport(), "$162.00");
        assertEquals(getPage().getTotalsOfSubtotals(), "$800.27");
    }

    @Override
    protected SimpleTablePage createPage() {
        return new SimpleTablePage();
    }

}
