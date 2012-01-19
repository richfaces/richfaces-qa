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

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState.ACCEPTING;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState.DRAGGING;
import static org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState.REJECTING;

import org.jboss.arquillian.ajocado.actions.Drag;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.richDragIndicator.Indicator.IndicatorState;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 21352 $
 */
public abstract class AbstractDragNDropTest extends AbstractAjocadoTest {

    protected Draggable drg1 = new Draggable("drg1", jq("[id$=draggable1]"));
    protected Draggable drg2 = new Draggable("drg2", jq("[id$=draggable2]"));
    protected Draggable drg3 = new Draggable("drg3", jq("[id$=draggable3]"));

    protected Droppable drop1 = new Droppable("drop1", jq("[id$=droppable1]"));
    protected Droppable drop2 = new Droppable("drop2", jq("[id$=droppable2]"));

    protected Drag drag;
    protected Indicator indicator;

    @BeforeMethod
    public void setupIndicator() {
        indicator = new Indicator("ind", jq("div.rf-ind[id$=indicator]"));
    }

    protected void testMovingOverDifferentStates() {
        enterAndVerify(drop2, REJECTING);
        enterAndVerify(drop1, ACCEPTING);
        enterAndVerify(drg1, DRAGGING);
        enterAndVerify(drop1, ACCEPTING);
        enterAndVerify(drg2, DRAGGING);
        enterAndVerify(drop2, REJECTING);
        enterAndVerify(drg2, DRAGGING);
        enterAndVerify(drop1, ACCEPTING);
        enterAndVerify(drop2, REJECTING);
    }

    protected void enterAndVerify(ElementLocator<?> target, IndicatorState state) {
        drag.setDropTarget(target);
        drag.enter();
        indicator.verifyState(state);
    }
}
