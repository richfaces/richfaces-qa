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
package org.richfaces.tests.metamer.ftest.richList;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.richList.TestListAttributes.ListType;
import org.richfaces.tests.metamer.ftest.webdriver.Attributes;
import org.richfaces.tests.page.fragments.impl.list.RichFacesList;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestListFragment extends AbstractListTest {

    @FindBy(css = "[id$=List]")
    private RichFacesList list;

    private static final int ROW_COUNT = 20;
    private static final int ROW_COUNT_DEFINITIONS = ROW_COUNT * 2;
    private final Attributes<ListAttributes> attributes = getAttributes();

    @Use(empty = false, enumeration = true)
    @Inject
    private ListType type;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richList/simple.xhtml");
    }

    @Test
    @Templates("plain")
    public void testRows() {
        attributes.set(ListAttributes.type, type.getValue());
        if (type.equals(ListType.DEFINITIONS)) {
            for (int position = 0; position < ROW_COUNT; position += 2) {
                assertEquals(list.getItem(2 * position + 1).getText(), employees.get(position).getName());
            }
        } else {
            for (int position = 0; position < ROW_COUNT; position += 2) {
                assertEquals(list.getItem(position).getText(), employees.get(position).getName());
            }
        }
    }

    @Test
    @Templates("plain")
    public void testSize() {
        attributes.set(ListAttributes.type, type.getValue());
        if (type.equals(ListType.DEFINITIONS)) {
            assertEquals(list.size(), ROW_COUNT_DEFINITIONS);
        } else {
            assertEquals(list.size(), ROW_COUNT);
        }
    }
}
