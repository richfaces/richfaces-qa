/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.jboss.test.selenium.android;

/**
 * <a href="http://thecodeartist.blogspot.com/2011/03/simulating-keyevents-on-android-device.html">
 * Simulating Key Events on Android Device</a>
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public enum Key {

    UP_ARROW(19), DOWN_ARROW(20), LEFT_ARROW(21), RIGHT_ARROW(22),
    A(29), B(30), C(31), D(32), E(33), F(34), G(35), H(36), I(37),
    J(38), K(39), L(40), ENTER(66), DELETE(67);

    private int code;

    private Key(int code) {
        this.code = code;
    }

    /**
     * Returns a key code which is used by Android SDK
     *
     * @return a key code
     */
    public int getCode() {
        return code;
    }

}