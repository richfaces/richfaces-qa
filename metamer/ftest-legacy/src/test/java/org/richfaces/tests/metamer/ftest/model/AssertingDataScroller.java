/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;

/**
 * Provides DataScroller control methods with assertions about scroller state.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class AssertingDataScroller extends DataScroller {

    public AssertingDataScroller(JQueryLocator root) {
        super(root);
    }

    public AssertingDataScroller(String name, JQueryLocator root) {
        super(name, root);
    }

    @Override
    public void gotoPage(int pageNumber) {
        int startCount = this.getCountOfVisiblePages();
        super.gotoPage(pageNumber);

        int currentPage = this.getCurrentPage();
        assertEquals(this.getCountOfVisiblePages(), startCount);
        assertEquals(currentPage, pageNumber);

        assertEquals(isFirstPageButtonDisabled(), this.isFirstPage());
        assertEquals(isLastPageButtonDisabled(), this.isLastPage());

        if (fastStep != null) {
            assertEquals(isFastRewindDisabled(), currentPage - fastStep < 1);
            if (lastPage != null) {
                assertEquals(isFastForwardDisabled(), currentPage + fastStep > lastPage);
            }
        }
    }

    @Override
    public void gotoFirstPage() {
        super.gotoFirstPage();
        assertTrue(isFastRewindDisabled());
        assertTrue(isFirstPageButtonDisabled());
    }

    @Override
    public void gotoLastPage() {
        super.gotoLastPage();
        assertTrue(isFastForwardDisabled());
        assertTrue(isLastPageButtonDisabled());
    }

    public boolean isFastForwardDisabled() {
        return selenium.belongsClass(fastForwardButton, CLASS_DISABLED);
    }

    public boolean isFastRewindDisabled() {
        return selenium.belongsClass(fastRewindButton, CLASS_DISABLED);
    }

    public boolean isLastPageButtonDisabled() {
        return selenium.belongsClass(lastPageButton, CLASS_DISABLED);
    }

    public boolean isFirstPageButtonDisabled() {
        return selenium.belongsClass(firstPageButton, CLASS_DISABLED);
    }
}
