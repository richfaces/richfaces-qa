/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitAjax;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.jboss.test.selenium.locator.utils.LocatorEscaping.jq;
import static org.richfaces.tests.metamer.ftest.a4jStatus.StatusAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.attributes.AttributeList.statusAttributes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.jboss.cheiron.halt.XHRHalter;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22684 $
 */
public class TestSimple extends AbstracStatusTest {

    JQueryLocator defaultStatus = jq("span[id$=a4jStatusPanel]");
    TextRetriever retrieveDefaultStatus = retrieveText.locator(defaultStatus);

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/simple.xhtml");
    }

    @Test
    public void testRequestButton1() {
        testRequestButton(button1, "START", "STOP");
    }

    @Test
    public void testRequestButton2() {
        testRequestButton(button2, "START", "STOP");
    }

    @Test
    public void testRequestButtonError() {
        testRequestButton(buttonError, "START", "ERROR");
    }

    @Test
    public void testInterleaving() {
        testRequestButton1();
        testRequestButtonError();
        testRequestButton2();
        testRequestButtonError();
        testRequestButton1();
    }

    @Test
    public void testRendered() {
        assertTrue(selenium.isElementPresent(status));

        statusAttributes.set(rendered, false);

        assertFalse(selenium.isElementPresent(status));

        XHRHalter.enable();
        selenium.click(button1);
        XHRHalter halt = getCurrentXHRHalter();
        assertEquals(retrieveDefaultStatus.retrieve(), "WORKING");
        retrieveDefaultStatus.initializeValue();
        halt.complete();
        waitAjax.waitForChange(retrieveDefaultStatus);
        assertEquals(retrieveDefaultStatus.retrieve(), "");
        XHRHalter.disable();
    }
}
