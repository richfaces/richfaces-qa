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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import org.richfaces.tests.metamer.ftest.abstractions.AbstractResetValuesTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.On;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.AndExpression;
import org.richfaces.tests.metamer.ftest.extension.configurator.skip.annotation.Skip;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Templates("plain")
@RegressionTest("https://issues.jboss.org/browse/RF-13532")
public class TestResetValues extends AbstractResetValuesTest {

    @Override
    public String getComponentTestPagePath() {
        return "a4jAjax/rf-13532.xhtml";
    }

    @Test
    @Skip(expressions = {
        @AndExpression(On.JSF.VersionLowerThan22.class),
        @AndExpression(On.JSF.MyFaces.class)
    })
    @CoversAttributes("resetValues")
    public void testResetValues() {
        checkResetValues();
    }
}
