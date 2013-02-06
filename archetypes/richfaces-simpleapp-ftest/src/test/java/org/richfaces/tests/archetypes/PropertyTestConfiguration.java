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
package org.richfaces.tests.archetypes;

import java.io.File;

public class PropertyTestConfiguration implements TestConfiguration {

    public File getApplicationWar() {
        return new File(getProperty("application.war"));
    }

    @Override
    public String getContextPath() {
        return getProperty("context.path");
    }

    @Override
    public String getContextRoot() {
        return getProperty("context.root");
    }

    @Override
    public int getWebDriverElementTries() {
        return Integer.parseInt(getProperty("webdriver.stale.tries"));
    }

    private String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("The property <" + key + "> is not set.");
        }
        return value;
    }

}
