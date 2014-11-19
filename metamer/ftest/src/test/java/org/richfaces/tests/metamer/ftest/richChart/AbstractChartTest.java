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

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.testng.AssertJUnit.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Abstract class for chart tests. Allows to keep common tests altogether while like chart can have extra tests for zooming etc.
 */
public abstract class AbstractChartTest extends AbstractWebDriverTest {

    private final Attributes<ChartAttributes> chartAttributes = getAttributes();

    @Page
    private ChartSimplePage page;

    public void testRendered() {
        assertPresent(page.getChartElement(), "Chart should be rendered.");
        chartAttributes.set(ChartAttributes.rendered, false);
        assertNotPresent(page.getChartElement(), "Chart should not be rendered.");
    }

    public void testTitle() {
        // assert title is present
        assertPresent(page.getChartTitleElement(), "Title should be rendered");

        // change title and assert
        final String newTitle = "SomeNewAwesomeAndSlightlyLongTitle";
        chartAttributes.set(ChartAttributes.title, newTitle);
        waitAjax(driver).until().element(page.getChartTitleElement()).text().equalTo(newTitle);
    }

    public void testStyleClass() {
        final String newStyleClass = "newStyleClass";
        chartAttributes.set(ChartAttributes.styleClass, newStyleClass);
        assertTrue(page.getChartElement().getAttribute("class").contains(newStyleClass));
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(chartAttributes, ChartAttributes.onmouseout, new Action() {
            @Override
            public void perform() {
                // move to chart element
                new Actions(driver).moveToElement(page.getChartElement()).build().perform();
                // move out of chart element area
                new Actions(driver).moveToElement(page.getPlotClickMessage()).build().perform();
            }
        });
    }
}
