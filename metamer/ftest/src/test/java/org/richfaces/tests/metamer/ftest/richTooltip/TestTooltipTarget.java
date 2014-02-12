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
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.hideEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.showEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.target;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Event;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * Test for @target attribute on page faces/components/richTooltip/targetting.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTooltipTarget extends AbstractWebDriverTest {

    private final Attributes<TooltipAttributes> tooltipAttributes = getAttributes();

    @Page
    private TooltipPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTooltip/targetting.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11370")
    public void testTarget() {
        tooltipAttributes.set(showEvent, "mouseover");
        tooltipAttributes.set(hideEvent, "mouseout");
        page.getTooltip().advanced().setupShowEvent(Event.MOUSEOVER);
        page.getTooltip().advanced().setupHideEvent(Event.MOUSEOUT);
        for (WebElement panel : Lists.newArrayList(page.getPanel(), page.getPanelJSFDiv(), page.getPanelRegularDiv())) {
            tooltipAttributes.set(target, panel.getAttribute("id"));
            page.getTooltip().show(panel);
            page.getTooltip().hide();
        }
    }
}
