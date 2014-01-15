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
package org.richfaces.tests.photoalbum.ftest.webdriver.tests;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.fragment.tree.Tree;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.ShelfView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.ShelvesView;
import org.richfaces.tests.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestLeftPanelNavigations extends AbstractPhotoalbumTest {

    private static final List<String> PREDEFINED_SHELVES = Lists.newArrayList("Monuments", "Nature", "Portrait", "Sport & Cars", "Water");
    private static final List<String> CUSTOM_SHELVES = Lists.newArrayList("Nature", "Sport & Cars");

    @Test
    public void testInitialState() {
        assertEquals(PhotoalbumUtils.getStringsFromElements(page.getLeftPanel().getPreDefinedShelvesTree().advanced().getNodesElements()), PREDEFINED_SHELVES);
    }

    @Test
    public void testInitialStateLoggedUser() {
        login();
        assertEquals(PhotoalbumUtils.getStringsFromElements(page.getLeftPanel().getPreDefinedShelvesTree().advanced().getNodesElements()), PREDEFINED_SHELVES);
        assertEquals(PhotoalbumUtils.getStringsFromElements(page.getLeftPanel().getMyShelvesTree().advanced().getNodesElements()), CUSTOM_SHELVES);
    }

    @Test
    public void testOwnAlbumView() {
        login();

        AlbumView album = getView(AlbumView.class);
        // check Nature shelf
        Tree.TreeNode node = Graphene.guardNoRequest(page.getLeftPanel().getMyShelvesTree()).expandNode(0);
        assertEquals(node.advanced().getNodes().size(), 2);
        // open Animals album
        Graphene.guardAjax(node).selectNode(0);
        album.checkAll("Animals", "Created 2009-12-18, contains 6 images", "Animals pictures", "Album group: Nature");
        assertEquals(album.getPhotos().size(), 6);
        album.getPhotos().get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", DEC_17_2009);
        album.getPhotos().get(1).checkAll(120, "1906662004_655d0f6ccf_o.jpg", DEC_17_2009);
        album.getPhotos().get(4).checkAll(120, "2298556444_2151b7a6c4_o.jpg", DEC_17_2009);
        album.getPhotos().get(5).checkAll(120, "2508246015_313952406c_o.jpg", DEC_17_2009);

        // check Sport & Cars shelf
        node = Graphene.guardNoRequest(page.getLeftPanel().getMyShelvesTree()).expandNode(1);
        assertEquals(node.advanced().getNodes().size(), 2);
        // open Sport album
        Graphene.guardAjax(node).selectNode(1);
        album.checkAll("Sport", "Created 2009-12-18, contains 3 images", "Sport pictures", "Album group: Sport & Cars");
        assertEquals(album.getPhotos().size(), 3);
        album.getPhotos().get(0).checkAll(120, "103193233_860c47c909_o.jpg", DEC_17_2009);
        album.getPhotos().get(1).checkAll(120, "1350250361_2d963dd4e7_o.jpg", DEC_17_2009);
        album.getPhotos().get(2).checkAll(120, "2042654579_d25c0db64f_o.jpg", DEC_17_2009);
    }

    @Test
    public void testOwnShelfView() {
        login();
        ShelfView shelf = getView(ShelfView.class);

        // check Nature shelf
        page.getLeftPanel().openOwnShelf(0);
        shelf.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", false);
        assertEquals(shelf.getAlbumPreviews().size(), 2);
        shelf.getAlbumPreview(0).checkAll("Animals", DEC_17_2009);
        shelf.getAlbumPreview(1).checkAll("Nature", DEC_17_2009);

        // check Sport & Cars shelf
        page.getLeftPanel().openOwnShelf(1);
        shelf.checkAll("Sport & Cars", "Created 2009-12-18, contains 9 images into 2 albums", "Sport & Cars pictures", false);
        assertEquals(shelf.getAlbumPreviews().size(), 2);
        shelf.getAlbumPreview(0).checkAll("Cars", DEC_17_2009);
        shelf.getAlbumPreview(1).checkAll("Sport", DEC_17_2009);
    }

    @Test
    public void testOwnShelvesView() {
        login();
        page.getLeftPanel().openOwnShelves();
        ShelvesView shelvesView = getView(ShelvesView.class);
        shelvesView.checkHeader("My album groups (2)");
        List<ShelfView> shelves = shelvesView.getShelves();
        assertEquals(shelves.size(), 2);

        // check Monuments shelf
        ShelfView shelf = shelves.get(0);
        shelf.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", true);
        assertEquals(shelf.getAlbumPreviews().size(), 2);
        shelf.getAlbumPreview(0).checkAll("Animals", DEC_17_2009);
        shelf.getAlbumPreview(1).checkAll("Nature", DEC_17_2009);

        // check Nature shelf
        shelf = shelves.get(1);
        shelf.checkAll("Sport & Cars", "Created 2009-12-18, contains 9 images into 2 albums", "Sport & Cars pictures", true);
        assertEquals(shelf.getAlbumPreviews().size(), 2);
        shelf.getAlbumPreview(0).checkAll("Cars", DEC_17_2009);
        shelf.getAlbumPreview(1).checkAll("Sport", DEC_17_2009);
    }

    @Test
    public void testPublicAlbumView() {
        AlbumView album = getView(AlbumView.class);
        // check Monuments shelf
        Tree.TreeNode node = Graphene.guardNoRequest(page.getLeftPanel().getPreDefinedShelvesTree()).expandNode(0);
        assertEquals(node.advanced().getNodes().size(), 1);
        // open Monuments and just buildings album
        Graphene.guardAjax(node).selectNode(0);
        album.checkAll("Monuments and just buildings", "Created 2009-12-18, contains 3 images", "Monuments and just buildings pictures", "Album group: Monuments");
        assertEquals(album.getPhotos().size(), 3);
        album.getPhotos().get(0).checkAll(120, "05[303x457].jpg", DEC_17_2009);
        album.getPhotos().get(1).checkAll(120, "07[303x457].jpg", DEC_17_2009);
        album.getPhotos().get(2).checkAll(120, "1805365000_ca64d20b10_o.jpg", DEC_17_2009);

        // check Nature shelf
        node = Graphene.guardNoRequest(page.getLeftPanel().getPreDefinedShelvesTree()).expandNode(1);
        assertEquals(node.advanced().getNodes().size(), 2);
        // open Animals album
        Graphene.guardAjax(node).selectNode(0);
        album.checkAll("Animals", "Created 2009-12-18, contains 6 images", "Animals pictures", "Album group: Nature");
        assertEquals(album.getPhotos().size(), 6);
        album.getPhotos().get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", DEC_17_2009);
        album.getPhotos().get(1).checkAll(120, "1906662004_655d0f6ccf_o.jpg", DEC_17_2009);
        album.getPhotos().get(4).checkAll(120, "2298556444_2151b7a6c4_o.jpg", DEC_17_2009);
        album.getPhotos().get(5).checkAll(120, "2508246015_313952406c_o.jpg", DEC_17_2009);

        assertEquals(node.advanced().getNodes().size(), 2);
        // open Nature album
        Graphene.guardAjax(node).selectNode(1);
        album.checkAll("Nature", "Created 2009-12-18, contains 6 images", "Nature pictures", "Album group: Nature");
        assertEquals(album.getPhotos().size(), 6);
        album.getPhotos().get(0).checkAll(120, "01[303x202].jpg", DEC_17_2009);
        album.getPhotos().get(1).checkAll(120, "1[305x457].jpg", DEC_17_2009);
        album.getPhotos().get(4).checkAll(120, "3392730627_1cdb18cba6_o.jpg", DEC_17_2009);
        album.getPhotos().get(5).checkAll(120, "3392993334_36d7f097df_o.jpg", DEC_17_2009);
    }

    @Test
    public void testPublicShelfView() {
        ShelfView shelf = getView(ShelfView.class);

        // check Monuments shelf
        page.getLeftPanel().openPredefinedShelf(0);
        shelf.checkAll("Monuments", "Created 2009-12-18, contains 3 images into 1 albums", "Monuments pictures", false);
        assertEquals(shelf.getAlbumPreviews().size(), 1);
        shelf.getAlbumPreview(0).checkAll("Monuments and just buildings", DEC_17_2009);

        // check Nature shelf
        page.getLeftPanel().openPredefinedShelf(1);
        shelf.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", false);
        assertEquals(shelf.getAlbumPreviews().size(), 2);
        shelf.getAlbumPreview(0).checkAll("Animals", DEC_17_2009);
        shelf.getAlbumPreview(1).checkAll("Nature", DEC_17_2009);
    }

    @Test
    public void testPublicShelvesView() {
        page.getLeftPanel().openPredefinedShelves();
        ShelvesView shelvesView = getView(ShelvesView.class);
        shelvesView.checkHeader("Public album groups (5)");
        List<ShelfView> shelves = shelvesView.getShelves();
        assertEquals(shelves.size(), 5);

        // check Monuments shelf
        ShelfView shelf = shelves.get(0);
        shelf.checkAll("Monuments", "Created 2009-12-18, contains 3 images into 1 albums", "Monuments pictures", true);
        assertEquals(shelf.getAlbumPreviews().size(), 1);
        shelf.getAlbumPreview(0).checkAll("Monuments and just buildings", DEC_17_2009);

        // check Nature shelf
        shelf = shelves.get(1);
        shelf.checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", true);
        assertEquals(shelf.getAlbumPreviews().size(), 2);
        shelf.getAlbumPreview(0).checkAll("Animals", DEC_17_2009);
        shelf.getAlbumPreview(1).checkAll("Nature", DEC_17_2009);
    }
}
