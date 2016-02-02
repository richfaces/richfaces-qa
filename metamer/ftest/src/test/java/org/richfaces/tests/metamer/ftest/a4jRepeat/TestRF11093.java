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
package org.richfaces.tests.metamer.ftest.a4jRepeat;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF11093 extends AbstractWebDriverTest {

    private void assertPhasesContainsMessage(String msg) {
        for (String m : getMetamerPage().getPhases()) {
            if (m.contains(msg)) {
                return;
            }
        }
        throw new AssertionError(format("Message <{0}> not found in phases.", msg));
    }

    @Override
    public String getComponentTestPagePath() {
        return "a4jRepeat/rf-11093.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11093")
    public void testVariableIsAvailableInContext() {
        final List<String> values = Lists.newArrayList("one", "two", "three", "four");

        List<WebElement> links = driver.findElements(By.cssSelector("a[id$=link]"));
        assertEquals(links.size(), 4);
        for (int i : new int[] { 1, 3, 0, 2 }) {
            Graphene.guardAjax(links.get(i)).click();
            assertPhasesContainsMessage("item: " + values.get(i));
        }
    }
}
