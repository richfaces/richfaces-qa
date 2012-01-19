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
package org.richfaces.tests.metamer.ftest.richInputNumberSlider;

import static org.jboss.arquillian.ajocado.Ajocado.textEquals;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.TextContainsCondition;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Test for faces/components/richInputNumberSpinner/jsr303.xhtml page
 * 
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision: 23011 $
 */
public class TestRichSliderWithJSR303 extends AbstractAjocadoTest {

    /** Wrong value for input validated to min value */
    public static final String WRONG_MIN_VAL = "1";
    /** Wrong value for input validated to max value */
    public static final String WRONG_MAX_VAL = "5";
    /** Wrong value for input validated to custom value */
    public static final String WRONG_CUSTOM_VAL = "-1";

    /** Wrong value for input validated to min value */
    public static final String CORRECT_MIN_VAL = "3";
    /** Wrong value for input validated to max value */
    public static final String CORRECT_MAX_VAL = "1";
    /** Wrong value for input validated to custom value */
    public static final String CORRECT_CUSTOM_VAL = "5";

    /** validation message for input validated to min value */
    public static final String MSG_MIN = "must be greater than or equal to 2";
    /** validation message for input validated to max value */
    public static final String MSG_MAX = "must be less than or equal to 2";
    /** validation message for input validated to custom value */
    public static final String MSG_CUSTOM = "must be a positive number";

    private JQueryLocator inputFormat = pjq("span[id$=:slider{0}] span.rf-insl-inp-cntr > input");
    private JQueryLocator msgFormat = pjq("span.rf-msg[id$=:inputMsg{0}] span.rf-msg-det");
    private JQueryLocator outputFormat = pjq("span[id$=:output{0}]");
    private JQueryLocator sliderFormat = pjq("span[id$=:slider{0}] span.rf-insl-trc");

    private JQueryLocator hCommandBtn = pjq("input[id$=:hButton]");
    private JQueryLocator a4jCommandBtn = pjq("input[id$=:a4jButton]");

    /** Codes for inputs */
    private enum ID {
        /** input validated to min val */
        MIN(1),
        /** input validated to max val */
        MAX(2),
        /** input validated to custom (positive) val */
        CUSTOM(3);

        private int id;

        private ID(int i) {
            this.id = i;
        }

        public int val() {
            return id;
        }
    }

    /**
     * Position coordinates for slider. Width is 200px, positions are relative to this
     */
    private enum POSITION {
        LESS_THAN_ZERO(10), ZERO(100), MORE_THAN_TWO(150);

        private int position;

        private POSITION(int position) {
            this.position = position;
        }

        public int val() {
            return position;
        }
    }

    private void setAllCorrect() {
        selenium.type(inputFormat.format(ID.MIN.val()), CORRECT_MIN_VAL);
        selenium.fireEvent(inputFormat.format(ID.MIN.val()), Event.BLUR);
        selenium.type(inputFormat.format(ID.MAX.val()), CORRECT_MAX_VAL);
        selenium.fireEvent(inputFormat.format(ID.MAX.val()), Event.BLUR);
        selenium.type(inputFormat.format(ID.CUSTOM.val()), CORRECT_CUSTOM_VAL);
        selenium.fireEvent(inputFormat.format(ID.CUSTOM.val()), Event.BLUR);
    }

    private void setAllWrong() {
        selenium.type(inputFormat.format(ID.MIN.val()), WRONG_MIN_VAL);
        selenium.fireEvent(inputFormat.format(ID.MIN.val()), Event.BLUR);
        selenium.type(inputFormat.format(ID.MAX.val()), WRONG_MAX_VAL);
        selenium.fireEvent(inputFormat.format(ID.MAX.val()), Event.BLUR);
        selenium.type(inputFormat.format(ID.CUSTOM.val()), WRONG_CUSTOM_VAL);
        selenium.fireEvent(inputFormat.format(ID.CUSTOM.val()), Event.BLUR);

        // wait until validation appears on last input before go ahead (e.g. submit form)
        waitGui.until(TextContainsCondition.getInstance().text(MSG_CUSTOM).locator(msgFormat.format(ID.CUSTOM.val())));
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSlider/jsr303.xhtml");
    }

