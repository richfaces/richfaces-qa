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
package org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view;

import static org.richfaces.tests.photoalbum.ftest.webdriver.tests.AbstractPhotoalbumTest.NO_OWNER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.tests.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ShelfView {

    @FindBy(className = "shelf-header-table")
    private ShelfHeader shelfHeader;
    @FindBy(tagName = "p")
    private WebElement shelfDescription;
    @FindBy(css = "div.preview_box_album_120")
    private List<AlbumPreview> albumPreviews;

    public void checkAll(String headerInfo, String headerAdditionalInfo, String shelfInfo, boolean showShelfLinkVisible) {
        checkShelfHeader(headerInfo, headerAdditionalInfo, showShelfLinkVisible);
        checkShelfDescription(shelfInfo);
    }

    public void checkUserOwnsShelf(boolean owns) {
        ArrayList<WebElement> ownShelfLinks = Lists.newArrayList(shelfHeader.getDeleteShelfLink(), shelfHeader.getEditShelfPropertiesLink());
        if (owns) {
            PhotoalbumUtils.checkVisible(ownShelfLinks);
        } else {
            PhotoalbumUtils.checkNotVisible(ownShelfLinks);
        }
    }

    public void checkShelfHeader(String headerInfo, String headerAdditionalInfo, boolean showShelfLinkVisible) {
        shelfHeader.checkAll(headerInfo, headerAdditionalInfo, showShelfLinkVisible);
    }

    public void checkShelfHeader(String headerInfo, String headerAdditionalInfo) {
        shelfHeader.checkName(headerInfo);
        shelfHeader.checkAdditionalInfo(headerAdditionalInfo);
    }

    private void checkShelfDescription(String info) {
        assertEquals(shelfDescription.getText().trim(), info);
    }

    public AlbumPreview getAlbumPreview(int index) {
        return albumPreviews.get(index);
    }

    public List<AlbumPreview> getAlbumPreviews() {
        return Collections.unmodifiableList(albumPreviews);
    }

    public ShelfHeader getShelfHeader() {
        return shelfHeader;
    }

    public WebElement getShelfInfo() {
        return shelfDescription;
    }

    public static class ShelfHeader {

        @FindBy(css = "td > h1")
        private WebElement name;
        @FindBy(css = "td > h1 .rf-ii")
        private RichFacesInplaceInput input;
        @FindByJQuery(".additional-info-text:not('a')")
        private WebElement additionalInfo;
        @FindByJQuery(".shelf-header-table-col2 a:contains('View album group')")
        private WebElement viewShelfLink;
        @FindByJQuery(".shelf-header-table-col2 a:contains('Edit album group properties')")
        private WebElement editShelfPropertiesLink;
        @FindByJQuery(".shelf-header-table-col2 a:contains('Delete album group')")
        private WebElement deleteShelfLink;

        public void checkAll(String name, String additionalInfo, boolean visible) {
            checkAdditionalInfo(additionalInfo);
            checkName(name);
            checkViewShelfLinkVisible(visible);
        }

        public void checkViewShelfLinkVisible(boolean visible) {
            assertEquals(Utils.isVisible(viewShelfLink), visible);
        }

        public void checkName(String name) {
            assertEquals(this.name.getText().trim(), name);
        }

        public void checkAdditionalInfo(String additionalInfo) {
            assertTrue(this.getAdditionalInfo().getText().trim().matches(additionalInfo),
                "Was " + this.getAdditionalInfo().getText().trim() + " , expected " + additionalInfo);
        }

        public WebElement getAdditionalInfo() {
            return additionalInfo;
        }

        public WebElement getDeleteShelfLink() {
            return deleteShelfLink;
        }

        public WebElement getEditShelfPropertiesLink() {
            return editShelfPropertiesLink;
        }

        public RichFacesInplaceInput getInput() {
            return input;
        }

        public WebElement getNameElement() {
            return name;
        }

        public WebElement getViewShelfLink() {
            return viewShelfLink;
        }
    }

    public static class AlbumPreview {

        @ArquillianResource
        private WebDriver driver;

        @FindBy(css = "img.pr_album_bg")
        private WebElement albumBackgroundImage;
        @FindBy(css = "img.album-cover-image")
        private WebElement albumCoverImage;
        @FindBy(css = "div.album_data")
        private WebElement albumData;
        @FindBy(css = ".album_data a")
        private WebElement albumOwnerLink;
        @FindBy(css = "div.album_name")
        private WebElement albumName;
        @FindBy(tagName = "a")
        private WebElement link;
        private AlbumView albumInfo;

        private AlbumView getAlbumView() {
            if (albumInfo == null) {
                albumInfo = Graphene.createPageFragment(AlbumView.class, driver.findElement(By.cssSelector(".content_box")));
            }
            return albumInfo;
        }

        public AlbumView open() {
            Graphene.guardAjax(link).click();
            return getAlbumView();
        }

        public void checkAll(String albumName, Object albumData) {
            checkAll(albumName, albumData, NO_OWNER);
        }

        public void checkAll(String albumName, Object albumData, String albumOwner) {
            checkAlbumBackGroundImage();
            checkAlbumData(albumData);
            checkAlbumName(albumName);
            checkAlbumOwner(albumOwner);
        }

        private void checkAlbumBackGroundImage() {
            assertTrue(albumBackgroundImage.isDisplayed());
        }

//        private void checkAlbumCoverImage(String imageSrc) {
//            assertTrue(albumCoverImage.isDisplayed());
//            if (!imageSrc.equals(UNKNOWN_IMG_SRC)) {
//                assertTrue(albumCoverImage.getAttribute("src").contains(imageSrc));
//            }
//        }
        private void checkAlbumData(Object data) {
            assertEquals(albumData.getText(), data);
        }

        private void checkAlbumName(String name) {
            assertEquals(albumName.getText(), name);
        }

        private void checkAlbumOwner(String owner) {
            if (owner.equals(NO_OWNER)) {
                assertFalse(Utils.isVisible(albumOwnerLink));
            } else {
                assertEquals(albumOwnerLink.getText(), owner);
            }
        }
    }
}
