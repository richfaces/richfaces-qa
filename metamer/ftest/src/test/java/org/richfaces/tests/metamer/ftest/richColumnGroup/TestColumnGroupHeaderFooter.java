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
package org.richfaces.tests.metamer.ftest.richColumnGroup;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes.rendered;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Utils;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.richColumn.AbstractColumnTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestColumnGroupHeaderFooter extends AbstractColumnTest {

    private WebElement footerCell(int row, int column) {
        return getTable().getFooter().getCell(row, column);
    }

    private int footerCount(int row) {
        return getTable().getFooter().getRow(row).findElements(By.className("rf-dt-ftr-c")).size();
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richColumnGroup/headerFooter.xhtml");
    }

    private WebElement headerCell(int row, int column) {
        return getTable().getHeader().getCell(row, column);
    }

    private int headerCount(int row) {
        return getTable().getHeader().getRow(row).findElements(By.className("rf-dt-hdr-c")).size();
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        int bodyRowCount = getTable().advanced().getNumberOfVisibleRows();

        assertTrue(bodyRowCount > 0);

        assertEquals(getTable().getHeader().getRows().size(), 2);
        assertEquals(headerCount(0), 2);
        assertEquals(headerCount(1), 4);
        assertEquals(headerCell(0, 0).getAttribute("rowspan"), "2");

        assertEquals(getTable().getFooter().getRows().size(), 3);
        assertEquals(footerCount(0), 1);
        assertEquals(footerCount(1), 5);
        assertEquals(footerCount(2), 1);
        assertEquals(footerCell(1, 0).getAttribute("rowspan"), "2");

        columnAttributes.set(rendered, false);

        assertEquals(getTable().advanced().getNumberOfVisibleRows(), bodyRowCount);
        assertFalse(Utils.isVisible(getTable().getHeader().getRootElement()));
        assertFalse(Utils.isVisible(getTable().getFooter().getRootElement()));
    }
}
