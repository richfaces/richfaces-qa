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

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for keeping visual state (KVS) for autocomplete on page:
 * faces/components/richAutocomplete/autocomplete.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteKVS extends AbstractAutocompleteTest {

    @Page
    private MetamerPage page;

    @BeforeMethod
    public void setParser() {
        autocomplete.setSuggestionParser(new TextSuggestionParser());
    }

    @BeforeMethod
    public void prepareAutocomplete() {
        autocomplete.clear(ClearType.BACK_SPACE);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @Test(groups = { "keepVisualStateTesting" })
    public void testRefreshFullPage() {
        new AutocompleteReloadTester().testFullPageRefresh();
    }

    @Test(groups = { "keepVisualStateTesting" })
    public void testRerenderAll() {
        new AutocompleteReloadTester().testRerenderAll();
    }

    private class AutocompleteReloadTester extends AbstractWebDriverTest.ReloadTester<String> {

        public AutocompleteReloadTester() {
            super(page);
        }

        @Override
        public void doRequest(String inputValue) {
            Graphene.guardAjax(autocomplete).type(inputValue);
            Graphene.guardAjax(autocomplete).finish();
        }

        @Override
        public void verifyResponse(String inputValue) {
            assertEquals(autocomplete.getInputValue(), inputValue);
        }

        @Override
        public String[] getInputValues() {
            return new String[]{ "not-in-list-value" };
        }
    }
}
