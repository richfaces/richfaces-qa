/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Ajocado.elementPresent;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;


/**
 * Test case for page /faces/components/a4jActionListener/all.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22470 $
 */
public class TestA4JActionListener extends AbstractAjocadoTest {

    private JQueryLocator invokeButtonType = pjq("input[id$=invokeByTypeButton]");
    private JQueryLocator invokeButtonBinding = pjq("input[id$=invokeByBindingButton]");
    private JQueryLocator invokeButtonMethod = pjq("input[id$=invokeMethodButton]");
    private JQueryLocator invokeButtonCC = pjq("input[id$=invokeFromCCButton:button]");
    private JQueryLocator message = pjq("ul[id$=messages] li");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jActionListener/all.xhtml");
    }

    @Test
    public void testInvokeListenerByType() {
        final String hashCodeRegExp = "@[0-9a-fA-F]{1,}$";
        final String msg = "Implementation of ActionListener created and called: "
            + "org.richfaces.tests.metamer.bean.a4j.A4JActionListenerBean$ActionListenerImpl";

        selenium.click(invokeButtonType);
        waitGui.until(elementPresent.locator(message));
        final String output1 = selenium.getText(message);

        assertEquals(output1.replaceAll(hashCodeRegExp, ""), msg, "Message after first invocation of listener by type.");

        int count = selenium.getCount(message);
        assertEquals(count, 1, "Only one message should be displayed on the page.");

        // do the same once again

        selenium.click(invokeButtonType);

        waitModel.failWith("New object of class ActionListenerImpl should be instantiated.").until(
            new SeleniumCondition() {

                public boolean isTrue() {
                    return !output1.equals(selenium.getText(message));
                }
            });

        count = selenium.getCount(message);
        assertEquals(count, 1, "Only one message should be displayed on the page.");

    }

    @Test
    public void testInvokeListenerByBinding() {
        final String msg = "Bound listener called";

        selenium.click(invokeButtonBinding);
        waitGui.until(elementPresent.locator(message));
        String output = selenium.getText(message);

        assertEquals(output, msg, "Message after first invocation of listener by binding.");

        int count = selenium.getCount(message);
        assertEquals(count, 1, "Only one message should be displayed on the page.");
    }

    @Test
    public void testInvokeListenerMethod() {
        final String msg = "Method expression listener called";

        selenium.click(invokeButtonMethod);
        waitGui.until(elementPresent.locator(message));
        String output = selenium.getText(message);

        assertEquals(output, msg, "Message after first invocation of listener method.");

        int count = selenium.getCount(message);
        assertEquals(count, 1, "Only one message should be displayed on the page.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10585")
    public void testInvokeListenerMethodCC() {
        final String msg = "Method expression listener called from composite component";

        selenium.click(invokeButtonCC);
        waitGui.until(elementPresent.locator(message));
        String output = selenium.getText(message);

        assertEquals(output, msg, "Message after first invocation of listener method from composite component.");

        int count = selenium.getCount(message);
        assertEquals(count, 1, "Only one message should be displayed on the page.");
    }
}
