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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractStringInputComponentValidationTest extends AbstractWebDriverTest {

    @Page
    private InputValidationPage page;

    //Submit types:
    public static final String A4J_COMMANDBUTTON = "a4jCommandButton";
    public static final String H_COMMANDBUTTON = "hCommandButton";
    public static final String CSV = "csv";

    protected abstract String getSubmitMethod();

    protected WebDriverWait getWait() {
        if (getSubmitMethod().equals(H_COMMANDBUTTON)) {
            return Graphene.waitModel();
        } else if (getSubmitMethod().equals(A4J_COMMANDBUTTON)) {
            return Graphene.waitAjax();
        } else {
            return Graphene.waitGui();
        }
    }

    @BeforeMethod
    public void setAllCorrectly() {
        page.setAllCorrectly();
    }

    protected void submit() {
        if (getSubmitMethod().equals(H_COMMANDBUTTON)) {
            MetamerPage.waitRequest(page.getHCommandButton(),
                    WaitRequestType.HTTP).click();
        } else if (getSubmitMethod().equals(A4J_COMMANDBUTTON)) {
            MetamerPage.waitRequest(
                    page.getA4jCommandButton(), WaitRequestType.XHR).click();
        }
    }

    protected void verifyAllInputs() {
        page.setAllWrongly();
        submit();
        page.waitForCustomStringInputMessage(getWait());
        page.waitForNotEmptyInputMessage(getWait());
        page.waitForRegExpPatternInputMessage(getWait());
        page.waitForStringSizeInputMessage(getWait());

        setAllCorrectly();
        submit();

        page.waitForCustomStringInputWithoutMessage(getWait());
        page.waitForNotEmptyInputWithoutMessage(getWait());
        page.waitForRegExpPatternInputWithoutMessage(getWait());
        page.waitForStringSizeInputWithoutMessage(getWait());
    }

    protected void verifyCustomString() {
        page.setCustomStringInputWrongly();
        submit();
        page.waitForCustomStringInputMessage(getWait());

        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());

        page.setCustomStringInputCorrectly();
        submit();
        page.waitForCustomStringInputWithoutMessage(getWait());

        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());
    }

    protected void verifyNotEmpty() {
        page.setNotEmptyInputWrongly();
        submit();
        page.waitForNotEmptyInputMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());

        page.setNotEmptyInputCorrectly();
        submit();
        page.waitForNotEmptyInputWithoutMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());
    }

    protected void verifyRegExpPattern() {
        page.setRegExpPatternInputWrongly();
        submit();
        page.waitForRegExpPatternInputMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());

        page.setRegExpPatternInputCorrectly();
        submit();
        page.waitForRegExpPatternInputWithoutMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());
    }

    protected void verifyStringSize() {
        page.setStringSizeInputWrongly();
        submit();
        page.waitForStringSizeInputMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());

        page.setStringSizeInputCorrectly();
        submit();
        page.waitForStringSizeInputWithoutMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
    }
}
