/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.Graphene;
import org.richfaces.component.SwitchType;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.tree.Tree.TreeNode;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.testng.annotations.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestTreeAttributes extends AbstractTreeTest {

    @Test
    public void testData() {
        testData(selectFirstNodeAjaxAction);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testDataRFTreeNode() {
        testData();
    }

    @Test
    public void testDir() {
        testDir(tree.advanced().getRootElement());
    }

    @Test
    public void testExecute() {
        treeAttributes.set(TreeAttributes.execute, "executeChecker @this");
        selectFirstNodeAjaxAction.perform();
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testExecuteRFTreeNode() {
        testExecute();
    }

    @Test
    @Templates("plain")
    public void testHandleClass() {
        TreeNode node = getGuarded(tree, SwitchType.ajax).expandNode(0);
        testStyleClass(node.advanced().getHandleElement(), BasicAttributes.handleClass);
        testStyleClass(getGuarded(node, SwitchType.ajax).expandNode(0).advanced().getHandleElement(),
            BasicAttributes.handleClass);
    }

    @Test
    @Templates("plain")
    public void testIconClass() {
        TreeNode node = getGuarded(tree, SwitchType.ajax).expandNode(0);
        testStyleClass(node.advanced().getIconElement(), BasicAttributes.iconClass);
        testStyleClass(getGuarded(node, SwitchType.ajax).expandNode(0).advanced().getIconElement(), BasicAttributes.iconClass);
    }

    @Test
    @Templates("plain")
    public void testIconCollapsed() {
        treeAttributes.set(TreeAttributes.iconCollapsed, IMAGE_URL);
        TreeNode node = tree.advanced().getFirstNode();
        assertTrue(node.advanced().getIconElement().getAttribute("src").endsWith(IMAGE_URL));
        String attribute = Optional.fromNullable(
            getGuarded(node.advanced(), SwitchType.ajax).expand().advanced().getIconElement().getAttribute("src")).or("");
        assertFalse(attribute.endsWith(IMAGE_URL));
        assertTrue(getGuarded(node.advanced(), SwitchType.ajax).collapse().advanced().getIconElement().getAttribute("src")
            .endsWith(IMAGE_URL));
    }

    @Test
    @Templates("plain")
    public void testIconExpanded() {
        treeAttributes.set(TreeAttributes.iconExpanded, IMAGE_URL);
        TreeNode node = getGuarded(tree, SwitchType.ajax).expandNode(0);
        assertTrue(node.advanced().getIconElement().getAttribute("src").endsWith(IMAGE_URL));
        String attribute = Optional.fromNullable(
            getGuarded(node.advanced(), SwitchType.ajax).collapse().advanced().getIconElement().getAttribute("src")).or("");
        assertFalse(attribute.endsWith(IMAGE_URL));
        assertTrue(getGuarded(node.advanced(), SwitchType.ajax).expand().expandNode(0).advanced().getIconElement()
            .getAttribute("src").endsWith(IMAGE_URL));
    }

    @Test
    @Templates("plain")
    public void testIconLeaf() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.client);
        treeAttributes.set(TreeAttributes.iconLeaf, IMAGE_URL);
        TreeNode leaf = tree.expandNode(0).expandNode(0).advanced().getFirstNode();
        assertTrue(leaf.advanced().isLeaf());
        assertTrue(leaf.advanced().getIconElement().getAttribute("src").endsWith(IMAGE_URL));
    }

    @Test
    public void testImmediate() {
        treeAttributes.set(TreeAttributes.immediate, Boolean.FALSE);
        expandFirstNodeAjaxAction.perform();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "tree toggle listener invoked");

        treeAttributes.set(TreeAttributes.immediate, Boolean.TRUE);
        collapseFirstNodeAjaxAction.perform();
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "tree toggle listener invoked");
    }

    @Test
    @Templates("plain")
    public void testLabelClass() {
        testStyleClass(tree.advanced().getFirstNode().advanced().getLabelElement(), BasicAttributes.labelClass);
    }

    @Test
    @Templates("plain")
    public void testLang() {
        testAttributeLang(tree.advanced().getRootElement());
    }

    @Test
    public void testLimitRender() {
        treeAttributes.set(TreeAttributes.limitRender, true);
        treeAttributes.set(TreeAttributes.render, "@this renderChecker");
        String renderCheckerText = page.getRenderCheckerOutputElement().getText();
        selectFirstNodeAjaxAction.perform();
        Graphene.waitAjax().until().element(page.getRenderCheckerOutputElement()).text().not().equalTo(renderCheckerText);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testLimitRenderRFTreeNode() {
        testLimitRender();
    }

    @Test
    @Templates("plain")
    public void testNodeClass() {
        testStyleClass(tree.advanced().getFirstNode().advanced().getNodeInfoElement(), BasicAttributes.nodeClass);
    }

    @Test
    public void testOnbeforedomupdate() {
        testFireEvent(treeAttributes, TreeAttributes.onbeforedomupdate, selectFirstNodeAjaxAction);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testOnbeforedomupdateRFTreeNode() {
        testOnbeforedomupdate();
    }

    @Test
    public void testOnbeforenodetoggle() {
        testFireEvent(treeAttributes, TreeAttributes.onbeforenodetoggle, expandFirstNodeAjaxAction);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testOnbeforenodetoggleRFTreeNode() {
        testOnbeforenodetoggle();
    }

    @Test
    public void testOnbeforeselectionchange() {
        testFireEvent(treeAttributes, TreeAttributes.onbeforeselectionchange, selectFirstNodeAjaxAction);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testOnbeforeselectionchangeRFTreeNode() {
        testOnbeforeselectionchange();
    }

    @Test
    public void testOnbegin() {
        testFireEvent(treeAttributes, TreeAttributes.onbegin, selectFirstNodeAjaxAction);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testOnbeginRFTreeNode() {
        testOnbegin();
    }

    @Test
    @Templates("plain")
    public void testOnclick() {
        testFireEvent(treeAttributes, TreeAttributes.onclick, expandFirstNodeAjaxAction);
    }

    @Test
    public void testOncomplete() {
        testFireEvent(treeAttributes, TreeAttributes.oncomplete, selectFirstNodeAjaxAction);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testOncompleteRFTreeNode() {
        testOncomplete();
    }

    @Test
    @Templates("plain")
    public void testOndblclick() {
        testFireEvent(treeAttributes, TreeAttributes.ondblclick,
            new Actions(driver).doubleClick(tree.advanced().getRootElement()).build());
    }

    @Test
    @Templates("plain")
    public void testOnkeydown() {
        testFireEventWithJS(tree.advanced().getRootElement(), treeAttributes, TreeAttributes.onkeydown);
    }

    @Test
    @Templates("plain")
    public void testOnkeypress() {
        testFireEventWithJS(tree.advanced().getRootElement(), treeAttributes, TreeAttributes.onkeypress);
    }

    @Test
    @Templates("plain")
    public void testOnkeyup() {
        testFireEventWithJS(tree.advanced().getRootElement(), treeAttributes, TreeAttributes.onkeyup);
    }

    @Test
    @Templates("plain")
    public void testOnmousedown() {
        testFireEvent(treeAttributes, TreeAttributes.onmousedown,
            new Actions(driver).triggerEventByWD(Event.CLICK, tree.advanced().getRootElement()).build());
    }

    @Test
    @Templates("plain")
    public void testOnmousemove() {
        testFireEvent(treeAttributes, TreeAttributes.onmousemove,
            new Actions(driver).triggerEventByWD(Event.MOUSEMOVE, tree.advanced().getRootElement()).build());
    }

    @Test
    @Templates("plain")
    public void testOnmouseout() {
        testFireEvent(treeAttributes, TreeAttributes.onmouseout,
            new Actions(driver).triggerEventByWD(Event.MOUSEOUT, tree.advanced().getRootElement()).build());
    }

    @Test
    @Templates("plain")
    public void testOnmouseover() {
        new Actions(driver).moveToElement(page.getRequestTimeElement()).perform();
        testFireEvent(treeAttributes, TreeAttributes.onmouseover,
            new Actions(driver).triggerEventByWD(Event.MOUSEOVER, tree.advanced().getRootElement()).build());
    }

    @Test
    @Templates("plain")
    public void testOnmouseup() {
        testFireEvent(treeAttributes, TreeAttributes.onmouseup,
            new Actions(driver).triggerEventByWD(Event.CLICK, tree.advanced().getRootElement()).build());
    }

    @Test
    public void testOnnodetoggle() {
        testFireEvent(treeAttributes, TreeAttributes.onnodetoggle, expandFirstNodeAjaxAction);
    }

    @Test
    public void testOnselectionchange() {
        testFireEvent(treeAttributes, TreeAttributes.onselectionchange, selectFirstNodeAjaxAction);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testOnselectionChangeRFTreeNode() {
        testOnselectionchange();
    }

    @Test
    public void testRender() {
        treeAttributes.set(TreeAttributes.render, "renderChecker");
        String renderCheckerText = page.getRenderCheckerOutputElement().getText();
        selectFirstNodeAjaxAction.perform();
        Graphene.waitAjax().until().element(page.getRenderCheckerOutputElement()).text().not().equalTo(renderCheckerText);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testRenderRFTreeNode() {
        testRender();
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        treeAttributes.set(TreeAttributes.rendered, Boolean.TRUE);
        assertVisible(tree.advanced().getRootElement(), "Tree should be visible");
        treeAttributes.set(TreeAttributes.rendered, Boolean.FALSE);
        assertNotVisible(tree.advanced().getRootElement(), "Tree should not be visible");
    }

    @Test
    @Use(field = "sample", strings = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates(exclude = "a4jRegion")
    public void testSelectionClientSideEventsOrder() {
        String[] events = new String[] { "beforeselectionchange", "begin", "beforedomupdate", "complete", "selectionchange" };
        testRequestEventsBefore(events);
        selectFirstNodeAjaxAction.perform();
        testRequestEventsAfter(events);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Use(field = "sample", strings = { "simpleRichFacesTreeNode" })
    @Templates(exclude = "a4jRegion")
    public void testSelectionClientSideEventsOrderWithSimpleTreeNode() {
        testSelectionClientSideEventsOrder();
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11319")
    @Templates(value = "a4jRegion")
    public void testSelectionClientSideEventsOrderInRegion() {
        testSelectionClientSideEventsOrder();
    }

    @Test
    public void testSelectionType() {
        treeAttributes.set(TreeAttributes.selectionType, SwitchType.ajax);
        selectFirstNodeAjaxAction.perform();

        treeAttributes.set(TreeAttributes.selectionType, SwitchType.client);
        Graphene.guardNoRequest(tree).selectNode(1);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testSelectionTypeRFTreeNode() {
        testSelectionType();
    }

    @Test
    public void testStatus() {
        testStatus(selectFirstNodeAjaxAction);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testStatusRFTreeNode() {
        testStatus();
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        testStyle(tree.advanced().getRootElement());
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        testStyleClass(tree.advanced().getRootElement());
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        testStyleClass(tree.advanced().getRootElement());
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-10265")
    public void testToggleClientSideEventsOrder() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.ajax);
        String[] events = new String[] { "beforenodetoggle", "begin", "beforedomupdate", "complete", "nodetoggle" };
        testRequestEventsBefore(events);
        expandFirstNodeAjaxAction.perform();
        testRequestEventsAfter(events);
    }

    @Test
    public void testToggleNodeEvent() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.ajax);
        List<Event> testedEvents = Lists.newArrayList(Event.CLICK, Event.CONTEXTCLICK, Event.DBLCLICK);
        for (boolean toggleByHandle : new boolean[] { true, false }) {
            tree.advanced().setupToggleByHandle(toggleByHandle);
            for (Event event : testedEvents) {
                treeAttributes.set(TreeAttributes.toggleNodeEvent, event.toString());
                if (!toggleByHandle) {
                    tree.advanced().setupToggleNodeEvent(event);
                }
                (tree.advanced().getFirstNode().advanced().isExpanded() ? collapseFirstNodeAjaxAction
                    : expandFirstNodeAjaxAction).perform();
            }
        }
    }

    @Test
    public void testToggleType() {
        treeAttributes.set(TreeAttributes.toggleType, SwitchType.ajax);
        getGuarded(tree, SwitchType.ajax).expandNode(0);

        treeAttributes.set(TreeAttributes.toggleType, SwitchType.client);
        getGuarded(tree, SwitchType.client).expandNode(1);

        treeAttributes.set(TreeAttributes.toggleType, SwitchType.server);
        getGuarded(tree, SwitchType.server).expandNode(2);
    }

    @Test
    @Use(field = "sample", strings = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testTreeSelectionChangeListener() {
        selectFirstNodeAjaxAction.perform();
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "selection change listener invoked");
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Use(field = "sample", strings = { "simpleRichFacesTreeNode" })
    public void testTreeSelectionChangeListenerWithSimpleTreeNode() {
        testTreeSelectionChangeListener();
    }

    @Test
    public void testTreeToggleListener() {
        expandFirstNodeAjaxAction.perform();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "tree toggle listener invoked");
    }
}
