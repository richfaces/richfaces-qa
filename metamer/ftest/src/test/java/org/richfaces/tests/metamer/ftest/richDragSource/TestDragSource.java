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
package org.richfaces.tests.metamer.ftest.richDragSource;

import static org.jboss.arquillian.ajocado.Ajocado.waitModel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.dragSourceAttributes;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.dragIndicator;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richDragSource.DragSourceAttributes.type;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.net.URL;

import org.jboss.arquillian.ajocado.actions.Drag;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.richfaces.tests.metamer.ftest.richDragIndicator.AbstractDragNDropTest;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22742 $
 */
public class TestDragSource extends AbstractDragNDropTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDragSource/simple.xhtml");
    }

    @Test
    public void testDefaultIndicator() {
        indicator = new Indicator("defaultIndicator", drg1.get(2));
        indicator.setDefaultIndicator(true);
        dragSourceAttributes.set(dragIndicator, "");

        drag = new Drag(drg1, drop1);
        drag.setDragIndicator(indicator);
        drag.setNumberOfSteps(10);
        testMovingOverDifferentStates();
    }

    @Test
    public void testCustomIndicator() {
        indicator = new Indicator("ind", jq("div.rf-ind[id$=indicator2]"));
        dragSourceAttributes.set(dragIndicator, "indicator2");

        drag = new Drag(drg1, drop1);
        drag.setDragIndicator(indicator);
        drag.setNumberOfSteps(20);
        testMovingOverDifferentStates();
    }

    @Test
    public void testRendered() {
        dragSourceAttributes.set(rendered, false);
        selenium.getPageExtensions().install();
        selenium.getRequestGuard().clearRequestDone();

        drag = new Drag(drg1, drop1);
        drag.setDragIndicator(indicator);
        drag.start();
        assertFalse(indicator.isVisible());
        drag.enter();
        assertFalse(indicator.isVisible());

        drag.drop();

        waitModel.timeout(5000).waitForTimeout();
        assertEquals(selenium.getRequestGuard().getRequestDone(), RequestType.NONE);
    }

    @Test
    public void testType() {
        dragSourceAttributes.set(type, "drg3");
        drag = new Drag(drg1, drop2);
        drag.setDragIndicator(indicator);
        enterAndVerify(drop2, IndicatorState.ACCEPTING);
        enterAndVerify(drg2, IndicatorState.DRAGGING);
        enterAndVerify(drop1, IndicatorState.REJECTING);
    }

}
