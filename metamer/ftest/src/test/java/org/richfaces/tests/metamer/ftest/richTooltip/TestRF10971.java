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
package org.richfaces.tests.metamer.ftest.richTooltip;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.hideEvent;
import static org.richfaces.tests.metamer.ftest.richTooltip.TooltipAttributes.showEvent;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.tooltip.TextualRichFacesTooltip;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF10971 extends AbstractWebDriverTest {

    private final Attributes<TooltipAttributes> tooltipAttributes = getAttributes();
    @FindByJQuery(value = ".rf-edt-c-cnt:contains('Dover'):last")
    private TextualRichFacesTooltip tooltipCapitalDover;
    @FindByJQuery(value = ".rf-edt-c-cnt:contains('Sacramento'):last")
    private TextualRichFacesTooltip tooltipCapitalSacramento;
    @FindByJQuery(".rf-edt-c-cnt:contains('Arkansas'):last")
    private TextualRichFacesTooltip tooltipStateArkansas;
    @FindByJQuery(".rf-edt-c-cnt:contains('Hawaii'):last")
    private TextualRichFacesTooltip tooltipStateHawaii;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTooltip/rf-10971.xhtml");
    }

    private void setupTooltips() {
        // set show/hide events to click/dblclick to stabilize tests
        tooltipAttributes.set(showEvent, "click");
        tooltipAttributes.set(hideEvent, "dblclick");

        tooltipCapitalDover.advanced().setShowEvent(Event.CLICK);
        tooltipCapitalDover.advanced().setHideEvent(Event.DBLCLICK);
        tooltipCapitalSacramento.advanced().setShowEvent(Event.CLICK);
        tooltipCapitalSacramento.advanced().setHideEvent(Event.DBLCLICK);
        tooltipStateArkansas.advanced().setShowEvent(Event.CLICK);
        tooltipStateArkansas.advanced().setHideEvent(Event.DBLCLICK);
        tooltipStateHawaii.advanced().setShowEvent(Event.CLICK);
        tooltipStateHawaii.advanced().setHideEvent(Event.DBLCLICK);

        // set some offset so the tooltip will not block triggering the event on element
        tooltipAttributes.set(TooltipAttributes.horizontalOffset, 100);
        tooltipAttributes.set(TooltipAttributes.verticalOffset, 100);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10971")
    public void testTooltipWillShowInEDT() {
        setupTooltips();

        tooltipCapitalDover.show();
        assertEquals(tooltipCapitalDover.getContentText(), "Tooltip of Dover");
        tooltipCapitalDover.hide();

        tooltipCapitalSacramento.show();
        assertEquals(tooltipCapitalSacramento.getContentText(), "Tooltip of Sacramento");
        tooltipCapitalSacramento.hide();

        tooltipStateArkansas.show();
        assertEquals(tooltipStateArkansas.getContentText(), "Tooltip of Arkansas");
        tooltipStateArkansas.hide();

        tooltipStateHawaii.show();
        assertEquals(tooltipStateHawaii.getContentText(), "Tooltip of Hawaii");
        tooltipStateHawaii.hide();
    }
}
