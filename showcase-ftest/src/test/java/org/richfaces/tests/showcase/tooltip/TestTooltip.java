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
package org.richfaces.tests.showcase.tooltip;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.showcase.AbstractWebDriverTest;
import org.richfaces.tests.showcase.tooltip.page.TooltipPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTooltip extends AbstractWebDriverTest {

    private static final String TOOLTIP_TEXT_AJAX_CLICK = "This tool-tip content was rendered on server";
    private static final String TOOLTIP_TEXT_AJAX = "This tool-tip content was rendered on the server";
    private static final String TOOLTIP_CLIENT = "This tool-tip content was pre-rendered to the page.";
    private static final String TOOLTIP_CLIENT_WITH_DELAY = "This tool-tip content is also pre-rendered to the page.";

    @Page
    private TooltipPage page;

    @ArquillianResource
    private Actions actions;

    @Test
    public void testClientTooltip() {
        scrollToElementSoItIsFullyVisible(page.clientTooltipActivatingArea);
        actions.moveToElement(page.clientTooltipActivatingArea).build().perform();
        waitForTooltipText(TOOLTIP_CLIENT);
    }

    @Test
    public void testClientWithDelayTooltip() {
        scrollToElementSoItIsFullyVisible(page.clientWithDelayTooltipActivatingArea);
        actions.moveToElement(page.clientWithDelayTooltipActivatingArea).build().perform();
        waitForTooltipText(TOOLTIP_CLIENT_WITH_DELAY);
    }

    @Test
    public void testAjaxTooltip() {
        scrollToElementSoItIsFullyVisible(page.ajaxTooltipActivatingArea);
        actions.moveToElement(page.ajaxTooltipActivatingArea).build().perform();
        waitForTooltipText(TOOLTIP_TEXT_AJAX);
    }

    @Test
    public void testAjaxClickTooltip() {
        scrollToElementSoItIsFullyVisible(page.ajaxClickTooltipActivatingArea);
        actions.moveToElement(page.ajaxClickTooltipActivatingArea).click().build().perform();
        waitForTooltipText(TOOLTIP_TEXT_AJAX_CLICK);
    }

    private void scrollToElementSoItIsFullyVisible(WebElement element) {
        Point elementLocation = element.getLocation();
        Dimension elementDimension = element.getSize();
        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(" + elementLocation.getX() + elementDimension.width + ", " + elementLocation.getY() + ")");
    }

    private void waitForTooltipText(String message) {
        waitAjax(webDriver).withTimeout(5, TimeUnit.SECONDS)
                .until("The tool tip text is different!")
                .element(page.tooltip)
                .text()
                .contains(message);
    }
}
