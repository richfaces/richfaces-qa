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
package org.richfaces.tests.showcase.fileUpload;

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.showcase.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestImgUpload extends AbstractAjocadoTest {

	/* ***********************************************************************
	 * Locators
	 * ***********************************************************************
	 */

	private JQueryLocator addButton = jq(".rf-fu-btn-add");
	private JQueryLocator uploadArea = jq("div[id$=upload]");
	private JQueryLocator uploadFilesInfo = jq("div[id$=info]");
	private JQueryLocator divWithUploadFilesMessage = jq("div.rf-p-b.info");

	/* ************************************************************************
	 * Constants
	 * ************************************************************************
	 */

	private final String MSG_NO_FILES = "No files currently uploaded";

	/* *********************************************************************
	 * Tests
	 * *********************************************************************
	 */

	@Test
	public void testThereAreAllRequiredElements() {

		assertTrue(selenium.isElementPresent(addButton),
				"The add button should be there!");
		assertTrue(selenium.isElementPresent(uploadArea),
				"The upload area should be there!");
		assertTrue(selenium.isElementPresent(uploadFilesInfo),
				"The upload files info should be there!");
		assertTrue(selenium.isElementPresent(divWithUploadFilesMessage),
				"The div with uplad files messages should be there!");
		assertEquals(selenium.getText(divWithUploadFilesMessage), MSG_NO_FILES,
				"The message that no files is currently uploaded should be there!");
	}
}
