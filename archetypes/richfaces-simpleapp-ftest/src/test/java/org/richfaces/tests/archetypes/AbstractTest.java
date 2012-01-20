/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import org.jboss.arquillian.testng.Arquillian;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractTest extends Arquillian {

    private TestConfiguration configuration;

    /**
     * Creates a new instance of {@link AbstractShowcaseTest} with
     * the given configuration
     *
     * @param configuration
     */
    protected AbstractTest(TestConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Returns a test configuration
     *
     * @return test configuration
     */
    protected TestConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Returns a full path where the test page is located
     *
     * @return full path with the demo
     */
    protected String getPath() {
        return getConfiguration().getContextRoot() + "/" + getConfiguration().getContextPath() + "/" + getTestUrl();
    }

    /**
     * Returns the url to test page to be opened by Selenium (without context root and context path)
     *
     * @return url to the test page to be opened by Selenium - it doesn't contain context root and context path
     */
    protected abstract String getTestUrl();

}
