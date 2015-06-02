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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestRF11679 extends AbstractWebDriverTest {

    @FindBy(css = ".rf-edt-b[id$='edt:b']")
    private WebElement edt;

    private int getEDTHeight() {
        return Integer.parseInt(edt.getCssValue("height").replace("px", ""));
    }

    @Override
    public String getComponentTestPagePath() {
        return "richExtendedDataTable/rf-11679.xhtml";
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-11679")
    @Templates("plain")
    public void testEDTResizesWithWindow() {
        Dimension sizeBefore = driver.manage().window().getSize();
        try {
            final int tolerance = 10; // px
            // save the height of EDT
            final int edtHeightBefore = getEDTHeight();
            // simulate browser's window resizing
            driver.manage().window().setSize(new Dimension(sizeBefore.getWidth(), 250));
            waiting(200);
            driver.manage().window().setSize(new Dimension(sizeBefore.getWidth(), 350));
            waiting(200);
            // resize browser's window back to original size
            driver.manage().window().setSize(sizeBefore);
            waiting(200);
            // check height of EDT after browser's window maximized
            Graphene.waitGui().until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver t) {
                    return Math.abs(getEDTHeight() - edtHeightBefore) <= tolerance;
                }

                @Override
                public String toString() {
                    return "height of the EDT is resized back to the original size.";
                }
            });
        } finally {
            driver.manage().window().setSize(sizeBefore);
        }
    }

}
