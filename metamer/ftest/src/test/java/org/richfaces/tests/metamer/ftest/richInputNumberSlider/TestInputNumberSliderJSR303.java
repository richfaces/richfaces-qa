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
package org.richfaces.tests.metamer.ftest.richInputNumberSlider;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.abstractions.validations.AbstractNumberInputComponentValidationTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestInputNumberSliderJSR303 extends AbstractNumberInputComponentValidationTest {

    @Page
    private CustomValidationTestsPage tests;

    @Override
    public String getComponentName() {
        return "richInputNumberSlider";
    }

    @Test
    @Use(field = "commonCase", value = "commonCases")
    public void testCommonCases() {
        verifyCases();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11314")
    public void testCustomBySliding() {
        tests.verifyCustomBySliding(submitMethod, getWait());
    }

    @Test
    public void testMaxBySliding() {
        tests.verifyMinBySliding(submitMethod, getWait());
    }

    @Test
    public void testMinBySliding() {
        tests.verifyMinBySliding(submitMethod, getWait());
    }
}
