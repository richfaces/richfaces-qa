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

import org.jboss.arquillian.graphene.context.GrapheneContext;
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
     * Executes jQuery command on input element. E.g. to trigger click use jQ("click()", element).
     * @param cmd command to be executed
     * @param element element on which the command will be executed
     */
    public static void jQ(String cmd, WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) GrapheneContext.getProxy();
        String jQueryCmd = String.format("jQuery(arguments[0]).%s", cmd);
        executor.executeScript(jQueryCmd, element);
    }

    /**
     * Executes jQuery trigger command on input element. Useful for easy triggering
     * of JavaScript events like click, dblclick, mouseout...
     * @param event event to be triggered
     * @param element element on which the command will be executed
     */
    public static void triggerJQ(String event, WebElement element) {
        jQ(String.format("trigger('%s')", event), element);
    }
}
