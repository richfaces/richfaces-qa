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

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.HowItWorksPopupPanel;
import org.richfaces.tests.photoalbum.ftest.webdriver.fragments.view.PhotoView;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestHowItWorksPanels extends AbstractPhotoalbumTest {

    @FindBy(className = "rf-pp-cntr[id*='helpPanel']")
    private HowItWorksPopupPanel howItWorksPanel;

    @FindByJQuery("a:has(img):eq(0)")
    private WebElement howItWorksNavigation;
    @FindByJQuery("a:has(img):last")
    private WebElement howItWorksStatus;

    @Test
    public void testHowItWorksFileUpload() {
        login();
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddImagesLink()).click();
        Graphene.guardAjax(page.getContentPanel().addImagesView().getFileUploadHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Upload Images");
    }

    @Test
    public void testHowItWorksImagesScroller() {
        page.getLeftPanel().openAlbumInPredefinedShelf("Animals", "Nature");
        page.getContentPanel().albumView().getPhotos().get(0).open();
        PhotoView photoView = page.getContentPanel().photoView();
        Graphene.guardAjax(photoView.getImagesScroller().getImageScrollerHelp()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Images Scroller");
    }

    @Test
    public void testHowItWorksInputNumberSlider() {
        page.getLeftPanel().openAlbumInPredefinedShelf("Animals", "Nature");
        Graphene.guardAjax(page.getContentPanel().albumView().getSliderHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Image Size Control with <rich:inputNumberSlider>");
    }

    @Test
    public void testHowItWorksNavigationForPredefinedShelves() {
        Graphene.guardAjax(page.getLeftPanel().getPreDefinedShelvesHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Navigation tree with pre-defined shelves.");
    }

    @Test
    public void testHowItWorksNavigationForOwnShelves() {
        login();

        Graphene.guardAjax(page.getLeftPanel().getMyShelvesHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Navigation tree for a registered user");
    }
    @Test
    public void testHowItWorksSelect() {
        login();
        Graphene.guardAjax(page.getHeaderPanel().getToolbar().getAddImagesLink()).click();
        Graphene.guardAjax(page.getContentPanel().addImagesView().getSelectHelpLink()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("How the button is created and how it acts");
    }
    @Test
    public void testHowItWorksSlideShow() {
        page.getLeftPanel().openAlbumInPredefinedShelf("Animals", "Nature");
        page.getContentPanel().albumView().getPhotos().get(0).open();
        PhotoView photoView = page.getContentPanel().photoView();
        Graphene.guardAjax(photoView.getSlideShowHelp()).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("Slideshow mechanism.");
    }

    @Test
    public void testHowItWorksStatus() {
        Graphene.guardAjax(howItWorksStatus).click();
        howItWorksPanel.advanced().waitUntilPopupIsVisible().perform();
        howItWorksPanel.checkAll("The <a4j:status> component");
    }
}
