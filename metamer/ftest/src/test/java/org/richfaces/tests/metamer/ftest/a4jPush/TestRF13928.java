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
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.bean.issues.RF13928;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13928 extends AbstractWebDriverTest {

    private static final String DATA_TEMPLATE = "received data #{0}";
    private static final int NUMBER_OF_TESTED_UPDATES = 3;
    private static final String SUBSCRIPTION_TEMPLATE = "subscribed #{0} times";
    private static final int UPDATE_INTERVAL_SECONDS = 5;

    private Attributes<PushAttributes> attributes = getAttributes();

    @FindBy(css = "span[id$=data]")
    private WebElement dataOutput;
    @FindBy(css = "span[id$=subscription]")
    private WebElement subscriptionOutput;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jPush/rf-13928.xhtml");
    }

    private FluentWait<WebDriver, Void> getWait() {
        return Graphene.waitModel().withTimeout(UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-13928")
    public void testPushTriggersAjaxListener() {
        assertEquals(dataOutput.getText(), RF13928.DATA_WAITING);
        assertEquals(subscriptionOutput.getText(), RF13928.SUBSCRIPTION_WAITING);

        String previousDataOutputText = dataOutput.getText();

        // enable push
        attributes.set(PushAttributes.rendered, Boolean.TRUE);
        for (int i = 1; i <= NUMBER_OF_TESTED_UPDATES; ++i) {
            getWait().until().element(dataOutput).text().not().equalTo(previousDataOutputText);
            previousDataOutputText = dataOutput.getText();
            assertEquals(previousDataOutputText, MessageFormat.format(DATA_TEMPLATE, i));
            assertEquals(subscriptionOutput.getText(), MessageFormat.format(SUBSCRIPTION_TEMPLATE, 1), "Only 1 subscription expected.");
        }

        // test second subscription
        // disable push
        attributes.set(PushAttributes.rendered, Boolean.FALSE);
        previousDataOutputText = dataOutput.getText();
        // enable push
        attributes.set(PushAttributes.rendered, Boolean.TRUE);
        for (int i = NUMBER_OF_TESTED_UPDATES + 1; i <= 2 * NUMBER_OF_TESTED_UPDATES; ++i) {
            getWait().until().element(dataOutput).text().not().equalTo(previousDataOutputText);
            previousDataOutputText = dataOutput.getText();
            assertEquals(previousDataOutputText, MessageFormat.format(DATA_TEMPLATE, i));
            assertEquals(subscriptionOutput.getText(), MessageFormat.format(SUBSCRIPTION_TEMPLATE, 2), "2 subscriptions expected.");
        }
    }
}
