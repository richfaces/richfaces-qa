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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.model.Capital;
import org.richfaces.tests.page.fragments.impl.autocomplete.RichFacesAutocomplete;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractAutocompleteTest extends AbstractWebDriverTest {

    private static final List<Capital> capitals = Model.unmarshallCapitals();
    protected Attributes<AutocompleteAttributes> autocompleteAttributes = getAttributes();

    @FindBy(css = "div[id$=autocomplete]")
    protected RichFacesAutocomplete autocomplete;

    @FindBy(css = "[id$='output']")
    private WebElement output;

    protected void checkOutput(String expected) {
        Graphene.waitAjax().until().element(output).text().equalTo(expected);
    }

    protected List<Capital> getCapitals() {
        return Collections.unmodifiableList(capitals);
    }

    public String getExpectedStateForPrefix(String prefix, boolean selectFirst) {
        if (selectFirst && prefix.length() > 0) {
            return getStatesByPrefix(prefix).get(0);
        }

        return prefix;
    }

    protected List<String> getStatesByPrefix(String prefix) {
        List<String> states = new LinkedList<String>();

        for (Capital cap : capitals) {
            if (cap.getState().toLowerCase().startsWith(prefix)) {
                states.add(cap.getState());
            }
        }

        return states;
    }
}
