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
package org.richfaces.tests.photoalbum.ftest.webdriver.tests;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.fragment.tree.Tree;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AlbumGroupsView;
import org.richfaces.tests.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestLeftPanelNavigations extends AbstractPhotoalbumTest {

    private static final List<String> PREDEFINED_SHELVES = Lists.newArrayList("Monuments", "Nature", "Portrait", "Sport & Cars", "Water");
    private static final List<String> CUSTOM_SHELVES = Lists.newArrayList("Nature", "Sport & Cars");

    @Test
    public void testInitialState() {
        assertEquals(PhotoalbumUtils.getStringsFromElements(page.getLeftPanel().getPreDefinedGroupsTree().advanced().getNodesElements()), PREDEFINED_SHELVES);
    }

    @Test
    public void testInitialStateLoggedUser() {
        login();
        assertEquals(PhotoalbumUtils.getStringsFromElements(page.getLeftPanel().getPreDefinedGroupsTree().advanced().getNodesElements()), PREDEFINED_SHELVES);
        assertEquals(PhotoalbumUtils.getStringsFromElements(page.getLeftPanel().getMyGroupsTree().advanced().getNodesElements()), CUSTOM_SHELVES);
    }

    @Test
    public void testOwnAlbumsView() throws InterruptedException {
        login();

        AlbumView album = page.getContentPanel().albumView();
        // check Nature shelf
        Tree.TreeNode node = Graphene.guardNoRequest(page.getLeftPanel().getMyGroupsTree()).expandNode(0);
        assertEquals(node.advanced().getNodes().size(), 2);
        // open Animals album
        Graphene.guardAjax(node).selectNode(0);
        waitAjax().until().element(album.getAlbumHeader().getRoot()).is().visible();
        album.checkAll("Animals", "Created 2009-12-18, contains 6 images", "Animals pictures", "Album group: Nature");
        assertEquals(album.getPhotos().size(), 6);
        album.getPhotos().get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(1).checkAll(120, "1906662004_655d0f6ccf_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(4).checkAll(120, "9855284863_da027be6cf_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(5).checkAll(120, "5395800621_c0bc80ca53_o.jpg", "Dec 17, 2009");

        // check Sport & Cars shelf
        node = Graphene.guardNoRequest(page.getLeftPanel().getMyGroupsTree()).expandNode(1);
        assertEquals(node.advanced().getNodes().size(), 2);
        // open Sport album
        Graphene.guardAjax(node).selectNode(1);
        waitAjax().until().element(album.getAlbumInfo()).text().contains("Sport pictures");
        album.checkAll("Sport", "Created 2009-12-18, contains 3 images", "Sport pictures", "Album group: Sport & Cars");
        assertEquals(album.getPhotos().size(), 3);
        album.getPhotos().get(0).checkAll(120, "103193233_860c47c909_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(1).checkAll(120, "1350250361_2d963dd4e7_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(2).checkAll(120, "2042654579_d25c0db64f_o.jpg", "Dec 17, 2009");
    }

    @Test
    public void testPredefinedAlbumView() {
        AlbumView album = page.getContentPanel().albumView();
        // check Monuments shelf
        Tree.TreeNode node = Graphene.guardNoRequest(page.getLeftPanel().getPreDefinedGroupsTree()).expandNode(0);
        assertEquals(node.advanced().getNodes().size(), 1);
        // open Monuments and just buildings album
        Graphene.guardAjax(node).selectNode(0);
        waitAjax().until().element(album.getAlbumInfo()).text().contains("Monuments and just buildings pictures");
        album.checkAll("Monuments and just buildings", "Created 2009-12-18, contains 3 images", "Monuments and just buildings pictures", "Album group: Monuments");
        assertEquals(album.getPhotos().size(), 3);
        album.getPhotos().get(0).checkAll(120, "8561041065_43449e8697_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(1).checkAll(120, "2523480499_e988ddf4c1_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(2).checkAll(120, "4065627169_2e3cea3acf_o.jpg", "Dec 17, 2009");

        // check Nature shelf
        node = Graphene.guardNoRequest(page.getLeftPanel().getPreDefinedGroupsTree()).expandNode(1);
        assertEquals(node.advanced().getNodes().size(), 2);
        // open Animals album
        Graphene.guardAjax(node).selectNode(0);
        waitAjax().until().element(album.getAlbumInfo()).text().contains("Animals pictures");
        album.checkAll("Animals", "Created 2009-12-18, contains 6 images", "Animals pictures", "Album group: Nature");
        assertEquals(album.getPhotos().size(), 6);
        album.getPhotos().get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(1).checkAll(120, "1906662004_655d0f6ccf_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(4).checkAll(120, "9855284863_da027be6cf_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(5).checkAll(120, "5395800621_c0bc80ca53_o.jpg", "Dec 17, 2009");

        assertEquals(node.advanced().getNodes().size(), 2);
        // open Nature album
        Graphene.guardAjax(node).selectNode(1);
        waitAjax().until().element(album.getAlbumInfo()).text().contains("Nature pictures");
        album.checkAll("Nature", "Created 2009-12-18, contains 6 images", "Nature pictures", "Album group: Nature");
        assertEquals(album.getPhotos().size(), 6);
        album.getPhotos().get(0).checkAll(120, "294928909_01ab1f5696_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(1).checkAll(120, "1352614209_6bfde3b6c2_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(4).checkAll(120, "3392730627_1cdb18cba6_o.jpg", "Dec 17, 2009");
        album.getPhotos().get(5).checkAll(120, "3392993334_36d7f097df_o.jpg", "Dec 17, 2009");
    }

    @Test
    public void testPredefinedGroupView() {
        GroupView groupView = page.getContentPanel().groupView();

        // check Monuments shelf
        page.getLeftPanel().openPredefinedGroup(0);
        waitAjax().until().element(groupView.getGroupHeader().getAdditionalInfo()).is().visible();
        waitAjax().until().element(groupView.getGroupInfo()).text().contains("Monuments pictures");
        groupView.checkAll("Monuments", "Created 2009-12-18, contains 3 images into 1 albums", "Monuments pictures", false);
        assertEquals(groupView.getAlbumPreviews().size(), 1);
        groupView.getAlbumPreview(0).checkAll("Monuments and just buildings", "Dec 17, 2009");

        // check Nature shelf
        page.getLeftPanel().openPredefinedGroup(1);
        waitAjax().until().element(groupView.getGroupHeader().getAdditionalInfo()).is().visible();
        waitAjax().until().element(groupView.getGroupInfo()).text().contains("Nature pictures");
        groupView.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", false);
        assertEquals(groupView.getAlbumPreviews().size(), 2);
        groupView.getAlbumPreview(0).checkAll("Animals", "Dec 17, 2009");
        groupView.getAlbumPreview(1).checkAll("Nature", "Dec 17, 2009");
    }

//    @Test
//    public void testPredefinedGroupsView() {
//        page.getLeftPanel().openPredefinedGroup();
//        AlbumGroupsView publicAlbumView = page.getContentPanel().groupsView();
//        publicAlbumView.checkHeader("Public album groups (5)");
//        List<GroupView> groups = publicAlbumView.getGroups();
//        assertEquals(groups.size(), 5);
//
//        // check Monuments shelf
//        GroupView groupView = groups.get(0);
//        groupView.checkAll("Monuments", "Created 2009-12-18, contains 3 images into 1 albums", "Monuments pictures", true);
//        assertEquals(groupView.getAlbumPreviews().size(), 1);
//        groupView.getAlbumPreview(0).checkAll("Monuments and just buildings", "Dec 17, 2009");
//
//        // check Nature shelf
//        groupView = groups.get(1);
//        groupView.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", true);
//        assertEquals(groupView.getAlbumPreviews().size(), 2);
//        groupView.getAlbumPreview(0).checkAll("Animals", "Dec 17, 2009");
//        groupView.getAlbumPreview(1).checkAll("Nature", "Dec 17, 2009");
//    }
}
