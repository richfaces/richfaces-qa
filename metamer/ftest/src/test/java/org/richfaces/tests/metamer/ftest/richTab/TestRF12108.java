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
package org.richfaces.tests.metamer.ftest.richTab;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.ajocado.Graphene.jq;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.a4jStatus.AbstractStatusTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test for RF-12092
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestRF12108 extends AbstractStatusTest {

    private JQueryLocator inputNotHandledCorrectly = jq("input[id*='notHandled']");
    private JQueryLocator outputGenerated = jq("span[id*='out']");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/rf-12108.xhtml");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12108")
    public void testStatusIsClearedWhenRequestCompleted() {

        String expectedOutput = "Should be rendered aside as well!";
        selenium.type(inputNotHandledCorrectly, expectedOutput);

        String actualOutput = selenium.getText(outputGenerated);
        assertEquals(actualOutput, expectedOutput, "The AJAX update is corrupted, when triggered from the second tab!");
    }
}
