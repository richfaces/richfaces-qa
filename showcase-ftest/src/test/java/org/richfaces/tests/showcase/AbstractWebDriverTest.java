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

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractWebDriverTest<P> extends AbstractShowcaseTest {

    @Drone
    protected WebDriver webDriver;

    @Page
    protected P page;

    @BeforeMethod
    public void loadPage() {
        this.contextRoot = getContextRoot();

        if (runInPortalEnv) {
            webDriver.get(format("{0}://{1}:{2}/{3}",
                contextRoot.getProtocol(), contextRoot.getHost(), contextRoot.getPort(), "portal/classic/showcase"));
            webDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            String setTextQuery = "document.querySelector(\"input[id$='portalForm:{0}']\").value = '{1}';";
            js.executeScript(format(setTextQuery, "seleniumTestDemo", getDemoName()));
            js.executeScript(format(setTextQuery, "seleniumTestSample", getSampleName()));
            js.executeScript("document.querySelector(\"a[id$='portalForm:redirectToPortlet']\").click()");
        } else {
            webDriver.get(URLUtils.buildUrl(contextRoot, "/showcase/", getAdditionToContextRoot())toExternalForm());
        }
    }
}
