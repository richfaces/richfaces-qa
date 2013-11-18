/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richNotifyMessages;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.TimeoutException;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.richNotify.TestNotifyAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.AttributeList;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.richfaces.fragment.notify.NotifyMessage.NotifyMessagePosition;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestNotifyMessagesAttributes extends AbstractNotifyMessagesTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richNotifyMessages/jsr303.xhtml");
    }

    @Test
    public void testAjaxRendered() {
        checkAjaxRendered();
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12923")
    @Test(groups = "Future")
    public void testDir() {
        checkDir();
    }

    @Test
    public void testEscape() {
        checkEscape();
    }

    @Test
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testFor() {
        checkFor(2);//2 messages
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11298")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testForInIterationComponents() {
        testFor();
    }

    @Test
    @Templates(exclude = { "richAccordion", "richCollapsiblePanel" })
    public void testGlobalOnly() {
        checkGlobalOnly(2);//2 messages
    }

    @Test
    @Templates(value = { "richAccordion", "richCollapsiblePanel" })
    @RegressionTest("https://issues.jboss.org/browse/RF-11415")
    public void testGlobalOnlyInAccordionCollapsiblePanel() {
        testGlobalOnly();
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12923")
    @Test(groups = "Future")
    public void testLang() {
        checkLang();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11433")
    public void testMessagesTypes() {
        checkMessagesTypes();
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12925")
    @Test(groups = "Future")
    public void testNoShowDetailNoShowSummary() {
        checkNoShowDetailNoShowSummary();
    }

    @Test
    public void testNonblocking() {
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.nonblocking, Boolean.TRUE);
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.nonblockingOpacity, 0);
        generateValidationMessagesWithWait();
        Utils.triggerJQ(executor, "mouseover", getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement());
        TestNotifyAttributes.waitForOpacityChange(0, getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement());
        Utils.triggerJQ(executor, "mouseout", getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement());
        TestNotifyAttributes.waitForOpacityChange(1, getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement());
    }

    @Test
    public void testNonblockingOpacity() {
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.nonblocking, Boolean.TRUE);
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.nonblockingOpacity, 0.5);
        generateValidationMessagesWithWait();
        Utils.triggerJQ(executor, "mouseover", getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement());
        TestNotifyAttributes.waitForOpacityChange(0.5, getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement());
        Utils.triggerJQ(executor, "mouseout", getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement());
        TestNotifyAttributes.waitForOpacityChange(1, getPage().getMessagesComponentWithGlobal().getItem(0).getRootElement());
    }

    @Test
    public void testOnClick() {
        checkOnClick();
    }

    @Test
    public void testOnDblClick() {
        checkOnDblClick();
    }

    @Test
    public void testOnKeyDown() {
        checkOnKeyDown();
    }

    @Test
    public void testOnKeyPress() {
        checkOnKeyPress();
    }

    @Test
    public void testOnKeyUp() {
        checkOnKeyUp();
    }

    @Test
    public void testOnMouseDown() {
        checkOnMouseDown();
    }

    @Test
    public void testOnMouseMove() {
        checkOnMouseMove();
    }

    @Test
    public void testOnMouseOut() {
        checkOnMouseOut();
    }

    @Test
    public void testOnMouseOver() {
        checkOnMouseOver();
    }

    @Test
    public void testOnMouseUp() {
        checkOnMouseUp();
    }

    @Test
    public void testRendered() {
        checkRendered();
    }

    @Test
    public void testShowCloseButton() {
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.showCloseButton, Boolean.TRUE);
        generateValidationMessagesWithWait();
        int sizeBefore = getPage().getMessagesComponentWithGlobal().size();
        getPage().getMessagesComponentWithGlobal().getItem(0).close();
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().size(), sizeBefore - 1);

        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.showCloseButton, Boolean.FALSE);
        generateValidationMessagesWithWait();
        try {
            getPage().getMessagesComponentWithGlobal().getItem(0).close();
        } catch (TimeoutException ok) {
            return;
        }
        Assert.fail("The notify message should not be closeable if there is no close button.");
    }

    @Test
    public void testShowDetail() {
        checkShowDetail();
    }

    @Test
    public void testShowShadow() {
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.showShadow, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertVisible(getPage().getMessagesComponentWithGlobal().getItem(0).advanced().getShadowElement(), "Shadow should be visible");

        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.showShadow, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertNotVisible(getPage().getMessagesComponentWithGlobal().getItem(0).advanced().getShadowElement(), "Shadow should not be visible");
    }

    @Test
    public void testShowSummary() {
        checkShowSummary();
    }

    @Test
    public void testStack() {
        String[] stacks = { "topLeftStack", "bottomRightStack", "notRenderedStack" };
        //default position is top right
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.stack, "");
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().getItem(0).advanced().getPosition(), NotifyMessagePosition.TOP_RIGHT);

        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.stack, stacks[0]);
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().getItem(0).advanced().getPosition(), NotifyMessagePosition.TOP_LEFT);

        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.stack, stacks[1]);
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().getItem(0).advanced().getPosition(), NotifyMessagePosition.BOTTOM_RIGHT);

        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.stack, stacks[2]);
        generateValidationMessagesWithWait();
        Assert.assertFalse(getPage().getMessagesComponentWithGlobal().advanced().isVisible());
        Assert.assertFalse(getPage().getMessagesComponentWithFor().advanced().isVisible());
    }

    @Test
    public void testStayTime() {
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.stayTime, 1000);
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.sticky, Boolean.FALSE);
        generateValidationMessagesWithWait();
        waiting(3000);
        Assert.assertEquals(getPage().getMessagesComponentWithGlobal().size(), 0, "There should be no message anymore.");
        Assert.assertEquals(getPage().getMessagesComponentWithFor().size(), 0, "There should be no message anymore.");
    }

    @Test
    public void testSticky() {
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.stayTime, 1000);
        AttributeList.notifyMessagesAttributes.set(NotifyMessagesAttributes.sticky, Boolean.TRUE);
        generateValidationMessagesWithWait();
        waiting(3000);
        Assert.assertTrue(getPage().getMessagesComponentWithGlobal().size() > 0, "There should be some messages.");
        Assert.assertTrue(getPage().getMessagesComponentWithFor().size() > 0, "There should be some messages.");
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12923")
    @Test(groups = "Future")
    public void testStyle() {
        checkStyle();
    }

    @Test
    public void testStyleClass() {
        checkStyleClass();
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12923")
    @Test(groups = "Future")
    public void testTitle() {
        checkTitle();
    }

    @Override
    protected void waitingForValidationMessagesToShow() {
        submitWithA4jBtn();
    }
}
