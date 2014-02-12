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

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.pickList.RichFacesPickList;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickListJSApi extends AbstractWebDriverTest {

    @FindBy(id = "addFirstFourItems")
    private WebElement addFirstFourButton;
    @FindBy(id = "addAllItems")
    private WebElement addAllButton;
    @FindBy(id = "removeItemsFirstThree")
    private WebElement removeFirstThreeButton;
    @FindBy(id = "removeAllItems")
    private WebElement removeAllButton;
    @FindBy(css = "[id$=pickList]")
    private RichFacesPickList pickList;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/simple.xhtml");
    }

    @Test
    public void testAddItems() {
        addFirstFourButton.click();
        Assert.assertEquals(pickList.advanced().getTargetList().size(), 4);
        Assert.assertEquals(pickList.advanced().getTargetList().getItem(0).getText(), "Alabama");
    }

    @Test
    public void testRemoveItems() {
        testAddItems();
        removeFirstThreeButton.click();
        Assert.assertEquals(pickList.advanced().getTargetList().size(), 1);
        Assert.assertEquals(pickList.advanced().getTargetList().getItem(0).getText(), "Arkansas");
        Assert.assertEquals(pickList.advanced().getSourceList().getItem(0).getText(), "Alabama");
    }

    @Test
    public void testAddAllItems() {
        int size = pickList.advanced().getSourceList().size();
        addAllButton.click();
        Assert.assertEquals(pickList.advanced().getTargetList().size(), size);
    }

    @Test
    public void testRemoveAllItems() {
        int size = pickList.advanced().getSourceList().size();
        testAddAllItems();
        removeAllButton.click();
        Assert.assertEquals(pickList.advanced().getSourceList().size(), size);
        Assert.assertEquals(pickList.advanced().getTargetList().size(), 0);
    }
}