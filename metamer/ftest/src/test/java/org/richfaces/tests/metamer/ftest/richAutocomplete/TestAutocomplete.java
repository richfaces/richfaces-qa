/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.Suggestion;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.autocomplete.SuggestionImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocomplete extends AbstractAutocompleteTest {

    @Inject
    @Use(strings = { "mouse", "keys" })
    private String scrollingType;

    @Inject
    @Use(booleans = { true, false })
    Boolean autofill;

    @Inject
    @Use(booleans = { true, false })
    Boolean selectFirst;

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

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenConfirm() throws InterruptedException {
        assertFalse(autocomplete.areSuggestionsAvailable());
        autocomplete.type("ala");
        waiting(1000);
        assertTrue(autocomplete.areSuggestionsAvailable());
        autocomplete.autocomplete();
        assertFalse(autocomplete.areSuggestionsAvailable());
        assertEquals(autocomplete.getInputValue().toLowerCase(), getExpectedStateForPrefix("ala", selectFirst).toLowerCase());
    }

    @Test(groups = "Future")
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
        Graphene.guardAjax(autocomplete).autocompleteWithSuggestion(expected, getScrollingType());
        assertEquals(autocomplete.getInputValue(), expected.getValue());
        assertEquals(getOutput(), expected.getValue());
    }

    protected ScrollingType getScrollingType() {
        return ScrollingType.valueOf("BY_" + scrollingType.toUpperCase());
    }
}
