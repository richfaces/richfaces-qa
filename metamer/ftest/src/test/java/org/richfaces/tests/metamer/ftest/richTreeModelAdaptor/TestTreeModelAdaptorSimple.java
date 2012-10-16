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
package org.richfaces.tests.metamer.ftest.richTreeModelAdaptor;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractGrapheneTest;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.attributes.Attributes;
import org.richfaces.tests.metamer.ftest.richTree.TreeModel;
import org.richfaces.tests.metamer.ftest.richTree.TreeNodeModel;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22754 $
 */
public class TestTreeModelAdaptorSimple extends AbstractGrapheneTest {

    protected TreeModel tree = new TreeModel(pjq("div.rf-tr[id$=richTree]"));
    protected TreeNodeModel treeNode;

    private Attributes<ModelAdaptorAttributes> modelAdaptorAttributes = new Attributes<ModelAdaptorAttributes>(
        pjq("span[id$=:listAttributes:panel]"));
    private Attributes<RecursiveModelAdaptorAttributes> recursiveModelAdaptorAttributes =
        new Attributes<RecursiveModelAdaptorAttributes>(pjq("span[id$=:recursiveAttributes:panel]"));


    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/treeAdaptors.xhtml");
    }

    @Override
    public MetamerNavigation getComponentExampleNavigation() {
        return new MetamerNavigation("Rich", "Rich Tree", "Tree Adaptors");
    }

    @Test
    public void testModelAdaptorRendered() {
        tree.getNode(2).expand();
        tree.getNode(2).getNode(2).expand();
        treeNode = tree.getNode(2).getNode(2).getNode(1);

        assertTrue(treeNode.isLeaf());

        modelAdaptorAttributes.set(ModelAdaptorAttributes.rendered, false);

        assertFalse(treeNode.isLeaf());
    }

    @Test
    public void testRecursiveModelAdaptorRendered() {
        tree.getNode(2).expand();
        tree.getNode(2).getNode(2).expand();

        boolean subnodePresent = false;
        for (TreeNodeModel treeNode : tree.getNode(2).getNode(2).getNodes()) {
            if (!treeNode.isLeaf()) {
                subnodePresent = true;
            }
        }
        assertTrue(subnodePresent, "there should be at least one subnode (not leaf) in expanded branch");

        recursiveModelAdaptorAttributes.set(RecursiveModelAdaptorAttributes.rendered, false);

        for (TreeNodeModel treeNode : tree.getNode(2).getNode(2).getNodes()) {
            if (!treeNode.isLeaf()) {
                fail("there should be no subnode (not leaf) in expanded branch");
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
