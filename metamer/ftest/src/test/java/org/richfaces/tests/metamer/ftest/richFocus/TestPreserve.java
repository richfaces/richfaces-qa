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

import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.test.selenium.support.ui.ElementIsFocused;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.AttributeList;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @since 4.3.0.Final
 */
public class TestPreserve extends AbstractWebDriverTest {

    @Page
    private FocusSimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFocus/preserve.xhtml");
    }

    @Test(groups = "Future")
    // false negative - should be fixed
    public void testPreserveTrueValidationAwareTrue() {
        testPreserveTrue();
    }

    @Test(groups = "Future")
    // false negative - should be fixed
    public void testPreserveTrueValidationAwareFalse() {
        AttributeList.focusAttributes.set(FocusAttributes.validationAware, false);

        testPreserveTrue();
    }

    private void testPreserveTrue() {
        page.getAgeInput().advanced().focus();
        page.getAgeInput().advanced().getInputElement().click();

        page.ajaxValidateInputs();
        waitModel().until(new ElementIsFocused(page.getAgeInput().advanced().getInputElement()));

        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getAgeInput().getStringValue();
        assertTrue(actual.contains(AbstractFocusPage.EXPECTED_STRING),
            "The age input should be focused, since the preserve is true and before form submission that input was focused!");
    }

}
