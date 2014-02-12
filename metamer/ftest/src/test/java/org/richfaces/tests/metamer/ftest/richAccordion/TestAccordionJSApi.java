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
package org.richfaces.tests.metamer.ftest.richAccordion;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.accordion.RichFacesAccordion;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAccordionJSApi extends AbstractWebDriverTest {

    @FindBy(css = "[id$=value]")
    private TextInputComponentImpl valueOutput;
    @FindBy(css = "[id$=accordion]")
    private RichFacesAccordion accordion;

    @FindBy(css = "[id$=getItems]")
    private WebElement getItemsButton;

    @FindBy(css = "[id$=getItemsNames]")
    private WebElement getItemsNamesButton;

    @FindBy(css = "[id$=switchTo1]")
    private WebElement switchTo1Button;
    @FindBy(css = "[id$=switchTo2]")
    private WebElement switchTo2Button;
    @FindBy(css = "[id$=switchTo3]")
    private WebElement switchTo3Button;
    @FindBy(css = "[id$=switchTo4]")
    private WebElement switchTo4Button;
    @FindBy(css = "[id$=switchTo5]")
    private WebElement switchTo5Button;

    @FindBy(css = "[id$=firstItem]")
    private WebElement firstItemButton;
    @FindBy(css = "[id$=prevItem]")
    private WebElement prevItemButton;
    @FindBy(css = "[id$=nextItem]")
    private WebElement nextItemButton;
    @FindBy(css = "[id$=lastItem]")
    private WebElement lastItemButton;

    private final Attributes<AccordionAttributes> attributes = getAttributes();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAccordion/simple.xhtml");
    }

    @Test
    @Templates(value = { "plain" })
    public void testFirstItem() {
        firstItemButton.click();
        assertEquals(valueOutput.getStringValue(), "item1");
    }

    @Test
    @Templates(value = { "plain" })
    public void testGetItems() {
        getItemsButton.click();
        assertEquals(valueOutput.getIntValue(), 5, "There should be 5 items");
    }

    @Test
    @Templates(value = { "plain" })
    public void testGetItemsNames() {
        getItemsNamesButton.click();
        assertEquals(valueOutput.getStringValue(), "item1,item2,item3,item4,item5");
    }

    @Test
    @Templates(value = { "plain" })
    public void testLastItem() {
        lastItemButton.click();
        assertEquals(valueOutput.getStringValue(), "item5");
    }

    @Test
    @Templates(value = { "plain" })
    public void testNextItem() {
        nextItemButton.click();
        assertEquals(valueOutput.getStringValue(), "item2");

        Graphene.guardAjax(switchTo5Button).click();
        nextItemButton.click();
        assertEquals(valueOutput.getStringValue(), "null");
    }

    @Test
    @Templates(value = { "plain" })
    public void testNextItemWithCycledSwitching() {
        attributes.set(AccordionAttributes.cycledSwitching, Boolean.TRUE);

        nextItemButton.click();
        assertEquals(valueOutput.getStringValue(), "item2");

        Graphene.guardAjax(switchTo5Button).click();
        nextItemButton.click();
        assertEquals(valueOutput.getStringValue(), "item1");

    }

    @Test
    @Templates(value = { "plain" })
    public void testPreviousItem() {
        prevItemButton.click();
        assertEquals(valueOutput.getStringValue(), "null");

        Graphene.guardAjax(switchTo5Button).click();
        prevItemButton.click();
        assertEquals(valueOutput.getStringValue(), "item4");
    }

    @Test
    @Templates(value = { "plain" })
    public void testPreviousItemWithCycledSwitching() {
        attributes.set(AccordionAttributes.cycledSwitching, Boolean.TRUE);

        prevItemButton.click();
        assertEquals(valueOutput.getStringValue(), "item5");

        Graphene.guardAjax(switchTo5Button).click();
        prevItemButton.click();
        assertEquals(valueOutput.getStringValue(), "item4");
    }

    @Test
    @Templates(value = { "plain" })
    public void testSwitchTo() {
        List<WebElement> buttons = Lists.newArrayList(switchTo1Button, switchTo2Button, switchTo3Button, switchTo5Button);
        List<String> headers = Lists.newArrayList("Item 1", "Item 2", "Item 3", "Item 5");
        for (int i = 0; i < buttons.size(); i++) {
            Graphene.guardAjax(buttons.get(i)).click();
            assertEquals(accordion.advanced().getActiveItem().advanced().getHeader(), headers.get(i));
        }

        // switch to disabled item
        Graphene.guardNoRequest(switchTo4Button).click();
        assertEquals(accordion.advanced().getActiveItem().advanced().getHeader(), "Item 5", "Active item should not change.");
    }
}