    @Test
    public void testMin() {
        selenium.type(inputFormat.format(ID.MIN.val()), WRONG_MIN_VAL);
        selenium.fireEvent(inputFormat.format(ID.MIN.val()), Event.BLUR);
        waitGui.until(TextContainsCondition.getInstance().locator(msgFormat.format(ID.MIN.val())).text(MSG_MIN));
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testSlideToMin() {
        setAllCorrect();

        selenium.mouseDownAt(sliderFormat.format(ID.MIN.val()), new Point(POSITION.ZERO.val(), 0));
        waitGui.until(TextContainsCondition.getInstance().locator(msgFormat.format(ID.MIN.val())).text(MSG_MIN));
    }

    @Test(groups = { "4.Future" })
    @Templates(value = { "richPopupPanel" })
    public void testSlideToMinInPopupPanel() {
        testSlideToMin();
    }

    @Test
    public void testMax() {
        selenium.type(inputFormat.format(ID.MAX.val()), WRONG_MAX_VAL);
        selenium.fireEvent(inputFormat.format(ID.MAX.val()), Event.BLUR);
        waitGui.until(TextContainsCondition.getInstance().locator(msgFormat.format(ID.MAX.val())).text(MSG_MAX));
    }

    @Test
    public void testSlideToMax() {
        setAllCorrect();

        selenium.mouseDownAt(sliderFormat.format(ID.MAX.val()), new Point(POSITION.MORE_THAN_TWO.val(), 0));
        waitGui.until(TextContainsCondition.getInstance().locator(msgFormat.format(ID.MAX.val())).text(MSG_MAX));
    }

    @Test
    public void testCustom() {
        selenium.type(inputFormat.format(ID.CUSTOM.val()), WRONG_CUSTOM_VAL);
        selenium.fireEvent(inputFormat.format(ID.CUSTOM.val()), Event.BLUR);
        waitGui.until(TextContainsCondition.getInstance().text(MSG_CUSTOM).locator(msgFormat.format(ID.CUSTOM.val())));
    }

    @Test
    @Templates(exclude = { "richPopupPanel" })
    public void testSlideToNegative() {
        setAllCorrect();

        selenium.mouseDownAt(sliderFormat.format(ID.CUSTOM.val()), new Point(POSITION.LESS_THAN_ZERO.val(), 0));
        waitGui.until(TextContainsCondition.getInstance().locator(msgFormat.format(ID.CUSTOM.val())).text(MSG_CUSTOM));
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11314")
    @Templates(value = { "richPopupPanel" })
    public void testSlideToNegativeInPopupPanel() {
        setAllCorrect();

        selenium.mouseDownAt(sliderFormat.format(ID.CUSTOM.val()), new Point(POSITION.LESS_THAN_ZERO.val(), 0));
        waitGui.until(TextContainsCondition.getInstance().locator(msgFormat.format(ID.CUSTOM.val())).text(MSG_CUSTOM));
    }

    @Test
    @Templates(exclude = { "a4jRegion" })
    public void testAllCorrect() {

        setAllCorrect();

        waitGui.until(textEquals.text(CORRECT_MIN_VAL).locator(outputFormat.format(ID.MIN.val())));
        Assert.assertEquals(selenium.getText(outputFormat.format(ID.MAX.val())), CORRECT_MAX_VAL);
        Assert.assertEquals(selenium.getText(outputFormat.format(ID.CUSTOM.val())), CORRECT_CUSTOM_VAL);

    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11264")
    @Templates(exclude = { "a4jRegion" })
    public void testAllWrong() {

        setAllCorrect();
        setAllWrong();

        selenium.click(hCommandBtn);

        waitGui.until(TextContainsCondition.getInstance().locator(msgFormat.format(ID.MIN.val())).text(MSG_MIN));
        Assert.assertEquals(selenium.getText(msgFormat.format(ID.MAX.val())), MSG_MAX);
        Assert.assertEquals(selenium.getText(msgFormat.format(ID.CUSTOM.val())), MSG_CUSTOM);

        setAllCorrect();
        setAllWrong();

        selenium.click(a4jCommandBtn);

        waitGui.until(TextContainsCondition.getInstance().locator(msgFormat.format(ID.MIN.val())).text(MSG_MIN));
        Assert.assertEquals(selenium.getText(msgFormat.format(ID.MAX.val())), MSG_MAX);
        Assert.assertEquals(selenium.getText(msgFormat.format(ID.CUSTOM.val())), MSG_CUSTOM);
    }

}
