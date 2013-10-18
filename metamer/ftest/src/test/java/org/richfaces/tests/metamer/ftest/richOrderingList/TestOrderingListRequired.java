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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestOrderingListRequired extends AbstractOrderingListTest {

    private static final String DEFAULT_REQUIRED_MSG = "Validation Error: Value is required.";
    @FindBy(css = "[id$=message]")
    private RichFacesMessage message;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/required.xhtml");
    }

    @Override
    protected void submit() {
        MetamerPage.waitRequest(submitButton, MetamerPage.WaitRequestType.XHR).click();
    }

    @Test
    public void testRequired() {
        attributes.set(OrderingListAttributes.required, Boolean.FALSE);
        submit();
        assertNotVisible(message.advanced().getRootElement(), "Message should not be visible.");

        attributes.set(OrderingListAttributes.required, Boolean.TRUE);
        submit();
        assertVisible(message.advanced().getRootElement(), "Message should be visible.");
        assertTrue(message.getDetail().endsWith(DEFAULT_REQUIRED_MSG), "Detail should end with '" + DEFAULT_REQUIRED_MSG + "'.");
    }

    @Test
    public void testRequiredMessage() {
        String requiredMessage = "Custom required message.";
        attributes.set(OrderingListAttributes.required, Boolean.TRUE);
        attributes.set(OrderingListAttributes.requiredMessage, requiredMessage);
        submit();
        assertVisible(message.advanced().getRootElement(), "Message should be visible.");
        assertEquals(message.getDetail(), requiredMessage);
    }
}
