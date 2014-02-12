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
package org.richfaces.tests.metamer.ftest.richPickList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.fragment.pickList.RichFacesPickList;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickListFragment extends AbstractWebDriverTest {

    @FindBy(css = "[id$=pickList]")
    private RichFacesPickList pickList;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/simple.xhtml");
    }

    @Test
    public void testAdd() {
        int sizeS = pickList.advanced().getSourceList().size();
        int sizeT = pickList.advanced().getTargetList().size();
        assertEquals(sizeT, 0);
        String itemText = pickList.advanced().getSourceList().getItem(0).getText();

        pickList.add(ChoicePickerHelper.byIndex().index(0));

        assertEquals(pickList.advanced().getSourceList().size(), sizeS - 1);
        assertEquals(pickList.advanced().getTargetList().size(), sizeT + 1);
        assertEquals(pickList.advanced().getTargetList().getItem(0).getText(), itemText);
    }

    @Test
    public void testAddAll() {
        int sizeS = pickList.advanced().getSourceList().size();
        int sizeT = pickList.advanced().getTargetList().size();
        assertEquals(sizeT, 0);

        pickList.addAll();

        assertEquals(pickList.advanced().getSourceList().size(), 0);
        assertEquals(pickList.advanced().getTargetList().size(), sizeS);
    }

    @Test
    public void testAddMultiple() {
        int sizeS = pickList.advanced().getSourceList().size();
        int sizeT = pickList.advanced().getTargetList().size();
        assertEquals(sizeT, 0);

        pickList.addMultiple(ChoicePickerHelper.byIndex().first().last().index(7));

        assertEquals(pickList.advanced().getSourceList().size(), sizeS - 3);
        assertEquals(pickList.advanced().getTargetList().size(), sizeT + 3);
    }

    @Test
    public void testRemove() {
        testAdd();
        int sizeS = pickList.advanced().getSourceList().size();
        int sizeT = pickList.advanced().getTargetList().size();
        assertEquals(sizeT, 1);
        String itemText = pickList.advanced().getTargetList().getItem(0).getText();

        pickList.remove(ChoicePickerHelper.byIndex().index(0));

        assertEquals(pickList.advanced().getSourceList().size(), sizeS + 1);
        assertEquals(pickList.advanced().getTargetList().size(), sizeT - 1);
        assertEquals(pickList.advanced().getSourceList().getItem(ChoicePickerHelper.byIndex().last()).getText(), itemText);
    }

    @Test
    public void testRemoveAll() {
        int sizeS = pickList.advanced().getSourceList().size();
        int sizeT = pickList.advanced().getTargetList().size();
        testAddMultiple();

        pickList.removeAll();

        assertEquals(pickList.advanced().getSourceList().size(), sizeS);
        assertEquals(pickList.advanced().getTargetList().size(), sizeT);
    }

    @Test
    public void testRemoveMultiple() {
        testAddMultiple();
        int sizeS = pickList.advanced().getSourceList().size();
        int sizeT = pickList.advanced().getTargetList().size();
        assertEquals(sizeT, 3);

        pickList.removeMultiple(ChoicePickerHelper.byIndex().first().index(1));

        assertEquals(pickList.advanced().getSourceList().size(), sizeS + 2);
        assertEquals(pickList.advanced().getTargetList().size(), sizeT - 2);
    }
}
