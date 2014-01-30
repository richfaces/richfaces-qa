/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.tests.metamer.ftest.richDragSource;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 * Test extending default drag source test for RF-12256 simulation
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
public class TestDragSourceRF12256 extends AbstractDragSourceTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDragSource/RF-12256.xhtml");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12441")
    public void testDefaultIndicator() {
        super.testDefaultIndicator();
    }

    @Test
    public void testCustomIndicator() {
        super.testCustomIndicator();
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    public void testType() {
        super.testType();
    }

}
