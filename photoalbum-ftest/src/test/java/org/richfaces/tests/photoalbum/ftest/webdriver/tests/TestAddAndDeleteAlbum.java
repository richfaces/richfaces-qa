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

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.tree.RichFacesTree;
import org.richfaces.fragment.tree.Tree;
import org.richfaces.tests.photoalbum.ftest.webdriver.annotations.DoNotLogoutAfter;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.AddAlbumPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.ConfirmationPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.ShelfView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.ShelvesView;
import org.testng.annotations.Test;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAddAndDeleteAlbum extends AbstractPhotoalbumTest {

    private static final String SHELFNAME = "Nature";
    private static final String ALBUMNAME = "New album";

    @Test
    @DoNotLogoutAfter
    public void addAlbum() {
        login();

        // open and cancel
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddAlbumLink()).click();
        AddAlbumPanel panel = page.getAddAlbumPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.cancel();

        // open and close
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddAlbumLink()).click();
        panel = page.getAddAlbumPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.cancel();

        // check initial state
        RichFacesTree myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 2);
        Tree.TreeNode node = Graphene.guardNoRequest(myShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(SHELFNAME));
        assertEquals(node.advanced().getNodes().size(), 2);

        // create album
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddAlbumLink()).click();
        panel = page.getAddAlbumPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.addAlbum(SHELFNAME, ALBUMNAME);

        // check changed state in left panel
        myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 2);
        node = Graphene.guardNoRequest(myShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(SHELFNAME));
        assertEquals(node.advanced().getNodes().size(), 3);
        // check changed state in shelves view
        page.getLeftPanel().openOwnShelves();
        ShelvesView shelvesView = getView(ShelvesView.class);
        shelvesView.checkHeader("My album groups (2)");
        shelvesView.getShelves().get(0).checkShelfHeader(SHELFNAME, "Created 2009-12-18, contains 12 images into 3 albums");
        // check state in shelf view
        page.getLeftPanel().openOwnShelf(SHELFNAME);
        ShelfView shelfView = getView(ShelfView.class);
        shelfView.checkShelfHeader(SHELFNAME, "Created 2009-12-18, contains 12 images into 3 albums");

        // open album
        page.getLeftPanel().openAlbumInOwnShelf(ALBUMNAME, SHELFNAME);
        DateTime dt = new DateTime();
        AlbumView albumView = getView(AlbumView.class);

        // check data
        DateTimeFormatter pattern = DateTimeFormat.forPattern("EEE MMM dd");
        albumView.checkAlbumHeader(ALBUMNAME, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images");
        assertEquals(albumView.getPhotos().size(), 0);
        albumView.checkUserOwnsAlbum(true);

    }

    @Test(dependsOnMethods = "addAlbum")
    public void deleteAlbum() {
        AlbumView albumView = getView(AlbumView.class);

        // cancel before delete
        Graphene.guardAjax(albumView.getAlbumHeader().getDeleteAlbumLink()).click();
        ConfirmationPanel confirmationPanel = page.getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.check("Are you sure? All images associated with this album will also be dropped! Click OK to proceed, otherwise click Cancel.");
        confirmationPanel.cancel();

        // close before delete
        Graphene.guardAjax(albumView.getAlbumHeader().getDeleteAlbumLink()).click();
        confirmationPanel = page.getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.close();

        // delete
        Graphene.guardAjax(albumView.getAlbumHeader().getDeleteAlbumLink()).click();
        confirmationPanel = page.getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.ok();

        // check
        RichFacesTree myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 2);
        Tree.TreeNode node = Graphene.guardNoRequest(myShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(SHELFNAME));
        assertEquals(node.advanced().getNodes().size(), 2);
    }
}
