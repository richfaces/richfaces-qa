/**
 *
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.autocompleteAttributes;
import static org.richfaces.tests.metamer.ftest.richAutocomplete.AutocompleteAttributes.clientFilterFunction;
import static org.richfaces.tests.metamer.ftest.richAutocomplete.AutocompleteAttributes.mode;

import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.testng.annotations.Test;


/**
 * @author jjamrich
 *
 */
public class TestAutocompleteFiltering extends AbstractAutocompleteTest {

    private static final String CLIENT_FILTER_FUNCTION_NAME = "customClientFilterFunction";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/filtering.xhtml");
    }

    @Test
    public void testClientFilterFunction() {
        autocompleteAttributes.set(mode, "client");
        autocompleteAttributes.set(clientFilterFunction, CLIENT_FILTER_FUNCTION_NAME);

        autocomplete.typeKeys("No");

        assertTrue(autocomplete.isLabeledOptionAvailable("Springfield of Illinois"));
        assertTrue(autocomplete.isLabeledOptionAvailable("Raleigh of North Carolina"));
        assertTrue(autocomplete.isLabeledOptionAvailable("Bismarck of North Dakota"));

    }

}
