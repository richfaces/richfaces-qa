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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 *
 * It checks whether the moving is OK.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestOrderingList extends AbstractOrderingListTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/withColumn.xhtml");
    }

    @Test
    public void testInit() {
        assertVisible(orderingList.advanced().getRootElement(), "The ordering list should be visible.");
        assertButtonEnabled(orderingList.advanced().getBottomButtonElement(), "bottom");
        assertButtonEnabled(orderingList.advanced().getDownButtonElement(), "down");
        assertButtonEnabled(orderingList.advanced().getTopButtonElement(), "top");
        assertButtonEnabled(orderingList.advanced().getUpButtonElement(), "up");
    }

    @Test
    public void testSelectFirst() {
        orderingList.select(0);
        checkButtonsStateTop();
    }

    @Test
    public void testSelectLast() {
        orderingList.select(ChoicePickerHelper.byIndex().last());
        checkButtonsStateBottom();
    }

    @Test
    public void testSelectMiddle() {
        orderingList.select(2);
        checkButtonsStateMiddle();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-13339")
    public void testSubmit() {
        String firstBefore = orderingList.advanced().getItemsElements().get(0).getText();
        String secondBefore = orderingList.advanced().getItemsElements().get(1).getText();
        orderingList.select(1).putItBefore(0);
        String firstAfter = orderingList.advanced().getItemsElements().get(0).getText();
        String secondAfter = orderingList.advanced().getItemsElements().get(1).getText();
        assertEquals(firstAfter, secondBefore, "Items should be moved.");
        assertEquals(firstBefore, secondAfter, "Items should be moved.");
        submit();
        firstAfter = orderingList.advanced().getItemsElements().get(0).getText();
        secondAfter = orderingList.advanced().getItemsElements().get(1).getText();
        assertEquals(firstAfter, secondBefore, "After submitting the ordering list doesn't preserve the chosen order.");
        assertEquals(firstBefore, secondAfter, "After submitting the ordering list doesn't preserve the chosen order.");
        submit();
        firstAfter = orderingList.advanced().getItemsElements().get(0).getText();
        secondAfter = orderingList.advanced().getItemsElements().get(1).getText();
        assertEquals(firstAfter, secondBefore, "After second submitting the ordering list doesn't preserve the chosen order.");
        assertEquals(firstBefore, secondAfter, "After second submitting the ordering list doesn't preserve the chosen order.");
    }
}
