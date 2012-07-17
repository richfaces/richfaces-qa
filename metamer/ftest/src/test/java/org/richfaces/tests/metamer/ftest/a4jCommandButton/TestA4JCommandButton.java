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
package org.richfaces.tests.metamer.ftest.a4jCommandButton;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.commandButtonAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.event.PhaseId;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.jboss.test.selenium.support.ui.TextEquals;
import org.jboss.test.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestA4JCommandButton extends AbstractWebDriverTest {

    private CommandButtonPage page = new CommandButtonPage();
    private static final String STRING_RF1 = "RichFaces 4";
    private static final String STRING_RF1_X2 = "RichFaces 4RichFaces 4";
    private static final String STRING_RF2 = "RichFa";//first 6 characters
    private static final String STRING_RF3 = "RICHFACES 4";
    private static final String STRING_RF_UNICODE = "RichFaces 4š";
    private static final String STRING_RF_UNICODE_UPPERCASE = "RICHFACES 4Š";
    private static final String STRING_UNICODE1 = "ľščťžýáíéňô";
    private static final String STRING_UNICODE2 = "ľščťžý";
    private static final String STRING_UNICODE3 = "ĽŠČŤŽÝÁÍÉŇÔ";
    private static final String STRING_ACTIONLISTENER_MSG = "* action listener invoked";
    private static final String STRING_ACTION_MSG = "* action invoked";
    private static final String STRING_EXECUTE_CHECKER_MSG = "* executeChecker";
    @Inject
    @Use(empty = false)
    private String type;

    @BeforeMethod
    public void loadPage() {
        injectWebElementsToPage(page);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jCommandButton/simple.xhtml");
    }

    @Test(groups = "client-side-perf")
    public void testSimpleClick() {
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1);

        page.verifyOutput1Text();
        page.verifyOutput2Text();
        page.verifyOutput3Text();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleClickUnicode() {
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_UNICODE1);

        page.verifyOutput1Text(STRING_UNICODE1);
        page.verifyOutput2Text(STRING_UNICODE2);
        page.verifyOutput3Text(STRING_UNICODE3);
    }

    @Test
    public void testAction() {
        commandButtonAttributes.set(CommandButtonAttributes.action, "doubleStringAction");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1);
        page.verifyOutput2Text(STRING_RF1_X2);

        commandButtonAttributes.set(CommandButtonAttributes.action, "first6CharsAction");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1 + "ě");
        page.verifyOutput2Text(STRING_RF2);

        commandButtonAttributes.set(CommandButtonAttributes.action, "toUpperCaseAction");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF_UNICODE);
        page.verifyOutput2Text(STRING_RF_UNICODE_UPPERCASE);
    }

    @Test
    public void testActionListener() {
        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "doubleStringActionListener");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1);
        page.verifyOutput3Text(STRING_RF1_X2);

        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "first6CharsActionListener");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1 + "ě");
        page.verifyOutput3Text(STRING_RF2);

        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "toUpperCaseActionListener");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF_UNICODE);
        page.verifyOutput3Text(STRING_RF_UNICODE_UPPERCASE);
    }

    @Test
    public void testBypassUpdates() {
        commandButtonAttributes.set(CommandButtonAttributes.bypassUpdates, true);
        page.submit();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
        page.assertPhasesDontContainSomeOf(PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION);
        page.assertPhasesContainAllOf(STRING_ACTIONLISTENER_MSG, STRING_ACTION_MSG);
    }

    @Test
    public void testData() {
        commandButtonAttributes.set(CommandButtonAttributes.data, STRING_RF1);
        commandButtonAttributes.set(CommandButtonAttributes.oncomplete, "data = event.data");
        //Does not matter what we type here
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1);
        String data = expectedReturnJS("return window.data", STRING_RF1);
        assertEquals(data, STRING_RF1);
    }

    @Test
    public void testDisabled() {
        commandButtonAttributes.set(CommandButtonAttributes.disabled, true);
        assertTrue(page.button.getAttribute("disabled").equalsIgnoreCase("true"));
    }

    @Test
    public void testExecute() {
        commandButtonAttributes.set(CommandButtonAttributes.execute, "input executeChecker");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1);
        page.assertPhasesContainAllOf(STRING_EXECUTE_CHECKER_MSG);
    }

    @Test
    public void testImmediate() {
        commandButtonAttributes.set(CommandButtonAttributes.immediate, true);
        page.submit();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
        page.assertPhasesDontContainSomeOf(PhaseId.PROCESS_VALIDATIONS, PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION);
        page.assertPhasesContainAllOf(STRING_ACTIONLISTENER_MSG, STRING_ACTION_MSG);
    }

    @Test
    public void testLimitRender() {
        commandButtonAttributes.set(CommandButtonAttributes.limitRender, true);
        String timeValue = page.getRequestTime();
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1);
        assertEquals(page.getRequestTime(), timeValue, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    @Test
    public void testOnBegin() {
        page.testEvent(CommandButtonAttributes.onbegin);
    }

    @Test
    public void testOnBeforeDOMUpdate() {
        page.testEvent(CommandButtonAttributes.onbeforedomupdate);
    }

    @Test
    public void testOnComplete() {
        page.testEvent(CommandButtonAttributes.oncomplete);
    }

    @Test
    public void testOnclick() {
        page.testFireJSEvent(CommandButtonAttributes.onclick);
    }

    @Test
    public void testOndblclick() {
        page.testFireJSEvent(CommandButtonAttributes.ondblclick);
    }

    @Test
    public void testOnkeydown() {
        page.testFireJSEvent(CommandButtonAttributes.onkeydown);
    }

    @Test
    public void testOnkeypress() {
        page.testFireJSEvent(CommandButtonAttributes.onkeypress);
    }

    @Test
    public void testOneyup() {
        page.testFireJSEvent(CommandButtonAttributes.onkeyup);
    }

    @Test
    public void testOnmousedown() {
        page.testFireJSEvent(CommandButtonAttributes.onmousedown);
    }

    @Test
    public void testOnmousemove() {
        page.testFireJSEvent(CommandButtonAttributes.onmousemove);
    }

    @Test
    public void testOnmouseout() {
        page.testFireJSEvent(CommandButtonAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        page.testFireJSEvent(CommandButtonAttributes.onmouseover);
    }

    @Test
    public void testOnmouseup() {
        page.testFireJSEvent(CommandButtonAttributes.onmouseup);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10555")
    public void testRender() {
        commandButtonAttributes.set(CommandButtonAttributes.action, "doubleStringAction");
        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "doubleStringActionListener");

        commandButtonAttributes.set(CommandButtonAttributes.render, "output1");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(STRING_RF1);
        page.verifyOutput1Text(STRING_RF1);
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");

        page.typeToInputAndSubmitAndWaitUntilOutput1Changes("");

        commandButtonAttributes.set(CommandButtonAttributes.render, "output2 output3");
        page.typeToInputAndSubmitAndWaitUntilOutput2ChangesToText(STRING_RF1, STRING_RF1_X2);
        page.verifyOutput1Text("");
        page.verifyOutput2Text(STRING_RF1_X2);
        page.verifyOutput3Text(STRING_RF1_X2);
    }

    @Test
    public void testRendered() {
        commandButtonAttributes.set(CommandButtonAttributes.rendered, false);
        page.assertButtonNotPresent();
    }

    @Test
    public void testStyle() {
        testStyle(page.button);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9307")
    public void testStyleClass() {
        testStyleClass(page.button);
    }

    @Test
    public void testTitle() {
        testTitle(page.button);
    }

    @Test
    @Use(field = "type", strings = { "image", "reset", "submit", "button" })
    public void testType() {
        page.testType(type);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10115")
    public void testTypeNull() {
        page.testTypeNull();
    }

    @Test
    public void testValue() {
        commandButtonAttributes.set(CommandButtonAttributes.value, STRING_RF1);
        page.assertButtonValue(STRING_RF1);
    }

    private class CommandButtonPage {

        @FindBy(css = "input[id$=input]")
        WebElement input;
        @FindBy(css = "input[id$=a4jCommandButton]")
        WebElement button;
        @FindBy(css = "span[id$=output1]")
        WebElement output1;
        @FindBy(css = "span[id$=output2]")
        WebElement output2;
        @FindBy(css = "span[id$=output3]")
        WebElement output3;
        @FindBy(css = "span[id$=requestTime]")
        WebElement requestTime;
        @FindBy(css = "div#phasesPanel li")
        List<WebElement> phases;

        public void typeToInputAndSubmitAndWaitUntilOutput1Changes(String s) {
            typeToInputAndSubmit(s);
            new WebDriverWait(driver).until(TextEquals.getInstance().element(output1).text(s));
        }

        public void typeToInputAndSubmitAndWaitUntilOutput2ChangesToText(String testValue, String expectedText) {
            typeToInputAndSubmit(testValue);
            new WebDriverWait(driver, 5).until(TextEquals.getInstance().element(output2).text(expectedText));
        }

        public void typeToInputAndSubmit(String s) {
            input.clear();
            input.sendKeys(s);
            submit();
        }

        public void submit() {
            waitRequest(button, WaitRequestType.XHR).click();
        }

        public String getRequestTime() {
            return requestTime.getText();
        }

        public void verifyOutput1Text() {
            verifyOutput1Text(STRING_RF1);
        }

        public void verifyOutput2Text() {
            verifyOutput2Text(STRING_RF2);
        }

        public void verifyOutput3Text() {
            verifyOutput3Text(STRING_RF3);
        }

        public void verifyOutput1Text(String s) {
            verifyOutputText(output1, s);
        }

        public void verifyOutput2Text(String s) {
            verifyOutputText(output2, s);
        }

        public void verifyOutput3Text(String s) {
            verifyOutputText(output3, s);
        }

        private void verifyOutputText(WebElement elem, String text) {
            assertEquals(elem.getText(), text);
        }

        public List<String> getPhases() {
            List<String> result = new ArrayList<String>();
            for (WebElement webElement : phases) {
                result.add(webElement.getText());
            }
            return result;
        }

        public void assertPhasesDontContainSomeOf(PhaseId... phase) {
            assertTrue(new PhasesWrapper(getPhases()).notContainsSomeOf(phase), "Phases contain some of " + Arrays.asList(phase));
        }

        public void assertPhasesContainAllOf(String... s) {
            assertTrue(new PhasesWrapper(getPhases()).containsAllOf(s), "Phases don't contain some of " + Arrays.asList(s));
        }

        public void assertButtonNotPresent() {
            assertTrue(ElementNotPresent.getInstance().element(button).apply(driver), "Button should not be on page.");
        }

        public void assertButtonValue(String value) {
            assertEquals(button.getAttribute("value"), value, "Button's value did not change");
        }

        public void testType(String value) {
            testHTMLAttribute(button, commandButtonAttributes, CommandButtonAttributes.type, value);
        }

        public void testTypeNull() {
            testHTMLAttribute(button, commandButtonAttributes, CommandButtonAttributes.type, "null", "submit");
        }

        public void testFireJSEvent(CommandButtonAttributes event) {
            testFireEventWithJS(button, commandButtonAttributes, event);
        }

        public void testEvent(CommandButtonAttributes testedAttribute) {
            testFireEvent(commandButtonAttributes, testedAttribute, new Action() {

                @Override
                public void perform() {
                    button.click();
                }
            });
        }
    }
}
