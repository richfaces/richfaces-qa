/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.autocomplete.SelectOrConfirm;
import org.richfaces.fragment.common.ScrollingType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestAutocomplete extends AbstractAutocompleteTest {

    private Boolean autofill;
    private Boolean selectFirst;

    @Override
    public String getComponentTestPagePath() {
        return "richAutocomplete/autocomplete.xhtml";
    }

    public void prepareProperties() {
        attsSetter()
            .setAttribute(AutocompleteAttributes.autofill).toValue(autofill)
            .setAttribute(AutocompleteAttributes.selectFirst).toValue(selectFirst)
            .asSingleAction().perform();
        autocomplete.clear();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14087")
    @CoversAttributes({ "mode", "minChars", "autofill", "showButton", "value" })
    public void testShowSuggestionsWithButton() {
        // prepare autocomplete attributes
        attsSetter()
            .setAttribute(AutocompleteAttributes.showButton).toValue(true)
            .setAttribute(AutocompleteAttributes.autofill).toValue(false)
            .setAttribute(AutocompleteAttributes.mode).toValue("client")
            .setAttribute(AutocompleteAttributes.minChars).toValue(0)
            .setAttribute(AutocompleteAttributes.value).toValue("Alaska")
            .asSingleAction().perform();

        // try to open the suggestions by clicking the button and assert
        WebElement button = driver.findElement(By.cssSelector("span[class$=btn-arrow]"));
        button.click();
        autocomplete.advanced().waitForSuggestionsToBeVisible();
        assertVisible(autocomplete.advanced().getSuggestionsElements().get(0), "Suggestion should be visible");
    }

    private void testSimpleSelection(boolean withMouse) {
        prepareProperties();
        if (!withMouse) {
            autocomplete.advanced().setScrollingType(ScrollingType.BY_KEYS);
        }
        SelectOrConfirm typed = Graphene.guardAjax(autocomplete).type("a");
        Graphene.guardAjax(typed).select(ChoicePickerHelper.byVisibleText().endsWith("na"));
        checkOutput("Arizona");
        if (withMouse) {
            blur(WaitRequestType.XHR);// prevent ViewExpiredException
        }
    }

    @Test
    @Uses({
        @UseWithField(field = "autofill", valuesFrom = ValuesFrom.FROM_FIELD, value = "booleans"),
        @UseWithField(field = "selectFirst", valuesFrom = ValuesFrom.FROM_FIELD, value = "booleans")
    })
    @CoversAttributes({ "autofill", "selectFirst" })
    public void testSimpleSelectionWithKeyboard() {
        testSimpleSelection(false);
    }

    @Test
    @Uses({
        @UseWithField(field = "autofill", valuesFrom = ValuesFrom.FROM_FIELD, value = "booleans"),
        @UseWithField(field = "selectFirst", valuesFrom = ValuesFrom.FROM_FIELD, value = "booleans")
    })
    @CoversAttributes({ "autofill", "selectFirst" })
    public void testSimpleSelectionWithMouse() {
        testSimpleSelection(true);
    }

    @Test(groups = "smoke")
    @IssueTracking("https://issues.jboss.org/browse/RF-11323")
    @Unstable
    @Uses({
        @UseWithField(field = "autofill", valuesFrom = ValuesFrom.FROM_FIELD, value = "booleans"),
        @UseWithField(field = "selectFirst", valuesFrom = ValuesFrom.FROM_FIELD, value = "booleans")
    })
    @CoversAttributes({ "autocompleteMethod", "autofill", "selectFirst" })
    public void testTypePrefixDeleteAll_typePrefixConfirm() {
        prepareProperties();
        Graphene.guardAjax(autocomplete).type("ala");
        TextInputComponentImpl input = autocomplete.advanced().getInput();
        while (!input.getStringValue().isEmpty()) {
            input.sendKeys(Keys.BACK_SPACE);
        }
        autocomplete.advanced().waitForSuggestionsToBeNotVisible().perform();
        assertTrue(autocomplete.advanced().getSuggestionsElements().isEmpty());

        SelectOrConfirm typed = Graphene.guardAjax(autocomplete).type("ala");
        autocomplete.advanced().waitForSuggestionsToBeVisible().perform();
        assertFalse(autocomplete.advanced().getSuggestionsElements().isEmpty());
        Graphene.guardAjax(typed).confirm();

        autocomplete.advanced().waitForSuggestionsToBeNotVisible().perform();
        String expectedStateForPrefix = getExpectedStateForPrefix("ala", selectFirst);
        assertEquals(autocomplete.advanced().getInput().getStringValue(), expectedStateForPrefix);
        checkOutput(expectedStateForPrefix);
        if (selectFirst) {
            blur(WaitRequestType.XHR);// prevent ViewExpiredException
        }
    }
}
