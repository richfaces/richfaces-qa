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

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.tooltip.TextualRichFacesTooltip;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTooltipDefaultZindex extends AbstractWebDriverTest {

    private final Attributes<TooltipAttributes> tooltipAttributes = getAttributes();

    @Page
    private TooltipPage page;

    @Override
    public String getComponentTestPagePath() {
        return "richTooltip/zindex.xhtml";
    }

    @BeforeMethod
    public void setupAttributes() {
        tooltipAttributes.set(TooltipAttributes.hideEvent, "mouseout");
        tooltipAttributes.set(TooltipAttributes.showEvent, "click");
        tooltip().advanced().setHideEvent(Event.MOUSEOUT);
        tooltip().advanced().setShowEvent(Event.CLICK);
    }

    @Test
    @Templates(value = "plain")
    public void testZindexDefaultValue() {
        String defaultZindex = "1000";
        assertEquals(tooltip().show().advanced().getTooltipElement().getCssValue("z-index"), defaultZindex);
    }

    private TextualRichFacesTooltip tooltip() {
        return page.getTooltip();
    }

}
