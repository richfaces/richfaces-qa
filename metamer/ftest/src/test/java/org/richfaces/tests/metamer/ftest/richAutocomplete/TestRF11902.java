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

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.autocomplete.RichFacesAutocomplete;
import org.richfaces.fragment.autocomplete.SelectOrConfirm;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.configurator.unstable.annotation.Unstable;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Unstable
public class TestRF11902 extends AbstractWebDriverTest {

    private static final int DELAY_ATTACHED_QUEUE_REFERENCING_OTHER_QUEUE = 1000;
    private static final int DELAY_ATTACHED_QUEUE_WITH_OWN_DELAY = 100;
    private static final int DELAY_GLOBAL = 2000;
    private static final int TOLERANCE = 800;

    @FindBy(css = ".rf-au[id$=autocompleteUsingAttachedQueueReferencingNamedQueue]")
    private RichFacesAutocomplete autocompleteUsingAttachedQueueReferencingNamedQueue;
    @FindBy(css = ".rf-au[id$=autocompleteUsingAttachedQueueWithRequestDelay]")
    private RichFacesAutocomplete autocompleteUsingAttachedQueueWithRequestDelay;
    @FindBy(css = ".rf-au[id$=autocompleteUsingGlobalQueue]")
    private RichFacesAutocomplete autocompleteUsingGlobalQueue;
    @FindBy(css = "[id$=delay1]")
    private WebElement delayPopupDisplay;
    @FindBy(css = "[id$=delay2]")
    private WebElement delaySelectItem;
    @FindBy(css = "[id$=output]")
    private WebElement output;

    private void assertDelayForItemSelected(int value) {
        assertEquals(Integer.parseInt(delaySelectItem.getText()), value, TOLERANCE);
    }

    private void assertDelayForPopupDisplayed(int value) {
        assertEquals(Integer.parseInt(delayPopupDisplay.getText()), value, TOLERANCE);
    }

    private void check(RichFacesAutocomplete au, int delayTyped, int delaySelected) {
        SelectOrConfirm typed = Graphene.guardAjax(au).type("a");
        // this will fail (only when using nested attachQueue) before RF 4.5.12
        assertDelayForPopupDisplayed(delayTyped);

        // select by visible text -- this will fail (only when using nested attachQueue) before RF 4.5.12
        Graphene.guardAjax(typed).select(ChoicePickerHelper.byVisibleText().endsWith("na"));
        checkOutput("Arizona");
        assertDelayForItemSelected(delaySelected);
    }

    protected void checkOutput(String expected) {
        Graphene.waitAjax().until().element(output).text().equalTo(expected);
    }

    @Override
    public String getComponentTestPagePath() {
        return "richAutocomplete/rf-11902.xhtml";
    }

    @Test
    public void testAutocompleteUsingGlobalQueue() {
        check(autocompleteUsingGlobalQueue, DELAY_GLOBAL, DELAY_GLOBAL);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11902")
    public void testAutocompleteWithAttachedQueueReferencigNamedQueue() {
        check(autocompleteUsingAttachedQueueReferencingNamedQueue, DELAY_ATTACHED_QUEUE_REFERENCING_OTHER_QUEUE, DELAY_GLOBAL);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11902")
    public void testAutocompleteWithAttachedQueueWithRequestDelay() {
        check(autocompleteUsingAttachedQueueWithRequestDelay, DELAY_ATTACHED_QUEUE_WITH_OWN_DELAY, DELAY_GLOBAL);
    }
}
