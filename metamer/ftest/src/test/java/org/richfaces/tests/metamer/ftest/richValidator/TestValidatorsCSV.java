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
package org.richfaces.tests.metamer.ftest.richValidator;

import javax.faces.event.PhaseId;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richValidator/csv.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestValidatorsCSV extends AbstractValidatorsTest {

    @Override
    public String getComponentTestPagePath() {
        return "richValidator/csv.xhtml";
    }

    public Action getSuccessfulAjaxValidationAction() {
        return new CustomAction(getPage().getInputDecimalMinMax(), "5", true);
    }

    public Action getSuccessfulNonAjaxValidationAction() {
        return new CustomAction(getPage().getInputMax(), "-10", false);
    }

    public Action getUnsuccessfulAjaxValidationAction() {
        return new CustomAction(getPage().getInputDecimalMinMax(), "100", true);
    }

    public Action getUnsuccessfulNonAjaxValidationAction() {
        return new CustomAction(getPage().getInputMax(), "100", false);
    }

    @Test
    public void testAllWrong() {
        verifyAllWrongWithAjaxSubmit();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11035")
    public void testAllWrongJSF() {
        verifyAllWrongWithJSFSubmit();
    }

    @Test
    public void testBooleanFalse() {
        verifyBooleanFalse();
    }

    @Test
    public void testBooleanTrue() {
        verifyBooleanTrue();
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        // needs ajax request
        testData(getSuccessfulAjaxValidationAction());
        testData(getUnsuccessfulAjaxValidationAction());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-14174")
    @CoversAttributes({ "immediate", "listener" })
    public void testImmediate() {
        final String listenerMsg = "listener invoked";
        attsSetter()
            .setAttribute("listener").toValue("dummyListener")
            .setAttribute("immediate").toValue(false)
            .asSingleAction().perform();
        getSuccessfulAjaxValidationAction().perform();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, listenerMsg);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);

        getUnsuccessfulAjaxValidationAction().perform();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, listenerMsg);
        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);

        setAttribute("immediate", true);
        getSuccessfulAjaxValidationAction().perform();
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, listenerMsg);
        getMetamerPage().assertPhases(PhaseId.ANY_PHASE);

        getUnsuccessfulAjaxValidationAction().perform();
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, listenerMsg);
        getMetamerPage().assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
            PhaseId.RENDER_RESPONSE);
    }

    @Test
    public void testDateFuture() {
        verifyDateFuture();
    }

    @Test
    public void testDatePast() {
        verifyDatePast();
    }

    @Test
    public void testDecimalMinMax() {
        verifyDecimalMinMax();
    }

    @Test
    public void testIntegerMax() {
        verifyMax();
    }

    @Test
    public void testIntegerMin() {
        verifyMin();
    }

    @Test
    public void testIntegerMinMax() {
        verifyMinMax();
    }

    @Test
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        // needs ajax request
        testFireEvent("onbeforedomupdate", getSuccessfulAjaxValidationAction());
        testFireEvent("onbeforedomupdate", getUnsuccessfulAjaxValidationAction());
    }

    @Test
    @CoversAttributes("onbeforesubmit")
    public void testOnbeforesubmit() {
        // needs ajax request
        testFireEvent("onbeforesubmit", getSuccessfulAjaxValidationAction());
        testFireEvent("onbeforesubmit", getUnsuccessfulAjaxValidationAction());
    }

    @Test
    @CoversAttributes("onbegin")
    public void testOnbegin() {
        // needs ajax request
        testFireEvent("onbegin", getSuccessfulAjaxValidationAction());
        testFireEvent("onbegin", getUnsuccessfulAjaxValidationAction());
    }

    @Test
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        // needs ajax request
        testFireEvent("oncomplete", getSuccessfulAjaxValidationAction());
        testFireEvent("oncomplete", getUnsuccessfulAjaxValidationAction());
    }

    @Test
    @CoversAttributes({ "listener", "onerror" })
    public void testOnerror() {
        // needs ajax request
        setAttribute("listener", "causeAjaxErrorListener");
        testFireEvent("onerror", getSuccessfulAjaxValidationAction());
        testFireEvent("onerror", getUnsuccessfulAjaxValidationAction());
    }

    @Test
    @Templates("plain")
    @CoversAttributes("oninvalid")
    public void testOninvalid() {
        testFireEvent("oninvalid", getUnsuccessfulNonAjaxValidationAction());
        testFireEvent("oninvalid", getUnsuccessfulAjaxValidationAction());
    }

    @Test
    @Templates("plain")
    @CoversAttributes("onvalid")
    public void testOnvalid() {
        testFireEvent("onvalid", getSuccessfulNonAjaxValidationAction());
        testFireEvent("onvalid", getSuccessfulAjaxValidationAction());
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11035")
    public void testSelectionSize() {
        verifySelectionSize();
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        // needs ajax request
        testStatus(getSuccessfulAjaxValidationAction());
        testStatus(getUnsuccessfulAjaxValidationAction());
    }

    @Test
    public void testStringSize() {
        verifyStringSize();
    }

    @Test
    public void testTextCustomPattern() {
        verifyCustom();
    }

    @Test
    public void testTextNotEmpty() {
        verifyNotEmpty();
    }

    @Test
    public void testTextNotNull() {
        verifyNotNull();
    }

    @Test
    public void testTextPattern() {
        verifyPattern();
    }

    @Test
    public void testTextRegExp() {
        verifyRegExp();
    }

    private final class CustomAction implements Action {

        private final WebElement input;
        private final boolean isAjax;
        private final String value;

        public CustomAction(WebElement input, String value, boolean isAjax) {
            this.input = input;
            this.value = value;
            this.isAjax = isAjax;
        }

        @Override
        public void perform() {
            // have to clear the value with JS or there will be unwanted request when invoking 'input.clear()'
            Utils.jQ("val('')", input);
            // set value
            input.sendKeys(value);
            // blur >>> validation will be triggered
            blur(isAjax ? WaitRequestType.XHR : WaitRequestType.NONE);
        }
    }
}
