/**
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
 */
package org.richfaces.tests.metamer.ftest.richTreeToggleListener;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTreeToggleListenerSimple extends AbstractTreeToggleListenerTest {

    private final String TTL_as_ComponentAttribute_PhaseName = "tree toggle listener invoked";
    private final String TTL_inComponent_usingListener_PhaseName = "TreeToggleListenerBean2";
    private final String TTL_inComponent_usingType_PhaseName = "TreeToggleListenerBean1";
    private final String TTL_outsideComponent_usingType_PhaseName = "TreeToggleListenerBean3";

    public TestTreeToggleListenerSimple() {
        super("simple");
    }

    @Test
    public void testTTLAsAttribute() {
        super.testTTLAsAttributeOfComponent(TTL_as_ComponentAttribute_PhaseName);
    }

    @Test
    public void testTTLInsideComponentUsingType() {
        super.testTTLInComponentWithListener(TTL_inComponent_usingType_PhaseName);
    }

    @Test
    public void testTTLInsideComponentUsingListener() {
        super.testTTLInComponentWithListener(TTL_inComponent_usingListener_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12121")
    @Test(groups = "4.Future")
    public void testTTLOutsideComponentUsingForAndType() {
        super.testTTLAsForAttributeWithType(TTL_outsideComponent_usingType_PhaseName);
    }
}
