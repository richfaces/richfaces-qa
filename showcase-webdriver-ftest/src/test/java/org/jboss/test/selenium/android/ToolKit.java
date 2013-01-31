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
package org.jboss.test.selenium.android;

import java.io.IOException;

import org.apache.commons.lang.Validate;

/**
 * The Android SDK driver. It provides basic functionality to
 * control running device.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ToolKit {

    public static String ADB_DIR = "platform-tools";

    private ToolKitConfiguration configuration;

    /**
     * Creates a new instance of ToolKit based on the given configuration
     *
     * @param configuration
     */
    public ToolKit(ToolKitConfiguration configuration) {
        Validate.notNull(configuration);
        this.configuration = configuration;
    }

    /**
     * Sends a key event to the running device and delays for the time set
     * in the configuration.
     *
     * @param key
     * @throws ToolKitException if the events can't be sent or the delay fails
     */
    public void sendKey(Key key) throws ToolKitException {
        sendKey(key, configuration.getSendKeyDelay());
    }

    /**
     * Sends a key event to the running device and delays for the given time
     *
     * @param key
     * @param delay amount of time in miliseconds
     * @throws ToolKitException if the events can't be sent or the delay fails
     */
    public void sendKey(Key key, long delay) throws ToolKitException {
        try {
            if (configuration.isInPath()) {
                Runtime.getRuntime().exec("adb shell input keyevent " + key.getCode());
            } else {
                Runtime.getRuntime().exec(configuration.getDirectory().getAbsolutePath() + "/" + ADB_DIR + "/adb shell input keyevent " + key.getCode());
            }
            Thread.sleep(delay);
        } catch(IOException e) {
            throw new ToolKitException("The key can't be sent to the device.", e);
        } catch(InterruptedException e) {
            throw new ToolKitException("The key can't be sent to the device.", e);
        }

    }

}
