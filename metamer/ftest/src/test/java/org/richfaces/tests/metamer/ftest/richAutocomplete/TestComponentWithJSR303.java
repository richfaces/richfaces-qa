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
//FIXME TestComponentWithJSR303 shoud not be generic (Graphene bug)
public abstract class TestComponentWithJSR303<P extends JSR303Page> extends AbstractWebDriverTest<P> {

    @Inject
    @Use(strings={"hCommandButton", "a4jCommandButton"})
    String button;

    protected void verifyNotEmpty() {
        page.setAllCorrectly(false);
        page.setNotEmptyInputWrongly(false);
        submit();
        page.waitForNotEmptyInputMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());

        page.setNotEmptyInputCorrectly(false);
        submit();
        page.waitForNotEmptyInputWithoutMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());
    }

    protected void verifyRegExpPattern() {
        page.setAllCorrectly(false);
        page.setRegExpPatternInputWrongly(false);
        submit();
        page.waitForRegExpPatternInputMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());

        page.setRegExpPatternInputCorrectly(false);
        submit();
        page.waitForRegExpPatternInputWithoutMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());
    }

    protected void verifyStringSize() {
        page.setAllCorrectly(false);
        page.setStringSizeInputWrongly(false);
        submit();
        page.waitForStringSizeInputMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());

        page.setStringSizeInputCorrectly(false);
        submit();
        page.waitForStringSizeInputWithoutMessage(getWait());

        assertFalse(page.isCustomStringInputMessageVisible());
        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
    }

    protected void verifyCustomString() {
        page.setAllCorrectly(false);
        page.setCustomStringInputWrongly(false);
        submit();
        page.waitForCustomStringInputMessage(getWait());

        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());

        page.setCustomStringInputCorrectly(false);
        submit();
        page.waitForCustomStringInputWithoutMessage(getWait());

        assertFalse(page.isNotEmptyInputMessageVisible());
        assertFalse(page.isRegExpPatternInputMessageVisible());
        assertFalse(page.isStringSizeInputMessageVisible());
    }

    protected void verifyAllInputs() {
        page.setAllWrongly(false);
        submit();

        page.waitForCustomStringInputMessage(getWait());
        page.waitForNotEmptyInputMessage(getWait());
        page.waitForRegExpPatternInputMessage(getWait());
        page.waitForStringSizeInputMessage(getWait());

        page.setAllCorrectly(true);
        submit();

        page.waitForCustomStringInputWithoutMessage(getWait());
        page.waitForNotEmptyInputWithoutMessage(getWait());
        page.waitForRegExpPatternInputWithoutMessage(getWait());
        page.waitForStringSizeInputWithoutMessage(getWait());
    }

    protected final void submit() {
        if (button.equals("hCommandButton")) {
            page.getHCommandButton().click();
        } else {
            page.getA4jCommandButton().click();
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
