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
package org.richfaces.tests.metamer.ftest.richTreeModelAdaptor;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import org.richfaces.component.SwitchType;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.richTree.AbstractTreeTest;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestTreeModelAdaptorSimple extends AbstractTreeTest {

    private TreeNode treeNode;

    private final Attributes<ModelAdaptorAttributes> modelAdaptorAttributes = getAttributes("listAttributes");
    private final Attributes<RecursiveModelAdaptorAttributes> recursiveModelAdaptorAttributes = getAttributes("recursiveAttributes");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/treeAdaptors.xhtml");
    }

    @Test
    public void testModelAdaptorRendered() {
        treeNode = getGuarded(tree, SwitchType.ajax).expandNode(1);
        treeNode = getGuarded(treeNode, SwitchType.ajax).expandNode(1);
        treeNode = treeNode.advanced().getFirstNode();

        assertTrue(treeNode.advanced().isLeaf());

        modelAdaptorAttributes.set(ModelAdaptorAttributes.rendered, false);

        assertFalse(treeNode.advanced().isLeaf());
    }

    @Test
    public void testRecursiveModelAdaptorRendered() {
        treeNode = getGuarded(tree, SwitchType.ajax).expandNode(1);
        treeNode = getGuarded(treeNode, SwitchType.ajax).expandNode(1);

        boolean subnodePresent = false;
        for (TreeNode actTreeNode : treeNode.advanced().getNodes()) {
            if (!actTreeNode.advanced().isLeaf()) {
                subnodePresent = true;
            }
        }
        assertTrue(subnodePresent, "there should be at least one subnode (not leaf) in expanded branch");

        recursiveModelAdaptorAttributes.set(RecursiveModelAdaptorAttributes.rendered, false);

        for (TreeNode actTreeNode : treeNode.advanced().getNodes()) {
            if (!actTreeNode.advanced().isLeaf()) {
                fail("there should be no subnode (only leaves) in expanded branch");
            }
        }
    }

    private enum ModelAdaptorAttributes implements AttributeEnum {

        rendered,
    }

    private enum RecursiveModelAdaptorAttributes implements AttributeEnum {

        rendered,
    }

}
