/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.Graphene.waitGui;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;

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

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import static org.jboss.arquillian.ajocado.guard.RequestGuardFactory.guard;

import static org.jboss.test.selenium.JQuerySelectors.append;
import static org.jboss.test.selenium.JQuerySelectors.not;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.nodeClass;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.treeAttributes;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.data;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.execute;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.iconCollapsed;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.iconExpanded;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.iconLeaf;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.limitRender;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.render;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.selectionType;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.status;
import static org.richfaces.tests.metamer.ftest.richTree.TreeAttributes.toggleNodeEvent;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.jboss.cheiron.halt.XHRHalter;
import org.richfaces.component.SwitchType;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23125 $
 */
public class TestTreeSimple extends AbstractTestTree {

    private static final String IMAGE_URL = "/resources/images/loading.gif";

    SeleniumCondition treeNodeExpanded = new SeleniumCondition() {

        @Override
        public boolean isTrue() {
            return treeNode.isExpanded();
        }
    };

    @Inject
    @Use(empty = true)
    Event eventToFire;
    Event[] eventsToFire = new Event[] { MOUSEDOWN, MOUSEUP, MOUSEOVER, MOUSEOUT };

    @Inject
    @Use(empty = true)
    Event domEvent;
    Event[] domEvents = { CLICK, DBLCLICK, KEYDOWN, KEYPRESS, KEYUP, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP };

    TreeModel tree = new TreeModel(pjq("div.rf-tr[id$=richTree]"));
    TreeNodeModel treeNode;

    JQueryLocator expandAll = jq("input:submit[id$=expandAll]");
    JQueryLocator loadingFacet = jq("input:checkbox[id$=loadingFacet]");

    @Test
    public void testData() {
        treeAttributes.set(data, "RichFaces 4");
        treeAttributes.set(oncomplete, "data = event.data");

        retrieveRequestTime.initializeValue();
        tree.getNode(1).select();
        waitGui.waitForChange(retrieveRequestTime);

        assertEquals(retrieveWindowData.retrieve(), "RichFaces 4");
    }

    @Test
    public void testDir() {
        super.testDir(tree);
    }

    @Test
    public void testExecute() {
        treeAttributes.set(execute, "executeChecker @this");
        tree.getNode(1).select();
        assertTrue(selenium.isTextPresent("* executeChecker"));
    }

    @Test
    public void testHandleClass() {
        expandAll();

        final String value = "metamer-ftest-class";
        selenium.type(jq("input[id$=attributes:handleClassInput]"), value);
        selenium.waitForPageToLoad();
        AttributeLocator<?> styleAttr = tree.getAnyNode().getHandle().getAttribute(Attribute.CLASS);
        assertTrue(selenium.getAttribute(styleAttr).contains(value), "Attribute handleClass should contain \"" + value
            + "\"");
    }

    @Test
    public void testIconClass() {
        expandAll();

        final String value = "metamer-ftest-class";
        selenium.type(jq("input[id$=attributes:iconClassInput]"), value);
        selenium.waitForPageToLoad();
        AttributeLocator<?> styleAttr = tree.getAnyNode().getIcon().getAttribute(Attribute.CLASS);
        assertTrue(selenium.getAttribute(styleAttr).contains(value), "Attribute iconClass should contain \"" + value
            + "\"");
    }

    @Test
    public void testLabelClass() {
        expandAll();

        final String value = "metamer-ftest-class";
        selenium.type(jq("input[id$=attributes:labelClassInput]"), value);
        selenium.waitForPageToLoad();
        AttributeLocator<?> styleAttr = tree.getAnyNode().getLabel().getAttribute(Attribute.CLASS);
        assertTrue(selenium.getAttribute(styleAttr).contains(value), "Attribute labelClass should contain \"" + value
            + "\"");
    }

    @Test
    public void testNodeClass() {
        expandAll();
        super.testStyleClass(tree.getAnyNode().getTreeNode(), nodeClass);
    }

    @Test
    public void testIconCollapsed() {
        treeAttributes.set(iconCollapsed, IMAGE_URL);

        for (int i = 0; i < 3; i++) {
            ExtendedLocator<JQueryLocator> icons = tree.getAnyNode().getIcon();
            JQueryLocator iconsWithTheGivenUrl = append(icons, format("[src$={0}]", IMAGE_URL));
            JQueryLocator iconsWithoutTheGivenUrl = not(icons, format("[src$={0}]", IMAGE_URL));

            int with = selenium.getCount(iconsWithTheGivenUrl);
            int without = selenium.getCount(iconsWithoutTheGivenUrl);

            assertEquals(with > 0, i < 2);
            assertEquals(without > 0, i > 0);

            expandLevel(i);
        }
    }

