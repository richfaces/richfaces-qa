/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.treeAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Deque;
import java.util.LinkedList;

import org.richfaces.component.SwitchType;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23005 $
 */
public class TestTreeToggling extends AbstractTestTree {

    private static final int TOP_LEVEL_NODES = 4;

    protected int[][] paths = new int[][] {{1, 2, 1 }, {4, 4, 1 }};

    @Inject
    @Use(enumeration = true)
    SwitchType toggleType = SwitchType.ajax;

    private TreeModel tree = new TreeModel(pjq("div.rf-tr[id$=richTree]"));
    private TreeNodeModel treeNode;

    @BeforeMethod
    public void verifyInitialState() {
        treeAttributes.set(TreeAttributes.toggleType, toggleType);
        tree.setToggleType(toggleType);
        assertEquals(tree.getCollapsedNodesCount(), TOP_LEVEL_NODES);
        assertEquals(tree.getExpandedNodesCount(), 0);
    }

    @Test
    public void testTopLevelNodesExpansion() {
        for (int i = 1; i <= TOP_LEVEL_NODES; i++) {
            treeNode = tree.getNode(i);
            treeNode.expand();
            assertEquals(tree.getCollapsedNodesCount(), TOP_LEVEL_NODES - i);
            assertEquals(tree.getExpandedNodesCount(), i);
            assertTrue(treeNode.isExpanded());
        }
    }

    @Test
    public void testTopLevelNodesCollapsion() {
        testTopLevelNodesExpansion();
        for (int i = 1; i <= TOP_LEVEL_NODES; i++) {
            treeNode = tree.getNode(i);
            treeNode.expand();
            assertEquals(tree.getCollapsedNodesCount(), i);
            assertEquals(tree.getExpandedNodesCount(), TOP_LEVEL_NODES - i);
            assertTrue(treeNode.isCollapsed());
        }
    }

    @Test
    public void testDeepExpansion() {
        for (int[] path : paths) {
            int depth = path.length;

            for (int d = 1; d <= path.length; d++) {
                int number = path[d - 1];

                treeNode = (d == 1) ? tree.getNode(number) : treeNode.getNode(number);

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

    @Test
    public void testDeepCollapsion() {
        Deque<TreeNodeModel> stack = new LinkedList<TreeNodeModel>();

        testDeepExpansion();

        for (TreeNodeModel treeNode1 : tree.getExpandedNodes()) {
            stack.push(treeNode1);
            for (TreeNodeModel treeNode2 : treeNode1.getExpandedNodes()) {
                stack.push(treeNode2);
            }
        }

        while ((treeNode = stack.poll()) != null) {
            treeNode.expand();
        }
    }

    public void assertNodeState(NodeState state) {
        assertEquals(treeNode.isLeaf() && treeNode.getIcon().isLeaf() && treeNode.getHandle().isLeaf(),
            state == NodeState.LEAF);
        assertEquals(treeNode.isCollapsed() && treeNode.getIcon().isCollapsed() && treeNode.getHandle().isCollapsed(),
            state == NodeState.COLLAPSED);
        assertEquals(treeNode.isExpanded() && treeNode.getIcon().isExpanded() && treeNode.getHandle().isExpanded(),
            state == NodeState.EXPANDED);
    }

    public enum NodeState {
        LEAF, COLLAPSED, EXPANDED;
    }
}
