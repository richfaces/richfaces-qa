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
package org.richfaces.tests.metamer.ftest.richAccordionItem;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.contentClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerActiveClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerDisabledClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerInactiveClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.headerStyle;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.checker.IconsCheckerWebdriver;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test rich:accordionItem attributes on page faces/components/richAccordion/simple.xhtml
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class TestAccordionItem extends AbstractWebDriverTest {

    private final Attributes<AccordionItemAttributes> accordionItemAttributes = getAttributes();

    @Page
    private AccordionItemPage page;

    @FindByJQuery(value = "div[id$=item1].rf-ac-itm")
    private WebElement testElement;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordionItem/simple.xhtml");
    }

    /**
     * Checks whether given element has given string as class
     *
     * @param elem WebElement to check for
     * @param className Name of class
     * @return true is element as the class, false otherwise
     */
    private boolean belongsClass(WebElement elem, String className) {
        if (elem == null || className == null || className.isEmpty()) {
            return false;
        }
        return elem.getAttribute("class").contains(className);
    }

    private void verifyStandardIcons(AccordionItemAttributes attribute, WebElement icon, By image, String classSuffix) {
        IconsCheckerWebdriver checker = new IconsCheckerWebdriver<AccordionItemAttributes>(driver, accordionItemAttributes,
            "rf-ico-", "-hdr");
        checker.checkCssImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkCssNoImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), image, classSuffix);
        checker.checkNone(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
    }

    @Test
    public void testInit() {
        assertVisible(page.getAccordionRootElement(), "Accordion is not visible!");

        assertVisible(page.getItemHeaders().get(2), "Item3's header should be visible.");

        assertVisible(page.getItemContents().get(2), "Content of item3 should be visible.");

        assertNotVisible(page.getItemContents().get(0), "Item1's content should not be visible.");
    }

    @Test
    @Templates("plain")
    public void testContentClass() {
        testStyleClass(page.getItemContents().get(0), contentClass);
    }

    @Test
    @Templates("plain")
    public void testDir() {
        testDir(testElement);
    }

    @Test
    public void testDisabled() {
        accordionItemAttributes.set(AccordionItemAttributes.disabled, true);
        guardNoRequest(page.getItemHeaders().get(0)).click();
        assertNotVisible(page.getItemContents().get(0), "Item1's content should not be visible.");
    }

    @Test
    public void testHeader() {
        accordionItemAttributes.set(AccordionItemAttributes.header, "new header");
        String headerText = page.getInactiveHeaders().get(0).getText();
        assertEquals("new header", headerText);
    }

    @Test
    @Templates("plain")
    @RegressionTest("https://issues.jboss.org/browse/RF-10297")
    public void testHeaderActiveClass() {
        testStyleClass(page.getActiveHeaders().get(0), headerActiveClass);
        assertFalse(belongsClass(page.getActiveHeaders().get(1), "metamer-ftest-class"),
            "headerActiveClass should be set only on first item");
        assertFalse(belongsClass(page.getActiveHeaders().get(2), "metamer-ftest-class"),
            "headerActiveClass should be set only on first item");
        assertFalse(belongsClass(page.getActiveHeaders().get(3), "metamer-ftest-class"),
            "headerActiveClass should be set only on first item");
    }

    @Test
    @Templates("plain")
    public void testHeaderClass() {
        testStyleClass(page.getItemHeaders().get(0), headerClass);
        assertFalse(belongsClass(page.getItemHeaders().get(1), "metamer-ftest-class"),
            "headerClass should be set only on first item");
        assertFalse(belongsClass(page.getItemHeaders().get(2), "metamer-ftest-class"),
            "headerClass should be set only on first item");
        assertFalse(belongsClass(page.getItemHeaders().get(3), "metamer-ftest-class"),
            "headerClass should be set only on first item");
    }

    @Test
    @Templates("plain")
    @RegressionTest("https://issues.jboss.org/browse/RF-10297")
    public void testHeaderDisabledClass() {
        accordionItemAttributes.set(AccordionItemAttributes.disabled, true);

        testStyleClass(page.getDisabledHeaders().get(0), headerDisabledClass);
    }

    @Test
    @Templates("plain")
    @RegressionTest("https://issues.jboss.org/browse/RF-10297")
    public void testHeaderInactiveClass() {
        testStyleClass(page.getInactiveHeaders().get(0), headerInactiveClass);
        assertFalse(belongsClass(page.getInactiveHeaders().get(1), "metamer-ftest-class"),
            "headerInactiveClass should be set only on first item");
        assertFalse(belongsClass(page.getInactiveHeaders().get(2), "metamer-ftest-class"),
            "headerInactiveClass should be set only on first item");
        assertFalse(belongsClass(page.getInactiveHeaders().get(3), "metamer-ftest-class"),
            "headerInactiveClass should be set only on first item");
    }

    @Test
    @Templates("plain")
    public void testHeaderStyle() {
        testStyle(page.getItemHeaders().get(0), headerStyle);
    }

    @Test
    @Templates("plain")
    public void testLang() {
        testAttributeLang(testElement);
    }

    @Test
    @Templates("plain")
    @RegressionTest("https://issues.jboss.org/browse/RF-10488")
    public void testLeftActiveIcon() {
        By image = By.cssSelector(page.getLeftIconSelector() + " img");

        // icon should not be locateable
        for (int i = 1; i < 6; i++) {
            try {
                driver.findElement(ByJQuery.selector(page.getLeftIconSelector().replace("1", String.valueOf(i))));
            } catch (NoSuchElementException e) {
                // ok
            }
        }
        guardAjax(page.getItemHeaders().get(0)).click();
        waitAjax(driver).until().element(page.getItemContents().get(0)).is().visible();
        verifyStandardIcons(AccordionItemAttributes.leftActiveIcon, page.getLeftActiveIcon(), image, "");
    }

    @Test
    @Templates("plain")
    public void testLeftDisabledIcon() {
        By image = By.cssSelector(page.getLeftIconSelector() + " img");

        // icon should not be locateable
        for (int i = 1; i < 6; i++) {
            try {
                driver.findElement(ByJQuery.selector(page.getLeftIconSelector().replace("1", String.valueOf(i))));
            } catch (NoSuchElementException e) {
                // ok
            }
        }

        accordionItemAttributes.set(AccordionItemAttributes.disabled, true);

        verifyStandardIcons(AccordionItemAttributes.leftDisabledIcon, page.getLeftDisabledIcon(), image, "-dis");
    }

    @Test
    @Templates("plain")
    public void testLeftInactiveIcon() {
        By image = By.cssSelector(page.getLeftIconSelector() + " img");

        // icon should not be locateable
        for (int i = 1; i < 6; i++) {
            try {
                driver.findElement(ByJQuery.selector(page.getLeftIconSelector().replace("1", String.valueOf(i))));
            } catch (NoSuchElementException e) {
                // ok
            }
        }

        verifyStandardIcons(AccordionItemAttributes.leftInactiveIcon, page.getLeftInactiveIcon(), image, "");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10488")
    public void testName() {
        accordionItemAttributes.set(AccordionItemAttributes.name, "new name");
        guardAjax(driver.findElement(ByJQuery.selector("input[type=submit][name$=switchButtonCustom]"))).click();
        waitAjax(driver).until().element(page.getItemContents().get(0)).is().visible();
    }

    @Test
    @Templates("plain")
    public void testOnclick() {
        Action action = new Actions(driver).click(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onclick, action);
    }

    @Test
    @Templates("plain")
    public void testOndblclick() {
        Action action = new Actions(driver).doubleClick(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.ondblclick, action);
    }

    @Test
    public void testOnenter() {
        Action action = new Actions(driver).click(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onenter, action);
    }

    @Test
    @Templates("plain")
    public void testOnheaderclick() {
        Action action = new Actions(driver).click(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onheaderclick, action);
    }

    @Test
    @Templates("plain")
    public void testOnheaderdblclick() {
        Action action = new Actions(driver).doubleClick(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onheaderdblclick, action);
    }

    @Test
    @Templates("plain")
    public void testOnheadermousedown() {
        Action action = new Actions(driver).clickAndHold(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onheadermousedown, action);
        new Actions(driver).release().build().perform();
    }

    @Test
    @Templates("plain")
    public void testOnheadermousemove() {
        Action action = new Actions(driver).moveToElement(page.getItemHeaders().get(2))
            .moveToElement(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onheadermousemove, action);
    }

    @Test
    @Templates("plain")
    public void testOnheadermouseup() {
        Action action = new Actions(driver).moveToElement(page.getItemHeaders().get(1)).clickAndHold()
            .moveToElement(page.getItemHeaders().get(0)).release().build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onheadermouseup, action);
    }

    @Test(groups = "smoke")
    @RegressionTest("https://issues.jboss.org/browse/RF-9821 https://issues.jboss.org/browse/RF-10488")
    public void testOnleave() {
        Action action = new Actions(driver).click(page.getItemHeaders().get(0)).click(page.getItemHeaders().get(2)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onleave, action);
    }

    @Test
    @Templates("plain")
    public void testOnmousedown() {
        Action action = new Actions(driver).clickAndHold(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onmousedown, action);
        new Actions(driver).release().build().perform();
    }

    @Test
    @Templates("plain")
    public void testOnmousemove() {
        Action action = new Actions(driver).moveToElement(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onmousemove, action);
    }

    @Test
    @Templates("plain")
    public void testOnmouseout() {
        Action action = new Actions(driver).moveToElement(page.getItemHeaders().get(0))
            .moveToElement(page.getItemHeaders().get(3)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onmouseout, action);
    }

    @Test
    @Templates(value = "plain")
    public void testOnmouseover() {
        Action action = new Actions(driver).moveToElement(page.getRequestTimeElement()).moveToElement(page.getItemHeaders().get(0)).build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onmouseover, action);
    }

    @Test
    @Templates("plain")
    public void testOnmouseup() {
        Action action = new Actions(driver).clickAndHold(page.getItemHeaders().get(2))
            .moveToElement(page.getItemHeaders().get(0)).release().build();
        testFireEvent(accordionItemAttributes, AccordionItemAttributes.onmouseup, action);
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        accordionItemAttributes.set(AccordionItemAttributes.rendered, false);
        assertNotPresent(testElement, "Item1 should not be rendered when rendered=false.");
    }

    @Test
    @Templates("plain")
    public void testRightActiveIcon() {
        By image = By.cssSelector(page.getRightIconSelector() + " img");

        // icon should not be locateable
        for (int i = 1; i < 6; i++) {
            try {
                driver.findElement(ByJQuery.selector(page.getRightIconSelector().replace("1", String.valueOf(i))));
            } catch (NoSuchElementException e) {
                // ok
            }
        }
        guardAjax(page.getItemHeaders().get(0)).click();
        waitAjax(driver).until().element(page.getItemContents().get(0)).is().visible();
        verifyStandardIcons(AccordionItemAttributes.rightActiveIcon, page.getRightActiveIcon(), image, "");
    }

    @Test
    @Templates("plain")
    public void testRightDisabledIcon() {
        By image = By.cssSelector(page.getRightIconSelector() + " img");

        // icon should not be locateable
        for (int i = 1; i < 6; i++) {
            try {
                driver.findElement(ByJQuery.selector(page.getRightIconSelector().replace("1", String.valueOf(i))));
            } catch (NoSuchElementException e) {
                // ok
            }
        }

        accordionItemAttributes.set(AccordionItemAttributes.disabled, true);

        verifyStandardIcons(AccordionItemAttributes.rightDisabledIcon, page.getRightDisabledIcon(), image, "-dis");
    }

    @Test
    @Templates("plain")
    public void testRightInactiveIcon() {
        By image = By.cssSelector(page.getRightIconSelector() + " img");

        // icon should not be locateable
        for (int i = 1; i < 6; i++) {
            try {
                driver.findElement(ByJQuery.selector(page.getRightIconSelector().replace("1", String.valueOf(i))));
            } catch (NoSuchElementException e) {
                // ok
            }
        }

        verifyStandardIcons(AccordionItemAttributes.rightInactiveIcon, page.getRightInactiveIcon(), image, "");
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        testStyle(testElement);
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(testElement);
    }

    @Test
    public void testSwitchTypeNull() {
        guardAjax(page.getItemHeaders().get(0)).click();
        waitAjax(driver).until().element(page.getItemContents().get(0)).is().visible();
    }

    @Test(groups = "smoke")
    @Templates("plain")
    public void testSwitchTypeAjax() {
        accordionItemAttributes.set(AccordionItemAttributes.switchType, "ajax");

        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        accordionItemAttributes.set(AccordionItemAttributes.switchType, "client");

        guardNoRequest(page.getItemHeaders().get(0)).click();
        waitGui(driver).until().element(page.getItemContents().get(0)).is().visible();
    }

    @Test(groups = "smoke")
    public void testSwitchTypeServer() {
        accordionItemAttributes.set(AccordionItemAttributes.switchType, "server");

        guardHttp(page.getItemHeaders().get(0)).click();
        waitModel(driver).until().element(page.getItemContents().get(0)).is().visible();
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        testTitle(testElement);
    }
}
