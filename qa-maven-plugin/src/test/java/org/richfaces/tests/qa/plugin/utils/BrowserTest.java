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
package org.richfaces.tests.qa.plugin.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.richfaces.tests.qa.plugin.utils.Browser.BrowserName;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class BrowserTest {

    private Browser browser;

    @Before
    public void setUp() {
        browser = null;
    }

    @Test
    public void testParsingBadFormat_returnsUnknownBrowser() {
        browser = Browser.parseFromString("firefox32-bad");
        assertTrue(browser.isUnknown());
        assertTrue(browser.isUnknownVersion());
    }

    @Test
    public void testParsingFirefox() {
        browser = Browser.parseFromString("firefox");
        assertEquals(BrowserName.firefox, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("ff");
        assertEquals(BrowserName.firefox, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("fIrEfOx");
        assertEquals(BrowserName.firefox, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("ff32");
        assertEquals(BrowserName.firefox, browser.getName());
        assertEquals(Integer.valueOf(32), browser.getVersion());

        browser = Browser.parseFromString("firefox29");
        assertEquals(BrowserName.firefox, browser.getName());
        assertEquals(Integer.valueOf(29), browser.getVersion());
    }

    @Test
    public void testParsingChrome() {
        browser = Browser.parseFromString("cr");
        assertEquals(BrowserName.chrome, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("chrome");
        assertEquals(BrowserName.chrome, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("chrome36");
        assertEquals(BrowserName.chrome, browser.getName());
        assertEquals(Integer.valueOf(36), browser.getVersion());

        browser = Browser.parseFromString("cr33");
        assertEquals(BrowserName.chrome, browser.getName());
        assertEquals(Integer.valueOf(33), browser.getVersion());

        browser = Browser.parseFromString("ChRoME32");
        assertEquals(BrowserName.chrome, browser.getName());
        assertEquals(Integer.valueOf(32), browser.getVersion());

    }

    @Test
    public void testParsingIE() {
        browser = Browser.parseFromString("internetExplorer");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("ie");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("explorer");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("internetExplorer11");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Integer.valueOf(11), browser.getVersion());

        browser = Browser.parseFromString("ie10");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Integer.valueOf(10), browser.getVersion());

        browser = Browser.parseFromString("explorer9");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Integer.valueOf(9), browser.getVersion());

        browser = Browser.parseFromString("ExPloreR9");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Integer.valueOf(9), browser.getVersion());
        browser = Browser.parseFromString("internetEXPLORER10");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Integer.valueOf(10), browser.getVersion());
    }

    @Test
    public void testParsingUnknown() {
        browser = Browser.parseFromString("ffirefox28");
        assertEquals(BrowserName.unknown, browser.getName());
        assertEquals(Integer.valueOf(28), browser.getVersion());

        browser = Browser.parseFromString("frajerFox22");
        assertEquals(BrowserName.unknown, browser.getName());
        assertEquals(Integer.valueOf(22), browser.getVersion());

        browser = Browser.parseFromString("unknown");
        assertEquals(BrowserName.unknown, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("unknown33");
        assertEquals(BrowserName.unknown, browser.getName());
        assertEquals(Integer.valueOf(33), browser.getVersion());
    }
}
