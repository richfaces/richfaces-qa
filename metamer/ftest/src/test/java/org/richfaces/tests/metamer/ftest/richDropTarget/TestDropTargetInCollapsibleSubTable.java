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
package org.richfaces.tests.metamer.ftest.richDropTarget;

import static javax.faces.event.PhaseId.ANY_PHASE;
import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;
import static javax.faces.event.PhaseId.INVOKE_APPLICATION;
import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;
import static javax.faces.event.PhaseId.RESTORE_VIEW;
import static javax.faces.event.PhaseId.UPDATE_MODEL_VALUES;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState.ACCEPTING;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState.REJECTING;
import static org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes.acceptedTypes;
import static org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes.bypassUpdates;
import static org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes.execute;
import static org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes.immediate;
import static org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes.onbeforedomupdate;
import static org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes.onbegin;
import static org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richDropTarget.DropTargetAttributes.render;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.dropTargetAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.0.CR1
 *
 */
@IssueTracking("https://issues.jboss.org/browse/RFPL-2751")
@Templates(value = {"plain", "richCollapsibleSubTable"} )
public class TestDropTargetInCollapsibleSubTable extends AbstractWebDriverTest {

    @Page
    DropTargetSimplePage page;

    private Indicator indicator;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDropTarget/simple.xhtml");
    }

    @Test(groups = "Future")
    public void testAcceptedTypes() {
        dropTargetAttributes.set(acceptedTypes, "drg2");
        indicator = new Indicator(page.indicator);

        testAcception(page.drg1, REJECTING); doDrop();
        testAcception(page.drg2, ACCEPTING); doDrop();
        testAcception(page.drg3, REJECTING); doDrop();

        dropTargetAttributes.set(acceptedTypes, "drg1, drg3");

        testAcception(page.drg1, ACCEPTING); doDrop();
        testAcception(page.drg2, REJECTING); doDrop();
        testAcception(page.drg3, ACCEPTING); doDrop();
    }

    @Test(groups = "Future")
    public void testRender() {
        dropTargetAttributes.set(render, "droppable1 droppable2 renderChecker");

        indicator = new Indicator(page.indicator);

        testAcception(page.drg1, ACCEPTING);

        String drop1Content = page.drop1.getText();
        String drop2Content = page.drop2.getText();

        new Actions(driver).dragAndDrop(page.drg1, page.drop1).build().perform();

        // TODO JJa: find replacement
        // waitAjax.waitForChange(retrieveDrop1);
        // assertTrue(retrieveDrop2.isValueChanged());
        Graphene.waitModel().until().element(page.drop1).text().not().equalTo(drop1Content);
        // Graphene.waitModel().until(Graphene.element(page.drop2).not().text().equalTo(drop2Content));
        assertFalse(Graphene.element(page.drop2).equals(drop2Content));
    }

    @Test(groups = "Future")
    public void testDropListenerAndEvent() {

        indicator =  new Indicator(page.indicator);

        testAcceptedDropping(page.drg1);
        assertTrue(page.clientId.getText().endsWith("richDropTarget1"));
        assertTrue(page.dragValue.getText().contains("1"));
        assertTrue(page.dropValue.getText().contains("1"));

        testAcceptedDropping(page.drg1);
        assertTrue(page.clientId.getText().endsWith("richDropTarget1"));
        assertTrue(page.dragValue.getText().contains("1"));
        assertTrue(page.dropValue.getText().contains("2"));

        testAcceptedDropping(page.drg2);
        assertTrue(page.clientId.getText().endsWith("richDropTarget1"));
        assertTrue(page.dragValue.getText().contains("2"));
        assertTrue(page.dropValue.getText().contains("3"));

        testAcceptedDropping(page.drg1);
        assertTrue(page.clientId.getText().endsWith("richDropTarget1"));
        assertTrue(page.dragValue.getText().contains("1"));
        assertTrue(page.dropValue.getText().contains("4"));

        new Actions(driver).clickAndHold(page.drg3).moveByOffset(1, 1).moveToElement(page.drop2).build().perform();
        String requestTime = page.requestTime.getText();
        waiting(500);
        doDrop();
        Graphene.waitModel().until().element(page.requestTime).text().not().equalTo(requestTime);
        assertTrue(page.clientId.getText().endsWith("richDropTarget2"));
        assertTrue(page.dragValue.getText().contains("3"));
        assertTrue(page.dropValue.getText().contains("5"));
    }

    @Test(groups = "Future")
    public void testExecute() {
        dropTargetAttributes.set(execute, "executeChecker");

        testAcception(page.drg1, ACCEPTING);
        guardedDrop();

        Graphene.waitModel().until().element(page.dropValue).is().present();
        page.assertListener(UPDATE_MODEL_VALUES, "executeChecker");
        page.assertListener(INVOKE_APPLICATION, "dropListener");
        page.assertPhases(ANY_PHASE);
    }

    @Test(groups = "Future")
    @RegressionTest("https://issues.jboss.org/browse/RF-10535")
    public void testImmediate() {
        dropTargetAttributes.set(immediate, true);
        indicator =  new Indicator(page.indicator);

        testAcception(page.drg1, ACCEPTING);
        guardedDrop();

        Graphene.waitModel().until().element(page.dropValue).is().present();
        page.assertListener(APPLY_REQUEST_VALUES, "dropListener");
        page.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, RENDER_RESPONSE);
    }

    @Test(groups = "Future")
    @RegressionTest("https://issues.jboss.org/browse/RF-10535")
    public void testBypassUpdates() {
        dropTargetAttributes.set(bypassUpdates, true);
        indicator =  new Indicator(page.indicator);

        testAcception(page.drg1, ACCEPTING);
        guardedDrop();
        // wait for drop accept before assert listener to avoid IndexOutOfBounds
        // while drop processing change phases list
        Graphene.waitModel().until().element(page.dropValue).is().present();
        page.assertListener(PROCESS_VALIDATIONS, "dropListener");
        page.assertPhases(RESTORE_VIEW, APPLY_REQUEST_VALUES, PROCESS_VALIDATIONS, RENDER_RESPONSE);
    }

    @Test(groups = "Future")
    public void testEvents() {
        dropTargetAttributes.set(onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        dropTargetAttributes.set(onbegin, "metamerEvents += \"begin \"");
        dropTargetAttributes.set(oncomplete, "metamerEvents += \"complete \"");
        indicator =  new Indicator(page.indicator);

        executeJS("metamerEvents = \"\";");

        testAcception(page.drg1, ACCEPTING);
        guardedDrop();
        Graphene.waitModel().until().element(page.dropValue).is().present();

        String[] events = ((String) executeJS("return metamerEvents;")).split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    private void testAcception(WebElement drag, IndicatorState state) {
        new Actions(driver).clickAndHold(drag).moveToElement(page.drop1).perform();
        indicator.verifyState(state);
    }

    private void testAcceptedDropping(WebElement draggable) {
        testAcception(draggable, ACCEPTING);
        String requestTime = page.requestTime.getText();
        // waiting(1000);
        doDrop();
        Graphene.waitModel().until().element(page.requestTime).text().not().equalTo(requestTime);
        assertNotPresent(page.indicator, "Indicator should be no longer visible");
    }

    private void doDrop() {
        new Actions(driver).release().build().perform();
    }

    private void guardedDrop() {
        Actions release = new Actions(driver).release();
        MetamerPage.waitRequest(release, WaitRequestType.XHR);
        release.build().perform();
    }

}
