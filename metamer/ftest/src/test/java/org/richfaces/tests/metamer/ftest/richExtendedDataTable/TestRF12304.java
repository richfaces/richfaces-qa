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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision: 22407 $
 */
public class TestRF12304 extends AbstractWebDriverTest {

    @Page
    private RF12304Page page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/rf-12304.xhtml");
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12304")
    @Templates(value = { "plain" })
    public void testMultiSelectionUsingControlIterationComponents() {
        // selecting from first table should work
        page.getThirdRowFirstTable().click();
        assertTrue(page.isSelected(page.getThirdRowFirstTable()), "The row from first table should be selected!");

        // this is the bug part
        page.getThirdRowSecondTable().click();
        assertTrue(page.isSelected(page.getThirdRowSecondTable()), "The row from second table should be selected!");
    }

//    @Override
//    protected RF12304Page createPage() {
//        return new RF12304Page();
//    }

}
