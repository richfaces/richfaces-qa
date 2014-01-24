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

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;

/**
 * Abstract base for testing all kinds of JSR303 and CSV tests for input components.
 * All such tests has to have:
 * # Pages: page named "csv" and "jsr303", page needs to implement an *InputComponentValidationTemplate.xhtml
 * # Test: test class name must end with 'JSR303' or 'CSV' and must extend some of Abstract*InputComponentValidationTest.
 *
 * All tests will be run with all submit methods (ajax, http, none [CSV only]).
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractInputComponentValidationTest extends AbstractWebDriverTest {

    //Test types
    private static final String CSV_TEST = "CSV";
    private static final String JSR303_TEST = "JSR303";
    private static final String[] TEST_KINDS = { CSV_TEST, JSR303_TEST };
    //Submit types:
    public static final String A4J_COMMANDBUTTON = "a4jCommandButton";
    public static final String H_COMMANDBUTTON = "hCommandButton";
    public static final String CSV = "csv";
    static final String[] CSV_SUBMIT_METHODS = { A4J_COMMANDBUTTON, H_COMMANDBUTTON, CSV };
    static final String[] JSR303_SUBMIT_METHODS = { A4J_COMMANDBUTTON, H_COMMANDBUTTON };
    @Inject
    @Use(value = "submitMethods")
    protected String submitMethod;
    protected final String[] submitMethods;
    //Cases
    @Inject
    @Use(empty = true)
    protected String commonCase;
    protected String[] commonCases;
    //
    private WebDriverWait wait;
    private Map<String, ValidationMessageCase> vmcs = new HashMap<String, ValidationMessageCase>();

    public AbstractInputComponentValidationTest() {
        String kindOfTest = kindOfTest();
        if (CSV_TEST.equals(kindOfTest)) {
            submitMethods = CSV_SUBMIT_METHODS;
        } else if (JSR303_TEST.equals(kindOfTest)) {
            submitMethods = JSR303_SUBMIT_METHODS;
        } else {
            throw new RuntimeException("Cannot set submit methods.");
        }
    }

    public abstract String getComponentName();

    protected abstract InputValidationPage getPage();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + getComponentName() + "/" + kindOfTest().toLowerCase(Locale.ENGLISH) + ".xhtml");
    }

    /**
     * Returns wait method for actual submit method.
     */
    protected WebDriverWait getWait() {
        if (submitMethod.equals(H_COMMANDBUTTON)) {
            return Graphene.waitModel();
        } else if (submitMethod.equals(A4J_COMMANDBUTTON)) {
            return Graphene.waitAjax();
        } else {
            return Graphene.waitGui();
        }
    }

    @BeforeMethod(groups = "smoke")
    public void init() {
        getPage().initCustomMessages();
        wait = getWait();
        initValidationMessageCases();
    }

    private void initValidationMessageCases() {
        if (vmcs.isEmpty()) {
            vmcs.putAll(getPage().getMessageCases());
        }
    }

    private String kindOfTest() {
        String simpleName = getClass().getSimpleName();
        for (String testKind : TEST_KINDS) {
            if (simpleName.endsWith(testKind)) {
                return testKind;
            }
        }
        throw new RuntimeException("Test name does not end with any of " + Arrays.asList(TEST_KINDS));
    }

    /**
     * Submits the page with correct button ajax/http or no submit with CSV.
     */
    public final void submit() {
        getPage().submit(submitMethod);
    }

    public final void verifyCases() {
        verify(vmcs.get(commonCase));
    }

    /**
     * Verifies, for given case, that default output will not change and validation
     * message, that will appear, is correct and visible if wrong value is set
     * by button. Also verifies, that correct output will be set and message will
     * hide after correct value is set by button. During testing this case also
     * verifies that other outputs will not change and no other messages will appear.
     * @param vmc
     */
    private void verify(ValidationMessageCase vmc) {
        if (vmc == null) {
            verifyAllInputs();
        } else {
            vmc.setWrong();
            submit();
            vmc.waitForMessageShow(wait);
            vmc.assertMessageDetailIsCorrect();
            getPage().assertNoOtherMessagesAreVisible(vmc);
            getPage().assertOtherOutputsAreDefault(null);//all outputs should be default

            vmc.setCorrect();
            submit();
            vmc.waitForMessageHide(wait);
            getPage().assertNoOtherMessagesAreVisible(vmc);
            vmc.assertValidOutput();
            getPage().assertOtherOutputsAreDefault(vmc);
        }
    }

    /**
     * Verifies for all input on the page, that default output will not change
     * and validation message, that will appear, is correct and visible if wrong
     * value is set by button. Also verifies, that correct output will be set
     * and message will hide after correct value is set by button.
     * @param vmc
     */
    private void verifyAllInputs() {
        getPage().setAllWrongly();
        submit();
        getPage().assertAllMessagesAreVisibleAndCorrect();
        getPage().assertOtherOutputsAreDefault(null);//all outputs should be default

        getPage().setAllCorrectly();
        submit();
        getPage().assertNoOtherMessagesAreVisible(null);//all messages should be hidden
        getPage().assertAllOutputsAreValid();
    }
}
