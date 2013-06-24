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

import static org.jboss.arquillian.ajocado.dom.Event.CLICK;
import static org.jboss.arquillian.ajocado.dom.Event.DBLCLICK;
import static org.jboss.arquillian.ajocado.dom.Event.KEYDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.KEYPRESS;
import static org.jboss.arquillian.ajocado.dom.Event.KEYUP;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEDOWN;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEMOVE;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOUT;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEOVER;
import static org.jboss.arquillian.ajocado.dom.Event.MOUSEUP;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.nodeClass;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.data;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.execute;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.iconCollapsed;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.iconExpanded;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.iconLeaf;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.limitRender;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.render;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.status;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.treeAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebDriver;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNodeIcon;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23125 $
 */
@Use(field = "sample", value = "swingTreeNode")
public class TestTreeSimple extends AbstractTestTree {

    private static final String IMAGE_URL = "/resources/images/loading.gif";
    private static final String testClassValue = "metamer-ftest-class";

    @Inject
    @Use(empty = true)
    Event eventToFire;
    Event[] eventsToFire = new Event[] { MOUSEDOWN, MOUSEUP, MOUSEOVER, MOUSEOUT };
    @Inject
    @Use(empty = true)
    Event domEvent;
    Event[] domEvents = { CLICK, DBLCLICK, KEYDOWN, KEYPRESS, KEYUP, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP };

    @Page
    TreeSimplePage page;

    RichFacesTreeNode treeNode;

    @Test
    public void testData() {
        treeAttributes.set(data, "RichFaces 4");
        treeAttributes.set(oncomplete, "data = event.data");

        String requestTime = page.getRequestTimeElement().getText();
        page.tree.getNodes().get(1).select();
        Graphene.waitGui().until().element(page.getRequestTimeElement()).text().not().equalTo(requestTime);

        assertEquals(executeJS("return window.data;"), "RichFaces 4");
    }

    @Test
    @Templates(value = "plain")
    public void testDir() {
        super.testDir(page.tree.getRoot());
    }

    @Test
    public void testExecute() {
        treeAttributes.set(execute, "executeChecker @this");
        page.tree.getNodes().get(1).select();
        page.assertListener(PhaseId.UPDATE_MODEL_VALUES, "executeChecker");
    }

    @Test
    @Templates(value = "plain")
    public void testHandleClass() {
        page.expandAll();

        treeAttributes.set(TreeAttributes.handleClass, testClassValue);
        String styleAttr = page.tree.getNodes().get(0).getHandle().getRoot().getAttribute("class");
        assertTrue(styleAttr.contains(testClassValue), "Attribute handleClass should contain \"" + testClassValue
            + "\"");
    }

    @Test
    @Templates(value = "plain")
    public void testIconClass() {
        page.expandAll();

        treeAttributes.set(TreeAttributes.iconClass, testClassValue);
        String styleAttr = page.tree.getNodes().get(0).getIcon().getRoot().getAttribute("class");
        assertTrue(styleAttr.contains(testClassValue), "Attribute iconClass should contain \"" + testClassValue + "\"");
    }

    @Test
    @Templates(value = "plain")
    public void testLabelClass() {
        page.expandAll();

        treeAttributes.set(TreeAttributes.labelClass, testClassValue);
        String styleAttr = page.tree.getNodes().get(0).getNodeLabel().getAttribute("class");
        assertTrue(styleAttr.contains(testClassValue), "Attribute labelClass should contain \"" + testClassValue + "\"");
    }

    @Test
    @Templates(value = "plain")
    public void testNodeClass() {
        page.expandAll();
        super.testStyleClass(page.tree.getNodes().get(0).getNodeItself(), nodeClass);
    }

    @Test
    @Templates(value = "plain")
    public void testIconCollapsed() {
        treeAttributes.set(iconCollapsed, IMAGE_URL);

        for (int i = 0; i < 3; i++) {
            List<RichFacesTreeNode> nodes = page.tree.getNodes();
            int[] withAndwithout = { 0, 0 };

            for (RichFacesTreeNode node : nodes) {
                int[] currentLevel = getCollapsedIconCount(withAndwithout[0], withAndwithout[1], node);
                withAndwithout[0] += currentLevel[0];
                withAndwithout[1] += currentLevel[1];
            }

            assertEquals(withAndwithout[0] > 0, i < 2);
            assertEquals(withAndwithout[1] > 0, i > 0);

            expandLevel(i);
        }
    }

    private int[] getCollapsedIconCount(int with, int without, RichFacesTreeNode node) {

        int[] wout = { 0, 0 };

        String srcAttrVal = node.getIcon().getRoot().getAttribute("src");
        if (null != srcAttrVal && srcAttrVal.contains(IMAGE_URL)) {
            wout[0] += 1;
        } else {
            wout[1] += 1;
        }

        List<RichFacesTreeNode> x = node.getNodes();
        for (RichFacesTreeNode node2 : x) {
            int[] wout2 = getCollapsedIconCount(with, without, node2);
            wout[0] += wout2[0];
            wout[1] += wout2[1];
        }
        return wout;
    }

