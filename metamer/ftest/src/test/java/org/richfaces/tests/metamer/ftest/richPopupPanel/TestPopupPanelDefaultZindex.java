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
package org.richfaces.tests.metamer.ftest.richPopupPanel;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richPopupPanel.TestPopupPanel.TestedPopupPanel;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPopupPanelDefaultZindex extends AbstractWebDriverTest {

    private final Attributes<PopupPanelAttributes> popupPanelAttributes = getAttributes();

    @FindBy(css = "input[id$=openPanelButton]")
    private WebElement openButton;
    @FindBy(css = "div.rf-pp-cntr[id$=popupPanel_container]")
    private TestedPopupPanel panel;

    private void checkCssValueOf(String cssValue, double value, WebElement element) {
        int tolerance = 5;
        assertEquals(Double.valueOf(element.getCssValue(cssValue).replace("px", "")),
            value,
            tolerance,
            cssValue + " of the panel");
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPopupPanel/zindex.xhtml");
    }

    private void openPopupPanel() {
        openButton.click();
        panel.advanced().waitUntilPopupIsVisible().perform();
    }

    @Test
    @Templates(value = "plain")
    public void testZindexDefaultValue() {
        int defaultZindex = 100;
        openPopupPanel();
        checkCssValueOf("z-index", defaultZindex, panel.advanced().getRootElement());
    }
}
