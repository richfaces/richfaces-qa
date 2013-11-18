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

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class TestAutocompleteAttributes extends AbstractAutocompleteTest {

    @Page
    private SimplePage page;

    private static final String FIRST_LISTENER_MSG_FORMAT = "1 value changed: %s -> %s";
    private static final String SECOND_LISTENER_MSG = "2 value changed";

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @Test
    public void testValueChangeListener() {
        Graphene.guardAjax(autocomplete).type("h").select("Hawaii");
        Graphene.guardAjax(page).blur();
        checkOutput("Hawaii");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, String.format(FIRST_LISTENER_MSG_FORMAT, "null", "Hawaii"));
        page.assertListener(PhaseId.INVOKE_APPLICATION, SECOND_LISTENER_MSG);

        autocomplete.clear();
        Graphene.guardAjax(autocomplete).type("ka").select("Kansas");
        Graphene.guardAjax(page).blur();
        checkOutput("Kansas");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, String.format(FIRST_LISTENER_MSG_FORMAT, "Hawaii", "Kansas"));
        page.assertListener(PhaseId.INVOKE_APPLICATION, SECOND_LISTENER_MSG);

        autocomplete.clear();
        Graphene.guardAjax(autocomplete).type("nonexisting");
        Graphene.guardAjax(page).blur();
        checkOutput("nonexisting");

        page.assertListener(PhaseId.PROCESS_VALIDATIONS, String.format(FIRST_LISTENER_MSG_FORMAT, "Kansas", "nonexisting"));
        page.assertListener(PhaseId.INVOKE_APPLICATION, SECOND_LISTENER_MSG);
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
