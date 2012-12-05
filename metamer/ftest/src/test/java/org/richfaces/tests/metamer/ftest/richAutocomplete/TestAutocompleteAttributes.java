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

import static java.text.MessageFormat.format;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.autocompleteAttributes;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.autocomplete.ClearType;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.page.fragments.impl.autocomplete.AutocompleteComponentImpl;
import org.richfaces.tests.page.fragments.impl.autocomplete.TextSuggestionParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocompleteAttributes<P> extends AbstractAutocompleteTest {

    @Page
    private SimplePage page;

    private static final String PHASE_LISTENER_LOG_FORMAT = "*1 value changed: {0} -> {1}";

    @FindBy(css="span[id$=autocomplete]")
    private AutocompleteComponentImpl<String> autocomplete;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Autocomplete", "Simple");
    }

    @BeforeMethod
    public void setParser() {
        autocomplete.setSuggestionParser(new TextSuggestionParser());
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
        page.blur();

        Graphene.waitAjax().withTimeout(4, TimeUnit.SECONDS).until(Graphene.element(page.getOutput()).text().equalTo("something"));

        autocomplete.clear(ClearType.BACK_SPACE);
        autocomplete.type("something else");
        page.blur();
        // valueChangeListener output as 4th record
        Graphene.waitAjax().until(Graphene.element(page.getOutput()).text().equalTo("something else"));
        assertEquals(page.getPhases().get(3), format(PHASE_LISTENER_LOG_FORMAT, "something", "something else"));
    }

    @Test(groups = { "4.Future" })
    @Templates(value = { "richPopupPanel" })
    public void testValueChangeListenerInPopupPanel() {
        testValueChangeListener();
    }
}
