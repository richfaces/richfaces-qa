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
package org.richfaces.tests.metamer.ftest.abstractions.validations;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage.WaitRequestType;
import org.testng.collections.Lists;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class InputValidationPage {

    public static final String MESSAGE_ALL_INPUTS_NAME = "allInputs";
    @FindBy(css = "input[id$=hButton]")
    private WebElement hCommandButton;
    @FindBy(css = "input[id$=a4jButton]")
    private WebElement a4jCommandButton;
    //
    protected final Map<String, ValidationMessageCase> messageCases = new HashMap<String, ValidationMessageCase>();

    public final void assertAllMessagesAreVisibleAndCorrect() {
        for (ValidationMessageCase vmc : getMessageCases().values()) {
            if (vmc != null) {
                vmc.assertMessageIsDisplayed();
                vmc.assertMessageDetailIsCorrect();
            }
        }
    }

    public final void assertAllOutputsAreValid() {
        for (ValidationMessageCase vmc : getMessageCases().values()) {
            if (vmc != null) {
                vmc.assertValidOutput();
            }
        }
    }

    public final void assertNoOtherMessagesAreVisible(ValidationMessageCase vm) {
        Map<String, ValidationMessageCase> map = Maps.newHashMap(getMessageCases());
        if (vm != null) {
            map.remove(vm.getName());
        }
        for (ValidationMessageCase vmc : map.values()) {
            if (vmc != null) {
                vmc.assertMessageIsHidden();
            }
        }
    }

    public final void assertOtherOutputsAreDefault(ValidationMessageCase testeVMC) {
        Map<String, ValidationMessageCase> map = Maps.newHashMap(getMessageCases());
        if (testeVMC != null) {
            map.remove(testeVMC.getName());
        }
        for (ValidationMessageCase vmc : map.values()) {
            if (vmc != null) {
                vmc.assertDefaultOutput();
            }
        }
    }

    public final WebElement getA4jCommandButton() {
        return a4jCommandButton;
    }

    private List<WebElement> getAllCorrectButtons() {
        List<WebElement> result = Lists.newArrayList();
        for (ValidationMessageCase vmc : getMessageCases().values()) {
            result.add(vmc.getButtonCorrect());
        }
        return result;
    }

    private List<WebElement> getAllWrongButtons() {
        List<WebElement> result = Lists.newArrayList();
        for (ValidationMessageCase vmc : getMessageCases().values()) {
            result.add(vmc.getButtonWrong());
        }
        return result;
    }

    public final WebElement getHCommandButton() {
        return hCommandButton;
    }

    public final Map<String, ValidationMessageCase> getMessageCases() {
        if (messageCases.isEmpty()) {
            messageCases.put(MESSAGE_ALL_INPUTS_NAME, null);
            initCustomMessages();
        }
        return messageCases;
    }

    protected abstract void initCustomMessages();

    public final void setAllCorrectly() {
        for (WebElement btn : getAllCorrectButtons()) {
            MetamerPage.waitRequest(btn, WaitRequestType.XHR).click();
        }
    }

    public final void setAllWrongly() {
        for (WebElement btn : getAllWrongButtons()) {
            MetamerPage.waitRequest(btn, WaitRequestType.XHR).click();
        }
    }

    public final void submit(String submitMethod) {
        if (submitMethod.equals(AbstractInputComponentValidationTest.H_COMMANDBUTTON)) {
            MetamerPage.waitRequest(hCommandButton, WaitRequestType.HTTP).click();
        } else if (submitMethod.equals(AbstractInputComponentValidationTest.A4J_COMMANDBUTTON)) {
            MetamerPage.waitRequest(a4jCommandButton, WaitRequestType.XHR).click();
        }
        //no submit for csv
    }
}
