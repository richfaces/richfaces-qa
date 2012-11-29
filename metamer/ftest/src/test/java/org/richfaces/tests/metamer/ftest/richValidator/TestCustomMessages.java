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
package org.richfaces.tests.metamer.ftest.richValidator;

import java.net.URL;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestCustomMessages extends AbstractWebDriverTest {

    @Page
    private CustomMessagesPage page;

    @Inject
    @Use(strings={"hButton", "a4jButton"})
    private String button;

    @Test
    public void testMessages1() {
        page.setAllWrong1();
        submit();
        verifyMessages();
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-12511")
    public void testMessages2() {
        page.setAllWrong2();
        submit();
        verifyMessages();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richValidator/customMessages.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Validator", "Client Side Validation With Custom Validator Messages");
    }

    public void submit() {
        if (button.equals("hButton")) {
            page.submitByHButton();
        } else {
            page.submitByA4jButton();
        }
    }

    protected void verifyMessages() {
        assertEquals(page.getCustomMessageText(), CustomMessagesPage.CUSTOM_MESSAGE_TEXT, "Custom message for custom validator doesn't match.");
        assertEquals(page.getDoubleRangeMessageText(), CustomMessagesPage.CUSTOM_MESSAGE_TEXT, "Custom message for 'double range' validator doesn't match.");
        assertEquals(page.getLengthMessageText(), CustomMessagesPage.CUSTOM_MESSAGE_TEXT, "Custom message for 'length' validator doesn't match.");
        assertEquals(page.getLongRangeMessageText(), CustomMessagesPage.CUSTOM_MESSAGE_TEXT, "Custom message for 'long range' validator doesn't match.");
        assertEquals(page.getRegexpMessageText(), CustomMessagesPage.CUSTOM_MESSAGE_TEXT, "Custom message for 'regexp' validator doesn't match.");
        assertEquals(page.getRequiredMessageText(), CustomMessagesPage.CUSTOM_MESSAGE_TEXT, "Custom message for 'required' validator doesn't match.");
    }

}
