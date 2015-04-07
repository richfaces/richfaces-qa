/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richTree;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_FIELD;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.tree.RichFacesTreeNode;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.tests.metamer.ftest.MetamerAttributes;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

public class TestTreeLoadingFacet extends AbstractTreeTest {

    private static final int[] INDEXES = new int[] { 0, 1 };

    private final Attributes<MetamerAttributes> metamerAttributes = getAttributes();

    @FindBy(css = "input[id$=loadingFacet]")
    private WebElement loadingFacet;

    private void setLoadingFacet(boolean turnOn) {
        boolean checked = Boolean.valueOf(loadingFacet.isSelected());
        if (checked != turnOn) {
            Graphene.guardAjax(loadingFacet).click();
        }
    }

    @Test(groups = "smoke")
    @UseWithField(field = "sample", valuesFrom = FROM_FIELD, value = "simpleSwingTreeNode")
    @RegressionTest("https://issues.jboss.org/browse/RF-12696")
    public void testLoadingFacet() {
        int sufficientTimeToCheckHandles = 1500;// ms
        setLoadingFacet(true);
        metamerAttributes.set(MetamerAttributes.metamerResponseDelay, sufficientTimeToCheckHandles);
        TreeNode treeNode = null;
        WebElement handleLoadingElement;
        for (int index : INDEXES) {
            treeNode = treeNode == null ? Graphene.createPageFragment(RichFacesTreeNode.class, driver.findElement(By.cssSelector("div[id$=richTree] > .rf-tr-nd"))) : treeNode.advanced().getNodes().get(index);
            handleLoadingElement = treeNode.advanced().getHandleLoadingElement();
            assertFalse(Utils.isVisible(handleLoadingElement));
            // trigger expand node without waiting for expanding
            treeNode.advanced().getHandleElement().click();
            assertTrue(Utils.isVisible(handleLoadingElement));
            assertTrue(handleLoadingElement.findElement(By.tagName("img")).getAttribute("src").endsWith(IMAGE_URL));
            treeNode.advanced().waitUntilNodeIsExpanded().perform();
        }
    }
}
