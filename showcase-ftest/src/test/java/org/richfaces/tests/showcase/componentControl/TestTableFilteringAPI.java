/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.componentControl;

import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;

import java.util.Iterator;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.dataTable.AbstractDataIterationWithCars;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestTableFilteringAPI extends AbstractDataIterationWithCars {
	
	/* ***********************************************************************
	 * Locators
	 *************************************************************************/
	
	protected JQueryLocator filterValues = jq("a[name*=form]:eq({0})"); 
	protected JQueryLocator tableRow = jq("tbody.rf-dt-b tr[class*=rf-dt-r]");
	
	/* *************************************************************************
	 * Tests
	 ***************************************************************************/
	
	@Test
	public void testFilteringOnTheFirstPage() {
		
		for( int i = 0; i < 6; i++ ) {
			
			JQueryLocator filterValue = filterValues.format(i);
			guardXhr(selenium).click( filterValue );
			
			for( Iterator<JQueryLocator> j = tableRow.iterator(); j.hasNext(); ) {
				
				Car carFromRow = retrieveCarFromRow(j.next(), 0, 0);
				
				String expectedVendor = selenium.getText(filterValue);
				assertEquals( carFromRow.getVendor(), expectedVendor, "The table should contain cars with vendors " + expectedVendor);
			}
		}
		
	}

}
