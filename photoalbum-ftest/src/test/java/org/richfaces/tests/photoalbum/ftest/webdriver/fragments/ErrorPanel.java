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
package org.richfaces.tests.photoalbum.ftest.webdriver.fragments;

import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.panel.TextualFragmentPart;
import org.richfaces.tests.page.fragments.impl.popupPanel.RichFacesPopupPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.ErrorPanel.Body;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.HowItWorksPopupPanel.Controls;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ErrorPanel extends RichFacesPopupPanel<TextualFragmentPart, Controls, Body> {

    public void checkAll(String contentStartsWith) {
        checkHeader();
        checkContent(contentStartsWith);
        checkContainsHelp();
        checkContainsWarning();
        checkCloseWithControls();
    }

    public void checkCloseWithControls() {
        Graphene.guardNoRequest(this).close();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void checkContainsHelp() {
        assertTrue(getBodyContent().getHelpLink().getAttribute("src").contains("img/icons/help_sign.png"));
    }

    public void checkContainsWarning() {
        assertTrue(getBodyContent().getIcon().getAttribute("src").contains("img/modal/ico_warning.png"));
    }

    public void checkContent(String contentContains) {
        assertTrue(getContentText().contains(contentContains));
    }

    public void checkHeader() {
        assertTrue(getHeaderText().contains("Error occurred"));
    }

    public void close() {
        getHeaderControlsContent().close();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public String getContentText() {
        return getBodyContent().getText();
    }

    public String getHeaderText() {
        return getHeaderContent().getText();
    }

    public static class Body extends TextualFragmentPart {

        @FindByJQuery("img:eq(1)")
        private WebElement helpLink;
        @FindByJQuery("img:eq(0)")
        private WebElement icon;

        public WebElement getHelpLink() {
            return helpLink;
        }

        public WebElement getIcon() {
            return icon;
        }
    }
}
