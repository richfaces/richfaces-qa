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

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAutocompleteJSApi extends AbstractAutocompleteTest {

    @FindBy(id = "getValue")
    private WebElement getValue;
    @FindBy(className = "rf-au-lst-cord")
    private WebElement autocompletePopup;
    @FindBy(css = "[id$=value]")
    private WebElement gettersValue;
    @FindBy(id = "hidePopup")
    private WebElement hidePopup;
    @FindBy(id = "setValue")
    private WebElement setValue;
    @FindBy(id = "showPopup")
    private WebElement showPopup;

    @Override
    public String getComponentTestPagePath() {
        return "richAutocomplete/autocomplete.xhtml";
    }

    @Test
    public void testGetValue() {
        Graphene.guardAjax(Graphene.guardAjax(autocomplete).type("ala")).select(0);
        checkOutput("Alabama");

        getValue.click();
        assertEquals(gettersValue.getAttribute("value"), "Alabama");
    }

    @Test
    public void testHidePopup() {
        Graphene.guardAjax(autocomplete).type("ala");
        autocomplete.advanced().waitForSuggestionsToBeVisible().perform();
        Utils.triggerJQ("mouseover", hidePopup);
        autocomplete.advanced().waitForSuggestionsToBeNotVisible().perform();
    }

    @Test
    public void testSetValue() {
        setValue.click();

        assertEquals(autocomplete.advanced().getInput().getStringValue(), "Value set by JS API");
    }

    @Test
    public void testShowPopup() {
        Utils.triggerJQ("mouseover", showPopup);
        Graphene.waitGui().until().element(autocompletePopup).is().visible();
        List<WebElement> suggestionsElements = autocomplete.advanced().getSuggestionsElements();
        assertEquals(suggestionsElements.size(), 0);

        Graphene.guardAjax(autocomplete).type("ala");
        // blur to close the popup
        Graphene.guardAjax(getMetamerPage().getResponseDelayElement()).click();
        autocomplete.advanced().waitForSuggestionsToBeNotVisible().perform();

        Utils.triggerJQ("mouseover", showPopup);
        autocomplete.advanced().waitForSuggestionsToBeVisible().perform();
        suggestionsElements = autocomplete.advanced().getSuggestionsElements();
        assertEquals(suggestionsElements.size(), 2);
        assertEquals(suggestionsElements.get(0).getText(), "Alabama");
        assertEquals(suggestionsElements.get(1).getText(), "Alaska");
    }
}
