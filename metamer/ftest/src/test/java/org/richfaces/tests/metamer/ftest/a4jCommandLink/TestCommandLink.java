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
package org.richfaces.tests.metamer.ftest.a4jCommandLink;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.interactions.Action;
import org.richfaces.fragment.common.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.a4jCommandButton.CommandButtonLinkPage;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jCommandLink/simple.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
public class TestCommandLink extends AbstractWebDriverTest {

    private final Attributes<CommandLinkAttributes> commandLinkAttributes = getAttributes();

    @Page
    private CommandButtonLinkPage page;

    private final Action submitAction = new Action() {
        @Override
        public void perform() {
            page.submitByLink();
        }
    };

    @Override
    public String getComponentTestPagePath() {
        return "a4jCommandLink/simple.xhtml";
    }

    @Test
    @CoversAttributes("accesskey")
    @Templates(value = "plain")
    public void testAccesskey() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.accesskey, "a");
    }

    @Test
    @CoversAttributes("action")
    public void testAction() {
        commandLinkAttributes.set(CommandLinkAttributes.action, "doubleStringAction");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_RF1_X2);

        commandLinkAttributes.set(CommandLinkAttributes.action, "first6CharsAction");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1 + "ě");
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1 + "ě");
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_RF2);

        commandLinkAttributes.set(CommandLinkAttributes.action, "toUpperCaseAction");
        page.typeToInput(CommandButtonLinkPage.STRING_RF_UNICODE);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF_UNICODE);
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_RF_UNICODE_UPPERCASE);
    }

    @Test
    @CoversAttributes("actionListener")
    public void testActionListener() {
        commandLinkAttributes.set(CommandLinkAttributes.actionListener, "doubleStringActionListener");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_RF1_X2);

        commandLinkAttributes.set(CommandLinkAttributes.actionListener, "first6CharsActionListener");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1 + "ě");
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1 + "ě");
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_RF2);

        commandLinkAttributes.set(CommandLinkAttributes.actionListener, "toUpperCaseActionListener");
        page.typeToInput(CommandButtonLinkPage.STRING_RF_UNICODE);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF_UNICODE);
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_RF_UNICODE_UPPERCASE);
    }

    @Test
    @CoversAttributes("bypassUpdates")
    public void testBypassUpdates() {
        commandLinkAttributes.set(CommandLinkAttributes.bypassUpdates, true);
        MetamerPage.waitRequest(page.getLinkElement(), WaitRequestType.XHR).click();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, CommandButtonLinkPage.STRING_ACTIONLISTENER_MSG);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, CommandButtonLinkPage.STRING_ACTION_MSG);
    }

    @Test
    @CoversAttributes("charset")
    @Templates(value = "plain")
    public void testCharset() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.charset, "utf-8");
    }

    @Test
    @CoversAttributes("coords")
    @Templates(value = "plain")
    public void testCoords() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.coords, "circle: 150, 60, 60");
    }

    @Test
    @CoversAttributes("data")
    public void testData() {
        testData(new Action() {
            @Override
            public void perform() {
                //Does not matter what we type here
                page.typeToInput(CommandButtonLinkPage.STRING_RF1);
                page.submitByLink();
                page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
            }
        });
    }

    @Test
    @CoversAttributes("disabled")
    public void testDisabled() {
        commandLinkAttributes.set(CommandLinkAttributes.disabled, true);

        Graphene.waitModel().until("Link should not be on page.").element(page.getLinkElement()).is().not().present();
        assertTrue(page.getDisabledLinkElement().isDisplayed(), "Link should be disabled.");
    }

    @Test
    @CoversAttributes({ "onbegin", "onbeforedomupdate", "oncomplete" })
    public void testEvents() {
        eventsOrderTester()
            .testOrderOfEvents(CommandLinkAttributes.onbegin, CommandLinkAttributes.onbeforedomupdate, CommandLinkAttributes.oncomplete)
            .triggeredByAction(submitAction).test();
    }

    @Test
    @CoversAttributes("execute")
    public void testExecute() {
        commandLinkAttributes.set(CommandLinkAttributes.execute, "input executeChecker");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, MetamerPage.STRING_EXECUTE_CHECKER_MSG);
    }

    @Test
    @CoversAttributes("hreflang")
    @Templates(value = "plain")
    public void testHreflang() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.hreflang, "sk");
    }

    @Test
    @CoversAttributes("immediate")
    public void testImmediate() {
        commandLinkAttributes.set(CommandLinkAttributes.immediate, true);

        String reqTime = page.getRequestTimeElement().getText();
        page.submitByLink();
        Graphene.waitModel().until("Page was not updated").element(page.getRequestTimeElement()).text().not().equalTo(reqTime);

        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");

        page.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, MetamerPage.STRING_ACTIONLISTENER_MSG);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, MetamerPage.STRING_ACTION_MSG);
    }

    @Test
    @CoversAttributes("limitRender")
    public void testLimitRender() {
        attsSetter()
            .setAttribute(CommandLinkAttributes.limitRender).toValue("true")
            .setAttribute(CommandLinkAttributes.render).toValue("output1 requestTime")
            .asSingleAction().perform();
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput1Text(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
    }

    @Test
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        eventTester().testEvent(CommandLinkAttributes.onbeforedomupdate).withCustomAction(submitAction).test();
    }

    @Test
    @CoversAttributes("onbegin")
    public void testOnbegin() {
        eventTester().testEvent(CommandLinkAttributes.onbegin).withCustomAction(submitAction).test();
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        eventTester().testEvent(CommandLinkAttributes.onclick).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        eventTester().testEvent(CommandLinkAttributes.oncomplete).withCustomAction(submitAction).test();
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        eventTester().testEvent(CommandLinkAttributes.ondblclick).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOneyup() {
        eventTester().testEvent(CommandLinkAttributes.onkeyup).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onerror")
    public void testOnerror() {
        commandLinkAttributes.set(CommandLinkAttributes.action, "causeAjaxErrorAction");
        testFireEvent("onerror", new Action() {
            @Override
            public void perform() {
                page.submitByLink();
            }
        });
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        eventTester().testEvent(CommandLinkAttributes.onkeydown).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        eventTester().testEvent(CommandLinkAttributes.onkeypress).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        eventTester().testEvent(CommandLinkAttributes.onkeypress).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        eventTester().testEvent(CommandLinkAttributes.onmousedown).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        eventTester().testEvent(CommandLinkAttributes.onmousemove).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        eventTester().testEvent(CommandLinkAttributes.onmouseout).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        eventTester().testEvent(CommandLinkAttributes.onmouseover).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        eventTester().testEvent(CommandLinkAttributes.onmouseup).onElement(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("rel")
    @Templates(value = "plain")
    public void testRel() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.rel, "metamer");
    }

    @Test
    @CoversAttributes("render")
    @RegressionTest("https://issues.jboss.org/browse/RF-10555")
    public void testRender() {
        attsSetter()
            .setAttribute(CommandLinkAttributes.action).toValue("doubleStringAction")
            .setAttribute(CommandLinkAttributes.actionListener).toValue("doubleStringActionListener")
            .setAttribute(CommandLinkAttributes.render).toValue("output1")
            .asSingleAction().perform();

        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput1Text(CommandButtonLinkPage.STRING_RF1);
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");

        page.typeToInput("");
        page.submitByLink();
        page.waitUntilOutput1Changes("");

        commandLinkAttributes.set(CommandLinkAttributes.render, "output2 output3");
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByLink();
        page.waitUntilOutput2ChangesToText(CommandButtonLinkPage.STRING_RF1_X2);
        page.verifyOutput1Text("");
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_RF1_X2);
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_RF1_X2);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        commandLinkAttributes.set(CommandLinkAttributes.rendered, false);

        Graphene.waitModel().until("Link should not be on page.").element(page.getLinkElement()).is().not().present();
    }

    @Test
    @CoversAttributes("rev")
    @Templates(value = "plain")
    public void testRev() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.rev, "metamer");
    }

    @Test
    @CoversAttributes("shape")
    @Templates(value = "plain")
    public void testShape() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.shape, "default");
    }

    @Test(groups = "client-side-perf")
    public void testSimpleClick() {
        page.typeToInput(CommandButtonLinkPage.STRING_RF1);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_RF1);

        page.verifyOutput1Text();
        page.verifyOutput2Text();
        page.verifyOutput3Text();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleClickUnicode() {
        page.typeToInput(CommandButtonLinkPage.STRING_UNICODE1);
        page.submitByLink();
        page.waitUntilOutput1Changes(CommandButtonLinkPage.STRING_UNICODE1);

        page.verifyOutput1Text(CommandButtonLinkPage.STRING_UNICODE1);
        page.verifyOutput2Text(CommandButtonLinkPage.STRING_UNICODE2);
        page.verifyOutput3Text(CommandButtonLinkPage.STRING_UNICODE3);
    }

    @Test
    @CoversAttributes("status")
    public void testStatus() {
        testStatus(new Actions(driver).click(page.getLinkElement()).build());
    }

    @Test
    @CoversAttributes("style")
    @Templates(value = "plain")
    public void testStyle() {
        htmlAttributeTester().testStyle(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("styleClass")
    @RegressionTest("https://issues.jboss.org/browse/RF-9307")
    @Templates(value = "plain")
    public void testStyleClass() {
        htmlAttributeTester().testStyleClass(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("tabindex")
    @Templates(value = "plain")
    public void testTabindex() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.tabindex, "10");
    }

    @Test
    @CoversAttributes("title")
    @Templates(value = "plain")
    public void testTitle() {
        htmlAttributeTester().testTitle(page.getLinkElement()).test();
    }

    @Test
    @CoversAttributes("type")
    @Templates(value = "plain")
    public void testType() {
        testHTMLAttribute(page.getLinkElement(), commandLinkAttributes, CommandLinkAttributes.type, "default");
    }

    @Test
    @CoversAttributes("value")
    @Templates("plain")
    public void testValue() {
        commandLinkAttributes.set(CommandLinkAttributes.value, "new label");
        assertEquals(page.getLinkElement().getText(), "new label", "Value of the button did not change");
    }
}
