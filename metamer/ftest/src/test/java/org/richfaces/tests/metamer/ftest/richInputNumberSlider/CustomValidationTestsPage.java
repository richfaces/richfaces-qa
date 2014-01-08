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
package org.richfaces.tests.metamer.ftest.richInputNumberSlider;

import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inputNumberSlider.RichFacesInputNumberSlider;
import org.richfaces.tests.metamer.ftest.abstractions.validations.NumberInputValidationPage;
import org.richfaces.tests.metamer.ftest.abstractions.validations.ValidationMessageCase;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CustomValidationTestsPage extends NumberInputValidationPage {

    @FindBy(css = "span[id$='min']")
    private RichFacesInputNumberSlider sliderMin;
    @FindBy(css = "span[id$='max']")
    private RichFacesInputNumberSlider sliderMax;
    @FindBy(css = "span[id$='custom']")
    private RichFacesInputNumberSlider sliderCustom;

    /**
     * Position coordinates for slider. Width is 200px, positions are relative to this
     */
    private enum Position {
        // the trace has ~ 191px
        //-1 ~ 86
        // 0 ~ 96
        // 3 ~ 126

        LESS_THAN_ZERO(86), ZERO(96), MORE_THAN_TWO(126);
        private final int position;

        private Position(int position) {
            this.position = position;
        }
    }

    private void moveSliderWithWaitRequest(int byPixels, RichFacesInputNumberSlider slider) {
        MetamerPage.waitRequest(slider.advanced(), MetamerPage.WaitRequestType.XHR)
                .dragHandleToPointInTrace(byPixels);
    }

    @RegressionTest("https://issues.jboss.org/browse/RF-11314")
    public void verifyCustomBySliding(String submitMethod, WebDriverWait wait) {
        moveSliderWithWaitRequest(Position.LESS_THAN_ZERO.position, sliderCustom);
        submit(submitMethod);
        ValidationMessageCase vmc = getMessageCases().get(MESSAGE_CUSTOM_NAME);
        vmc.waitForMessageShow(wait);
        vmc.assertMessageDetailIsCorrect();
        assertNoOtherMessagesAreVisible(vmc);
        assertOtherOutputsAreDefault(null);//all outputs should be default
    }

    public void verifyMaxBySliding(String submitMethod, WebDriverWait wait) {
        moveSliderWithWaitRequest(Position.MORE_THAN_TWO.position, sliderMax);
        submit(submitMethod);
        ValidationMessageCase vmc = getMessageCases().get(MESSAGE_MAX_NAME);
        vmc.waitForMessageShow(wait);
        vmc.assertMessageDetailIsCorrect();
        assertNoOtherMessagesAreVisible(vmc);
        assertOtherOutputsAreDefault(null);//all outputs should be default
    }

    public void verifyMinBySliding(String submitMethod, WebDriverWait wait) {
        moveSliderWithWaitRequest(Position.ZERO.position, sliderMin);
        submit(submitMethod);
        ValidationMessageCase vmc = getMessageCases().get(MESSAGE_MIN_NAME);
        vmc.waitForMessageShow(wait);
        vmc.assertMessageDetailIsCorrect();
        assertNoOtherMessagesAreVisible(vmc);
        assertOtherOutputsAreDefault(null);//all outputs should be default
    }
}
