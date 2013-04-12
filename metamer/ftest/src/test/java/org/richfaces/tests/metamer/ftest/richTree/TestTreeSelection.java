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

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.annotations.Uses;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 23169 $
 */
public class TestTreeSelection extends AbstractTestTreeSelectionWD {

    @Test
    @Use(field = "selectionType", value = "selectionTypeClient")
    public void testTopLevelSelectionClient() {
        testTopLevelSelection();
    }

    @Test
    @Uses({ @Use(field = "selectionType", value = "selectionTypeAjax"),
        @Use(field = "sample", strings = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" }) })
    public void testTopLevelSelectionAjax() {
        testTopLevelSelection();
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "selectionType", value = "selectionTypeAjax"),
        @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testTopLevelSelectionAjaxWithSimpleTreeNode() {
        testTopLevelSelection();
    }

    @Test
    @Use(field = "selectionType", value = "selectionTypeClient")
    public void testSubNodesSelectionClient() {
        testSubNodesSelection();
    }

    @Test
    @Uses({ @Use(field = "selectionType", value = "selectionTypeAjax"),
        @Use(field = "sample", strings = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" }) })
    public void testSubNodesSelectionAjax() {
        testSubNodesSelection();
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "selectionType", value = "selectionTypeAjax"),
        @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testSubNodesSelectionAjaxWithSimpleTreeNode() {
        testSubNodesSelection();
    }

    @Test
    @Uses({ @Use(field = "selectionType", value = "eventEnabledSelectionTypes"),
        @Use(field = "sample", strings = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" }) })
    public void testSubNodesSelectionEventsAjax() {
        testSubNodesSelectionEvents();
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Uses({ @Use(field = "selectionType", value = "eventEnabledSelectionTypes"),
        @Use(field = "sample", strings = { "simpleRichFacesTreeNode" }) })
    public void testSubNodesSelectionEventsAjaxWithSimpleTreeNode() {
        testSubNodesSelectionEvents();
    }

}
