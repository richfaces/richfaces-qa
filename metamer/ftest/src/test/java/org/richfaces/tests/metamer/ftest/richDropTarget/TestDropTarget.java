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
package org.richfaces.tests.metamer.ftest.richDropTarget;

import static javax.faces.event.PhaseId.ANY_PHASE;
import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.0.CR1
 *
 */
@Templates(exclude = "richCollapsibleSubTable")
public class TestDropTarget extends AbstractWebDriverTest {

    private final Attributes<DropTargetAttributes> dropTargetAttributes = getAttributes();

    private Indicator indicator;

    @Page
    private DropTargetSimplePage page;

    private final Action successfullDragAndDropAction = new Action() {
        @Override
        public void perform() {
            testAcceptedDropping(page.getDrg1());
        }
    };

    private void doDrop() {
        new Actions(driver).release().build().perform();
    }

    @Override
    public String getComponentTestPagePath() {
        return "richDropTarget/simple.xhtml";
    }

    private void guardedDrop() {
        Graphene.guardAjax(new Actions(driver).release()).perform();
    }

    private void testAcceptedDropping(WebElement draggable) {
        verifyAcception(draggable, IndicatorState.ACCEPTING);
        guardedDrop();
        assertNotPresent(page.getIndicator(), "Indicator should be no longer visible");
    }

    @Test
    @CoversAttributes("acceptedTypes")
    public void testAcceptedTypes() {
        dropTargetAttributes.set(DropTargetAttributes.acceptedTypes, "drg2");
        indicator = new Indicator(page.getIndicator());

        verifyAcception(page.getDrg1(), IndicatorState.REJECTING);
        doDrop();
        verifyAcception(page.getDrg2(), IndicatorState.ACCEPTING);
        guardedDrop();
        verifyAcception(page.getDrg3(), IndicatorState.REJECTING);
        doDrop();

        dropTargetAttributes.set(DropTargetAttributes.acceptedTypes, "drg1, drg3");

        verifyAcception(page.getDrg1(), IndicatorState.ACCEPTING);
        guardedDrop();
        verifyAcception(page.getDrg2(), IndicatorState.REJECTING);
        doDrop();
        verifyAcception(page.getDrg3(), IndicatorState.ACCEPTING);
        guardedDrop();
    }

