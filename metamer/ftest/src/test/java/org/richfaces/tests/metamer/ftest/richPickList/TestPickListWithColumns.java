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
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.pickListAttributes;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.page.fragments.impl.list.ordering.RichFacesSimpleOrderingListItem;
import org.richfaces.tests.page.fragments.impl.list.pick.RichFacesPickList;
import org.richfaces.tests.page.fragments.impl.list.pick.RichFacesSourceList;
import org.richfaces.tests.page.fragments.impl.list.pick.RichFacesTargetList;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickListWithColumns extends AbstractWebDriverTest {

    @FindBy(css = "[id$=pickList]")
    private ThreeColumnPickList picklist;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/columnLayout.xhtml");
    }

    @Test
    public void testColumnClasses() {
        String testedClass = "metamer-ftest-class";
        pickListAttributes.set(PickListAttributes.columnClasses, testedClass);
        for (ThreeColumnListItem li : picklist.source().getItems()) {
            for (WebElement e : li.getItemElement().findElements(By.tagName("td"))) {
                assertTrue(e.getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
            }
        }
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(picklist.source().getHeaderElement(), BasicAttributes.headerClass);
        testStyleClass(picklist.target().getHeaderElement(), BasicAttributes.headerClass);
    }

    public static class ThreeColumnPickList extends RichFacesPickList<ThreeColumnListItem, ThreeColumnSourceList, ThreeColumnTargetList> {

        @Override
        protected Class<ThreeColumnSourceList> getSourceListType() {
            return ThreeColumnSourceList.class;
        }

        @Override
        protected Class<ThreeColumnTargetList> getTargetListType() {
            return ThreeColumnTargetList.class;
        }
    }

    public static class ThreeColumnSourceList extends RichFacesSourceList<ThreeColumnListItem> {

        @Override
        protected Class<ThreeColumnListItem> getListItemType() {
            return ThreeColumnListItem.class;
        }
    }

    public static class ThreeColumnTargetList extends RichFacesTargetList<ThreeColumnListItem> {

        @Override
        protected Class<ThreeColumnListItem> getListItemType() {
            return ThreeColumnListItem.class;
        }
    }

    public static class ThreeColumnListItem extends RichFacesSimpleOrderingListItem {

        @FindBy(tagName = "td")
        private List<WebElement> columns;

        public String city() {
            return columns.get(1).getText().trim();
        }

        public String flag() {
            return columns.get(0).findElement(By.tagName("image")).getAttribute("src").trim();
        }

        public String state() {
            return columns.get(2).getText().trim();
        }
    }
}
