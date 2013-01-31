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
package org.richfaces.tests.metamer.ftest.richItemChangeListener;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTogglePanelItemChangeListener extends AbstractItemChangeListenerTest<ICLTogglePanelPage> {

    private final String ICL_as_ComponentAttribute_PhaseName = "item changed: item1 -> item2";
    private final String ICL_inComponent_usingType_PhaseName = "itemChangeListenerBean item changed: item1 -> item2";
    private final String ICL_inComponent_usingListener_PhaseName = "itemChangeListenerBean2 item changed: item1 -> item2";
    private final String ICL_outsideComponent_usingType_PhaseName = "itemChangeListenerBean3 item changed: item1 -> item2";

    public TestTogglePanelItemChangeListener() {
        super("togglePanel");
    }

    @Test
    @Templates(exclude = { "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "a4jRepeat" })
    public void testICLAsAttribute() {
        super.testICLAsAttributeOfComponent(ICL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12173")
    @Test(groups = { "4.Future" })
    @Templates(value = "richCollapsibleSubTable")
    public void testICLAsAttributeInRichCollapsibleSubTable() {
        super.testICLAsAttributeOfComponent(ICL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12173")
    @Test(groups = { "4.Future" })
    @Templates(value = "richExtendedDataTable")
    public void testICLAsAttributeInRichExtendedDataTable() {
        super.testICLAsAttributeOfComponent(ICL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12173")
    @Test(groups = { "4.Future" })
    @Templates(value = "richDataGrid")
    public void testICLAsAttributeInRichDataGrid() {
        super.testICLAsAttributeOfComponent(ICL_as_ComponentAttribute_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12173")
    @Test(groups = { "4.Future" })
    @Templates(value = "a4jRepeat")
    public void testICLAsAttributeInA4jRepeat() {
        super.testICLAsAttributeOfComponent(ICL_as_ComponentAttribute_PhaseName);
    }

    @Test
    @Templates(exclude = { "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid", "a4jRepeat" })
    public void testICLInsideComponentUsingType() {
        super.testICLInComponentWithType(ICL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12173")
    @Test(groups = { "4.Future" })
    @Templates(value = "richCollapsibleSubTable")
    public void testICLInsideComponentUsingTypeInRichCollapsibleSubTable() {
        super.testICLInComponentWithType(ICL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12173")
    @Test(groups = { "4.Future" })
    @Templates(value = "richExtendedDataTable")
    public void testICLInsideComponentUsingTypeInRichExtendedDataTable() {
        super.testICLInComponentWithType(ICL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12173")
    @Test(groups = { "4.Future" })
    @Templates(value = "richDataGrid")
    public void testICLInsideComponentUsingTypeInRichDataGrid() {
        super.testICLInComponentWithType(ICL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12173")
    @Test(groups = { "4.Future" })
    @Templates(value = "a4jRepeat")
    public void testICLInsideComponentUsingTypeInA4jRepeat() {
        super.testICLInComponentWithType(ICL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12087")
    @Test(groups = "4.Future")
    public void testICLInsideComponentUsingListener() {
        super.testICLInComponentWithListener(ICL_inComponent_usingListener_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12089")
    @Test(groups = "4.Future")
    public void testICLOutsideComponentUsingForAndType() {
        super.testICLAsForAttributeWithType(ICL_outsideComponent_usingType_PhaseName);
    }
}
