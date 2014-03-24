/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richDropTarget;

import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.0.CR1
 */
@RegressionTest("https://issues.jboss.org/browse/RFPL-2751")
@Templates(value = { "richCollapsibleSubTable" })
public class TestDropTargetInCollapsibleSubTable extends TestDropTarget {

    @Test
    public void testAcceptedTypes() {
        super.testAcceptedTypes();
    }

    @Test
    public void testRender() {
        super.testRender();
    }

    @Test
    public void testDropListenerAndEvent() {
        super.testDropListenerAndEvent();
    }

    @Test
    public void testExecute() {
        super.testExecute();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10535")
    public void testImmediate() {
        super.testImmediate();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10535")
    public void testBypassUpdates() {
        super.testBypassUpdates();
    }

    @Test
    public void testEvents() {
        super.testEvents();
    }
}
