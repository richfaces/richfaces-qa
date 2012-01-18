/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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

import static org.jboss.test.selenium.RequestTypeModelGuard.guardXhr;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.autocompleteAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.Autocomplete;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Test for example with formatted suggestions on page faces/components/richAutocomplete/autocomplete.xhtml 
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 *
 * @version $Revision$
 */
public class TestAutocompleteFormatting extends AbstractAjocadoTest {
    
    Autocomplete autocomplete = new Autocomplete();
    StringBuilder partialInput;
    
    List<Capital> capitals = Model.unmarshallCapitals();
    
    @Inject
    @Use(booleans = { true, false })
    Boolean autofill;

    @Inject
    @Use(booleans = { true, false })
    Boolean selectFirst;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/fetchValueAttr.xhtml");
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

    /**
     * This should test combination of @var and @fetchValue attributes of autocomplete
     */
    @Test
    public void testFormatting() {
        assertFalse(autocomplete.isCompletionVisible());
        typePrefix("ala");
        assertTrue(autocomplete.isCompletionVisible());
        confirm();
        assertFalse(autocomplete.isCompletionVisible());
        assertTrue(autocomplete.getInputText().toLowerCase().startsWith(getExpectedStateForPrefix().toLowerCase()));
    }
    
    @Test
    public void testLayout() {
        
        JQueryLocator suggestionsGridDiv = new JQueryLocator("div.rf-au-lst-scrl > div[id$=autocompleteItems] > div:contains({0})");
        JQueryLocator suggestionsList = new JQueryLocator("div.rf-au-lst-scrl > ul[id$=autocompleteItems] > li:contains({0})");
        JQueryLocator suggestionsTable = new JQueryLocator("div.rf-au-lst-scrl > table[id$=autocompleteItems] tr>td:contains({0})");
        
        verifyLayout("div", suggestionsGridDiv);
        
        verifyLayout("grid", suggestionsGridDiv);
        
        verifyLayout("list", suggestionsList);
        
        verifyLayout("table", suggestionsTable);
        
    }
    
    private void verifyLayout(String layout, JQueryLocator suggestionLocatorFormat) {
        
        autocompleteAttributes.set(AutocompleteAttributes.layout, layout);
        
        autocomplete.typeKeys("Co");
               
        assertTrue(selenium.isElementPresent(suggestionLocatorFormat.format("Colorado")));
        assertTrue(selenium.isElementPresent(suggestionLocatorFormat.format("[Denver]")));
        
        assertTrue(selenium.isElementPresent(suggestionLocatorFormat.format("Connecticut")));
        assertTrue(selenium.isElementPresent(suggestionLocatorFormat.format("[Hartford]")));
    }
    
    public void typePrefix(String wholeInput) {
        partialInput = new StringBuilder(autocomplete.getInputText());

        for (int i = 0; i < wholeInput.length(); i++) {
            String chr = String.valueOf(wholeInput.charAt(i));

            guardXhr(autocomplete).typeKeys(chr);
            partialInput.append(chr);

            assertEquals(autocomplete.getInputText(), getExpectedStateForPrefix());
            assertEquals(autocomplete.getSelectedOptionIndex(), getExpectedSelectedOptionIndex());
        }
    }
    
    public String getExpectedStateForPrefix() {
        if (selectFirst && autofill && partialInput.length() > 0) {
            return getStatesByPrefix(partialInput.toString()).get(0).toLowerCase();
        }

        return partialInput.toString();
    }
    
    public String getExpectedCompletionForPrefix() {
        if (selectFirst && autofill && partialInput.length() > 0) {
            return getCompletionByPrefix(partialInput.toString()).get(0).toLowerCase();
        }

        return partialInput.toString();
    }
    
    public List<String> getStatesByPrefix(String prefix) {
        List<String> states = new LinkedList<String>();

        for (Capital cap : capitals) {
            if (cap.getState().toLowerCase().startsWith(prefix)) {
                states.add(cap.getState());
            }
        }

        return states;
    }
    
    public List<String> getCompletionByPrefix(String prefix) {
        List<String> states = new LinkedList<String>();

        for (Capital cap : capitals) {
            if (cap.getState().toLowerCase().startsWith(prefix)) {
                states.add(cap.getState() + " [" + cap.getName() + "]");
            }
        }

        return states;
    }
    
    public int getExpectedSelectedOptionIndex() {
        return (selectFirst && partialInput.length() > 0) ? 0 : -1;
    }
    
    public void confirm() {
        autocomplete.confirmByKeys();
        autocomplete.waitForCompletionVisible();
    }

    public Boolean getAutofill() {
        return autofill;
    }

    public void setAutofill(Boolean autofill) {
        this.autofill = autofill;
    }

    public Boolean getSelectFirst() {
        return selectFirst;
    }

    public void setSelectFirst(Boolean selectFirst) {
        this.selectFirst = selectFirst;
    }
}

