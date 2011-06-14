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
package org.richfaces.tests.showcase.extendedDataTable;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestSimpleTable extends AbstractExtendedTableTest {
	
	JQueryLocator tableNonScrollablePart = jq("tbody[id$=tbf]");
	
	/*
	 * Tests
	 * 
	 * The method of testing should be improved in the future, but I chose this way of testing due 
	 * to problems of scrolling with selenium over extended data table 
	 * I also have to test manually the expanding of columns and that there is a possibility to change the
	 * order of columns, it should be implemented in the future too.
	 **********************************************************************************************/
	@Test
	public void testFirstRow() {
		
		JQueryLocator row = tableNonScrollablePart.getChild(jq("tr:eq(0)"));
		
		checkTheRow("Chevrolet", "Corvette", row);
		
	}
	
	@Test
	public void testLastRow() {
		
		JQueryLocator row = tableNonScrollablePart.getChild(jq("tr:last"));
		
		checkTheRow("Infiniti", "EX35", row);
	}
}
