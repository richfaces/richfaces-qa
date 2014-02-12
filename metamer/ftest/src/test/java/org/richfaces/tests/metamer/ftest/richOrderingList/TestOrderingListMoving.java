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

import java.net.URL;

import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 *
 * It checks whether the moving is OK.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestOrderingListMoving extends AbstractOrderingListTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/withColumn.xhtml");
    }

    @Test
    public void testMoveFirstBottom() {
        orderingList.select(0);
        moveSelectedToBottom();
    }

    @Test
    public void testMoveFirstDown() {
        orderingList.select(0);
        moveSelectedDown();
    }

    @Test
    public void testMoveLastTop() {
        orderingList.select(ChoicePickerHelper.byIndex().last());
        moveSelectedToTop();
    }

    @Test
    public void testMoveLastUp() {
        orderingList.select(ChoicePickerHelper.byIndex().last());
        moveSelectedUp();
    }

    @Test
    public void testMoveMiddleBottom() {
        orderingList.select(2);
        moveSelectedToBottom();
    }

    @Test
    public void testMoveMiddleDown() {
        orderingList.select(2);
        moveSelectedDown();
    }

    @Test(groups = "smoke")
    public void testMoveMiddleTop() {
        orderingList.select(2);
        moveSelectedToTop();
    }

    @Test
    public void testMoveMiddleUp() {
        orderingList.select(2);
        moveSelectedUp();
    }
}
