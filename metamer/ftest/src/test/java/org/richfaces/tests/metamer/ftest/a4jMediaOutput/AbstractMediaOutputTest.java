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
package org.richfaces.tests.metamer.ftest.a4jMediaOutput;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractMediaOutputTest extends AbstractGrapheneTest {

    protected static final JQueryLocator MEDIA_OUTPUT = pjq("*[id$=mediaOutput]");

    protected String getTextContentByUrlAttribute(AttributeLocator<?> urlAttribute) throws IOException {
        URL url = buildUrl(contextPath, selenium.getAttribute(urlAttribute));
        BufferedReader input = null;
        StringBuilder foundContent = new StringBuilder();
        try {
            input = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while((line = input.readLine()) != null) {
                foundContent.append(line);
            }
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return foundContent.toString();
    }

}
