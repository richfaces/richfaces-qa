/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2014, Red Hat, Inc.
 * and individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.metamer.ftest.richDataScroller;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

public class TestCustomizedPageFacets extends AbstractWebDriverTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataScroller/customizedFacets.xhtml");
    }

    @FindBy(xpath = "//span/select/option[@selected]")
    private WebElement selectOptionElement;

    @FindByJQuery(".rf-ds-pages:eq(1) .rf-insp")
    private RichFacesInputNumberSpinner rfSpinner;

    @FindByJQuery(".rf-ds:eq(0) select option")
    List<WebElement> listOfOptions;

    @FindByJQuery(".rf-dt-r:eq(0)")
    WebElement firstRowOfTable;

    @Test
    public void testTopCustomizedPageFacet() {
        assertInitialState();
        // change page via customized page facet
        Action selectPage2Action = new Actions(driver).moveToElement(listOfOptions.get(1)).click().build();
        MetamerPage.waitRequest(selectPage2Action, WaitRequestType.XHR).perform();

        // assert that secondary custom facet was updated
        assertTrue(rfSpinner.getValue() == 2);

        // assert that table data are correct
        // since we already test scrollers, it is enough to assert one row
        assertAfterSwitchToSecondPage();
    }

    @Test
    public void testBottomCustomizedPageFacet() {
        assertInitialState();
        // change page via customized page facet
        rfSpinner.advanced().getArrowIncreaseElement().click();
        // assert that secondary custom facet was updated
        waitAjax(driver).until().element(selectOptionElement).attribute("value").equals("2");
        // assert that table data are correct
        // since we already test scrollers, it is enough to assert one row
        assertAfterSwitchToSecondPage();
    }

    private boolean assertInitialState() {
        List<WebElement> stateAndCapital = firstRowOfTable.findElements(By.tagName("td"));
        return stateAndCapital.get(0).equals("Alabama") && stateAndCapital.get(1).equals("Montgomery");
    }

    private boolean assertAfterSwitchToSecondPage() {
        List<WebElement> stateAndCapital = firstRowOfTable.findElements(By.tagName("td"));
        return stateAndCapital.get(0).equals("Georgia") && stateAndCapital.get(1).equals("Atlanta");
    }
}
