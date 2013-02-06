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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richInputNumberSpinner;

import static org.testng.Assert.assertEquals;

import org.jboss.test.selenium.android.ToolKitException;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richInputNumberSpinner.Spinner;
import org.richfaces.tests.showcase.ftest.webdriver.page.richInputNumberSpinner.SpinnersPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichInputNumberSpinner extends AbstractWebDriverTest<SpinnersPage>{

    @Test
    public void testFirstDecreaseAndIncrease() {
        testDecreaseAndIncrease(getPage().getFirstSpinner(), getPage().getSecondSpinner());
    }

    @Test
    public void testSecondDecreaseAndIncrease() {
        testDecreaseAndIncrease(getPage().getSecondSpinner(), getPage().getFirstSpinner());
    }

    @Test(enabled = false)
    // Fails beacuse https://issues.jboss.org/browse/RF-11628
    public void testFirstSetAndDecrease() throws ToolKitException {
        testSetAndDecrease(getPage().getFirstSpinner(), getPage().getSecondSpinner());
    }

    @Test(enabled = false)
    // Fails beacuse https://issues.jboss.org/browse/RF-11628
    public void testSecondSetAndDecrease() throws ToolKitException {
        testSetAndDecrease(getPage().getSecondSpinner(), getPage().getFirstSpinner());
    }

    @Test(enabled = false)
    // Fails beacuse https://issues.jboss.org/browse/RF-11628
    public void testFirstSetAndIncrease() throws ToolKitException {
        testSetAndIncrease(getPage().getFirstSpinner(), getPage().getSecondSpinner());
    }

    @Test(enabled = false)
    // Fails beacuse https://issues.jboss.org/browse/RF-11628
    public void testSecondSetAndIncrease() throws ToolKitException {
        testSetAndIncrease(getPage().getSecondSpinner(), getPage().getFirstSpinner());
    }

    @Test
    public void testInit() {
        assertEquals(getPage().getFirstSpinner().getNumber(), 50);
        assertEquals(getPage().getSecondSpinner().getNumber(), 50);
    }

    @Override
    protected SpinnersPage createPage() {
        return new SpinnersPage(getWebDriver());
    }

    private void testDecreaseAndIncrease(Spinner toTest, Spinner toStay) {
        toTest.decrease();
        assertEquals(toStay.getNumber(), 50);
        toTest.increase();
        assertEquals(toStay.getNumber(), 50);
    }

    private void testSetAndIncrease(Spinner toTest, Spinner toStay) throws ToolKitException {
        toTest.setNumber(23);
        assertEquals(toTest.getNumber(), 23);
        toTest.increase();
        assertEquals(toStay.getNumber(), 50);
    }

    private void testSetAndDecrease(Spinner toTest, Spinner toStay) throws ToolKitException {
        toTest.setNumber(23);
        assertEquals(toTest.getNumber(), 23);
        toTest.decrease();
        assertEquals(toStay.getNumber(), 50);
    }

}
