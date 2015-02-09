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
package org.richfaces.tests.metamer.ftest.richAccordion;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.switchable.SwitchType;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13969 extends AbstractWebDriverTest {

    private static final int CONTENT_PADDING = 20;// px
    private final Attributes<AccordionAttributes> accordionAttributes = getAttributes();

    @Page
    private AccordionPage page;

    private SwitchType switchType;

    private int getActualItemContentHeight() {
        return Utils.getLocations(page.getAccordion().advanced().getActiveItem().advanced().getContentElement()).getHeight()
            + CONTENT_PADDING;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordion/simple.xhtml");
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-12574", "https://issues.jboss.org/browse/RF-13969" })
    @UseWithField(field = "switchType", valuesFrom = ValuesFrom.FROM_ENUM, value = "")
    public void testHeightIsComputedCorrectlyWhenSwitched() {
        final int testedHeight = 500;
        int tolerance = 40;// px
        int headersHeight = Utils.getLocations(page.getAccordion().advanced().getAccordionHeaders().get(0)).getHeight();
        headersHeight *= page.getAccordion().getNumberOfAccordionItems();
        int heightBefore = getActualItemContentHeight();

        accordionAttributes.set(AccordionAttributes.switchType, switchType.toString().toLowerCase());
        accordionAttributes.set(AccordionAttributes.height, testedHeight + "px");
        page.getAccordion().advanced().setSwitchType(switchType);

        int heightAfter = getActualItemContentHeight();
        Assert.assertNotEquals(heightAfter, heightBefore, tolerance);
        Assert.assertEquals(heightAfter + headersHeight, testedHeight, tolerance);

        page.getAccordion().switchTo(1);
        Assert.assertEquals(getActualItemContentHeight() + headersHeight, testedHeight, tolerance);
        page.getAccordion().switchTo(0);
        Assert.assertEquals(getActualItemContentHeight() + headersHeight, testedHeight, tolerance);
        page.getAccordion().switchTo(0);
        Assert.assertEquals(getActualItemContentHeight() + headersHeight, testedHeight, tolerance);
        page.getAccordion().switchTo(2);
        Assert.assertEquals(getActualItemContentHeight() + headersHeight, testedHeight, tolerance);
    }
}