    @Test
    @CoversAttributes("bypassUpdates")
    @RegressionTest("https://issues.jboss.org/browse/RF-10535")
    public void testBypassUpdates() {
        dropTargetAttributes.set(DropTargetAttributes.bypassUpdates, true);
        indicator = new Indicator(page.getIndicator());

        verifyAcception(page.getDrg1(), IndicatorState.ACCEPTING);
        guardedDrop();
        // wait for drop accept before assert listener to avoid IndexOutOfBounds
        // while drop processing change phases list
        Graphene.waitModel().until().element(page.getDropValue()).is().present();
        page.assertListener(PROCESS_VALIDATIONS, "dropListener");
        page.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, RENDER_RESPONSE);
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(successfullDragAndDropAction);
    }

    @Test
    @CoversAttributes({ "dropListener", "dropValue" })
    public void testDropListenerAndValue() {
        indicator = new Indicator(page.getIndicator());

        testAcceptedDropping(page.getDrg1());
        assertTrue(page.getClientId().getText().endsWith("richDropTarget1"));
        assertTrue(page.getDragValue().getText().contains("1"));
        assertTrue(page.getDropValue().getText().contains("1"));

        testAcceptedDropping(page.getDrg1());
        assertTrue(page.getClientId().getText().endsWith("richDropTarget1"));
        assertTrue(page.getDragValue().getText().contains("1"));
        assertTrue(page.getDropValue().getText().contains("2"));

        testAcceptedDropping(page.getDrg2());
        assertTrue(page.getClientId().getText().endsWith("richDropTarget1"));
        assertTrue(page.getDragValue().getText().contains("2"));
        assertTrue(page.getDropValue().getText().contains("3"));

        testAcceptedDropping(page.getDrg1());
        assertTrue(page.getClientId().getText().endsWith("richDropTarget1"));
        assertTrue(page.getDragValue().getText().contains("1"));
        assertTrue(page.getDropValue().getText().contains("4"));

        new Actions(driver).clickAndHold(page.getDrg3()).moveByOffset(1, 1).moveToElement(page.getDrop2()).build().perform();
        guardedDrop();
        assertTrue(page.getClientId().getText().endsWith("richDropTarget2"));
        assertTrue(page.getDragValue().getText().contains("3"));
        assertTrue(page.getDropValue().getText().contains("5"));
    }

    @Test
    @CoversAttributes({ "onbeforedomupdate", "onbegin", "oncomplete" })
    public void testEvents() {
        dropTargetAttributes.set(DropTargetAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        dropTargetAttributes.set(DropTargetAttributes.onbegin, "metamerEvents += \"begin \"");
        dropTargetAttributes.set(DropTargetAttributes.oncomplete, "metamerEvents += \"complete \"");
        indicator = new Indicator(page.getIndicator());

        executeJS("metamerEvents = \"\";");

        verifyAcception(page.getDrg1(), IndicatorState.ACCEPTING);
        guardedDrop();
        Graphene.waitModel().until().element(page.getDropValue()).is().present();

        String[] events = ((String) executeJS("return metamerEvents;")).split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    @CoversAttributes("execute")
    public void testExecute() {
        dropTargetAttributes.set(DropTargetAttributes.execute, "executeChecker");

        verifyAcception(page.getDrg1(), IndicatorState.ACCEPTING);
        guardedDrop();

        Graphene.waitModel().until().element(page.getDropValue()).is().present();
        page.assertListener(UPDATE_MODEL_VALUES, "executeChecker");
        page.assertListener(INVOKE_APPLICATION, "dropListener");
        page.assertPhases(ANY_PHASE);
    }

    @Test
    @CoversAttributes("immediate")
    @RegressionTest("https://issues.jboss.org/browse/RF-10535")
    public void testImmediate() {
        dropTargetAttributes.set(DropTargetAttributes.immediate, true);
        indicator = new Indicator(page.getIndicator());

        verifyAcception(page.getDrg1(), IndicatorState.ACCEPTING);
        guardedDrop();

        Graphene.waitModel().until().element(page.getDropValue()).is().present();
        page.assertListener(APPLY_REQUEST_VALUES, "dropListener");
        page.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE);
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        dropTargetAttributes.set(DropTargetAttributes.limitRender, true);
        dropTargetAttributes.set(DropTargetAttributes.render, "@this renderChecker");
        String renderCheckerText = getMetamerPage().getRenderCheckerOutputElement().getText();
        String requestTime = getMetamerPage().getRequestTimeElement().getText();
        Graphene.guardAjax(new ActionWrapper(successfullDragAndDropAction)).perform();
        Graphene.waitGui().until().element(getMetamerPage().getRenderCheckerOutputElement()).text().not()
            .equalTo(renderCheckerText);
        Graphene.waitGui().until().element(getMetamerPage().getRequestTimeElement()).text()
            .equalTo(requestTime);
    }

    @Test
    @CoversAttributes("render")
    public void testRender() {
        dropTargetAttributes.set(DropTargetAttributes.render, "droppable1 droppable2 renderChecker");

        indicator = new Indicator(page.getIndicator());

        verifyAcception(page.getDrg1(), IndicatorState.ACCEPTING);

        String drop1Content = page.getDrop1().getText();
        String drop2Content = page.getDrop2().getText();
        new Actions(driver).release(page.getDrop1()).perform();

        // new Actions(driver).dragAndDrop(page.drg1, page.drop1).build().perform();
        // TODO JJa: find replacement
        // waitAjax.waitForChange(retrieveDrop1);
        // assertTrue(retrieveDrop2.isValueChanged());
        Graphene.waitModel().until().element(page.getDrop1()).text().not().equalTo(drop1Content);
        Graphene.waitModel().until(new WebElementConditionFactory(page.getDrop2()).not().text().equalTo(drop2Content));
    }

    @Test
    @CoversAttributes("rendered")
    public void testRendered() {
        dropTargetAttributes.set(DropTargetAttributes.rendered, false);
        indicator = new Indicator(page.getIndicator());
        verifyAcception(page.getDrg1(), IndicatorState.DRAGGING);
        new Actions(driver).release().perform();
        verifyAcception(page.getDrg2(), IndicatorState.DRAGGING);
        new Actions(driver).release().perform();
        verifyAcception(page.getDrg3(), IndicatorState.DRAGGING);
        new Actions(driver).release().perform();

        dropTargetAttributes.set(DropTargetAttributes.rendered, true);
        indicator = new Indicator(page.getIndicator());
        verifyAcception(page.getDrg1(), IndicatorState.ACCEPTING);
        Graphene.guardAjax(new Actions(driver).release()).perform();
        verifyAcception(page.getDrg2(), IndicatorState.ACCEPTING);
        Graphene.guardAjax(new Actions(driver).release()).perform();
        verifyAcception(page.getDrg3(), IndicatorState.REJECTING);
        Graphene.guardNoRequest(new Actions(driver).release()).perform();
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        testStatus(successfullDragAndDropAction);
    }

    private void verifyAcception(WebElement drag, IndicatorState state) {
        new Actions(driver).clickAndHold(drag).moveToElement(page.getDrop1()).perform();
        indicator.verifyState(state);
    }
}
