/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.a4jOutputPanel;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.page.fragments.impl.utils.Event.CLICK;
import static org.richfaces.tests.page.fragments.impl.utils.Event.DBLCLICK;
import static org.richfaces.tests.page.fragments.impl.utils.Event.KEYDOWN;
import static org.richfaces.tests.page.fragments.impl.utils.Event.KEYPRESS;
import static org.richfaces.tests.page.fragments.impl.utils.Event.KEYUP;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEDOWN;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEMOVE;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEOUT;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEOVER;
import static org.richfaces.tests.page.fragments.impl.utils.Event.MOUSEUP;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jOutputPanel/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22733 $
 */
public class TestA4JOutputPanel extends AbstractWebDriverTest {

    private final Attributes<OutputPanelAttributes> outputPanelAttributes = getAttributes();

    private Event[] events = new Event[]{ CLICK, DBLCLICK, KEYDOWN, KEYPRESS, KEYUP, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER,
        MOUSEUP };
    private String[] layouts = new String[]{ "block", "inline" };
    @Inject
    @Use(empty = true)
    private Event event;
    @Inject
    @Use(empty = true)
    private String layout;
    @FindBy(css = "input[id$=button]")
    private WebElement increaseCounterButton;
    @FindBy(css = "div[id$=outputPanel]")
    private GrapheneElement outputDiv;
    @FindBy(css = "span[id$=outputPanel]")
    private GrapheneElement outputSpan;

    @Page
    private MetamerPage metamerPage;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jOutputPanel/simple.xhtml");
    }

    @Test
    @Uses({ @Use(field = "event", value = "events"), @Use(field = "layout", value = "layouts") })
    public void testEvent() {
        WebElement element;

        if ("inline".equals(layout)) {
            outputPanelAttributes.set(OutputPanelAttributes.layout, "inline");
            element = outputSpan;
        } else {
            element = outputDiv;
        }

        testFireEvent(event, element);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11312")
    public void testClick() {
        increaseCounterButton.click();
        Graphene.waitGui().until().element(outputDiv).text().equalTo("1");

        increaseCounterButton.click();
        Graphene.waitGui().until().element(outputDiv).text().equalTo("2");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10555")
    public void testAjaxRendered() {
        outputPanelAttributes.set(OutputPanelAttributes.ajaxRendered, false);

        increaseCounterButton.click();
        increaseCounterButton.click();

        String output = outputDiv.getText();
        assertEquals(output, "0", "Output after two clicks when ajaxRendered is set to false.");

        metamerPage.rerenderAll();
        Graphene.waitGui().until().element(outputDiv).text().equalTo("2");
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        testDir(outputDiv);
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        testHTMLAttribute(outputDiv, outputPanelAttributes, OutputPanelAttributes.lang, "sk");
    }

    @Test
    public void testLayout() {
        assertTrue(outputDiv.isPresent(), "Div should be rendered on the beginning.");
        assertFalse(outputSpan.isPresent(), "Div should be rendered on the beginning.");

        outputPanelAttributes.set(OutputPanelAttributes.layout, "inline");
        assertFalse(outputDiv.isPresent(), "Span should be rendered when inline is set.");
        assertTrue(outputSpan.isPresent(), "Span should be rendered when inline is set.");

        outputPanelAttributes.set(OutputPanelAttributes.layout, "block");
        assertTrue(outputDiv.isPresent(), "Div should be rendered when block is set.");
        assertFalse(outputSpan.isPresent(), "Div should be rendered when block is set.");

        // TODO uncomment as soon as implemented https://issues.jboss.org/browse/RF-7819
        // selenium.click(optionNone);
        // selenium.waitForPageToLoad(TIMEOUT);
        // assertFalse(selenium.isElementPresent(outputDiv), "Span should be rendered when none is set.");
        // assertTrue(selenium.isElementPresent(outputSpan), "Span should be rendered when none is set.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11312")
    @Templates(value = "plain")
    public void testRendered() {
        outputPanelAttributes.set(OutputPanelAttributes.rendered, false);
        assertFalse(outputDiv.isPresent(), "Panel should not be rendered.");

        String timeValue = metamerPage.getRequestTimeElement().getText();
        Graphene.guardAjax(increaseCounterButton).click();
        Graphene.waitGui().withMessage("Page was not updated").until().element(metamerPage.getRequestTimeElement()).text()
            .not().equalTo(timeValue);
        timeValue = metamerPage.getRequestTimeElement().getText();
        Graphene.guardAjax(increaseCounterButton).click();
        Graphene.waitGui().withMessage("Page was not updated").until().element(metamerPage.getRequestTimeElement()).text()
            .not().equalTo(timeValue);

        outputPanelAttributes.set(OutputPanelAttributes.rendered, true);
        assertTrue(outputDiv.isPresent(), "Panel should be rendered.");

        String counter = outputDiv.getText();
        assertEquals(counter, "2", "Counter after two clicks on button.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(outputDiv);
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(outputDiv);
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        testTitle(outputDiv);
    }
}
