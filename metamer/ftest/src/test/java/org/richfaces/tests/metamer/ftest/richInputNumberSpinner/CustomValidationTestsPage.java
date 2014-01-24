/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richInputNumberSpinner;

import static java.lang.Double.parseDouble;
import org.jboss.arquillian.graphene.wait.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.tests.metamer.ftest.abstractions.validations.NumberInputValidationPage;
import org.richfaces.tests.metamer.ftest.abstractions.validations.ValidationMessageCase;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CustomValidationTestsPage extends NumberInputValidationPage {

    @FindBy(css = "span[id$='min']")
    private RichFacesInputNumberSpinner spinnerMin;
    @FindBy(css = "span[id$='max']")
    private RichFacesInputNumberSpinner spinnerMax;
    @FindBy(css = "span[id$='custom']")
    private RichFacesInputNumberSpinner spinnerCustom;

    private enum Value {

        min(-1), max(5), custom(-1);
        private final int wrong;

        private Value(int wrong) {
            this.wrong = wrong;
        }

        public static Value valueForInput(int inputNumber) {
            return values()[inputNumber];
        }
    }

    private double getActValue(RichFacesInputNumberSpinner spinner) {
        return parseDouble(spinner.advanced().getInput().getStringValue());
    }

    public void verifyCustomBySpinning(WebDriverWait wait) {
        while (getActValue(spinnerCustom) > Value.custom.wrong) {
            MetamerPage.waitRequest(spinnerCustom, WaitRequestType.XHR).decrease();
        }
        ValidationMessageCase vmc = getMessageCases().get(MESSAGE_CUSTOM_NAME);
        vmc.waitForMessageShow(wait);
        assertOtherOutputsAreDefault(vmc);
        vmc.assertOutput(1);//last positive number
    }

    public void verifyMaxBySpinning(WebDriverWait wait) {
        while (getActValue(spinnerMax) < Value.max.wrong) {
            MetamerPage.waitRequest(spinnerMax, WaitRequestType.XHR).increase();
        }
        ValidationMessageCase vmc = getMessageCases().get(MESSAGE_MAX_NAME);
        vmc.waitForMessageShow(wait);
        assertOtherOutputsAreDefault(vmc);
        vmc.assertOutput(2);//last max number
    }

    public void verifyMinBySpinning(WebDriverWait wait) {
        while (getActValue(spinnerMin) > Value.min.wrong) {
            MetamerPage.waitRequest(spinnerMin, WaitRequestType.XHR).decrease();
        }
        ValidationMessageCase vmc = getMessageCases().get(MESSAGE_MIN_NAME);
        vmc.waitForMessageShow(wait);
        assertOtherOutputsAreDefault(vmc);
        vmc.assertOutput(2);//last max number
    }
}