    @Test
    @Templates(value = "plain")
    public void testIconExpanded() {
        treeAttributes.set(iconExpanded, IMAGE_URL);

        for (int i = 0; i < 3; i++) {
            List<RichFacesTreeNode> nodes = page.tree.getNodes();
            int[] withAndwithout = { 0, 0 };

            for (RichFacesTreeNode node : nodes) {
                int[] currentLevel = getExpandedIconCount(withAndwithout[0], withAndwithout[1], node);
                withAndwithout[0] += currentLevel[0];
                withAndwithout[1] += currentLevel[1];
            }

            assertEquals(withAndwithout[0] > 0, i > 0);
            assertEquals(withAndwithout[1] > 0, i < 2);

            expandLevel(i);
        }
    }

    private int[] getExpandedIconCount(int with, int without, RichFacesTreeNode node) {

        int[] wout = { 0, 0 };

        String srcAttrVal = node.getIcon().getRoot().getAttribute("src");
        if (null != srcAttrVal && srcAttrVal.contains(IMAGE_URL)) {
            wout[0] += 1;
        } else {
            if (!node.getIcon().getRoot().getAttribute("class").contains(RichFacesTreeNodeIcon.CLASS_ICON_LEAF)) {
                // ignore leaf nodes
                wout[1] += 1;
            }
        }

        List<RichFacesTreeNode> x = node.getNodes();
        for (RichFacesTreeNode node2 : x) {
            int[] wout2 = getExpandedIconCount(with, without, node2);
            wout[0] += wout2[0];
            wout[1] += wout2[1];
        }
        return wout;
    }

    @Test
    @Templates(value = "plain")
    public void testIconLeaf() {
        treeAttributes.set(iconLeaf, IMAGE_URL);

        for (int i = 0; i < 3; i++) {
            List<RichFacesTreeNode> nodes = page.tree.getNodes();
            int[] withAndwithout = { 0, 0 };

            for (RichFacesTreeNode node : nodes) {
                int[] currentLevel = getLeafIconCount(withAndwithout[0], withAndwithout[1], node);
                withAndwithout[0] += currentLevel[0];
                withAndwithout[1] += currentLevel[1];
            }

            assertEquals(withAndwithout[0] > 0, i > 1, "Found: with the given URL " + withAndwithout[0] + ", i: " + i);
            assertEquals(withAndwithout[1] > 0, i < 2, "Found: without the given URL " + withAndwithout[1] + ", i: "
                + i);

            expandLevel(i);
        }
    }

    private int[] getLeafIconCount(int with, int without, RichFacesTreeNode node) {

        int[] wout = { 0, 0 };

        String srcAttrVal = node.getIcon().getRoot().getAttribute("src");
        if (null != srcAttrVal && srcAttrVal.contains(IMAGE_URL)) {
            wout[0] += 1;
        } else {
            if (!node.getIcon().getRoot().getAttribute("class").contains(RichFacesTreeNodeIcon.CLASS_ICON_EXPANDED)) {
                // ignore collapsed nodes
                wout[1] += 1;
            }
        }

        List<RichFacesTreeNode> x = node.getNodes();
        for (RichFacesTreeNode node2 : x) {
            int[] wout2 = getLeafIconCount(with, without, node2);
            wout[0] += wout2[0];
            wout[1] += wout2[1];
        }
        return wout;
    }

    @Test
    @Templates(value = "plain")
    public void testLang() {
        super.testAttributeLang(page.tree.getRoot());
    }

    @Test
    public void testLimitRender() {
        treeAttributes.set(render, "@this renderChecker");
        treeAttributes.set(limitRender, true);
        String renderChecker = page.getRenderCheckerOutputElement().getText();
        String requestTime = page.getRequestTimeElement().getText();
        page.tree.getNodes().get(0).select();
        Graphene.waitGui().until().element(page.getRenderCheckerOutputElement()).text().not().equalTo(renderChecker);
        assertEquals(page.getRequestTimeElement().getText(), requestTime);
    }

