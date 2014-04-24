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
package org.richfaces.tests.metamer.ftest.richCollapsibleSubTable;

import org.richfaces.fragment.dataScroller.RichFacesDataScroller;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public abstract class PaginationTester {

    protected RichFacesDataScroller dataScroller;
    private int[] testPages;

    public void initializeTestedPages(int lastPage) {
        int l = lastPage;
        testPages = new int[]{ 3, l, l / 2, l - 1 };
        for (int i = 0; i < testPages.length; i++) {
            testPages[i] = Math.min(l, Math.max(1, testPages[i]));
        }
    }

    public void setDataScroller(RichFacesDataScroller dataScroller) {
        this.dataScroller = dataScroller;
    }

    public void testNumberedPages() {
        Integer lastNumber = null;
        for (int pageNumber : testPages) {
            if (lastNumber == (Integer) pageNumber) {
                continue;
            }
            verifyBeforeScrolling();
            dataScroller.switchTo(pageNumber);
            verifyAfterScrolling();
            lastNumber = pageNumber;
        }
    }

    protected abstract void verifyBeforeScrolling();

    protected abstract void verifyAfterScrolling();
}
