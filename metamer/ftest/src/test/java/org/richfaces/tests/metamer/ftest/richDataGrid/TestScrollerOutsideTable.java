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
package org.richfaces.tests.metamer.ftest.richDataGrid;

import javax.xml.bind.JAXBException;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.richfaces.fragment.dataScroller.DataScroller;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom;

import org.testng.annotations.Test;

/**
 * <p>
 * Templates: doesn't work inside iteration components.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
@Templates(exclude = {"a4jRepeat", "hDataTable", "richDataTable", "uiRepeat"})
public class TestScrollerOutsideTable extends AbstractDataGridScrollerTest {

    @FindByJQuery("span.rf-ds[id$=scroller1]")
    private RichFacesDataScroller dataScroller1;

    @Override
    public DataScroller getDataScroller() {
        return dataScroller1;
    }

    public TestScrollerOutsideTable() throws JAXBException {
    }

    @Test
    @UseWithField(field = "columns", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS1")
    public void testColumnsAttribute() {
        super.testColumnsAttribute();
    }

    @Test
    @UseWithField(field = "elements", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS2")
    public void testElementsAttribute() {
        super.testElementsAttribute();
    }

    @Test
    @UseWithField(field = "first", valuesFrom = ValuesFrom.FROM_FIELD, value = "COUNTS2")
    public void testFirstAttributeDoesntInfluentScroller() {
        super.testFirstAttributeDoesntInfluentScroller();
    }

}
