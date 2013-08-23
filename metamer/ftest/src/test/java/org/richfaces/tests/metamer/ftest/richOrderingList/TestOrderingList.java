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

import java.net.URL;

import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
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
        assertButtonDisabled(orderingList.advanced().getBottomButtonElement(), "The button [bottom] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getDownButtonElement(), "The button [down] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getTopButtonElement(), "The button [top] should be disabled.");
        assertButtonDisabled(orderingList.advanced().getUpButtonElement(), "The button [up] should be disabled.");
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
    public void testSubmit() {
        String firstBefore = orderingList.advanced().getItems().get(0).getText();
        String secondBefore = orderingList.advanced().getItems().get(1).getText();
        orderingList.select(1).putItBefore(0);
        submit();
        String firstAfter = orderingList.advanced().getItems().get(0).getText();
        String secondAfter = orderingList.advanced().getItems().get(1).getText();
        assertEquals(firstAfter, secondBefore, "After submitting the ordering list doesn't preserve the chosen order.");
        assertEquals(firstBefore, secondAfter, "After submitting the ordering list doesn't preserve the chosen order.");
        submit();
        firstAfter = orderingList.advanced().getItems().get(0).getText();
        secondAfter = orderingList.advanced().getItems().get(1).getText();
        assertEquals(firstAfter, secondBefore, "After submitting the ordering list doesn't preserve the chosen order.");
        assertEquals(firstBefore, secondAfter, "After submitting the ordering list doesn't preserve the chosen order.");
    }
}
