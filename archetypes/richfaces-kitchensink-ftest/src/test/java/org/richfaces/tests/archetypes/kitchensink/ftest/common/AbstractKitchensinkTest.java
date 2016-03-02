/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.archetypes.kitchensink.ftest.common;

import static java.text.MessageFormat.format;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.richfaces.tests.configurator.unstable.UnstableTestConfigurator;
import org.testng.IHookCallBack;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AbstractKitchensinkTest extends Arquillian {

    protected final String CSV_EMAIL = "not a well-formed email address";
    protected final String CSV_ERROR_MSG = "The number of error messages after client side validation is wrong!";
    protected final String CSV_NAME_PATTERN = "must contain only letters and spaces";
    protected final String CSV_NAME_SIZE = "size must be between 1 and 25";
    protected final String CSV_NOT_EMPTY = "may not be empty";
    protected final String CSV_NOT_NULL = "may not be null";
    protected final String CSV_PHONE = "size must be between 10 and 12";
    protected final String CSV_PHONE_SIZE = "numeric value out of bounds (<12 digits>.<0 digits> expected)";

    protected final int WAIT_FOR_ERR_MSG_RENDER = 3;

    @ArquillianResource
    protected URL contextRoot;

    @Drone
    protected WebDriver webDriver;

    static {
        if (!System.getProperty("activated.maven.profiles").contains("browser")) {
            throw new RuntimeException("The tests depends on profile 'browser'. Use e.g. '-Dbrowser=ff', '-Dbrowser=cr', '-Dbrowser=ie'");
        }
    }

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, getWarFile());
    }

    private static File getWarFile() {
        File warFile = new File(System.getProperty("application.war"));
        if (!warFile.exists()) {
            throw new RuntimeException(format("The war at <{0}> does not exist. Use script at <archetypes/apps/richfaces-archetype-kitchensink.sh> to create a simple project.", warFile.getAbsolutePath()));
        }
        return warFile;
    }

    protected URL getDeployedURL() {
        return contextRoot;
    }

    protected String getUrlSuffix() {
        return null;
    }

    @BeforeMethod(dependsOnGroups = "arquillian")
    public void loadPage() throws MalformedURLException {
        webDriver.manage().deleteAllCookies();
        if (getUrlSuffix() == null) {
            webDriver.get(getDeployedURL().toExternalForm());
        } else {
            webDriver.get(new URL(getDeployedURL(), getUrlSuffix()).toExternalForm());
        }
    }

    /**
     * Overriding method from Arquillian to introduce new test execution behavior
     */
    @Override
    public void run(final IHookCallBack callBack, final ITestResult testResult) {
        super.run(UnstableTestConfigurator.getGuardedCallback(callBack), testResult);
    }
}
