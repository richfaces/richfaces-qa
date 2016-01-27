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

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.log.Log.LogEntryLevel;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.fragment.status.Status.StatusState;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
public class TestRF12661 extends AbstractWebDriverTest {

    @FindBy(css = "[id$='log']")
    private RichFacesLog log;
    @FindBy(css = ".rf-p[id$='panelWithTooltip']")
    private WebElement panelWithTooltip;
    @FindBy(css = "[id$='setNullButton']")
    private WebElement setNullButton;
    @FindBy(className = "rf-tt")
    private WebElement tooltip;

    @Override
    public String getComponentTestPagePath() {
        return "richTooltip/rf-12661.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12661")
    public void testTooltipWithNullValue() {
        log.changeLevel(LogEntryLevel.ERROR);
        // scroll to panel with tooltip
        jsUtils.scrollToView(panelWithTooltip);
        // show tooltip
        panelWithTooltip.click();
        Graphene.waitAjax().until().element(tooltip).is().visible();
        // check initial content text
        assertEquals(tooltip.getText(), "initial value");
        // hide tooltip
        new Actions(driver).doubleClick(panelWithTooltip).perform();

        // set tooltip value to null, HERE starts the issue
        Graphene.guardAjax(setNullButton).click();
        // there should be no error entries in log
        assertEquals(log.getLogEntries().size(), 0);
        // the status should be in 'STOP' state
        assertEquals(getMetamerPage().getStatus().getStatusState(), StatusState.STOP);
        // scroll to panel with tooltip
        jsUtils.scrollToView(panelWithTooltip);
        // tooltip can be displayed
        panelWithTooltip.click();
        Graphene.waitAjax().until().element(tooltip).is().visible();        // tooltip's content should be empty
        assertEquals(tooltip.getText(), "");
        // tooltip can be hidden
        new Actions(driver).doubleClick(panelWithTooltip).perform();
    }
}
