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
package org.richfaces.tests.metamer.ftest;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractWebDriverTest extends AbstractMetamerTest {
	
	@Drone
	protected WebDriver webDriver;
	
	/**
	 * Opens the tested page. If templates is not empty nor null, it appends url
	 * parameter with templates.
	 * 
	 * @param templates
	 *            templates that will be used for test, e.g. "red_div"
	 */
	@BeforeMethod(alwaysRun = true)
	public void loadPage(Object[] templates) {
		if (webDriver == null) {
			throw new SkipException("webDriver isn't initialized");
		}
		webDriver.get(buildUrl(getTestUrl() + "?templates="
				+ template.toString()).toExternalForm());
	}

}
