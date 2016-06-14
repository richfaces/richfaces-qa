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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.fragment.select.SelectSuggestions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@IssueTracking("https://issues.jboss.org/browse/RF-6678")
public class TestRF6678 extends AbstractWebDriverTest {

    private static final String DISABLED_CLASS = "rf-sel-opt-dis";

    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=select]")
    private RichFacesSelect select;
    @FindBy(css = "[id$=setValueFirst]")
    private WebElement setValueFirstButtonJSAPI;
    @FindByJQuery(value = "[class*=rf-sel-opt]:visible")
    private List<WebElement> visibleSuggestions;

    @Override
    public String getComponentTestPagePath() {
        return "richSelect/rf-6678.xhtml";
    }

    @Test
    public void testDisabledItemsCannotBeSelected() {
        setAttribute("enableManualInput", "false");
        // open select
        SelectSuggestions openedSelect = select.openSelect();
        // check all suggestions are visible
        assertEquals(visibleSuggestions.size(), 5);
        assertEquals(visibleSuggestions.get(0).getText(), "first");
        assertTrue(visibleSuggestions.get(0).getAttribute("class").contains(DISABLED_CLASS));
        assertEquals(visibleSuggestions.get(1).getText(), "second");
        assertFalse(visibleSuggestions.get(1).getAttribute("class").contains(DISABLED_CLASS));
        assertEquals(visibleSuggestions.get(2).getText(), "third");
        assertTrue(visibleSuggestions.get(2).getAttribute("class").contains(DISABLED_CLASS));
        assertEquals(visibleSuggestions.get(3).getText(), "fourth");
        assertFalse(visibleSuggestions.get(3).getAttribute("class").contains(DISABLED_CLASS));
        assertEquals(visibleSuggestions.get(4).getText(), "fifth");
        assertTrue(visibleSuggestions.get(4).getAttribute("class").contains(DISABLED_CLASS));
        // try to select any disabled options
        for (int i : new int[] { 0, 2, 4 }) {
            // select disabled option
            visibleSuggestions.get(i).click();
            // suggestions should stay visible
            select.advanced().waitUntilSuggestionsAreVisible().perform();
            // output should not change
            assertEquals(output.getText(), "");
        }
        // select some option
        Graphene.guardAjax(openedSelect).select(0);// fragment is working only with enabled items
        assertEquals(output.getText(), "second");
    }

    @Test
    public void testDisabledItemsCannotBeSelectedWithJSAPI() {
        for (boolean val : new boolean[] { true, false }) {
            setAttribute("enableManualInput", val);
            // try to select disabled value with JS API
            setValueFirstButtonJSAPI.click();
            // check select input stays clear
            assertEquals(select.advanced().getInput().getStringValue(), "");
            // check output was not changed
            assertEquals(output.getText(), "");
        }
    }

    @Test
    public void testOnlyEnabledItemsAreVisibleWhenEnableManulInputTrue() {
        setAttribute("enableManualInput", "true");
        // open select
        SelectSuggestions openedSelect = select.openSelect();
        // check only enabled suggestions are visible
        assertEquals(visibleSuggestions.size(), 2);
        assertEquals(visibleSuggestions.get(0).getText(), "second");
        assertFalse(visibleSuggestions.get(0).getAttribute("class").contains(DISABLED_CLASS));
        assertEquals(visibleSuggestions.get(1).getText(), "fourth");
        assertFalse(visibleSuggestions.get(1).getAttribute("class").contains(DISABLED_CLASS));
        // select some option
        Graphene.guardAjax(openedSelect).select(0);
        assertEquals(output.getText(), "second");
    }
}
