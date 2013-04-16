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
package org.richfaces.tests.metamer.ftest.richNotifyMessage;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.TimeoutException;
import org.richfaces.tests.metamer.ftest.richNotify.TestNotifyAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.AttributeList;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.notify.NotifyMessagePosition;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestNotifyMessageAttributes extends AbstractNotifyMessageTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richNotifyMessage/jsr303.xhtml");
    }

    @Test
    public void testAjaxRendered() {
        checkAjaxRendered();
    }

    @Test(groups = "Future")
    public void testDir() {
        checkDir();
    }

    @Test
    public void testEscape() {
        checkEscape();
    }

    @Test
    public void testFor() {
        checkFor();
    }

    @Test(groups = "Future")
    public void testLang() {
        checkLang();
    }

    @Test(groups = "Future")
    public void testNoShowDetailNoShowSummary() {
        checkNoShowDetailNoShowSummary();
    }

    @Test
    public void testNonblocking() {
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.nonblocking, Boolean.TRUE);
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.nonblockingOpacity, 0);
        generateValidationMessagesWithWait();
        Utils.triggerJQ("mouseover", getPage().getMessageComponentForFirstInput().getRoot());
        TestNotifyAttributes.waitForOpacityChange(0, getPage().getMessageComponentForFirstInput().getRoot());
        Utils.triggerJQ("mouseout", getPage().getMessageComponentForFirstInput().getRoot());
        TestNotifyAttributes.waitForOpacityChange(1, getPage().getMessageComponentForFirstInput().getRoot());
    }

    @Test
    public void testNonblockingOpacity() {
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.nonblocking, Boolean.TRUE);
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.nonblockingOpacity, 0.5);
        generateValidationMessagesWithWait();
        Utils.triggerJQ("mouseover", getPage().getMessageComponentForFirstInput().getRoot());
        TestNotifyAttributes.waitForOpacityChange(0.5, getPage().getMessageComponentForFirstInput().getRoot());
        Utils.triggerJQ("mouseout", getPage().getMessageComponentForFirstInput().getRoot());
        TestNotifyAttributes.waitForOpacityChange(1, getPage().getMessageComponentForFirstInput().getRoot());
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
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.showCloseButton, Boolean.TRUE);
        generateValidationMessagesWithWait();
        int sizeBefore = getPage().getGlobalNotify().size();
        getPage().getGlobalNotify().getMessageAtIndex(0).close();
        Assert.assertEquals(getPage().getGlobalNotify().size(), sizeBefore - 1);

        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.showCloseButton, Boolean.FALSE);
        generateValidationMessagesWithWait();
        try {
            getPage().getGlobalNotify().getMessageAtIndex(0).close();
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
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.showShadow, Boolean.TRUE);
        generateValidationMessagesWithWait();
        assertVisible(getPage().getGlobalNotify().getMessageAtIndex(0).getShadowElement(), "Shadow should be visible");

        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.showShadow, Boolean.FALSE);
        generateValidationMessagesWithWait();
        assertNotVisible(getPage().getGlobalNotify().getMessageAtIndex(0).getShadowElement(), "Shadow should not be visible");
    }

    @Test
    public void testShowSummary() {
        checkShowSummary();
    }

    @Test
    public void testStack() {
        String[] stacks = { "topLeftStack", "bottomRightStack", "notRenderedStack" };
        //default position is top right
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.stack, "");
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessageComponentForFirstInput().getPosition(), NotifyMessagePosition.TOP_RIGHT);

        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.stack, stacks[0]);
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessageComponentForFirstInput().getPosition(), NotifyMessagePosition.TOP_LEFT);

        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.stack, stacks[1]);
        generateValidationMessagesWithWait();
        Assert.assertEquals(getPage().getMessageComponentForFirstInput().getPosition(), NotifyMessagePosition.BOTTOM_RIGHT);

        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.stack, stacks[2]);
        generateValidationMessagesWithWait();
        Assert.assertFalse(getPage().getMessageComponentForFirstInput().isVisible());
        Assert.assertFalse(getPage().getMessageComponentForSecondInput().isVisible());
        Assert.assertFalse(getPage().getMessageComponentForSelectableInput().isVisible());
    }

    @Test
    public void testStaytime() {
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.stayTime, 1000);
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.sticky, Boolean.FALSE);
        generateValidationMessagesWithWait();
        waiting(3000);
        Assert.assertEquals(getPage().getGlobalNotify().size(), 0, "There should be no message anymore.");
    }

    @Test
    public void testSticky() {
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.stayTime, 1000);
        AttributeList.notifyMessageAttributes.set(NotifyMessageAttributes.sticky, Boolean.TRUE);
        generateValidationMessagesWithWait();
        waiting(3000);
        Assert.assertTrue(getPage().getGlobalNotify().size() > 0, "There should be some messages.");
    }

    @Test(groups = "Future")
    public void testStyle() {
        checkStyle();
    }

    @Test
    public void testStyleClass() {
        checkStyleClass();
    }

    @Test(groups = "Future")
    public void testTitle() {
        checkTitle();
    }

    @Override
    protected void waitingForValidationMessagesToShow() {
        submitWithA4jBtn();
    }
}
