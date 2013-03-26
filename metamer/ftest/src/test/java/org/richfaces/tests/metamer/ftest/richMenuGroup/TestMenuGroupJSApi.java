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
package org.richfaces.tests.metamer.ftest.richMenuGroup;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import java.net.URL;
import org.jboss.arquillian.ajocado.dom.Event;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richMenuGroup/simple.xhtml
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestMenuGroupJSApi extends AbstractWebDriverTest {

    @FindBy(css = "div[id$=menuGroup4_list]")
    private WebElement groupList;
    @FindBy(css = "div[id$=menu1_list]")
    private WebElement menuList;
    @FindBy(css = "div[id$=menuItem41]")
    private WebElement menuItem41;
    @FindBy(id = "show")
    private WebElement showButton;
    @FindBy(id = "hide")
    private WebElement hideButton;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMenuGroup/simple.xhtml");
    }

    @Test
    public void testHide() {
        fireEvent(hideButton, Event.MOUSEOVER);
        Graphene.waitGui().until().element(menuList).is().visible();
        assertNotVisible(menuItem41, "Save button should not be visible.");
        assertNotVisible(groupList, "Group list should not be visible.");
    }

    @Test
    public void testShow() {
        fireEvent(showButton, Event.MOUSEOVER);
        Graphene.waitGui().until().element(menuList).is().visible();
        assertVisible(groupList, "Group list is not visible.");
        assertVisible(menuItem41, "Save button is not visible.");
    }
}
