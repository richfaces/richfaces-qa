/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jActionListener;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebDriver;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * Test case for page /faces/components/a4jActionListener/all.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class TestActionListener extends AbstractWebDriverTest {

    @Page
    private ActionListenerPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jActionListener/all.xhtml");
    }

    Predicate<WebDriver> messageIsVisible = new Predicate<WebDriver>() {
        @Override
        public boolean apply(WebDriver arg0) {
            return !page.messages.isEmpty();
        }
    };

    @Test
    public void testInvokeListenerByType() {
        final String hashCodeRegExp = "@[0-9a-fA-F]{1,}$";
        final String msg = "Implementation of ActionListener created and called: "
            + "org.richfaces.tests.metamer.bean.a4j.A4JActionListenerBean$ActionListenerImpl";

        Graphene.guardAjax(page.invokeButtonType).click();
        Graphene.waitGui().until(messageIsVisible);

        assertEquals(page.messages.size(), 1, "Only one message should be displayed on the page.");
        final String output1 = page.messages.get(0).getText();
        assertEquals(output1.replaceAll(hashCodeRegExp, ""), msg, "Message after first invocation of listener by type.");

        // do the same once again

        MetamerPage.waitRequest(page.invokeButtonType, WaitRequestType.XHR).click();

        assertFalse(page.messages.get(0).getText().equals(output1), "Message should change");
        assertEquals(page.messages.size(), 1, "Only one message should be displayed on the page.");
        final String output2 = page.messages.get(0).getText();
        assertEquals(output2.replaceAll(hashCodeRegExp, ""), msg, "Message after first invocation of listener by type.");
    }

    @Test
    public void testInvokeListenerByBinding() {
        final String msg = "Bound listener called";

        Graphene.guardAjax(page.invokeButtonBinding).click();
        Graphene.waitGui().until(messageIsVisible);

        assertEquals(page.messages.size(), 1, "Only one message should be displayed on the page.");

        String output = page.messages.get(0).getText();
        assertEquals(output, msg, "Message after first invocation of listener by binding.");
    }

    @Test
    public void testInvokeListenerMethod() {
        final String msg = "Method expression listener called";

        Graphene.guardAjax(page.invokeButtonMethod).click();
        Graphene.waitGui().until(messageIsVisible);

        assertEquals(page.messages.size(), 1, "Only one message should be displayed on the page.");

        String output = page.messages.get(0).getText();
        assertEquals(output, msg, "Message after first invocation of listener method.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10585")
    public void testInvokeListenerMethodCC() {
        final String msg = "Method expression listener called from composite component";

        Graphene.guardAjax(page.invokeButtonCC).click();
        Graphene.waitGui().until(messageIsVisible);

        assertEquals(page.messages.size(), 1, "Only one message should be displayed on the page.");

        String output = page.messages.get(0).getText();
        assertEquals(output, msg, "Message after first invocation of listener method from composite component.");
    }
}
