/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010-2016, Red Hat, Inc., and individual contributors
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

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.collapsiblePanel.TextualRichFacesCollapsiblePanel;
import org.richfaces.fragment.inplaceInput.RichFacesInplaceInput;
import org.richfaces.fragment.log.Log.LogEntryLevel;
import org.richfaces.fragment.log.RichFacesLog;
import org.richfaces.tests.metamer.bean.issues.RF13780;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13780 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=input]")
    private RichFacesInplaceInput input;
    @FindBy(css = "[id$=log]")
    private RichFacesLog log;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=panel]")
    private TextualRichFacesCollapsiblePanel panel;

    private void assertNoErrorsInLog() {
        assertEquals(log.getLogEntries().size(), 0, "There should be no errors in a4j:log.");
    }

    private void changeInputTextTo(String text) {
        Graphene.guardAjax(input.type(text)).confirm();
    }

    private void checkOutput(String text) {
        Graphene.waitGui().withMessage("Output should be updated.").until().element(output).text().equalTo(text);
    }

    @Override
    public String getComponentTestPagePath() {
        return "richCollapsiblePanel/rf-13780.xhtml";
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-13988", "https://issues.jboss.org/browse/RF-13993" })
    public void testAjaxWillUpdateComponentsFollowingCollapsedCollapsiblePanel() {
        String text = "1";
        testCollapsedCollapsiblePanelWillNotBeVisited();
        checkOutput(text);

        text = "12";
        changeInputTextTo(text);
        checkOutput(text);
        assertNoErrorsInLog();

        text = "";
        changeInputTextTo(text);
        checkOutput(RF13780.DEFAULT_VALUE);
        assertNoErrorsInLog();
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-13780", "https://issues.jboss.org/browse/RF-13993" })
    public void testCollapsedCollapsiblePanelWillNotBeVisited() {
        log.changeLevel(LogEntryLevel.ERROR);
        assertNoErrorsInLog();
        Graphene.guardAjax(panel).collapse();
        assertNoErrorsInLog();

        changeInputTextTo("1");
        assertNoErrorsInLog();
    }
}
