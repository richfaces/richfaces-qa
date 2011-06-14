/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.treeAdaptors;

import static org.testng.Assert.assertTrue;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;


import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.AttributeRetriever;
import org.richfaces.tests.showcase.tree.AbstractTreeTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTreeModelRecursiveAdaptor extends AbstractTreeTest {

	/* *******************************************************************************************
	 * Tests
	 * *********************************************************************
	 * **********************
	 */

	@Test
	public void testTreeIsNotEmpty() {

		collapseOrExpandAllNodes(allExpandedHnd);

		JQueryLocator folders = jq(FIRST_LVL_NODE);

		int numberOfFolders = selenium.getCount(folders);

		assertTrue(numberOfFolders > 0,
				"The number of first lvl folders should be more than 0");

		JQueryLocator leavesOfFirstFolder = jq(FIRST_LVL_NODE + ":eq(0) > "
				+ LEAF);

		JQueryLocator expandSignFirstNode = jq(FIRST_LVL_NODE + ":eq(0) > "
				+ EXPAND_SIGN);

		selenium.click(expandSignFirstNode);
		waitModel.waitForChange(AttributeRetriever.getInstance()
				.attributeLocator(
						jq(FIRST_LVL_NODE + ":eq(0)").getAttribute(
								Attribute.CLASS)));

		int numberOfFirstFolderLeaves = selenium.getCount(leavesOfFirstFolder);

		assertTrue(numberOfFirstFolderLeaves > 0, "The number of first folder "
				+ "leaves should be more than 0!");
	}

	@Test
	public void testRememberExpandedNodeWhenParentIsCollapsed() {

		collapseOrExpandAllNodes(allExpandedHnd);

		JQueryLocator secondFolderExpand = jq(FIRST_LVL_NODE + ":eq(1) > "
				+ EXPAND_SIGN);

		selenium.click(secondFolderExpand);
		waitModel.waitForChange(AttributeRetriever.getInstance()
				.attributeLocator(
						jq(FIRST_LVL_NODE + ":eq(1)").getAttribute(
								Attribute.CLASS)));

		JQueryLocator secondFolderFirstFolderExpand = jq(FIRST_LVL_NODE
				+ ":eq(1) > " + SECOND_LVL_NODE + ":eq(0) > " + EXPAND_SIGN);

		selenium.click(secondFolderFirstFolderExpand);
		waitModel.waitForChange(AttributeRetriever.getInstance()
				.attributeLocator(
						jq(
								FIRST_LVL_NODE + ":eq(1) > " + SECOND_LVL_NODE
										+ ":eq(0)").getAttribute(
								Attribute.CLASS)));

		JQueryLocator secondFolderCollapse = jq(FIRST_LVL_NODE + ":eq(1) > "
				+ COLLAPSE_SIGN);

		selenium.click(secondFolderCollapse);
		waitModel.waitForChange(AttributeRetriever.getInstance()
				.attributeLocator(
						jq(FIRST_LVL_NODE + ":eq(1)").getAttribute(
								Attribute.CLASS)));

		selenium.click(secondFolderExpand);
		waitModel.waitForChange(AttributeRetriever.getInstance()
				.attributeLocator(
						jq(FIRST_LVL_NODE + ":eq(1)").getAttribute(
								Attribute.CLASS)));

		JQueryLocator secondFolderFirstFolderCollapse = jq(FIRST_LVL_NODE
				+ ":eq(1) > " + SECOND_LVL_NODE + ":eq(0) > " + COLLAPSE_SIGN);
		assertTrue(selenium.isElementPresent(secondFolderFirstFolderCollapse),
				"The expansion of the subfolder should be remained, "
						+ "when the parent node is collapsed");

	}

}
