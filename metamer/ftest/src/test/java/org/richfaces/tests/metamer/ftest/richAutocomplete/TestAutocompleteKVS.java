/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import java.net.URL;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for keeping visual state (KVS) for autocomplete on page:
 *  faces/components/richAutocomplete/autocomplete.xhtml
 *
 *  There were some problems with
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteKVS extends AbstractAutocompleteTest<SimplePage> {

    @FindBy(id="form:autocomplete")
    private AutocompleteComponentImpl<String> autocomplete;

    private AutocompleteReloadTester reloadTester = new AutocompleteReloadTester();

    @Override
    protected SimplePage createPage() {
        return new SimplePage();
    }

    @BeforeMethod
    public void setParser() {
        autocomplete.setSuggestionParser(new TextSuggestionParser());
    }

    @BeforeMethod
    public void prepareAutocomplete() {
        autocomplete.clear(ClearType.BACK_SPACE);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @Test(groups = {"keepVisualStateTesting"})
    public void testRefreshFullPage() {
        reloadTester.testFullPageRefresh();
    }

    @Test(groups = {"keepVisualStateTesting"})
    public void testRerenderAll() {
        reloadTester.testRerenderAll();
    }

    private class AutocompleteReloadTester extends AbstractWebDriverTest<SimplePage>.ReloadTester<String> {

        @Override
        public void doRequest(String inputValue) {
            autocomplete.type(inputValue);
        }

        @Override
        public void verifyResponse(String inputValue) {
            assertEquals(autocomplete.getInputValue(), inputValue);
        }

        @Override
        public String[] getInputValues() {
            return new String[] {"not-in-list-value"};
        }

    }

}
