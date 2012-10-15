/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import java.net.URL;
import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.autocompleteAttributes;
import org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteAttributes extends AbstractAutocompleteTest<SimplePage>{

    private static final String PHASE_LISTENER_LOG_FORMAT = "*1 value changed: {0} -> {1}";

    @FindBy(id="form:autocomplete")
    private AutocompleteComponentImpl<String> autocomplete;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
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
    public void testClientFilterFunction() {
        autocompleteAttributes.set(AutocompleteAttributes.clientFilterFunction, Boolean.TRUE);
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testValueChangeListener() {
        autocomplete.clear(ClearType.BACK_SPACE);
        autocomplete.type("something");
        getPage().blur();

        Graphene.waitAjax().until(Graphene.element(getPage().getOutput()).textEquals("something"));

        autocomplete.clear(ClearType.BACK_SPACE);
        autocomplete.type("something else");
        getPage().blur();
        // valueChangeListener output as 4th record
        Graphene.waitAjax().until(Graphene.element(getPage().getOutput()).textEquals("something else"));
        assertEquals(getPage().getPhases().get(3), format(PHASE_LISTENER_LOG_FORMAT, "something", "something else"));
    }

    @Test(groups = { "4.Future" })
    @Templates(value = { "richPopupPanel" })
    public void testValueChangeListenerInPopupPanel() {
        testValueChangeListener();
    }
}
