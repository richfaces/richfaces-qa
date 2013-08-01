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
package org.richfaces.tests.metamer.ftest.richList;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.page.fragments.impl.dataScroller.RichFacesDataScroller;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class TestListWithScroller extends AbstractListTest {

    @FindBy(css = "span.rf-ds[id$=scroller1]")
    private RichFacesDataScroller scrollerOutsideTable;
    @FindBy(css = "span.rf-ds[id$=scroller2]")
    private RichFacesDataScroller scrollerInTableFooter;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richList/scroller.xhtml");
    }

    @Test
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testScrollerWithRowsAttributeOut() {
        testNumberedPages(scrollerOutsideTable);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11787")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat", "hDataTable", "uiRepeat" })
    public void testScrollerWithRowsAttributeOutIterationComponents() {
        testNumberedPages(scrollerOutsideTable);
    }

    @Test
    @Templates(exclude = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testScrollerWithRowsAttributeIn() {
        testNumberedPages(scrollerInTableFooter);
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-11787")
    @Templates(value = { "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richList", "a4jRepeat" })
    public void testScrollerWithRowsAttributeInIterationComponents() {
        testNumberedPages(scrollerInTableFooter);
    }

    private void testNumberedPages(RichFacesDataScroller dataScroller) {
        final int [] testPages = new int[]{ 3, 10, 1, 9, 2 };
        rows = 20;

        for (int pageNumber : testPages) {
            dataScroller.switchTo(pageNumber);

            first = rows * (dataScroller.getActivePageNumber() - 1);
            verifyList();
        }
    }
}
