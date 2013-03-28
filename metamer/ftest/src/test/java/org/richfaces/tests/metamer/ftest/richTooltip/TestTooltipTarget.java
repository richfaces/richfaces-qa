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
package org.richfaces.tests.metamer.ftest.richTooltip;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.hideEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.showEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.target;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.tooltipAttributes;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Test for @target attribute on page faces/components/richTooltip/targetting.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 *
 * @version $Revision$
 */
public class TestTooltipTarget extends AbstractWebDriverTest {

    @Page
    TooltipPage page;

    // private TooltipAttributes attributes = new TooltipAttributes();
    // private JQueryLocator panel1 = pjq("div[id$=regular-div]");
    // private JQueryLocator panel2 = pjq("div[id$=jsf-div]");
    // private JQueryLocator panel3 = pjq("div[id$=panel_body]");

    // TooltipModel tooltip1 = new TooltipModel(jq(".rf-tt"), panel1);
    // TooltipModel tooltip2 = new TooltipModel(jq(".rf-tt"), panel2);
    // TooltipModel tooltip3 = new TooltipModel(jq(".rf-tt"), panel3);

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTooltip/targetting.xhtml");
    }

    @BeforeMethod
    public void setupAttributes() {
        tooltipAttributes.set(showEvent, "mouseover");
        tooltipAttributes.set(hideEvent, "mouseout");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11370")
    public void testTarget() {
        // 2. target
        tooltipAttributes.set(target, "jsf-div");
        page.tooltip.recall(page.panel2);
        // waitGui.until(elementVisible.locator(page.panel2.tooltip));
        Graphene.waitGui().until(Graphene.element(page.tooltip.root).isVisible());

        // 3. default target
        tooltipAttributes.set(target, "panel");
        page.tooltip.recall(page.panel3);
        // waitGui.until(elementVisible.locator(page.panel3.tooltip));
        Graphene.waitGui().until(Graphene.element(page.tooltip.root).isVisible());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11370")
    public void testTargetWithRegularDiv() {
        tooltipAttributes.set(target, "regular-div");
        page.tooltip.recall(page.panel1);
        // waitGui.until(elementVisible.locator(page.panel1.tooltip));
        Graphene.waitGui().until(Graphene.element(page.tooltip.root).isVisible());
    }
}
