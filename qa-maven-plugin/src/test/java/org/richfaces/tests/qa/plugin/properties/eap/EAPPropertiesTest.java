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

    @Before
    public void setUp() {
        props = null;
        MockitoAnnotations.initMocks(this);

        when(servant_linux.isOnLinux()).thenReturn(Boolean.TRUE);
    }

    @Test
    public void testEAP624PatchedURL_linux() throws IOException {
        props = new EAP624PatchedProperties(servant_linux);
        props.getUrlToEapZip();
        assertEquals(new URL("http://download.englab.brq.redhat.com/released/JBEAP-6/6.2.4/jboss-eap-6.2.4-full-build.zip"), props.getUrlToEapZip());
//        assertTrue(props.getUrlToEapZip().openConnection().getContentLengthLong() > 100000);
    }

    @Test
    public void testEAP624PatchedURL_win() throws IOException {
        props = new EAP624PatchedProperties(servant_win);
        props.getUrlToEapZip();
        assertEquals(new URL("http://download.englab.brq.redhat.com/released/JBEAP-6/6.2.4/jboss-eap-6.2.4-full-build.zip"), props.getUrlToEapZip());
//        assertTrue(props.getUrlToEapZip().openConnection().getContentLengthLong() > 100000);
    }

    @Test
    public void testEAP624PatchedZIP_linux() {
        props = new EAP624PatchedProperties(servant_linux);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticLinux, "6.2.4", "6.2.4-patched")), props.getJenkinsEapZipFile());
    }

    @Test
    public void testEAP624PatchedZIP_win() {
        props = new EAP624PatchedProperties(servant_win);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticWin, "6.2.4", "6.2.4-patched")), props.getJenkinsEapZipFile());
    }

    @Test
    public void testEAP630URL_linux() throws IOException {
        props = new EAP630Properties(servant_linux);
        assertEquals(new URL("http://download.englab.brq.redhat.com/released/JBEAP-6/6.3.0/jboss-eap-6.3.0.zip"), props.getUrlToEapZip());
//        assertTrue(props.getUrlToEapZip().openConnection().getContentLengthLong() > 100000);
    }

    @Test
    public void testEAP630URL_win() throws IOException {
        props = new EAP630Properties(servant_win);
        assertEquals(new URL("http://download.englab.brq.redhat.com/released/JBEAP-6/6.3.0/jboss-eap-6.3.0.zip"), props.getUrlToEapZip());
//        assertTrue(props.getUrlToEapZip().openConnection().getContentLengthLong() > 100000);
    }

    @Test
    public void testEAP630ZIP_linux() {
        props = new EAP630Properties(servant_linux);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticLinux, "6.3.0", "6.3.0")), props.getJenkinsEapZipFile());
    }

    @Test
    public void testEAP630ZIP_win() {
        props = new EAP630Properties(servant_win);
        assertEquals(new File(String.format("%s/eap/%s/jboss-eap-%s.zip", hudsonStaticWin, "6.3.0", "6.3.0")), props.getJenkinsEapZipFile());
    }
}
