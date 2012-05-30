/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.mediaOutputAttributes;

import java.net.URL;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestElementAWebDriver extends AbstractWebDriverTest {

    private MediaOutputPage page;
    @Inject
    @Use(empty = false)
    private String typeValue;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jMediaOutput/elementA.xhtml");
    }

    @BeforeMethod
    public void loadPage() {
        page = new MediaOutputPage();
        injectWebElementsToPage(page);
    }

    @Test
    public void testLang() {
        testHTMLAttribute(page.link, mediaOutputAttributes, MediaOutputAttributes.lang, "cz");
    }

    @Test
    @Use(field = "typeValue", strings = { "text/html", "image/png", "image/gif", "video/mpeg", "text/css", "audio/basic" })
    public void testType() {
        testHTMLAttribute(page.link, mediaOutputAttributes, MediaOutputAttributes.type, typeValue);
    }

    private class MediaOutputPage {

        @FindBy(css = "a[id$=mediaOutput]")
        WebElement link;
    }
}
