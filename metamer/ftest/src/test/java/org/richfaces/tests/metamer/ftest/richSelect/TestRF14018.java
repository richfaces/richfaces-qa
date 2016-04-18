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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/autocompleteMethod.xhtml.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14018 extends AbstractWebDriverTest {

    private final Attributes<SelectAttributes> attributes = getAttributes();

    @FindBy(css = "div[id$=select]")
    private RichFacesSelect select;

    @Override
    public String getComponentTestPagePath() {
        return "richSelect/autocompleteMethod.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14018")
    public void testTypeUnknownWillThrowJSException() {
        attributes.set(SelectAttributes.selectFirst, true);

        List<String> errorMessages = jsErrorStorage.getMessages();
        assertTrue(errorMessages.isEmpty());

        // this leads to javascript error
        select.advanced().getInput().sendKeys("z");

        // wait and check if there was no javascript error
        waiting(1000);
        errorMessages = jsErrorStorage.getMessages();
        assertTrue(errorMessages.isEmpty());
    }
}