    @Test
    @Use(field = "sample", strings = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates(exclude = "a4jRegion")
    public void testSelectionClientSideEvents() {
        String[] events = new String[] { "beforeselectionchange", "begin", "beforedomupdate", "complete",
            "selectionchange" };
        testRequestEventsBefore(events);
        page.tree.getNodes().get(0).select();
        testRequestEventsAfter(events);
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Use(field = "sample", strings = { "simpleRichFacesTreeNode" })
    @Templates(exclude = "a4jRegion")
    public void testSelectionClientSideEventsWithSimpleTreeNode() {
        testSelectionClientSideEvents();
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11319")
    @Templates(value = "a4jRegion")
    public void testSelectionClientSideEventsInRegion() {
        String[] events = new String[] { "beforeselectionchange", "begin", "beforedomupdate", "complete",
            "selectionchange" };
        testRequestEventsBefore(events);
        page.tree.getNodes().get(0).select();
        testRequestEventsAfter(events);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-10265")
    public void testToggleClientSideEvents() {
        String[] events = new String[] { "beforenodetoggle", "begin", "beforedomupdate", "complete", "nodetoggle" };
        testRequestEventsBefore(events);
        page.tree.getNodes().get(0).expand();
        testRequestEventsAfter(events);
    }

    @Test
    @Use(field = "domEvent", value = "domEvents")
    public void testDomEvents() {
        testFireEvent(domEvent, page.tree.getRoot());
    }

    @Test
    public void testRender() {
        treeAttributes.set(render, "@this renderChecker");
        String renderChecker = page.getRenderCheckerOutputElement().getText();
        page.tree.getNodes().get(0).select();
        Graphene.waitGui().until().element(page.getRenderCheckerOutputElement()).text().not().equalTo(renderChecker);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        assertTrue(Graphene.element(page.tree.getRoot()).isPresent().apply(driver)
            && Graphene.element(page.tree.getRoot()).isVisible().apply(driver));
        treeAttributes.set(rendered, false);
        assertFalse(Graphene.element(page.tree.getRoot()).isPresent().apply(driver));
    }

    @Test
    public void testStatus() {
        treeAttributes.set(status, "statusChecker");
        String statusChecker = page.getStatusCheckerOutputElement().getText();
        page.tree.getNodes().get(0).select();
        Graphene.waitAjax().until().element(page.getStatusCheckerOutputElement()).text().not().equalTo(statusChecker);

        treeAttributes.reset(status);
        statusChecker = page.getStatusCheckerOutputElement().getText();
        page.tree.getNodes().get(0).select();
        page.tree.getNodes().get(0).isSelected();
        Graphene.waitAjax().until().element(page.getStatusCheckerOutputElement()).text().equalTo(statusChecker);
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        final String value = "background-color: yellow; font-size: 1.5em;";
        treeAttributes.set(TreeAttributes.style, value);
        String styleAttr = page.tree.getRoot().getAttribute("style");
        assertTrue(styleAttr.contains(value), "Attribute style should contain \"" + value + "\"");
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        treeAttributes.set(TreeAttributes.styleClass, testClassValue);
        String styleAttr = page.tree.getRoot().getAttribute("class");
        assertTrue(styleAttr.contains(testClassValue), "Attribute class should contain \"" + testClassValue + "\"");
    }

    @Test
    @Templates(value = "plain")
    public void testTitle() {
        this.testTitle(page.tree.getRoot());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12696")
    public void testLoadingFacet() {
        int sufficientTimeToCheckHandles = 2000;// ms
        setLoadingFacet(true);
        setResponseDelay(sufficientTimeToCheckHandles);
        page.tree.setToggleType(null);

        for (int index : new int[] { 0, 1 }) {
            treeNode = (index == 0) ? page.tree.getNodes().get(index) : treeNode.getNode(index);
            treeNode.setToggleType(null);

            assertFalse(treeNode.getHandleLoading().isVisible());
            assertTrue(treeNode.getHandle().isCollapsed());
            assertFalse(treeNode.getHandle().isExpanded());
            // trigger expand node without waiting for expanding
            treeNode.triggerNodeHandlerClick();
            assertTrue(treeNode.getHandleLoading().isVisible());
            assertTrue(treeNode.getHandleLoading().getImageUrl().endsWith(IMAGE_URL));
            assertFalse(treeNode.getHandle().isCollapsed());
            assertFalse(treeNode.getHandle().isExpanded());

            Graphene.waitModel().until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver arg0) {
                    return treeNode.isExpanded();
                }
            });
        }
    }

    private void setLoadingFacet(boolean turnOn) {
        boolean checked = Boolean.valueOf(page.loadingFacet.isSelected());
        if (checked != turnOn) {
            Graphene.guardAjax(page.loadingFacet).click();
        }
    }

    private void setResponseDelay(int milis) {
        page.getResponseDelayElement().sendKeys(String.valueOf(milis));
        page.getResponseDelayElement().submit();
    }

    private void expandLevel(int level) {
        switch (level) {
            case 0:
                for (RichFacesTreeNode treeNode1 : page.tree.getNodes()) {
                    treeNode1.expand();
                }
                break;
            case 1:
                for (RichFacesTreeNode treeNode1 : page.tree.getNodes()) {
                    for (RichFacesTreeNode treeNode2 : treeNode1.getNodes()) {
                        treeNode2.expand();
                    }
                }
                break;
            default:
                break;
        }
    }
}
