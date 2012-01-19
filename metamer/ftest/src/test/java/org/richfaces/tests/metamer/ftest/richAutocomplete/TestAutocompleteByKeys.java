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

import static org.jboss.test.selenium.RequestTypeModelGuard.guardNoRequest;
import static org.jboss.test.selenium.RequestTypeModelGuard.guardXhr;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.autocompleteAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23092 $
 */
@IssueTracking("https://issues.jboss.org/browse/RF-10254")
public class TestAutocompleteByKeys extends AbstractAutocompleteTest {
    
    @Inject
    @Use(booleans = { true, false })
    Boolean autofill;

    @Inject
    @Use(booleans = { true, false })
    Boolean selectFirst;

    StringBuilder partialInput;

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
        autocomplete.clearInputValue();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenConfirm() {
        assertCompletionVisible(false);
        typePrefix("ala");
        assertCompletionVisible(true);
        confirm();
        assertCompletionVisible(false);
        assertTrue(getAutocomplete().getInputText().toLowerCase().startsWith(getExpectedStateForPrefix().toLowerCase()));
    }
    
    @Test
    public void testSimpleSelection() {
       String expected = "Alaska"; // this item is 2nd if type filter "ala", so it ensure that it was not picked first item
       typePrefix("ala");
       autocomplete.selectByMouse(expected);
       assertEquals(getAutocomplete().getInputText(), expected);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11323")
    public void testTypingPrefixAndThenDeleteAll() {
        assertCompletionVisible(false);
        typePrefix("ala");
        assertCompletionVisible(true);
        deleteAll();
        assertCompletionVisible(false);
        typePrefix("ala");
        assertCompletionVisible(true);
    }

    private void assertCompletionVisible(boolean assertCompletionVisible) {
        assertEquals(getAutocomplete().isCompletionVisible(), assertCompletionVisible);
    }

    public void confirm() {
        getAutocomplete().confirmByKeys();
        getAutocomplete().waitForCompletionVisible();
    }

    public void deleteAll() {
        partialInput = new StringBuilder();

        getAutocomplete().textSelectAll();
        guardNoRequest(getAutocomplete()).pressBackspace();

        assertEquals(getAutocomplete().getInputText(), getExpectedStateForPrefix());
        assertEquals(getAutocomplete().getSelectedOptionIndex(), getExpectedSelectedOptionIndex());
    }

    public void typePrefix(String wholeInput) {
        partialInput = new StringBuilder(getAutocomplete().getInputText());

        for (int i = 0; i < wholeInput.length(); i++) {
            String chr = String.valueOf(wholeInput.charAt(i));

            guardXhr(getAutocomplete()).typeKeys(chr);
            partialInput.append(chr);

            assertEquals(getAutocomplete().getInputText(), getExpectedStateForPrefix());
            assertEquals(getAutocomplete().getSelectedOptionIndex(), getExpectedSelectedOptionIndex());
        }
    }

    public String getExpectedStateForPrefix() {
        if (selectFirst && autofill && partialInput.length() > 0) {
            return getStatesByPrefix(partialInput.toString()).get(0).toLowerCase();
        }

        return partialInput.toString();
    }

    public int getExpectedSelectedOptionIndex() {
        return (selectFirst && partialInput.length() > 0) ? 0 : -1;
    }
}
