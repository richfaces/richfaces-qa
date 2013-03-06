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
package org.richfaces.tests.archetypes.kitchensink.ftest.common;

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
import org.openqa.selenium.android.AndroidDriver;
import org.testng.annotations.BeforeMethod;


/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractKitchensinkTest extends Arquillian {

    protected final String ERROR_MSG_CSV = "The number of error messages after client side validation is wrong!";
    protected final String CSV_EMAIL = "not a well-formed email address";
    protected final String CSV_PHONE = "size must be between 10 and 12";
    protected final String SSV_NOT_EMPTY = "may not be empty";
    protected final String CSV_NOT_NULL = "may not be null";
    protected final String SSV_NAME_SIZE = "size must be between 1 and 25";
    protected final String SSV_PHONE_SIZE = "numeric value out of bounds (<12 digits>.<0 digits> expected)";
    protected final String SSV_NAME_PATTERN = "must contain only letters and spaces";

    protected final int WAIT_FOR_ERR_MSG_RENDER = 3;
    protected final String ANDORID_LOOPBACK = "10.0.2.2";

    @ArquillianResource
    protected URL contextRoot;

    @Drone
    protected WebDriver webDriver;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {

        String pathToAUT = System.getProperty("application.war");

        WebArchive war = ShrinkWrap.createFromZipFile(WebArchive.class, new File(pathToAUT));
        return war;
    }

    @BeforeMethod(groups = "arquillian")
    public void loadPage() throws MalformedURLException {
        if (getUrlSuffix() == null) {
            webDriver.get(getDeployedURL().toExternalForm());
        } else {
            webDriver.get(new URL(getDeployedURL(), getUrlSuffix()).toExternalForm());
        }
    }

    protected URL getDeployedURL() {
        if (!(webDriver instanceof AndroidDriver)) {
            return contextRoot;
        } else {
            String host = System.getProperty("host");
            try {
                return new URL(contextRoot.toString().replace(contextRoot.getHost(), host));
            } catch (MalformedURLException e) {
                throw new RuntimeException("You are attempting to load malformed URL");
            }
        }
    }

    protected String getUrlSuffix() {
        return null;
    }
}
