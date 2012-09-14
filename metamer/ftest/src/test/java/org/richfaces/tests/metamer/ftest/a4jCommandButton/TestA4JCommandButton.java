/**
 *  JBoss, Home of Professional Open Source
 *  Copyright 2012, Red Hat, Inc. and individual contributors
 *  by the @authors tag. See the copyright.txt in the distribution for a
 *  full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.a4jCommandButton;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.commandButtonAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.interactions.Action;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M1
 */
public class TestA4JCommandButton extends AbstractWebDriverTest<CommandButtonPage> {

    @Inject
    @Use(empty = false)
    private String type;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jCommandButton/simple.xhtml");
    }

    @Test(groups = "client-side-perf")
    public void testSimpleClick() {
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1);

        page.verifyOutput1Text();
        page.verifyOutput2Text();
        page.verifyOutput3Text();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleClickUnicode() {
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_UNICODE1);

        page.verifyOutput1Text(CommandButtonPage.STRING_UNICODE1);
        page.verifyOutput2Text(CommandButtonPage.STRING_UNICODE2);
        page.verifyOutput3Text(CommandButtonPage.STRING_UNICODE3);
    }

    @Test
    public void testAction() {
        commandButtonAttributes.set(CommandButtonAttributes.action, "doubleStringAction");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1);
        page.verifyOutput2Text(CommandButtonPage.STRING_RF1_X2);

        commandButtonAttributes.set(CommandButtonAttributes.action, "first6CharsAction");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1 + "ě");
        page.verifyOutput2Text(CommandButtonPage.STRING_RF2);

        commandButtonAttributes.set(CommandButtonAttributes.action, "toUpperCaseAction");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF_UNICODE);
        page.verifyOutput2Text(CommandButtonPage.STRING_RF_UNICODE_UPPERCASE);
    }

    @Test
    public void testActionListener() {
        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "doubleStringActionListener");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1);
        page.verifyOutput3Text(CommandButtonPage.STRING_RF1_X2);

        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "first6CharsActionListener");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1 + "ě");
        page.verifyOutput3Text(CommandButtonPage.STRING_RF2);

        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "toUpperCaseActionListener");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF_UNICODE);
        page.verifyOutput3Text(CommandButtonPage.STRING_RF_UNICODE_UPPERCASE);
    }

    @Test
    public void testBypassUpdates() {
        commandButtonAttributes.set(CommandButtonAttributes.bypassUpdates, true);
        Graphene.guardXhr(page.button).click();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, CommandButtonPage.STRING_ACTIONLISTENER_MSG);
        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, CommandButtonPage.STRING_ACTION_MSG);
    }

    @Test
    public void testData() {
        commandButtonAttributes.set(CommandButtonAttributes.data, CommandButtonPage.STRING_RF1);
        commandButtonAttributes.set(CommandButtonAttributes.oncomplete, "data = event.data");
        //Does not matter what we type here
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1);
        String data = expectedReturnJS("return window.data", CommandButtonPage.STRING_RF1);
        assertEquals(data, CommandButtonPage.STRING_RF1);
    }

    @Test
    public void testDisabled() {
        commandButtonAttributes.set(CommandButtonAttributes.disabled, true);
        assertTrue(page.button.getAttribute("disabled").equalsIgnoreCase("true"));
    }

    @Test
    public void testExecute() {
        commandButtonAttributes.set(CommandButtonAttributes.execute, "input executeChecker");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1);
        phaseInfo.assertListener(PhaseId.UPDATE_MODEL_VALUES, CommandButtonPage.STRING_EXECUTE_CHECKER_MSG);
    }

    @Test
    public void testImmediate() {
        commandButtonAttributes.set(CommandButtonAttributes.immediate, true);
        Graphene.guardXhr(page.button).click();
        page.verifyOutput1Text("");
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, CommandButtonPage.STRING_ACTIONLISTENER_MSG);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, CommandButtonPage.STRING_ACTION_MSG);
    }

    @Test
    public void testLimitRender() {
        commandButtonAttributes.set(CommandButtonAttributes.limitRender, true);
        commandButtonAttributes.set(CommandButtonAttributes.render, "output1 requestTime");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1);
        page.verifyOutput1Text(CommandButtonPage.STRING_RF1);
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");
    }

    @Test
    public void testOnBegin() {
        testFireEvent(commandButtonAttributes, CommandButtonAttributes.onbegin, new Action() {
            @Override
            public void perform() {
                page.button.click();
            }
        });
    }

    @Test
    public void testOnBeforeDOMUpdate() {
        testFireEvent(commandButtonAttributes, CommandButtonAttributes.onbeforedomupdate, new Action() {
            @Override
            public void perform() {
                page.button.click();
            }
        });
    }

    @Test
    public void testOnComplete() {
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
    @IssueTracking("https://issues.jboss.org/browse/RF-10555")
    public void testRender() {
        commandButtonAttributes.set(CommandButtonAttributes.action, "doubleStringAction");
        commandButtonAttributes.set(CommandButtonAttributes.actionListener, "doubleStringActionListener");

        commandButtonAttributes.set(CommandButtonAttributes.render, "output1");
        page.typeToInputAndSubmitAndWaitUntilOutput1Changes(CommandButtonPage.STRING_RF1);
        page.verifyOutput1Text(CommandButtonPage.STRING_RF1);
        page.verifyOutput2Text("");
        page.verifyOutput3Text("");

        page.typeToInputAndSubmitAndWaitUntilOutput1Changes("");

        commandButtonAttributes.set(CommandButtonAttributes.render, "output2 output3");
        page.typeToInputAndSubmitAndWaitUntilOutput2ChangesToText(CommandButtonPage.STRING_RF1, CommandButtonPage.STRING_RF1_X2);
        page.verifyOutput1Text("");
        page.verifyOutput2Text(CommandButtonPage.STRING_RF1_X2);
        page.verifyOutput3Text(CommandButtonPage.STRING_RF1_X2);
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
        testHTMLAttribute(page.button, commandButtonAttributes, CommandButtonAttributes.type, type);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10115")
    public void testTypeNull() {
        testHTMLAttribute(page.button, commandButtonAttributes, CommandButtonAttributes.type, "null", "submit");
    }

    @Test
    public void testValue() {
        commandButtonAttributes.set(CommandButtonAttributes.value, CommandButtonPage.STRING_RF1);
        page.assertButtonValue(CommandButtonPage.STRING_RF1);
    }

    @Override
    protected CommandButtonPage createPage() {
        return new CommandButtonPage();
    }

}
