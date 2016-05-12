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
package org.richfaces.tests.metamer.ftest.richChart;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestChartJSAPI extends AbstractChartTest {

    @JavaScript
    private ChartJs chartJS;
    @FindBy(css = "[id$=getPlotObject]")
    private WebElement getMaxButton;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=resetZoom]")
    private WebElement resetZoomButton;

    private void getAndCheckMaxDisplayedXAxisValue(String toMatchValue) {
        // get max displayed year value (value will be saved to output)
        getMetamerPage().performJSClickOnButton(getMaxButton, WaitRequestType.NONE);
        // check
        Graphene.waitGui().until().element(output).value().matches(toMatchValue);
    }

    @Override
    public String getComponentTestPagePath() {
        return "richChart/simpleLine.xhtml";
    }

    @Test
    public void testResetZoomAndGetPlotObject() {
        // check max displayed x value
        getAndCheckMaxDisplayedXAxisValue("2000");
        // zoom some area
        Graphene.guardAjax(new Actions(driver)
            .moveToElement(page.getChartCanvas(), chartJS.pointXPos(page.getChartId(), 1, 1), chartJS.pointYPos(page.getChartId(), 1, 1))
            .clickAndHold()
            .moveToElement(page.getChartCanvas(), chartJS.pointXPos(page.getChartId(), 2, 2), chartJS.pointYPos(page.getChartId(), 2, 2)).release()
        ).perform();

        // check max displayed x value
        getAndCheckMaxDisplayedXAxisValue("^199.*");

        // reset zoom
        getMetamerPage().performJSClickOnButton(resetZoomButton, WaitRequestType.NONE);
        // check max displayed x value
        getAndCheckMaxDisplayedXAxisValue("2000");
    }
}
