/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.archetypes.simpleapp;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.waitXhr;
import static org.jboss.test.selenium.locator.LocatorFactory.id;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;
import static org.testng.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.locator.ElementLocator;
import org.testng.annotations.Test;

/**
 * <p>
 * Tests that input reacts to keyup events by sending XHR request and rerendering output as greeting to given name.
 * </p>
 * 
 * <p>
 * If input has empty value, output is also empty.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestInput extends AbstractTestCase {

    final String inputName = "RichFaces Fan";
    Pattern pattern = Pattern.compile("Hello ([\\w ]+)!");

    ElementLocator<?> input = id("nameInput");
    ElementLocator<?> output = id("output");

    @Test
    public void test() {
        selenium.open(contextPath);
        selenium.waitForPageToLoad();

        for (int i = 0; i <= inputName.length(); i++) {
            String nameToType = inputName.substring(0, i);
            typeName(nameToType);
            assertEquals(parseNameFromOutput(), nameToType);
        }

        typeName("");
        assertEquals(parseNameFromOutput(), "");
    }

    private void typeName(String name) {
        selenium.type(input, name);
        waitXhr(selenium).fireEvent(input, Event.KEYUP);
    }

    private String parseNameFromOutput() {
        String outputText = selenium.getText(output);

        Matcher matcher = pattern.matcher(outputText);
        if (!matcher.find()) {
            if ("".equals(outputText)) {
                return "";
            }
            throw new IllegalStateException(format("Output text '{0}' doesn't match pattern '{1}'", outputText,
                pattern.toString()));
        }

        return matcher.group(1);
    }
}
