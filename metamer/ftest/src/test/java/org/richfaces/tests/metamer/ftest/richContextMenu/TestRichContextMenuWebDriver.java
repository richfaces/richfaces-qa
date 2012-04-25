/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
 */
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.contextMenuAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.test.selenium.support.ui.ElementDisplayed;
import org.jboss.test.selenium.support.ui.ElementNotDisplayed;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for rich:contextMenu component at
 * faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @since 4.2.1.Final
 */
public class TestRichContextMenuWebDriver extends AbstractWebDriverTest {

    private ContextMenuSimplePage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richContextMenu/simple.xhtml");
    }

    @BeforeMethod(dependsOnMethods = {"loadPage"})
    public void initPageObject() {
        page = new ContextMenuSimplePage();
        injectWebElementsToPage(page);
    }

    @BeforeMethod(dependsOnMethods = {"initPageObject"})
    public void updateShowAction() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
    }

    @Test
    public void testHideDelay() throws InterruptedException {
        // set hideDelay
        contextMenuAttributes.set(ContextMenuAttributes.hideDelay, 500);
        // perform the test
        page.targetPanel1.click();
        // check whether the context menu is displayed
        new WebDriverWait(driver, WAIT_TIME).until(
                ElementDisplayed.getInstance().element(page.contextMenuContent));
        // lose focus
        page.targetPanel2.click();
        assertTrue(page.contextMenuContent.isDisplayed());
        Thread.sleep(600);
        assertFalse(page.contextMenuContent.isDisplayed());
    }

    @Test
    public void testOnhide() {
        // set onhide
        contextMenuAttributes.set(ContextMenuAttributes.onhide, "metamerEvents += \"hide\"");
        page.targetPanel1.click();
        // check whether the context menu is displayed
        new WebDriverWait(driver, WAIT_TIME).until(
                ElementDisplayed.getInstance().element(page.contextMenuContent));
        page.targetPanel2.click();
        // check whether the context menu isn't displayed
        new WebDriverWait(driver, WAIT_TIME).until(
                ElementNotDisplayed.getInstance().element(page.contextMenuContent));
        String event = ((String) executeJS("return window.metamerEvents")).trim();
        assertEquals(event, "hide");
    }

    @Test
    public void testVerticalOffset() {
        int offset = 11;
        // show context menu
        page.targetPanel1.click();
        // check whether the context menu is displayed
        new WebDriverWait(driver, WAIT_TIME).until(
                ElementDisplayed.getInstance().element(page.contextMenuContent));
        // get position before offset is set
        Point before = page.contextMenuContent.getLocation();
        // set verticalOffset
        contextMenuAttributes.set(ContextMenuAttributes.verticalOffset, offset);
        // show context menu
        page.targetPanel1.click();
        // check whether the context menu is displayed
        new WebDriverWait(driver, WAIT_TIME).until(
                ElementDisplayed.getInstance().element(page.contextMenuContent));
        // get position after offset is set
        Point after = page.contextMenuContent.getLocation();
        // check offset
        assertEquals(before.getY(), after.getY() - offset);
    }
}
