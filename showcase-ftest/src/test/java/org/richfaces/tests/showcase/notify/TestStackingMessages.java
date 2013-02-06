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
package org.richfaces.tests.showcase.notify;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.JQueryScriptWindowObject;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestStackingMessages extends AbstractNotifyTest {

    /* *********************************************************
     * Constants*********************************************************
     */

    private final int TIMEOUT_RENDER = 2000;
    private final int TIMEOUT_DISAPPEAR = 11000;

    /* ***************************************************************************************************
     * Locators***************************************************************************************************
     */

    private JQueryLocator renderFirstButtonTopLeft = jq("input[type=submit]:eq(0)");
    private JQueryLocator renderSecondBottomRight = jq("input[type=submit]:eq(1)");

    /* **********************************************************************
     * Tests*********************************************************************
     */

    @Test
    public void testRenderFirst() {
        waitUntilNotifyDissappeares(TIMEOUT_DISAPPEAR);

        guardXhr(selenium).click(renderFirstButtonTopLeft);

        waitUntilNotifyAppears(TIMEOUT_RENDER);

        Point notifyPosition = selenium.getElementPosition(notify);

        JQueryScriptWindowObject heightJQ = new JQueryScriptWindowObject("height()");
        JQueryScriptWindowObject widthJQ = new JQueryScriptWindowObject("width()");

        String screenHeight = selenium.getEval(heightJQ);
        String screenWidth = selenium.getEval(widthJQ);

        System.err.println(screenHeight + " " + screenWidth);
        System.err.println(notifyPosition.getX() + " " + notifyPosition.getY());

        assertTrue(notifyPosition.getX() < ((Integer.valueOf(screenWidth) / 10)), "The X coordination is wrong");
        assertTrue(notifyPosition.getY() < ((Integer.valueOf(screenHeight) / 10)), "The Y coordination is wrong");
    }

    @Test
    public void testRenderSecond() {
        waitUntilNotifyDissappeares(TIMEOUT_DISAPPEAR);

        guardXhr(selenium).click(renderSecondBottomRight);

        waitUntilNotifyAppears(TIMEOUT_RENDER);

        Point notifyPosition = selenium.getElementPosition(notify);

        JQueryScriptWindowObject heightJQ = new JQueryScriptWindowObject("height()");
        JQueryScriptWindowObject widthJQ = new JQueryScriptWindowObject("width()");

        String screenHeight = selenium.getEval(heightJQ);
        String screenWidth = selenium.getEval(widthJQ);

        System.err.println(screenHeight + " " + screenWidth);
        System.err.println(notifyPosition.getX() + " " + notifyPosition.getY());

        assertTrue(notifyPosition.getX() > ((Integer.valueOf(screenWidth) / 10) * 6), "The X coordination is wrong");
        assertTrue(notifyPosition.getY() > ((Integer.valueOf(screenHeight) / 10) * 8), "The Y coordination is wrong");
    }

}
