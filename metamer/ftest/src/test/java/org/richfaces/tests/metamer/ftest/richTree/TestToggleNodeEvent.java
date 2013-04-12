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
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;
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
    TreeSimplePage page;

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

    private RichFacesTreeNode getTestedNode() {
        // left second node as tested since it was originally written this way
        return page.tree.getNodes().get(1);
    }

    private void fireEvent(boolean fireOnHandle, MouseEvent firedEvent) {
        if (fireOnHandle) {
            fireEventOnHandle(getTestedNode().getHandle().root, firedEvent);
        } else {
            fireEventOnLabel(getTestedNode().getNodeLabel(), firedEvent);
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
                Graphene.waitGui().withMessage("The node did not expand.").until(getTestedNode().isExpandedCondition());
                fireEvent(fireOnHandle, firedEvent);
                Graphene.waitGui().withMessage("The node did not collapse.").until(getTestedNode().isCollapsedCondition());
            } else {
                waiting(NODE_TOGGLE_WAIT_TIME);
                if (getTestedNode().isExpanded()) {
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

    public void fireEventOnHandle(WebElement handle, MouseEvent event) {
            Utils.triggerJQ(event.getValue(), handle);
    }

    public void fireEventOnLabel(WebElement label, MouseEvent event) {
        Utils.triggerJQ(event.getValue(), label);
    }

}
