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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.status.Status.StatusState;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestFacets extends AbstractStatusTest {

    @FindBy(css = "input[id$=facetErrorTextInput]")
    private TextInputComponentImpl facetErrorTextInput;
    @FindBy(css = "input[id$=facetStartTextInput]")
    private TextInputComponentImpl facetStartTextInput;
    @FindBy(css = "input[id$=facetStopTextInput]")
    private TextInputComponentImpl facetStopTextInput;
    @FindBy(css = "input[id$=applyFacets]")
    private WebElement applyFacetsButton;

    private static final String INCREMENT = "*";

    public String getErrorText() {
        return facetErrorTextInput.getStringValue();
    }

    public String getStartText() {
        return facetStartTextInput.getStringValue();
    }

    public String getStopText() {
        return facetStopTextInput.getStringValue();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/facets.xhtml");
    }

    @Test
    public void testInterleavedChangingOfFacets() {
        for (int i = 0; i < 9; i++) {
            WebElement button = (i % 2 == 0) ? getButton1() : getButtonError();
            StatusState state = StatusState.values()[i % StatusState.values().length];
            testChangingFacet(button, state);
        }
    }

    private void testChangingFacet(WebElement button, StatusState state) {
        switch (state) {
            case START:
                facetStartTextInput.sendKeys(INCREMENT);
                break;
            case STOP:
                facetStopTextInput.sendKeys(INCREMENT);
                break;
            case ERROR:
                facetErrorTextInput.sendKeys(INCREMENT);
                break;
            default:
                throw new IllegalStateException();
        }
        MetamerPage.requestTimeChangesWaiting(applyFacetsButton).click();

        final String startText = getStartText();
        final String stopText = button.getAttribute("id").equals(getButtonError().getAttribute("id")) ? getErrorText() : getStopText();

        checkStatus(button, startText, stopText);
    }
}
