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
import org.richfaces.tests.page.fragments.impl.list.pick.RichFacesSimplePickList;
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
    private RichFacesSimplePickList pickList;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/simple.xhtml");
    }

    @Test
    public void testAdd() {
        addButton.click();
        Assert.assertEquals(pickList.target().getItems().size(), 0);
        pickList.source().selectItemsByIndex(0, 1);
        String selectedItems = pickList.source().getSelectedItems().toString();
        addButton.click();
        Assert.assertEquals(pickList.target().getItems().size(), 2);
        Assert.assertEquals(pickList.target().toString(), selectedItems);
    }

    @Test
    public void testAddAll() {
        int size = pickList.source().getItems().size();
        addAllButton.click();
        Assert.assertEquals(pickList.target().getItems().size(), size);

    }

    @Test
    public void testRemove() {
        addAllButton.click();
        pickList.target().selectItemsByIndex(0);
        String selectedItems = pickList.target().getSelectedItems().toString();
        removeButton.click();
        Assert.assertEquals(pickList.source().getItems().size(), 1);
        Assert.assertEquals(pickList.source().getItems().toString(), selectedItems);
    }

    @Test
    public void testRemoveAll() {
        int size = pickList.source().getItems().size();
        testAddAll();
        removeAllButton.click();
        Assert.assertEquals(pickList.source().getItems().size(), size);
        Assert.assertEquals(pickList.target().getItems().size(), 0);
    }

    @Test
    public void testTargetList() {
        getTargetListLength.click();
        int size = value.getIntValue();
        Assert.assertEquals(size, 0);
        addAllButton.click();
        getTargetListLength.click();
        size = value.getIntValue();
        Assert.assertEquals(size, pickList.target().getItems().size());
    }

    @Test
    public void testSourceList() {
        getSourceListLength.click();
        int size = value.getIntValue();
        Assert.assertEquals(size, pickList.source().getItems().size());
        addAllButton.click();
        getSourceListLength.click();
        size = value.getIntValue();
        Assert.assertEquals(size, 0);
    }
}
