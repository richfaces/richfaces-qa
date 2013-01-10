/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDragSource;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.richDragIndicator.IndicatorWD.IndicatorState.ACCEPTING;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.IndicatorWD.IndicatorState.DRAGGING;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.IndicatorWD.IndicatorState.REJECTING;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.dragSourceAttributes;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.dragIndicator;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.type;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.richDragIndicator.IndicatorWD;
import org.richfaces.tests.metamer.ftest.richDragIndicator.IndicatorWD.IndicatorState;
import org.testng.annotations.Test;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.0.CR1
 *
 */
public class TestRichDragSourceWebDriver extends AbstractWebDriverTest {

    @Page
    private DragSourceSimplePage page;

    private IndicatorWD indicator;

    private ElementPresent elementPresent = ElementPresent.getInstance();
    private ElementNotPresent elementNotPresent = ElementNotPresent.getInstance();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDragSource/simple.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12441")
    public void testDefaultIndicator() {

        By defaultIndocatorLoc = By.cssSelector("body > div.ui-draggable");
        indicator = new IndicatorWD(defaultIndocatorLoc);
        indicator.setDefaultIndicator(true);
        dragSourceAttributes.set(dragIndicator, "");
        Actions actionQueue = new Actions(driver);

        Action clickAndHold = actionQueue.clickAndHold(page.drag1).build();
        clickAndHold.perform();
        assertTrue(elementNotPresent.element(page.defaultIndicator).apply(driver));

        Action move = actionQueue.moveByOffset(1, 1).build();
        move.perform();
        assertTrue(elementPresent.element(page.defaultIndicator).apply(driver));

        testMovingOverDifferentStates();

    }

    @Test
    public void testCustomIndicator() {

        dragSourceAttributes.set(dragIndicator, "indicator2");
        By customIndicatorLoc = By.cssSelector("div.rf-ind[id$=indicator2Clone");
        indicator = new IndicatorWD(customIndicatorLoc);

        testMovingOverDifferentStates();
    }

    @Test
    public void testRendered() {

        dragSourceAttributes.set(dragIndicator, "indicator2");
        dragSourceAttributes.set(rendered, true);

        By indicatorLoc = By.cssSelector("div.rf-ind[id$=indicator2Clone]");

        // before any mouse move, no indicator appears on page
        // elementNotPresent.element(indicator.getIndicator(indicatorLoc)).apply(driver);

        indicator = new IndicatorWD(indicatorLoc);
        Actions actionQueue = new Actions(driver);

        // firstly just drag and don't move. Indicator no displayed
        Action dragging = actionQueue.clickAndHold(page.drag1).build();
        dragging.perform();
        elementPresent.element(indicator.getIndicator(indicatorLoc)).apply(driver);

        // just small move to display indicator
        dragging = actionQueue.moveByOffset(1, 1).build();
        dragging.perform();
        elementPresent.element(indicator.getIndicator(indicatorLoc)).apply(driver);

        dragging = actionQueue.release().build();
        elementNotPresent.element(indicator.getIndicator(indicatorLoc)).apply(driver);
        dragging.perform();
        elementNotPresent.element(indicator.getIndicator(indicatorLoc)).apply(driver);

    }

    @Test
    public void testType() {
        dragSourceAttributes.set(type, "drg3");

        enterAndVerify(page.drop2, IndicatorState.ACCEPTING);
        enterAndVerify(page.drag2, IndicatorState.DRAGGING);
        enterAndVerify(page.drop1, IndicatorState.REJECTING);
    }

    protected void testMovingOverDifferentStates() {
        enterAndVerify(page.drop2, REJECTING);
        enterAndVerify(page.drop1, ACCEPTING);
        enterAndVerify(page.drag1, DRAGGING);
        enterAndVerify(page.drop1, ACCEPTING);
        enterAndVerify(page.drag2, DRAGGING);
        enterAndVerify(page.drop2, REJECTING);
        enterAndVerify(page.drag2, DRAGGING);
        enterAndVerify(page.drop1, ACCEPTING);
        enterAndVerify(page.drop2, REJECTING);
    }

    protected void enterAndVerify(WebElement target, IndicatorState state) {
        new Actions(driver).clickAndHold(page.drag1).moveToElement(target).perform();
        indicator.verifyState(state);
    }
}
