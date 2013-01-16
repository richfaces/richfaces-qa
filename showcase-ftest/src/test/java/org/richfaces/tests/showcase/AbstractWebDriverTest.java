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
package org.richfaces.tests.showcase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.android.AndroidDriver;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractWebDriverTest extends AbstractShowcaseTest {

    @Drone
    protected WebDriver webDriver;

    @BeforeMethod
    public void loadPage() {

        String addition = getAdditionToContextRoot();

        this.contextRoot = getContextRoot();

        webDriver.get(URLUtils.buildUrl(contextRoot, "/showcase/", addition).toExternalForm());
    }

    @Override
    protected URL getContextRoot() {
        URL contextRootFromParent = super.getContextRoot();
        if (webDriver instanceof AndroidDriver) {
            try {
                return new URL(contextRootFromParent.toExternalForm().replace(contextRootFromParent.getHost(), "10.0.2.2"));
            } catch (MalformedURLException ex) {
                Logger.getLogger(AbstractWebDriverTest.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            return contextRootFromParent;
        }
    }
}
