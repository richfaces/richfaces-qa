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
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.STRINGS;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.By;
import org.richfaces.fragment.autocomplete.SelectOrConfirm;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteFormatting extends AbstractAutocompleteTest {

    private Boolean autofill;
    private Boolean selectFirst;

    @UseForAllTests(valuesFrom = STRINGS, value = { "div", "list", "table" })
    private String layout;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/fetchValueAttr.xhtml");
    }

    @BeforeMethod
    public void prepareProperties() {
        autocompleteAttributes.set(AutocompleteAttributes.autofill, autofill);
        autocompleteAttributes.set(AutocompleteAttributes.selectFirst, selectFirst);
        autocompleteAttributes.set(AutocompleteAttributes.layout, layout);
        if (autofill == null) {
            autofill = false;
        }
        if (selectFirst == null) {
            selectFirst = false;
        }
        autocomplete.clear();
    }

    /**
     * This should test combination of @var and @fetchValue attributes of autocomplete
     */
    @Test
    @Uses({
        @UseWithField(field = "autofill", valuesFrom = FROM_FIELD, value = "booleans"),
        @UseWithField(field = "selectFirst", valuesFrom = FROM_FIELD, value = "booleans")
    })
    public void testFormatting() {
        assertTrue(autocomplete.advanced().getSuggestionsElements().isEmpty());
        autocomplete.clear();
        SelectOrConfirm typed = Graphene.guardAjax(autocomplete).type("ala");
        assertFalse(autocomplete.advanced().getSuggestionsElements().isEmpty());
        Graphene.guardAjax(typed).confirm();
        autocomplete.advanced().waitForSuggestionsToBeNotVisible().perform();
        String expected = getExpectedStateForPrefix("ala", selectFirst).toLowerCase();
        String found = autocomplete.advanced().getInput().getStringValue().toLowerCase();
        assertTrue(found.startsWith(expected), "The input value should start with '" + expected + "', but '" + found
            + "' found.");
    }

    @Test
    public void testLayout() {
        Graphene.guardAjax(autocomplete).type("Co");

        Graphene.waitGui().until().element(getSuggestion("Colorado")).is().present();
        Graphene.waitGui().until().element(getSuggestion("[Denver]")).is().present();
        Graphene.waitGui().until().element(getSuggestion("Connecticut")).is().present();
        Graphene.waitGui().until().element(getSuggestion("[Hartford]")).is().present();

        Graphene.waitGui().until().element(getSuggestion("Hawaii")).is().not().present();
    }

    private By getSuggestion(String value) {
        switch (getLayout()) {
            case DIV:
                return ByJQuery.selector("div[id$=autocompleteItems] > div:contains('" + value + "')");
            case LIST:
                return ByJQuery.selector("ul[id$=autocompleteItems] > li:contains('" + value + "')");
            case TABLE:
                return ByJQuery.selector("table[id$=autocompleteItems] > tbody > tr > td:contains('" + value
                    + "')");
            default:
                throw new UnsupportedOperationException();
        }
    }

    private Layout getLayout() {
        return Layout.valueOf(layout.toUpperCase());
    }

    private static enum Layout {

        DIV, LIST, TABLE;
    }

}
