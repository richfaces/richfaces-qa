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
package org.richfaces.tests.qa.plugin.properties.eap;

import static java.text.MessageFormat.format;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.utils.Version;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleEAPPropertiesTest {

    private static final String candidates = "http://download.englab.brq.redhat.com/devel/candidates/JBEAP/JBEAP-";
    private static final String pathLinux = "/home/hudson/static_build_env";
    private static final String pathMac = "/Users/hudson/static_build_env";
    private static final String pathWin = "h:/hudson/static_build_env";
    private static final String released = "http://download.englab.brq.redhat.com/released/JBEAP-6/";

    private static final String version610 = "6.1.0";
    private static final String version610cr1 = "6.1.0.cr1";
    private static final String version611 = "6.1.1";
    private static final String version613cr1 = "6.1.3.cr1";
    private static final String version630 = "6.3.0";
    private static final String version630dr1 = "6.3.0-dr1";
    private static final String version631 = "6.3.1";
    private static final String version631dr1 = "6.3.1-dr1";

    private Version actualVersion;
    private EAPProperties props;

    @Mock
    private PropertiesProvider providerLinux;
    @Mock
    private PropertiesProvider providerMac;
    @Mock
    private PropertiesProvider providerWin;

    private File getZipFile() {
        return props.getJenkinsEapZipFile();
    }

    private URL getZipURL() {
        return props.getUrlToEapZip();
    }

    @Before
    public void setUp() {
        props = null;
        MockitoAnnotations.initMocks(this);

        when(providerLinux.isOnWindows()).thenReturn(Boolean.FALSE);
        when(providerLinux.isOnLinux()).thenReturn(Boolean.TRUE);
        when(providerLinux.isOnMac()).thenReturn(Boolean.FALSE);

        when(providerMac.isOnWindows()).thenReturn(Boolean.FALSE);
        when(providerMac.isOnLinux()).thenReturn(Boolean.FALSE);
        when(providerMac.isOnMac()).thenReturn(Boolean.TRUE);

        when(providerWin.isOnWindows()).thenReturn(Boolean.TRUE);
        when(providerWin.isOnLinux()).thenReturn(Boolean.FALSE);
        when(providerWin.isOnMac()).thenReturn(Boolean.FALSE);
    }

    private void setupMockVersion(String version) {
        actualVersion = Version.parseEapVersion(version);
        when(providerLinux.getEapVersion()).thenReturn(actualVersion);
        when(providerWin.getEapVersion()).thenReturn(actualVersion);
        when(providerMac.getEapVersion()).thenReturn(actualVersion);
    }

    @Test
    public void testNotReleasedEAP61WithMinorVersionEqualToZeroURL() throws IOException {
        setupMockVersion(version610cr1);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new URL(format("{0}{1}/jboss-eap-{1}.zip", candidates, actualVersion)), getZipURL());
    }

    @Test
    public void testNotReleasedEAP61WithMinorVersionEqualToZeroZIP_linux() {
        setupMockVersion(version610cr1);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}.zip", pathLinux, actualVersion)), getZipFile());
    }

    @Test
    public void testNotReleasedEAP61WithMinorVersionGreaterThanZeroZIP_linux() {
        setupMockVersion(version613cr1);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}.zip", pathLinux, actualVersion)), getZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroURL() throws IOException {
        setupMockVersion(version630dr1);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new URL(format("{0}{1}/jboss-eap-{1}.zip", candidates, actualVersion)), getZipURL());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroURLIsSameForLinuxAndWindows() throws IOException {
        setupMockVersion(version630dr1);
        assertEquals(new SimpleEAPProperties(providerLinux).getUrlToEapZip(), new SimpleEAPProperties(providerWin).getUrlToEapZip());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroZIP_linux() {
        setupMockVersion(version630dr1);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}.zip", pathLinux, actualVersion)), getZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroZIP_mac() {
        setupMockVersion(version630dr1);
        props = new SimpleEAPProperties(providerMac);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}.zip", pathMac, actualVersion)), getZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionEqualToZeroZIP_win() {
        setupMockVersion(version630dr1);
        props = new SimpleEAPProperties(providerWin);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}.zip", pathWin, actualVersion)), getZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionGreaterThanZeroURL() throws IOException {
        setupMockVersion(version631dr1);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new URL(format("{0}{1}/jboss-eap-{1}-full-build.zip", candidates, actualVersion)), getZipURL());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionGreaterThanZeroZIP_linux() {
        setupMockVersion(version631dr1);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}-full-build.zip", pathLinux, actualVersion)), getZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionGreaterThanZeroZIP_win() {
        setupMockVersion(version631dr1);
        props = new SimpleEAPProperties(providerWin);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}-full-build.zip", pathWin, actualVersion)), getZipFile());
    }

    @Test
    public void testNotReleasedEAPWithMinorVersionGreaterThanZeroZIP_mac() {
        setupMockVersion(version631dr1);
        props = new SimpleEAPProperties(providerMac);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}-full-build.zip", pathMac, actualVersion)), getZipFile());
    }

    @Test
    public void testReleasedEAP61WithMinorVersionEqualToZeroURL() throws IOException {
        setupMockVersion(version610);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new URL(format("{0}{1}/jboss-eap-{1}.zip", released, actualVersion)), getZipURL());
    }

    @Test
    public void testReleasedEAP61WithMinorVersionGreaterThanZeroZeroURL() throws IOException {
        setupMockVersion(version611);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new URL(format("{0}{1}/jboss-eap-{1}.zip", released, actualVersion)), getZipURL());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroURL() throws IOException {
        setupMockVersion(version630);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new URL(format("{0}{1}/jboss-eap-{1}.zip", released, actualVersion)), getZipURL());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroURLIsSameForLinuxAndWindows() throws IOException {
        setupMockVersion(version630);
        assertEquals(new SimpleEAPProperties(providerLinux).getUrlToEapZip(), new SimpleEAPProperties(providerWin).getUrlToEapZip());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroZIP_linux() {
        setupMockVersion(version630);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}.zip", pathLinux, actualVersion)), getZipFile());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroZIP_win() {
        setupMockVersion(version630);
        props = new SimpleEAPProperties(providerWin);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}.zip", pathWin, actualVersion)), getZipFile());
    }

    @Test
    public void testReleasedEAPWithMinorVersionEqualToZeroZIP_mac() {
        setupMockVersion(version630);
        props = new SimpleEAPProperties(providerMac);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}.zip", pathMac, actualVersion)), getZipFile());
    }

    @Test
    public void testReleasedEAPWithMinorVersionGreaterThanZeroURL() throws IOException {
        setupMockVersion(version631);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new URL(format("{0}{1}/jboss-eap-{1}-full-build.zip", released, actualVersion)), getZipURL());
    }

    @Test
    public void testReleasedEAPWithMinorVersionGreaterThanZeroZIP_linux() {
        setupMockVersion(version631);
        props = new SimpleEAPProperties(providerLinux);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}-full-build.zip", pathLinux, actualVersion)), getZipFile());
    }

    @Test
    public void testReleasedEAPWithMinorVersionGreaterThanZeroZIP_win() {
        setupMockVersion(version631);
        props = new SimpleEAPProperties(providerWin);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}-full-build.zip", pathWin, actualVersion)), getZipFile());
    }

    @Test
    public void testReleasedEAPWithMinorVersionGreaterThanZeroZIP_mac() {
        setupMockVersion(version631);
        props = new SimpleEAPProperties(providerMac);
        assertEquals(new File(format("{0}/eap/{1}/jboss-eap-{1}-full-build.zip", pathMac, actualVersion)), getZipFile());
    }

}
