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
package org.richfaces.tests.metamer.ftest.a4jCommandButton;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.commandButtonAttributes;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.commandLinkAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.test.selenium.support.ui.ElementNotPresent;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.a4jCommandLink.CommandLinkAttributes;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M1
 */
public class TestA4JCommandButton extends AbstractWebDriverTest {

    @Page
    private CommandButtonLinkPage page;

    @Inject
    @Use(empty = false)
    private String type;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jCommandButton/simple.xhtml");
    }

    @Test(groups = "client-side-perf")
    public void testSimpleClick() {
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);

        page.verifyOutput1Text();
        page.verifyOutput2Text();
        page.verifyOutput3Text();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleClickUnicode() {
        page.typeToInput(CommandButtonLinkPage.STRING_UNICODE1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_UNICODE1);

        page.verifyOutput1Text(CommandButtonLinkPage.STRING_UNICODE1);
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_UNICODE2);
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_UNICODE3);
    }

    @Test
    public void testAction() {
        commandButtonAttributes.set(CommandButtonAttributes.action, "doubleStringAction");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_RF1_X2);

        commandButtonAttributes.set(CommandButtonAttributes.action, "first6CharsAction");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1 + "ě");
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1 + "ě");
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_RF2);

        commandButtonAttributes.set(CommandButtonAttributes.action, "toUpperCaseAction");
        page.typeToInput(CommandButtonLinkPage.STRING_RF_UNICODE);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF_UNICODE);
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_RF_UNICODE_UPPERCASE);
    }

    @Test
    public void testActionListener() {
        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "doubleStringActionListener");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_RF1_X2);

        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "first6CharsActionListener");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1 + "ě");
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1 + "ě");
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_RF2);

        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "toUpperCaseActionListener");
        page.typeToInput(CommandButtonLinkPage.STRING_RF_UNICODE);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF_UNICODE);
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_RF_UNICODE_UPPERCASE);
    }

    @Test
    public void testBypassUpdates() {
        commandButtonAttributes.set(CommandButtonAttributes.bypassUpdates, true);
        MetamerPage.waitRequest(page.button, WaitRequestType.XHR).click();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, CommandButtonLinkPage.STRING_ACTIONLISTENER_MSG);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, CommandButtonLinkPage.STRING_ACTION_MSG);
    }

    @Test
    public void testData() {
        commandButtonAttributes.set(CommandButtonAttributes.data, CommandButtonLinkPage.STRING_RF1);
        commandButtonAttributes.set(CommandButtonAttributes.oncomplete, "data = event.data");
        //Does not matter what we type here
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        String data = expectedReturnJS("return window.data", CommandButtonLinkPage.STRING_RF1);
        assertEquals(data, CommandButtonLinkPage.STRING_RF1);
    }

    @Test
    public void testDisabled() {
        commandButtonAttributes.set(CommandButtonAttributes.disabled, true);
        assertTrue(page.button.getAttribute("disabled").equalsIgnoreCase("true"));
    }

    @Test
    public void testExecute() {
        commandButtonAttributes.set(CommandButtonAttributes.execute, "input executeChecker");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, CommandButtonLinkPage.STRING_EXECUTE_CHECKER_MSG);
    }

    @Test
    public void testImmediate() {
        commandButtonAttributes.set(CommandButtonAttributes.immediate, true);
        MetamerPage.waitRequest(page.button, WaitRequestType.XHR).click();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");

        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, CommandButtonLinkPage.STRING_ACTIONLISTENER_MSG);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, CommandButtonLinkPage.STRING_ACTION_MSG);
    }

    @Test
    public void testLimitRender() {
        commandButtonAttributes.set(CommandButtonAttributes.limitRender, true);
        commandButtonAttributes.set(CommandButtonAttributes.render, "output1 requestTime");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput1Text(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
    }

    @Test
    public void testEvents() {
        commandLinkAttributes.set(CommandLinkAttributes.onbegin, "metamerEvents += \"begin \"");
        commandLinkAttributes.set(CommandLinkAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        commandLinkAttributes.set(CommandLinkAttributes.oncomplete, "metamerEvents += \"complete \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");
        String reqTime = page.requestTime.getText();
        Graphene.guardXhr(page.button).click();
        Graphene.waitModel().withMessage("Page was not updated")
            .until(Graphene.element(page.requestTime).not().textEquals(reqTime));

        String[] events = ((JavascriptExecutor) driver).executeScript("return metamerEvents").toString().split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testOnbegin() {
        testFireEvent(commandButtonAttributes, CommandButtonAttributes.onbegin, new Action() {
            @Override
            public void perform() {
                page.button.click();
            }
        });
    }

    @Test
    public void testOnbeforedomupdate() {
        testFireEvent(commandButtonAttributes, CommandButtonAttributes.onbeforedomupdate, new Action() {
            @Override
            public void perform() {
                page.button.click();
            }
        });
    }

    @Test
    public void testOncomplete() {
        testFireEvent(commandButtonAttributes, CommandButtonAttributes.oncomplete, new Action() {
            @Override
            public void perform() {
                page.button.click();
            }
        });
    }

    @Test
    public void testOnclick() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onclick);
    }

    @Test
    public void testOndblclick() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.ondblclick);
    }

    @Test
    public void testOnkeydown() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onkeydown);
    }

    @Test
    public void testOnkeypress() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onkeypress);
    }

    @Test
    public void testOneyup() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onkeyup);
    }

    @Test
    public void testOnmousedown() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onmousedown);
    }

    @Test
    public void testOnmousemove() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onmousemove);
    }

    @Test
    public void testOnmouseout() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onmouseout);
    }

    @Test
    public void testOnmouseover() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onmouseover);
    }

    @Test
    public void testOnmouseup() {
        testFireEventWithJS(page.button, commandButtonAttributes, CommandButtonAttributes.onmouseup);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10555")
    public void testRender() {
        commandButtonAttributes.set(CommandButtonAttributes.action, "doubleStringAction");
        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "doubleStringActionListener");

        commandButtonAttributes.set(CommandButtonAttributes.render, "output1");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput1Text(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");

        page.typeToInput("");
        page.submitByButton();
        page.waitUntilOutput1Changes("");

        commandButtonAttributes.set(CommandButtonAttributes.render, "output2 output3");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput2ChangesToText(CommandButtonLinkPage.STRING_RF1_X2);
        page.verifyOutput1Text("");
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_RF1_X2);
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_RF1_X2);
    }

    @Test
    public void testRendered() {
        commandButtonAttributes.set(CommandButtonAttributes.rendered, false);
        assertTrue(ElementNotPresent.getInstance().element(page.button).apply(driver), "Button should not be on page.");
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
        testHTMLAttribute(page.button, commandButtonAttributes, CommandButtonAttributes.type, type);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10115")
    public void testTypeNull() {
        testHTMLAttribute(page.button, commandButtonAttributes, CommandButtonAttributes.type, "null", "submit");
    }

    @Test
    public void testValue() {
        commandButtonAttributes.set(CommandButtonAttributes.value, CommandButtonLinkPage.STRING_RF1);
        assertEquals(page.button.getAttribute("value"), CommandButtonLinkPage.STRING_RF1, "Button's value did not change");
    }
}
