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
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
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
    @IssueTracking("https://issues.jboss.org/browse/RF-14231")
    public void testFocusManager() {
        // workaround to get the keyboard interface working, need to interact with page first
        blur(WaitRequestType.NONE);
        getMetamerPage().fullPageRefresh();
        // age input should be focused after page refresh
        Graphene.waitModel().until(new ElementIsFocused(page.getAgeInput().advanced().getInputElement()));
        page.typeStringAndDoNotCareAboutFocus();
        // check typed text
        String actual = page.getAgeInput().getStringValue();
        Assert.assertEquals(actual, AbstractFocusPage.EXPECTED_STRING,
            "Age input should be focused by focus manager from backing bean!");
    }
}
