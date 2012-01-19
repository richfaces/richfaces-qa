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
package org.richfaces.tests.metamer.ftest.richGraphValidator;

import static org.jboss.arquillian.ajocado.Ajocado.elementNotPresent;
import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.graphValidatorAttributes;
import static org.richfaces.tests.metamer.ftest.richGraphValidator.GraphValidatorAttributes.groups;
import static org.richfaces.tests.metamer.ftest.richGraphValidator.GraphValidatorAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richGraphValidator.GraphValidatorAttributes.summary;
import static org.richfaces.tests.metamer.ftest.richGraphValidator.GraphValidatorAttributes.value;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionValueLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;


/**
 * Test for page /faces/components/richGraphValidator/all.xhtml
 * 
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 22622 $
 */
public class TestRichGraphValidator extends AbstractAjocadoTest {

    private static final String SMILE = ":-)";

    private static final String[] GROUPS = { "Default", "null", "ValidationGroupAllComponents",
        "ValidationGroupBooleanInputs", "ValidationGroupNumericInputs" };

    private static final int BOOLEAN_INPUTS_GROUP = 3;
    private static final int NUMERIC_INPUTS_GROUP = 4;

    private JQueryLocator autocomplete = pjq("input[id$=autocompleteInput]");
    private JQueryLocator inputSecret = pjq("input[id$=inputSecret]");
    private JQueryLocator inputText = pjq("input[id$=inputText]");
    private JQueryLocator calendar = pjq("input[id$=calendarInputDate]");
    private JQueryLocator inputTextArea = pjq("textarea[id$=inputTextarea]");
    private JQueryLocator inplaceSelect = pjq("span[id$=inplaceSelect] input[id$=inplaceSelectFocus]");
    private JQueryLocator inplaceInput = pjq("span[id$=inplaceInput] input[id$=inplaceInputFocus]");
    private JQueryLocator selectManyCheckbox = pjq("input[id$=selectManyCheckbox:4]");
    private JQueryLocator selectManyListbox = pjq("select[id$=selectManyListbox]");
    private JQueryLocator selectManyMenu = pjq("select[id$=selectManyMenu]");
    private JQueryLocator selectBooleanCheckbox = pjq("input[id$=selectBooleanCheckbox]");
    private JQueryLocator inputNumberSliderInput = pjq("span[id$=inputNumberSlider] input.rf-insl-inp");
    private JQueryLocator inputNumberSpinnerInput = pjq("span[id$=inputNumberSpinner] input.rf-insp-inp");

    private JQueryLocator globalMessagesContainer = pjq("span[id$=_globalMessages]");
    private JQueryLocator errorMessagesContainer = pjq("span.rf-msgs");
    private JQueryLocator header = pjq("div.rf-p-hdr[id$=gv1h_header]");

    private OptionValueLocator optionSmile = new OptionValueLocator(SMILE);

    private JQueryLocator applyChangesBtn = pjq("input[id$=applyChanges]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richGraphValidator/all.xhtml");
    }

    @Test
    public void testGroups() {
        for (int i = 0; i < GROUPS.length; ++i) {
            graphValidatorAttributes.set(groups, GROUPS[i]);
            waitFor(6000);
            setAllValidatedFields();

            if (i == BOOLEAN_INPUTS_GROUP) {
                // only Boolean inputs validated
                allFieldsSetToWrong();
                selenium.check(selectBooleanCheckbox, true);
            } else if (i == NUMERIC_INPUTS_GROUP) {
                // only numeric inputs validated
                allFieldsSetToWrong();
                selenium.type(inputNumberSliderInput, "10");
                selenium.type(inputNumberSpinnerInput, "10");
            }

            // let's submit the form
            selenium.click(applyChangesBtn);

            // wait for success validation
            waitGui.until(textEquals.locator(
                globalMessagesContainer.getDescendant(jq("span.rf-msgs-inf span.rf-msgs-sum"))).text(
                "Action sucessfully done!"));
        }
    }

    @Test
    public void testSummary() {
        String msg = "My own validation message!";
        graphValidatorAttributes.set(summary, msg);

        setAllValidatedFields();
        allFieldsSetToWrong();

        selenium.click(applyChangesBtn);

        waitGui.until(textEquals.locator(errorMessagesContainer.getDescendant(jq("span.rf-msgs-sum"))).text(msg));
    }

    @Test
    public void testValue() {

        graphValidatorAttributes.set(value, "testValue");

        setAllValidatedFields();

        // let's submit the form
        selenium.click(applyChangesBtn);

        // wait for success validation
        waitGui.until(textEquals
            .locator(globalMessagesContainer.getDescendant(jq("span.rf-msgs-inf span.rf-msgs-sum"))).text(
                "Action sucessfully done!"));
    }

    @Test
    public void testRendered() {
        setAllValidatedFields();

        graphValidatorAttributes.set(rendered, Boolean.FALSE);

        waitGui.until(elementNotPresent.locator(header));
    }

    private void setAllValidatedFields() {
        // inputSecret don't keed entered value after submit
        selenium.type(inputSecret, SMILE);

        // input returning List/Set are by default without any element checked
        selenium.check(selectManyCheckbox, true);
        selenium.select(selectManyListbox, optionSmile);
        selenium.select(selectManyMenu, optionSmile);
    }

    private void allFieldsSetToWrong() {
        String wrongString = "---";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String wrongDate = sdf.format(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));

        selenium.type(inplaceSelect, wrongString);
        selenium.type(inplaceInput, wrongString);
        selenium.type(inputNumberSpinnerInput, "10");
        selenium.check(selectBooleanCheckbox, false);
        selenium.type(inputSecret, wrongString);

        selenium.type(inputNumberSliderInput, "15");
        selenium.type(inputNumberSpinnerInput, "15");
        selenium.type(autocomplete, wrongString);
        selenium.type(inputText, wrongString);
        selenium.type(calendar, wrongDate);
        selenium.type(inputTextArea, wrongString);
    }
}
