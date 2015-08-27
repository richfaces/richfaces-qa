/*
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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.select.RichFacesSelect;
import org.richfaces.fragment.select.SelectSuggestions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF14129 extends AbstractWebDriverTest {

    @FindBy(css = "[id$=select]")
    private RichFacesSelect select;

    @Override
    public String getComponentTestPagePath() {
        return "richSelect/RF-14129.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-14129")
    public void testSuggestionsWillShowUpWhenFirstOptionIsEmpty() {
        // this was fixed
        SelectSuggestions openedSelect = select.openSelect();

        // check selecting various options to verify the component is working correctly
        openedSelect.select(1);
        assertEquals(select.advanced().getInput().getStringValue(), "Option 1");

        waiting(200);// stabilization wait time
        select.openSelect().select(0);
        assertEquals(select.advanced().getInput().getStringValue(), "");

        waiting(200);// stabilization wait time
        select.openSelect().select(3);
        assertEquals(select.advanced().getInput().getStringValue(), "Option 3");
    }

}
