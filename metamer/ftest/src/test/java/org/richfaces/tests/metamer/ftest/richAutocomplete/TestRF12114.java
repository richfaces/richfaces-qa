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

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.autocomplete.RichFacesAutocomplete;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12114 extends AbstractWebDriverTest {

    @FindBy(css = ".rf-au[id$=autocompleteWithAjax]")
    private RichFacesAutocomplete autocompleteWithAjax;
    @FindBy(css = ".rf-au[id$=autocompleteWithJSFunction]")
    private RichFacesAutocomplete autocompleteWithJSFunction;
    @FindBy(css = "[id$=out1]")
    private WebElement output1;
    @FindBy(css = "[id$=out2]")
    private WebElement output2;

    private void assertListenerForAutocompleteWasInvoked(RichFacesAutocomplete au) {
        int listenerNumber = au == autocompleteWithAjax ? 1 : 2;
        getMetamerPage().assertListener(PhaseId.INVOKE_APPLICATION, "on blur listener " + listenerNumber);
    }

    private void assertListenerForAutocompleteWasNotInvoked(RichFacesAutocomplete au) {
        int listenerNumber = au == autocompleteWithAjax ? 1 : 2;
        getMetamerPage().assertNoListener("on blur listener " + listenerNumber);
    }

    private void assertOutputForAutocompleteEqualsTo(RichFacesAutocomplete au, String to) {
        WebElement output = au == autocompleteWithAjax ? output1 : output2;
        assertEquals(output.getText().trim(), to);
    }


    private void checkIssue(RichFacesAutocomplete tested, RichFacesAutocomplete other) {
        // focus + blur autocomplete with empty value
        focusOnAutocomplete(tested);
        blur(WaitRequestType.XHR);
        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE);
        assertListenerForAutocompleteWasNotInvoked(tested);
        assertListenerForAutocompleteWasNotInvoked(other);
        assertOutputForAutocompleteEqualsTo(tested, "");
        assertOutputForAutocompleteEqualsTo(other, "");

        // choose first option beginning with 'a' and blur
        Graphene.guardAjax(tested).type("a").confirm();
        blur(WaitRequestType.XHR);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        assertListenerForAutocompleteWasInvoked(tested);
        assertListenerForAutocompleteWasNotInvoked(other);
        assertOutputForAutocompleteEqualsTo(tested, "Alabama");
        assertOutputForAutocompleteEqualsTo(other, "");

        // focus + blur autocomplete with non-empty value
        focusOnAutocomplete(tested);
        blur(WaitRequestType.XHR);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);
        assertListenerForAutocompleteWasInvoked(tested);
        assertListenerForAutocompleteWasNotInvoked(other);
        assertOutputForAutocompleteEqualsTo(tested, "Alabama");
        assertOutputForAutocompleteEqualsTo(other, "");
    }

    private void focusOnAutocomplete(RichFacesAutocomplete au) {
        au.advanced().getInput().advanced().getInputElement().click();
    }

    @Override
    public String getComponentTestPagePath() {
        return "richAutocomplete/rf-12114.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12114")
    public void testAutocompleteAjaxOnBlur() {
        // check the first autocomplete (issue RF-12114)
        checkIssue(autocompleteWithAjax, autocompleteWithJSFunction);
    }

    @Test
    public void testAutocompleteJSFunctionOnBlur() {
        // check the second autocomplete (workaround)
        checkIssue(autocompleteWithJSFunction, autocompleteWithAjax);
    }
}
