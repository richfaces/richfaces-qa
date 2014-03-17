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
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.orderingList.SelectableListItem;
import org.richfaces.fragment.pickList.RichFacesPickList;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPickListWithColumns extends AbstractWebDriverTest {

    private final Attributes<PickListAttributes> pickListAttributes = getAttributes();

    @FindBy(css = "[id$=pickList]")
    private RichFacesPickList picklist;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPickList/columnLayout.xhtml");
    }

    @Test
    public void testColumnClasses() {
        String testedClass = "metamer-ftest-class";
        pickListAttributes.set(PickListAttributes.columnClasses, testedClass);
        for (SelectableListItem li : picklist.advanced().getSourceList().getItems()) {
            for (WebElement e : li.getRootElement().findElements(By.tagName("td"))) {
                assertTrue(e.getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
            }
        }
    }

    @Test
    public void testHeaderClass() {
        testStyleClass(picklist.advanced().getSourceHeaderElement(), BasicAttributes.headerClass);
        testStyleClass(picklist.advanced().getTargetHeaderElement(), BasicAttributes.headerClass);
    }
}
