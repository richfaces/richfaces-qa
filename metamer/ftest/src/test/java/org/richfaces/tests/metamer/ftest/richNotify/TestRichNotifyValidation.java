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
package org.richfaces.tests.metamer.ftest.richNotify;

import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.NegationCondition;
import org.testng.annotations.Test;


/**
 * Test case for pages faces/components/notify/validation.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichNotifyValidation extends AbstractRichNotifyTest {

    private JQueryLocator number = pjq("input[id$=number]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richNotify/validation.xhtml");
    }

    @Test
    public void testAttributeMessagesGlobalOnly() {

    }

    @Test
    public void testValidationMessageAppear() {
        closeAll(notify);
        selenium.type(number, "4");
        selenium.fireEvent(number, Event.BLUR);
        waitGui
            .failWith("After typing <4> into the number field, the error should appear.")
            .until(elementPresent.locator(notifyError));
    }

    @Test
    public void testValidationMessageNotAppear() {
        closeAll(notify);
        selenium.type(number, "5");
        selenium.fireEvent(number, Event.BLUR);
        waitGui
            .failWith("After typing <5> into the number field, the error should appear.")
            .until(NegationCondition.getInstance().condition(elementPresent.locator(notifyError)));
    }

}
