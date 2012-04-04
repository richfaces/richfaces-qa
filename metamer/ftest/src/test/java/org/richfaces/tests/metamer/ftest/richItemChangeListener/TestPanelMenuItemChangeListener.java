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
package org.richfaces.tests.metamer.ftest.richItemChangeListener;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPanelMenuItemChangeListener extends AbstractItemChangeListenerTest {

    private final String ICL_as_ComponentAttribute_PhaseName = "* item changed: null -> item1";
    private final String ICL_inComponent_usingType_PhaseName = "* itemChangeListenerBean item changed: null -> item1";
    private final String ICL_inComponent_usingListener_PhaseName = "* itemChangeListenerBean2 item changed: null -> item1";
    private final String ICL_outsideComponent_usingType_PhaseName = "* itemChangeListenerBean3 item changed: null -> item1";

    public TestPanelMenuItemChangeListener() {
        super("panelMenu");
    }

    @Override
    public void loadPage() {
        page = new ICLPanelMenuPage();
    }

    @Test
    public void testICLAsAttribute() {
        super.testICLAsAttributeOfComponent(ICL_as_ComponentAttribute_PhaseName);
    }

    @Test
    public void testICLInsideComponentUsingType() {
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
