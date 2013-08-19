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
package org.richfaces.tests.metamer.ftest.richSelect;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.common.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.select.RichFacesSelect;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestSelectJSApi extends AbstractWebDriverTest {

    @FindBy(css = "input[id$=getValue]")
    private WebElement getValueButton;
    @FindBy(css = "input[id$=setValue]")
    private WebElement setValueButton;
    @FindBy(css = "input[id$=getLabel]")
    private WebElement getLabelButton;
    @FindBy(css = "input[id$=showPopup]")
    private WebElement showPopupButton;
    @FindBy(css = "input[id$=hidePopup]")
    private WebElement hidePopupButton;
    @FindBy(css = "input[id$=value]")
    private TextInputComponentImpl value;
    @FindBy(css = "div[id$=select]")
    private RichFacesSelect select;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richSelect/simple.xhtml");
    }

    private String getValue() {
        return value.getStringValue();
    }

    @Test
    public void testGetValue() {
        Graphene.guardAjax(select.openSelect()).select("Hawaii");
        getValueButton.click();
        Assert.assertEquals(getValue(), "Hawaii");
    }

    @Test
    public void testSetValue() {
        setValueButton.click();// sets value to Arizona
        Assert.assertEquals(select.advanced().getInput().getStringValue(), "Arizona");
    }

    @Test
    public void testGetLabel() {
        getLabelButton.click();
        Assert.assertEquals(getValue(), "Click here to edit");
    }

    @Test
    public void testShowPopup() {
        Utils.triggerJQ(executor, "mouseover", showPopupButton);
        select.advanced().waitUntilSuggestionsAreVisible();
    }

    @Test
    public void testHidePopup() {
        select.openSelect();
        select.advanced().waitUntilSuggestionsAreVisible();
        Utils.triggerJQ(executor, "mouseover", hidePopupButton);
        select.advanced().waitUntilSuggestionsAreNotVisible();
    }
}
