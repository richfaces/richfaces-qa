/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jAjax/hInputText.xhtml
 *
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 * @since 4.3.0.M2
 */
@Templates("plain")
public class TestHInputText extends AbstractAjaxTest {

    @Override
    public String getDefaultOutput() {
        return "";
    }

    @Override
    public String getExpectedOutput() {
        return "RichFaces 4";
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jAjax/hInputText.xhtml");
    }

    @Override
    public void performAction() {
        performAction("RichFaces 4");
    }

    @Override
    public void performAction(String input) {
        page.input.clear();
        typeKeys(input);
    }

    @Test(groups = "smoke")
    public void testBypassUpdates() {
        super.testBypassUpdates();
    }

    @Test(groups = "smoke")
    public void testData() {
        super.testData();
    }

    @Test
    public void testDisabled() {
        super.testDisabledForTextInputs();
    }

    @Test
    public void testEvents() {
        super.testEventsForTextInputs();
    }

    @Test(groups = "smoke")
    public void testExecute() {
        super.testExecute();
    }

    @Test(groups = "smoke")
    public void testImmediate() {
        super.testImmediate();
    }

    @Test
    public void testImmediateBypassUpdates() {
        super.testImmediateBypassUpdates();
    }

    @Test(groups = "smoke")
    public void testLimitRender() {
        super.testLimitRender("RichFaces 4");
    }

    @Test
    @UseWithField(field = "listener", valuesFrom = ValuesFrom.FROM_ENUM, value = "")
    public void testListener() {
        testListener(getActionMapForListeners());
    }

    @Test(groups = "smoke")
    public void testRender() {
        super.testRender();
    }

    @Test
    public void testSimpleType() {
        super.testType();
    }

    @Test(groups = "smoke")
    @RegressionTest("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleTypeUnicode() {
        super.testTypeUnicode();
    }

    @Test(groups = "smoke")
    public void testStatus() {
        super.testStatus();
    }
}
