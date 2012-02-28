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
package org.richfaces.tests.metamer.ftest.richList;

import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractAjocadoTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestListKeepSaved extends AbstractAjocadoTest {

    private final JQueryLocator inputFirst = pjq("input[type=text]:first");
    private final JQueryLocator submit = pjq("input[id$=submitButton]");
    private final JQueryLocator errorMessage = pjq("div.messages li");
    private final JQueryLocator keepSavedInput = pjq("input[type=radio][value=true]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richList/keepSaved.xhtml");
    }

    @Test
    public void testKeepSavedFalse() {
        selenium.type(inputFirst, "a");
        guardHttp(selenium).click(submit);
        assertTrue(selenium.isElementPresent(errorMessage), "No error message shown.");
        guardHttp(selenium).click(submit);
        assertFalse(selenium.isElementPresent(errorMessage), "Error message should be shown.");
    }

    @Test
    public void testKeepSavedTrue() {
        guardHttp(selenium).click(keepSavedInput);
        selenium.type(inputFirst, "a");
        guardHttp(selenium).click(submit);
        assertTrue(selenium.isElementPresent(errorMessage), "No error message shown.");
        guardHttp(selenium).click(submit);
        assertTrue(selenium.isElementPresent(errorMessage), "No error message shown.");
    }
}
