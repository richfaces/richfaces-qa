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
    private WebElement preDefinedGroupsLink;
    @FindBy(css = ".rf-tr[id$='PreDefinedTree']")
    private CustomTree preDefinedGroupsTree;
    @FindByJQuery("td:has(> a:contains('Public album groups')) + td > span > a")
    private WebElement preDefinedGroupsHelpLink;
    @FindByJQuery("a:contains('My album groups')")
    private WebElement myGroupsLink;
    @FindBy(css = ".rf-tr[id$='userTree']")
    private CustomTree myGroupsTree;
    @FindByJQuery("td:has(> a:contains('My album groups')) + td > span > a")
    private WebElement myGroupsHelpLink;

    public void checkIfUserLogged() {
        checkVisibleForAll();
        PhotoalbumUtils.checkVisible(Lists.newArrayList(myGroupsHelpLink, myGroupsLink, myGroupsTree.advanced().getRootElement()));
    }

    public void checkIfUserNotLogged() {
        checkVisibleForAll();
        PhotoalbumUtils.checkNotVisible(Lists.newArrayList(myGroupsHelpLink, myGroupsLink, myGroupsTree.advanced().getRootElement()));
    }

    private void checkVisibleForAll() {
        PhotoalbumUtils.checkVisible(Lists.newArrayList(preDefinedGroupsHelpLink, preDefinedGroupsLink, preDefinedGroupsTree.advanced().getRootElement()));
    }

    public WebElement getMyGroupsHelpLink() {
        return myGroupsHelpLink;
    }

    public WebElement getMyGroupsLink() {
        return myGroupsLink;
    }

    public RichFacesTree getMyGroupsTree() {
        return myGroupsTree;
    }

    public WebElement getPreDefinedGroupsHelpLink() {
        return preDefinedGroupsHelpLink;
    }

    public WebElement getPreDefinedGroupsLink() {
        return preDefinedGroupsLink;
    }

    public RichFacesTree getPreDefinedGroupsTree() {
        return preDefinedGroupsTree;
    }

    public void openAlbumInOwnGroup(String albumName, String shelfName) {
        Graphene.guardAjax(Graphene.guardNoRequest(myGroupsTree).expandNode(ChoicePickerHelper.byVisibleText().contains(shelfName))).selectNode(ChoicePickerHelper.byVisibleText().contains(albumName));
    }

    public void openAlbumInPredefinedGroup(String albumName, String shelfName) {
        Graphene.guardAjax(Graphene.guardNoRequest(preDefinedGroupsTree).expandNode(ChoicePickerHelper.byVisibleText().contains(shelfName))).selectNode(ChoicePickerHelper.byVisibleText().contains(albumName));
    }

    public void openOwnGroup(String name) {
        Graphene.guardAjax(myGroupsTree).selectNode(ChoicePickerHelper.byVisibleText().contains(name));
    }

    public void openOwnGroup(int index) {
        Graphene.guardAjax(myGroupsTree).selectNode(index);
    }

    public void openOwnGroups() {
        Graphene.guardAjax(myGroupsLink).click();
    }

    public void openPredefinedGroups(String name) {
        Graphene.guardAjax(preDefinedGroupsTree).selectNode(ChoicePickerHelper.byVisibleText().contains(name));
    }

    public void openPredefinedGroup(int index) {
        Graphene.guardAjax(preDefinedGroupsTree).selectNode(index);
    }

    public void openPredefinedGroup() {
        Graphene.guardAjax(preDefinedGroupsLink).click();
    }
}
