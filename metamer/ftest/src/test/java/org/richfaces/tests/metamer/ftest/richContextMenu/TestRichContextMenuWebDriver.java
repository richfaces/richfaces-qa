/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.contextMenuAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.Point;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for rich:contextMenu component at
 * faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @since 4.2.1.Final
 */
public class TestRichContextMenuWebDriver extends AbstractWebDriverTest {

    @Page
    private ContextMenuSimplePage page;
    @Inject
    @Use(empty = false)
    private Integer delay;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richContextMenu/simple.xhtml");
    }

    @BeforeMethod(alwaysRun = true)
    public void updateShowAction() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
    }

    @Test
    @Use(field = "delay", ints = { 1000, 1500, 2500 })
    public void testHideDelay() {
        double differenceThreshold = delay * 0.5;
        // set hideDelay
        contextMenuAttributes.set(ContextMenuAttributes.hideDelay, delay);
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // save the time
        double beforeTime = System.currentTimeMillis();
        // blur  >>> menu will disappear after delay
        page.clickOnSecondPanel(driverType);
        assertTrue(page.contextMenuContent.isDisplayed());
        //wait until menu hides
        page.waitUntilContextMenuHides();

        double diff = System.currentTimeMillis() - beforeTime;
        double diff2 = Math.abs(diff - delay);
        assertTrue(diff2 < differenceThreshold, "The measured delay was far"
                + " from set value. The difference was: " + diff2
                + ". The difference threshold was: " + differenceThreshold);
    }

    @Test
    public void testOnhide() {
        final String VALUE = "hide";
        // set onhide
        contextMenuAttributes.set(ContextMenuAttributes.onhide, "metamerEvents += \"" + VALUE + "\"");
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        //lose focus >>> menu will disappear
        page.clickOnSecondPanel(driverType);
        // check whether the context menu isn't displayed
        page.waitUntilContextMenuHides();
        assertEquals(expectedReturnJS("return metamerEvents", VALUE), VALUE);
    }

    @Test
    public void testVerticalOffset() {
        int offset = 11;
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // get position before offset is set
        Point before = page.contextMenuContent.getLocation();
        // set verticalOffset
        contextMenuAttributes.set(ContextMenuAttributes.verticalOffset, offset);
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // get position after offset is set
        Point after = page.contextMenuContent.getLocation();
        // check offset
        assertEquals(before.getY(), after.getY() - offset);
    }
}
