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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.SwitchType;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.tree.Tree.TreeNode;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 */
public abstract class AbstractTreeSelectionTest extends AbstractTreeTest {

    @FindByJQuery("input:submit[id$=expandAll]")
    private WebElement expandAll;
    @FindBy(css = "span[id$=selection]")
    private WebElement selection;
    @FindBy(css = "span[id$=selectionEventNewSelection]")
    private WebElement newSelection;
    @FindBy(css = "span[id$=selectionEventOldSelection]")
    private WebElement oldSelection;
    @FindBy(css = "span[id$=selectionEventClientId]")
    private WebElement clientId;
    @FindByJQuery(value = "div[id$=richTree] span.rf-trn-sel")
    private List<WebElement> allSelectedItems;

    @Inject
    @Use(value = "selectionTypes")
    private SwitchType selectionType;

    private static final SwitchType[] selectionTypes = new SwitchType[]{ SwitchType.ajax, SwitchType.client };
    private static final SwitchType[] selectionTypeAjax = new SwitchType[]{ SwitchType.ajax };
    private static final SwitchType[] selectionTypeClient = new SwitchType[]{ SwitchType.client };
    private static final SwitchType[] eventEnabledSelectionTypes = new SwitchType[]{ SwitchType.ajax };

    protected Integer[][] selectionPaths = new Integer[][]{ { 3, 2 }, { 1, 0, 0 }, { 1 }, { 3, 9, 2 } };

    protected TreeNode treeNode;

    protected void expandAll() {
        for (Integer[] path : selectionPaths) {
            treeNode = null;
            for (int i = 0; i < path.length; i++) {
                int index = path[i];
                treeNode = (treeNode == null)
                    ? tree.advanced().getNodes().get(index)
                    : treeNode.advanced().getNodes().get(index);
                if (i < path.length - 1) {
                    treeNode.advanced().expand();
                }
            }
        }
    }

    protected String getClientId() {
        return clientId.getText();
    }

    protected Integer[] getIntsFromString(String string) {
        Pattern pattern = Pattern.compile(".*\\[((?:(?:\\d+)(?:, )?)+)\\].*");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            String[] strings = StringUtils.split(matcher.group(1), ", ");
            Integer[] numbers = new Integer[strings.length];
            for (int i = 0; i < strings.length; i++) {
                numbers[i] = Integer.valueOf(strings[i]);
            }
            return numbers;
        }
        throw new IllegalStateException("pattern does not match");
    }

    protected Integer[] getNewSelection() {
        return getIntsFromString(newSelection.getText());
    }

    protected Integer[] getOldSelection() {
        return getIntsFromString(oldSelection.getText());
    }

    protected Integer[] getSelection() {
        return getIntsFromString(selection.getText());
    }

    @BeforeMethod
    public void prepareTest() {
        treeAttributes.set(TreeAttributes.selectionType, selectionType);
        treeAttributes.set(TreeAttributes.toggleType, selectionType);
    }

    protected void testSubNodesSelection() {
        expandAll();
        assertEquals(allSelectedItems.size(), 0);

        for (Integer[] path : selectionPaths) {
            treeNode = null;
            for (int index : path) {
                treeNode = (treeNode == null) ? tree.advanced().getNodes().get(index) : treeNode.advanced().getNodes().get(index);
            }
            assertFalse(treeNode.advanced().isSelected());
            getGuarded(treeNode.advanced(), selectionType).select();
            assertTrue(treeNode.advanced().isSelected());
            assertEquals(allSelectedItems.size(), 1);
        }
    }

    protected void testSubNodesSelectionEvents() {
        expandAll();
        Integer[] old = null;
        for (Integer[] path : selectionPaths) {
            treeNode = null;
            for (int index : path) {
                treeNode = (treeNode == null) ? tree.advanced().getNodes().get(index) : treeNode.advanced().getNodes().get(index);
            }
            String previousSelectionValue = selection.getText();
            getGuarded(treeNode.advanced(), selectionType).select();
            // selection output take some time until updated
            Graphene.waitAjax().until().element(clientId).is().present();
            assertEquals(getClientId(), "richTree");

            // there is delay before select triggers output update
            Graphene.waitAjax().until().element(selection).text().not().equalTo(previousSelectionValue);

            assertEquals(
                getSelection(),
                path,
                SimplifiedFormat.format("Actual Selection ({0}) doesn't correspond to expected ({1})",
                Arrays.deepToString(getSelection()), Arrays.deepToString(path)));

            assertEquals(
                getNewSelection(),
                path,
                SimplifiedFormat.format("Actual New selection ({0}) doesn't correspond to expected ({1})",
                Arrays.deepToString(getNewSelection()), Arrays.deepToString(path)));
            if (old != null) {
                assertEquals(
                    getOldSelection(),
                    old,
                    SimplifiedFormat.format("Actual Old selection ({0}) doesn't correspond to expected ({1})",
                    Arrays.deepToString(getOldSelection()), Arrays.deepToString(old)));
            } else {
                assertEquals(oldSelection.getText(), "[]");
            }
            old = getNewSelection();
        }
    }

    protected void testTopLevelSelection() {
        assertEquals(allSelectedItems.size(), 0);
        for (TreeNode node : tree.advanced().getNodes()) {
            assertFalse(node.advanced().isSelected());
            assertTrue(node.advanced().isCollapsed());
            getGuarded(node.advanced().getLabelElement(), selectionType).click();
            node.advanced().waitUntilNodeIsSelected();
            assertTrue(node.advanced().isCollapsed());
            assertEquals(allSelectedItems.size(), 1);
        }
    }

}
