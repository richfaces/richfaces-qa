/******************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************/
package org.richfaces.tests.metamer.ftest.richChart;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

public class ChartSimplePage extends MetamerPage {

    @FindByJQuery(value = "span[id$='msg']")
    private WebElement serverSideMessage;

    @FindByJQuery(value = "span[id$='clickInfo']")
    private WebElement plotClickMessage;

    @FindByJQuery(value = "span[id$='hoverInfo']")
    private WebElement plotHoverMessage;

    @FindByJQuery(value = "div[id$='chart']")
    private WebElement chartElement;

    @FindByJQuery(value = "div[id$='chart'] div.chart-title")
    private WebElement chartTitleElement;

    public WebElement getChartTitleElement() {
        return chartTitleElement;
    }

    public WebElement getServerSideMessage() {
        return serverSideMessage;
    }

    public WebElement getPlotClickMessage() {
        return plotClickMessage;
    }

    public WebElement getPlotHoverMessage() {
        return plotHoverMessage;
    }

    public WebElement getChartElement() {
        return chartElement;
    }
}
