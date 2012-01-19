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
package org.jboss.test.selenium.android;

import java.io.File;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class PropertyToolKitConfiguration implements ToolKitConfiguration {

    public static final long DEFAULT_SEND_KEY_DELAY = 500;
    
    private File directory;
    private int inPath = -1;
    private String prefix;
    private long sendKeyDelay = -1;
    
    public PropertyToolKitConfiguration(String prefix) {
        this.prefix = prefix == null ? "" : (prefix.endsWith(".") ? prefix : (prefix + ".")); 
    }

    @Override
    public File getDirectory() {
        if (directory == null) {
            String dirname = System.getProperty(prefix + "dir") == null ? System.getenv("ANDROID_SDK_DIRECTORY") : System.getProperty(prefix + "dir");
            if (dirname == null) {
                throw new IllegalStateException("The android SDK directory is not set. Looking for " + prefix + "dir property.");
            }
            directory = new File(dirname);
        }
        return directory;
    }

    public boolean isInPath() {
        if (inPath == -1) {
            inPath = System.getProperty(prefix + "inpath") == null ? 0 : 1;
        }
        return inPath == 1;
    }
    
    @Override
    public long getSendKeyDelay() {
        if (sendKeyDelay == -1) {
            String delay = System.getProperty(prefix + "sendkey.delay");
            sendKeyDelay = delay == null ? DEFAULT_SEND_KEY_DELAY : Long.parseLong(delay);
        }
        return sendKeyDelay;
    }
    
}
