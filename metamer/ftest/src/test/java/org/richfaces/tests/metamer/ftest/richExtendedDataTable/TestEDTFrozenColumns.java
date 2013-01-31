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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.extendedDataTableAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestEDTFrozenColumns extends AbstractWebDriverTest {

    @Page
    private FrozenColumnsPage page;

    @Inject
    @Use(empty = false)
    private Integer numberOfColumns;
    private Point location;// for testScrollerForNotFrozenColumns

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/frozenColumns.xhtml");
    }

    @Test
    public void testInit() {
        Boolean notPresent = ElementNotPresent.getInstance().element(page.frozenColumnsTd).apply(driver);
        assertTrue(notPresent, "No frozen columns should be in page.");
    }

    /**
     * Tests if frozen columns html elements show when frozen columns are enabled(set to >=1).Checks if they are still
     * there after switching to another data page.
     */
    @Test
    @Use(field = "numberOfColumns", ints = { 0, 2, 4 })
    public void testFrozenColumnsShow() {
        extendedDataTableAttributes.set(ExtendedDataTableAttributes.frozenColumns, numberOfColumns);
        // test
        _testFrozenColumnsShow();

        // change page
        page.nextPage.click();
        Graphene.waitModel().until(Graphene.element(page.secondPageSpan).isVisible());
        // test
        _testFrozenColumnsShow();

        // change page
        page.nextPage.click();
        Graphene.waitModel().until(Graphene.element(page.thirdPageSpan).isVisible());
        // test
        _testFrozenColumnsShow();
    }

    public void _testFrozenColumnsShow() {
        // wait for list of elements with frozen columns with expected size
        List<WebElement> frozenColumns = guardListSize(page.frozenColumns, numberOfColumns);
        assertEquals(Integer.valueOf(frozenColumns.size()), numberOfColumns,
            "The number of frozen columns set is not equal to the number of frozen columns found");
    }

    /**
     * Tests if scroller for not frozen columns is moved to another position and if the default one is removed. Checks
     * if scroller is still there after switching to another data page.
     */
    @Test
    @Use(field = "numberOfColumns", ints = { 1, 3 })
    @Templates(exclude = { "richExtendedDataTable" })
    // TODO https://issues.jboss.org/browse/RF-12236 , when numberOfColumns=4
    public void testScrollerForNotFrozenColumns() {
        // check if default scroller is present and get its location
        Graphene.waitModel().until("Default scroller is not in the page.").element(page.defaultScroller).is().present();
        location = page.defaultScroller.getLocation();
        extendedDataTableAttributes.set(ExtendedDataTableAttributes.frozenColumns, numberOfColumns);
        // test
        _testScrollerForNotFrozenColumns();

        // change page
        page.nextPage.click();
        Graphene.waitModel().until(Graphene.element(page.secondPageSpan).isVisible());
        // test
        _testScrollerForNotFrozenColumns();

        // change page
        page.nextPage.click();
        Graphene.waitModel().until(Graphene.element(page.thirdPageSpan).isVisible());
        // test
        _testScrollerForNotFrozenColumns();
    }

    /**
     * Tests if scroller for not frozen columns is moved to another position and if the default one is removed. Checks
     * if scroller is still there after switching to another data page.
     */
    @Test(groups = { "4.Future" })
    @Use(field = "numberOfColumns", ints = { 1, 3 })
    @Templates(value = "richExtendedDataTable")
    @IssueTracking("https://issues.jboss.org/browse/RF-12278")
    // TODO https://issues.jboss.org/browse/RF-12236 , when numberOfColumns=4
    public void testScrollerForNotFrozenColumnsInRichExtendedDataTable() {
        // check if default scroller is present and get its location
        Graphene.waitModel().until("Default scroller is not in the page.").element(page.defaultScroller).is().present();
        location = page.defaultScroller.getLocation();
        extendedDataTableAttributes.set(ExtendedDataTableAttributes.frozenColumns, numberOfColumns);
        // test
        _testScrollerForNotFrozenColumns();

        // change page
        page.nextPage.click();
        Graphene.waitModel().until(Graphene.element(page.secondPageSpan).isVisible());
        // test
        _testScrollerForNotFrozenColumns();

        // change page
        page.nextPage.click();
        Graphene.waitModel().until(Graphene.element(page.thirdPageSpan).isVisible());
        // test
        _testScrollerForNotFrozenColumns();
    }

    private void _testScrollerForNotFrozenColumns() {
        // check if there is default scroller
        Graphene.waitModel().until("Default scroller should not be in the page.").element(page.defaultScroller).is()
            .not().present();
        // check if there is scroller for not frozen columns
        Graphene.waitModel().until("Scroller for the not frozen columns should be in the page.")
            .element(page.movedScroller).is().present();
        // check if the location of scroller moved
        assertNotEquals(location, page.movedScroller.getLocation(), "The position of scroller has not been changed.");
    }

    public class FrozenColumnsPage extends MetamerPage {

        @FindBy(xpath = "//td[@class='rf-edt-ftr-fzn']//div//table//tr//td")
        List<WebElement> frozenColumns;
        @FindBy(xpath = "//td[@class='rf-edt-ftr-fzn']")
        WebElement frozenColumnsTd;
        @FindBy(xpath = "//div[@class='rf-edt-ftr']//table//tbody//tr//td[@colspan=1][1]//div[@class='rf-edt-scrl']")
        WebElement defaultScroller;
        @FindBy(xpath = "//div[@class='rf-edt-ftr']//table//tbody//tr//td[@colspan=1][2]//div[@class='rf-edt-scrl']")
        WebElement movedScroller;
        @FindBy(css = "a.rf-ds-btn-next")
        WebElement nextPage;
        @FindBy(xpath = "//span[contains(@id, 'scroller2_ds_2')][contains(text(), '2')]")
        WebElement secondPageSpan;
        @FindBy(xpath = "//span[contains(@id, 'scroller2_ds_3')][contains(text(), '3')]")
        WebElement thirdPageSpan;
    }
}
