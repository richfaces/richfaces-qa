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
package org.richfaces.tests.photoalbum.ftest.webdriver.fragments;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.tree.RichFacesTree;
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

    @FindByJQuery("a:contains('Public album groups')")
    private WebElement preDefinedShelvesLink;
    @FindBy(css = ".rf-tr[id$='PreDefinedTree']")
    private CustomTree preDefinedShelvesTree;
    @FindByJQuery("td:has(> a:contains('Public album groups')) + td a:contains(?)")
    private WebElement preDefinedShelvesHelpLink;
    @FindByJQuery("a:contains('My album groups')")
    private WebElement myShelvesLink;
    @FindBy(css = ".rf-tr[id$='userTree']")
    private CustomTree myShelvesTree;
    @FindByJQuery("td:has(> a:contains('My album groups')) + td a:contains(?)")
    private WebElement myShelvesHelpLink;
    @FindByJQuery("a:contains('G+ Albums')")
    private WebElement gPlusShelvesLink;
    @FindByJQuery("td:has(> a:contains('G+ Albums')) + td a:contains(?)")
    private WebElement gPlusShelvesHelpLink;
    @FindByJQuery("a:contains('Facebook Albums')")
    private WebElement fbShelvesLink;

    public void checkIfUserLogged(boolean hasOwnAlbums, boolean hasFBAlbums, boolean hasGPlusAlbums) {
        checkVisibleForAll();
        PhotoalbumUtils.checkVisible(myShelvesHelpLink, myShelvesLink);
        if (hasOwnAlbums) {
            PhotoalbumUtils.checkVisible(Lists.newArrayList(myShelvesTree.advanced().getRootElement()));
        } else {
            PhotoalbumUtils.checkNotVisible(Lists.newArrayList(myShelvesTree.advanced().getRootElement()));
        }
        if (hasFBAlbums) {
            PhotoalbumUtils.checkVisible(Lists.newArrayList(fbShelvesLink));
        } else {
            PhotoalbumUtils.checkNotVisible(Lists.newArrayList(fbShelvesLink));
        }
        if (hasGPlusAlbums) {
            PhotoalbumUtils.checkVisible(Lists.newArrayList(gPlusShelvesLink, gPlusShelvesHelpLink));
        } else {
            PhotoalbumUtils.checkNotVisible(Lists.newArrayList(gPlusShelvesLink, gPlusShelvesHelpLink));
        }
    }

    public void checkIfUserNotLogged() {
        checkVisibleForAll();
        PhotoalbumUtils.checkNotVisible(Lists.newArrayList(myShelvesHelpLink, myShelvesLink, myShelvesTree.advanced().getRootElement(), fbShelvesLink, gPlusShelvesHelpLink, gPlusShelvesLink));
    }

    private void checkVisibleForAll() {
        PhotoalbumUtils.checkVisible(Lists.newArrayList(preDefinedShelvesHelpLink, preDefinedShelvesLink, preDefinedShelvesTree.advanced().getRootElement()));
    }

    public WebElement getGPlusShelvesHelpLink() {
        return gPlusShelvesHelpLink;
    }

    public WebElement getGPlusShelvesLink() {
        return gPlusShelvesLink;
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
        PhotoalbumUtils.waitFor(2000L);
    }

    public void openAlbumInPredefinedShelf(String albumName, String shelfName) {
        Graphene.guardAjax(Graphene.guardNoRequest(preDefinedShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(shelfName))).selectNode(ChoicePickerHelper.byVisibleText().contains(albumName));
        PhotoalbumUtils.waitFor(2000L);
    }

    public void openOwnShelf(String name) {
        Graphene.guardAjax(myShelvesTree).selectNode(ChoicePickerHelper.byVisibleText().contains(name));
        PhotoalbumUtils.waitFor(2000L);
    }

    public void openOwnShelf(int index) {
        Graphene.guardAjax(myShelvesTree).selectNode(index);
        PhotoalbumUtils.waitFor(2000L);
    }

    public void openOwnShelves() {
        Graphene.guardAjax(myShelvesLink).click();
        PhotoalbumUtils.waitFor(2000L);
    }

    public void openPredefinedShelf(String name) {
        Graphene.guardAjax(preDefinedShelvesTree).selectNode(ChoicePickerHelper.byVisibleText().contains(name));
        PhotoalbumUtils.waitFor(2000L);
    }

    public void openPredefinedShelf(int index) {
        Graphene.guardAjax(preDefinedShelvesTree).selectNode(index);
        PhotoalbumUtils.waitFor(2000L);
    }

    public void openPredefinedShelves() {
        Graphene.guardAjax(preDefinedShelvesLink).click();
        PhotoalbumUtils.waitFor(2000L);
    }
}
