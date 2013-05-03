/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

import java.util.List;

import javax.annotation.Nullable;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.tree.RichFacesTree;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;

import com.google.common.base.Predicate;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class TreeSimplePage extends MetamerPage {

    @FindBy(css = "div[id$=richTree]")
    public RichFacesTree tree;

    @FindBy(jquery = "input:submit[id$=expandAll]")
    private WebElement expandAll;

    @FindBy(css = "span[id$=selection]")
    public WebElement selection;

    @FindBy(css = "span[id$=selectionEventClientId]")
    public WebElement clientId;

    @FindBy(css = "span[id$=selectionEventNewSelection]")
    public WebElement newSelection;

    @FindBy(css = "span[id$=selectionEventOldSelection]")
    public WebElement oldSelection;

    @FindBy(css = "span[id$=lazyInitialized]")
    public WebElement lazyInitialized;

    @FindBy(jquery = "input:checkbox[id$=loadingFacet]")
    public WebElement loadingFacet;

    @FindBy(jquery = "input[id$='treeNode1Attributes:styleClassInput']")
    public WebElement node1StyleClassInput;

    @FindBy(css = "input[id$='treeNode1Attributes:styleInput']")
    public WebElement node1StyleInput;

    /** rich:treeNode type="country" (1st level) */
    @FindBy(jquery = "span[id$='treeNode1Attributes:panel']")
    public WebElement node1AttributesTable;

    /** rich:treeNode type="company" (2nd level) */
    @FindBy(jquery = "span[id$='treeNode2Attributes:panel']")
    public WebElement node2AttributesTable;

    /** rich:treeNode type="cd" (3rd level) */
    @FindBy(jquery = "span[id$='treeNode3Attributes:panel']")
    public WebElement node3AttributesTable;

    @FindBy(jquery = ":radio[id*=recursiveModelRepresentation]")
    public List<WebElement> recursiveModelRepresentations;

    @FindBy(jquery = ":checkbox[id$=recursiveLeafChildrenNullable]")
    public WebElement recursiveLeafChildrenNullable;

    public WebElement getNode1InputForAttribute(String attributeName) {
        return getInputForAttribute(node1AttributesTable, attributeName);
    }

    public WebElement getNode2InputForAttribute(String attributeName) {
        return getInputForAttribute(node2AttributesTable, attributeName);
    }

    public WebElement getNode3InputForAttribute(String attributeName) {
        return getInputForAttribute(node3AttributesTable, attributeName);
    }

    public WebElement getInputForAttribute(WebElement attributeTable, String attributeName) {
        return attributeTable.findElement(By.cssSelector("input[id$=on" + attributeName + "Input]"));
    }

    public void expandAll() {
        Graphene.guardAjax(expandAll).click();

        // Expand all nodes takes some time, so wait for some leaf nodes to be expanded
        Graphene.waitModel().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(@Nullable WebDriver arg0) {
                List<RichFacesTreeNode> allNodes = tree.getNodes();
                RichFacesTreeNode lastNodeAtLevel1 = allNodes.get(allNodes.size() - 1);
                if (lastNodeAtLevel1.isExpanded()) {
                    RichFacesTreeNode nodeAtLevel2 = lastNodeAtLevel1.getNodes().get(0);
                    if (nodeAtLevel2.isExpanded()) {
                        // close enough :-)
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
