/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Ajocado.elementVisible;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.tooltipAttributes;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.hideEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.showEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.target;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Test for @target attribute on page faces/components/richTooltip/targetting.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 *
 * @version $Revision$
 */
public class TestTooltipTarget extends AbstractAjocadoTest {

    // private TooltipAttributes attributes = new TooltipAttributes();
    private JQueryLocator panel1 = pjq("div[id$=regular-div]");
    private JQueryLocator panel2 = pjq("div[id$=jsf-div]");
    private JQueryLocator panel3 = pjq("div[id$=panel_body]");

    TooltipModel tooltip1 = new TooltipModel(jq(".rf-tt"), panel1);
    TooltipModel tooltip2 = new TooltipModel(jq(".rf-tt"), panel2);
    TooltipModel tooltip3 = new TooltipModel(jq(".rf-tt"), panel3);

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
        tooltip2.recall();
        waitGui.until(elementVisible.locator(tooltip2));

        // 3. default target
        tooltipAttributes.set(target, "panel");
        tooltip3.recall();
        waitGui.until(elementVisible.locator(tooltip3));
    }

    @Test(groups = { "4.3" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11370")
    public void testTargetWithRegularDiv() {
        tooltipAttributes.set(target, "regular-div");
        tooltip1.recall();
        waitGui.until(elementVisible.locator(tooltip1));
    }
}
