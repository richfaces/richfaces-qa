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
package org.richfaces.tests.metamer.ftest.richInputNumberSpinner;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.abstractions.converter.AbstractConverterTest;
import org.richfaces.tests.page.fragments.impl.common.ClearType;
import org.richfaces.tests.page.fragments.impl.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSpinnerConverter extends AbstractConverterTest {

    @FindBy(css = "[id$=convertableInput]")
    private RichFacesInputNumberSpinner input;
    private static final String VALUE = "10";
    private static final String DEFAULT_OUTPUT = "0";

    @Override
    protected String badValue() {
        return VALUE;
    }

    @Override
    protected String outputForEmptyValue() {
        return DEFAULT_OUTPUT;
    }

    @Override
    public String getComponentName() {
        return "richInputNumberSpinner";
    }

    @Override
    protected void setBadValue() {
        input.getInput().advanced().clear(ClearType.JS).sendKeys(VALUE);
    }

    @Test
    public void testConverter() {
        checkConverter();
    }

    @Test
    public void testConverterMessage() {
        checkConverterMessage();
    }
}
