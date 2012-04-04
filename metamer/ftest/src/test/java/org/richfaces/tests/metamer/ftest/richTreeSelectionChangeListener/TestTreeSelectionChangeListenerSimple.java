/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.richTreeSelectionChangeListener;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTreeSelectionChangeListenerSimple extends AbstractTreeSelectionChangeListenerTest {

    private final String TSCL_as_ComponentAttribute_PhaseName = "* selection change listener invoked";
    private final String TSCL_inComponent_usingListener_PhaseName = "* TreeSelectionChangeListenerBean2";
    private final String TSCL_inComponent_usingType_PhaseName = "* TreeSelectionChangeListenerBean1";
    private final String TSCL_outsideComponent_usingType_PhaseName = "* TreeSelectionChangeListenerBean3";

    public TestTreeSelectionChangeListenerSimple() {
        super("simple");
    }

    @Override
    public void loadPage() {
        page = new TSCLPage();
    }

    @Test
    public void testTSCLAsAttribute() {
        super.testTSCLAsAttributeOfComponent(TSCL_as_ComponentAttribute_PhaseName);
    }

    @Test
    public void testTSCLInsideComponentUsingType() {
        super.testTSCLInComponentWithListener(TSCL_inComponent_usingType_PhaseName);
    }

    @Test
    public void testTSCLInsideComponentUsingListener() {
        super.testTSCLInComponentWithListener(TSCL_inComponent_usingListener_PhaseName);
    }

//    @IssueTracking("https://issues.jboss.org/browse/RF-x")
    @Test(groups = "4.Future")
    public void testTSCLOutsideComponentUsingForAndType() {
        super.testTSCLAsForAttributeWithType(TSCL_outsideComponent_usingType_PhaseName);
    }
}
