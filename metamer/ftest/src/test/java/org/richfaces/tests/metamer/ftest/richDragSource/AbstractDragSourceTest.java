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

import static org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState.ACCEPTING;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState.DRAGGING;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState.REJECTING;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.dragIndicator;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.type;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.dragSourceAttributes;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public abstract class AbstractDragSourceTest extends AbstractWebDriverTest {

    private ElementPresent elementPresent = ElementPresent.getInstance();
    private ElementNotPresent elementNotPresent = ElementNotPresent.getInstance();

    @Page
    private DragSourceSimplePage page;

    protected Indicator indicator;

    public void testDefaultIndicator() {

        indicator = new Indicator(page.defaultIndicator);
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

        Action drop = actionQueue.release(page.drop1).build();
        drop.perform();

    }

    public void testCustomIndicator() {

        dragSourceAttributes.set(dragIndicator, "indicator2");
        indicator = new Indicator(page.indicator2);

        testMovingOverDifferentStates();
    }

    public void testRendered() {

        dragSourceAttributes.set(dragIndicator, "indicator2");
        dragSourceAttributes.set(rendered, true);

        // before any mouse move, no indicator appears on page
        elementNotPresent.element(page.indicator2).apply(driver);

        // indicator = new IndicatorWD(indicatorLoc);
        indicator = new Indicator(page.indicator2);
        Actions actionQueue = new Actions(driver);

        // firstly just drag and don't move. Indicator no displayed
        Action dragging = actionQueue.clickAndHold(page.drag1).build();
        dragging.perform();
        elementNotPresent.element(page.indicator2).apply(driver);

        // just small move to display indicator
        dragging = actionQueue.moveByOffset(1, 1).build();
        dragging.perform();
        elementPresent.element(page.indicator2).apply(driver);

        dragging = actionQueue.release().build();
        elementPresent.element(page.indicator2).apply(driver);
        dragging.perform();
        elementNotPresent.element(page.indicator2).apply(driver);

    }

    public void testType() {
        dragSourceAttributes.set(type, "drg3");

        indicator = new Indicator(page.indicator);

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
        // since this method verify indicator rendering over drag source, need force move a bit
        // to render indicator in this case (otherwise it doesn't get rendered)
        // split into more steps for better ability debug in case it doesn't work
        new Actions(driver).clickAndHold(page.drag1).build().perform();
        new Actions(driver).moveByOffset(1, 1).build().perform();
        new Actions(driver).moveToElement(target).build().perform();
        indicator.verifyState(state);
        // since dragSource is the same for all iteration, it is not required drop.
        // but keep droping to simulate real behavior
        new Actions(driver).moveToElement(page.drag1).release().build().perform();
    }

}
