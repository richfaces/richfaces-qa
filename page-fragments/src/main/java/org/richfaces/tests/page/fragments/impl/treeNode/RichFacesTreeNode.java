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
package org.richfaces.tests.page.fragments.impl.treeNode;

import com.google.common.base.Predicate;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.tree.RichFacesTree;
import org.richfaces.ui.common.SwitchType;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichFacesTreeNode extends RichFacesTree {

    public static final String CLASS_NODE_EXPANDED = "rf-tr-nd-exp";
    public static final String CLASS_NODE_LEAF = "rf-tr-nd-lf";
    public static final String CLASS_NODE_COLLAPSED = "rf-tr-nd-colps";
    public static final String CLASS_SELECTED = "rf-trn-sel";
    public static final String CLASS_CONTENT = "rf-trn-cnt";
    public static final String CLASS_ICON = "rf-trn-ico";

    public static final String CSS_NODE = "div.rf-tr-nd";
    public static final String CSS_NODE_ITSELF = "div.rf-trn"; // if node expanded, this is part without child nodes
    public static final String CSS_NODE_HANDLER = "div span.rf-trn-hnd";
    public static final String CSS_NODE_HANDLER_LOADING = "div span.rf-trn-hnd-ldn-fct";
    public static final String CSS_NODE_LABEL = "span.rf-trn-lbl";
    public static final String CSS_NODE_CONTENT = "span." + CLASS_CONTENT;
    public static final String CSS_NODE_ICON = "." + CLASS_ICON; // icon should be span or img

    public static final String JQUERY_NODES_SELECTED = "div.rf-tr-nd:has(> .rf-trn > .rf-trn-sel)";
    public static final String CSS_NODES_COLLAPSED = "div." + CLASS_NODE_COLLAPSED;
    public static final String CSS_NODES_EXPANDED = "div." + CLASS_NODE_EXPANDED;

    @FindBy(css = CSS_NODE_ICON)
    private RichFacesTreeNodeIcon icon;

    @FindBy(css = CSS_NODE_HANDLER)
    private RichFacesTreeNodeHandle handle;

    @FindBy(css = CSS_NODE_HANDLER_LOADING)
    private RichFacesTreeNodeHandleLoadingFacet handleLoading;

    // part of node without subnodes
    @FindBy(css = CSS_NODE_ITSELF)
    private WebElement nodeItself;

    @FindByJQuery(">" + CSS_NODE)
    public List<RichFacesTreeNode> nodes;

//    @FindByJQuery(">" + CSS_NODES_COLLAPSED)
//    public List<RichFacesTreeNode> nodesCollapsed;
//
//
//    @FindByJQuery(">" + CSS_NODES_EXPANDED)
//    public List<RichFacesTreeNode> nodesExpanded;
//
    public List<RichFacesTreeNode> getNodes() {
        return nodes;
    }

    public RichFacesTreeNode getNode(int index) {
        if (getNodes().size() <= index) {
            return null;
        }
        return getNodes().get(index);
    }

    public WebElement getNodeItself() {
        return nodeItself;
    }

    public RichFacesTreeNodeIcon getIcon() {
        return icon;
    }

    public WebElement getNodeLabel() {
        return getRoot().findElement(By.cssSelector(CSS_NODE_LABEL));
    }

    public RichFacesTreeNodeHandle getHandle() {
        return handle;
    }

    public RichFacesTreeNodeHandleLoadingFacet getHandleLoading() {
        return handleLoading;
    }

    public String getNodeLabelText() {
        return getNodeLabel().getText();
    }

    public boolean isLeaf() {
        return getRoot().getAttribute("class").contains(CLASS_NODE_LEAF);
    }

    public int getNodesCount() {
        return getNodes().size();
    }

    /** Click on node expand handler and wait until expanded **/
    public void expand() {
        if (isCollapsed()) {
            triggerNodeHandlerClick();
        }

        Graphene.waitAjax().until(isExpandedCondition());
    }

    /** Click on node collapse handler and wait until collapsed **/
    public void collapse() {
        if (isExpanded()) {
            triggerNodeHandlerClick();
        }

        Graphene.waitAjax().until(isCollapsedCondition());
    }

    public void select() {
        getGuardByRequestType(getSelectionType(), getNodeLabel()).click();

        Graphene.waitAjax().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver arg0) {
                return isSelected();
            }
        });
    }

    /** Just click on node handler (expand/collapse), without waiting **/
    public void triggerNodeHandlerClick() {
        if (getToggleType() == null) {
            getHandle().getRoot().click();
        } else {
            getGuardByRequestType(getToggleType(), getHandle().getRoot()).click();
        }
    }

    public boolean isSelected() {
        return getNodeItself().findElement(By.cssSelector(CSS_NODE_CONTENT)).getAttribute("class").contains(CLASS_SELECTED);
    }

    public boolean isCollapsed() {
        return getRoot().getAttribute("class").contains(CLASS_NODE_COLLAPSED);
    }

    public ExpectedCondition<Boolean> isCollapsedCondition() {
        return new WebElementConditionFactory(getRoot()).attribute("class").contains(CLASS_NODE_COLLAPSED);
    }

    public boolean isExpanded() {
        return getRoot().getAttribute("class").contains(CLASS_NODE_EXPANDED);
    }

    public ExpectedCondition<Boolean> isExpandedCondition() {
        return new WebElementConditionFactory(getRoot()).attribute("class").contains(CLASS_NODE_EXPANDED);
    }

    public int getAnySelectedNodesCount() {
        return getRoot().findElements(By.cssSelector(JQUERY_NODES_SELECTED)).size();
    }

    public int getCollapsedNodesCount() {
        return getNodesCollapsed().size();
    }

    public int getExpandedNodesCount() {
        return getNodesExpanded().size();
    }

    private <T> T getGuardByRequestType(SwitchType switchType, T target) {
        switch (switchType) {
            case ajax:
                return Graphene.guardAjax(target);
            case server:
                return Graphene.guardHttp(target);
            default:
                return Graphene.guardNoRequest(target);
        }
    }
}
