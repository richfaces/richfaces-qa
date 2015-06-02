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
package org.richfaces.tests.metamer.ftest.richColumn;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestColumnWidth extends AbstractWebDriverTest {

    private final Attributes<ColumnAttributes> attributes = getAttributes();
    @FindByJQuery(value = ".rf-edt[id$=richDataTable] .rf-edt-c:first")
    private WebElement firstColumn;

    private String getColumnWidth() {
        return firstColumn.getCssValue("width");
    }

    @Override
    public String getComponentTestPagePath() {
        return "richColumn/simpleWithEDT.xhtml";
    }

    @Test
    @CoversAttributes("width")
    @Templates("plain")
    public void testWidth() {
        String width = "400px";
        attributes.set(ColumnAttributes.width, width);
        assertEquals(getColumnWidth(), width);
        width = "150px";
        attributes.set(ColumnAttributes.width, width);
        assertEquals(getColumnWidth(), width);
    }
}
