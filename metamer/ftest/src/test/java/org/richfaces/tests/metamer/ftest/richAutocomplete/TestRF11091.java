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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF11091 extends AbstractWebDriverTest {

    @FindBy(css = ".rf-au .rf-au-inp")
    private WebElement autocompleteInput;
    @FindBy(css = "input[id$=':hInputText']")
    private WebElement hInputText;

    @Override
    public String getComponentTestPagePath() {
        return "richAutocomplete/rf-11091.xhtml";
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11091")
    public void testAutocompleteIsAlignedVerticallyAsHInputText() {
        final int tolerance = 3;// px
        // check the top left location of inputs
        assertEquals(hInputText.getLocation().y, autocompleteInput.getLocation().y, tolerance);
    }
}
