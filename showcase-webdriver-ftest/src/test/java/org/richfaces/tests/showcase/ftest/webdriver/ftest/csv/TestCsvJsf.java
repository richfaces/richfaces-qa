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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.csv;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import static org.testng.Assert.assertTrue;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.csv.JsfValidatorsPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestCsvJsf extends AbstractWebDriverTest {

    @Page
    private JsfValidatorsPage page;

    @Test
    public void testAllWrongAndCorrect() {
        getPage().getAgeInput().click();
        getPage().getAgeInput().sendKeys("17");
        getPage().getEmailInput().click();
        getPage().getEmailInput().sendKeys("name@domain");
        getPage().getNameInput().click();
        getPage().getNameInput().sendKeys("123456789");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing invalid value into the age input field, an error message should be present.")
            .until(Graphene.element(getPage().getAgeErrorArea()).isPresent());
        Graphene.waitAjax()
            .withMessage("After typing invalid value into the e-mail input field, an error message should be present.")
            .until(Graphene.element(getPage().getEmailErrorArea()).isPresent());
        Graphene.waitAjax()
            .withMessage("After typing invalid value into the name input field, an error message should be present.")
            .until(Graphene.element(getPage().getNameErrorArea()).isPresent());
        getPage().getAgeInput().click();
        getPage().getAgeInput().clear();
        getPage().getAgeInput().sendKeys("18");
        getPage().getEmailInput().click();
        getPage().getEmailInput().clear();
        getPage().getEmailInput().sendKeys("name@domain.com");
        getPage().getNameInput().click();
        getPage().getNameInput().clear();
        getPage().getNameInput().sendKeys("12345678");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing a valid value into the age input field, no error message should be present.")
            .until(Graphene.element(getPage().getAgeErrorArea()).not().isPresent());
        Graphene.waitAjax()
            .withMessage("After typing a valid value into the e-mail input field, no error message should be present.")
            .until(Graphene.element(getPage().getEmailErrorArea()).not().isPresent());
        Graphene.waitAjax()
            .withMessage("After typing a valid value into the name input field, no error message should be present.")
            .until(Graphene.element(getPage().getNameErrorArea()).not().isPresent());
    }

    @Test
    public void testWrongNotNumberAndCorrectAge() {
        getPage().getAgeInput().click();
        getPage().getAgeInput().sendKeys("aa");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing invalid value into the age input field, an error message should be present.")
            .until(Graphene.element(getPage().getAgeErrorArea()).isPresent());
        assertTrue(getPage().getAgeErrorArea().getText().contains("must be a number"));
        getPage().getAgeInput().click();
        getPage().getAgeInput().clear();
        getPage().getAgeInput().sendKeys("18");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing a valid value into the age input field, no error message should be present.")
            .until(Graphene.element(getPage().getAgeErrorArea()).not().isPresent());
    }

    @Test
    public void testWrongAndCorrectEmail() {
        getPage().getEmailInput().click();
        getPage().getEmailInput().sendKeys("name@domain");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing invalid value into the e-mail input field, an error message should be present.")
            .until(Graphene.element(getPage().getEmailErrorArea()).isPresent());
        assertTrue(getPage().getEmailErrorArea().getText().contains("Invalid email address"));
        getPage().getEmailInput().click();
        getPage().getEmailInput().clear();
        getPage().getEmailInput().sendKeys("name@domain.com");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing a valid e-mail into the e-mail input field, no error message should be present.")
            .until(Graphene.element(getPage().getEmailErrorArea()).not().isPresent());
    }

    @Test
    public void testWrongLongAndCorrectName() {
        getPage().getNameInput().click();
        getPage().getNameInput().sendKeys("123456789");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing a long string into the name input field, an error message should be present.")
            .until(Graphene.element(getPage().getNameErrorArea()).isPresent());
        assertTrue(getPage().getNameErrorArea().getText().contains("Specified attribute is not between the expected values of 3 and 8"));
        getPage().getNameInput().click();
        getPage().getNameInput().clear();
        getPage().getNameInput().sendKeys("12345678");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing a correct string into the name input field, no error message should be present.")
            .until(Graphene.element(getPage().getNameErrorArea()).not().isPresent());
    }

    @Test
    public void testWrongHighAge() {
        getPage().getAgeInput().click();
        getPage().getAgeInput().sendKeys("100");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing invalid value into the age input field, an error message should be present.")
            .until(Graphene.element(getPage().getAgeErrorArea()).isPresent());
        assertTrue(getPage().getAgeErrorArea().getText().contains("between the expected values"));
    }

    @Test
    public void testWrongLowAge() {
        getPage().getAgeInput().click();
        getPage().getAgeInput().sendKeys("17");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing invalid value into the age input field, an error message should be present.")
            .until(Graphene.element(getPage().getAgeErrorArea()).isPresent());
        assertTrue(getPage().getAgeErrorArea().getText().contains("between the expected values"));
    }

    @Test
    public void testWrongShortName() {
        getPage().getNameInput().click();
        getPage().getNameInput().sendKeys("12");
        getPage().loseFocus();
        Graphene.waitAjax()
            .withMessage("After typing a short string into the name input field, an error message should be present.")
            .until(Graphene.element(getPage().getNameErrorArea()).isPresent());
        assertTrue(getPage().getNameErrorArea().getText().contains("Specified attribute is not between the expected values of 3 and 8"));
    }

    @Override
    protected JsfValidatorsPage getPage() {
        return page;
    }

}
