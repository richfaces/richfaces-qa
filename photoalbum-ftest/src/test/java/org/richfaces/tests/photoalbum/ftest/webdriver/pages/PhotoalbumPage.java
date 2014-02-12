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
package org.richfaces.tests.photoalbum.ftest.webdriver.pages;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.AddAlbumPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.AddShelfPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.ConfirmationPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.ContentPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.ErrorPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.FooterPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.HeaderPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.LeftPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.LoginPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.SearchPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.SlideShowPanel;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class PhotoalbumPage {

    /**
     * TODO: there are two messages components on the top of the page
     */
    @FindBy(className = "rf-msgs")
    private RichFacesMessages messages;
    @FindBy(css = ".rf-p.header-panel")
    private HeaderPanel headerPanel;
    @FindBy(css = "div[id$='treePanel']")
    private LeftPanel leftPanel;
    @FindBy(tagName = "body")
    private SearchPanel searchPanel;
    @FindBy(css = ".content_box")
    private ContentPanel contentPanel;
    @FindBy(css = ".rf-p.footer-panel")
    private FooterPanel footerPanel;

    @FindBy(css = ".rf-pp-cntr.login-panel")
    private LoginPanel loginPanel;
    @FindBy(css = ".rf-pp-cntr[id*='errorPanel']")
    private ErrorPanel errorPanel;
    @FindBy(css = ".rf-pp-cntr[id*='slideShow']")
    private SlideShowPanel slideShowPanel;
    @FindBy(css = ".rf-pp-cntr[id*='shelfModalPanel']")
    private AddShelfPanel addShelfPanel;
    @FindBy(css = ".rf-pp-cntr[id*='albumModalPanel']")
    private AddAlbumPanel addAlbumPanel;
    @FindBy(css = ".rf-pp-cntr[id*='confirmation']")
    private ConfirmationPanel confirmationPanel;

    public void checkLogged(String user) {
        headerPanel.checkIfUserLogged(user);
        leftPanel.checkIfUserLogged();
        footerPanel.check();
    }

    public void checkNotLogged() {
        headerPanel.checkIfUserNotLogged();
        leftPanel.checkIfUserNotLogged();
        footerPanel.check();
    }

    public AddAlbumPanel getAddAlbumPanel() {
        return addAlbumPanel;
    }

    public AddShelfPanel getAddShelfPanel() {
        return addShelfPanel;
    }

    public ConfirmationPanel getConfirmationPanel() {
        return confirmationPanel;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public ErrorPanel getErrorPanel() {
        return errorPanel;
    }

    public FooterPanel getFooterPanel() {
        return footerPanel;
    }

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public RichFacesMessages getMessages() {
        return messages;
    }

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public SlideShowPanel getSlideShowPanel() {
        return slideShowPanel;
    }

    public void login(String user, String pswd) {
        Graphene.guardAjax(headerPanel.getLoginLink()).click();
        loginPanel.advanced().waitUntilPopupIsVisible().perform();
        loginPanel.login(user, pswd);
    }

    public void logout() {
        Graphene.guardAjax(headerPanel.getLogoutLink()).click();
    }
}
