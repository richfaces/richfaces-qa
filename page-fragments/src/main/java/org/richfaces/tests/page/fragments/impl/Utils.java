/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl;

import java.util.Iterator;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public final class Utils {

    /**
     * Returns Locations of input element.
     * @see Locations
     */
    public static Locations getLocations(WebElement root) {
        Point topLeft = root.getLocation();
        Dimension dimension = root.getSize();
        Point topRight = topLeft.moveBy(dimension.getWidth(), 0);
        Point bottomRight = topRight.moveBy(0, dimension.getHeight());
        Point bottomLeft = topLeft.moveBy(0, dimension.getHeight());
        return new Locations(topLeft, topRight, bottomLeft, bottomRight);
    }

    /**
     * Returns text from given hidden element. WebDriver, in this case, returns empty String.
     * @param element not visible element
     */
    public static String getTextFromHiddenElement(WebElement element) {
        GrapheneContext context = ((GrapheneProxyInstance) element).getContext();
        return returningJQ((JavascriptExecutor) context.getWebDriver(JavascriptExecutor.class), "text()", element);
    }

    /**
     * Executes jQuery command on input element. E.g. to trigger click use jQ("click()", element).
     * @param cmd command to be executed
     * @param element element on which the command will be executed
     */
    public static void jQ(JavascriptExecutor executor, String cmd, WebElement element) {
        String jQueryCmd = String.format("jQuery(arguments[0]).%s", cmd);
        executor.executeScript(jQueryCmd, unwrap(element));
    }

    /**
     * Executes returning jQuery command on input element. E.g. to get a position
     * of element from top of the page use returningjQ("position().top", element).
     * @param cmd command to be executed
     * @param element element on which the command will be executed
     */
    public static String returningJQ(JavascriptExecutor executor, String cmd, WebElement element) {
        String jQueryCmd = String.format("x = jQuery(arguments[0]).%s ; return x;", cmd);
        return String.valueOf(executor.executeScript(jQueryCmd, unwrap(element)));
    }

    private static boolean _tolerantAssertPointEquals(Point p1, Point p2, int xTolerance, int yTolerance) {
        return (Math.abs(p1.x - p2.x) <= xTolerance && Math.abs(p1.y - p2.y) <= yTolerance);
    }

    /**
     * Asserts that two points are equal with some allowed tolerance.
     */
    public static void tolerantAssertPointEquals(Point p1, Point p2, int xTolerance, int yTolerance, String message) {
        if (!_tolerantAssertPointEquals(p1, p2, xTolerance, yTolerance)) {
            throw new AssertionError("The points are not equal or not in tolerance.\n"
                    + " The tolerance for x axis was: " + xTolerance
                    + ". The tolerance for y axis was: " + yTolerance + ".\n"
                    + "First point: " + p1 + "\n"
                    + "Second point: " + p2 + ".\n"
                    + message);
        }
    }

    /**
     * Asserts that two locations are equal with some allowed tolerance.
     */
    public static void tolerantAssertLocationsEquals(Locations l1, Locations l2, int xTolerance, int yTolerance, String message) {
        Iterator<Point> it1 = l1.iterator();
        Iterator<Point> it2 = l2.iterator();
        Point p1, p2;
        while (it1.hasNext()) {
            p1 = it1.next();
            p2 = it2.next();
            if (!_tolerantAssertPointEquals(p1, p2, xTolerance, yTolerance)) {
                throw new AssertionError("The locations are not equal or are not in tolerance.\n"
                        + "First location: " + l1 + ".\n"
                        + "Second location: " + l2 + ".\n"
                        + "Diverging point: " + p1 + " (first), " + p2 + " (second).\n"
                        + message);
            }
        }
    }

    /**
     * Asserts that elements locations and some other locations are equal with some allowed tolerance.
     */
    public static void tolerantAssertLocationsEquals(WebElement element, Locations l2, int xTolerance, int yTolerance, String message) {
        tolerantAssertLocationsEquals(Utils.getLocations(element), l2, xTolerance, yTolerance, message);
    }

    /**
     * Executes jQuery trigger command on input element. Useful for easy triggering
     * of JavaScript events like click, dblclick, mouseout...
     * @param event event to be triggered
     * @param element element on which the command will be executed
     */
    public static void triggerJQ(JavascriptExecutor executor, String event, WebElement element) {
        jQ(executor, String.format("trigger('%s')", event), element);
    }

    public static WebElement unwrap(WebElement e) {
        WebElement result = e;
        while (GrapheneProxy.isProxyInstance(result)) {
            result = ((GrapheneProxyInstance) result).unwrap();
        }
        return result;
    }
}
