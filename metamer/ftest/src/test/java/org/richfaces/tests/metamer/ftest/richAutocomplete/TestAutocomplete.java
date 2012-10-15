/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import java.net.URL;
import java.util.List;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.Suggestion;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.autocompleteAttributes;
import org.richfaces.tests.page.fragments.impl.autocomplete.SuggestionImpl;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocomplete extends AbstractAutocompleteTest<SimplePage> {

    @FindBy(id="form:autocomplete")
    protected AutocompleteComponentImpl<String> autocomplete;

    @Inject
    @Use(strings= { "mouse", "keys" })
    private String scrollingType;

    @Inject
    @Use(booleans = { true, false })
    Boolean autofill;

    @Inject
    @Use(booleans = { true, false })
    Boolean selectFirst;

    List<Capital> capitals = Model.unmarshallCapitals();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @BeforeMethod
    public void setParser() {
        autocomplete.setSuggestionParser(new TextSuggestionParser());
    }

    @BeforeMethod
    public void prepareProperties() {
        autocompleteAttributes.set(AutocompleteAttributes.autofill, autofill);
        autocompleteAttributes.set(AutocompleteAttributes.selectFirst, selectFirst);
        if (autofill == null) {
            autofill = false;
        }
        if (selectFirst == null) {
            selectFirst = false;
        }
        autocomplete.clear(ClearType.BACK_SPACE);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenConfirm() throws InterruptedException {
        assertFalse(autocomplete.areSuggestionsAvailable());
        autocomplete.type("ala");
        assertTrue(autocomplete.areSuggestionsAvailable());
        autocomplete.autocomplete();
        assertFalse(autocomplete.areSuggestionsAvailable());
        assertEquals(autocomplete.getInputValue().toLowerCase(), getExpectedStateForPrefix("ala", selectFirst).toLowerCase());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenDeleteAll() {
        assertFalse(autocomplete.areSuggestionsAvailable());
        autocomplete.type("ala");
        assertTrue(autocomplete.areSuggestionsAvailable());
        autocomplete.clear(ClearType.BACK_SPACE);
        assertFalse(autocomplete.areSuggestionsAvailable());
        autocomplete.type("ala");
        assertTrue(autocomplete.areSuggestionsAvailable());
    }

    @Test
    public void testSimpleSelection() throws InterruptedException {
        // this item is 2nd if type filter "ala", so it ensure that it was not picked first item
       Suggestion<String> expected = new SuggestionImpl<String>("Alaska");
       autocomplete.type("ala");
       autocomplete.autocompleteWithSuggestion(expected, getScrollingType());
       assertEquals(autocomplete.getInputValue(), expected.getValue());
    }

    @Override
    protected SimplePage createPage() {
        return new SimplePage();
    }
    protected ScrollingType getScrollingType() {
        return ScrollingType.valueOf("BY_" + scrollingType.toUpperCase());
    }
}
