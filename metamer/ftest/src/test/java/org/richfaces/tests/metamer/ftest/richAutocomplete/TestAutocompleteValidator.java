/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.autocomplete.RichFacesAutocomplete;
import org.richfaces.tests.metamer.ftest.abstractions.validator.AbstractInputComponentValidatorTest;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/validator.xhtml.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAutocompleteValidator extends AbstractInputComponentValidatorTest {

    private static final String NOT_PHOENIX = "Not Phoenix";
    private static final String PHOENIX = "Phoenix";

    @FindBy(css = "[id$=component]")
    private RichFacesAutocomplete autocomplete;

    @Override
    public String getComponentTestPagePath() {
        return "richAutocomplete/validator.xhtml";
    }

    @Override
    protected void setCorrectValue() {
        autocomplete.clear();
        Graphene.guardAjax(autocomplete).type(PHOENIX);
        // blur
        Graphene.guardAjax(getMetamerPage().getResponseDelayElement()).click();
    }

    @Override
    protected void setIncorrectValue() {
        autocomplete.clear();
        Graphene.guardAjax(autocomplete).type(NOT_PHOENIX);
        // blur
        Graphene.guardAjax(getMetamerPage().getResponseDelayElement()).click();
    }

    @Test
    public void testValidator() {
        super.testValidator();
    }
}
