/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
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
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.statusAttributes;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * Tests for a4jStatus JS API. Tests are done via testFireEvent
 *
 * @author <a href="mailto:manovotn@redhat.com"> Matej Novotny </a>
 *
 */
public class TestStatusJSApi extends AbstractWebDriverTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/withoutFacets.xhtml");
    }

    @FindByJQuery("span[id$=status]")
    private WebElement statusMessage;

    @FindByJQuery("span[id$=status] span.rf-st-start")
    private WebElement workingElement;

    @FindByJQuery("span[id$=status] span.rf-st-stop")
    private WebElement stopElement;

    @FindByJQuery("span[id$=status] span.rf-st-error")
    private WebElement errorElement;

    @FindByJQuery("input[id$=invalidateSessionButton]")
    private WebElement invalidateSession;

    private void setAndAssertInitialState() {
        invalidateSession.click();
        assertNotVisible(workingElement, "WebElement with text " + workingElement.getText() + " should not be visible");
        assertNotVisible(errorElement, "WebElement with text " + errorElement.getText() + " should not be visible");
        assertVisible(stopElement, "WebElement with text " + stopElement.getText() + " should be visible");
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsStart() {
        setAndAssertInitialState();
        testFireEvent(statusAttributes, StatusAttributes.onstart, new Action() {
            @Override
            public void perform() {
                executeJS("RichFaces.$('" + statusMessage.getAttribute("id") + "').start()");
            }
        });

    }

    @Test
    @Templates(value = { "plain" })
    public void testJsStop() {
        setAndAssertInitialState();
        testFireEvent(statusAttributes, StatusAttributes.onstop, new Action() {
            @Override
            public void perform() {
                executeJS("RichFaces.$('" + statusMessage.getAttribute("id") + "').stop()");
            }
        });
    }

    @Test
    @Templates(value = { "plain" })
    public void testJsError() {
        setAndAssertInitialState();
        testFireEvent(statusAttributes, StatusAttributes.onerror, new Action() {
            @Override
            public void perform() {
                executeJS("RichFaces.$('" + statusMessage.getAttribute("id") + "').error()");
            }
        });
    }
}
