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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;
import org.richfaces.tests.metamer.ftest.richTree.TreeSimplePage;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22754 $
 */
public class TestTreeModelAdaptorSimple extends AbstractWebDriverTest {

    @Page
    TreeSimplePage page;

    protected RichFacesTreeNode treeNode;

    private Attributes<ModelAdaptorAttributes> modelAdaptorAttributes = new Attributes<ModelAdaptorAttributes>(
        "listAttributes");
    private Attributes<RecursiveModelAdaptorAttributes> recursiveModelAdaptorAttributes =
        new Attributes<RecursiveModelAdaptorAttributes>("recursiveAttributes");


    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/treeAdaptors.xhtml");
    }

    @Test
    public void testModelAdaptorRendered() {
        page.tree.getNodes().get(1).expand();
        page.tree.getNodes().get(1).getNode(1).expand();
        treeNode = page.tree.getNodes().get(1).getNode(1).getNode(0);

        assertTrue(treeNode.isLeaf());

        modelAdaptorAttributes.set(ModelAdaptorAttributes.rendered, false);

        assertFalse(treeNode.isLeaf());
    }

    @Test
    public void testRecursiveModelAdaptorRendered() {
        page.tree.getNodes().get(1).expand();
        page.tree.getNodes().get(1).getNode(1).expand();

        boolean subnodePresent = false;
        for (RichFacesTreeNode treeNode : page.tree.getNodes().get(1).getNode(1).getNodes()) {
            if (!treeNode.isLeaf()) {
                subnodePresent = true;
            }
        }
        assertTrue(subnodePresent, "there should be at least one subnode (not leaf) in expanded branch");

        recursiveModelAdaptorAttributes.set(RecursiveModelAdaptorAttributes.rendered, false);

        for (RichFacesTreeNode treeNode : page.tree.getNodes().get(1).getNode(1).getNodes()) {
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
