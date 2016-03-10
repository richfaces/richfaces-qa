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
package org.richfaces.tests.metamer.ftest.richInputNumberSpinner;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inputNumberSpinner.RichFacesInputNumberSpinner;
import org.richfaces.tests.metamer.ftest.abstractions.validator.AbstractInputComponentValidatorTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richSelect/validator.xhtml.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSpinnerValidator extends AbstractInputComponentValidatorTest {

    private static final Integer NOT_VALID = 15;
    private static final Integer VALID = 5;

    @FindBy(css = "[id$=component]")
    private RichFacesInputNumberSpinner spinner;

    @Override
    public String getComponentTestPagePath() {
        return "richInputNumberSpinner/validator.xhtml";
    }

    @Override
    protected void setCorrectValue() {
        spinner.setValue(VALID);
        blur(WaitRequestType.XHR);
    }

    @Override
    protected void setIncorrectValue() {
        spinner.setValue(NOT_VALID);
        blur(WaitRequestType.XHR);
    }

    @Test
    public void testValidator() {
        super.testValidator();
    }
}
