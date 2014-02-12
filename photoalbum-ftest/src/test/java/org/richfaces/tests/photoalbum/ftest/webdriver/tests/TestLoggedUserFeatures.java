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
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.fileUpload.RichFacesFileUpload;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.fragment.tree.RichFacesTree;
import org.richfaces.fragment.tree.Tree;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.AddAlbumPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.AddShelfPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.ConfirmationPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AddImagesView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AllAlbumsView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.AllImagesView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.PhotoView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.ShelfView;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.ShelvesView;
import org.testng.annotations.Test;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestLoggedUserFeatures extends AbstractPhotoalbumTest {

    private static final String GOOD_IMAGE_TO_UPLOAD = "good.jpg";
    private static final String BAD_IMAGE_TO_UPLOAD = "bad.txt";

    private File createFileFromString(String filename) {
        File file = null;
        try {
            file = new File(TestLoggedUserFeatures.class.getResource(filename).toURI());
        } catch (URISyntaxException ex) {
        }
        assertTrue(file != null && file.exists(), "File does not exist.");
        return file;
    }

    @Test
    public void testAddAndDeleteComment() {
        login();

        DateTime dt = new DateTime();
        DateTimeFormatter pattern = DateTimeFormat.forPattern("MMM d, YYYY");

        // open first photo in album 'Monuments and just buildings'
        page.getLeftPanel().openAlbumInPredefinedShelf("Monuments and just buildings", "Monuments");
        page.getContentPanel().albumView().getPhotos().get(0).open();
        PhotoView photoView = page.getContentPanel().photoView();
        PhotoView.CommentsPanel commentPanel = photoView.getCommentPanel();

        // check previous comments
        List<PhotoView.CommentsPanel.Comment> comments = commentPanel.getComments();
        assertEquals(comments.size(), 3);
        comments.get(0).checkAll("Jan 7, 1985", "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll("Jan 7, 1985", "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_default.png", "amarkhel");
        comments.get(2).checkAll("Jan 7, 1985", "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");

        // add comment
        String comment = "new comment";
        commentPanel.addComment(comment);

        // check comments
        comments = commentPanel.getComments();
        assertEquals(comments.size(), 4);
        comments.get(0).checkAll("Jan 7, 1985", "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll("Jan 7, 1985", "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_default.png", "amarkhel");
        comments.get(2).checkAll("Jan 7, 1985", "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");
        comments.get(3).checkAll(dt.toString(pattern), comment, "avatar_default.png", "amarkhel");
        comments.get(3).checkIfUsersComment();

        // move to second image in album and back
        photoView.getImagesScroller().getPreviews().get(1).open();
        photoView.getImagesScroller().getPreviews().get(0).open();

        // check comments again
        comments = commentPanel.getComments();
        assertEquals(comments.size(), 4);
        comments.get(0).checkAll("Jan 7, 1985", "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll("Jan 7, 1985", "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_default.png", "amarkhel");
        comments.get(2).checkAll("Jan 7, 1985", "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");
        comments.get(3).checkAll(dt.toString(pattern), comment, "avatar_default.png", "amarkhel");
        comments.get(3).checkIfUsersComment();

        // delete
        comments.get(3).delete();

        // check if deleted
        comments = commentPanel.getComments();
        assertEquals(comments.size(), 3);
        comments.get(0).checkAll("Jan 7, 1985", "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll("Jan 7, 1985", "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_default.png", "amarkhel");
        comments.get(2).checkAll("Jan 7, 1985", "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");

    }

    @Test
    public void testEditAlbumName() {
        login();

        // switch to Animals album
        page.getLeftPanel().openAlbumInOwnShelf("Animals", "Nature");

        AlbumView.AlbumHeader albumHeader = page.getContentPanel().albumView().getAlbumHeader();
        assertEquals(albumHeader.getNameElement().getText().trim(), "Animals");
        RichFacesInplaceInput input = albumHeader.getInput();
        assertEquals(input.getTextInput().getStringValue(), "Animals");

        // change the name of the Animals album
        Graphene.guardAjax(input.type("Animals album")).confirm();
        // check the change in left panel (navigation tree) and on current content view
        assertEquals(albumHeader.getNameElement().getText().trim(), "Animals album");
        assertEquals(page.getLeftPanel().getMyShelvesTree().advanced().getFirstNode() // animals are first album in first own shelf
            .advanced().getFirstNode().advanced().getLabelElement().getText().trim(), "Animals album");
        // check the change in the name of the link of inner photo
        page.getContentPanel().albumView().getPhotos().get(0).open();
        PhotoView photoView = page.getContentPanel().photoView();
        String albumLinkText = photoView.getPhotoHeader().getLinks().get(1).getText();
        assertEquals(albumLinkText.trim(), "Album: Animals album");
    }

    @Test
    public void testEditPhotoName() {
        login();

        // switch to Animals album
        page.getLeftPanel().openAlbumInOwnShelf("Animals", "Nature");

        // open first photo
        page.getContentPanel().albumView().getPhotos().get(0).open();

        PhotoView photoView = page.getContentPanel().photoView();
        assertEquals(photoView.getPhotoHeader().getNameElement().getText().trim(), "1750979205_6e51b47ce9_o.jpg");
        RichFacesInplaceInput input = photoView.getPhotoHeader().getInput();
        assertEquals(input.getTextInput().getStringValue(), "1750979205_6e51b47ce9_o.jpg");
//        assertEquals(photoView.getPhotoHeader().getAdditionalInfo().getText().trim(), "1750979205_6e51b47ce9_o.jpg");

        // change photo name
        Graphene.guardAjax(input.type("firstPhoto.jpg")).confirm();
        // check photo name
        assertEquals(photoView.getPhotoHeader().getNameElement().getText().trim(), "firstPhoto.jpg");
        assertEquals(input.getTextInput().getStringValue(), "firstPhoto.jpg");
//        assertEquals(photoView.getPhotoHeader().getAdditionalInfo().getText().trim(), "firstPhoto.jpg");
    }

    @Test
    public void testEditShelfName() {
        login();

        // switch to Nature shelf
        page.getLeftPanel().openOwnShelf("Nature");

        ShelfView.ShelfHeader shelfHeader = page.getContentPanel().shelfView().getShelfHeader();
        assertEquals(shelfHeader.getNameElement().getText().trim(), "Nature");
        RichFacesInplaceInput input = shelfHeader.getInput();
        assertEquals(input.getTextInput().getStringValue(), "Nature");

        // change the name of the shelf
        Graphene.guardAjax(input.type("Nature shelf")).confirm();
        // check the change in left panel (navigation tree) and on current content view
        assertEquals(shelfHeader.getNameElement().getText().trim(), "Nature shelf");
        assertEquals(page.getLeftPanel().getMyShelvesTree().advanced().getFirstNode()
            .advanced().getLabelElement().getText().trim(), "Nature shelf");

        // check the change in the name of the link of inner album
        page.getContentPanel().shelfView().getAlbumPreviews().get(0).open();
        AlbumView albumView = page.getContentPanel().albumView();
        String albumLinkText = albumView.getAlbumHeader().getLinks().get(0).getText();
        assertEquals(albumLinkText.trim(), "Shelf: Nature shelf");
    }

    @Test
    public void testToolbarAddAndDeleteAlbum() {
        login();
        String shelfName = "Nature";
        String albumName = "New album";

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
        Tree.TreeNode node = Graphene.guardNoRequest(myShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(shelfName));
        assertEquals(node.advanced().getNodes().size(), 2);

        // create album
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddAlbumLink()).click();
        panel = page.getAddAlbumPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.addAlbum(shelfName, albumName);

        // check changed state in left panel
        myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 2);
        node = Graphene.guardNoRequest(myShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(shelfName));
        assertEquals(node.advanced().getNodes().size(), 3);
        // check changed state in shelves view
        page.getLeftPanel().openOwnShelves();
        ShelvesView shelvesView = page.getContentPanel().shelvesView();
        shelvesView.checkHeader("My shelves (2)");
        shelvesView.getShelves().get(0).checkShelfHeader(shelfName, "Created 2009-12-18, contains 12 images into 3 albums");
        // check state in shelf view
        page.getLeftPanel().openOwnShelf(shelfName);
        ShelfView shelfView = page.getContentPanel().shelfView();
        shelfView.checkShelfHeader(shelfName, "Created 2009-12-18, contains 12 images into 3 albums");

        // open album
        page.getLeftPanel().openAlbumInOwnShelf(albumName, shelfName);
        DateTime dt = new DateTime();
        AlbumView albumView = page.getContentPanel().albumView();

        // check data
        DateTimeFormatter pattern = DateTimeFormat.forPattern("EEE MMM dd");
        albumView.checkAlbumHeader(albumName, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images");
        assertEquals(albumView.getPhotos().size(), 0);
        albumView.checkUserOwnsAlbum(true);

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
        myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 2);
        node = Graphene.guardNoRequest(myShelvesTree).expandNode(ChoicePickerHelper.byVisibleText().contains(shelfName));
        assertEquals(node.advanced().getNodes().size(), 2);
    }

    @Test
    public void testToolbarAddAndDeleteShelf() {
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
        String shelfName = "New Shelf";
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddShelfLink()).click();
        panel = page.getAddShelfPanel();
        panel.advanced().waitUntilPopupIsVisible().perform();
        panel.addShelf(shelfName, false);

        // check changed state in left panel
        myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 3);
        assertEquals(myShelvesTree.advanced().getNodes().get(2).advanced().getNodes().size(), 0);

        // check changed state in shelves view
        page.getLeftPanel().openOwnShelves();
        ShelvesView shelvesView = page.getContentPanel().shelvesView();
        shelvesView.checkHeader("My shelves (3)");
        shelvesView.getShelves().get(2).checkAll(shelfName, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images into 0 albums", "", true);

        // open shelf
        page.getLeftPanel().openOwnShelf(shelfName);
        ShelfView shelfView = page.getContentPanel().shelfView();

        // check data
        shelfView.checkShelfHeader(shelfName, "Created " + dt.toString(pattern) + ".*" + dt.getYear() + ", contains 0 images into 0 albums");
        assertEquals(shelfView.getAlbumPreviews().size(), 0);
        shelfView.checkUserOwnsShelf(true);

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
        myShelvesTree = page.getLeftPanel().getMyShelvesTree();
        assertEquals(myShelvesTree.advanced().getNodes().size(), 2);
        assertEquals(myShelvesTree.advanced().getLeafNodes().size(), 0);
    }

    @Test
    public void testToolbarViewMyAlbumShelves() {
        login();

        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getMyAlbumShelvesLink()).click();
        ShelvesView shelvesView = page.getContentPanel().shelvesView();
        shelvesView.checkHeader("My shelves (2)");
        assertEquals(shelvesView.getShelves().size(), 2);
        shelvesView.getShelves().get(0).checkAll("Nature", "Created 2009-12-18, contains 12 images into 2 albums", "Nature pictures", true);
        shelvesView.getShelves().get(1).checkAll("Sport & Cars", "Created 2009-12-18, contains 9 images into 2 albums", "Sport & Cars pictures", true);
    }

    @Test
    public void testToolbarViewMyAllAlbums() {
        login();

        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getMyAllAlbumsLink()).click();
        AllAlbumsView albumsView = page.getContentPanel().allAlbumsView();
        albumsView.checkAlbumsHeader("My all albums (4)");
        assertEquals(albumsView.getAlbumPreviews().size(), 4);
        albumsView.getAlbumPreview(0).checkAll("Animals", "Dec 17, 2009");
        albumsView.getAlbumPreview(1).checkAll("Nature", "Dec 17, 2009");
        albumsView.getAlbumPreview(2).checkAll("Cars", "Dec 17, 2009");
        albumsView.getAlbumPreview(3).checkAll("Sport", "Dec 17, 2009");
    }

    @Test
    public void testToolbarViewMyAllImages() {
        login();

        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getMyAllImagesLink()).click();

        AllImagesView allImagesView = page.getContentPanel().allImagesView();
        allImagesView.checkHeader("My all images (21)");
        allImagesView.checkScrollerVisible();
        allImagesView.checkSliderVisible();
        assertEquals(allImagesView.getDataScroller().advanced().getCountOfVisiblePages(), 2);
        assertEquals(allImagesView.getPhotos().size(), 12);
        allImagesView.getPhotos().get(0).checkAll(120, "1750979205_6e51b47ce9_o.jpg", "Dec 17, 2009");
        allImagesView.getPhotos().get(11).checkAll(120, "3392993334_36d7f097df_o.jpg", "Dec 17, 2009");

        // scroll to next page
        Graphene.guardAjax(allImagesView.getDataScroller()).switchTo(2);
        assertEquals(allImagesView.getPhotos().size(), 9);
        allImagesView.getPhotos().get(0).checkAll(120, "190193308_ce2a4de5fa_o.jpg", "Dec 17, 2009");
        allImagesView.getPhotos().get(8).checkAll(120, "2042654579_d25c0db64f_o.jpg", "Dec 17, 2009");
    }

    @Test(enabled = false)
    public void testToolbarAddImages() {
        login();

        // open view
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddImagesLink()).click();
        AddImagesView addImagesView = page.getContentPanel().addImagesView();

        // select album to add the pictures
        addImagesView.getSelect().openSelect().select("Nature");

        // upload the picture
        RichFacesFileUpload fileUpload = addImagesView.getFileUpload();
        fileUpload.addFile(createFileFromString(GOOD_IMAGE_TO_UPLOAD));
        Graphene.guardAjax(fileUpload).upload();

        // TODO: check, delete
    }
}
