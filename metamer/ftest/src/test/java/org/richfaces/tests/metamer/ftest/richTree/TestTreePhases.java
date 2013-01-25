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

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.treeAttributes;

import javax.faces.event.PhaseId;

import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 23125 $
 */
public class TestTreePhases extends AbstractTestTree {
    @Inject
    @Use(booleans = { true, false })
    Boolean immediate;

    private TreeModel tree = new TreeModel(pjq("div.rf-tr[id$=richTree]"));

    @BeforeMethod
    public void initialize() {
        treeAttributes.set(TreeAttributes.immediate, immediate);
    }

    @Test
    @Use(field = "sample", strings = { "simpleSwingTreeNode", "simpleRichFacesTreeDataModel" })
    public void testPhasesSelection() {
        tree.getNode(4).expand();
        tree.getNode(4).getNode(3).select();
        phaseInfo.assertPhases(PhaseId.ANY_PHASE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "selection change listener invoked");
    }

    @Test(groups = { "Future" })
    @IssueTracking("https://issues.jboss.org/browse/RF-11766")
    @Use(field = "sample", strings = { "simpleRichFacesTreeNode" })
    public void testPhasesSelectionWithSimpleTreeNode() {
        testPhasesSelection();
    }

    @Test
    public void testPhasesToggling() {
        tree.getNode(2).expand();
        PhaseId phaseId = (immediate) ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.PROCESS_VALIDATIONS;
        phaseInfo.assertListener(phaseId, "tree toggle listener invoked");
    }
}
