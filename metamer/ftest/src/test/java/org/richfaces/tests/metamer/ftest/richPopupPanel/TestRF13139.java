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
package org.richfaces.tests.metamer.ftest.richPopupPanel;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.status.Status;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.richPopupPanel.TestPopupPanel.TestedPopupPanel;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13139 extends AbstractWebDriverTest {

    @FindBy(css = "a[id$=openPanelLink]")
    private WebElement openPanelLink;
    @FindBy(css = "div.rf-pp-cntr[id$=popupPanel_container]")
    private List<TestedPopupPanel> panels;

    private void check() {
        // click the open button link twice immediately after each other
        Utils.jQ("click().click()", openPanelLink);
        waiting(2000);
        // wait for the requests are completed
        getMetamerPage().getStatus().advanced().waitUntilStatusStateChanges(Status.StatusState.STOP).perform();
        assertEquals(panels.size(), 1, "Only one panel should be rendered!");
    }

    @Override
    public String getComponentTestPagePath() {
        return "richPopupPanel/rf-13139.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13139")
    public void testPanelIsNotOpenedTwice() {
        check();
        // close the panel
        TestedPopupPanel pp = panels.get(0);
        pp.getBodyContent().getHideLinkElement().click();
        pp.advanced().waitUntilPopupIsNotVisible().perform();
        // try again
        check();
    }

}
