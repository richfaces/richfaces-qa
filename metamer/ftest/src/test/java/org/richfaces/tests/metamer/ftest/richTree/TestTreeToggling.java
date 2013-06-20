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
package org.richfaces.tests.metamer.ftest.richTree;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.treeAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Deque;
import java.util.LinkedList;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;
import org.richfaces.ui.common.SwitchType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23005 $
 */
public class TestTreeToggling extends AbstractTestTree {

    private static final int TOP_LEVEL_NODES = 4;

    protected int[][] paths = new int[][]{ { 1, 2, 1 }, { 4, 4, 1 } };

    @Inject
    @Use(enumeration = true)
    SwitchType toggleType = SwitchType.ajax;

    @Page
    protected TreeSimplePage page;

    private RichFacesTreeNode treeNode;

    @BeforeMethod
    public void verifyInitialState() {
        treeAttributes.set(TreeAttributes.toggleType, toggleType);
        page.tree.setToggleType(toggleType);
//        disabled because of https://issues.jboss.org/browse/ARQGRA-309
//        chechInitialState();
    }

    private void chechInitialState() {
        assertEquals(page.tree.getCollapsedNodes().size(), TOP_LEVEL_NODES);
        assertEquals(page.tree.getExpandedNodes().size(), 0);
    }

    @Test
    @Use(field = "sample", value = "swingTreeNode")
    public void testTopLevelNodesExpansion() {
        chechInitialState();
        for (int i = 1; i <= TOP_LEVEL_NODES; i++) {
            treeNode = page.tree.getNodes().get(i - 1);
            treeNode.setToggleType(toggleType);
            treeNode.expand();
            assertEquals(page.tree.getCollapsedNodes().size(), TOP_LEVEL_NODES - i);
            assertEquals(page.tree.getExpandedNodes().size(), i);
            assertTrue(treeNode.isExpanded());
        }
    }

    @Test(groups = "extended")
    @Use(field = "sample", value = "richFacesTreeNodes")
    public void testTopLevelNodesExpansion2() {
        chechInitialState();
        testTopLevelNodesExpansion();
    }

    @Test
    @Use(field = "sample", value = "swingTreeNode")
    public void testTopLevelNodesCollapsion() {
        chechInitialState();
        testTopLevelNodesExpansion();
        for (int i = 1; i <= TOP_LEVEL_NODES; i++) {
            treeNode = page.tree.getNodes().get(i - 1);
            treeNode.setToggleType(toggleType);
            treeNode.collapse();
            assertEquals(page.tree.getCollapsedNodes().size(), i);
            assertEquals(page.tree.getExpandedNodes().size(), TOP_LEVEL_NODES - i);
            assertTrue(treeNode.isCollapsed());
        }
    }

    @Test(groups = "extended")
    @Use(field = "sample", value = "richFacesTreeNodes")
    public void testTopLevelNodesCollapsion2() {
        chechInitialState();
        testTopLevelNodesCollapsion();
    }

    @Test
    @Use(field = "sample", value = "swingTreeNode")
    public void testDeepExpansion() {
        chechInitialState();
        for (int[] path : paths) {
            int depth = path.length;

            for (int d = 1; d <= path.length; d++) {
                int number = path[d - 1];

                treeNode = (d == 1) ? page.tree.getNodes().get(number - 1) : treeNode.getNode(number - 1);
                treeNode.setToggleType(toggleType);

                if (d < depth) {
                    assertNodeState(NodeState.COLLAPSED);
                    treeNode.expand();
                    assertNodeState(NodeState.EXPANDED);
                } else {
                    assertNodeState(NodeState.LEAF);
                }
            }
        }
    }

    @Test(groups = "extended")
    @Use(field = "sample", value = "richFacesTreeNodes")
    public void testDeepExpansion2() {
        chechInitialState();
        testDeepExpansion();
    }

    @Test
    @Use(field = "sample", value = "swingTreeNode")
    public void testDeepCollapsion() {
        chechInitialState();
        Deque<RichFacesTreeNode> stack = new LinkedList<RichFacesTreeNode>();

        testDeepExpansion();

        for (RichFacesTreeNode treeNode1 : page.tree.getExpandedNodes()) {
            stack.push(treeNode1);
            for (RichFacesTreeNode treeNode2 : treeNode1.getExpandedNodes()) {
                stack.push(treeNode2);
            }
        }

        while ((treeNode = stack.poll()) != null) {
            treeNode.setToggleType(toggleType);
            treeNode.expand();
        }
    }

    @Test(groups = "extended")
    @Use(field = "sample", value = "richFacesTreeNodes")
    public void testDeepCollapsion2() {
        chechInitialState();
        testDeepCollapsion();
    }

    public void assertNodeState(NodeState state) {
        assertEquals(treeNode.isLeaf() && treeNode.getIcon().isLeaf() && treeNode.getHandle().isLeaf(),
                state == NodeState.LEAF);
        assertEquals(treeNode.isCollapsed() && treeNode.getIcon().isCollapsed() && treeNode.getHandle().isCollapsed(),
                state == NodeState.COLLAPSED);
        assertEquals(treeNode.isExpanded() && treeNode.getIcon().isExpanded() && treeNode.getHandle().isExpanded(),
                state == NodeState.EXPANDED);
    }

    public static enum NodeState {

        LEAF, COLLAPSED, EXPANDED;
    }
}
