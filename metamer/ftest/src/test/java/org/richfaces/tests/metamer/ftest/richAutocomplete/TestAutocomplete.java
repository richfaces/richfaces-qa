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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.autocomplete.SelectOrConfirm;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent.ClearType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocomplete extends AbstractAutocompleteTest {

    @Inject
    @Use(empty = true)
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

//    @BeforeMethod
//    public void setParser() {
//        autocomplete.setSuggestionParser(new TextSuggestionParser());
//    }

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
        autocomplete.advanced().getInput().clear(ClearType.BACKSPACE);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenConfirm() {
        assertTrue(autocomplete.advanced().getSuggestions().isEmpty());
        SelectOrConfirm typed = Graphene.guardAjax(autocomplete).type("ala");
        assertFalse(autocomplete.advanced().getSuggestions().isEmpty());
        Graphene.guardAjax(typed).confirm();
        assertTrue(autocomplete.advanced().getSuggestions().isEmpty());
        String expectedStateForPrefix = getExpectedStateForPrefix("ala", selectFirst);
        assertEquals(autocomplete.advanced().getInput().getStringValue(), expectedStateForPrefix);
        checkOutput(expectedStateForPrefix);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenDeleteAll() {
        Graphene.guardAjax(autocomplete).type("ala");
        autocomplete.advanced().getInput().clear(ClearType.BACKSPACE);
        autocomplete.advanced().waitForSuggestionsHide();
        assertTrue(autocomplete.advanced().getSuggestions().isEmpty());
        Graphene.guardAjax(autocomplete).type("ala");
        assertFalse(autocomplete.advanced().getSuggestions().isEmpty());
    }

//    @Test
//    @Use(field = "scrollingType", strings = { "mouse", "keys" })
//    public void testSimpleSelection() {
//        // this item is 2nd if type filter "ala", so it ensure that it was not picked first item
//        Suggestion<String> expected = new SuggestionImpl<String>("Alaska");
//        Graphene.guardAjax(autocomplete).type("ala");
//        Graphene.guardAjax(autocomplete).autocompleteWithSuggestion(expected, getScrollingType());
//        checkOutput(expected.getValue());
//        assertEquals(autocomplete.getInputValue(), expected.getValue());
//    }

    protected ScrollingType getScrollingType() {
        return ScrollingType.valueOf("BY_" + scrollingType.toUpperCase());
    }
}
