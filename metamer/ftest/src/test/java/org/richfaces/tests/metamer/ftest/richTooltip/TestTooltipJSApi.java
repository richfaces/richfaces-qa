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
package org.richfaces.tests.metamer.ftest.richTooltip;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.tooltip.TextualRichFacesTooltip;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

public class TestTooltipJSApi extends AbstractWebDriverTest {

    @Page
    private TooltipPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTooltip/simple.xhtml");
    }

    @Test
    public void testJsAPIbyClick() {
        page.getJsAPIshowClick().click();
        tooltip().advanced().waitUntilTooltipIsVisible().perform();

        page.getJsAPIhideClick().click();
        tooltip().advanced().waitUntilTooltipIsNotVisible().perform();
    }

    @Test
    public void testJsAPIbyMouseOver() {
        new Actions(driver).moveToElement(page.getJsAPIshowMouseOver()).build().perform();
        tooltip().advanced().waitUntilTooltipIsVisible().perform();

        new Actions(driver).moveToElement(page.getJsAPIhideMouseOver()).build().perform();
        tooltip().advanced().waitUntilTooltipIsNotVisible().perform();
    }

    private TextualRichFacesTooltip tooltip() {
        return page.getTooltip();
    }
}
