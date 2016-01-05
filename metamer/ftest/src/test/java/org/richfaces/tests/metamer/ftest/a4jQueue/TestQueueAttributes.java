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
package org.richfaces.tests.metamer.ftest.a4jQueue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestQueueAttributes extends AbstractWebDriverTest {

    @FindBy(css = "[id$=actionButton]")
    private WebElement actionButton;
    @FindBy(css = "[id$=errorButton]")
    private WebElement errorButton;

    private final Attributes<QueueAttributes> attributes = getAttributes();

    private final Action guardedClickActionButtonAction = new Action() {
        @Override
        public void perform() {
            Graphene.guardAjax(actionButton).click();
        }
    };
    private final Action guardedClickErrorButtonAction = new Action() {
        @Override
        public void perform() {
            Graphene.guardAjax(errorButton).click();
        }
    };

    @Override
    public String getComponentTestPagePath() {
        return "a4jQueue/globalQueue.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11805")
    @CoversAttributes("onerror")
    public void testOnerror() {
        attributes.set(QueueAttributes.requestDelay, 0);
        testFireEvent("onerror", guardedClickErrorButtonAction);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11711")
    @CoversAttributes("onbeforedomupdate")
    public void testOnbeforedomupdate() {
        attributes.set(QueueAttributes.requestDelay, 0);
        testFireEvent("onbeforedomupdate", guardedClickActionButtonAction);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11711")
    @CoversAttributes("oncomplete")
    public void testOncomplete() {
        attributes.set(QueueAttributes.requestDelay, 0);
        testFireEvent("oncomplete", guardedClickActionButtonAction);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11711")
    @CoversAttributes("onsubmit")
    public void testOnsubmit() {
        attributes.set(QueueAttributes.requestDelay, 0);
        testFireEvent("onsubmit", guardedClickActionButtonAction);
    }

    @Test
    @CoversAttributes("onrequestdequeue")
    public void testOnrequestdequeue() {
        attributes.set(QueueAttributes.requestDelay, 0);
        testFireEvent("onrequestdequeue", guardedClickActionButtonAction);
    }

    @Test
    @CoversAttributes(value = "onrequestqueue")
    public void testOnrequestqueue() {
        attributes.set(QueueAttributes.requestDelay, 0);
        testFireEvent("onrequestqueue", guardedClickActionButtonAction);
    }

    @Test
    @CoversAttributes(value = "status")
    @RegressionTest(value = "https://issues.jboss.org/browse/RF-13203")
    public void testStatus() {
        attributes.set(QueueAttributes.requestDelay, 0);
        testStatus(new Actions(driver).click(actionButton).build());
    }
}
