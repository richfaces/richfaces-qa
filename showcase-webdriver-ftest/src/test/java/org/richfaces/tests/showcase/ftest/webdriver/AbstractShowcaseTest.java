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
package org.richfaces.tests.showcase.ftest.webdriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunAsClient
public abstract class AbstractShowcaseTest extends Arquillian {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());
    private TestConfiguration configuration;
    @ArquillianResource
    private URL deployedURL;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class,
            new File("target/showcase.war"));
        return war;
    }

    /**
     * Creates a new instance of {@link AbstractShowcaseTest} with
     * the given configuration
     *
     * @param configuration
     */
    protected AbstractShowcaseTest(TestConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Returns a path to the deployed application
     * @throws MalformedURLException
     */
    protected URL getDeployedURL() throws MalformedURLException {
        if (configuration.getContextRoot() != null) {
            return new URL(deployedURL.toString().replace(deployedURL.getHost(), configuration.getContextRoot()));
        } else {
            return deployedURL;
        }
    }

    /**
     * Creates a new instance of {@link AbstractShowcaseTest} with
     * the default configuration {@link PropertyTestConfiguration}
     *
     */
    protected AbstractShowcaseTest() {
        this(new PropertyTestConfiguration());
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
     * @throws MalformedURLException
     */
    protected URL getPath() throws MalformedURLException {
        return new URL(getDeployedURL(), getTestUrl());
    }

    /**
     * Returns a skin name which is used in tests.
     * The skin is defaultly set by the configuration.
     *
     * @return skin name
     * @see TestConfiguration#getSkinName()
     */
    protected String getSkinName() {
        return getConfiguration().getSkinName();
    }

    /**
     * Returns the url to test page to be opened by Selenium (without context root and context path)
     *
     * @return url to the test page to be opened by Selenium - it doesn't contain context root and context path
     */
    protected String getTestUrl() {
        if (getConfiguration().isMobile()) {
            return "mobile/#" + getDemoName() + ":" + getSampleName();
        } else {
            return "richfaces/component-sample.jsf?skin=" + getSkinName() + "&demo=" + getDemoName() + "&sample=" + getSampleName();
        }
    }

    /**
     * Returns a demo name which is currently tested
     *
     * @return demo name
     */
    protected abstract String getDemoName();

    /**
     * Returns a sample name which is currently tested
     *
     * @return sample name
     */
    protected abstract String getSampleName();

}
