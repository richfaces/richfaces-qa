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

import java.net.URL;
import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.autocompleteAttributes;
import org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteAttributes extends AbstractAutocompleteTest<SimplePage>{

    private static final String PHASE_LISTENER_LOG_FORMAT = "*1 value changed: {0} -> {1}";

    @FindBy(id="form:autocomplete")
    private AutocompleteComponentImpl<String> autocomplete;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @BeforeMethod
    public void setParser() {
        autocomplete.setSuggestionParser(new TextSuggestionParser());
    }

    @Override
    protected SimplePage createPage() {
        return new SimplePage();
    }

    @Test
    public void testClientFilterFunction() {
        autocompleteAttributes.set(AutocompleteAttributes.clientFilterFunction, Boolean.TRUE);
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testValueChangeListener() {
        autocomplete.clear(ClearType.BACK_SPACE);
        autocomplete.type("something");
        getPage().blur();

        Graphene.waitAjax().until(Graphene.element(getPage().getOutput()).textEquals("something"));

        autocomplete.clear(ClearType.BACK_SPACE);
        autocomplete.type("something else");
        getPage().blur();
        // valueChangeListener output as 4th record
        Graphene.waitAjax().until(Graphene.element(getPage().getOutput()).textEquals("something else"));
        assertEquals(getPage().getPhases().get(3), format(PHASE_LISTENER_LOG_FORMAT, "something", "something else"));
    }

    @Test(groups = { "4.Future" })
    @Templates(value = { "richPopupPanel" })
    public void testValueChangeListenerInPopupPanel() {
        testValueChangeListener();
    }
}
