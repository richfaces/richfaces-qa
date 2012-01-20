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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richInplaceInput;

import static org.testng.Assert.*;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richInplaceInput.InplaceInputPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichInplaceInput extends AbstractWebDriverTest<InplaceInputPage> {

    @Test
    public void testInit() {
        assertFalse(getPage().isEmailInputFocused());
        assertFalse(getPage().isNameInputFocused());
        assertTrue(getPage().getEmailInput().getText().contains("click to enter your email"));
        assertTrue(getPage().getNameInput().getText().contains("click to enter your name"));
    }

    @Test
    public void testFocus() {
       getPage().getEmailInput().click();
       getPage().waitUntilFocused(getPage().getEmailInput());
       assertFalse(getPage().isNameInputFocused());
       getPage().getNameInput().click();
       getPage().waitUntilFocused(getPage().getNameInput());
    }

    @Test
    public void testTypeSomething() {
        getPage().getEmailInput().click();
        getPage().getEmailInput().sendKeys("Hello");
        getPage().getNameInput().click();
        assertTrue(getPage().getEmailInput().getText().contains("Hello"));
    }

    @Override
    protected InplaceInputPage createPage() {
        return new InplaceInputPage(getWebDriver());
    }

}
