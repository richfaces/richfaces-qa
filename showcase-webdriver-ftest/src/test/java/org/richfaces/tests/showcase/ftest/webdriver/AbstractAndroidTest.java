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

import java.lang.reflect.Constructor;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.android.PropertyToolKitConfiguration;
import org.jboss.test.selenium.android.ToolKit;
import org.jboss.test.selenium.android.ToolKitConfiguration;
import org.richfaces.tests.showcase.ftest.webdriver.page.ShowcasePage;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractAndroidTest<Page extends ShowcasePage> extends AbstractWebDriverTest<Page> {

    private ToolKit toolKit;
    
    /**
     * Creates a new instance of {@link AbstractAndroidTest} with
     * the default configuration {@link PropertyTestConfiguration}
     * and default toolkit configuration {@link PropertyToolKitConfiguration}
     * 
     * @throws IllegalStateException if the android web driver isn't turned on
     */
    public AbstractAndroidTest() {
        this(new PropertyTestConfiguration(), new PropertyToolKitConfiguration("android.sdk"));
    }

    /**
     * Creates a new instance of {@link AbstractAndroidTest} with
     * the given configuration
     * 
     * @param testConfiguration
     * @param toolKitConfiguration
     * @throws IllegalStateException if the android web driver isn't turned on
     */
    public AbstractAndroidTest(TestConfiguration testConfiguration, ToolKitConfiguration toolKitConfiguration) {
        super(testConfiguration);
        Validate.notNull(toolKitConfiguration);
        this.toolKit = new ToolKit(toolKitConfiguration);       
        if (!getConfiguration().isAndroid()) {
            throw new IllegalStateException("The android test is used, but android web driver isn't turned on.");
        }
    }
    
    /**
     * Returns a toolkit to control running device through Android SDK.
     * Its configurations is set in the {@link Constructor}.
     * 
     * @return toolkit
     */
    protected ToolKit getToolKit() {
        return toolKit;
    }
    
}
