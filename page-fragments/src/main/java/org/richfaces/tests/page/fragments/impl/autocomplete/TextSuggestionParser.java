package org.richfaces.tests.page.fragments.impl.autocomplete;

import org.jboss.arquillian.graphene.component.object.api.autocomplete.Suggestion;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.SuggestionParser;
import org.openqa.selenium.WebElement;

public class TextSuggestionParser implements SuggestionParser<String> {

    @Override
    public Suggestion<String> parse(WebElement rootOfSuggestion) {
        String data = rootOfSuggestion.getText();
        Suggestion<String> suggestion = new SuggestionImpl<String>(data);

        return suggestion;
    }
}
