/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richTreeNode;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.STRINGS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.SwitchType;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.richTree.AbstractTreeTest;
import org.richfaces.tests.metamer.ftest.richTree.TreeAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Optional;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTreeNodeAttributes extends AbstractTreeTest {

    @FindBy(css = "[id$='collapseAll']")
    private WebElement collapseAllButton;
    @FindBy(css = "[id$='expandAll']")
    private WebElement expandAllButton;

    private TreeNode getFirstNode() {
        return tree.advanced().getFirstNode();
    }

    private TreeNode getFirstNodesFirstChild() {
        return getFirstNode().advanced().expand().advanced().getFirstNode();
    }

    private String getImageSrcFromTreeNode(TreeNode node) {
        return Optional.fromNullable(node.advanced().getIconElement().getAttribute("src")).or("");
    }

    @BeforeMethod
    public void prepareTree() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.client);
    }

    @Test
    @CoversAttributes("dir")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testDir() {
        testHTMLAttribute(getFirstNode().advanced().getNodeInfoElement(), firstNodeAttributes, TreeNodeAttributes.dir, "rtl");
        testHTMLAttribute(getFirstNode().advanced().getNodeInfoElement(), firstNodeAttributes, TreeNodeAttributes.dir, "ltr");
    }

    @Test
    @CoversAttributes("expanded")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testExpanded() {
        assertEquals(tree.advanced().getNodesExpanded().size(), 0);
        assertEquals(tree.advanced().getNodesCollapsed().size(), 4);
        Graphene.guardAjax(expandAllButton).click();// sets @expanded of all nodes to true
        assertEquals(tree.advanced().getNodesExpanded().size(), 4);
        assertEquals(tree.advanced().getNodesCollapsed().size(), 0);
        Graphene.guardAjax(collapseAllButton).click();// sets @expanded of all nodes to false
        assertEquals(tree.advanced().getNodesExpanded().size(), 0);
        assertEquals(tree.advanced().getNodesCollapsed().size(), 4);
    }

    @Test
    @CoversAttributes("handleClass")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testHandleClass() {
        testHTMLAttribute(getFirstNode().advanced().getHandleElement(), firstNodeAttributes, TreeNodeAttributes.handleClass,
            "metamer-ftest-class");
        // check child node
        assertFalse(getFirstNodesFirstChild().advanced().getHandleElement().getAttribute("class")
            .contains("metamer-ftest-class"));
    }

    @Test
    @CoversAttributes("iconClass")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testIconClass() {
        testHTMLAttribute(getFirstNode().advanced().getIconElement(), firstNodeAttributes, TreeNodeAttributes.iconClass,
            "metamer-ftest-class");
        // check child node
        assertFalse(getFirstNodesFirstChild().advanced().getHandleElement().getAttribute("class")
            .contains("metamer-ftest-class"));
    }

    @Test(groups = "smoke")
    @CoversAttributes("iconCollapsed")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testIconCollapsed() {
        firstNodeAttributes.set(TreeNodeAttributes.iconCollapsed, IMAGE_URL);
        String attribute = getImageSrcFromTreeNode(getFirstNode());
        assertTrue(attribute.endsWith(IMAGE_URL));
        // expand
        attribute = getImageSrcFromTreeNode(getFirstNode().advanced().expand());
        assertFalse(attribute.endsWith(IMAGE_URL));
        // collapse
        attribute = getImageSrcFromTreeNode(getFirstNode().advanced().collapse());
        assertTrue(attribute.endsWith(IMAGE_URL));

        // check child node
        attribute = getImageSrcFromTreeNode(getFirstNodesFirstChild().advanced().expand());
        assertFalse(attribute.endsWith(IMAGE_URL));
        attribute = getImageSrcFromTreeNode(getFirstNodesFirstChild().advanced().collapse());
        assertFalse(attribute.endsWith(IMAGE_URL));
    }

    @Test(groups = "smoke")
    @CoversAttributes("iconExpanded")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testIconExpanded() {
        firstNodeAttributes.set(TreeNodeAttributes.iconExpanded, IMAGE_URL);
        String attribute = getImageSrcFromTreeNode(getFirstNode());
        assertFalse(attribute.endsWith(IMAGE_URL));
        // expand
        attribute = getImageSrcFromTreeNode(getFirstNode().advanced().expand());
        assertTrue(attribute.endsWith(IMAGE_URL));

        // check child node
        attribute = getImageSrcFromTreeNode(getFirstNodesFirstChild().advanced().expand());
        assertFalse(attribute.endsWith(IMAGE_URL));
        attribute = getImageSrcFromTreeNode(getFirstNodesFirstChild().advanced().collapse());
        assertFalse(attribute.endsWith(IMAGE_URL));
    }

    @Test
    @CoversAttributes("iconLeaf")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testIconLeaf() {
        lastNodeAttributes.set(TreeNodeAttributes.iconLeaf, IMAGE_URL);
        TreeNode leaf = tree.expandNode(0).expandNode(0).advanced().getFirstNode();
        assertTrue(leaf.advanced().isLeaf());
        assertTrue(leaf.advanced().getIconElement().getAttribute("src").endsWith(IMAGE_URL));
    }

    @Test(groups = "smoke")
    @CoversAttributes("immediate")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testImmediate() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.ajax);

        firstNodeAttributes.set(TreeNodeAttributes.immediate, Boolean.FALSE);
        expandFirstNodeAjaxAction.perform();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "node toggle listener invoked");

        firstNodeAttributes.set(TreeNodeAttributes.immediate, Boolean.TRUE);
        collapseFirstNodeAjaxAction.perform();
        getMetamerPage().assertListener(PhaseId.APPLY_REQUEST_VALUES, "node toggle listener invoked");
    }

    @Test
    @CoversAttributes("labelClass")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testLabelClass() {
        testHTMLAttribute(getFirstNode().advanced().getLabelElement(), firstNodeAttributes, TreeNodeAttributes.labelClass,
            "metamer-ftest-class");
        // check child node
        assertFalse(getFirstNodesFirstChild().advanced().getLabelElement().getAttribute("class")
            .contains("metamer-ftest-class"));
    }

    @Test
    @CoversAttributes("lang")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testLang() {
        testHTMLAttribute(getFirstNode().advanced().getNodeInfoElement(), firstNodeAttributes, TreeNodeAttributes.lang);
    }

    @Test
    @CoversAttributes("onbeforetoggle")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testOnbeforetoggle() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.ajax);
        testFireEvent(firstNodeAttributes, TreeNodeAttributes.onbeforetoggle, expandFirstNodeAjaxAction);
    }

    @Test
    @CoversAttributes("onclick")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testOnclick() {
        treeAttributes.set(TreeAttributes.toggleType, "client");// prevent ViewExpiredException
        testFireEvent(firstNodeAttributes, TreeNodeAttributes.onclick,
            new Actions(driver).triggerEventByWD(Event.CLICK, getFirstNode().advanced().getLabelElement()).build());
    }

    @Test
    @CoversAttributes("ondblclick")
    @Templates("plain")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testOndblclick() {
        treeAttributes.set(TreeAttributes.toggleType, "client");// prevent ViewExpiredException
        testFireEvent(firstNodeAttributes, TreeNodeAttributes.ondblclick,
            new Actions(driver).doubleClick(getFirstNode().advanced().getLabelElement()).build());
    }

    @Test
    @CoversAttributes("onkeydown")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testOnkeydown() {
        testFireEventWithJS(getFirstNode().advanced().getLabelElement(), firstNodeAttributes, TreeNodeAttributes.onkeydown);
    }

    @Test
    @CoversAttributes("onkeypress")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testOnkeypress() {
        testFireEventWithJS(getFirstNode().advanced().getLabelElement(), firstNodeAttributes, TreeNodeAttributes.onkeypress);
    }

    @Test
    @CoversAttributes("onkeyup")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testOnkeyup() {
        testFireEventWithJS(getFirstNode().advanced().getLabelElement(), firstNodeAttributes, TreeNodeAttributes.onkeyup);
    }

    @Test
    @CoversAttributes("onmousedown")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testOnmousedown() {
        testFireEvent(firstNodeAttributes, TreeNodeAttributes.onmousedown,
            new Actions(driver).triggerEventByWD(Event.CLICK, getFirstNode().advanced().getLabelElement()).build());
    }

    @Test
    @CoversAttributes("onmousemove")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testOnmousemove() {
        testFireEvent(firstNodeAttributes, TreeNodeAttributes.onmousemove,
            new Actions(driver).triggerEventByWD(Event.MOUSEMOVE, getFirstNode().advanced().getLabelElement()).build());
    }

    @Test
    @CoversAttributes("onmouseout")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testOnmouseout() {
        new Actions(driver).moveToElement(getFirstNode().advanced().getLabelElement()).build().perform();
        testFireEvent(firstNodeAttributes, TreeNodeAttributes.onmouseout,
            new Actions(driver).triggerEventByWD(Event.MOUSEOUT, getFirstNode().advanced().getLabelElement()).build());
    }

    @Test
    @CoversAttributes("onmouseover")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testOnmouseover() {
        // the click on requestTimeElement is a workaround to make test pass automatically
        // without this simpleRichFacesTreeDataModel test was failing (no event)
        testFireEvent(
            firstNodeAttributes,
            TreeNodeAttributes.onmouseover,
            new Actions(driver).moveToElement(getMetamerPage().getRequestTimeElement()).click()
            .moveToElement(getFirstNode().advanced().getLabelElement()).build());
    }

    @Test
    @CoversAttributes("onmouseup")
    @Templates("plain")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testOnmouseup() {
        testFireEvent(firstNodeAttributes, TreeNodeAttributes.onmouseup,
            new Actions(driver).triggerEventByWD(Event.CLICK, getFirstNode().advanced().getLabelElement()).build());
    }

    @Test
    @CoversAttributes("ontoggle")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testOntoggle() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.ajax);
        testFireEvent(firstNodeAttributes, TreeNodeAttributes.ontoggle, expandFirstNodeAjaxAction);
    }

    @Test
    @CoversAttributes("rendered")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates("plain")
    public void testRendered() {
        firstNodeAttributes.set(TreeNodeAttributes.rendered, Boolean.TRUE);
        assertVisible(getFirstNode().advanced().getRootElement(), "Tree node should be visible");
        firstNodeAttributes.set(TreeNodeAttributes.rendered, Boolean.FALSE);
        assertEquals(tree.advanced().getNodes().size(), 0, "Tree should not be visible");
    }

    @Test
    @CoversAttributes("style")
    @Templates("plain")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testStyle() {
        testHTMLAttribute(getFirstNode().advanced().getNodeInfoElement(), firstNodeAttributes, TreeNodeAttributes.style,
            "background-color: yellow");
    }

    @Test
    @CoversAttributes("styleClass")
    @Templates("plain")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testStyleClass() {
        testHTMLAttribute(getFirstNode().advanced().getNodeInfoElement(), firstNodeAttributes, TreeNodeAttributes.styleClass,
            "metamer-ftest-class");
    }

    @Test
    @CoversAttributes("title")
    @Templates("plain")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testTitle() {
        testHTMLAttribute(getFirstNode().advanced().getNodeInfoElement(), firstNodeAttributes, TreeNodeAttributes.title);
    }

    @Test
    @CoversAttributes("toggleListener")
    @UseWithField(field = "sample", valuesFrom = STRINGS, value = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testToggleListener() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.ajax);
        expandFirstNodeAjaxAction.perform();
        getMetamerPage().assertListener(PhaseId.PROCESS_VALIDATIONS, "node toggle listener invoked");
        Graphene.guardAjax(tree.advanced().getNodes().get(0)).expandNode(0);
        getMetamerPage().assertNoListener("node toggle listener invoked");
    }
}
