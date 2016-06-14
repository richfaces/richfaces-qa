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

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.fragment.select.SelectSuggestions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14225 extends AbstractWebDriverTest {

    private static final List<String> SUGGESTIONS = Lists.newArrayList("label1", "label1", "label1", "label2", "label2", "label2", "label1");

    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindBy(css = "[id$=select]")
    private RichFacesSelect select;

    @Override
    public String getComponentTestPagePath() {
        return "richSelect/rf-14225.xhtml";
    }

    private List<String> getSuggestionsText() {
        List<String> result = Lists.newArrayList();
        for (WebElement suggestion : select.advanced().getSuggestionsElements()) {
            result.add(suggestion.getText());
        }
        return result;
    }

    private SelectSuggestions openSelectAndCheckAllSuggestionsAreVisible() {
        SelectSuggestions openedSelect = select.openSelect();
        assertEquals(getSuggestionsText(), SUGGESTIONS);
        return openedSelect;
    }

    private void selectItemAtIndexAndCheckOutput(SelectSuggestions openedSelect, int index) {
        Graphene.guardAjax(openedSelect).select(index);
        Graphene.waitAjax().until().element(output).text().equalTo("value" + (index + 1));
        waiting(200);// stabilization wait time
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14225")
    public void testAllOptionsAreVisibleWhenDifferentItemsWithSameLabelSelected() {
        SelectSuggestions openedSelect = openSelectAndCheckAllSuggestionsAreVisible();

        for (int i : new int[] { 0, 1, 4, 3, 5, 2, 1, 6 }) {
            selectItemAtIndexAndCheckOutput(openedSelect, i);
            openedSelect = openSelectAndCheckAllSuggestionsAreVisible();
        }

        selectItemAtIndexAndCheckOutput(openedSelect, 0);
        select.openSelect();
        openSelectAndCheckAllSuggestionsAreVisible();
    }
}
