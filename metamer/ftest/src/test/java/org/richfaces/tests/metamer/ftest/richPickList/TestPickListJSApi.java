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
package org.richfaces.tests.metamer.ftest.richPickList;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.pickList.RichFacesPickList;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickListJSApi extends AbstractWebDriverTest {

    @FindBy(id = "getSourceList")
    private WebElement getSourceListLength;
    @FindBy(id = "getTargetList")
    private WebElement getTargetListLength;
    @FindBy(id = "add")
    private WebElement addButton;
    @FindBy(id = "addAll")
    private WebElement addAllButton;
    @FindBy(id = "remove")
    private WebElement removeButton;
    @FindBy(id = "removeAll")
    private WebElement removeAllButton;
    @FindBy(css = "input[id$=value]")
    private TextInputComponentImpl value;
    @FindBy(css = "[id$=pickList]")
    private RichFacesPickList pickList;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/simple.xhtml");
    }

    @Test
    public void testAdd() {
        addButton.click();
        Assert.assertTrue(pickList.advanced().getTargetList().isEmpty(), "Target list should be empty.");
        String text = pickList.advanced().getSourceList().getItem(0).getText();
        pickList.advanced().getSourceList().getItem(0).select();
        addButton.click();
        Assert.assertEquals(pickList.advanced().getTargetList().size(), 1);
        Assert.assertEquals(pickList.advanced().getTargetList().getItem(0).getText(), text);
    }

    @Test
    public void testAddAll() {
        int size = pickList.advanced().getSourceList().size();
        addAllButton.click();
        Assert.assertEquals(pickList.advanced().getTargetList().size(), size);
    }

    @Test
    public void testRemove() {
        addAllButton.click();
        String text = pickList.advanced().getTargetList().getItem(0).getText();
        pickList.advanced().getTargetList().getItem(0).select();
        removeButton.click();
        Assert.assertEquals(pickList.advanced().getSourceList().size(), 1);
        Assert.assertEquals(pickList.advanced().getSourceList().getItem(0).getText(), text);
    }

    @Test
    public void testRemoveAll() {
        int size = pickList.advanced().getSourceList().size();
        testAddAll();
        removeAllButton.click();
        Assert.assertEquals(pickList.advanced().getSourceList().size(), size);
        Assert.assertEquals(pickList.advanced().getTargetList().size(), 0);
    }

    @Test
    public void testTargetList() {
        getTargetListLength.click();
        int size = value.getIntValue();
        Assert.assertEquals(size, 0);
        addAllButton.click();
        getTargetListLength.click();
        size = value.getIntValue();
        Assert.assertEquals(size, pickList.advanced().getTargetList().size());
    }

    @Test
    public void testSourceList() {
        getSourceListLength.click();
        int size = value.getIntValue();
        Assert.assertEquals(size, pickList.advanced().getSourceList().size());
        addAllButton.click();
        getSourceListLength.click();
        size = value.getIntValue();
        Assert.assertEquals(size, 0);
    }
}
