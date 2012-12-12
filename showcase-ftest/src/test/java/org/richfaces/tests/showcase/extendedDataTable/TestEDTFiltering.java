/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.tests.showcase.extendedDataTable;

import static org.jboss.arquillian.ajocado.Graphene.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.richfaces.tests.showcase.dataTable.TestTableFiltering;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestEDTFiltering extends TestTableFiltering {

    @Override
    protected String getAdditionToContextRoot() {
        String sampleName = "edt-filtering";

        // demo name - takes last part of package name
        String demoName = this.getClass().getPackage().getName();
        demoName = StringUtils.substringAfterLast(demoName, ".");

        String addition = SimplifiedFormat.format("richfaces/component-sample.jsf?skin=blueSky&demo={0}&sample={1}",
                demoName, sampleName);

        return addition;
    }

    @Override
    public void testNoRowsSatisfyConditions() {
        selenium.type(mileageInput, "1");
        guardXhr(selenium).fireEvent(mileageInput, Event.BLUR);

        selenium.type(vinInput, "RICHFACES 4");//maybe not necessary
        guardXhr(selenium).fireEvent(vinInput, Event.BLUR);

        boolean nothingFoundPresent = selenium.isElementPresent(jq("div.rf-edt-ndt:contains('Nothing found')"));

        assertTrue(nothingFoundPresent, "No rows should satisfy the filter conditions");

        eraseAllInputs();
    }

    protected String getSampleLabel() {
        return "Table Filtering";
    }
}
