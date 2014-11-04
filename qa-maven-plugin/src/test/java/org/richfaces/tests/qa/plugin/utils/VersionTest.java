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

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;
import org.richfaces.tests.qa.plugin.utils.Version.Format;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class VersionTest {

    private Version v;

    @Before
    public void cleanup() {
        v = null;
    }

    @Test
    public void testCompareVersion() {
        Version one = new Version("1");
        Version oneOne = new Version("1.1");
        Version oneOneOne = new Version("1.1.1");
        Version oneOneOneString = new Version("1.1.1-SPEC");
        Version twoOne = new Version("2.1");
        Version twoOneOne = new Version("2.1.1");
        Version twoTwoOne = new Version("2.2.1");

        ArrayList<Version> versions = Lists.newArrayList(oneOne, one);
        Collections.sort(versions);
        assertEquals(versions.get(0), one);

        versions.add(twoOne);
        Collections.sort(versions);
        assertEquals(versions.get(0), one);
        assertEquals(versions.get(1), oneOne);
        assertEquals(versions.get(2), twoOne);

        versions.add(oneOneOne);
        Collections.sort(versions);
        assertEquals(versions.get(0), one);
        assertEquals(versions.get(1), oneOne);
        assertEquals(versions.get(2), oneOneOne);
        assertEquals(versions.get(3), twoOne);

        versions.add(twoTwoOne);
        Collections.sort(versions);
        assertEquals(versions.get(0), one);
        assertEquals(versions.get(1), oneOne);
        assertEquals(versions.get(2), oneOneOne);
        assertEquals(versions.get(3), twoOne);
        assertEquals(versions.get(4), twoTwoOne);

        versions.add(twoOneOne);
        Collections.sort(versions);
        assertEquals(versions.get(0), one);
        assertEquals(versions.get(1), oneOne);
        assertEquals(versions.get(2), oneOneOne);
        assertEquals(versions.get(3), twoOne);
        assertEquals(versions.get(4), twoOneOne);
        assertEquals(versions.get(5), twoTwoOne);

        versions.add(oneOneOneString);
        Collections.sort(versions);
        assertEquals(versions.get(0), one);
        assertEquals(versions.get(1), oneOne);
        assertEquals(versions.get(2), oneOneOne);
        assertEquals(versions.get(3), oneOneOneString);
        assertEquals(versions.get(4), twoOne);
        assertEquals(versions.get(5), twoOneOne);
        assertEquals(versions.get(6), twoTwoOne);
    }

    @Test
    public void testParseVersion() {
        v = new Version("35");
        assertEquals(35, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("", v.getSpecifier());

        v = new Version("35.1esr");
        assertEquals(35, v.getMajor());
        assertEquals(1, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr", v.getSpecifier());

        v = new Version("35.1.2esr");
        assertEquals(35, v.getMajor());
        assertEquals(1, v.getMinor());
        assertEquals(2, v.getMicro());
        assertEquals("esr", v.getSpecifier());

        v = new Version("35.12esr.old");
        assertEquals(35, v.getMajor());
        assertEquals(12, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr.old", v.getSpecifier());

        v = new Version("24.2esr.old.2");
        assertEquals(24, v.getMajor());
        assertEquals(2, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr.old.2", v.getSpecifier());

        v = new Version("24.2.1.1.esr");
        assertEquals(24, v.getMajor());
        assertEquals(2, v.getMinor());
        assertEquals(1, v.getMicro());
        assertEquals(".1.esr", v.getSpecifier());

        v = new Version("24esr");
        assertEquals(24, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr", v.getSpecifier());

        v = new Version("24esr.1");
        assertEquals(24, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr.1", v.getSpecifier());

        v = new Version("6.2.4-patched");
        assertEquals(6, v.getMajor());
        assertEquals(2, v.getMinor());
        assertEquals(4, v.getMicro());
        assertEquals("-patched", v.getSpecifier());
    }

    @Test
    public void testParseFirefoxVersion() {
        v = Version.parseFirefoxVersion("firefox-35");
        assertEquals(35, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("", v.getSpecifier());

        v = Version.parseFirefoxVersion("firefox-35.1esr");
        assertEquals(35, v.getMajor());
        assertEquals(1, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr", v.getSpecifier());

        v = Version.parseFirefoxVersion("firefox-35.1.2esr");
        assertEquals(35, v.getMajor());
        assertEquals(1, v.getMinor());
        assertEquals(2, v.getMicro());
        assertEquals("esr", v.getSpecifier());

        v = Version.parseFirefoxVersion("firefox-35.12esr.old");
        assertEquals(35, v.getMajor());
        assertEquals(12, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr.old", v.getSpecifier());

        v = Version.parseFirefoxVersion("firefox-24.2esr.old.2");
        assertEquals(24, v.getMajor());
        assertEquals(2, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr.old.2", v.getSpecifier());

        v = Version.parseFirefoxVersion("firefox-24.2.1.1.esr");
        assertEquals(24, v.getMajor());
        assertEquals(2, v.getMinor());
        assertEquals(1, v.getMicro());
        assertEquals(".1.esr", v.getSpecifier());

        v = Version.parseFirefoxVersion("firefox-24esr");
        assertEquals(24, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr", v.getSpecifier());

        v = Version.parseFirefoxVersion("firefox-24esr.1");
        assertEquals(24, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr.1", v.getSpecifier());

        v = Version.parseFirefoxVersion("24esr.1");
        assertEquals(24, v.getMajor());
        assertEquals(0, v.getMinor());
        assertEquals(0, v.getMicro());
        assertEquals("esr.1", v.getSpecifier());
    }

    @Test
    public void testParseEAPVersion() {
        v = Version.parseEapVersion("jboss-eap-6.3.1");
        assertEquals(6, v.getMajor());
        assertEquals(3, v.getMinor());
        assertEquals(1, v.getMicro());

        v = Version.parseEapVersion("jboss-eap-6.3.1-patched");
        assertEquals(6, v.getMajor());
        assertEquals(3, v.getMinor());
        assertEquals(1, v.getMicro());
        assertEquals("-patched", v.getSpecifier());

        v = Version.parseEapVersion("6.3.1-patched");
        assertEquals(6, v.getMajor());
        assertEquals(3, v.getMinor());
        assertEquals(1, v.getMicro());
        assertEquals("-patched", v.getSpecifier());
    }

    @Test
    public void testFormat() {
        String versionSttring = "3.2.1abc";
        v = Version.parseVersion(versionSttring);

        assertEquals(versionSttring, v.getFullFormat());
        assertEquals("3.2", v.getMajorMinorFormat());
        assertEquals("3.2.1", v.getMajorMinorMicroFormat());
        assertEquals("3.2.1abc", v.getFormat(EnumSet.of(Format.major, Format.minor, Format.micro, Format.specifier)));
        assertEquals("abc", v.getFormat(EnumSet.of(Format.specifier)));
        assertEquals("", v.getFormat(EnumSet.of(Format.prefix)));

        versionSttring = "prefix-3.2.1abc";
        v = Version.parseVersion(versionSttring, "prefix-");
        assertEquals("3.2", v.getMajorMinorFormat());
        assertEquals("3.2.1", v.getMajorMinorMicroFormat());
        assertEquals(versionSttring, v.getFullFormat());
        assertEquals("3.2.1abc", v.getFormat(EnumSet.of(Format.major, Format.minor, Format.micro, Format.specifier)));
        assertEquals("abc", v.getFormat(EnumSet.of(Format.specifier)));
        assertEquals("prefix-", v.getFormat(EnumSet.of(Format.prefix)));
    }
}
