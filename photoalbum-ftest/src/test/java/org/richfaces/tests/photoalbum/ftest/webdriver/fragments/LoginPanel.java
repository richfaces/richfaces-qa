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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.messages.RichFacesMessages;
import org.richfaces.tests.page.fragments.impl.panel.TextualFragmentPart;
import org.richfaces.tests.page.fragments.impl.popupPanel.RichFacesPopupPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.HowItWorksPopupPanel.Controls;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.LoginPanel.Body;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LoginPanel extends RichFacesPopupPanel<TextualFragmentPart, Controls, Body> {

    public void login(String user, String password) {
        loginWithoutWait(user, password);
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public void loginWithoutWait(String user, String password) {
        getBodyContent().getLoginInput().sendKeys(user);
        getBodyContent().getPasswordInput().sendKeys(password);
        Graphene.guardAjax(getBodyContent().getLoginButton()).click();
    }

    public void close() {
        getHeaderControlsContent().close();
        advanced().waitUntilPopupIsNotVisible().perform();
    }

    public static class Body {

        @FindByJQuery("td.login-table-col2:contains('Login') + td input")
        private TextInputComponentImpl loginInput;
        @FindByJQuery("td.login-table-col2:contains('Password') + td input")
        private TextInputComponentImpl passwordInput;
        @FindByJQuery("div:contains('Login') + input")
        private WebElement loginButton;
        @FindByJQuery("div:contains('Register') + input")
        private WebElement registerButton;
        @FindByJQuery(".rf-msgs")
        private RichFacesMessages messages;

        public WebElement getLoginButton() {
            return loginButton;
        }

        public TextInputComponentImpl getLoginInput() {
            return loginInput;
        }

        public RichFacesMessages getMessages() {
            return messages;
        }

        public TextInputComponentImpl getPasswordInput() {
            return passwordInput;
        }

        public WebElement getRegisterButton() {
            return registerButton;
        }
    }
}
