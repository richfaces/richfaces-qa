/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.Suggestion;
import org.jsoup.safety.Cleaner;
import org.openqa.selenium.support.FindBy;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.autocompleteAttributes;
import org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.SuggestionImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import org.testng.Assert;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteFiltering extends AbstractAutocompleteTest<SimplePage>{

    private static final String CLIENT_FILTER_FUNCTION_NAME = "customClientFilterFunction";

    @FindBy(id="form:autocomplete")
    private AutocompleteComponentImpl<String> autocomplete;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/filtering.xhtml");
    }

    @BeforeMethod
    public void setParser() {
        autocomplete.setSuggestionParser(new TextSuggestionParser());
    }

    @Override
    protected SimplePage createPage() {
        return new SimplePage();
    }

    @Test
    public void testClientFilterFunction() throws InterruptedException {
        autocompleteAttributes.set(AutocompleteAttributes.mode, "client");
        autocompleteAttributes.set(AutocompleteAttributes.clientFilterFunction, CLIENT_FILTER_FUNCTION_NAME);

        autocomplete.type("No");

        List<Suggestion<String>> found = autocomplete.getAllSuggestions();
        Assert.assertNotNull(found, "Suggestions aren't available.");
        Set<Suggestion<String>> suggestions = new HashSet<Suggestion<String>>(found);

        String[] expected = new String[] {"Springfield of Illinois", "Raleigh of North Carolina", "Bismarck of North Dakota"};

        for (String text: expected) {
            assertTrue(suggestions.contains(new SuggestionImpl<String>(text)), "Suggestions should contain '"+text+"', found " + suggestions + ".");
        }
    }


}
