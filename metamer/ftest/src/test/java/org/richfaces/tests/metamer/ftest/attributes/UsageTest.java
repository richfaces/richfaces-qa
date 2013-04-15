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
package org.richfaces.tests.metamer.ftest.attributes;

import static org.jboss.arquillian.ajocado.Graphene.elementNotVisible;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.tooltipAttributes;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.direction;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.hideEvent;

import java.net.URL;

import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.richTooltip.TooltipModel;
import org.richfaces.ui.common.Positioning;
import org.testng.annotations.Test;


public class UsageTest extends AbstractGrapheneTest {

    JQueryLocator panel = pjq("div[id$=panel]");
    TooltipModel tooltip = new TooltipModel(jq(".rf-tt"), panel);

    @Test
    public void test1() {
        tooltipAttributes.set(direction, Positioning.auto);
    }

    @Test
    public void testHideEvent() {
        tooltipAttributes.set(hideEvent, "mouseup");

        tooltip.recall();

        selenium.mouseUpAt(panel, new Point(5, 5));
        waitGui.until(elementNotVisible.locator(tooltip));
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTooltip/simple.xhtml");
    }
}
