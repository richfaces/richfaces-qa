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
package org.richfaces.tests.metamer.ftest.a4jJSFunction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF12510 extends AbstractWebDriverTest {

    @FindBy(css = "span[id$=counter]")
    private WebElement counter;
    @FindBy(id = "trigger")
    private WebElement jsFunctionTrigger;
    @FindBy(css = "span[id$=oncompleteCounter]")
    private WebElement oncompleteCounter;

    @Override
    public String getComponentTestPagePath() {
        return "a4jJSFunction/rf-12510.xhtml";
    }

    private int parseCounterToInt(WebElement counter) {
        return Integer.parseInt(counter.getText());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12510")
    public void testTriggerJSFunctionAndSeeHowManyTimesCalledMethodOnData() {
        Graphene.guardAjax(jsFunctionTrigger).click();

        int afterTrigger1 = parseCounterToInt(counter);
        int afterTrigger2 = parseCounterToInt(oncompleteCounter);

        // this was fixed
        assertEquals(afterTrigger1, afterTrigger2, "Both counters should have the same data.");
        // used case is not valid and getter in bean contain business logic, the data could increase more than one time.
        assertTrue(afterTrigger1 >= 1, "The counter should increase at least by one.");
    }
}
