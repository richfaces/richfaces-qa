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

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.treeAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.richfaces.component.SwitchType;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.treeNode.RichFacesTreeNode;
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

    @Page
    TreeSimplePage page;

    protected Integer[][] selectionPaths = new Integer[][] { { 4, 3 }, { 2, 1, 1 }, { 2 }, { 4, 10, 3 } };

    protected RichFacesTreeNode treeNode;

    @BeforeMethod
    public void testInitialize() {
        treeAttributes.set(TreeAttributes.selectionType, selectionType);
        page.tree.setSelectionType(selectionType);
    }

    protected void testTopLevelSelection() {
        assertEquals(page.tree.getAnySelectedNodesCount(), 0);

        for (RichFacesTreeNode treeNode : page.tree.getNodes()) {
            assertFalse(treeNode.isSelected());
            assertTrue(treeNode.isCollapsed());
            treeNode.setSelectionType(selectionType);
            treeNode.select();
            assertTrue(treeNode.isSelected());
            assertTrue(treeNode.isCollapsed());

            assertEquals(page.tree.getAnySelectedNodesCount(), 1);
        }
    }

    protected void testSubNodesSelection() {
        expandAll();
        assertEquals(page.tree.getAnySelectedNodesCount(), 0);

        for (Integer[] path : selectionPaths) {
            treeNode = null;
            for (int index : path) {
                treeNode = (treeNode == null) ? page.tree.getNodes().get(index-1) : treeNode.getNode(index-1);
                treeNode.setSelectionType(selectionType);
            }
            assertFalse(treeNode.isSelected());
            treeNode.select();
            assertTrue(treeNode.isSelected());
            assertEquals(page.tree.getAnySelectedNodesCount(), 1);
        }
    }

    protected void testSubNodesSelectionEvents() {
        expandAll();
        Integer[] old = null;
        for (Integer[] path : selectionPaths) {
            treeNode = null;
            for (int index : path) {
                treeNode = (treeNode == null) ? page.tree.getNodes().get(index-1) : treeNode.getNode(index-1);
                treeNode.setSelectionType(selectionType);
            }
            String previousSelectionValue = page.selection.getText();
            treeNode.select();
            // selection output take some time until updated
            Graphene.waitAjax().until().element(page.clientId).is().present();
            assertEquals(getClientId(), "richTree");

            // there is delay before select triggers output update
            Graphene.waitAjax().until().element(page.selection).text().not().equalTo(previousSelectionValue);

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
                assertEquals(page.oldSelection.getText(), "[]");
            }
            old = getNewSelection();
        }
    }

    protected Integer[] getSelection() {
        String string = page.selection.getText();
        string = page.selection.getText();
        return getIntsFromString(string);
    }

    protected Integer[] getNewSelection() {
        String string = page.newSelection.getText();
        return getIntsFromString(string);
    }

    protected Integer[] getOldSelection() {
        String string = page.oldSelection.getText();
        return getIntsFromString(string);
    }

    protected String getClientId() {
        return page.clientId.getText();
    }

    protected void expandAll() {
        for (Integer[] path : selectionPaths) {
            treeNode = null;
            for (int i = 0; i < path.length; i++) {
                int index = path[i];
                treeNode = (treeNode == null) ? page.tree.getNodes().get(index-1) : treeNode.getNode(index-1);
                if (i < path.length - 1) {
                    treeNode.expand();
                }
            }
        }
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

}
