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

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.arquillian.ajocado.dom.Attribute.ROWSPAN;

import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.columnAttributes;
import static org.richfaces.tests.metamer.ftest.richColumn.ColumnAttributes.rendered;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.richColumn.AbstractColumnModelTest;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22738 $
 */
public class TestColumnGroupHeaderFooter extends AbstractColumnModelTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richColumnGroup/headerFooter.xhtml");
    }

    @Test
    public void testRendered() {
        int bodyRowCount = model.getBodyRowCount();

        assertTrue(bodyRowCount > 0);

        assertEquals(model.getHeaderRowCount(), 2);
        assertEquals(model.getHeaderRowCellCount(1), 2);
        assertEquals(model.getHeaderRowCellCount(2), 4);
        assertEquals(selenium.getAttribute(model.getHeaderCell(1, 1).getAttribute(ROWSPAN)), "2");

        assertEquals(model.getFooterRowCount(), 3);
        assertEquals(model.getFooterRowCellCount(1), 1);
        assertEquals(model.getFooterRowCellCount(2), 5);
        assertEquals(model.getFooterRowCellCount(3), 1);
        assertEquals(selenium.getAttribute(model.getFooterCell(2, 1).getAttribute(ROWSPAN)), "2");

        columnAttributes.set(rendered, false);

        assertEquals(model.getBodyRowCount(), bodyRowCount);
        assertEquals(model.getHeaderRowCount(), 0);
        assertEquals(model.getFooterRowCount(), 0);
    }
}
