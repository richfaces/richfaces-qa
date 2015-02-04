/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.ClearType;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.fragment.select.SelectSuggestions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/autocompleteMethod.xhtml.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSelectAutocompleteMethod extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=select]")
    private RichFacesSelect select;
    @FindBy(css = "span[id$=output]")
    private WebElement output;

    private final Attributes<SelectAttributes> attributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richSelect/autocompleteMethod.xhtml");
    }

    @Test(groups = "smoke")
    public void testAutocompleteFunctionality() {
        SelectSuggestions selectState = Graphene.guardAjax(select).type("a");
        List<WebElement> suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 4);
        assertEquals(suggestions.get(0).getText(), "Alabama");
        assertEquals(suggestions.get(1).getText(), "Alaska");
        assertEquals(suggestions.get(2).getText(), "Arizona");
        assertEquals(suggestions.get(3).getText(), "Arkansas");
        Graphene.guardAjax(selectState).select(1);// select Alaska
        waitUntilOutputEqualsTo("Alaska");

        selectState = Graphene.guardAjax(select).type("w");
        suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 4);
        assertEquals(suggestions.get(0).getText(), "Washington");
        assertEquals(suggestions.get(1).getText(), "West Virginia");
        assertEquals(suggestions.get(2).getText(), "Wisconsin");
        assertEquals(suggestions.get(3).getText(), "Wyoming");
        Graphene.guardAjax(selectState).select(3);// select Wyoming
        waitUntilOutputEqualsTo("Wyoming");
    }

    @Test
    public void testMinChars() {
        attributes.set(SelectAttributes.mode, Mode.ajax);
        attributes.set(SelectAttributes.minChars, 3);

        Graphene.guardNoRequest(select.advanced().getInput().sendKeys("a").advanced()).trigger("blur");

        Graphene.guardNoRequest(select.advanced().getInput().clear().sendKeys("al").advanced()).trigger("blur");

        Graphene.guardAjax(Graphene.guardAjax(select).type("ala")).select(0);
        waitUntilOutputEqualsTo("Alabama");

        Graphene.guardAjax(Graphene.guardAjax(select).type("alas")).select(0);
        waitUntilOutputEqualsTo("Alaska");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-13965")
    public void testPopupWillShowAfterWholeInputClearedAtOnce() {
        attributes.set(SelectAttributes.mode, Mode.ajax);
        // fill in some value
        Graphene.guardAjax(Graphene.guardAjax(select).type("a")).select(1);
        waitUntilOutputEqualsTo("Alaska");
        // clear the whole input at once with JS
        TextInputComponentImpl input = select.advanced().getInput();
        input.advanced().clear(ClearType.JS);
        // type 'a' >>> popup will show
        Graphene.guardAjax(input).sendKeys("a");
        select.advanced().waitUntilSuggestionsAreVisible();
        select.advanced().getSuggestionsElements().get(3).click();
        select.advanced().waitUntilSuggestionsAreNotVisible();
        // blur >>> trigger onchange event
        Graphene.guardAjax(getMetamerPage().getResponseDelayElement()).click();
        waitUntilOutputEqualsTo("Arkansas");
    }

    @Test
    public void testMode() {
        attributes.set(SelectAttributes.mode, Mode.cachedAjax);
        Graphene.guardAjax(Graphene.guardAjax(select).type("a")).select(1);
        waitUntilOutputEqualsTo("Alaska");
        Graphene.guardAjax(Graphene.guardNoRequest(select).type("a")).select(2);
        waitUntilOutputEqualsTo("Arizona");

        attributes.set(SelectAttributes.mode, Mode.client);
        Graphene.guardAjax(Graphene.guardNoRequest(select).type("a")).select(3);
        waitUntilOutputEqualsTo("Arkansas");

        attributes.set(SelectAttributes.mode, Mode.lazyClient);
        Graphene.guardAjax(Graphene.guardAjax(select).type("a")).select(0);
        waitUntilOutputEqualsTo("Alabama");
        Graphene.guardAjax(Graphene.guardNoRequest(select).type("a")).select(1);
        waitUntilOutputEqualsTo("Alaska");
        Graphene.guardAjax(Graphene.guardNoRequest(select).type("a")).select(2);
        waitUntilOutputEqualsTo("Arizona");

        attributes.set(SelectAttributes.mode, Mode.ajax);
        Graphene.guardAjax(Graphene.guardAjax(select).type("ala")).select(0);
        waitUntilOutputEqualsTo("Alabama");
    }

    @Test
    public void testTypeExistingVal_typeNotExistingVal_typeExistingtVal() {
        attributes.set(SelectAttributes.mode, Mode.ajax);
        attributes.set(SelectAttributes.minChars, 2);

        Graphene.guardAjax(select).type("al");
        List<WebElement> suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 2);
        assertEquals(suggestions.get(0).getText(), "Alabama");
        assertEquals(suggestions.get(1).getText(), "Alaska");

        Graphene.guardAjax(select.advanced().getInput().clear()).sendKeys("aq");
        suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 0);

        SelectSuggestions selectState = Graphene.guardAjax(select).type("al");
        suggestions = select.advanced().getSuggestionsElements();
        assertEquals(suggestions.size(), 2);
        assertEquals(suggestions.get(0).getText(), "Alabama");
        assertEquals(suggestions.get(1).getText(), "Alaska");

        Graphene.guardAjax(selectState).select(1);
        waitUntilOutputEqualsTo("Alaska");
    }

    private void waitUntilOutputEqualsTo(String value) {
        Graphene.waitAjax().until().element(output).text().equalTo(value);
    }

    public static enum Mode {

        ajax, cachedAjax, client, lazyClient
    }
}
