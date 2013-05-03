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
package org.richfaces.tests.metamer.ftest.richNotify;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.notifyAttributes;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.metamer.bean.rich.RichNotifyBean;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.notify.NotifyMessagePosition;
import org.richfaces.tests.page.fragments.impl.notify.RichFacesNotifyMessage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestNotifyAttributes extends AbstractWebDriverTest {

    @FindBy(className = "rf-ntf")
    private RichFacesNotifyMessage message;
    @FindBy(id = "newSpan")
    private WebElement newSpan;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richNotify/simple.xhtml");
    }

    @Test
    public void testDetail() {
        String detail = "Some detail";
        notifyAttributes.set(NotifyAttributes.detail, detail);
        Assert.assertEquals(message.getDetail(), detail);
        Assert.assertEquals(message.getSummary(), RichNotifyBean.DEFAULT_SUMMARY);
    }

    @Test
    public void testEscape() {
        String newSpanString = "<span id='newSpan'>new span</span>";
        notifyAttributes.set(NotifyAttributes.detail, newSpanString);

        notifyAttributes.set(NotifyAttributes.escape, Boolean.FALSE);
        assertVisible(newSpan, "New created span should be visible");

        notifyAttributes.set(NotifyAttributes.escape, Boolean.TRUE);
        assertNotVisible(newSpan, "New created span should not be visible");
    }

    @Test
    public void testInit() {
        Assert.assertTrue(message.isVisible());
        Assert.assertEquals(message.getDetail(), RichNotifyBean.DEFAULT_DETAIL);
        Assert.assertEquals(message.getSummary(), RichNotifyBean.DEFAULT_SUMMARY);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12925")
    @Test(groups = "Future")
    public void testNoDetailNoSummary() {
        notifyAttributes.set(NotifyAttributes.detail, "");
        notifyAttributes.set(NotifyAttributes.summary, "");
        Assert.assertFalse(message.isVisible());
    }

    @Test
    public void testNonblocking() {
        notifyAttributes.set(NotifyAttributes.nonblocking, Boolean.TRUE);
        notifyAttributes.set(NotifyAttributes.nonblockingOpacity, 0);
        Utils.triggerJQ("mouseover", message.getRoot());
        TestNotifyAttributes.waitForOpacityChange(0, message.getRoot());
        Utils.triggerJQ("mouseout", message.getRoot());
        TestNotifyAttributes.waitForOpacityChange(1, message.getRoot());
    }

    @Test
    public void testNonblockingOpacity() {
        notifyAttributes.set(NotifyAttributes.nonblocking, Boolean.TRUE);
        notifyAttributes.set(NotifyAttributes.nonblockingOpacity, 0.5);
        Utils.triggerJQ("mouseover", message.getRoot());
        TestNotifyAttributes.waitForOpacityChange(0.5, message.getRoot());
        Utils.triggerJQ("mouseout", message.getRoot());
        TestNotifyAttributes.waitForOpacityChange(1, message.getRoot());
    }

    @Test
    public void testOnClick() {
        testFireEvent(Event.CLICK, message.getRoot());
    }

    @Test
    public void testOnDblClick() {
        testFireEvent(Event.DBLCLICK, message.getRoot());
    }

    @Test
    public void testOnKeyDown() {
        testFireEvent(Event.KEYDOWN, message.getRoot());
    }

    @Test
    public void testOnKeyPress() {
        testFireEvent(Event.KEYPRESS, message.getRoot());
    }

    @Test
    public void testOnKeyUp() {
        testFireEvent(Event.KEYUP, message.getRoot());
    }

    @Test
    public void testOnMouseDown() {
        testFireEvent(Event.MOUSEDOWN, message.getRoot());
    }

    @Test
    public void testOnMouseMove() {
        testFireEvent(Event.MOUSEMOVE, message.getRoot());
    }

    @Test
    public void testOnMouseOut() {
        testFireEvent(Event.MOUSEOUT, message.getRoot());
    }

    @Test
    public void testOnMouseOver() {
        testFireEvent(Event.MOUSEOVER, message.getRoot());
    }

    @Test
    public void testOnMouseUp() {
        testFireEvent(Event.MOUSEUP, message.getRoot());
    }

    @Test
    public void testRendered() {
        notifyAttributes.set(NotifyAttributes.rendered, Boolean.TRUE);
        assertVisible(message.getRoot(), "Message should be visible.");
        notifyAttributes.set(NotifyAttributes.rendered, Boolean.FALSE);
        assertNotVisible(message.getRoot(), "Message should not be visible.");
    }

    @Test
    public void testShowCloseButton() {
        notifyAttributes.set(NotifyAttributes.showCloseButton, Boolean.TRUE);
        message.close();
        assertNotVisible(message.getRoot(), "Message should not be visible after close.");

        notifyAttributes.set(NotifyAttributes.showCloseButton, Boolean.FALSE);
        try {
            message.close();
        } catch (TimeoutException ok) {
            return;
        }
        Assert.fail("The notify message should not be closeable if there is no close button.");
    }

    @Test
    public void testShowShadow() {
        notifyAttributes.set(NotifyAttributes.showShadow, Boolean.TRUE);
        assertVisible(message.getShadowElement(), "Shadow should be visible");

        notifyAttributes.set(NotifyAttributes.showShadow, Boolean.FALSE);
        assertNotVisible(message.getShadowElement(), "Shadow should not be visible");
    }

    @Test
    public void testStack() {
        String[] stacks = { "topLeftStack", "bottomRightStack" };
        //default position is top right
        notifyAttributes.set(NotifyAttributes.stack, "");
        Assert.assertEquals(message.getPosition(), NotifyMessagePosition.TOP_RIGHT);

        notifyAttributes.set(NotifyAttributes.stack, stacks[0]);
        Assert.assertEquals(message.getPosition(), NotifyMessagePosition.TOP_LEFT);

        notifyAttributes.set(NotifyAttributes.stack, stacks[1]);
        Assert.assertEquals(message.getPosition(), NotifyMessagePosition.BOTTOM_RIGHT);
    }

    @Test
    public void testStaytime() {
        notifyAttributes.set(NotifyAttributes.stayTime, 1000);
        notifyAttributes.set(NotifyAttributes.sticky, Boolean.FALSE);
        waiting(3000);
        assertNotVisible(message.getRoot(), "Message should not be visible now.");
    }

    @Test
    public void testSticky() {
        notifyAttributes.set(NotifyAttributes.stayTime, 1000);
        notifyAttributes.set(NotifyAttributes.sticky, Boolean.TRUE);
        waiting(3000);
        assertVisible(message.getRoot(), "Message should be visible until closed.");
    }

    @Test
    public void testStyleClass() {
        testStyleClass(message.getRoot());
    }

    @Test
    public void testSummary() {
        String summary = "Some summary";
        notifyAttributes.set(NotifyAttributes.summary, summary);
        Assert.assertEquals(message.getDetail(), RichNotifyBean.DEFAULT_DETAIL);
        Assert.assertEquals(message.getSummary(), summary);
    }

    public static void waitForOpacityChange(final double opacity, final WebElement element) {
        Graphene.waitGui().withTimeout(3, TimeUnit.SECONDS).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver from) {
                return element.getCssValue("opacity").equals((opacity == 0 ? "0" : (opacity == 1 ? "1" : String.valueOf(opacity))));
            }

            @Override
            public String toString() {
                return "opacity change to value '" + opacity + "'. Actual value '" + element.getCssValue("opacity") + "'";
            }
        });
    }
}
