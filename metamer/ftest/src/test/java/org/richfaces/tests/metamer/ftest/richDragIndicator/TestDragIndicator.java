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
package org.richfaces.tests.metamer.ftest.richDragIndicator;

import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.dragIndicatorAttributes;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.DragIndicatorAttributes.acceptClass;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.DragIndicatorAttributes.draggingClass;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.DragIndicatorAttributes.rejectClass;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.DragIndicatorAttributes.rendered;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;

import org.jboss.arquillian.ajocado.actions.Drag;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22741 $
 */
public class TestDragIndicator extends AbstractDragNDropTest {

    private static final String ACCEPT_CLASS = "sample-accept-class";
    private static final String REJECT_CLASS = "sample-reject-class";
    private static final String DRAGGING_CLASS = "sample-dragging-class";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDragIndicator/simple.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Drag Indicator", "Simple");
    }

    @BeforeMethod
    public void setup() {
        dragIndicatorAttributes.set(draggingClass, DRAGGING_CLASS);
        dragIndicatorAttributes.set(acceptClass, ACCEPT_CLASS);
        dragIndicatorAttributes.set(rejectClass, REJECT_CLASS);
        indicator.setDraggingClass(DRAGGING_CLASS);
        indicator.setAcceptClass(ACCEPT_CLASS);
        indicator.setRejectClass(REJECT_CLASS);
    }

    @Test
    public void testRendered() {
        dragIndicatorAttributes.set(rendered, false);
        selenium.getPageExtensions().install();
        selenium.getRequestGuard().clearRequestDone();

        drag = new Drag(drg1, drop1);
        drag.setDragIndicator(indicator);
        drag.start();
        assertFalse(selenium.isElementPresent(indicator));
        drag.enter();
        assertFalse(selenium.isElementPresent(indicator));

        drag.drop();

        waitModel.timeout(5000).waitForTimeout();
        assertEquals(selenium.getRequestGuard().getRequestDone(), RequestType.NONE);
    }

    @Test
    public void testDragging() {
        drag = new Drag(drg1, drg2);
        drag.setDragIndicator(indicator);
        indicator.verifyState(IndicatorState.HIDDEN);

        drag.start();
        indicator.verifyState(IndicatorState.DRAGGING);

        drag.mouseOut();
        indicator.verifyState(IndicatorState.DRAGGING);

        drag.enter();
        indicator.verifyState(IndicatorState.DRAGGING);
    }

    @Test
    public void testAccepting() {
        drag = new Drag(drg1, drop1);
        drag.setDragIndicator(indicator);
        indicator.verifyState(IndicatorState.HIDDEN);

        drag.start();
        indicator.verifyState(IndicatorState.DRAGGING);

        drag.mouseOut();
        indicator.verifyState(IndicatorState.DRAGGING);

        drag.enter();
        indicator.verifyState(IndicatorState.ACCEPTING);
    }

    @Test
    public void testRejecting() {
        drag = new Drag(drg1, drop2);
        drag.setDragIndicator(indicator);
        indicator.verifyState(IndicatorState.HIDDEN);

        drag.start();
        indicator.verifyState(IndicatorState.DRAGGING);

        drag.mouseOut();
        indicator.verifyState(IndicatorState.DRAGGING);

        drag.enter();
        indicator.verifyState(IndicatorState.REJECTING);
    }

    @Test
    public void testMovingOverDifferentStates() {
        drag = new Drag(drg1, drop2);
        drag.setDragIndicator(indicator);
        drag.setNumberOfSteps(20);
        super.testMovingOverDifferentStates();
    }

}
