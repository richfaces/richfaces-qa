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
package org.richfaces.tests.showcase.dragDrop;

import static org.jboss.arquillian.ajocado.Ajocado.elementNotPresent;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.actions.Drag;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractShowcaseTest;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractDragAndDropTest extends AbstractShowcaseTest {

	/* ***************************************************************************
	 * Constants
	 * *****************************************************************
	 * **********
	 */

	private final int NUMBER_OF_DRAGGABLE_SOURCES = 12;
	private final int NUMBER_OF_DRAGGABLE_TARGETS = 3;
	private final int NUMBER_OF_PHP_FRAMEWORKS = 5;
	private final int NUMBER_OF_DOTNET_FRAMEWORKS = 4;
	private final int NUMBER_OF_COLDFUSION_FRAMEWORKS = 3;

	private final String TEXT_OF_INDICATOR = "Drag the item to proper area..";

	/* **************************************************************************
	 * Locators
	 * ******************************************************************
	 * ********
	 */

	private JQueryLocator dragableObject = jq("div.ui-draggable:visible{0}");
	private JQueryLocator targets = jq(".dropTargetPanel:eq({0})");
	private JQueryLocator phpTableRow = jq("table[id$=phptable] tr");
	private JQueryLocator dotNetTableRow = jq("table[id$=dnettable] tr");
	private JQueryLocator coldFusionTableRow = jq("table[id$=cftable] tr");
	private JQueryLocator indicator = jq("div#ind:visible");

	/* ***************************************************************************
	 * Methods
	 * *********************************************************************
	 * ******
	 */

	protected void dragAndDropAllSourcesToCorrectTarget(
			boolean testAlsoIndicator) {

		for (int i = NUMBER_OF_DRAGGABLE_SOURCES - 1; i >= 0; i--) {

			for (int j = 0; j < NUMBER_OF_DRAGGABLE_TARGETS; j++) {

				int numberOfCurrentDraggableObjectsBefore = selenium
						.getCount(dragableObject);
				
				JQueryLocator particulardDragableObject = jq(dragableObject.format(":eq(" + i + ")").getRawLocator()); 
				
				Drag drag = new Drag(particulardDragableObject,
						targets.format(j));
				drag.setNumberOfSteps(5);
				drag.start();

				if (testAlsoIndicator) {

					checkingOfIndicator();
				}

				drag.drop();
				waitAjax.dontFail().timeout(1000).until(elementNotPresent.locator(particulardDragableObject));
				
				int numberOfCurrentDraggableObjectsAfter = selenium
						.getCount(dragableObject);

				if ((numberOfCurrentDraggableObjectsBefore - 1) == numberOfCurrentDraggableObjectsAfter) {

					break;
				}
			}

		}
		
		checkingThePositionOfObjectsAfterDragging();

	}

	/* *****************************************************************************************************************
	 * Help methods
	 * **************************************************************
	 * ***************************************************
	 */

	/**
	 * Checks whether indicator is present and whether contains particular text
	 */
	private void checkingOfIndicator() {

		assertTrue(selenium.isElementPresent(indicator),
				"The indicator should be present, when the object is dragged!");

		assertEquals(selenium.getText(indicator), TEXT_OF_INDICATOR,
				"The indicator text is not correct");
	}

	/**
	 * Checks whether all draggable objects where dragged and whether  
	 */
	private void checkingThePositionOfObjectsAfterDragging() {

		int numberOfCurrentDraggableObjectsAfter = selenium
				.getCount(dragableObject);

		assertEquals(numberOfCurrentDraggableObjectsAfter, 0,
				"All draggable objects should be draged to targets!");

		assertEquals(selenium.getCount(phpTableRow), NUMBER_OF_PHP_FRAMEWORKS,
				"The number of dragged php frameworks is wrong!");
		assertEquals(selenium.getCount(dotNetTableRow),
				NUMBER_OF_DOTNET_FRAMEWORKS,
				"The number of dragged dotnet frameworks is wrong!");
		assertEquals(selenium.getCount(coldFusionTableRow),
				NUMBER_OF_COLDFUSION_FRAMEWORKS,
				"The number of dragged cold fusion frameworks is wrong!");
	}
}
