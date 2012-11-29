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
package org.richfaces.tests.metamer.ftest.richMessages;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.messagesAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.richfaces.tests.page.fragments.impl.message.MessageComponent.MessageType;
import org.richfaces.tests.page.fragments.impl.messages.MessagesComponentImpl;
import org.testng.annotations.BeforeMethod;

/**
 * Common test case for rich:messages component
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractRichMessagesTest extends AbstractWebDriverTest {

    @Page
    protected MessagesPage page;

    // components
    @FindBy(xpath = "//fieldset/span[contains(@id, 'messagesWithFor')]")
    protected MessagesComponentImpl messagesWithFor;
    @FindBy(xpath = "//fieldset/span[contains(@id, 'messagesWithGlobal')]")
    protected MessagesComponentImpl messagesWithGlobal;

    protected void generateAllKindsOfMessagesWithWait() {
        setCorrectValues();
        submitWithA4jBtn();
        MetamerPage.waitRequest(page.generateMsgsBtn, WaitRequestType.XHR).click();
        Graphene.waitAjax().until(messagesWithGlobal.isVisibleCondition());
    }

    private void generateValidationMessagesWithoutWait() {
        executeJS("window.valuesSettingState=''");
        page.wrongValuesButton.click();
        waitForValuesSetting();
    }

    @BeforeMethod(alwaysRun = true)
    public void generateValidationMessagesWithWait() {
        generateValidationMessagesWithoutWait();
        waitingForValidationMessages();
    }

    private String getIDOfElement(WebElement element) {
        return element.getAttribute("id");
    }

    protected String getSimpleInput1ID() {
        return getIDOfElement(page.simpleInput1);
    }

    protected String getSimpleInput2ID() {
        return getIDOfElement(page.simpleInput2);
    }

    private WebElement getTestedElementRoot() {
        return messagesWithGlobal.getRoot();
    }

    /**
     * Sets correct values by clicking button and wait for the client update.
     * !Does not do any request!.
     */
    protected void setCorrectValues() {
        page.correctValuesButton.click();
        waitForValuesSetting();
    }

    protected void submitWithA4jBtn() {
        MetamerPage.waitRequest(page.a4jCommandButton, WaitRequestType.XHR).click();
    }

    protected void submitWithHBtn() {
        MetamerPage.waitRequest(page.hCommandButton, WaitRequestType.HTTP).click();
    }
    // ==================== test methods ====================

    public void testAjaxRendered() {
        assertTrue(messagesWithFor.isVisible());
        assertTrue(messagesWithGlobal.isVisible());

        messagesAttributes.set(MessagesAttributes.ajaxRendered, Boolean.FALSE);
        generateValidationMessagesWithoutWait();

        assertFalse(messagesWithFor.isVisible());
        assertFalse(messagesWithGlobal.isVisible());

        //submit with h:commandbutton
        MetamerPage.waitRequest(page.hCommandButton, WaitRequestType.HTTP).click();
        assertTrue(messagesWithFor.isVisible());
        assertTrue(messagesWithGlobal.isVisible());
    }

    public void testDir() {
        super.testDir(getTestedElementRoot());
    }

    public void testEscape() {
        //this will only show 1 message after generation of validation msgs,
        //because of unique id of span, taht will be created
        messagesAttributes.set(MessagesAttributes.globalOnly, Boolean.TRUE);
        String newSpanString = "<span id='newSpan'>newSpan</span>";
        page.simpleInput1.clear();
        page.simpleInput1.sendKeys(newSpanString);
        submitWithA4jBtn();
        assertTrue(Graphene.waitGui().until(Graphene.element(page.newSpan).not().isVisible()).booleanValue());

        messagesAttributes.set(MessagesAttributes.escape, Boolean.FALSE);
        page.simpleInput1.clear();
        page.simpleInput1.sendKeys(newSpanString);
        submitWithA4jBtn();
        assertTrue(Graphene.waitGui().until(Graphene.element(page.newSpan).isVisible()).booleanValue());
    }

    public void testFor(int expectedMessages) {
        // firstly, remove value from attribute for and generate message
        messagesAttributes.setLower(MessagesAttributes.FOR, "");
        generateValidationMessagesWithoutWait();
        submitWithA4jBtn();

        assertFalse(messagesWithFor.isVisible());

        // now set for attribute to "simpleInput1"
        messagesAttributes.setLower(MessagesAttributes.FOR, "simpleInput1");
        generateValidationMessagesWithWait();

        assertTrue(messagesWithFor.isVisible());
        assertEquals(messagesWithFor.size(), expectedMessages);
        assertEquals(messagesWithFor.getMessagesForInput(getSimpleInput1ID()).size(), expectedMessages, expectedMessages + " messages for input 1 were expected.");
        assertEquals(messagesWithFor.getMessagesForInput(getSimpleInput2ID()).size(), 0, "No messages for input 2 were expected.");

        // now set for attribute back to "simpleInput2"
        messagesAttributes.setLower(MessagesAttributes.FOR, "simpleInput2");
        generateValidationMessagesWithWait();

        assertTrue(messagesWithFor.isVisible());
        assertEquals(messagesWithFor.size(), expectedMessages);
        assertEquals(messagesWithFor.getMessagesForInput(getSimpleInput1ID()).size(), 0, "No messages for input 1 were expected.");
        assertEquals(messagesWithFor.getMessagesForInput(getSimpleInput2ID()).size(), expectedMessages, expectedMessages + " messages for input 2 were expected.");
    }

    /**
     * globalOnly change behavior of displaying messages.
     * When <b>true</b> only messages not bound to any input are displayed
     *      <b>false</b> all messages are displayed
     * This attribute cannot be set with <i>for</i> attribute.
     *
     * In this case, messages component messagesGlobal is relevant.
     */
    public void testGlobalOnly(int expectedMessagesPerInput) {
        //firstly set for attribute to null
        messagesAttributes.setLower(MessagesAttributes.FOR, "");
        //then set globalOnly attribute
        messagesAttributes.set(MessagesAttributes.globalOnly, Boolean.FALSE);

        generateValidationMessagesWithWait();
        //All messages should appear:
        assertTrue(messagesWithGlobal.isVisible());
        assertEquals(messagesWithGlobal.size(), expectedMessagesPerInput * 2);
        assertEquals(messagesWithGlobal.getMessagesForInput(getSimpleInput1ID()).size(), expectedMessagesPerInput, expectedMessagesPerInput + " messages for input 1 were expected.");
        assertEquals(messagesWithGlobal.getMessagesForInput(getSimpleInput2ID()).size(), expectedMessagesPerInput, expectedMessagesPerInput + " messages for input 2 were expected.");

        messagesAttributes.set(MessagesAttributes.globalOnly, Boolean.TRUE);
        generateValidationMessagesWithoutWait();
        //no messages should appear:
        assertFalse(messagesWithGlobal.isVisible());
    }

    public void testLang() {
        testAttributeLang(getTestedElementRoot());
    }

    public void testMesssagesTypes() {
        generateAllKindsOfMessagesWithWait();
        assertEquals(messagesWithFor.size(), 4);
        messagesWithFor.getMessage(0).isType(MessageType.fatal);
        messagesWithFor.getMessage(1).isType(MessageType.error);
        messagesWithFor.getMessage(2).isType(MessageType.warning);
        messagesWithFor.getMessage(3).isType(MessageType.information);
    }

    public void testNoShowDetailNoShowSummary() {
        messagesAttributes.set(MessagesAttributes.showSummary, Boolean.FALSE);
        messagesAttributes.set(MessagesAttributes.showDetail, Boolean.FALSE);

        generateValidationMessagesWithoutWait();
        submitWithA4jBtn();

        assertFalse(messagesWithGlobal.isVisible());
        assertFalse(messagesWithFor.isVisible());
    }

    public void testOnClick() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onclick);
    }

    public void testOnDblClick() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.ondblclick);
    }

    public void testOnKeyDown() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onkeydown);
    }

    public void testOnKeyPress() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onkeypress);
    }

    public void testOnKeyUp() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onkeyup);
    }

    public void testOnMouseDown() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onmousedown);
    }

    public void testOnMouseMove() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onmousemove);
    }

    public void testOnMouseOut() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onmouseout);
    }

    public void testOnMouseOver() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onmouseover);
    }

    public void testOnMouseUp() {
        testFireEventWithJS(getTestedElementRoot(), messagesAttributes, MessagesAttributes.onmouseup);
    }

    public void testRendered() {
        messagesAttributes.set(MessagesAttributes.rendered, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(messagesWithGlobal.isVisible());
        assertTrue(messagesWithFor.isVisible());


        messagesAttributes.set(MessagesAttributes.rendered, Boolean.FALSE);
        generateValidationMessagesWithoutWait();
        submitWithA4jBtn();

        assertFalse(messagesWithGlobal.isVisible());
        assertFalse(messagesWithFor.isVisible());
    }

    public void testShowDetail() {
        messagesAttributes.set(MessagesAttributes.showSummary, Boolean.TRUE);
        messagesAttributes.set(MessagesAttributes.showDetail, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(messagesWithGlobal.getMessage(0).isDetailVisible());
        assertTrue(messagesWithFor.getMessage(0).isDetailVisible());

        messagesAttributes.set(MessagesAttributes.showDetail, Boolean.FALSE);
        generateValidationMessagesWithWait();

        assertFalse(messagesWithGlobal.getMessage(0).isDetailVisible());
        assertFalse(messagesWithFor.getMessage(0).isDetailVisible());
    }

    public void testShowSummary() {
        messagesAttributes.set(MessagesAttributes.showDetail, Boolean.TRUE);
        messagesAttributes.set(MessagesAttributes.showSummary, Boolean.TRUE);
        generateValidationMessagesWithWait();

        assertTrue(messagesWithGlobal.getMessage(0).isSummaryVisible());
        assertTrue(messagesWithFor.getMessage(0).isSummaryVisible());

        messagesAttributes.set(MessagesAttributes.showSummary, Boolean.FALSE);
        generateValidationMessagesWithWait();

        assertFalse(messagesWithGlobal.getMessage(0).isSummaryVisible());
        assertFalse(messagesWithFor.getMessage(0).isSummaryVisible());
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

    private void waitForValuesSetting() {
        String finishedString = "finished";
        String ret = expectedReturnJS("return window.valuesSettingState", finishedString);
        if (ret == null || !ret.equalsIgnoreCase(finishedString)) {
            throw new IllegalStateException("The setting of values with buttons was not acomplished.");
        }
    }

    protected void waitingForValidationMessages() {
        submitWithA4jBtn();
        Graphene.waitGui().until(messagesWithGlobal.isVisibleCondition());
    }
}
