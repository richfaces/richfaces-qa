/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richPanelToggleListener;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPanelToggleListenerInCollapsiblePanel extends AbstractPanelToggleListenerTest {

    private final String PTL_as_ComponentAttribute_PhaseName = "panel";
    private final String PTL_inComponent_usingListener_PhaseName = "pannelToggleListenerBean panel";
    private final String PTL_inComponent_usingType_PhaseName = "pannelToggleListenerBean2 panel";
    private final String PTL_outsideComponent_usingType_PhaseName = "pannelToggleListenerBean3 panel";

    public TestPanelToggleListenerInCollapsiblePanel() {
        super("collapsiblePanel");
    }

    @Test
    @Templates(exclude={"richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "richCollapsiblePanel", "a4jRepeat"})
    public void testPTLAsAttribute() {
        super.testPTLAsAttributeOfComponent(PTL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-11568")
    @Test(groups={ "4.Future" })
    @Templates(value="richCollapsibleSubTable")
    public void testPTLAsAttributeInRichCollapsibleSubTable() {
        super.testPTLAsAttributeOfComponent(PTL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-11568")
    @Test(groups={ "4.Future" })
    @Templates(value="richExtendedDataTable")
    public void testPTLAsAttributeInRichExtendedDataTable() {
        super.testPTLAsAttributeOfComponent(PTL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-11568")
    @Test(groups={ "4.Future" })
    @Templates(value="richCollapsiblePanel")
    public void testPTLAsAttributeInRichCollapsiblePanel() {
        super.testPTLAsAttributeOfComponent(PTL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-11568")
    @Test(groups={ "4.Future" })
    @Templates(value="a4jRepeat")
    public void testPTLAsAttributeInA4jRepeat() {
        super.testPTLAsAttributeOfComponent(PTL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-11568")
    @Test(groups={ "4.Future" })
    @Templates(value="richDataGrid")
    public void testPTLAsAttributeInRichDataGrid() {
        super.testPTLAsAttributeOfComponent(PTL_as_ComponentAttribute_PhaseName);
    }

    @Test
    @Templates(exclude={"richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "richCollapsiblePanel", "a4jRepeat"})
    public void testPTLInsideComponentUsingType() {
        super.testPTLInComponentWithListener(PTL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12280")
    @Test(groups={ "4.Future" })
    @Templates(value="richCollapsibleSubTable")
    public void testPTLInsideComponentUsingTypeInRichCollapsibleSubTable() {
        super.testPTLInComponentWithListener(PTL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12280")
    @Test(groups={ "4.Future" })
    @Templates(value="richExtendedDataTable")
    public void testPTLInsideComponentUsingTypeInRichExtendedDataTable() {
        super.testPTLInComponentWithListener(PTL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12280")
    @Test(groups={ "4.Future" })
    @Templates(value="richCollapsiblePanel")
    public void testPTLInsideComponentUsingTypeInRichCollapsiblePanel() {
        super.testPTLInComponentWithListener(PTL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12280")
    @Test(groups={ "4.Future" })
    @Templates(value="a4jRepeat")
    public void testPTLInsideComponentUsingTypeInA4jRepeat() {
        super.testPTLInComponentWithListener(PTL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12280")
    @Test(groups={ "4.Future" })
    @Templates(value="richDataGrid")
    public void testPTLInsideComponentUsingTypeInRichDataGrid() {
        super.testPTLInComponentWithListener(PTL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12105")
    @Test(groups = "4.Future")
    public void testPTLInsideComponentUsingListener() {
        super.testPTLInComponentWithListener(PTL_inComponent_usingListener_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12106")
    @Test(groups = "4.Future")
    public void testPTLOutsideComponentUsingForAndType() {
        super.testPTLAsForAttributeWithType(PTL_outsideComponent_usingType_PhaseName);
    }
}
