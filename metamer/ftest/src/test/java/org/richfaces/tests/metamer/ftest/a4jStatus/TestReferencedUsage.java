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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.jboss.arquillian.ajocado.Graphene.retrieveText;
import static org.jboss.arquillian.ajocado.Graphene.waitAjax;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;

import static org.testng.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.jboss.cheiron.halt.XHRHalter;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22407 $
 */
public class TestReferencedUsage extends AbstracStatusTest {

    JQueryLocator status1 = pjq("span[id$=status1]");
    JQueryLocator status2 = pjq("span[id$=status2]");

    TextRetriever retrieveStatus1 = retrieveText.locator(status1);
    TextRetriever retrieveStatus2 = retrieveText.locator(status2);

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jStatus/referencedUsage.xhtml");
    }

    @Test
    public void testClickBothButtonsInSequence() {
        XHRHalter.enable();
        selenium.click(button1);
        XHRHalter halt = getCurrentXHRHalter();
        assertEquals(retrieveStatus1.retrieve(), "START");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        halt.complete();
        waitAjax.waitForChange("START", retrieveStatus1);
        selenium.click(button2);
        halt = getCurrentXHRHalter();
        assertEquals(retrieveStatus1.retrieve(), "STOP");
        assertEquals(retrieveStatus2.retrieve(), "START");
        halt.complete();
        waitAjax.waitForChange("START", retrieveStatus2);
        assertEquals(retrieveStatus1.retrieve(), "STOP");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        XHRHalter.disable();
    }

    @Test
    public void testClickBothButtonsImmediately() {
        XHRHalter.enable();
        selenium.click(button1);
        selenium.click(button2);
        XHRHalter halt = getCurrentXHRHalter();
        assertEquals(retrieveStatus1.retrieve(), "START");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        halt.complete();
        halt.waitForOpen();
        assertEquals(retrieveStatus1.retrieve(), "STOP");
        assertEquals(retrieveStatus2.retrieve(), "START");
        halt.complete();
        waitAjax.waitForChange("START", retrieveStatus2);
        assertEquals(retrieveStatus1.retrieve(), "STOP");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        XHRHalter.disable();
    }

    /**
     * @Test TODO: selenium is causing 3 requests, but manually we triggers 2 requests (use Firebug to reproduce)
     */
    public void testClickFirstButtonThenSecondButtonThenAgainFirstButtonImmediately() {
        XHRHalter.enable();
        selenium.click(button1);
        selenium.click(button2);
        selenium.click(button1);
        XHRHalter halt = XHRHalter.getHandleBlocking();
        assertEquals(retrieveStatus1.retrieve(), "START");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        halt.complete();
        halt.waitForOpen();
        assertEquals(retrieveStatus1.retrieve(), "START");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        halt.complete();
        waitAjax.waitForChange("START", retrieveStatus1);
        assertEquals(retrieveStatus1.retrieve(), "STOP");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        XHRHalter.disable();
    }

    @Test
    public void testDoubleClick() {
        XHRHalter.enable();
        selenium.click(button1);
        XHRHalter halt = getCurrentXHRHalter();
        assertEquals(retrieveStatus1.retrieve(), "START");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        halt.complete();
        selenium.click(button1);
        halt.waitForOpen();
        assertEquals(retrieveStatus1.retrieve(), "START");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        halt.complete();
        waitAjax.waitForChange("START", retrieveStatus1);
        assertEquals(retrieveStatus1.retrieve(), "STOP");
        assertEquals(retrieveStatus2.retrieve(), "STOP");
        XHRHalter.disable();
    }

}
