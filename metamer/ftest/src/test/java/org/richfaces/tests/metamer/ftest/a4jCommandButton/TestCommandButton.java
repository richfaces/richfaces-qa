/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.STRINGS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M1
 */
public class TestCommandButton extends AbstractWebDriverTest {

    private final Attributes<CommandButtonAttributes> commandButtonAttributes = getAttributes();

    @Page
    private CommandButtonLinkPage page;

    private String type;

    @Override
    public String getComponentTestPagePath() {
        return "a4jCommandButton/simple.xhtml";
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

    @Test(groups = "smoke")
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
    @CoversAttributes("accesskey")
    @Templates(value = "plain")
    public void testAccesskey() {
        testHTMLAttribute(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.accesskey, "a");
    }

    @Test
    @CoversAttributes("action")
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
    @CoversAttributes("actionListener")
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
    @CoversAttributes("bypassUpdates")
    public void testBypassUpdates() {
        commandButtonAttributes.set(CommandButtonAttributes.bypassUpdates, true);
        MetamerPage.waitRequest(page.getButtonElement(), WaitRequestType.XHR).click();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, CommandButtonLinkPage.STRING_ACTIONLISTENER_MSG);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, CommandButtonLinkPage.STRING_ACTION_MSG);
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                //Does not matter what we type here
                page.typeToInput(CommandButtonLinkPage.STRING_RF1);
                page.submitByButton();
                page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
            }
        });
    }

    @Test
    @CoversAttributes("disabled")
    public void testDisabled() {
        commandButtonAttributes.set(CommandButtonAttributes.disabled, true);
        assertTrue(page.getButtonElement().getAttribute("disabled").equalsIgnoreCase("true"));
    }

    @Test
    @CoversAttributes("execute")
    public void testExecute() {
        commandButtonAttributes.set(CommandButtonAttributes.execute, "input executeChecker");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByButton();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, MetamerPage.STRING_EXECUTE_CHECKER_MSG);
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        commandButtonAttributes.set(CommandButtonAttributes.immediate, true);
        MetamerPage.waitRequest(page.getButtonElement(), WaitRequestType.XHR).click();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");

        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, CommandButtonLinkPage.STRING_ACTIONLISTENER_MSG);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, CommandButtonLinkPage.STRING_ACTION_MSG);
    }

    @Test
    @CoversAttributes("limitRender")
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
    @CoversAttributes({ "onbegin", "onbeforedomupdate", "oncomplete" })
    public void testEvents() {
        commandButtonAttributes.set(CommandButtonAttributes.onbegin, "metamerEvents += \"begin \"");
        commandButtonAttributes.set(CommandButtonAttributes.onbeforedomupdate, "metamerEvents += \"beforedomupdate \"");
        commandButtonAttributes.set(CommandButtonAttributes.oncomplete, "metamerEvents += \"complete \"");

        ((JavascriptExecutor) driver).executeScript("metamerEvents = \"\"");
        MetamerPage.waitRequest(page.getButtonElement(), WaitRequestType.XHR).click();

        String[] events = ((JavascriptExecutor) driver).executeScript("return metamerEvents").toString().split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    @CoversAttributes("onbegin")
    public void testOnbegin() {
        testFireEvent(commandButtonAttributes, CommandButtonAttributes.onbegin, new Action() {
            @Override
            public void perform() {
                page.getButtonElement().click();
            }
        });
    }

    @Test
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        testFireEvent(commandButtonAttributes, CommandButtonAttributes.onbeforedomupdate, new Action() {
            @Override
            public void perform() {
                page.getButtonElement().click();
            }
        });
    }

    @Test
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        testFireEvent(commandButtonAttributes, CommandButtonAttributes.oncomplete, new Action() {
            @Override
            public void perform() {
                page.getButtonElement().click();
            }
        });
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onclick);
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.ondblclick);
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onkeydown);
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onkeypress);
    }

    @Test
    @CoversAttributes("onerror")
    public void testOnerror() {
        commandButtonAttributes.set(CommandButtonAttributes.action, "causeAjaxErrorAction");
        testFireEvent("onerror", new Action() {
            @Override
            public void perform() {
                page.submitByButton();
            }
        });
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOneyup() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onkeyup);
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onmousedown);
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onmousemove);
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onmouseout);
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onmouseover);
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        testFireEventWithJS(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.onmouseup);
    }

    @Test
    @CoversAttributes("render")
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
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        commandButtonAttributes.set(CommandButtonAttributes.rendered, false);
        assertFalse(new WebElementConditionFactory(page.getButtonElement()).isPresent().apply(driver), "Button should not be on page.");
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        testStatus(new Actions(driver).click(page.getButtonElement()).build());
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.getButtonElement());
    }

    @Test
    @CoversAttributes("styleClass")
    @RegressionTest("https://issues.jboss.org/browse/RF-9307")
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.getButtonElement());
    }

    @Test
    @CoversAttributes("tabindex")
    @Templates(value = "plain")
    public void testTabindex() {
        testHTMLAttribute(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.tabindex, "10");
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        htmlAttributeTester().testTitle(page.getButtonElement()).test();
    }

    @Test
    @CoversAttributes("type")
    @UseWithField(field = "type", valuesFrom = STRINGS, value = { "image", "reset", "submit", "button" })
    public void testType() {
        testHTMLAttribute(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.type, type);
    }

    @Test
    @CoversAttributes("type")
    @RegressionTest("https://issues.jboss.org/browse/RF-10115")
    public void testTypeNull() {
        testHTMLAttribute(page.getButtonElement(), commandButtonAttributes, CommandButtonAttributes.type, "null", "submit");
    }

    @Test
    @CoversAttributes("value")
    @Templates("plain")
    public void testValue() {
        commandButtonAttributes.set(CommandButtonAttributes.value, CommandButtonLinkPage.STRING_RF1);
        assertEquals(page.getButtonElement().getAttribute("value"), CommandButtonLinkPage.STRING_RF1, "Button's value did not change");
    }
}
