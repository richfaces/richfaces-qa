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
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.tree.RichFacesTree;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.richfaces.tests.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LeftPanel {

    public static final String MONUMENTS_PAGE = "Monuments";
    public static final String NATURE_PAGE = "Nature";
    public static final String PORTRAIT_PAGE = "Portrait";
    public static final String SPORT_AND_CARS_PAGE = "Sport & Cars";
    public static final String WATER_PAGE = "Water";

    @FindByJQuery("a:contains('Pre-defined shelves')")
    private WebElement preDefinedShelvesLink;
    @FindBy(css = ".rf-tr[id$='PreDefinedTree']")
    private CustomTree preDefinedShelvesTree;
    @FindByJQuery("td:has(> a:contains('Pre-defined shelves')) + td > a")
    private WebElement preDefinedShelvesHelpLink;
    @FindByJQuery("a:contains('My shelves')")
    private WebElement myShelvesLink;
    @FindBy(css = ".rf-tr[id$='userTree']")
    private CustomTree myShelvesTree;
    @FindByJQuery("td:has(> a:contains('My shelves')) + td > a")
    private WebElement myShelvesHelpLink;

    public void checkIfUserLogged() {
        checkVisibleForAll();
        PhotoalbumUtils.checkVisible(Lists.newArrayList(myShelvesHelpLink, myShelvesLink, myShelvesTree.advanced().getRootElement()));
    }

    public void checkIfUserNotLogged() {
        checkVisibleForAll();
        PhotoalbumUtils.checkNotVisible(Lists.newArrayList(myShelvesHelpLink, myShelvesLink, myShelvesTree.advanced().getRootElement()));
    }

    private void checkVisibleForAll() {
        PhotoalbumUtils.checkVisible(Lists.newArrayList(preDefinedShelvesHelpLink, preDefinedShelvesLink, preDefinedShelvesTree.advanced().getRootElement()));
    }

    public WebElement getMyShelvesHelpLink() {
        return myShelvesHelpLink;
    }

    public WebElement getMyShelvesLink() {
        return myShelvesLink;
    }

    public RichFacesTree getMyShelvesTree() {
        return myShelvesTree;
    }

    public WebElement getPreDefinedShelvesHelpLink() {
        return preDefinedShelvesHelpLink;
    }

    public WebElement getPreDefinedShelvesLink() {
        return preDefinedShelvesLink;
    }

    public RichFacesTree getPreDefinedShelvesTree() {
        return preDefinedShelvesTree;
    }

    public void openAlbumInOwnShelf(String albumName, String shelfName) {
        Graphene.guardAjax(Graphene.guardNoRequest(myShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(shelfName))).selectNode(ChoicePickerHelper.byVisibleText().contains(albumName));
    }

    public void openAlbumInPredefinedShelf(String albumName, String shelfName) {
        Graphene.guardAjax(Graphene.guardNoRequest(preDefinedShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(shelfName))).selectNode(ChoicePickerHelper.byVisibleText().contains(albumName));
    }

    public void openOwnShelf(String name) {
        Graphene.guardAjax(myShelvesTree).selectNode(ChoicePickerHelper.byVisibleText().contains(name));
    }

    public void openOwnShelf(int index) {
        Graphene.guardAjax(myShelvesTree).selectNode(index);
    }

    public void openOwnShelves() {
        Graphene.guardAjax(myShelvesLink).click();
    }

    public void openPredefinedShelf(String name) {
        Graphene.guardAjax(preDefinedShelvesTree).selectNode(ChoicePickerHelper.byVisibleText().contains(name));
    }

    public void openPredefinedShelf(int index) {
        Graphene.guardAjax(preDefinedShelvesTree).selectNode(index);
    }

    public void openPredefinedShelves() {
        Graphene.guardAjax(preDefinedShelvesLink).click();
    }
}
