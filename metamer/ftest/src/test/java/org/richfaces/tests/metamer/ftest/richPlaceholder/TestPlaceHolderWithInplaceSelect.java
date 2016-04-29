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
package org.richfaces.tests.metamer.ftest.richPlaceholder;

import static org.richfaces.tests.metamer.ftest.richPlaceholder.AbstractPlaceholderJSFTest.DEFAULT_PLACEHOLDER_TEXT;
import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.inplaceSelect.RichFacesInplaceSelect;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPlaceHolderWithInplaceSelect extends AbstractPlaceholderJSFTest {

    @FindBy(css = INPUT1_ID)
    private RichFacesInplaceSelect is1;
    @FindBy(css = INPUT2_ID)
    private RichFacesInplaceSelect is2;
    @FindBy(css = "span" + INPUT1_ID)
    private WebElement spanElementWithStyleClass;

    public TestPlaceHolderWithInplaceSelect() {
        super("inplaceSelect");
    }

    @Override
    protected void focusOnInput1() {
        is1.advanced().switchToEditingState();
    }

    @Override
    protected void focusOnInput2() {
        is2.advanced().switchToEditingState();
    }

    @Override
    WebElement getInput1() {
        return is1.advanced().getEditInputElement();
    }

    @Override
    protected String getInput1EditValue() {
        return getInput1().getAttribute("value");
    }

    @Override
    protected String getInput1StyleClass() {
        return is1.advanced().getLabelInputElement().getAttribute("class");
    }

    @Override
    protected String getInput1Value() {
        return is1.advanced().getLabelInputElement().getText().trim();
    }

    @Override
    WebElement getInput2() {
        return is2.advanced().getEditInputElement();
    }

    @Override
    protected String getInput2StyleClass() {
        return is2.advanced().getLabelInputElement().getAttribute("class");
    }

    @Override
    protected String getInput2Value() {
        return is2.advanced().getLabelInputElement().getText().trim();
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-12625", "https://issues.jboss.org/browse/RF-12623" })
    @Override
    public void testAjaxSubmit() {
        super.testAjaxSubmit();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testClickOnInputWithPlaceholder() {
        super.testClickOnInputWithPlaceholder();
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12651")
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testDeleteTextFromInputWithPlaceholder() {
        super.testDeleteTextFromInputWithPlaceholder();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testHTTPSubmit() {
        super.testHTTPSubmit();
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12650")
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    @Templates(value = "plain")
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-12623", "https://issues.jboss.org/browse/RF-12651",
        "https://issues.jboss.org/browse/RF-13783" })
    @Templates(value = "plain")
    @Override
    public void testStyleClass() {
        assertEquals(getInput1Value(), DEFAULT_PLACEHOLDER_TEXT, "Input 1 value");
        testStyleClass(spanElementWithStyleClass);
    }

    @Test
    @Skip
    @IssueTracking("https://issues.jboss.org/browse/RF-12651")
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testTypeToInputWithPlaceholder() {
        super.testTypeToInputWithPlaceholder();
    }
}
