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
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.contextMenuAttributes;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.richfaces.component.Positioning;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.DriverType;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.contextMenu.internal.RichFacesContextMenuInternal;

/**
 * Page object rich:contextMenu component at faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @since 4.2.1.Final
 */
public class ContextMenuSimplePage extends MetamerPage {

    @FindBy(css = "div[id$=targetPanel1]")
    public WebElement targetPanel1;

    @FindBy(css = "div[id$=targetPanel2]")
    public WebElement targetPanel2;

    @FindBy(css = "div.rf-ctx-lst")
    public WebElement contextMenuContent;

    @FindBy(css = "div[id$=ctxMenu]")
    public RichFacesContextMenuInternal contextMenu;

    @FindBy(jquery = "span[id$=output]")
    public WebElement output;

    @FindBy(jquery = "div[id$=\":ctxMenu\"]")
    public WebElement contextMenuRoot;

    @FindBy(tagName = "body")
    public WebElement body;

    //Chrome
    public final Point TOP_LEFT_CH = new Point(410, 109);
    public final Point TOP_RIGHT_CH = new Point(660, 109);
    public final Point BOTTOM_LEFT_CH = new Point(410, 245);
    public final Point BOTTOM_RIGHT_CH = new Point(660, 245);

    //Firefox
    public final Point TOP_LEFT_F = new Point(410, 143);
    public final Point TOP_RIGHT_F = new Point(660, 143);
    public final Point BOTTOM_LEFT_F = new Point(410, 279);
    public final Point BOTTOM_RIGHT_F = new Point(660, 279);

    public final int SHOW_DELAY_TOLERANCE = 400;

    public Point getExpectedLocation(Positioning positioning) {
        if (DriverType.getCurrentType(driver).equals(DriverType.FireFox)) {
            switch (positioning) {
                case topLeft:
                    return TOP_LEFT_F;
                case topRight:
                    return TOP_RIGHT_F;
                case bottomRight:
                    return BOTTOM_RIGHT_F;
                case bottomLeft:
                    return BOTTOM_LEFT_F;
                default:
                    throw new IllegalArgumentException("You should provide one of: topLeft, topRight, bottomRight, bottomLeft!");
            }
        } else {
            switch (positioning) {
                case topLeft:
                    return TOP_LEFT_CH;
                case topRight:
                    return TOP_RIGHT_CH;
                case bottomRight:
                    return BOTTOM_RIGHT_CH;
                case bottomLeft:
                    return BOTTOM_LEFT_CH;
                default:
                    throw new IllegalArgumentException("You should provide one of: topLeft, topRight, bottomRight, bottomLeft!");
            }
        }
    }

    public void clickOnFirstPanel(DriverType type) {
        if (type == DriverType.InternetExplorer) {
            targetPanel1.sendKeys(Keys.CONTROL);
        }
        targetPanel1.click();
    }

    public void clickOnSecondPanel(DriverType type) {
        if (type == DriverType.InternetExplorer) {
            targetPanel2.sendKeys(Keys.CONTROL);
        }
        targetPanel2.click();
    }

    public void waitUntilContextMenuAppears() {
        Graphene.waitModel().withMessage("Context menu did not show.").until(Graphene.element(contextMenuContent).isVisible());
    }

    public void waitUntilContextMenuHides() {
        Graphene.waitModel().withMessage("Context menu should not be visible.")
                .until(Graphene.element(contextMenuContent).not().isVisible());
    }

    public Point getContextMenuLocationWhenPosition(Positioning positioning) {
        contextMenuAttributes.set(ContextMenuAttributes.direction, positioning);
        contextMenu.invoke(targetPanel2);
        Point contextMenuLocation = contextMenuContent.getLocation();
        contextMenu.dismiss();
        return contextMenuLocation;
    }

    public long getActualShowDelay(final int showDelay) {
        contextMenuAttributes.set(ContextMenuAttributes.showDelay, String.valueOf(showDelay));
        long showEventObserving = System.currentTimeMillis();
        targetPanel1.click();
        contextMenu.waitUntilIsVisible();
        long menuVisible = System.currentTimeMillis();

        return menuVisible - showEventObserving;
    }

    public void assertShowDelayIsInTolerance(long actual, int expected) {
        assertTrue((actual + SHOW_DELAY_TOLERANCE > expected) && (actual - SHOW_DELAY_TOLERANCE < expected));
    }

    public void checkShowDelay(int expected) {
        long actual = getActualShowDelay(expected);
        System.out.println(actual);
        assertShowDelayIsInTolerance(actual, expected);
    }

    public String trimTheRGBAColor(String original) {
        return original.replaceAll("\\s", "");
    }
}
