/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.autocompleteAttributes;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteFormatting extends AbstractAutocompleteTest{

    @FindBy(id="form:autocomplete")
    private AutocompleteComponentImpl<String> autocomplete;

    @Inject
    @Use(booleans = { true, false })
    Boolean autofill;

    @Inject
    @Use(booleans = { true, false })
    Boolean selectFirst;

    @Inject
    @Use(strings= {"div", "list", "table"})
    String layout;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/fetchValueAttr.xhtml");
    }

    @BeforeMethod
    public void setParser() {
        autocomplete.setSuggestionParser(new TextSuggestionParser());
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
        autocomplete.clear(ClearType.BACK_SPACE);
    }

    /**
     * This should test combination of @var and @fetchValue attributes of autocomplete
     */
    @Test
    public void testFormatting() {
        assertFalse(autocomplete.areSuggestionsAvailable());
        autocomplete.clear(ClearType.BACK_SPACE);
        autocomplete.type("ala");
        assertTrue(autocomplete.areSuggestionsAvailable());
        autocomplete.autocomplete();
        Graphene.waitGui().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !autocomplete.areSuggestionsAvailable();
            }
        });
        String expected = getExpectedStateForPrefix("ala", selectFirst).toLowerCase();
        String found = autocomplete.getInputValue().toLowerCase();
        assertTrue(found.startsWith(expected), "The input value should start with '"+expected+"', but '" + found + "' found.");
    }

    @Test
    public void testLayout() {
        autocomplete.type("Co");
        assertTrue(Graphene.element(getSuggestion("Colorado")).isPresent().apply(driver));
        assertTrue(Graphene.element(getSuggestion("[Denver]")).isPresent().apply(driver));
        assertTrue(Graphene.element(getSuggestion("Connecticut")).isPresent().apply(driver));
        assertTrue(Graphene.element(getSuggestion("[Hartford]")).isPresent().apply(driver));
    }

    private By getSuggestion(String value) {
        switch(getLayout()) {
            case DIV:
                return By.xpath("//div[@id='form:autocompleteItems']/div[contains(text(), '" + value + "')]");
            case LIST:
                return By.xpath("//ul[@id='form:autocompleteItems']/li[contains(text(), '" + value + "')]");
            case TABLE:
                return By.xpath("//table[@id='form:autocompleteItems']/tbody/tr/td[contains(text(), '" + value + "')]");
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
