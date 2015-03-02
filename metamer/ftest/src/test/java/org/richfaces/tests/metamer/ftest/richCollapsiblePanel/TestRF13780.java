/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.tests.metamer.ftest.richCollapsiblePanel;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.collapsiblePanel.TextualRichFacesCollapsiblePanel;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.fragment.log.Log.LogEntryLevel;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13780 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=input1]")
    private RichFacesInplaceInput input1;
    @FindBy(css = "[id$=input2]")
    private RichFacesInplaceInput input2;
    @FindBy(css = "[id$=log]")
    private RichFacesLog log;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=panel]")
    private TextualRichFacesCollapsiblePanel panel;

    private void checkInput1(String text) {
        Graphene.waitGui().withMessage("Input 1 should be updated.").until().element(input1.advanced().getRootElement()).text().equalTo(text);
    }

    private void checkInput2(String text) {
        Graphene.waitGui().withMessage("Input 2 should be updated.").until().element(input2.advanced().getRootElement()).text().equalTo(text);
    }

    private void checkOutput(String text) {
        Graphene.waitGui().withMessage("Output should be updated.").until().element(output).text().equalTo(text);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsiblePanel/rf-13780.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13988")
    public void testAjaxWillUpdateComponentsFollowingCollapsedCollapsiblePanel() {
        String text = "1";
        testCollapsedCollapsiblePanelWillNotBeVisited();
        checkOutput(text);
        checkInput2(text);

        text += "2";
        Graphene.guardAjax(input2.type(text)).confirm();
        checkOutput(text);
        checkInput1(text);
        assertEquals(log.getLogEntries().size(), 0, "There should be no errors in a4j:log.");

        text += "3";
        Graphene.guardAjax(input1.type(text)).confirm();
        checkOutput(text);
        checkInput2(text);
        assertEquals(log.getLogEntries().size(), 0, "There should be no errors in a4j:log.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13780")
    public void testCollapsedCollapsiblePanelWillNotBeVisited() {
        log.changeLevel(LogEntryLevel.ERROR);
        assertEquals(log.getLogEntries().size(), 0, "There should be no errors in a4j:log.");
        Graphene.guardAjax(panel).collapse();
        assertEquals(log.getLogEntries().size(), 0, "There should be no errors in a4j:log.");

        Graphene.guardAjax(input1.type("1")).confirm();
        assertEquals(log.getLogEntries().size(), 0, "There should be no errors in a4j:log.");
    }
}
