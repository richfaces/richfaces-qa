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
package org.richfaces.tests.showcase.ftest.webdriver.ftest.richOrderingList;

import org.jboss.arquillian.graphene.page.Page;
import org.richfaces.tests.showcase.ftest.webdriver.AbstractWebDriverTest;
import org.richfaces.tests.showcase.ftest.webdriver.page.richOrderingList.OrderingListPage;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;


/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestRichOrderingListSimple extends AbstractWebDriverTest {

    @Page
    private OrderingListPage page;

    @Override
    protected OrderingListPage getPage() {
        return page;
    }

    @Test(groups = {"RF-11773"})
    public void testSelectionBottom() {
        getPage().select(getPage().getNumberOfItems() - 1);
        assertFalse(getPage().isBottomButtonEnabled());
        assertFalse(getPage().isDownButtonEnabled());
        assertTrue(getPage().isTopButtonEnabled());
        assertTrue(getPage().isUpButtonEnabled());
    }

    @Test(groups = {"RF-11773"})
    public void testSelectionMiddle() {
        getPage().select(2);
        assertTrue(getPage().isBottomButtonEnabled());
        assertTrue(getPage().isDownButtonEnabled());
        assertTrue(getPage().isTopButtonEnabled());
        assertTrue(getPage().isUpButtonEnabled());
    }

    @Test(groups = {"RF-11773"})
    public void testSelectionTop() {
        getPage().select(0);
        assertTrue(getPage().isBottomButtonEnabled());
        assertTrue(getPage().isDownButtonEnabled());
        assertFalse(getPage().isTopButtonEnabled());
        assertFalse(getPage().isUpButtonEnabled());
    }
}