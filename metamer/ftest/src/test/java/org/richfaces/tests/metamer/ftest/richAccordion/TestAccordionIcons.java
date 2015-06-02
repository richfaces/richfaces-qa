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

import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsChecker;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richAccordion/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAccordionIcons extends AbstractWebDriverTest {

    private final Attributes<AccordionAttributes> accordionAttributes = getAttributes();

    @Page
    private AccordionPage page;

    private final String leftIcon = "div[id$=item%s] td.rf-ac-itm-ico";
    private final String rightIcon = "div[id$=item%s] td.rf-ac-itm-exp-ico";

    @Override
    public String getComponentTestPagePath() {
        return "richAccordion/simple.xhtml";
    }

    @Test
    @CoversAttributes("itemActiveLeftIcon")
    @RegressionTest("https://issues.jboss.org/browse/RF-10352")
    @Templates(value = "plain")
    public void testItemActiveLeftIcon() {
        By image = By.cssSelector(String.format(leftIcon, "1") + " img");
        // icon=null
        assertFalse(new WebElementConditionFactory(page.getLeftActiveIcon()).isPresent().apply(driver), "Left icon of active item should not be present on the page.");
        verifyStandardIcons(AccordionAttributes.itemActiveLeftIcon, page.getLeftActiveIcon(), image);
    }

    @Test
    @CoversAttributes("itemActiveRightIcon")
    @Templates(value = "plain")
    public void testItemActiveRightIcon() {
        By image = By.cssSelector(String.format(rightIcon, "1") + " img");
        // icon=null
        assertFalse(new WebElementConditionFactory(page.getRightActiveIcon()).isPresent().apply(driver), "Right icon of active item should not be present on the page.");
        verifyStandardIcons(AccordionAttributes.itemActiveRightIcon, page.getRightActiveIcon(), image);
    }

    @Test
    @CoversAttributes("itemDisabledLeftIcon")
    @Templates(value = "plain")
    public void testItemDisabledLeftIcon() {
        By image = By.cssSelector(String.format(leftIcon, "4") + " img");
        verifyStandardIcons(AccordionAttributes.itemDisabledLeftIcon, page.getLeftDisabledIcon(), image);
    }

    @Test
    @CoversAttributes("itemDisabledRightIcon")
    @Templates(value = "plain")
    public void testItemDisabledRightIcon() {
        By image = By.cssSelector(String.format(rightIcon, "4") + " img");

        verifyStandardIcons(AccordionAttributes.itemDisabledRightIcon, page.getRightDisabledIcon(), image);
    }

    @Test
    @CoversAttributes("itemInactiveLeftIcon")
    @RegressionTest("https://issues.jboss.org/browse/RF-10352")
    @Templates(value = "plain")
    public void testItemInactiveLeftIcon() {
        By image = By.cssSelector(String.format(leftIcon, "3") + " img");
        verifyStandardIcons(AccordionAttributes.itemInactiveLeftIcon, page.getLeftInactiveIcon(), image);
    }

    @Test
    @CoversAttributes("itemInactiveRightIcon")
    @Templates(value = "plain")
    public void testItemInactiveRightIcon() {
        By image = By.cssSelector(String.format(rightIcon, "3") + " img");
        verifyStandardIcons(AccordionAttributes.itemInactiveRightIcon, page.getRightInactiveIcon(), image);
    }

    private void verifyStandardIcons(AccordionAttributes attribute, WebElement icon, By image) {
        new IconsChecker<AccordionAttributes>(driver, accordionAttributes).checkAll(attribute, icon, image, true, true);
    }
}
