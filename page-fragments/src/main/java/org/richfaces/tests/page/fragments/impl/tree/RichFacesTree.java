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
package org.richfaces.tests.page.fragments.impl.tree;

import static org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode.CSS_NODES_COLLAPSED;
import static org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode.CSS_NODES_EXPANDED;
import static org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode.JQUERY_NODES_SELECTED;

import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.component.SwitchType;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichFacesTree { //extends RichFacesTreeNode {

    @Root
    public WebElement root;

    @FindBy(jquery = ">" + RichFacesTreeNode.CSS_NODE)
    public List<RichFacesTreeNode> nodes;

    @FindBy(jquery = RichFacesTreeNode.CSS_NODE)
    public List<RichFacesTreeNode> anyNodes;

    @FindBy(jquery = ">" + CSS_NODES_COLLAPSED)
    public List<RichFacesTreeNode> nodesCollapsed;

    @FindBy(jquery = ">" + CSS_NODES_EXPANDED)
    public List<RichFacesTreeNode> nodesExpanded;

    @FindBy(jquery = JQUERY_NODES_SELECTED)
    public List<WebElement> anySelectedNodes;

    SwitchType toggleType = SwitchType.ajax;
    SwitchType selectionType = SwitchType.ajax;

    public void setToggleType(SwitchType toggleType) {
        this.toggleType = toggleType;
    }

    public SwitchType getToggleType() {
        return toggleType;
    }

    public void setSelectionType(SwitchType selectionType) {
        this.selectionType = selectionType;
    }

    public SwitchType getSelectionType() {
        return selectionType;
    }

    public List<RichFacesTreeNode> getNodes() {
        return nodes;
    }

    public List<RichFacesTreeNode> getAnyNodes() {
        return anyNodes;
    }

    public List<RichFacesTreeNode> getExpandedNodes() {
        return nodesExpanded;
    }

    public List<RichFacesTreeNode> getCollapsedNodes() {
        return nodesCollapsed;
    }

    public int getAnySelectedNodesCount() {
        return anySelectedNodes.size();
    }
}