    @Test
    public void testIconExpanded() {
        treeAttributes.set(iconExpanded, IMAGE_URL);

        for (int i = 0; i < 3; i++) {
            ExtendedLocator<JQueryLocator> icons = tree.getAnyNode().getIcon();
            JQueryLocator iconsWithTheGivenUrl = append(icons, format("[src$={0}]", IMAGE_URL));
            JQueryLocator iconsWithoutTheGivenUrl = not(not(icons, ".rf-trn-ico-lf"), format("[src$={0}]", IMAGE_URL));

            int with = selenium.getCount(iconsWithTheGivenUrl);
            int without = selenium.getCount(iconsWithoutTheGivenUrl);

            assertEquals(with > 0, i > 0);
            assertEquals(without > 0, i < 2);

            expandLevel(i);
        }
    }

    @Test
    public void testIconLeaf() {
        treeAttributes.set(iconLeaf, IMAGE_URL);

        for (int i = 0; i < 3; i++) {
            ExtendedLocator<JQueryLocator> icons = tree.getAnyNode().getIcon();
            JQueryLocator iconsWithTheGivenUrl = append(icons, format("[src$={0}]", IMAGE_URL));
            JQueryLocator iconsWithoutTheGivenUrl = not(not(icons, ".rf-trn-ico-exp"), format("[src$={0}]", IMAGE_URL));

            int with = selenium.getCount(iconsWithTheGivenUrl);
            int without = selenium.getCount(iconsWithoutTheGivenUrl);

            assertEquals(with > 0, i > 1);
            assertEquals(without > 0, i < 2);

            expandLevel(i);
        }
    }

    @Test
    public void testLang() {
        super.testLang(tree);
    }

    @Test
    public void testLimitRender() {
        treeAttributes.set(render, "@this renderChecker");
        treeAttributes.set(limitRender, true);
        retrieveRenderChecker.initializeValue();
        String requestTime = retrieveRequestTime.retrieve();
        tree.getNode(1).select();
        waitGui.waitForChange(retrieveRenderChecker);
        assertEquals(retrieveRequestTime.retrieve(), requestTime);
    }

