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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

@Use(field = "sample", value = "simpleSwingTreeNode")
public class TestTreeLoadingFacet extends AbstractTreeTest {

    @FindBy(css = "input[id$=loadingFacet]")
    private WebElement loadingFacet;

    private void setLoadingFacet(boolean turnOn) {
        boolean checked = Boolean.valueOf(loadingFacet.isSelected());
        if (checked != turnOn) {
            Graphene.guardAjax(loadingFacet).click();
        }
    }

    private void setResponseDelay(int milis) {
        page.getResponseDelayElement().sendKeys(String.valueOf(milis));
        page.getResponseDelayElement().submit();
    }

    @Test(groups = "smoke")
    @RegressionTest("https://issues.jboss.org/browse/RF-12696")
    public void testLoadingFacet() {
        int sufficientTimeToCheckHandles = 2000;// ms
        setLoadingFacet(true);
        setResponseDelay(sufficientTimeToCheckHandles);
        TreeNode treeNode = null;
        for (int index : new int[]{ 0, 1 }) {
            treeNode = (index == 0) ? tree.advanced().getNodes().get(index) : treeNode.advanced().getNodes().get(index);

            assertFalse(Utils.isVisible(treeNode.advanced().getHandleLoadingElement()));
            assertTrue(treeNode.advanced().getHandleElement().getAttribute("class").contains("rf-trn-hnd-colps"));
            assertFalse(treeNode.advanced().getHandleElement().getAttribute("class").contains("rf-trn-hnd-exp"));
            // trigger expand node without waiting for expanding
            treeNode.advanced().getHandleElement().click();
            assertTrue(Utils.isVisible(treeNode.advanced().getHandleLoadingElement()));
            assertTrue(treeNode.advanced().getHandleLoadingElement().findElement(By.tagName("img")).getAttribute("src").endsWith(IMAGE_URL));

            treeNode.advanced().waitUntilNodeIsExpanded().perform();
        }
    }

}
