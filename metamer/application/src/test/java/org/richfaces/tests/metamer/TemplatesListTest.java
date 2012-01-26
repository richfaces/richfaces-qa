/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

package org.richfaces.tests.metamer;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 21299 $
 */
public class TemplatesListTest {

    private TemplatesList templates;

    @BeforeMethod
    public void setUpTest() {
        templates = new TemplatesList();
    }

    @Test
    public void testNewList() {
        assertEquals(templates.size(), 1, "New list should contain one element");
        assertEquals(templates.get(0), Template.PLAIN, "New list should contain plain template");
    }

    @Test
    public void testSetIntTemplate() {
        templates.set(0, Template.REDDIV);
        assertEquals(templates.size(), 2, "Size after red_div was inserted on position 0");
        assertEquals(templates.get(0), Template.REDDIV, "position 0");
        assertEquals(templates.get(1), Template.PLAIN, "position 1");

        templates.set(1, Template.BLUEDIV);
        assertEquals(templates.size(), 3, "Size after blue_div was inserted on position 1");
        assertEquals(templates.get(0), Template.REDDIV, "position 0");
        assertEquals(templates.get(1), Template.BLUEDIV, "position 1");
        assertEquals(templates.get(2), Template.PLAIN, "position 2");

        templates.set(0, Template.BLUEDIV);
        assertEquals(templates.size(), 3, "Size after blue_div was inserted on position 0");
        assertEquals(templates.get(0), Template.BLUEDIV, "position 0");
        assertEquals(templates.get(1), Template.BLUEDIV, "position 1");
        assertEquals(templates.get(2), Template.PLAIN, "position 2");
    }
}