    @Test
    @Use(field = "sample", strings = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    @Templates(exclude = "a4jRegion")
    public void testSelectionClientSideEvents() {
        String[] events = new String[] { "beforeselectionchange", "begin", "beforedomupdate", "complete",
            "selectionchange" };
        testRequestEventsBefore(events);
        tree.getNode(1).select();
        testRequestEventsAfter(events);
    }

    @Test(groups = { "4.3" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Use(field = "sample", strings = { "simpleRichFacesTreeNode" })
    @Templates(exclude = "a4jRegion")
    public void testSelectionClientSideEventsWithSimpleTreeNode() {
        testSelectionClientSideEvents();
    }

    @Test(groups = { "4.Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11319")
    @Templates(value = "a4jRegion")
    public void testSelectionClientSideEventsInRegion() {
        String[] events = new String[] { "beforeselectionchange", "begin", "beforedomupdate", "complete",
            "selectionchange" };
        testRequestEventsBefore(events);
        tree.getNode(1).select();
        testRequestEventsAfter(events);
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-10265")
    public void testToggleClientSideEvents() {
        String[] events = new String[] { "beforenodetoggle", "begin", "beforedomupdate", "complete", "nodetoggle" };
        testRequestEventsBefore(events);
        tree.getNode(1).expand();
        testRequestEventsAfter(events);
    }

    @Test
    @Use(field = "domEvent", value = "domEvents")
    public void testDomEvents() {
        testFireEvent(domEvent, tree);
    }

    @Test
    public void testRender() {
        treeAttributes.set(render, "@this renderChecker");
        retrieveRenderChecker.initializeValue();
        tree.getNode(1).select();
        waitGui.waitForChange(retrieveRenderChecker);
    }

    @Test
    public void testRendered() {
        assertTrue(selenium.isElementPresent(tree) && selenium.isVisible(tree));
        treeAttributes.set(rendered, false);
        assertFalse(selenium.isElementPresent(tree));
    }

    @Test
    public void testStatus() {
        retrieveStatusChecker.initializeValue();
        tree.getNode(1).select();
        assertFalse(retrieveStatusChecker.isValueChanged());

        treeAttributes.set(status, "statusChecker");
        retrieveStatusChecker.initializeValue();
        tree.getNode(1).select();
        assertTrue(retrieveStatusChecker.isValueChanged());
    }

    @Test
    public void testStyle() {
        final String value = "background-color: yellow; font-size: 1.5em;";
        selenium.type(jq("input[id$=attributes:styleInput]"), value);
        selenium.waitForPageToLoad();
        AttributeLocator<?> styleAttr = tree.getAttribute(Attribute.STYLE);
        assertTrue(selenium.getAttribute(styleAttr).contains(value), "Attribute style should contain \"" + value + "\"");
    }

    @Test
    public void testStyleClass() {
        final String value = "metamer-ftest-class";
        selenium.type(jq("input[id$=attributes:styleClassInput]"), value);
        selenium.waitForPageToLoad();
        AttributeLocator<?> styleAttr = tree.getAttribute(Attribute.CLASS);
        assertTrue(selenium.getAttribute(styleAttr).contains(value), "Attribute class should contain \"" + value + "\"");
    }

    @Test
    public void testTitle() {
        this.testTitle(tree);
    }

    @Test
    @Use(field = "eventToFire", value = "eventsToFire")
    public void testToggleNodeEvent() {
        treeNode = tree.getNode(2);
        treeAttributes.set(selectionType, SwitchType.client);
        ExtendedLocator<JQueryLocator> target = treeNode.getLabel();

        for (Event eventToSetup : eventsToFire) {
            assertTrue(treeNode.isCollapsed());
            treeAttributes.set(toggleNodeEvent, eventToSetup.getEventName());

            fireEvent(target, eventToFire, eventToSetup);

            if (eventToFire == eventToSetup) {
                assertTrue(treeNode.isExpanded());
                fireEvent(target, eventToFire, eventToSetup);
                assertTrue(treeNode.isCollapsed());
            } else {
                assertTrue(treeNode.isCollapsed());
            }
        }
    }

    @Test
    public void testLoadingFacet() {
        setLoadingFacet(true);
        tree.setToggleType(null);
        XHRHalter.enable();

        XHRHalter xhrHalter = null;

        for (int index : new int[] { 1, 2 }) {
            treeNode = (index == 1) ? tree.getNode(index) : treeNode.getNode(index);

            assertFalse(treeNode.getHandleLoading().isVisible());
            assertTrue(treeNode.getHandle().isCollapsed());
            assertFalse(treeNode.getHandle().isExpanded());
            treeNode.expand();
            assertTrue(treeNode.getHandleLoading().isVisible());
            assertTrue(treeNode.getHandleLoading().getImageUrl().endsWith(IMAGE_URL));
            assertFalse(treeNode.getHandle().isCollapsed());
            assertFalse(treeNode.getHandle().isExpanded());

            if (xhrHalter == null) {
                xhrHalter = XHRHalter.getHandleBlocking();
            }
            xhrHalter.complete();

            waitModel.until(treeNodeExpanded);
        }
    }

    private void fireEvent(ElementLocator<?> target, Event eventToFire, Event eventToSetup) {
        RequestType requestType = (eventToFire == eventToSetup) ? RequestType.XHR : RequestType.NONE;
        if (eventToFire == MOUSEDOWN) {
            guard(selenium, requestType).mouseDown(target);
        }
        if (eventToFire == MOUSEUP) {
            guard(selenium, requestType).mouseUp(target);
        }
        if (eventToFire == MOUSEOVER) {
            guard(selenium, requestType).mouseOver(target);
        }
        if (eventToFire == MOUSEOUT) {
            guard(selenium, requestType).mouseOut(target);
        }
    }

    private void setLoadingFacet(boolean turnOn) {
        boolean checked = Boolean.valueOf(selenium.getValue(loadingFacet));
        if (checked != turnOn) {
            guardXhr(selenium).click(loadingFacet);
        }
    }

    private void expandAll() {
        guardXhr(selenium).click(expandAll);
    }

    private void expandLevel(int level) {
        switch (level) {
            case 0:
                for (TreeNodeModel treeNode1 : tree.getNodes()) {
                    treeNode1.expand();
                }
                break;
            case 1:
                for (TreeNodeModel treeNode1 : tree.getNodes()) {
                    for (TreeNodeModel treeNode2 : treeNode1.getNodes()) {
                        treeNode2.expand();
                    }
                }
                break;
            default:
                break;
        }
    }
}
