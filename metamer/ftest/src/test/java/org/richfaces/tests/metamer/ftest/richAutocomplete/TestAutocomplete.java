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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.autocomplete.SelectOrConfirm;
import org.richfaces.tests.page.fragments.impl.common.ScrollingType;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocomplete extends AbstractAutocompleteTest {

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
    public void prepareProperties() {
        autocompleteAttributes.set(AutocompleteAttributes.autofill, autofill);
        autocompleteAttributes.set(AutocompleteAttributes.selectFirst, selectFirst);
        if (autofill == null) {
            autofill = false;
        }
        if (selectFirst == null) {
            selectFirst = false;
        }
        autocomplete.clear();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenConfirm() {
        assertTrue(autocomplete.advanced().getSuggestionsElements().isEmpty());
        SelectOrConfirm typed = Graphene.guardAjax(autocomplete).type("ala");
        assertFalse(autocomplete.advanced().getSuggestionsElements().isEmpty());
        if (selectFirst == false) {
            waiting(200); // wait for DOM update
        }
        typed.confirm();
        assertTrue(autocomplete.advanced().getSuggestionsElements().isEmpty());
        String expectedStateForPrefix = getExpectedStateForPrefix("ala", selectFirst);
        assertEquals(autocomplete.advanced().getInput().getStringValue(), expectedStateForPrefix);
        checkOutput(expectedStateForPrefix);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenDeleteAll() {
        Graphene.guardAjax(autocomplete).type("ala");
        autocomplete.clear();
        autocomplete.advanced().waitForSuggestionsToBeNotVisible().perform();
        assertTrue(autocomplete.advanced().getSuggestionsElements().isEmpty());
        Graphene.guardAjax(autocomplete).type("ala");
        assertFalse(autocomplete.advanced().getSuggestionsElements().isEmpty());
    }

    @Test
    public void testSimpleSelectionWithMouse() {
        autocomplete.type("a").select(ChoicePickerHelper.byVisibleText().endsWith("na"));
        getMetamerPage().getRequestTimeElement().click(); // blur from autocomplete

        checkOutput("Arizona");
    }

    @Test
    public void testSimpleSelectionWithKeyboard() {
        autocomplete.advanced().setupScrollingType(ScrollingType.BY_KEYS);
        autocomplete.type("a").select(ChoicePickerHelper.byVisibleText().endsWith("na"));
        checkOutput("Arizona");
    }
}
