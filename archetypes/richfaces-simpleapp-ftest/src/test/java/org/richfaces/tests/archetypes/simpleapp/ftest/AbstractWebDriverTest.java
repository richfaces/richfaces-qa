/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.archetypes.simpleapp.ftest;

import static java.text.MessageFormat.format;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractWebDriverTest extends Arquillian {

    @Drone
    protected WebDriver browser;
    @ArquillianResource
    protected URL contextRoot;

    static {
        if (System.getProperty("browser") == null) {
            throw new RuntimeException("The tests depends on property 'browser'. Use e.g. '-Dbrowser=ff', '-Dbrowser=cr', '-Dbrowser=ie'");
        }
    }

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, getWarFile());
    }

    private static File getWarFile() {
        String warFilePath = System.getProperty("application.war");
        boolean isTomcatProfileActivated = System.getProperty("activated.maven.profiles").contains("tomcat");
        if (isTomcatProfileActivated) {
            warFilePath = warFilePath.replace("-jee6.war", ".war");
        }
        File warFile = new File(warFilePath);
        if (!warFile.exists()) {
            throw new RuntimeException(format("The war at <{0}> does not exist. Use script at <archetypes/apps/richfaces-archetype-simpleapp.sh> to create a simple project.", warFile.getAbsolutePath()));
        }
        return warFile;
    }

    /**
     * Initializes web driver to open a test page
     */
    @BeforeMethod(alwaysRun = true)
    public void initializePageUrl() {
        browser.get(contextRoot.toExternalForm());
    }
}
