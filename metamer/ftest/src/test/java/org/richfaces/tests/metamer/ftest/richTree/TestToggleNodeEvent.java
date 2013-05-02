/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richTree;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.treeAttributes;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestToggleNodeEvent extends AbstractWebDriverTest {

    @Inject
    @Use(empty = true)
    MouseEvent toggleReactEvent;
    @Page
    MetamerPage page;
    //
    @FindBy(css = "div[id*=':richTree:1:']")
    TreeNode testedNode;
    //
    private static final long NODE_TOGGLE_WAIT_TIME  = 500;//ms

    public enum MouseEvent {

        MOUSEOVER("mouseover"),
        MOUSEOUT("mouseout"),
        MOUSEUP("mouseup"),
        MOUSEDOWN("mousedown");
        private final String value;

        private MouseEvent(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private void fireEvent(boolean fireOnHandle, MouseEvent firedEvent) {
        if (fireOnHandle) {
            testedNode.fireEventOnHandle(firedEvent);
        } else {
            testedNode.fireEventOnLabel(firedEvent);
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/simpleSwingTreeNode.xhtml");
    }

    private void testToggleNodeEvent(boolean fireOnHandle) {
        treeAttributes.set(TreeAttributes.selectionType, "client");
        treeAttributes.set(TreeAttributes.toggleType, "client");
        for (MouseEvent firedEvent : MouseEvent.values()) {
            treeAttributes.set(TreeAttributes.toggleNodeEvent, toggleReactEvent.getValue());
            boolean correctEvent = (toggleReactEvent.equals(firedEvent) ? Boolean.TRUE : Boolean.FALSE);
            fireEvent(fireOnHandle, firedEvent);
            if (correctEvent) {
                Graphene.waitGui().withMessage("The node did not expand.").until(testedNode.isExpandedCondition());
                fireEvent(fireOnHandle, firedEvent);
                Graphene.waitGui().withMessage("The node did not collapse.").until(testedNode.isCollapsedCondition());
            } else {
                waiting(NODE_TOGGLE_WAIT_TIME);
                if (testedNode.isExpanded()) {
                    Assert.fail("The node shouldn't expand.");
                }
            }
        }
    }

    @Test
    @Use(field = "toggleReactEvent", enumeration = true)
    public void testToggleNodeEventOnHandle() {
        testToggleNodeEvent(Boolean.TRUE);
    }

    @Test
    @Use(field = "toggleReactEvent", enumeration = true)
    public void testToggleNodeEventOnLabel() {
        testToggleNodeEvent(Boolean.FALSE);
    }

    public class TreeNode {

        @Root
        WebElement root;
        @FindBy(css = "div > span > span.rf-trn-lbl")
        WebElement label;
        @FindBy(css = "div > span.rf-trn-hnd")
        WebElement handle;

        public void fireEventOnHandle(MouseEvent event) {
            Utils.triggerJQ(event.getValue(), handle);
        }

        public void fireEventOnLabel(MouseEvent event) {
            Utils.triggerJQ(event.getValue(), label);
        }

        public boolean isCollapsed() {
            return isCollapsedCondition().apply(driver);
        }

        public ExpectedCondition<Boolean> isCollapsedCondition() {
            return Graphene.element(root).attribute("class").contains("rf-tr-nd-colps");
        }

        public boolean isExpanded() {
            return isExpandedCondition().apply(driver);
        }

        public ExpectedCondition<Boolean> isExpandedCondition() {
            return Graphene.element(root).attribute("class").contains("rf-tr-nd-exp");
        }
    }
}
