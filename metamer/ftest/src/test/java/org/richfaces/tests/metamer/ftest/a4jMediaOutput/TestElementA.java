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
package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.STRINGS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.richfaces.tests.metamer.bean.a4j.A4JMediaOutputBean;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jMediaOutput/elementA.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestElementA extends AbstractMediaOutputTest {

    private final Attributes<MediaOutputAttributes> mediaOutputAttributes = getAttributes();

    private String typeValue;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jMediaOutput/elementA.xhtml");
    }

    @Test
    public void init() throws IOException {
        assertEquals(mediaOutput.getText(), "This is a link", "The link text doesn't match.");
        assertTrue(
            getTextContentByUrlAttribute(mediaOutput, "href").contains(A4JMediaOutputBean.HTML_TEXT),
            "Target HTML page doesn't match."
        );
    }

    @Test
    public void testLang() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.lang, "cz");
    }

    @Test
    @UseWithField(field = "typeValue", valuesFrom = STRINGS, value = { "text/html", "image/png", "image/gif", "video/mpeg", "text/css", "audio/basic" })
    public void testType() {
        testHTMLAttribute(mediaOutput, mediaOutputAttributes, MediaOutputAttributes.type, typeValue);
    }
}
