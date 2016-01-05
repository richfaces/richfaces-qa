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
package org.richfaces.tests.metamer.ftest.richTooltip;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.tooltip.TextualRichFacesTooltip;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StopWatch;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14136 extends AbstractWebDriverTest {

    private static final int HIDE_DELAY = 1500;
    private static final int TOLERANCE = 800;

    @FindBy(css = "[id$='imageWithTooltip']")
    private WebElement imageWithTooltip;
    @FindBy(className = "rf-tt")
    private TextualRichFacesTooltip tooltip;

    @Override
    public String getComponentTestPagePath() {
        return "richTooltip/rf-14136.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14136")
    public void testTooltipHideDelayOnGraphicImage() {
        // show tooltip
        new Actions(driver)
            .triggerEventByWD(Event.MOUSEOVER, imageWithTooltip)
            .perform();
        tooltip.advanced().waitUntilTooltipIsVisible().perform();

        assertEquals(StopWatch.watchTimeSpentInAction(new Action() {
            @Override
            public void perform() {
                // hide tooltip
                new Actions(driver)
                    .triggerEventByWD(Event.MOUSEOUT, imageWithTooltip)
                    .perform();
                tooltip.advanced().waitUntilTooltipIsNotVisible().withTimeout(HIDE_DELAY + TOLERANCE, TimeUnit.MILLISECONDS).perform();
            }
        }).inMillis().longValue(), HIDE_DELAY, TOLERANCE);
    }
}
