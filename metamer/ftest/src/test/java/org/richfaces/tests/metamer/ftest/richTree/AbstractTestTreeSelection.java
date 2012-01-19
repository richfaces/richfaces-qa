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

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.treeAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.component.SwitchType;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 */
public abstract class AbstractTestTreeSelection extends AbstractTestTree {

    @Inject
    @Use(value = "selectionTypes")
    protected SwitchType selectionType;

    protected SwitchType[] selectionTypes = new SwitchType[] { SwitchType.ajax, SwitchType.client };
    protected SwitchType[] selectionTypeAjax = new SwitchType[] { SwitchType.ajax };
    protected SwitchType[] selectionTypeClient = new SwitchType[] { SwitchType.client };
    protected SwitchType[] eventEnabledSelectionTypes = new SwitchType[] { SwitchType.ajax };

    protected TreeModel tree = new TreeModel(pjq("div.rf-tr[id$=richTree]"));
    protected TreeNodeModel treeNode;

    protected Integer[][] selectionPaths = new Integer[][] { { 4, 3 }, { 2, 1, 1 }, { 2 }, { 4, 10, 3 } };

    private JQueryLocator expandAll = jq("input:submit[id$=expandAll]");
    private JQueryLocator selection = jq("span[id$=selection]");
    private JQueryLocator clientId = jq("span[id$=selectionEventClientId]");
    private JQueryLocator newSelection = jq("span[id$=selectionEventNewSelection]");
    private JQueryLocator oldSelection = jq("span[id$=selectionEventOldSelection]");

    @BeforeMethod
    public void testInitialize() {
        treeAttributes.set(TreeAttributes.selectionType, selectionType);
        tree.setSelectionType(selectionType);
    }

    protected void testTopLevelSelection() {
        assertEquals(tree.getAnySelectedNodesCount(), 0);

        for (TreeNodeModel treeNode : tree.getNodes()) {
            assertFalse(treeNode.isSelected());
            assertTrue(treeNode.isCollapsed());
            treeNode.select();
            assertTrue(treeNode.isSelected());
            assertTrue(treeNode.isCollapsed());

            assertEquals(tree.getAnySelectedNodesCount(), 1);
        }
    }

    protected void testSubNodesSelection() {
        expandAll();
        assertEquals(tree.getAnySelectedNodesCount(), 0);

        for (Integer[] path : selectionPaths) {
            treeNode = null;
            for (int index : path) {
                treeNode = (treeNode == null) ? tree.getNode(index) : treeNode.getNode(index);
            }
            assertFalse(treeNode.isSelected());
            treeNode.select();
            assertTrue(treeNode.isSelected());
            assertEquals(tree.getAnySelectedNodesCount(), 1);
        }
    }

    protected void testSubNodesSelectionEvents() {
        expandAll();
        Integer[] old = null;
        for (Integer[] path : selectionPaths) {
            treeNode = null;
            for (int index : path) {
                treeNode = (treeNode == null) ? tree.getNode(index) : treeNode.getNode(index);
            }
            treeNode.select();
            assertEquals(getClientId(), "richTree");
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
                assertEquals(selenium.getText(oldSelection), "[]");
            }
            old = getNewSelection();
        }
    }

    protected Integer[] getSelection() {
        String string = selenium.getText(selection);
        return getIntsFromString(string);
    }

    protected Integer[] getNewSelection() {
        String string = selenium.getText(newSelection);
        return getIntsFromString(string);
    }

    protected Integer[] getOldSelection() {
        String string = selenium.getText(oldSelection);
        return getIntsFromString(string);
    }

    protected String getClientId() {
        return selenium.getText(clientId);
    }

    protected Integer[] getIntsFromString(String string) {
        Pattern pattern = Pattern.compile(".*\\[((?:(?:\\d+)(?:, )?)+)\\].*");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            String[] strings = StringUtils.split(matcher.group(1), ", ");
            Integer[] numbers = new Integer[strings.length];
            for (int i = 0; i < strings.length; i++) {
                numbers[i] = Integer.valueOf(strings[i]) + 1;
            }
            return numbers;
        }
        throw new IllegalStateException("pattern does not match");
    }

    protected void expandAll() {
        guardXhr(selenium).click(expandAll);
    }
}
