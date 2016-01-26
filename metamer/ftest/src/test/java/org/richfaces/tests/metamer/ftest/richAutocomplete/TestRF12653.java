/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12653 extends AbstractAutocompleteTest {

    private void checkVisibleSuggestions() {
        List<WebElement> suggestions = autocomplete.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 4);
        assertEquals(suggestions.get(0).getText(), "Alabama");
        assertEquals(suggestions.get(1).getText(), "Alaska");
        assertEquals(suggestions.get(2).getText(), "Arizona");
        assertEquals(suggestions.get(3).getText(), "Arkansas");
    }

    @Override
    public String getComponentTestPagePath() {
        return "richAutocomplete/autocomplete.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12653")
    public void testClosingSuggestionsPopupByBlur() {
        attsSetter()
            .setAttribute(AutocompleteAttributes.showButton).toValue(true)
            .setAttribute(AutocompleteAttributes.onclick).toValue("RichFaces.component('" + autocomplete.advanced().getRootElement().getAttribute("id") + "').showPopup();")
            .asSingleAction().perform();
        // fill in 'a', so there will be some suggestions available
        Graphene.guardAjax(autocomplete).type("a");
        // blur
        Graphene.guardAjax(getMetamerPage().getResponseDelayElement()).click();

        // focus the input element with attached @onclick
        autocomplete.advanced().getInput().advanced().getInputElement().click();
        // popup with suggestions will show up
        autocomplete.advanced().waitForSuggestionsToBeVisible().perform();
        // check
        checkVisibleSuggestions();
        // blur
        getMetamerPage().getResponseDelayElement().click();
        // popup with suggestions should hide
        autocomplete.advanced().waitForSuggestionsToBeNotVisible().perform();

        // now try the same with the autocomplete button (here is the issue)
        // click the autocomplete button
        WebElement button = autocomplete.advanced().getRootElement().findElement(By.className("rf-au-btn"));
        button.click();
        // popup with suggestions will show up
        autocomplete.advanced().waitForSuggestionsToBeVisible().perform();
        // check
        checkVisibleSuggestions();
        // blur
        getMetamerPage().getResponseDelayElement().click();
        // popup with suggestions should hide, HERE is the problem
        autocomplete.advanced().waitForSuggestionsToBeNotVisible().perform();
    }
}
