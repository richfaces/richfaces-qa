/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richMessage;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.messageAttributes;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.message.MessageComponentImpl;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
// FIXME AbstractRichMessageWDTest should not be generic (bug in Graphene)
public abstract class AbstractRichMessageTest<P extends MessagePage> extends AbstractWebDriverTest<P> {

    @FindBy(css = "span[id$=simpleInputMsg]")
    MessageComponentImpl messageComponentForInputX;
    @FindBy(css = "span[id$=simpleInputMsg1]")
    MessageComponentImpl messageComponentFoInput1;
    @FindBy(css = "span[id$=simpleInputMsg2]")
    MessageComponentImpl messageComponentForInput2;

    @BeforeMethod(alwaysRun = true)
    public void generateMessages() {
        executeJS("window.valuesSettingState=''");
        generateValidationMessagesWithWait();
    }

    protected abstract void waitingForValidationMessages();
    // ==================== test methods ====================

    public void testAjaxRendered() {
        assertTrue(messageComponentFoInput1.isVisible());
        assertTrue(messageComponentForInput2.isVisible());
        assertTrue(messageComponentForInputX.isVisible());

        messageAttributes.set(MessageAttributes.ajaxRendered, Boolean.FALSE);
        generateValidationMessagesWithWait();

        assertFalse(messageComponentFoInput1.isVisible());
        assertFalse(messageComponentForInput2.isVisible());
        assertFalse(messageComponentForInputX.isVisible());

        //submit with h:commandbutton
        MetamerPage.waitRequest(page.hCommandButton, WaitRequestType.HTTP).click();
        assertTrue(messageComponentFoInput1.isVisible());
        assertTrue(messageComponentForInput2.isVisible());
        assertTrue(messageComponentForInputX.isVisible());
    }

    public void testDir() {
        super.testDir(getTestedElementRoot());
    }

    public void testEscape() {
        String newSpanString = "<span id='newSpan'>newSpan</span>";
        page.simpleInput1.clear();
        page.simpleInput1.sendKeys(newSpanString);
        MetamerPage.waitRequest(page.a4jCommandButton, WaitRequestType.XHR).click();
        assertTrue(Graphene.waitGui().until(Graphene.element(page.newSpan).not().isVisible()).booleanValue());

        messageAttributes.set(MessageAttributes.escape, Boolean.FALSE);
        page.simpleInput1.clear();
        page.simpleInput1.sendKeys(newSpanString);
        MetamerPage.waitRequest(page.a4jCommandButton, WaitRequestType.XHR).click();
        assertTrue(Graphene.waitGui().until(Graphene.element(page.newSpan).isVisible()).booleanValue());
    }

    public void testFor() {
        // firstly, remove value from attribute for and generate message
        messageAttributes.setLower(MessageAttributes.FOR, "");
        generateValidationMessages();
        MetamerPage.waitRequest(page.a4jCommandButton, WaitRequestType.XHR).click();

        assertFalse(messageComponentForInputX.isVisible());

        // now set for attribute back to "simpleInput2"
        messageAttributes.setLower(MessageAttributes.FOR, "simpleInput2");
        generateValidationMessagesWithWait();

        assertTrue(messageComponentForInputX.isVisible());
    }

    public void testLang() {
        testAttributeLang(getTestedElementRoot());
    }

    public void testNoShowDetailNoShowSummary() {
        messageAttributes.set(MessageAttributes.showSummary, Boolean.FALSE);
        messageAttributes.set(MessageAttributes.showDetail, Boolean.FALSE);

        generateValidationMessages();
        MetamerPage.waitRequest(page.a4jCommandButton, WaitRequestType.XHR).click();

        assertFalse(messageComponentFoInput1.isVisible());
        assertFalse(messageComponentForInput2.isVisible());
        assertFalse(messageComponentForInputX.isVisible());
    }

    public void testOnClick() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onclick);
    }

    public void testOnDblClick() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.ondblclick);
    }

    public void testOnKeyDown() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onkeydown);
    }

    public void testOnKeyPress() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onkeypress);
    }

    public void testOnKeyUp() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onkeyup);
    }

    public void testOnMouseDown() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onmousedown);
    }

    public void testOnMouseMove() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onmousemove);
    }

    public void testOnMouseOut() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onmouseout);
    }

    public void testOnMouseOver() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onmouseover);
    }

    public void testOnMouseUp() {
        testFireEventWithJS(getTestedElementRoot(), messageAttributes, MessageAttributes.onmouseup);
    }

    public void testRendered() {
        messageAttributes.set(MessageAttributes.rendered, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(messageComponentFoInput1.isVisible());
        assertTrue(messageComponentForInput2.isVisible());
        assertTrue(messageComponentForInputX.isVisible());

        messageAttributes.set(MessageAttributes.rendered, Boolean.FALSE);
        generateValidationMessages();
        MetamerPage.waitRequest(page.a4jCommandButton, WaitRequestType.XHR).click();

        assertFalse(messageComponentFoInput1.isVisible());
        assertFalse(messageComponentForInput2.isVisible());
        assertFalse(messageComponentForInputX.isVisible());
    }

    public void testShowDetail() {
        messageAttributes.set(MessageAttributes.showSummary, Boolean.TRUE);
        messageAttributes.set(MessageAttributes.showDetail, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(messageComponentFoInput1.isDetailVisible());
        assertTrue(messageComponentForInput2.isDetailVisible());
        assertTrue(messageComponentForInputX.isDetailVisible());

        messageAttributes.set(MessageAttributes.showDetail, Boolean.FALSE);
        generateValidationMessagesWithWait();

        assertFalse(messageComponentFoInput1.isDetailVisible());
        assertFalse(messageComponentForInput2.isDetailVisible());
        assertFalse(messageComponentForInputX.isDetailVisible());
    }

    public void testShowSummary() {
        messageAttributes.set(MessageAttributes.showDetail, Boolean.TRUE);
        messageAttributes.set(MessageAttributes.showSummary, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(messageComponentFoInput1.isSummaryVisible());
        assertTrue(messageComponentForInput2.isSummaryVisible());
        assertTrue(messageComponentForInputX.isSummaryVisible());

        messageAttributes.set(MessageAttributes.showSummary, Boolean.FALSE);
        generateValidationMessagesWithWait();

        assertFalse(messageComponentFoInput1.isSummaryVisible());
        assertFalse(messageComponentForInput2.isSummaryVisible());
        assertFalse(messageComponentForInputX.isSummaryVisible());
    }

    public void testStyle() {
        super.testStyle(getTestedElementRoot());
    }

    public void testStyleClass() {
        super.testStyleClass(getTestedElementRoot());
    }

    public void testTitle() {
        super.testTitle(getTestedElementRoot());
    }

    private WebElement getTestedElementRoot() {
        return messageComponentFoInput1.getRoot();
    }

    private void generateValidationMessages() {
        page.wrongValuesButton.click();
        waitForValuesSetting();
    }

    private void generateValidationMessagesWithWait() {
        generateValidationMessages();
        waitingForValidationMessages();
    }

    protected void setCorrectValues() {
        page.correctValuesButton.click();
        waitForValuesSetting();
    }

    private void waitForValuesSetting() {
        String finishedString = "finished";
        String ret = expectedReturnJS("return window.valuesSettingState", finishedString);
        if (ret == null || !ret.equalsIgnoreCase(finishedString)) {
            throw new IllegalStateException("The setting of values with buttons was not acomplished.");
        }
    }
}
