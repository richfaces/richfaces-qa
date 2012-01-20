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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richDataGrid;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.model.Car;
import org.richfaces.tests.showcase.ftest.webdriver.page.richDataGrid.GridPage;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichDataGridSimple extends AbstractWebDriverTest<GridPage>{

    private static final Car FIRST_CAR_FIRST_PAGE = new Car("Chevrolet", "Corvette");
    private static final Car FIRST_CAR_SECOND_PAGE = new Car("Chevrolet", "Malibu");
    private static final Car FIRST_CAR_THIRD_PAGE = new Car("Chevrolet", "Tahoe");
    private static final Car LAST_CAR_FIRST_PAGE = new Car("Chevrolet", "Malibu");
    private static final Car LAST_CAR_SECOND_PAGE = new Car("Chevrolet", "Tahoe");
    private static final Car LAST_CAR_THIRD_PAGE = new Car("Ford", "Taurus");

    @Test
    public void testInit() {
        testFirstCar(FIRST_CAR_FIRST_PAGE);
        testLastCar(LAST_CAR_FIRST_PAGE);
    }

    @Test
    public void testSecondPage() {
        getPage().page(2);
        testFirstCar(FIRST_CAR_SECOND_PAGE);
        testLastCar(LAST_CAR_SECOND_PAGE);
    }

    @Test
    public void testThirdPage() {
        getPage().page(3);
        testFirstCar(FIRST_CAR_THIRD_PAGE);
        testLastCar(LAST_CAR_THIRD_PAGE);
    }

    @Override
    protected GridPage createPage() {
        return new GridPage(getWebDriver());
    }

    private void testFirstCar(Car expected) {
        assertEquals(getPage().getFirstCar(), expected, "The first car doesn't match.");
    }

    private void testLastCar(Car expected) {
        assertEquals(getPage().getLastCar(), expected, "The last car doesn't match.");
    }

}
