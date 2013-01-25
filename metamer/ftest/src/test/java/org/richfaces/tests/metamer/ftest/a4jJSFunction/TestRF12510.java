/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jJSFunction;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestRF12510 extends AbstractWebDriverTest {

    @Page
    private RF12510Page page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jJSFunction/rf-12510.xhtml");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12510")
    public void testTriggerJSFunctionAndSeeHowManyTimesCalledMethodOnData() {
        int beforeTrigger = page.parseCounterToInt();
        page.triggerJSFunction();

        int afterTrigger = page.parseCounterToInt();
        assertEquals(afterTrigger, beforeTrigger + 1,
            "Counter which counts number of invocations of jsFunction method on attribute data was called more than once!");
    }

    public class RF12510Page extends MetamerPage {

        @FindBy(css = "span[id$=showcounter]")
        private WebElement counter;

        @FindBy(id = "trigger")
        private WebElement jsFunctionTrigger;

        public void triggerJSFunction() {
            jsFunctionTrigger.click();
        }

        public int parseCounterToInt() {
            return Integer.parseInt(counter.getText());
        }

        public WebElement getCounter() {
            return counter;
        }

        public void setCounter(WebElement counter) {
            this.counter = counter;
        }

        public WebElement getJsFunctionTrigger() {
            return jsFunctionTrigger;
        }

        public void setJsFunctionTrigger(WebElement jsFunctionTrigger) {
            this.jsFunctionTrigger = jsFunctionTrigger;
        }
    }
}
