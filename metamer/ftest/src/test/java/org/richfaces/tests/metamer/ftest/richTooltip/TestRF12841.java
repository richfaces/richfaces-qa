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
package org.richfaces.tests.metamer.ftest.richTooltip;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.tooltip.TextualRichFacesTooltip;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.richSelect.TestRF14018.JSErrorStorage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12841 extends AbstractWebDriverTest {

    @FindBy(css = "[id$='commandLink']")
    private WebElement commandLink;
    @JavaScript
    private JSErrorStorage jsErrorStorage;
    @FindBy(className = "rf-tt")
    private TextualRichFacesTooltip tooltip;

    @Override
    public String getComponentTestPagePath() {
        return "richTooltip/rf-12841.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12841")
    public void testDestroyedTooltipWillNotThrowJavaScriptErrors() {
        // assert no JS errors in console
        assertTrue(jsErrorStorage.getMessages().isEmpty());
        // this was fixed: two clicks should cause JS error
        Graphene.guardAjax(commandLink).click();
        Graphene.guardAjax(commandLink).click();
        // tooltip should appear after the delay
        tooltip.advanced().waitUntilTooltipIsVisible().withTimeout(6, TimeUnit.SECONDS).perform();
        // check tooltip's text
        assertEquals(tooltip.advanced().getRootElement().getText(), "expanded");
        // assert no JS errors in console
        assertTrue(jsErrorStorage.getMessages().isEmpty());

        // try three clicks
        Graphene.guardAjax(commandLink).click();
        Graphene.guardAjax(commandLink).click();
        Graphene.guardAjax(commandLink).click();
        // tooltip should appear after the delay
        tooltip.advanced().waitUntilTooltipIsVisible().withTimeout(6, TimeUnit.SECONDS).perform();
        // check tooltip's text
        assertEquals(tooltip.advanced().getRootElement().getText(), "collapsed");
        // assert no JS errors in console
        assertTrue(jsErrorStorage.getMessages().isEmpty());
    }
}
