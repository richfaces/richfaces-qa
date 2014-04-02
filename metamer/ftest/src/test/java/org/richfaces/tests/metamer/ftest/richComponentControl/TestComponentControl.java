/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richComponentControl;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.6.Final
 */
@Templates("plain")
public class TestComponentControl extends AbstractWebDriverTest {

    private final Attributes<ComponentControlAttributes> componentControlAttributes = getAttributes();

    @FindBy(css = "input[id$=button]")
    private WebElement button;

    @FindBy(css = "span.rf-ds[id$=scroller]")
    private RichFacesDataScroller dataScroller;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richComponentControl/simple.xhtml");
    }

    @Test
    public void testOnbeforeoperation() {
        testFireEvent(componentControlAttributes, ComponentControlAttributes.onbeforeoperation, new Action() {
            @Override
            public void perform() {
                button.click();
            }
        });
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9306")
    public void testOperation() {
        // initially, the scroller is on 3rd page and operation is set to "previous"
        MetamerPage.waitRequest(button, WaitRequestType.XHR).click();
        Assert.assertEquals(dataScroller.getActivePageNumber(), 2);
        MetamerPage.waitRequest(button, WaitRequestType.XHR).click();
        Assert.assertEquals(dataScroller.getActivePageNumber(), 1);

        componentControlAttributes.set(ComponentControlAttributes.operation, "last");
        MetamerPage.waitRequest(button, WaitRequestType.XHR).click();
        Assert.assertEquals(dataScroller.getActivePageNumber(), 20);
    }

    @Test
    public void testSelector() {
        componentControlAttributes.set(ComponentControlAttributes.selector, "span.rf-ds[id$=scroller]");
        componentControlAttributes.set(ComponentControlAttributes.target, "");

        testOperation();
    }
}
