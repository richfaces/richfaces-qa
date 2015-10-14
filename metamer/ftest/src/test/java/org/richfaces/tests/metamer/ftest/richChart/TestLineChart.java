/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richChart;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

public class TestLineChart extends AbstractChartTest {

    @JavaScript
    protected ChartJs chartJS;

    @Override
    public String getComponentTestPagePath() {
        return "richChart/simpleLine.xhtml";
    }

    @Test
    @Templates("plain")
    public void testOnmouseout() {
        super.testOnmouseout();
    }

    @Test
    @Templates("plain")
    @CoversAttributes({ "onplotclick", "plotClickListener" })
    public void testOnplotclick() {
        final String plotClick = "Point with index 0 within series 0 was clicked";

        // retrieve plot offset in canvas via JS (coordinates differ based on browser)
        int x = chartJS.pointXPos(page.getChartId(), 0, 0);
        int y = chartJS.pointYPos(page.getChartId(), 0, 0);

        new Actions(driver).moveToElement(page.getChartCanvas(), x, y).click().build().perform();
        waitAjax(driver).until().element(page.getPlotClickMessage()).text().contains(plotClick);
        // plotClickListener
        waitAjax(driver).until().element(page.getServerSideMessage()).text().contains(plotClick);
    }

    @Test
    @Templates("plain")
    @CoversAttributes("onplothover")
    public void testOnplothover() {
        // retrieve plot offset in canvas via JS (coordinates differ based on browser)
        int x = chartJS.pointXPos(page.getChartId(), 0, 0);
        int y = chartJS.pointYPos(page.getChartId(), 0, 0);

        // now move over the plot and wait until the text changes
        new Actions(driver).moveToElement(page.getChartCanvas(), x, y).build().perform();
        waitAjax(driver).until().element(page.getPlotHoverMessage()).text().contains("USA [1990,19.1]");
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-14176")
    @Templates("plain")
    public void testStyle() {
        super.testStyle();
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        super.testStyleClass();
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        super.testTitle();
    }

    @Test
    @Templates("plain")
    @CoversAttributes("zoom")
    public void testZoom() {
        // zoom is only available in line chart

        // get plot coordinates on screen before zooming
        int xBeforeZoom = chartJS.pointXPos(page.getChartId(), 0, 0);

        // zoom some area
        new Actions(driver)
            .moveToElement(page.getChartCanvas(), chartJS.pointXPos(page.getChartId(), 1, 1), chartJS.pointYPos(page.getChartId(), 1, 1))
            .clickAndHold()
            .moveToElement(page.getChartCanvas(), chartJS.pointXPos(page.getChartId(), 2, 2), chartJS.pointYPos(page.getChartId(), 2, 2)).release()
            .build().perform();
        new Actions(driver).clickAndHold().moveByOffset(200, 50).release().build().perform();

        // get the coordinates after zooming, they should be different
        int xAfterZoom = chartJS.pointXPos(page.getChartId(), 0, 0);

        // assert coordinates do not equal after zoom, only x axis is zoomed
        assertNotEquals(xBeforeZoom, xAfterZoom);

        // perform zoom reset by JS
        chartJS.resetZoom(page.getChartId());

        // get the coordinates again
        int xZoomReset = chartJS.pointXPos(page.getChartId(), 0, 0);

        // assert that this equals initial state, only x axis is zoomed
        assertEquals(xBeforeZoom, xZoomReset);
    }
}
