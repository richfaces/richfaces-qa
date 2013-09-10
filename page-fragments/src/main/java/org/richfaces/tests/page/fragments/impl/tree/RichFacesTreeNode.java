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
package org.richfaces.tests.page.fragments.impl.tree;

import com.google.common.base.Predicate;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.utils.Actions;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;

public class RichFacesTreeNode extends RichFacesTree implements Tree.TreeNode {

    @FindByJQuery("> .rf-trn")
    private WebElement infoElement;

    @FindByJQuery("> .rf-trn > .rf-trn-hnd")
    private WebElement handleElement;

    @FindByJQuery("> .rf-trn > .rf-trn-hnd-ldn-fct")
    private WebElement handleLoadingElement;

    @FindByJQuery("> .rf-trn > .rf-trn-cnt")
    private WebElement containerElement;

    @FindByJQuery("> .rf-trn > .rf-trn-cnt > .rf-trn-ico:visible")
    private WebElement iconElement;
    @FindByJQuery("> .rf-trn > .rf-trn-cnt > .rf-trn-lbl")
    private WebElement labelElement;

    @Drone
    private WebDriver driver;

    private final AdvancedNodeInteractions interactions = new AdvancedNodeInteractionsImpl();

    @Override
    public AdvancedNodeInteractions advanced() {
        return interactions;
    }

    @Override
    protected int getIndexOfPickedElement(ChoicePicker picker) {
        // because the treeNode has an extra element at first index, we have to decrease the index by 1
        return super.getIndexOfPickedElement(picker) - 1;
    }

    public class AdvancedNodeInteractionsImpl extends AdvancedTreeInteractionsImpl implements AdvancedNodeInteractions {

        @Override
        public TreeNode collapse() {
            if (!isCollapsed()) {
                if (isToggleByHandle()) {
                    getHandleElement().click();
                } else {
                    new Actions(driver).triggerEventByWD(getToggleNodeEvent(), getLabelElement()).perform();
                }
            }
            waitUntilNodeIsCollapsed();
            return RichFacesTreeNode.this;
        }

        @Override
        public TreeNode expand() {
            if (!isExpanded()) {
                if (isToggleByHandle()) {
                    getHandleElement().click();
                } else {
                    new Actions(driver).triggerEventByWD(getToggleNodeEvent(), getLabelElement()).perform();
                }
            }
            waitUntilNodeIsExpanded();
            return RichFacesTreeNode.this;
        }

        @Override
        public WebElement getContainerElement() {
            return containerElement;
        }

        @Override
        public WebElement getHandleElement() {
            return handleElement;
        }

        @Override
        public WebElement getHandleLoadingElement() {
            return handleLoadingElement;
        }

        @Override
        public WebElement getIconElement() {
            return iconElement;
        }

        @Override
        public WebElement getLabelElement() {
            return labelElement;
        }

        @Override
        public WebElement getNodeInfoElement() {
            return infoElement;
        }

        @Override
        public boolean isCollapsed() {
            return getRootElement().getAttribute("class").contains("rf-tr-nd-colps")
                && getHandleElement().getAttribute("class").contains("rf-trn-hnd-colps")
                && getIconElement().getAttribute("class").contains("rf-trn-ico-colps");
        }

        @Override
        public boolean isExpanded() {
            return getRootElement().getAttribute("class").contains("rf-tr-nd-exp")
                && getHandleElement().getAttribute("class").contains("rf-trn-hnd-exp")
                && getIconElement().getAttribute("class").contains("rf-trn-ico-exp");
        }

        @Override
        public boolean isLeaf() {
            return getRootElement().getAttribute("class").contains("rf-tr-nd-lf")
                && getHandleElement().getAttribute("class").contains("rf-trn-hnd-lf")
                && getIconElement().getAttribute("class").contains("rf-trn-ico-lf");
        }

        @Override
        public boolean isSelected() {
            return getContainerElement().getAttribute("class").contains("rf-trn-sel");
        }

        @Override
        public TreeNode select() {
            if (!isSelected()) {
                getLabelElement().click();
            }
            waitUntilNodeIsSelected();
            return RichFacesTreeNode.this;
        }

        @Override
        public void waitUntilNodeIsCollapsed() {
            Graphene.waitModel().withMessage("Waiting for node to be collapsed").until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    return isCollapsed();
                }
            });
        }

        @Override
        public void waitUntilNodeIsExpanded() {
            Graphene.waitModel().withMessage("Waiting for node to be expanded").until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    return isExpanded();
                }
            });
        }

        @Override
        public void waitUntilNodeIsNotSelected() {
            Graphene.waitModel().withMessage("Waiting for node to be not selected").until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    return !isSelected();
                }
            });
        }

        @Override
        public void waitUntilNodeIsSelected() {
            Graphene.waitModel().withMessage("Waiting for node to be selected").until(new Predicate<WebDriver>() {

                @Override
                public boolean apply(WebDriver input) {
                    return isSelected();
                }
            });
        }
    }
}
