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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import javax.xml.bind.JAXBException;

import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;


/**
 * <p>
 * Templates: doesn't work inside iteration components.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
@Use(field = "dataScroller", value = "dataScroller1")
@Templates(exclude = {"a4jRepeat", "hDataTable", "richDataTable", "uiRepeat"})
public class TestScrollerOutsideTable extends AbstractScrollerTest {

    public TestScrollerOutsideTable() throws JAXBException {
    }

    @Test
    @Override
    public void testColumnsAttribute() {
        super.testColumnsAttribute();
    }

    @Test
    @Override
    public void testElementsAttribute() {
        super.testElementsAttribute();
    }

    @Test
    @Override
    public void testFirstAttributeDoesntInfluentScroller() {
        super.testFirstAttributeDoesntInfluentScroller();
    }
}
