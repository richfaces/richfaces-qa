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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.test.selenium.support.ui.ElementIsFocused;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestDelayed extends AbstractWebDriverTest {

    @Page
    private FocusDelayedPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richFocus/delayed.xhtml");
    }

    @Test
    @Templates(exclude = "richPopupPanel")
    public void testDelayed() {
        page.getNextButton().click();

        waitModel().until(new ElementIsFocused(page.getNameInput().advanced().getInputElement()));
        page.typeStringAndDoNotCareAboutFocus();

        String actual = page.getNameInput().getStringValue();
        assertEquals(actual, AbstractFocusPage.EXPECTED_STRING, "The name input should be focused!");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12823")
    @Templates(value = "richPopupPanel")
    public void testDelayedInPopup() {
        testDelayed();
    }
}
