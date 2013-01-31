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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.testng.annotations.Test;


/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 *
 * It checks whether the moving is OK.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestOrderingList extends AbstractOrderingListTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/withColumn.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(getOrderingList().isOrderingListPresent(), "The ordering list should be present.");
        assertFalse(getOrderingList().isButtonBottomEnabled(), "The button [bottom] should be disabled.");
        assertFalse(getOrderingList().isButtonDownEnabled(), "The button [down] should be disabled.");
        assertFalse(getOrderingList().isButtonTopEnabled(), "The button [top] should be disabled.");
        assertFalse(getOrderingList().isButtonUpEnabled(), "The button [up] should be disabled.");
    }

    @Test
    public void testSelectFirst() {
        getOrderingList().selectItem(0);
        checkButtonsTop();
    }

    @Test
    public void testSelectLast() {
        getOrderingList().selectItem(getOrderingList().getNumberOfItems() - 1);
        checkButtonsBottom();
    }

    @Test
    public void testSelectMiddle() {
        getOrderingList().selectItem(2);
        checkButtonsMiddle();
    }

    @Test
    public void testSubmit() {
        getOrderingList().selectItem(1);
        getOrderingList().moveTop();
        String expectedState = getOrderingList().getItemColumnValue(0, 0);
        submit();
        String foundState = getOrderingList().getItemColumnValue(0, 0);
        assertEquals(expectedState, foundState, "After submitting the ordering list doesn't preserve the chosen order.");
    }

}
