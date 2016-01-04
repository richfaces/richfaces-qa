/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.tests.metamer.ftest.richMenuGroup;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.dropDownMenu.RichFacesDropDownMenu;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.richSelect.TestRF14018.JSErrorStorage;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF11915 extends AbstractWebDriverTest {

    @FindByJQuery(".rf-tb-itm:eq(0)")
    private RichFacesDropDownMenu fileDropDownMenu;
    @JavaScript
    private JSErrorStorage jsErrorStorage;
    @FindBy(css = "[id$=output]")
    private WebElement output;
    @FindByJQuery(value = ".rf-ddm-lbl-dec:eq(0)")
    private WebElement target1;

    private void chechNoJSErrorsArePresent() {
        assertEquals(jsErrorStorage.getMessages().size(), 0, "There should be no error messages. Had: " + jsErrorStorage.getMessages() + '.');
    }

    @Override
    public String getComponentTestPagePath() {
        return "richMenuGroup/rf-11915.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11915")
    public void testOpeningOfDisabledOnlyGroupWontThrowJSErrors() {
        // there should be no JS errors present on page load
        chechNoJSErrorsArePresent();
        // setup show event of the page fragment
        fileDropDownMenu.advanced().setShowEvent(Event.CLICK);
        // show the menu group with disabled-only items, this should throw the JS Error
        fileDropDownMenu.expandGroup("Save As...", target1);
        // blur to hide menu
        getMetamerPage().getResponseDelayElement().click();
        fileDropDownMenu.advanced().waitUntilIsNotVisible().perform();
        chechNoJSErrorsArePresent();
        // select some item to check menu is working
        Graphene.guardAjax(fileDropDownMenu).selectItem("New", target1);
        assertEquals(output.getText(), "New");
        chechNoJSErrorsArePresent();
    }
}
