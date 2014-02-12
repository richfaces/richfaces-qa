/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.photoalbum.ftest.webdriver.tests;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.photoalbum.ftest.webdriver.pages.PhotoalbumPage;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RunAsClient
public abstract class AbstractPhotoalbumTest extends Arquillian {

    public static final String UNKNOWN_IMG_SRC = "-1";
    public static final String NO_OWNER = "-1";

    @Drone
    protected WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @Page
    protected PhotoalbumPage page;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/photoalbum.war"));
    }

//    @BeforeMethod(alwaysRun = true)
//    public void loadPage(Method m) {
//        if (browser == null) {
//            throw new SkipException("webDriver isn't initialized");
//        }
//        browser.manage().deleteAllCookies();
//        browser.get(contextPath.toString() + "/index.jsf");
//        // this method could also handle user logging
//        // i.e by introducing an annotation @LoggedUser used for marking test methods
//        // but https://issues.jboss.org/browse/ARQGRA-309
//        // so the logging has to be done manually
//    }
    @BeforeMethod(alwaysRun = true)
    public void loadPage() {
        if (browser == null) {
            throw new SkipException("webDriver isn't initialized");
        }
        browser.manage().deleteAllCookies();
        browser.get(contextPath.toString() + "/index.jsf");
    }

    @AfterMethod(alwaysRun = true)
    public void logoutAfter(ITestResult m) throws Exception {
        if (Utils.isVisible(page.getHeaderPanel().getLogoutLink())) {
            logout();
        }
    }

    public void login() {
        login("amarkhel");
    }

    public void login(String user) {
        page.login(user, "12345");
    }

    public void login(String user, String password) {
        page.login(user, password);
    }

    public void logout() {
        page.logout();
    }
}
