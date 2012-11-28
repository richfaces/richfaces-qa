/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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

import java.util.Arrays;
import java.util.Iterator;
import org.openqa.selenium.Point;

/**
 * Helper class for storing locations of borders.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Locations implements Iterable<Point> {

    private final Point topLeft;
    private final Point topRight;
    private final Point bottomLeft;
    private final Point bottomRight;

    public Locations(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getTopRight() {
        return topRight;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    /**
     * Returns new instance with moved borders.
     * @param x
     * @param y
     * @return
     */
    public Locations moveAllBy(int x, int y) {
        return new Locations(topLeft.moveBy(x, y), topRight.moveBy(x, y),
                bottomLeft.moveBy(x, y), bottomRight.moveBy(x, y));
    }

    @Override
    public Iterator<Point> iterator() {
        return Arrays.asList(topLeft, topRight, bottomLeft, bottomRight).iterator();
    }

    @Override
    public String toString() {
        return "Locations{" + "topLeft=" + topLeft + ", topRight=" + topRight + ", bottomLeft=" + bottomLeft + ", bottomRight=" + bottomRight + '}';
    }
}
