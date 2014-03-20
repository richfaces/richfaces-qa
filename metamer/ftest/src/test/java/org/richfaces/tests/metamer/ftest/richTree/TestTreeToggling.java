/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.richfaces.component.SwitchType;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseForAllTests;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestTreeToggling extends AbstractTreeTest {

    private static final int TOP_LEVEL_NODES = 4;

    protected Integer[][] paths = new Integer[][]{ { 0, 1, 0 }, { 3, 3, 0 } };

    @UseForAllTests(valuesFrom = FROM_ENUM, value = "")
    private SwitchType toggleType = SwitchType.client;

    private TreeNode treeNode;

    private void assertNodeState(NodeState state) {
        assertEquals(treeNode.advanced().isLeaf(), state.equals(NodeState.LEAF));
        assertEquals(treeNode.advanced().isCollapsed(), state.equals(NodeState.COLLAPSED));
        assertEquals(treeNode.advanced().isExpanded(), state.equals(NodeState.EXPANDED));
    }

    private void checkInitialState() {
        assertEquals(tree.advanced().getNodesCollapsed().size(), TOP_LEVEL_NODES);
        assertEquals(tree.advanced().getNodesExpanded().size(), 0);
    }

    @Test(groups = "smoke")
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "simpleSwingTreeNode")
    public void testDeepCollapsion() {
        checkInitialState();

        testDeepExpansion();

        for (TreeNode treeNode1 : tree.advanced().getNodesExpanded()) {
            for (TreeNode treeNode2 : treeNode1.advanced().getNodesExpanded()) {
                getGuarded(treeNode2.advanced(), toggleType).collapse();
            }
            treeNode1.advanced().collapse();
        }
        checkInitialState();
    }

    @Test(groups = "extended")
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "richFacesTreeNodes")
    public void testDeepCollapsion2() {
        checkInitialState();
        testDeepCollapsion();
    }

    @Test
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "simpleSwingTreeNode")
    public void testDeepExpansion() {
        checkInitialState();
        for (Integer[] path : paths) {
            int depth = path.length - 1;
            for (int d = 0; d <= depth; d++) {
                int number = path[d];

                treeNode = (d == 0) ? tree.advanced().getNodes().get(number) : treeNode.advanced().getNodes().get(number);

                if (d < depth) {
                    assertNodeState(NodeState.COLLAPSED);
                    getGuarded(treeNode.advanced(), toggleType).expand();
                    assertNodeState(NodeState.EXPANDED);
                } else {
                    assertNodeState(NodeState.LEAF);
                }
            }
        }
    }

    @Test(groups = "extended")
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "richFacesTreeNodes")
    public void testDeepExpansion2() {
        testDeepExpansion();
    }

    @Test
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "simpleSwingTreeNode")
    public void testTopLevelNodesCollapsion() {
        checkInitialState();
        testTopLevelNodesExpansion();
        for (int i = 1; i <= TOP_LEVEL_NODES; i++) {
            treeNode = tree.advanced().getNodes().get(i - 1);
            getGuarded(treeNode.advanced(), toggleType).collapse();
            assertEquals(tree.advanced().getNodesCollapsed().size(), i);
            assertEquals(tree.advanced().getNodesExpanded().size(), TOP_LEVEL_NODES - i);
            assertTrue(treeNode.advanced().isCollapsed());
        }
    }

    @Test(groups = "extended")
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "richFacesTreeNodes")
    public void testTopLevelNodesCollapsion2() {
        testTopLevelNodesCollapsion();
    }

    @Test
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "simpleSwingTreeNode")
    public void testTopLevelNodesExpansion() {
        checkInitialState();
        for (int i = 1; i <= TOP_LEVEL_NODES; i++) {
            treeNode = tree.advanced().getNodes().get(i - 1);
            getGuarded(treeNode.advanced(), toggleType).expand();
            assertEquals(tree.advanced().getNodesCollapsed().size(), TOP_LEVEL_NODES - i);
            assertEquals(tree.advanced().getNodesExpanded().size(), i);
            assertTrue(treeNode.advanced().isExpanded());
        }
    }

    @Test(groups = "extended")
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "richFacesTreeNodes")
    public void testTopLevelNodesExpansion2() {
        testTopLevelNodesExpansion();
    }

    @BeforeMethod(groups = "smoke")
    public void verifyInitialState() {
        treeAttributes.set(TreeAttributes.toggleType, toggleType);
//        disabled because of https://issues.jboss.org/browse/ARQGRA-309
//        chechInitialState();
    }

    private static enum NodeState {

        LEAF, COLLAPSED, EXPANDED;
    }
}
