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
package org.richfaces.tests.metamer.ftest.richPanelMenu;

import static java.text.MessageFormat.format;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.fragment.panelMenu.RichFacesPanelMenu;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF11159 extends AbstractPanelMenuTest {

    private static final boolean COLLAPSED = false;
    private static final boolean EXPANDED = true;

    private static final String GROUP_AT_INDEX_SHOULD_BE_COLLAPSED = "Group at index <{0}> should be collapsed";

    private static final String GROUP_AT_INDEX_SHOULD_BE_EXPANDED = "Group at index <{0}> should be expanded";

    private void checkGroupIsCollapsed(RichFacesPanelMenu panelMenu, int index) {
        assertFalse(panelMenu.advanced().isGroupExpanded(panelMenu.getMenuGroups().get(index)), format(GROUP_AT_INDEX_SHOULD_BE_COLLAPSED, index));
    }

    private void checkGroupIsExpanded(RichFacesPanelMenu panelMenu, int index) {
        assertTrue(panelMenu.advanced().isGroupExpanded(panelMenu.getMenuGroups().get(index)), format(GROUP_AT_INDEX_SHOULD_BE_EXPANDED, index));
    }

    private void checkGroupsStaysInSameStateAfterPageRefresh(RichFacesPanelMenu panelMenu, List<Boolean> expandedOrCollapsed) {
        int i = 0;
        for (Boolean state : expandedOrCollapsed) {
            if (state) {
                checkGroupIsExpanded(panelMenu, i);
            } else {
                checkGroupIsCollapsed(panelMenu, i);
            }
            i++;
        }
        getMetamerPage().fullPageRefresh();
        i = 0;
        for (Boolean state : expandedOrCollapsed) {
            if (state) {
                checkGroupIsExpanded(panelMenu, i);
            } else {
                checkGroupIsCollapsed(panelMenu, i);
            }
            i++;
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-11159")
    public void testGroupsSaveExpandedProperty() {
        getUnsafeAttributes("").set("groupMode", "ajax");
        RichFacesPanelMenu panelMenu = getPage().getPanelMenu();
        List<Boolean> expandedOrCollapsed = Lists.newArrayList(COLLAPSED, COLLAPSED, COLLAPSED);
        for (int i : new int[] { 0, 1, 2 }) {
            Graphene.guardAjax(panelMenu).expandGroup(i);
            expandedOrCollapsed.set(i, EXPANDED);
            checkGroupsStaysInSameStateAfterPageRefresh(panelMenu, expandedOrCollapsed);
        }
        for (int i : new int[] { 0, 2, 1 }) {
            Graphene.guardAjax(panelMenu).collapseGroup(i);
            expandedOrCollapsed.set(i, COLLAPSED);
            checkGroupsStaysInSameStateAfterPageRefresh(panelMenu, expandedOrCollapsed);
        }
    }
}
