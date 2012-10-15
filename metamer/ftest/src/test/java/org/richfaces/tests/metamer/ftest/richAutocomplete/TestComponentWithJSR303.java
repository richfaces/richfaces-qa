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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import static org.testng.Assert.assertFalse;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class TestComponentWithJSR303 extends AbstractWebDriverTest<JSR303Page> {

    @Inject
    @Use(strings={"hCommandButton", "a4jCommandButton"})
    String button;

    @Override
    protected JSR303Page createPage() {
        return new JSR303Page();
    }

    protected void verifyNotEmpty() {
        getPage().setAllCorrectly(false);
        getPage().setNotEmptyInputWrongly(false);
        submit();
        getPage().waitForNotEmptyInputMessage(getWait());

        assertFalse(getPage().isCustomStringInputMessageVisible());
        assertFalse(getPage().isRegExpPatternInputMessageVisible());
        assertFalse(getPage().isStringSizeInputMessageVisible());

        getPage().setNotEmptyInputCorrectly(false);
        submit();
        getPage().waitForNotEmptyInputWithoutMessage(getWait());

        assertFalse(getPage().isCustomStringInputMessageVisible());
        assertFalse(getPage().isRegExpPatternInputMessageVisible());
        assertFalse(getPage().isStringSizeInputMessageVisible());
    }

    protected void verifyRegExpPattern() {
        getPage().setAllCorrectly(false);
        getPage().setRegExpPatternInputWrongly(false);
        submit();
        getPage().waitForRegExpPatternInputMessage(getWait());

        assertFalse(getPage().isCustomStringInputMessageVisible());
        assertFalse(getPage().isNotEmptyInputMessageVisible());
        assertFalse(getPage().isStringSizeInputMessageVisible());

        getPage().setRegExpPatternInputCorrectly(false);
        submit();
        getPage().waitForRegExpPatternInputWithoutMessage(getWait());

        assertFalse(getPage().isCustomStringInputMessageVisible());
        assertFalse(getPage().isNotEmptyInputMessageVisible());
        assertFalse(getPage().isStringSizeInputMessageVisible());
    }

    protected void verifyStringSize() {
        getPage().setAllCorrectly(false);
        getPage().setStringSizeInputWrongly(false);
        submit();
        getPage().waitForStringSizeInputMessage(getWait());

        assertFalse(getPage().isCustomStringInputMessageVisible());
        assertFalse(getPage().isNotEmptyInputMessageVisible());
        assertFalse(getPage().isRegExpPatternInputMessageVisible());

        getPage().setStringSizeInputCorrectly(false);
        submit();
        getPage().waitForStringSizeInputWithoutMessage(getWait());

        assertFalse(getPage().isCustomStringInputMessageVisible());
        assertFalse(getPage().isNotEmptyInputMessageVisible());
        assertFalse(getPage().isRegExpPatternInputMessageVisible());
    }

    protected void verifyCustomString() {
        getPage().setAllCorrectly(false);
        getPage().setCustomStringInputWrongly(false);
        submit();
        getPage().waitForCustomStringInputMessage(getWait());

        assertFalse(getPage().isNotEmptyInputMessageVisible());
        assertFalse(getPage().isRegExpPatternInputMessageVisible());
        assertFalse(getPage().isStringSizeInputMessageVisible());

        getPage().setCustomStringInputCorrectly(false);
        submit();
        getPage().waitForCustomStringInputWithoutMessage(getWait());

        assertFalse(getPage().isNotEmptyInputMessageVisible());
        assertFalse(getPage().isRegExpPatternInputMessageVisible());
        assertFalse(getPage().isStringSizeInputMessageVisible());
    }

    protected void verifyAllInputs() {
        getPage().setAllWrongly(false);
        submit();

        getPage().waitForCustomStringInputMessage(getWait());
        getPage().waitForNotEmptyInputMessage(getWait());
        getPage().waitForRegExpPatternInputMessage(getWait());
        getPage().waitForStringSizeInputMessage(getWait());

        getPage().setAllCorrectly(true);
        submit();

        getPage().waitForCustomStringInputWithoutMessage(getWait());
        getPage().waitForNotEmptyInputWithoutMessage(getWait());
        getPage().waitForRegExpPatternInputWithoutMessage(getWait());
        getPage().waitForStringSizeInputWithoutMessage(getWait());
    }

    protected final void submit() {
        if (button.equals("hCommandButton")) {
            getPage().getHCommandButton().click();
        } else {
            getPage().getA4jCommandButton().click();
        }
    }

    protected final WebDriverWait getWait() {
        if (button.equals("hCommandButton")) {
            return Graphene.waitModel();
        } else {
            return Graphene.waitAjax();
        }
    }

}
