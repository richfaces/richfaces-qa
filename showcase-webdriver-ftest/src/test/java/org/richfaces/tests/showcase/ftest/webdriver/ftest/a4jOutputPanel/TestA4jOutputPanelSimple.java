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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.a4jOutputPanel;

import org.jboss.arquillian.graphene.Graphene;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.a4jOutputPanel.SimplePage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestA4jOutputPanelSimple extends AbstractWebDriverTest<SimplePage>{

    private static final String CORRECT = "aaaaaaaaaa";
    private static final String WRONG = "aaaaaaaaaaa";

    @Test(groups = { "RF-11573" })
    public void testFirstCorrectInput() {
        getPage().getFirstInput().click();
        getPage().getFirstInput().sendKeys(CORRECT);
        assertTrue(Graphene.element(getPage().getFirstOutput()).not().isPresent().apply(getWebDriver()), "After typing a correct value into the first input field no output text should be present.");
        assertTrue(Graphene.element(getPage().getFirstError()).not().isPresent().apply(getWebDriver()), "After typing a correct value into the first input field no error message text should be present.");
        getPage().getFirstInput().submit();
        Graphene.waitAjax()
            .withMessage("After typing a correct value and into the first input field and submitting  the output text should be present.")
            .until(Graphene.element(getPage().getFirstOutput()).isPresent());
        assertEquals(getPage().getFirstOutput().getText(), "Approved Text: " + CORRECT, "The output text doesn't match.");
    }

    @Test//(groups = { "RF-11573" })
    public void testFirstWrongInput() {
        getPage().getFirstInput().click();
        getPage().getFirstInput().sendKeys(WRONG);
        assertTrue(Graphene.element(getPage().getFirstOutput()).not().isPresent().apply(getWebDriver()), "After typing a wrong value into the first input field no output text should be present.");
        assertTrue(Graphene.element(getPage().getFirstError()).not().isPresent().apply(getWebDriver()), "After typing a wrong value into the first input field no error message should be present.");
        getPage().getFirstInput().submit();
        Graphene.waitAjax()
            .withMessage("After typing a wrong value and into the first input field and submitting the error message should be present.")
            .until(Graphene.element(getPage().getFirstError()).isPresent());
        assertTrue(Graphene.element(getPage().getFirstOutput()).not().isPresent().apply(getWebDriver()), "After typing a wrong value into the first input field and submitting no output text should be present.");
    }

    @Test
    public void testSecondCorrectInput() {
        getPage().getSecondInput().click();
        getPage().getSecondInput().sendKeys(CORRECT);
        Graphene.waitAjax()
            .withMessage("After typing a correct value into the second input field, the output text should be present.")
            .until(Graphene.element(getPage().getSecondOutput()).isPresent());
        assertEquals(getPage().getSecondOutput().getText(), "Approved Text: " + CORRECT, "The output text doesn't match.");
        assertTrue(Graphene.element(getPage().getSecondError()).not().isPresent().apply(getWebDriver()), "After typing a wrong value into the second input field no error message should be present.");
    }

    @Test
    public void testSecondWrongInput() throws InterruptedException {
        getPage().getSecondInput().click();
        getPage().getSecondInput().sendKeys(WRONG);
        Graphene.waitAjax()
            .withMessage("After typing a wrong value into the second input field, an error message should be present.")
            .until(Graphene.element(getPage().getSecondOutput()).isPresent());
        Thread.sleep(1000);
        assertTrue(
            Graphene.element(getPage().getSecondOutput()).not().isPresent().apply(getWebDriver())
            ||
            Graphene.element(getPage().getSecondOutput()).not().textContains("Approved Text").apply(getWebDriver()),
            "After typing a wrong value into the second input field no output text should be present.");
    }

    @Override
    protected SimplePage createPage() {
        return new SimplePage();
    }

}
