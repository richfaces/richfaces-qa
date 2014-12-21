/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
*/
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
    public void testParsingChrome() {
        browser = Browser.parseFromString("cr");
        assertEquals(BrowserName.chrome, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("chrome");
        assertEquals(BrowserName.chrome, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("chrome36");
        assertEquals(BrowserName.chrome, browser.getName());
        assertEquals(Version.parseVersion("36"), browser.getVersion());

        browser = Browser.parseFromString("cr33");
        assertEquals(BrowserName.chrome, browser.getName());
        assertEquals(Version.parseVersion("33"), browser.getVersion());

        browser = Browser.parseFromString("cr-31");
        assertEquals(BrowserName.chrome, browser.getName());
        assertEquals(Version.parseVersion("31"), browser.getVersion());

        browser = Browser.parseFromString("ChRoME32");
        assertEquals(BrowserName.chrome, browser.getName());
        assertEquals(Version.parseVersion("32"), browser.getVersion());

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
        assertEquals(Version.parseVersion("32"), browser.getVersion());

        browser = Browser.parseFromString("firefox29");
        assertEquals(BrowserName.firefox, browser.getName());
        assertEquals(Version.parseVersion("29"), browser.getVersion());

        browser = Browser.parseFromString("firefox29.1");
        assertEquals(BrowserName.firefox, browser.getName());
        assertEquals(Version.parseVersion("29.1"), browser.getVersion());

        browser = Browser.parseFromString("firefox31.1.0esr");
        assertEquals(BrowserName.firefox, browser.getName());
        assertEquals(Version.parseVersion("31.1.0esr"), browser.getVersion());

        browser = Browser.parseFromString("firefox-31.1.1esr");
        assertEquals(BrowserName.firefox, browser.getName());
        assertEquals(Version.parseVersion("31.1.1esr"), browser.getVersion());

        browser = Browser.parseFromString("firefox31.1.0-esrXYZ");
        assertEquals(BrowserName.firefox, browser.getName());
        assertEquals(Version.parseVersion("31.1.0-esrXYZ"), browser.getVersion());
    }

    @Test
    public void testParsingIE() {
        browser = Browser.parseFromString("internetExplorer");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("ie");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("ie-9");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Version.parseVersion("9"), browser.getVersion());

        browser = Browser.parseFromString("explorer");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("internetExplorer11");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Version.parseVersion("11.0.0"), browser.getVersion());

        browser = Browser.parseFromString("ie10");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Version.parseVersion("10.0"), browser.getVersion());

        browser = Browser.parseFromString("explorer9");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Version.parseVersion("9"), browser.getVersion());

        browser = Browser.parseFromString("ExPloreR9");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Version.parseVersion("9"), browser.getVersion());
        browser = Browser.parseFromString("internetEXPLORER10");
        assertEquals(BrowserName.internetExplorer, browser.getName());
        assertEquals(Version.parseVersion("10"), browser.getVersion());
    }

    @Test
    public void testParsingUnknown() {
        browser = Browser.parseFromString("ffirefox28");
        assertEquals(BrowserName.unknown, browser.getName());
        assertEquals(Version.parseVersion("28"), browser.getVersion());

        browser = Browser.parseFromString("frajerFox22");
        assertEquals(BrowserName.unknown, browser.getName());
        assertEquals(Version.parseVersion("22"), browser.getVersion());

        browser = Browser.parseFromString("unknown");
        assertEquals(BrowserName.unknown, browser.getName());
        assertTrue(browser.isUnknownVersion());

        browser = Browser.parseFromString("unknown33");
        assertEquals(BrowserName.unknown, browser.getName());
        assertEquals(Version.parseVersion("33"), browser.getVersion());

        browser = Browser.parseFromString("unknown33-super");
        assertEquals(BrowserName.unknown, browser.getName());
        assertEquals(Version.parseVersion("33-super"), browser.getVersion());
    }
}
