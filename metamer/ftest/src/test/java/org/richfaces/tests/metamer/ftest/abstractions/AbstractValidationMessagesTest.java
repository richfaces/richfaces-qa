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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractValidationMessagesTest extends AbstractWebDriverTest {

    protected static final String MSG_ATT = "Custom validator error message from attribute of component.";
    private static final String MSG_BEAN = "Custom validator error message from bean.";
    private static final String MSG_BUNDLE_JSF_CUSTOM = "Custom validator error message from bundle (JSF).";
    private static final String MSG_BUNDLE_JSF_DEFAULT = "jsf-inBundle: Validation Error: Specified attribute is not between the expected values of 2 and 9.";
    private static final String MSG_BUNDLE_JSR = "Custom validator error message from bundle (JSR-303).";
    private final String component;
    @Page
    protected ValidationPage page;

    public AbstractValidationMessagesTest(String component) {
        this.component = component;
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/" + component + "/validationMessages.xhtml");
    }

    @BeforeMethod(alwaysRun = true)
    public void resetPage() {
        //inits document JavaScript variable, which will hold state of setting of all inputs
        executeJS(ValidationPage.JS_STATE_VARIABLE + "=''");
    }

    @AfterClass(alwaysRun = true)
    public void setDefault() {
        page.deactivateCustomMessages();
    }

    /**
     * Tests correctness of CSV validation messages using h:commandButton for
     * submitting. Tests messages set in attribute of component, in annotations
     * of bean and in message bundle.
     */
    @CoversAttributes("validatorMessage")
    public void testCSVMessagesJSF() {
        page.setWrongValuesAndSubmitJSF();
        assertTrue(page.getCsvInAttMsgElement().getText().endsWith(MSG_ATT));
        assertTrue(page.getCsvInBeanMsgElement().getText().endsWith(MSG_BEAN));
        assertTrue(page.getCsvInBundleMsgElement().getText().endsWith(MSG_BUNDLE_JSR));
    }

    /**
     * Tests correctness of CSV validation messages using a4j:commandButton for
     * submitting. Tests messages set in attribute of component, in annotations
     * of bean and in message bundle.
     */
    @CoversAttributes("validatorMessage")
    public void testCSVMessagesRF() {
        page.setWrongValuesAndSubmitRF();
        assertTrue(page.getCsvInAttMsgElement().getText().endsWith(MSG_ATT));
        assertTrue(page.getCsvInBeanMsgElement().getText().endsWith(MSG_BEAN));
        assertTrue(page.getCsvInBundleMsgElement().getText().endsWith(MSG_BUNDLE_JSR));
    }

    @CoversAttributes("validatorMessage")
    public void testInit() {
        page.setCorrectValuesAndSubmitJSF();
        assertTrue(page.noErrorMessagesDisplayed(), "No error messages should now be on page.");
        page.setCorrectValuesAndSubmitRF();
        assertTrue(page.noErrorMessagesDisplayed(), "No error messages should now be on page.");
    }

    /**
     * Tests correctness of JSF validation messages using h:commandButton for
     * submitting. Tests messages set in attribute of component and in message
     * bundle.
     */
    @CoversAttributes("validatorMessage")
    public void testJSFMessagesJSF() {
        try {
            page.setWrongValuesAndSubmitJSF();
            assertTrue(page.getJsfInAttMsgElement().getText().endsWith(MSG_ATT));
            assertTrue(page.getJsfInBundleMsgElement().getText().endsWith(MSG_BUNDLE_JSF_DEFAULT));
            page.activateCustomMessages();
            page.setWrongValuesAndSubmitJSF();
            assertTrue(page.getJsfInAttMsgElement().getText().endsWith(MSG_ATT));
            assertTrue(page.getJsfInBundleMsgElement().getText().endsWith(MSG_BUNDLE_JSF_CUSTOM));
        } finally {
            page.deactivateCustomMessages();
        }
    }

    /**
     * Tests correctness of JSF validation messages using a4j:commandButton for
     * submitting. Tests messages set in attribute of component and in message
     * bundle.
     */
    @CoversAttributes("validatorMessage")
    public void testJSFMessagesRF() {
        try {
            page.setWrongValuesAndSubmitRF();
            assertTrue(page.getJsfInAttMsgElement().getText().endsWith(MSG_ATT));
            assertTrue(page.getJsfInBundleMsgElement().getText().endsWith(MSG_BUNDLE_JSF_DEFAULT));
            page.activateCustomMessages();
            page.setWrongValuesAndSubmitRF();
            assertTrue(page.getJsfInAttMsgElement().getText().endsWith(MSG_ATT));
            assertTrue(page.getJsfInBundleMsgElement().getText().endsWith(MSG_BUNDLE_JSF_CUSTOM));
        } finally {
            page.deactivateCustomMessages();
        }
    }

    /**
     * Tests correctness of JSR-303 validation messages using h:commandButton
     * for submitting. Tests messages set in attribute of component, in
     * annotations of bean and in message bundle.
     */
    @CoversAttributes("validatorMessage")
    public void testJSR303MessagesJSF() {
        page.setWrongValuesAndSubmitJSF();
        assertTrue(page.getJsr303InAttMsgElement().getText().endsWith(MSG_ATT));
        assertTrue(page.getJsr303InBeanMsgElement().getText().endsWith(MSG_BEAN));
        assertTrue(page.getJsr303InBundleMsgElement().getText().endsWith(MSG_BUNDLE_JSR));
    }

    /**
     * Tests correctness of JSR-303 validation messages using a4j:commandButton
     * for submitting. Tests messages set in attribute of component, in
     * annotations of bean and in message bundle.
     */
    @CoversAttributes("validatorMessage")
    public void testJSR303MessagesRF() {
        page.setWrongValuesAndSubmitRF();
        assertTrue(page.getJsr303InAttMsgElement().getText().endsWith(MSG_ATT));
        assertTrue(page.getJsr303InBeanMsgElement().getText().endsWith(MSG_BEAN));
        assertTrue(page.getJsr303InBundleMsgElement().getText().endsWith(MSG_BUNDLE_JSR));
    }
}
