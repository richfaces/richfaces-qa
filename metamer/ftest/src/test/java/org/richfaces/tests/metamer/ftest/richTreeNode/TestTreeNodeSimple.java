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
package org.richfaces.tests.metamer.ftest.richTreeNode;

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
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.handleClass;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.iconClass;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.iconCollapsed;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.iconExpanded;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.iconLeaf;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.immediate;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.labelClass;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.lang;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.onbeforetoggle;
import static org.richfaces.tests.metamer.ftest.richTreeNode.TreeNodeAttributes.rendered;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.component.SwitchType;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.richTree.TreeAttributes;
import org.richfaces.tests.metamer.ftest.richTree.TreeSimplePage;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNodeIcon;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23059 $
 */
public class TestTreeNodeSimple extends AbstractWebDriverTest {

    private static final String SAMPLE_CLASS = "sample-class";
    private static final String JQ_SAMPLE_CLASS = ".sample-class";
    private static final String IMAGE_URL = "/resources/images/loading.gif";

    Attributes<TreeAttributes> attributesTree = new Attributes<TreeAttributes>("attributes");
    Attributes<TreeNodeAttributes> attributesNode = new Attributes<TreeNodeAttributes>("treeNode1Attributes"); // country
    Attributes<TreeNodeAttributes> attributesLeaf = new Attributes<TreeNodeAttributes>("treeNode3Attributes");

    @Page
    TreeSimplePage page;

    public RichFacesTreeNode treeNode;

    @Inject
    @Use(empty = true)
    Event event = CLICK;
    Event[] events = new Event[] { CLICK, DBLCLICK, KEYDOWN, KEYPRESS, KEYUP, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP };

    @Inject
    @Use(empty = true)
    SwitchType toggleType;
    SwitchType[] toggleTypes = new SwitchType[] { SwitchType.ajax, SwitchType.server };

    @BeforeMethod
    public void init() {
        page.tree.setToggleType(SwitchType.ajax);
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTree/simpleSwingTreeNode.xhtml");
    }

    @Test
    public void testDir() {
        testHTMLAttribute(getTreeNode().getNodeItself(), attributesNode, TreeNodeAttributes.dir, "null");
        Graphene.waitGui().until().element(getTreeNode().getNodeItself()).is().present();
        testHTMLAttribute(getTreeNode().getNodeItself(), attributesNode, TreeNodeAttributes.dir, "ltr");
        Graphene.waitGui().until().element(getTreeNode().getNodeItself()).is().present();
        testHTMLAttribute(getTreeNode().getNodeItself(), attributesNode, TreeNodeAttributes.dir, "rtl");
    }

