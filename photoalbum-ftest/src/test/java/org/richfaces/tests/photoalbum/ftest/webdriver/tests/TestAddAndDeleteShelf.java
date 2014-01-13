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

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.fragment.tree.RichFacesTree;
import org.richfaces.tests.photoalbum.ftest.webdriver.annotations.DoNotLogoutAfter;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.AddShelfPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.ConfirmationPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.ShelfView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.ShelvesView;
import org.testng.annotations.Test;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAddAndDeleteShelf extends AbstractPhotoalbumTest {

    private static final String SHELF_NAME = "New Shelf";

    @Test
    @DoNotLogoutAfter
    public void addShelf() {
        login();

        DateTime dt = new DateTime();
        DateTimeFormatter pattern = DateTimeFormat.forPattern("EEE MMM dd");

        // open and cancel
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddShelfLink()).click();
        AddShelfPanel panel = page.getAddShelfPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.cancel();

        // open and close
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddShelfLink()).click();
        panel = page.getAddShelfPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.cancel();

        // check initial state
        RichFacesTree myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 2);
        assertEquals(myShelvesTree.advanced().getLeafNodes().size(), 0);

        // create shelf
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddShelfLink()).click();
        panel = page.getAddShelfPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.addShelf(SHELF_NAME, false);

        // check changed state in left panel
        myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 3);
        assertEquals(myShelvesTree.advanced().getNodes().get(2).advanced().getNodes().size(), 0);

        // check changed state in shelves view
        page.getLeftPanel().openOwnShelves();
        ShelvesView shelvesView = getView(ShelvesView.class);
        shelvesView.checkHeader("My album groups (3)");
        shelvesView.getShelves().get(2).checkAll(SHELF_NAME, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images into 0 albums", "", true);

        // open shelf
        page.getLeftPanel().openOwnShelf(SHELF_NAME);
        ShelfView shelfView = getView(ShelfView.class);

        // check data
        shelfView.checkShelfHeader(SHELF_NAME, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images into 0 albums");
        assertEquals(shelfView.getAlbumPreviews().size(), 0);
        shelfView.checkUserOwnsShelf(true);
    }

    @Test(dependsOnMethods = "addShelf")
    public void deleteShelf() {
        ShelfView shelfView = getView(ShelfView.class);
        // cancel before delete
        Graphene.guardAjax(shelfView.getShelfHeader().getDeleteShelfLink()).click();
        ConfirmationPanel confirmationPanel = page.getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.check("Are You sure? All nested albums and images will also be dropped! Click OK to proceed, otherwise click Cancel.");
        confirmationPanel.cancel();

        // close before delete
        Graphene.guardAjax(shelfView.getShelfHeader().getDeleteShelfLink()).click();
        confirmationPanel = page.getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.close();

        // delete
        Graphene.guardAjax(shelfView.getShelfHeader().getDeleteShelfLink()).click();
        confirmationPanel = page.getConfirmationPanel();
        confirmationPanel.advanced().waitUntilPopupIsVisible().perform();
        confirmationPanel.ok();

        // check
        RichFacesTree myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 2);
        assertEquals(myShelvesTree.advanced().getLeafNodes().size(), 0);

    }
}
