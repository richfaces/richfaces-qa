/**
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
package org.richfaces.tests.metamer.ftest.a4jPush;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.text.MessageFormat;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.issues.RF13929;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13929 extends AbstractWebDriverTest {

    private static final String TEMPLATE_OUTPUT_NO_SUBTOPIC = "data from topic #{0}";
    private static final String TEMPLATE_OUTPUT_SUBTOPIC1 = "data from subtopic 1 #{0}";
    private static final String TEMPLATE_OUTPUT_SUBTOPIC2 = "data from subtopic 2 #{0}";

    @FindBy(css = "[id$=output1]")
    private WebElement output1;
    @FindBy(css = "[id$=output2]")
    private WebElement output2;
    @FindBy(css = "[id$=output3]")
    private WebElement output3;
    @FindBy(css = "[id$=performPushEvent1]")
    private WebElement pushWithSubtopic1;
    @FindBy(css = "[id$=performPushEvent2]")
    private WebElement pushWithSubtopic2;
    @FindBy(css = "[id$=performPushEvent3]")
    private WebElement pushWithoutSubtopic;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jPush/rf-13929.xhtml");
    }

    private void performPush(WebElement push, PushNotificationsNumberStorage storage) {
        String expectedOutput1;
        String expectedOutput2;
        String expectedOutput3;
        if (push == pushWithSubtopic1) {
            storage.incrementPushWithSubtopic1Msgs();
            expectedOutput1 = expectedOutput3 = MessageFormat.format(TEMPLATE_OUTPUT_SUBTOPIC1, storage.getPushWithSubtopic1Msgs());
            expectedOutput2 = MessageFormat.format(TEMPLATE_OUTPUT_SUBTOPIC2, storage.getPushWithSubtopic2Msgs());
        } else if (push == pushWithSubtopic2) {
            storage.incrementPushWithSubtopic2Msgs();
            expectedOutput1 = MessageFormat.format(TEMPLATE_OUTPUT_SUBTOPIC1, storage.getPushWithSubtopic1Msgs());
            expectedOutput2 = expectedOutput3 = MessageFormat.format(TEMPLATE_OUTPUT_SUBTOPIC2, storage.getPushWithSubtopic2Msgs());
        } else {
            storage.incrementPushWithoutSubtopicMsgs();
            expectedOutput1 = MessageFormat.format(TEMPLATE_OUTPUT_SUBTOPIC1, storage.getPushWithSubtopic1Msgs());
            expectedOutput2 = MessageFormat.format(TEMPLATE_OUTPUT_SUBTOPIC2, storage.getPushWithSubtopic2Msgs());
            expectedOutput3 = MessageFormat.format(TEMPLATE_OUTPUT_NO_SUBTOPIC, storage.getPushWithoutSubtopicMsgs());
        }
        push.click();
        if (storage.getPushWithSubtopic1Msgs() < 1) {
            expectedOutput1 = RF13929.DEFAULT_DATA;
        }
        if (storage.getPushWithSubtopic2Msgs() < 1) {
            expectedOutput2 = RF13929.DEFAULT_DATA;
        }
        if (storage.getPushWithoutSubtopicMsgs() < 1 && storage.getPushWithSubtopic1Msgs() < 1 && storage.getPushWithSubtopic2Msgs() < 1) {
            expectedOutput3 = RF13929.DEFAULT_DATA;
        }
        Graphene.waitAjax().until().element(output1).text().equalTo(expectedOutput1);
        Graphene.waitAjax().until().element(output2).text().equalTo(expectedOutput2);
        Graphene.waitAjax().until().element(output3).text().equalTo(expectedOutput3);
    }

    private void performPushWithSubtopic1(PushNotificationsNumberStorage storage) {
        performPush(pushWithSubtopic1, storage);
    }

    private void performPushWithSubtopic2(PushNotificationsNumberStorage storage) {
        performPush(pushWithSubtopic2, storage);
    }

    private void performPushWithoutSubtopic(PushNotificationsNumberStorage storage) {
        performPush(pushWithoutSubtopic, storage);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13929")
    public void testPushesWithSubtopics() {
        PushNotificationsNumberStorage storage = new PushNotificationsNumberStorage();
        performPushWithSubtopic1(storage);
        performPushWithSubtopic1(storage);
        performPushWithSubtopic2(storage);
        performPushWithSubtopic1(storage);
        performPushWithoutSubtopic(storage);
        performPushWithoutSubtopic(storage);
        performPushWithoutSubtopic(storage);
        performPushWithoutSubtopic(storage);
        performPushWithSubtopic2(storage);
        performPushWithSubtopic2(storage);
        performPushWithoutSubtopic(storage);
        performPushWithSubtopic1(storage);
    }

    private class PushNotificationsNumberStorage {

        private int pushWithSubtopic1Msgs;
        private int pushWithSubtopic2Msgs;
        private int pushWithoutSubtopicMsgs;

        public int getPushWithSubtopic1Msgs() {
            return pushWithSubtopic1Msgs;
        }

        public int getPushWithSubtopic2Msgs() {
            return pushWithSubtopic2Msgs;
        }

        public int getPushWithoutSubtopicMsgs() {
            return pushWithoutSubtopicMsgs;
        }

        public void incrementPushWithSubtopic1Msgs() {
            this.pushWithSubtopic1Msgs++;
        }

        public void incrementPushWithSubtopic2Msgs() {
            this.pushWithSubtopic2Msgs++;
        }

        public void incrementPushWithoutSubtopicMsgs() {
            this.pushWithoutSubtopicMsgs++;
        }
    }
}
