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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.rich.RichInputNumberSpinnerBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent.ClearType;
import org.richfaces.tests.page.fragments.impl.input.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.tests.page.fragments.impl.message.RichFacesMessage;
import org.testng.annotations.Test;

/**
 * Test for faces/components/richInputNumberSpinner/jsr303.xhtml page
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 */
public class TestInputNumberSpinnerJSR303 extends AbstractWebDriverTest {

    /** validation message for input validated to min value */
    public static final String MSG_MIN = RichInputNumberSpinnerBean.MSG_MIN;
    /** validation message for input validated to max value */
    public static final String MSG_MAX = RichInputNumberSpinnerBean.MSG_MAX;
    /** validation message for input validated to custom value */
    public static final String MSG_CUSTOM = "must be a positive number";
    //
    @FindBy(css = "span[id$='spinner1']")
    RichFacesInputNumberSpinner spinnerMin;
    @FindBy(css = "span[id$='spinner2']")
    RichFacesInputNumberSpinner spinnerMax;
    @FindBy(css = "span[id$='spinner3']")
    RichFacesInputNumberSpinner spinnerCustom;
    //
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

    private enum Value {

        min(3, -1), max(1, 5), custom(5, -1);
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

    private double getActValue(RichFacesInputNumberSpinner spinner) {
        return Double.parseDouble(spinner.getInput().getStringValue());
    }

    public List<RichFacesMessage> getMessages() {
        return Arrays.asList(msgMin, msgMax, msgCustom);
    }

    public List<RichFacesInputNumberSpinner> getSpinners() {
        return Arrays.asList(spinnerMin, spinnerMax, spinnerCustom);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSpinner/jsr303.xhtml");
    }

    private void setAllCorrect() {
        List<RichFacesInputNumberSpinner> spinners = getSpinners();
        for (int i = 0; i < spinners.size(); i++) {
            typeToInputActionWithXHRWaitRequest(Value.valueForInput(i).correct, spinners.get(i));
        }
        for (RichFacesMessage message : getMessages()) {
            Graphene.waitAjax().until(message.isNotVisibleCondition());
        }
    }

    private void setAllWrong() {
        List<RichFacesInputNumberSpinner> spinners = getSpinners();
        for (int i = 0; i < spinners.size(); i++) {
            typeToInputActionWithXHRWaitRequest(Value.valueForInput(i).wrong, spinners.get(i));
        }
        Graphene.waitAjax().until(msgCustom.isVisibleCondition());//the last one
    }

    @Test
    @Templates(exclude = { "a4jRegion" })
    public void testAllCorrect() {
        setAllCorrect();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11264")
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
    public void testCustomBySpinning() {
        while (getActValue(spinnerCustom) > Value.custom.wrong) {
            MetamerPage.waitRequest(spinnerCustom, WaitRequestType.XHR).decrease();
        }
        Graphene.waitGui().until(msgCustom.isVisibleCondition());
        assertEquals(msgCustom.getDetail(), MSG_CUSTOM);
    }

    @Test
    public void testCustomByTyping() {
        typeToInputActionWithXHRWaitRequest(Value.custom.wrong, spinnerCustom);
        Graphene.waitGui().until(msgCustom.isVisibleCondition());
        assertEquals(msgCustom.getDetail(), MSG_CUSTOM);
    }

    @Test
    public void testMaxBySpinning() {
        while (getActValue(spinnerMax) < Value.max.wrong) {
            MetamerPage.waitRequest(spinnerMax, WaitRequestType.XHR).increase();
        }
        Graphene.waitGui().until(msgMax.isVisibleCondition());
        assertEquals(msgMax.getDetail(), MSG_MAX);
    }

    @Test
    public void testMaxByTyping() {
        typeToInputActionWithXHRWaitRequest(Value.max.wrong, spinnerMax);
        Graphene.waitGui().until(msgMax.isVisibleCondition());
        assertEquals(msgMax.getDetail(), MSG_MAX);
    }

    @Test
    public void testMinBySpinning() {
        while (getActValue(spinnerMin) > Value.min.wrong) {
            MetamerPage.waitRequest(spinnerMin, WaitRequestType.XHR).decrease();
        }
        Graphene.waitGui().until(msgMin.isVisibleCondition());
        assertEquals(msgMin.getDetail(), MSG_MIN);
    }

    @Test
    public void testMinByTyping() {
        typeToInputActionWithXHRWaitRequest(Value.min.wrong, spinnerMin);
        Graphene.waitGui().until(msgMin.isVisibleCondition());
        assertEquals(msgMin.getDetail(), MSG_MIN);
    }

    private void typeToInputActionWithXHRWaitRequest(int num, RichFacesInputNumberSpinner spinner) {
        MetamerPage.waitRequest(spinner.getInput().clear(ClearType.JS)
                .fillIn(String.valueOf(num)), WaitRequestType.XHR).trigger("blur");
    }
}
