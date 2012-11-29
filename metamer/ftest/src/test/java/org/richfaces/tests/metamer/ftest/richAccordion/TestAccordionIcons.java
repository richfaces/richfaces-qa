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
package org.richfaces.tests.metamer.ftest.richAccordion;

import java.net.URL;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsCheckerWebdriver;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.accordionAttributes;
import static org.testng.Assert.assertFalse;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richAccordion/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAccordionIcons extends AbstractWebDriverTest {

    @Page
    private AccordionPage page;

    private final String leftIcon = "div[id$=item%s] td.rf-ac-itm-ico";
    private final String rightIcon = "div[id$=item%s] td.rf-ac-itm-exp-ico";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordion/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Accordion", "Simple");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10352")
    public void testItemActiveLeftIcon() {
        By image = By.cssSelector(String.format(leftIcon, "1") + " img");

        // icon=null
        assertFalse(Graphene.element(page.getLeftActiveIcon()).isPresent().apply(driver), "Left icon of active item should not be present on the page.");

        verifyStandardIcons(AccordionAttributes.itemActiveLeftIcon, page.getLeftActiveIcon(), image, "");
    }

    @Test
    public void testItemActiveRightIcon() {
        By image = By.cssSelector(String.format(rightIcon, "1") + " img");

        // icon=null
        assertFalse(Graphene.element(page.getRightActiveIcon()).isPresent().apply(driver), "Right icon of active item should not be present on the page.");

        verifyStandardIcons(AccordionAttributes.itemActiveRightIcon, page.getRightActiveIcon(), image, "");
    }

    @Test
    public void testItemDisabledLeftIcon() {
        By image = By.cssSelector(String.format(leftIcon, "4") + " img");

        verifyStandardIcons(AccordionAttributes.itemDisabledLeftIcon, page.getLeftDisabledIcon(), image, "-dis");
    }

    @Test
    public void testItemDisabledRightIcon() {
        By image = By.cssSelector(String.format(rightIcon, "4") + " img");

        verifyStandardIcons(AccordionAttributes.itemDisabledRightIcon, page.getRightDisabledIcon(), image, "-dis");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10352")
    public void testItemInactiveLeftIcon() {
        By image = By.cssSelector(String.format(leftIcon, "3") + " img");

        verifyStandardIcons(AccordionAttributes.itemInactiveLeftIcon, page.getLeftInactiveIcon(), image, "");
    }

    @Test
    public void testItemInactiveRightIcon() {
        By image = By.cssSelector(String.format(rightIcon, "3") + " img");

        verifyStandardIcons(AccordionAttributes.itemInactiveRightIcon, page.getRightInactiveIcon(), image, "");
    }

    private void verifyStandardIcons(AccordionAttributes attribute, WebElement icon, By image, String classSuffix) {
        IconsCheckerWebdriver checker = new IconsCheckerWebdriver<AccordionAttributes>(driver, accordionAttributes, "rf-ico-", "-hdr");
        checker.checkCssImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkCssNoImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkImageIcons(attribute,  new IconsCheckerWebdriver.WebElementLocator(icon), image, classSuffix);
        checker.checkNone(attribute,  new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
    }
}
