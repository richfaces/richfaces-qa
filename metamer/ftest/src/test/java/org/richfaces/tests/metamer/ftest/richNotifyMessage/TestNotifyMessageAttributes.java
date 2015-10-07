/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richNotifyMessage;

import org.openqa.selenium.TimeoutException;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.notify.NotifyMessage.NotifyMessagePosition;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richNotify.TestNotifyAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestNotifyMessageAttributes extends AbstractNotifyMessageTest {

    private final Attributes<NotifyMessageAttributes> notifyMessageAttributes = getAttributes();

    @Override
    public String getComponentTestPagePath() {
        return "richNotifyMessage/jsr303.xhtml";
    }

    @Test
    @CoversAttributes("ajaxRendered")
    public void testAjaxRendered() {
        checkAjaxRendered();
    }

    @Test
    @CoversAttributes("dir")
    @RegressionTest("https://issues.jboss.org/browse/RF-12923")
    @Templates("plain")
    public void testDir() {
        checkDir();
    }

    @Test
    @CoversAttributes("escape")
    public void testEscape() {
        checkEscape();
    }

    @Test
    @CoversAttributes("FOR")
    public void testFor() {
        checkFor();
    }

    @Test
    @CoversAttributes("lang")
    @RegressionTest("https://issues.jboss.org/browse/RF-12923")
    @Templates("plain")
    public void testLang() {
        checkLang();
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12925")
    @Test
    @Skip
    @CoversAttributes({ "showDetail", "showSummary" })
    public void testNoShowDetailNoShowSummary() {
        checkNoShowDetailNoShowSummary();
    }

    @Test
    @CoversAttributes("nonblocking")
    public void testNonblocking() {
        attsSetter()
            .setAttribute(NotifyMessageAttributes.nonblocking).toValue(true)
            .setAttribute(NotifyMessageAttributes.nonblockingOpacity).toValue(0)
            .asSingleAction().perform();
        generateValidationMessagesWithWait();
        Utils.triggerJQ(executor, "mouseover", getPage().getMessageComponentForFirstInput().advanced().getRootElement());
        TestNotifyAttributes.waitForOpacityChange(0, getPage().getMessageComponentForFirstInput().advanced().getRootElement());
        Utils.triggerJQ(executor, "mouseout", getPage().getMessageComponentForFirstInput().advanced().getRootElement());
        TestNotifyAttributes.waitForOpacityChange(1, getPage().getMessageComponentForFirstInput().advanced().getRootElement());
    }

    @Test
    @CoversAttributes("nonblockingOpacity")
    public void testNonblockingOpacity() {
        attsSetter()
            .setAttribute(NotifyMessageAttributes.nonblocking).toValue(true)
            .setAttribute(NotifyMessageAttributes.nonblockingOpacity).toValue(0.5)
            .asSingleAction().perform();
        generateValidationMessagesWithWait();
        Utils.triggerJQ(executor, "mouseover", getPage().getMessageComponentForFirstInput().advanced().getRootElement());
        TestNotifyAttributes.waitForOpacityChange(0.5, getPage().getMessageComponentForFirstInput().advanced().getRootElement());
        Utils.triggerJQ(executor, "mouseout", getPage().getMessageComponentForFirstInput().advanced().getRootElement());
        TestNotifyAttributes.waitForOpacityChange(1, getPage().getMessageComponentForFirstInput().advanced().getRootElement());
    }

    @Test
    @CoversAttributes("onclick")
    @Templates(value = "plain")
    public void testOnclick() {
        checkOnclick();
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates(value = "plain")
    public void testOndblclick() {
        checkOndblclick();
    }

    @Test
    @CoversAttributes("onkeydown")
    @Templates(value = "plain")
    public void testOnkeydown() {
        checkOnkeydown();
    }

    @Test
    @CoversAttributes("onkeypress")
    @Templates(value = "plain")
    public void testOnkeypress() {
        checkOnkeypress();
    }

    @Test
    @CoversAttributes("onkeyup")
    @Templates(value = "plain")
    public void testOnkeyup() {
        checkOnkeyup();
    }

    @Test
    @CoversAttributes("onmousedown")
    @Templates(value = "plain")
    public void testOnmousedown() {
        checkOnmousedown();
    }

    @Test
    @CoversAttributes("onmousemove")
    @Templates(value = "plain")
    public void testOnmousemove() {
        checkOnmousemove();
    }

    @Test
    @CoversAttributes("onmouseout")
    @Templates(value = "plain")
    public void testOnmouseout() {
        checkOnmouseout();
    }

    @Test
    @CoversAttributes("onmouseover")
    @Templates(value = "plain")
    public void testOnmouseover() {
        checkOnmouseover();
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates(value = "plain")
    public void testOnmouseup() {
        checkOnmouseup();
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        checkRendered();
    }

    @Test
    @CoversAttributes("showCloseButton")
    public void testShowCloseButton() {
        notifyMessageAttributes.set(NotifyMessageAttributes.showCloseButton, Boolean.TRUE);
        generateValidationMessagesWithWait();
        int sizeBefore = getPage().getGlobalNotify().size();
        getPage().getGlobalNotify().getItem(0).close();
        Assert.assertEquals(getPage().getGlobalNotify().size(), sizeBefore - 1);

        notifyMessageAttributes.set(NotifyMessageAttributes.showCloseButton, Boolean.FALSE);
        generateValidationMessagesWithWait();
        try {
            getPage().getGlobalNotify().getItem(0).close();
        } catch (TimeoutException ok) {
            return;
        }
        Assert.fail("The notify message should not be closeable if there is no close button.");
    }

    @Test
    @CoversAttributes("showDetail")
    public void testShowDetail() {
        checkShowDetail();
    }

    @Test
    @CoversAttributes("showShadow")
    @RegressionTest("https://issues.jboss.org/browse/RF-13792")
    public void testShowShadow() {
        notifyMessageAttributes.set(NotifyMessageAttributes.showShadow, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertVisible(getPage().getGlobalNotify().getItem(0).advanced().getShadowElement(), "Shadow should be visible");

        notifyMessageAttributes.set(NotifyMessageAttributes.showShadow, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertNotVisible(getPage().getGlobalNotify().getItem(0).advanced().getShadowElement(), "Shadow should not be visible");
    }

    @Test
    @CoversAttributes("showSummary")
    public void testShowSummary() {
        checkShowSummary();
    }

    @Test
    @CoversAttributes("stack")
    public void testStack() {
        String[] stacks = { "topLeftStack", "bottomRightStack", "notRenderedStack" };
        //default position is top right
        notifyMessageAttributes.set(NotifyMessageAttributes.stack, "");
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessageComponentForFirstInput().advanced().getPosition(), NotifyMessagePosition.TOP_RIGHT);

        notifyMessageAttributes.set(NotifyMessageAttributes.stack, stacks[0]);
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessageComponentForFirstInput().advanced().getPosition(), NotifyMessagePosition.TOP_LEFT);

        notifyMessageAttributes.set(NotifyMessageAttributes.stack, stacks[1]);
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessageComponentForFirstInput().advanced().getPosition(), NotifyMessagePosition.BOTTOM_RIGHT);

        notifyMessageAttributes.set(NotifyMessageAttributes.stack, stacks[2]);
        generateValidationMessagesWithWait();
        Assert.assertFalse(getPage().getMessageComponentForFirstInput().advanced().isVisible());
        Assert.assertFalse(getPage().getMessageComponentForSecondInput().advanced().isVisible());
        Assert.assertFalse(getPage().getMessageComponentForSelectableInput().advanced().isVisible());
    }

    @Test
    @CoversAttributes("stayTime")
    public void testStaytime() {
        attsSetter()
            .setAttribute(NotifyMessageAttributes.stayTime).toValue(1000)
            .setAttribute(NotifyMessageAttributes.sticky).toValue(false)
            .asSingleAction().perform();
        generateValidationMessagesWithWait();
        waiting(3000);
        Assert.assertEquals(getPage().getGlobalNotify().size(), 0, "There should be no message anymore.");
    }

    @Test
    @CoversAttributes("sticky")
    @RegressionTest("https://issues.jboss.org/browse/RF-11558")
    public void testSticky() {
        attsSetter()
            .setAttribute(NotifyMessageAttributes.stayTime).toValue(1000)
            .setAttribute(NotifyMessageAttributes.sticky).toValue(true)
            .setAttribute(NotifyMessageAttributes.showCloseButton).toValue(true)
            .asSingleAction().perform();
        generateValidationMessagesWithWait();
        waiting(3000);
        int size = getPage().getGlobalNotify().size();
        Assert.assertTrue(size > 0, "There should be some messages.");
        getPage().getGlobalNotify().getItem(0).close();
        Assert.assertEquals(getPage().getGlobalNotify().size(), size - 1, "There should be one message less than before.");

        // when sticky, the close button should be always visible ( https://issues.jboss.org/browse/RF-11558 )
        notifyMessageAttributes.set(NotifyMessageAttributes.showCloseButton, false);
        generateValidationMessagesWithWait();
        waiting(3000);
        size = getPage().getGlobalNotify().size();
        Assert.assertTrue(size > 0, "There should be some messages.");
        getPage().getGlobalNotify().getItem(0).close();
        Assert.assertEquals(getPage().getGlobalNotify().size(), size - 1, "There should be one message less than before.");
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12923")
    @Test
    @Skip
    @CoversAttributes("style")
    public void testStyle() {
        checkStyle();
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates(value = "plain")
    public void testStyleClass() {
        checkStyleClass();
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12923")
    @Test
    @Skip
    @CoversAttributes("title")
    @Templates("plain")
    public void testTitle() {
        checkTitle();
    }

    @Override
    protected void waitingForValidationMessagesToShow() {
        submitWithA4jBtn();
    }
}
