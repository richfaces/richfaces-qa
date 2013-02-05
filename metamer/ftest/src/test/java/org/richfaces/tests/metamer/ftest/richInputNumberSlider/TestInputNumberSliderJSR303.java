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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.rich.RichInputNumberSliderBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent.ClearType;
import org.richfaces.tests.page.fragments.impl.input.inputNumberSlider.RichFacesInputNumberSlider;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;
import org.testng.annotations.Test;

/**
 * Test for faces/components/richInputNumberSpinner/jsr303.xhtml page
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSliderJSR303 extends AbstractWebDriverTest {

    public static final String MSG_MIN = RichInputNumberSliderBean.MSG_MIN;
    public static final String MSG_MAX = RichInputNumberSliderBean.MSG_MAX;
    public static final String MSG_CUSTOM = "must be a positive number";
    //
    @FindBy(css = "span[id$='slider1']")
    RichFacesInputNumberSlider sliderMin;
    @FindBy(css = "span[id$='slider2']")
    RichFacesInputNumberSlider sliderMax;
    @FindBy(css = "span[id$='slider3']")
    RichFacesInputNumberSlider sliderCustom;
    @FindBy(css = "span.rf-msg[id$=inputMsg1]")
    RichFacesMessage msgMin;
    @FindBy(css = "span.rf-msg[id$=inputMsg2]")
    RichFacesMessage msgMax;
    @FindBy(css = "span.rf-msg[id$=inputMsg3]")
    RichFacesMessage msgCustom;
    //
    @FindBy(css = "input[id$=a4jButton]")
    WebElement a4jCommandBtn;
    @FindBy(css = "input[id$=hButton]")
    WebElement hCommandBtn;
    @FindBy(css = "span[id$=output1]")
    WebElement output1;
    @FindBy(css = "span[id$=output2]")
    WebElement output2;
    @FindBy(css = "span[id$=output3]")
    WebElement output3;

    /**
     * Position coordinates for slider. Width is 200px, positions are relative to this
     */
    private enum Position {

        LESS_THAN_ZERO(10), ZERO(100), MORE_THAN_TWO(150);
        private int position;

        private Position(int position) {
            this.position = position;
        }

        public int val() {
            return position;
        }
    }

    private enum Value {

        min(3, 1), max(1, 5), custom(5, -1);
        public final int correct;
        public final int wrong;

        private Value(int correct, int wrong) {
            this.correct = correct;
            this.wrong = wrong;
        }

        public static Value valueForInput(int inputNumber) {
            return values()[inputNumber];
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSlider/jsr303.xhtml");
    }

    private void moveSliderWithXHRWaitRequest(int byPixels, RichFacesInputNumberSlider slider) {
        MetamerPage.waitRequest(slider.getNumberSlider(), WaitRequestType.XHR)
                .moveHandleToPointInTraceHorizontally(byPixels);
    }

    private List<RichFacesInputNumberSlider> getSliders() {
        return Arrays.asList(sliderMin, sliderMax, sliderCustom);
    }

    private List<WebElement> getOutputs() {
        return Arrays.asList(output1, output2, output3);
    }

    private void setAllCorrect() {
        List<RichFacesInputNumberSlider> sliders = getSliders();
        for (int i = 0; i < sliders.size(); i++) {
            typeToInputWithXHRWaitRequest(Value.valueForInput(i).correct, sliders.get(i));
        }
    }

    private void setAllWrong() {
        List<RichFacesInputNumberSlider> sliders = getSliders();
        for (int i = 0; i < sliders.size(); i++) {
            typeToInputWithXHRWaitRequest(Value.valueForInput(i).wrong, sliders.get(i));
        }
        // wait until validation appears on last input before go ahead (e.g. submit form)
        Graphene.waitAjax().until(msgCustom.isVisibleCondition());
    }

    @Test
    @Templates(exclude = { "a4jRegion" })
    public void testAllCorrect() {
        setAllCorrect();
        List<WebElement> outputs = getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            assertEquals(outputs.get(i).getText(), String.valueOf(Value.valueForInput(i).correct));
        }
    }

    @Test(groups = "4.Future")
    @IssueTracking({ "https://issues.jboss.org/browse/RF-11264", "https://issues.jboss.org/browse/RF-12301" })
    @Templates(value = { "a4jRegion" })
    public void testAllCorrectInA4jRegion() {
        setAllCorrect();
        List<WebElement> outputs = getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            assertEquals(outputs.get(i).getText(), String.valueOf(Value.valueForInput(i).correct));
        }
    }

    @Test
    @Templates(exclude = { "a4jRegion" })
    public void testAllWrong() {
        setAllCorrect();
        setAllWrong();

        MetamerPage.waitRequest(hCommandBtn, WaitRequestType.HTTP).click();

        assertEquals(msgMin.getDetail(), MSG_MIN);
        assertEquals(msgMax.getDetail(), MSG_MAX);
        assertEquals(msgCustom.getDetail(), MSG_CUSTOM);

        setAllCorrect();
        setAllWrong();

        MetamerPage.waitRequest(a4jCommandBtn, WaitRequestType.XHR).click();

        assertEquals(msgMin.getDetail(), MSG_MIN);
        assertEquals(msgMax.getDetail(), MSG_MAX);
        assertEquals(msgCustom.getDetail(), MSG_CUSTOM);
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-11264", "https://issues.jboss.org/browse/RF-12301" })
    @Templates(value = { "a4jRegion" })
    public void testAllWrongInA4jRegion() {
        setAllCorrect();
        setAllWrong();

        MetamerPage.waitRequest(hCommandBtn, WaitRequestType.HTTP).click();

        assertEquals(msgMin.getDetail(), MSG_MIN);
        assertEquals(msgMax.getDetail(), MSG_MAX);
        assertEquals(msgCustom.getDetail(), MSG_CUSTOM);

        setAllCorrect();
        setAllWrong();

        MetamerPage.waitRequest(a4jCommandBtn, WaitRequestType.XHR).click();

        assertEquals(msgMin.getDetail(), MSG_MIN);
        assertEquals(msgMax.getDetail(), MSG_MAX);
        assertEquals(msgCustom.getDetail(), MSG_CUSTOM);
    }

    @Test
    public void testCustom() {
        typeToInputWithXHRWaitRequest(Value.custom.wrong, sliderCustom);
        assertEquals(msgCustom.getDetail(), MSG_CUSTOM);
    }

    @Test
    public void testMax() {
        typeToInputWithXHRWaitRequest(Value.max.wrong, sliderMax);
        assertEquals(msgMax.getDetail(), MSG_MAX);
    }

    @Test
    public void testMin() {
        typeToInputWithXHRWaitRequest(Value.min.wrong, sliderMin);
        assertEquals(msgMin.getDetail(), MSG_MIN);
    }

    @Test
    public void testSlideToMax() {
        setAllCorrect();
        moveSliderWithXHRWaitRequest(Position.MORE_THAN_TWO.position, sliderMax);
        assertEquals(msgMax.getDetail(), MSG_MAX);
    }

    @Test
    public void testSlideToMin() {
        setAllCorrect();
        moveSliderWithXHRWaitRequest(Position.ZERO.position, sliderMin);
        assertEquals(msgMin.getDetail(), MSG_MIN);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11314")
    public void testSlideToNegative() {
        setAllCorrect();
        moveSliderWithXHRWaitRequest(Position.LESS_THAN_ZERO.position, sliderCustom);
        assertEquals(msgCustom.getDetail(), MSG_CUSTOM);
    }

    private void typeToInputWithXHRWaitRequest(int num, RichFacesInputNumberSlider slider) {
        MetamerPage.waitRequest(slider.getInput().clear(ClearType.JS)
                .fillIn(String.valueOf(num)), WaitRequestType.XHR).trigger("blur");
    }
}
