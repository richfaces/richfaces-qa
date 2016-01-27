/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richFocus;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.test.selenium.support.ui.ElementIsFocused;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestFocusManager extends AbstractWebDriverTest {

    @Page
    private FocusSimplePage page;

    @Override
    public String getComponentTestPagePath() {
        return "richFocus/focusManager.xhtml";
    }

    @Test(groups = "smoke")
    @Templates(exclude = { "richCollapsibleSubTable", "richExtendedDataTable", "richDataTable", "richDataGrid", "richList", "a4jRepeat", "uiRepeat" })
    public void testFocusManager() {
        // workaround to get the keyboard interface working, need to interact with page first
        getMetamerPage().getResponseDelayElement().click();
        getMetamerPage().fullPageRefresh();
        // age input should be focused after page refresh
        Graphene.waitModel().until(new ElementIsFocused(page.getAgeInput().advanced().getInputElement()));
        page.typeStringAndDoNotCareAboutFocus();
        // check typed text
        String actual = page.getAgeInput().getStringValue();
        Assert.assertEquals(actual, AbstractFocusPage.EXPECTED_STRING,
            "Age input should be focused by focus manager from backing bean!");
    }

    @Test(enabled = false)
    //TODO: fix sample (id problems)
    @Templates(value = { "richCollapsibleSubTable", "richExtendedDataTable", "richDataTable", "richDataGrid", "richList", "a4jRepeat", "uiRepeat" })
    public void testFocusManagerInIterationComponents() {
        testFocusManager();
    }
}
