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
package org.richfaces.tests.metamer.ftest.richFocus;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.test.selenium.support.ui.ElementIsFocused;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.AttributeList;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestSimple extends AbstractWebDriverTest {

    @Page
    private FocusSimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFocus/simple.xhtml");
    }

    @Test
    public void testFocusOnFirstInputAfterLoad() {
        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getNameInput().getStringValue();
        assertEquals(actual, AbstractFocusPage.EXPECTED_STRING,
            "The first input (with label name) was not focused after page load!");
    }

    @Test
    public void testAjaxRenderedFalse() {
        AttributeList.focusAttributes.set(FocusAttributes.ajaxRendered, false);

        page.ajaxValidateInputs();

        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getNameInput().getStringValue();
        assertEquals(actual.trim(), "", "The input should be empty! Because no inputs should have focus!");
    }

    @Test
    public void testValidationAwareTrue() {
        page.getNameInput().sendKeys("Robert");
        page.getAgeInput().sendKeys("38");

        page.ajaxValidateInputs();
        waitModel().until(new ElementIsFocused(page.getAddressInput().advanced().getInput()));

        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getAddressInput().getStringValue();
        assertEquals(actual, AbstractFocusPage.EXPECTED_STRING,
            "The address input should be focused! Since validationAware is true and that input is incorrect!");
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testValidationAwareFalse() {
        // richPopupPanel is disabled because in place where following attribute is to be set the popup
        // window appears, therefore making it unclickable
        AttributeList.focusAttributes.set(FocusAttributes.validationAware, false);

        page.getNameInput().sendKeys("Robert");
        page.getAgeInput().sendKeys("38");

        page.ajaxValidateInputs();
        waitModel().until(new ElementIsFocused(page.getNameInput().advanced().getInput()));

        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getNameInput().getStringValue();
        assertTrue(actual.contains(AbstractFocusPage.EXPECTED_STRING), "The name input should contain string "
            + AbstractFocusPage.EXPECTED_STRING + ", because validationAware is false!");
    }
}
