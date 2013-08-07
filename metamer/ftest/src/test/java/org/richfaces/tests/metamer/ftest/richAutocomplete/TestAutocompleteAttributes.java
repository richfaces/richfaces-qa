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

import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteAttributes<P> extends AbstractAutocompleteTest {

    @Page
    private SimplePage page;

    private static final String PHASE_LISTENER_LOG_FORMAT = "*1 value changed: {0} -> {1}";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @Test
    public void testValueChangeListener() {
        autocomplete.clear();
        Graphene.guardAjax(autocomplete).type("something");
        Graphene.guardAjax(page).blur();

        checkOutput("something");

        autocomplete.clear();
        Graphene.guardAjax(autocomplete).type("something else");
        Graphene.guardAjax(page).blur();
        // valueChangeListener output as 4th record
        checkOutput("something else");
        assertEquals(page.getPhases().get(3), format(PHASE_LISTENER_LOG_FORMAT, "something", "something else"));
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12820")
    public void testLayout() {
        String[] layouts = new String[] { "div", "list", "table" };
        for (String layout : layouts) {
            autocompleteAttributes.set(AutocompleteAttributes.layout, layout);
            autocomplete.clear();

            Graphene.guardAjax(autocomplete).type("ala").select(ChoicePickerHelper.byVisibleText().contains("Alaska"));
            // code before refactoring
            // Graphene.guardAjax(autocomplete).autocompleteWithSuggestion(expected, ScrollingType.BY_MOUSE);

            waiting(500);
            assertEquals(autocomplete.advanced().getInput().getStringValue(), "Alaska",
                "The input value doesn't match when layout is set to '" + layout + "'.");
        }
    }
}
