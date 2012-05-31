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
package org.richfaces.tests.showcase.ftest.webdriver;

public class PropertyTestConfiguration implements TestConfiguration {

    public static final String DEFAULT_SKIN_NAME = "blueSky";
    public static final String DEFAULT_WEBDRIVER_STALE_TRIES = "5";
    public static final String DEFAULT_WEBDRIVER_TIMEOUT = "30";

    @Override
    public String getContextRoot() {
        return System.getProperty("context.root");
    }

    @Override
    public String getSkinName() {
        return System.getProperty("skin.name", DEFAULT_SKIN_NAME);
    }

    @Override
    public int getWebDriverElementTries() {
        return Integer.parseInt(System.getProperty("webdriver.stale.tries", DEFAULT_WEBDRIVER_STALE_TRIES));
    }

    @Override
    public int getWebDriverTimeout() {
        return Integer.parseInt(System.getProperty("webdriver.timeout", DEFAULT_WEBDRIVER_TIMEOUT));
    }

    @Override
    public boolean isMobile() {
        return "mobile".equals(System.getProperty("showcase.layout"));
    }

    @Override
    public boolean isVerbose() {
        return System.getProperty("verbose") != null;
    }

}
