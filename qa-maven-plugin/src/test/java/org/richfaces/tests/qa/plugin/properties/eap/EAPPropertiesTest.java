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
package org.richfaces.tests.qa.plugin.properties.eap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.utils.Servant;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class EAPPropertiesTest {

    private final String hudsonStaticLinux = "/home/hudson/static_build_env/";
    private final String hudsonStaticWin = "h:/hudson/static_build_env/";

    private EAPProperties props;

    @Mock
    private Servant servant_linux;
    @Mock
    private Servant servant_win;

    private final String version610 = "6.1.0";
    private final String version610cr1 = "6.1.0.cr1";
    private final String version611 = "6.1.1";
    private final String version613cr1 = "6.1.3.cr1";
    private final String version630 = "6.3.0";
    private final String version630dr1 = "6.3.0-dr1";
    private final String version631 = "6.3.1";
    private final String version631dr1 = "6.3.1-dr1";

    @Before
    public void setUp() {
        props = null;
        MockitoAnnotations.initMocks(this);

        when(servant_linux.isOnWindows()).thenReturn(Boolean.FALSE);
        when(servant_win.isOnWindows()).thenReturn(Boolean.TRUE);
    }

    @Test
    public void testNotReleasedEAP61WithMinorVersionEqualToZeroURL() throws IOException {
        props = new EAPProperties(version610cr1, servant_linux);
        assertEquals(new URL("http://download.englab.brq.redhat.com/devel/candidates/JBEAP/JBEAP-6.1.0.cr1/jboss-eap-6.1.0.cr1.zip"), props.getUrlToEapZip());
    }

    @Test
    public void testNotReleasedEAP61WithMinorVersionEqualToZeroZIP_linux() {
        props = new EAPProperties(version610cr1, servant_linux);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticLinux, version610cr1, version610cr1)), props.getJenkinsEapZipFile());
    }

    @Test
    public void testNotReleasedEAP61WithMinorVersionGreaterThanZeroZIP_linux() {
        props = new EAPProperties(version613cr1, servant_linux);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticLinux, version613cr1, version613cr1)), props.getJenkinsEapZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroURL() throws IOException {
        props = new EAPProperties(version630dr1, servant_linux);
        assertEquals(new URL("http://download.englab.brq.redhat.com/devel/candidates/JBEAP/JBEAP-6.3.0-dr1/jboss-eap-6.3.0-dr1.zip"), props.getUrlToEapZip());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroURLIsSameForLinuxAndWindows() throws IOException {
        assertEquals(new EAPProperties(version630dr1, servant_linux).getUrlToEapZip(), new EAPProperties(version630dr1, servant_win).getUrlToEapZip());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroZIP_linux() {
        props = new EAPProperties(version630dr1, servant_linux);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticLinux, version630dr1, version630dr1)), props.getJenkinsEapZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroZIP_win() {
        props = new EAPProperties(version630dr1, servant_win);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticWin, version630dr1, version630dr1)), props.getJenkinsEapZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionGreaterThanZeroURL() throws IOException {
        props = new EAPProperties(version631dr1, servant_linux);
        assertEquals(new URL("http://download.englab.brq.redhat.com/devel/candidates/JBEAP/JBEAP-6.3.1-dr1/jboss-eap-6.3.1-dr1-full-build.zip"), props.getUrlToEapZip());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionGreaterThanZeroZIP_linux() {
        props = new EAPProperties(version631dr1, servant_linux);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticLinux, version631dr1, version631dr1 + "-full-build")), props.getJenkinsEapZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionGreaterThanZeroZIP_win() {
        props = new EAPProperties(version631dr1, servant_win);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticWin, version631dr1, version631dr1 + "-full-build")), props.getJenkinsEapZipFile());
    }

    @Test
    public void testReleasedEAP61WithMinorVersionEqualToZeroURL() throws IOException {
        props = new EAPProperties(version610, servant_linux);
        assertEquals(new URL("http://download.englab.brq.redhat.com/released/JBEAP-6/6.1.0/jboss-eap-6.1.0.zip"), props.getUrlToEapZip());
    }

    @Test
    public void testReleasedEAP61WithMinorVersionGreaterThanZeroZeroURL() throws IOException {
        props = new EAPProperties(version611, servant_linux);
        assertEquals(new URL("http://download.englab.brq.redhat.com/released/JBEAP-6/6.1.1/jboss-eap-6.1.1.zip"), props.getUrlToEapZip());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroURL() throws IOException {
        props = new EAPProperties(version630, servant_linux);
        assertEquals(new URL("http://download.englab.brq.redhat.com/released/JBEAP-6/6.3.0/jboss-eap-6.3.0.zip"), props.getUrlToEapZip());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroURLIsSameForLinuxAndWindows() throws IOException {
        assertEquals(new EAPProperties(version630, servant_linux).getUrlToEapZip(), new EAPProperties(version630, servant_win).getUrlToEapZip());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroZIP_linux() {
        props = new EAPProperties(version630, servant_linux);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticLinux, version630, version630)), props.getJenkinsEapZipFile());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroZIP_win() {
        props = new EAPProperties(version630, servant_win);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticWin, version630, version630)), props.getJenkinsEapZipFile());
    }

    @Test
    public void testReleasedEAPWithMinorVersionGreaterThanZeroURL() throws IOException {
        props = new EAPProperties(version631, servant_linux);
        assertEquals(new URL("http://download.englab.brq.redhat.com/released/JBEAP-6/6.3.1/jboss-eap-6.3.1-full-build.zip"), props.getUrlToEapZip());
    }

    @Test
    public void testReleasedEAPWithMinorVersionGreaterThanZeroZIP_linux() {
        props = new EAPProperties(version631, servant_linux);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticLinux, version631, version631 + "-full-build")), props.getJenkinsEapZipFile());
    }

    @Test
    public void testReleasedEAPWithMinorVersionGreaterThanZeroZIP_win() {
        props = new EAPProperties(version631, servant_win);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticWin, version631, version631 + "-full-build")), props.getJenkinsEapZipFile());
    }
}
