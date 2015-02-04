/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF13958 extends AbstractNestedCollapsibleSubTablesTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richCollapsibleSubTable/rf-13927.xhtml");// reused sample
    }

    @Test(groups = "Future")
    public void testParentTableCollapsionAndExpansionLeavesExpandedTableExpanded() {
        // collapse the 2nd level of the first subtable
        toggle(getToggler2a());
        // check the first subtable
        assertTrue(getToggler1a().isVisible());
        assertTrue(getToggler2a().isVisible());
        assertFalse(getToggler3a().isVisible());
        assertFirstTableDataNotVisible();
        // the second subtable remains the same
        assertTrue(getToggler1b().isVisible());
        assertTrue(getToggler2b1().isVisible());
        assertTrue(getToggler3b1().isVisible());
        assertTrue(getToggler2b2().isVisible());
        assertTrue(getToggler3b2().isVisible());
        assertSecondTableDataVisible();
        assertThirdTableDataVisible();

        // expand the 2nd level of the first subtable
        toggle(getToggler2a());
        // check the first subtable
        assertTrue(getToggler1a().isVisible());
        assertTrue(getToggler2a().isVisible());
        assertTrue(getToggler3a().isVisible());
        assertFirstTableDataVisible();
        // the second subtable remains the same
        assertTrue(getToggler1b().isVisible());
        assertTrue(getToggler2b1().isVisible());
        assertTrue(getToggler3b1().isVisible());
        assertTrue(getToggler2b2().isVisible());
        assertTrue(getToggler3b2().isVisible());
        assertSecondTableDataVisible();
        assertThirdTableDataVisible();

        // collapse the 2nd level of the second subtable
        toggle(getToggler2b1());
        // the first subtable remains the same
        assertTrue(getToggler1b().isVisible());
        assertTrue(getToggler2a().isVisible());
        assertTrue(getToggler3a().isVisible());
        assertFirstTableDataVisible();
        // check the second subtable
        assertTrue(getToggler1b().isVisible());
        assertTrue(getToggler2b1().isVisible());
        assertFalse(getToggler3b1().isVisible());
        assertTrue(getToggler2b2().isVisible());
        assertTrue(getToggler3b2().isVisible());
        assertSecondTableDataNotVisible();
        assertThirdTableDataVisible();

        // expand the 2nd level of the second subtable
        toggle(getToggler2b1());
        // the first subtable remains the same
        assertTrue(getToggler1a().isVisible());
        assertTrue(getToggler2a().isVisible());
        assertTrue(getToggler3a().isVisible());
        assertFirstTableDataVisible();
        // check the second subtable
        assertTrue(getToggler1b().isVisible());
        assertTrue(getToggler2b1().isVisible());
        assertTrue(getToggler3b1().isVisible());
        assertTrue(getToggler2b2().isVisible());
        assertTrue(getToggler3b2().isVisible());
        assertSecondTableDataVisible();
        assertThirdTableDataVisible();
    }
}
