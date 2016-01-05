/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.contextMenu.RichFacesContextMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12488 extends AbstractWebDriverTest {

    private static final int OFFSET = 1;
    private static final int TOLERANCE = 5;

    @FindBy(css = "div[id$=contextMenu1]")
    private RichFacesContextMenu contextMenu1;
    @FindBy(css = "div[id$=contextMenu2]")
    private RichFacesContextMenu contextMenu2;
    @FindBy(css = "[id$=menuDetached]")
    private WebElement menuDetached;

    @Override
    public String getComponentTestPagePath() {
        return "richContextMenu/RF-12488.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12488")
    public void testDetachedContextMenuShouldShowOnCorrectPositionAndStayVisible() {
        // check no menu is displayed
        contextMenu1.advanced().waitUntilIsNotVisible().perform();
        contextMenu2.advanced().waitUntilIsNotVisible().perform();

        // display detached menu in the upper left corner
        Locations l = Utils.getLocations(menuDetached);
        Point topLeft = l.getTopLeft();
        new Actions(driver).moveToElement(menuDetached, OFFSET, OFFSET).click().perform();
        contextMenu2.advanced().waitUntilIsVisible().perform();
        Locations popupLocation = Utils.getLocations(contextMenu2.advanced().getMenuPopup());
        Utils.tolerantAssertPointEquals(topLeft, popupLocation.getTopLeft(), TOLERANCE, TOLERANCE,
            "Detached menu should be displayed in the top left corner of the clicked element!");

        // wait some time and check menu stays displayed
        waiting(1500);
        // this was fixed
        contextMenu2.advanced().waitUntilIsVisible()
            .withTimeout(1, TimeUnit.SECONDS)
            .withMessage("Detached menu should stay displayed after 1.5 s.")
            .perform();
        // hide attached menu
        getMetamerPage().getResponseDelayElement().click();
        contextMenu2.advanced().waitUntilIsNotVisible().perform();
    }
}
