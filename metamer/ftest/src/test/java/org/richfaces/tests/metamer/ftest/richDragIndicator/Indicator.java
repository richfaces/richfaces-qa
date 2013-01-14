/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.test.selenium.support.ui.ElementPresent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: $
 */
public class Indicator {

    private static final String CLASS = "class";

    private WebDriver driver = GrapheneContext.getProxy();
    private WebElement indicator;

    private boolean defaultIndicator = false;
    private String acceptClass;
    private String rejectClass;
    private String draggingClass;

    WebElement activeIndicator;

    private ElementPresent elementPresent = ElementPresent.getInstance();

    public Indicator(WebElement indicator) {
        this.indicator = indicator;
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
        if (defaultIndicator) {
            return elementPresent.element(indicator).apply(driver)
                && indicator.isDisplayed();
        } else {
            return elementPresent.element(getActiveIndicator()).apply(driver)
                && getActiveIndicator().isDisplayed();
        }
    }

    public boolean belongsClass(String className) {
        if (className == null) {
            return false;
        }
        return indicator.getAttribute(CLASS).contains(className);
    }

    public boolean isAccepting() {
        return getActiveIndicator().getAttribute(CLASS).contains("rf-ind-acpt");
    }

    public boolean isDragging() {
        return getActiveIndicator().getAttribute(CLASS).contains("rf-ind-drag");
    }

    public boolean isRejecting() {
        return getActiveIndicator().getAttribute(CLASS).contains("rf-ind-rejt");
    }

    public void verifyState(IndicatorState state) {
        switch (state) {
            case HIDDEN:
                assertFalse(isVisible());
                break;
            case DRAGGING:
                assertTrue(isVisible());
                assertTrue(defaultIndicator ? true : isDragging());
                assertFalse(defaultIndicator ? false : isAccepting());
                assertFalse(defaultIndicator ? false : isRejecting());
                verifyCustomClass(draggingClass, true);
                verifyCustomClass(acceptClass, false);
                verifyCustomClass(rejectClass, false);
                break;
            case ACCEPTING:
                assertTrue(isVisible());
                assertTrue(defaultIndicator ? true : isDragging());
                assertTrue(defaultIndicator ? true : isAccepting());
                assertFalse(defaultIndicator ? false : isRejecting());
                verifyCustomClass(draggingClass, true);
                verifyCustomClass(acceptClass, true);
                verifyCustomClass(rejectClass, false);
                break;
            case REJECTING:
                assertTrue(isVisible());
                assertTrue(defaultIndicator ? true : isDragging());
                assertFalse(defaultIndicator ? false : isAccepting());
                assertTrue(defaultIndicator ? true : isRejecting());
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

    public WebElement getIndicator(By by) {
        return driver.findElement(by);
    }

    public WebElement getActiveIndicator() {
        return driver.findElement(By.cssSelector("body > div.rf-ind"));
    }

    public enum IndicatorState {
        HIDDEN, DRAGGING, ACCEPTING, REJECTING;
    }

}
