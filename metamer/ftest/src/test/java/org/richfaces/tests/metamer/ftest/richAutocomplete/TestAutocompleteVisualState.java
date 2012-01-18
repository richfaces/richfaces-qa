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

import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.testng.annotations.Test;


/**
 * Test for keeping visual state for autocomplete on page: 
 *  faces/components/richAutocomplete/autocomplete.xhtml
 *  
 *  There were some problems with 
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
public class TestAutocompleteVisualState extends AbstractAutocompleteTest {
    
    AutocompleteReloadTester reloadTester = new AutocompleteReloadTester();

    @Override
    public URL getTestUrl() {        
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }
    

    @Test
    public void testRefreshFullPage() {
        reloadTester.testFullPageRefresh();
    }
    
    @Test
    public void testRerenderAll() {        
        reloadTester.testRerenderAll();
    }
    
    private class AutocompleteReloadTester extends ReloadTester<String> {

        @Override
        public void doRequest(String inputValue) {
            // autocomplete.clearInputValue();
            getAutocomplete().type(inputValue);
            waitAjax.until(textEquals.locator(pjq("span[id$=:output]")).text(inputValue));            
        }

        @Override
        public void verifyResponse(String inputValue) {
            String found = getAutocomplete().getInputText();
            assertEquals(found, inputValue);
        }

        @Override
        public String[] getInputValues() {            
            return new String[] {"not-in-list-value"};
        }
        
    }

}
