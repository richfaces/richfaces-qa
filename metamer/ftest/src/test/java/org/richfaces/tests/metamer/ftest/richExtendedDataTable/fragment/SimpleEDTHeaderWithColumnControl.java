/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable.fragment;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.webdriver.utils.MetamerJavascriptUtils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleEDTHeaderWithColumnControl extends SimpleEDTHeader {

    @ArquillianResource
    private WebDriver browser;
    private ColumnControl cc;
    @FindBy(className = "rf-edt-colctrl-btn")
    private WebElement columnControlButton;
    @JavaScript
    private MetamerJavascriptUtils jsUtils;

    public ColumnControl openColumnControl() {
        if (cc == null) {
            cc = Graphene.createPageFragment(ColumnControl.class, browser.findElement(By.className("rf-edt-colctrl")));
        }
        if (!cc.isVisible()) {
            jsUtils.scrollToView(columnControlButton);

            columnControlButton.click();
            cc.waitUntilIsVisible();
        }
        return cc;
    }
}
