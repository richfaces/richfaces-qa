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
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractRichMessageWDTest extends AbstractWebDriverTest<MessagePage> {

    @Override
    protected MessagePage createPage() {
        return new MessagePage();
    }

    @BeforeMethod(alwaysRun = true)
    public void generateMessages() {
        executeJS("window.valuesSettingState=''");
        generateValidationMessagesWithWait(true);
    }

    abstract WebElement getTestedElementRoot();

    abstract WebElement getTestedElementDetail();

    abstract WebElement getTestedElementSummary();
    // ==================== test methods ====================

    /**
     * Attribute 'for' change behavior: only messages bound to element with
     * id specified in 'for' should be displayed
     */
    public void testFor(WebElement testedElementRoot) {
        // firstly, remove value from attribute for and generate message
        messageAttributes.setLower(MessageAttributes.FOR, "");

        generateValidationMessagesWithWait(true);
        assertTrue(Graphene.waitAjax().until(Graphene.element(testedElementRoot).not().isVisible()).booleanValue());

        // now set for attribute back to "simpleInput2"
        messageAttributes.setLower(MessageAttributes.FOR, "simpleInput2");

        generateValidationMessagesWithWait(true);
        assertTrue(Graphene.waitAjax().until(Graphene.element(testedElementRoot).isVisible()).booleanValue());
    }

    public void testRendered(boolean byAjax) {
        messageAttributes.set(MessageAttributes.rendered, Boolean.TRUE);
        generateValidationMessagesWithWait(byAjax);

        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementRoot()).isVisible()).booleanValue());

        messageAttributes.set(MessageAttributes.rendered, Boolean.FALSE);
        generateValidationMessagesWithWait(byAjax);

        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementRoot()).not().isVisible()).booleanValue());
    }

    public void testShowSummary() {
        messageAttributes.set(MessageAttributes.showSummary, Boolean.TRUE);
        generateValidationMessagesWithWait(true);

        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementRoot()).isVisible()).booleanValue());
        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementSummary()).isVisible()).booleanValue());

        messageAttributes.set(MessageAttributes.showSummary, Boolean.FALSE);
        generateValidationMessagesWithWait(true);

        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementRoot()).not().isVisible()).booleanValue());
        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementSummary()).not().isVisible()).booleanValue());
    }

    public void testShowDetail() {
        messageAttributes.set(MessageAttributes.showDetail, Boolean.TRUE);
        generateValidationMessagesWithWait(true);

        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementRoot()).isVisible()).booleanValue());
        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementDetail()).isVisible()).booleanValue());

        messageAttributes.set(MessageAttributes.showDetail, Boolean.FALSE);
        generateValidationMessagesWithWait(true);

        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementRoot()).isVisible()).booleanValue());
        assertTrue(Graphene.waitModel().until(Graphene.element(
                getTestedElementDetail()).not().isVisible()).booleanValue());
    }

    public void testTitle() {
        super.testTitle(getTestedElementRoot());
    }

    public void testDir() {
        super.testDir(getTestedElementRoot());
    }

    public void testLang() {
        testAttributeLang(getTestedElementRoot());
    }

    public void testStyle() {
        super.testStyle(getTestedElementRoot());
    }

    public void testStyleClass() {
        super.testStyleClass(getTestedElementRoot());
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

    protected void setCorrectValues() {
        page.correctValuesButton.click();
        waitForValuesSetting();
    }

    protected void setWrongValues() {
        page.wrongValuesButton.click();
        waitForValuesSetting();
    }

    protected void generateValidationMessagesWithWait(boolean byAjax) {
        setWrongValues();
        if (byAjax) {
            waitRequest(Graphene.guardXhr(page.a4jCommandButton), WaitRequestType.XHR).click();
        } else {
            waitRequest(Graphene.guardHttp(page.hCommandButton), WaitRequestType.HTTP).click();
        }
    }

    private void waitForValuesSetting() {
        String finishedString = "finished";
        String ret = expectedReturnJS("return window.valuesSettingState", finishedString);
        if (ret == null || !ret.equalsIgnoreCase(finishedString)) {
            throw new IllegalStateException("The setting of values with buttons was not acomplished.");
        }
    }
}
