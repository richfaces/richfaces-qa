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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteFiltering extends AbstractAutocompleteTest {

    private static final String CLIENT_FILTER_FUNCTION_NAME = "customClientFilterFunction";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/filtering.xhtml");
    }

    @Test
    public void testClientFilterFunction() {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        autocompleteAttributes.set(AutocompleteAttributes.clientFilterFunction, CLIENT_FILTER_FUNCTION_NAME);

        autocomplete.type("No");

        List<WebElement> found = autocomplete.advanced().getSuggestionsElements();
        assertNotNull(found, "Suggestions aren't available.");
        assertFalse(found.isEmpty(), "Suggestions aren't available.");

        String[] expected = new String[]{ "Springfield of Illinois", "Raleigh of North Carolina",
            "Bismarck of North Dakota" };

        Set<String> suggestions = new HashSet<String>();
        for (WebElement suggestion : found) {
            suggestions.add(suggestion.getText());
        }

        for (String text : expected) {
            assertTrue(suggestions.contains(text), "Suggestions should contain '" + text + "', found " + suggestions + ".");
        }
    }
}
