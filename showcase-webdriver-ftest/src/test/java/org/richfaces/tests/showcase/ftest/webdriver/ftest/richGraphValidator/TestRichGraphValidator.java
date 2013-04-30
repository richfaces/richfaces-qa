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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richGraphValidator;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import static org.testng.Assert.assertTrue;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richGraphValidator.PasswordValidationPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichGraphValidator extends AbstractWebDriverTest {

    @Page
    private PasswordValidationPage page;

    @Test(groups = {"RF-12146"})
    public void testConfirmation() {
        getPage().getPassword().click();
        getPage().getPassword().sendKeys("12345");
        getPage().getConfirmation().click();
        getPage().getConfirmation().sendKeys("1234a");
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After submitting a form wrong confirmation, validation message should be present.")
            .until(Graphene.element(getPage().getGraphValidatorMessageArea()).isPresent());
        assertTrue(getPage().getGraphValidatorMessageArea().getText().contains("Different passwords entered!"));
        getPage().getConfirmation().click();
        getPage().getConfirmation().clear();
        getPage().getConfirmation().sendKeys("12345");
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After submitting a form with correctt value, sucess message should be present.")
            .until(Graphene.element(getPage().getInfoMessageArea()).isPresent());
        assertTrue(getPage().getInfoMessageArea().getText().contains("Successfully changed!"));
    }

    @Test(groups = {"RF-12146"})
    public void testLongPassword() {
        getPage().getPassword().click();
        getPage().getPassword().sendKeys("12344567890abcdef");
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After submitting a form with long password, validation message should be present.")
            .until(Graphene.element(getPage().getValidatorMessageArea()).isPresent());
        assertTrue(getPage().getValidatorMessageArea().getText().contains("between 5 and 15 characters"));
    }

    @Test(groups = {"RF-12146"})
    public void testShortPassword() {
        getPage().getPassword().click();
        getPage().getPassword().sendKeys("1234");
        getPage().getSubmit().click();
        Graphene.waitAjax()
            .withMessage("After submitting a form with short password, validation message should be present.")
            .until(Graphene.element(getPage().getValidatorMessageArea()).isPresent());
        assertTrue(getPage().getValidatorMessageArea().getText().contains("between 5 and 15 characters"));
    }

    @Override
    protected PasswordValidationPage getPage() {
        return page;
    }

}