    @Test
    public void testHandleClass() {
        assertEquals(page.tree.getNodes().size(), 4);
        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_HANDLER + ":not(" + JQ_SAMPLE_CLASS + ")")).size(), 4);

        attributesNode.set(handleClass, SAMPLE_CLASS);

        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_HANDLER + JQ_SAMPLE_CLASS)).size(), 4);
        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_HANDLER + ":not(" + JQ_SAMPLE_CLASS + ")")).size(), 0);
    }

    @Test
    public void testIconClass() {
        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_ICON + ":not(" + JQ_SAMPLE_CLASS + ")")).size(), 4);
        attributesNode.set(iconClass, SAMPLE_CLASS);

        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_ICON + JQ_SAMPLE_CLASS)).size(), 4);
        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_ICON + ":not(" + JQ_SAMPLE_CLASS + ")")).size(), 0);
    }

    @Test
    public void testIconCollapsed() {
        WebElement iconImage = page.tree.getNodes().get(0).getIcon().getRoot();
        assertTrue(Graphene.element(iconImage).isPresent().apply(driver));
        assertFalse(Graphene.attribute(iconImage, "src").isPresent().apply(driver));

        attributesNode.set(iconCollapsed, IMAGE_URL);

        assertTrue(Graphene.element(iconImage).isPresent().apply(driver));
        assertTrue(Graphene.attribute(iconImage, "src").isPresent().apply(driver));

        assertTrue(Graphene.attribute(iconImage, "src").contains(IMAGE_URL).apply(driver));
    }

    @Test
    public void testIconExpanded() {
        WebElement iconImage = page.tree.getNodes().get(0).getIcon().getRoot();
        assertTrue(Graphene.element(iconImage).isPresent().apply(driver));
        assertFalse(Graphene.attribute(iconImage, "src").isPresent().apply(driver));

        attributesNode.set(iconExpanded, IMAGE_URL);

        assertTrue(Graphene.element(iconImage).isPresent().apply(driver));
        assertFalse(Graphene.attribute(iconImage, "src").isPresent().apply(driver));

        getTreeNode().expand();

        assertTrue(Graphene.element(iconImage).isPresent().apply(driver));
        assertTrue(Graphene.attribute(iconImage, "src").isPresent().apply(driver));

        assertTrue(Graphene.attribute(iconImage, "src").contains(IMAGE_URL).apply(driver));
    }

    @Test
    public void testIconLeaf() {
        getTreeNode().expand();
        RichFacesTreeNode subTreeNode = getTreeNode().getNode(0);
        subTreeNode.expand();

        RichFacesTreeNode leaf = subTreeNode.getNode(0);
        RichFacesTreeNodeIcon leafIcon = leaf.getIcon();

        assertTrue(Graphene.element(leafIcon.getRoot()).isPresent().apply(driver));
        assertFalse(Graphene.attribute(leafIcon.getRoot(), "src").isPresent().apply(driver));

        attributesLeaf.set(iconLeaf, IMAGE_URL);

        assertTrue(Graphene.element(leafIcon.getRoot()).isPresent().apply(driver));
        assertTrue(Graphene.attribute(leafIcon.getRoot(), "src").isPresent().apply(driver));

        assertTrue(Graphene.attribute(leafIcon.getRoot(), "src").contains(IMAGE_URL).apply(driver));
    }

    @Test
    @Use(field = "toggleType", value = "toggleTypes")
    public void testImmediate() {
        attributesTree.set(TreeAttributes.toggleType, toggleType);

        getTreeNode().expand();

        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "tree toggle listener invoked");

        getTreeNode().collapse();

        attributesNode.set(immediate, true);

        getTreeNode().expand();

        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "tree toggle listener invoked");
    }

    @Test
    public void testLabelClass() {
        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_LABEL + ":not(" + JQ_SAMPLE_CLASS + ")")).size(), 4);

        attributesNode.set(labelClass, SAMPLE_CLASS);

        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_LABEL + JQ_SAMPLE_CLASS)).size(), 4);
        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE_LABEL + ":not(" + JQ_SAMPLE_CLASS + ")")).size(), 0);
    }

    @Test
    public void testLang() {
        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE + ":not(" + "[lang=cs]" + ")")).size(), 4);

        attributesNode.set(lang, "cs");

        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE + "[lang=cs]")).size(), 0);
        assertEquals(page.tree.getRoot().findElements(By.cssSelector(
            RichFacesTreeNode.CSS_NODE + ":not(" + "[lang=cs]" + ")")).size(), 4);
    }

    @Test
    @Use(field = "event", value = "events")
    public void testClientEvents() {
        final String attributeName = event.getEventName();

        testRequestEventsBefore(page.node1AttributesTable, attributeName);

        fireEventAt(getTreeNode().getNodeItself(), event);

        testRequestEventsAfter(attributeName);
    }

    @Test
    public void testOnbeforetoggle() {
        super.testRequestEventsBefore(page.node1AttributesTable, "beforetoggle", "toggle");
        getTreeNode().expand();
        super.testRequestEventsAfter("beforetoggle", "toggle");
    }

    @Test
    public void testOnbeforetoggleWithJsFunction() {
        attributesNode.set(onbeforetoggle, "functionChecker()");
        String jsFunctionChecker = page.getJsFunctionCheckerElement().getText();
        String requestTime = page.getRequestTimeElement().getText();

        getTreeNode().getHandle().getRoot().click();

        Graphene.waitGui().until().element(page.getJsFunctionCheckerElement()).text().not().equalTo(jsFunctionChecker);
        page.assertNoListener("tree toggle listener invoked");

        Graphene.waitGui().until().element(page.getRequestTimeElement()).text().not().equalTo(requestTime);
        page.getListenerCondition(PhaseId.PROCESS_VALIDATIONS, "tree toggle listener invoked");
    }

    @Test
    public void testRendered() {
        getTreeNode().expand();
        RichFacesTreeNode subTreeNode = getTreeNode().getNode(0);
        subTreeNode.expand();
        RichFacesTreeNode leaf = subTreeNode.getNode(0);

        assertTrue(Graphene.element(leaf.getRoot()).isPresent().apply(driver));

        attributesLeaf.set(rendered, false);

        // verify parent of leaf node is present
        assertTrue(Graphene.element(getTreeNode().getNode(0).getRoot()).isPresent().apply(driver));
        // but leaf is not
        assertTrue(getTreeNode().getNode(0).getNode(0) == null);
    }

    @Test
    public void testStyle() {
        final String value = "background-color: yellow; font-size: 1.5em;";
        attributesNode.set(TreeNodeAttributes.style, value);
        assertTrue(Graphene.attribute(getTreeNode().getNodeItself(), "style").contains(value).apply(driver));
    }

    @Test
    public void testStyleClass() {
        final String value = "metamer-ftest-class";
        attributesNode.set(TreeNodeAttributes.styleClass, value);
        assertTrue(Graphene.attribute(getTreeNode().getNodeItself(), "class").contains(value).apply(driver));
    }

    @Test
    public void testTitle() {
        final String testTitle = "RichFaces 4";
        testHTMLAttribute(getTreeNode().getNodeItself(), attributesNode, TreeNodeAttributes.title, testTitle);
    }

    private void fireEventAt(WebElement element, Event event) {
        if (event == CLICK) {
            new Actions(driver).click(element).perform();
        } else if (event == DBLCLICK) {
            new Actions(driver).doubleClick(element).perform();
        } else if (event == MOUSEDOWN) {
            new Actions(driver).clickAndHold(element).perform();
        } else if (event == MOUSEMOVE) {
            new Actions(driver).moveToElement(element).perform();
//        } else if (event == MOUSEOUT) {
//            selenium.mouseOutAt(element, coords);
        } else if (event == MOUSEOVER) {
            new Actions(driver).moveToElement(element, 1, 1).perform();
        } else if (event == MOUSEUP) {
            new Actions(driver).clickAndHold(element).release().perform();
        } else {
            fireEvent(element, event);
        }
    }

    private RichFacesTreeNode getTreeNode() {
        if (treeNode == null) treeNode = page.tree.getNodes().get(0);

        treeNode.setToggleType(toggleType);

        return treeNode;
    }
}
