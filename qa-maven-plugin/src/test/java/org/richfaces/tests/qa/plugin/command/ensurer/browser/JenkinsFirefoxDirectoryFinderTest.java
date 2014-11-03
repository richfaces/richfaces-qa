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
package org.richfaces.tests.qa.plugin.command.ensurer.browser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.utils.Version;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class JenkinsFirefoxDirectoryFinderTest {

    @Mock
    private File FF36;
    @Mock
    private File FF35;
    @Mock
    private File FF351;
    @Mock
    private File FF35111;
    @Mock
    private File FF3501;
    @Mock
    private File FF19;
    @Mock
    private File FF10;
    @Mock
    private File FF24ESR;
    @Mock
    private File FF242ESR;
    @Mock
    private File FF242ESROLD;
    @Mock
    private File BAD;
    @Mock
    private File BAD2;
    @Mock
    private File BAD3;
    @Mock
    private File BAD4;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        File[] goodFilesLinux = new File[]{ new File("file1"), new File("firefox") };
        File[] goodFilesWindows = new File[]{ new File("file1"), new File("firefox.exe") };
        File[] noFiles = new File[]{};

        when(BAD.isDirectory()).thenReturn(Boolean.TRUE);
        when(BAD.getName()).thenReturn("badOne");
        when(BAD.listFiles()).thenReturn(goodFilesLinux);

        when(BAD2.isDirectory()).thenReturn(Boolean.FALSE);
        when(BAD2.getName()).thenReturn("badOne.x");
        when(BAD2.listFiles()).thenReturn(goodFilesWindows);

        when(BAD3.isDirectory()).thenReturn(Boolean.TRUE);
        when(BAD3.getName()).thenReturn("badOne-11");
        when(BAD3.listFiles()).thenReturn(goodFilesLinux);

        when(BAD4.isDirectory()).thenReturn(Boolean.TRUE);
        when(BAD4.getName()).thenReturn("firefox-123");
        when(BAD4.listFiles()).thenReturn(noFiles);

        when(FF36.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF36.getName()).thenReturn("firefox-36");
        when(FF36.listFiles()).thenReturn(goodFilesLinux);

        when(FF35.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF35.getName()).thenReturn("firefox-35");
        when(FF35.listFiles()).thenReturn(goodFilesWindows);

        when(FF351.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF351.getName()).thenReturn("firefox-35.1");
        when(FF351.listFiles()).thenReturn(goodFilesLinux);

        when(FF35111.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF35111.getName()).thenReturn("firefox-35.1.1.1");
        when(FF35111.listFiles()).thenReturn(goodFilesWindows);

        when(FF3501.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF3501.getName()).thenReturn("firefox-35.0.1");
        when(FF3501.listFiles()).thenReturn(goodFilesLinux);

        when(FF19.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF19.getName()).thenReturn("firefox-19");
        when(FF19.listFiles()).thenReturn(goodFilesWindows);

        when(FF10.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF10.getName()).thenReturn("firefox-10");
        when(FF10.listFiles()).thenReturn(goodFilesLinux);

        when(FF24ESR.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF24ESR.getName()).thenReturn("firefox-24esr");
        when(FF24ESR.listFiles()).thenReturn(goodFilesWindows);

        when(FF242ESR.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF242ESR.getName()).thenReturn("firefox-24.2esr");
        when(FF242ESR.listFiles()).thenReturn(goodFilesLinux);

        when(FF242ESROLD.isDirectory()).thenReturn(Boolean.TRUE);
        when(FF242ESROLD.getName()).thenReturn("firefox-24.2esr.old");
        when(FF242ESROLD.listFiles()).thenReturn(goodFilesWindows);
    }

    @Test
    public void testHighest() {
        assertEquals(FF36, JenkinsFirefoxDirectoryFinder.getHighestVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }));
    }

    @Test
    public void testHighest2() {
        assertEquals(FF35111, JenkinsFirefoxDirectoryFinder.getHighestVersion(new File[]{ FF19, FF351, FF3501, FF35111, FF35, FF24ESR, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }));
    }

    @Test
    public void testHighest_NonExistiong_returnsNull() {
        assertEquals(null, JenkinsFirefoxDirectoryFinder.getHighestVersion(new File[]{ BAD, BAD2, BAD3, BAD4 }));
    }

    @Test
    public void testSpecific_existing() {
        assertEquals(FF36, JenkinsFirefoxDirectoryFinder.getSpecificVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("36")));
    }

    @Test
    public void testSpecific_existing2() {
        assertEquals(FF3501, JenkinsFirefoxDirectoryFinder.getSpecificVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("35.0.1")));
    }

    @Test
    public void testSpecific_existing3() {
        assertEquals(FF3501, JenkinsFirefoxDirectoryFinder.getSpecificVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("35.0.1")));
    }

    @Test
    public void testSpecific_nonExisting_returnsNull() {
        assertEquals(null, JenkinsFirefoxDirectoryFinder.getSpecificVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("66")));
    }

    @Test
    public void testSpecific_fromNonMatching_returnsNull() {
        assertEquals(null, JenkinsFirefoxDirectoryFinder.getSpecificVersion(new File[]{ BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("123")));
    }

    @Test
    public void testSpecificOrHighest_nonExistingSpecific_returnsHighest() {
        assertEquals(FF36, JenkinsFirefoxDirectoryFinder.getSpecificVersionOrHighest(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("112")));
    }

    @Test
    public void testSpecificOrHighest_fromNonMatching_returnsNull() {
        assertEquals(null, JenkinsFirefoxDirectoryFinder.getSpecificVersionOrHighest(new File[]{ BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("123")));
    }

    @Test
    public void testSpecificOrHighest_existing() {
        assertEquals(FF351, JenkinsFirefoxDirectoryFinder.getSpecificVersionOrHighest(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("35.1")));
    }

    @Test
    public void testHighestOrSpecific_nonExistingSpecific_returnsHighest() {
        assertEquals(FF36, JenkinsFirefoxDirectoryFinder.getHighestOrSpecificVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("112")));
    }

    @Test
    public void testHighestOrSpecific_fromNonMatching_returnsNull() {
        assertEquals(null, JenkinsFirefoxDirectoryFinder.getHighestOrSpecificVersion(new File[]{ BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("123")));
    }

    @Test
    public void testHighestOrSpecific_existing() {
        assertEquals(FF36, JenkinsFirefoxDirectoryFinder.getHighestOrSpecificVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("35")));
    }

    @Test
    public void testOptimalOrMinimalVersion_optimalExists_returnsOptimal() {
        assertEquals(FF351, JenkinsFirefoxDirectoryFinder.getOptimalOrMinimalVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("35.1"), Version.parseVersion("10")));
    }

    @Test
    public void testOptimalOrMinimalVersion_optimalExists_returnsOptimal2() {
        assertEquals(FF24ESR, JenkinsFirefoxDirectoryFinder.getOptimalOrMinimalVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("24esr"), Version.parseVersion("19")));
    }

    @Test
    public void testOptimalOrMinimalVersion_optimalNotExistsMinimalExists_returnsMinimal() {
        assertEquals(FF10, JenkinsFirefoxDirectoryFinder.getOptimalOrMinimalVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("34"), Version.parseVersion("10")));
    }

    @Test
    public void testOptimalOrMinimalVersion_optimalNotExistsMinimalNotExists_returnsNull() {
        assertEquals(null, JenkinsFirefoxDirectoryFinder.getOptimalOrMinimalVersion(new File[]{ FF19, FF36, FF351, FF35111, FF10, FF24ESR, FF3501, FF35, FF242ESR, FF242ESROLD, BAD, BAD2, BAD3, BAD4 }, Version.parseVersion("34"), Version.parseVersion("11")));
    }
}
