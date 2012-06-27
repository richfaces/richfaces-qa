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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.model.AbstractModel;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22499 $
 */
public class Indicator extends AbstractModel<JQueryLocator> {

    private GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    private boolean defaultIndicator = false;
    private String acceptClass;
    private String rejectClass;
    private String draggingClass;

    public Indicator(String name, JQueryLocator root) {
        super(name, root);
    }

    public void setDefaultIndicator(boolean defaultIndicator) {
        this.defaultIndicator = defaultIndicator;
    }

    public void setAcceptClass(String acceptClass) {
        this.acceptClass = acceptClass;
    }

    public void setRejectClass(String rejectClass) {
        this.rejectClass = rejectClass;
    }

    public void setDraggingClass(String draggingClass) {
        this.draggingClass = draggingClass;
    }

    public boolean isVisible() {
        return selenium.isVisible(this);
    }

    public boolean belongsClass(String className) {
        if (className == null) {
            return false;
        }
        return selenium.belongsClass(this, className);
    }

    public boolean isAccepting() {
        return isVisible() && selenium.belongsClass(this, "rf-ind-acpt");
    }

    public boolean isDragging() {
        if (defaultIndicator) {
            return isVisible();
        }
        return isVisible() && selenium.belongsClass(this, "rf-ind-drag");
    }

    public boolean isRejecting() {
        return isVisible() && selenium.belongsClass(this, "rf-ind-rejt");
    }

    public void verifyState(IndicatorState state) {
        switch (state) {
            case HIDDEN:
                assertFalse(isVisible());
                break;
            case DRAGGING:
                assertTrue(isVisible());
                assertTrue(isDragging());
                assertFalse(isAccepting());
                assertFalse(isRejecting());
                verifyCustomClass(draggingClass, true);
                verifyCustomClass(acceptClass, false);
                verifyCustomClass(rejectClass, false);
                break;
            case ACCEPTING:
                assertTrue(isVisible());
                assertTrue(isDragging());
                assertTrue(isAccepting());
                assertFalse(isRejecting());
                verifyCustomClass(draggingClass, true);
                verifyCustomClass(acceptClass, true);
                verifyCustomClass(rejectClass, false);
                break;
            case REJECTING:
                assertTrue(isVisible());
                assertTrue(isDragging());
                assertFalse(isAccepting());
                assertTrue(isRejecting());
                verifyCustomClass(draggingClass, true);
                verifyCustomClass(acceptClass, false);
                verifyCustomClass(rejectClass, true);
                break;
            default:
                // default case required by checkstyle
                break;
        }
    }

    private void verifyCustomClass(String customClass, boolean shouldBePresent) {
        if (customClass == null) {
            return;
        }
        assertEquals(belongsClass(customClass), shouldBePresent);
    }

    public enum IndicatorState {
        HIDDEN, DRAGGING, ACCEPTING, REJECTING;
    }

}
