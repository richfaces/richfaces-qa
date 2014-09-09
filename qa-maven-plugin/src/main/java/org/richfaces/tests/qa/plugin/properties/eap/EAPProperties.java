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

import java.io.File;
import java.net.URL;

import org.richfaces.tests.qa.plugin.utils.Servant;
import org.richfaces.tests.qa.plugin.utils.Utils;
import org.richfaces.tests.qa.plugin.utils.Version;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class EAPProperties {

    private static final String hudsonStaticLinux = "/home/hudson/static_build_env/";
    private static final String hudsonStaticWin = "h:/hudson/static_build_env/";
    private File jenkinsEapZipFile;
    private final Servant servant;
    private URL urlToEapZip;
    private final Version version;

    public EAPProperties(Version v, Servant servant) {
        this.version = v;
        this.servant = servant;
    }

    public static EAPProperties getPropertiesForVersion(String versionString, Servant servant) {
        Version v = Version.parseEapVersion(versionString);
        if (v.equals(Version.parseVersion("6.3.0"))) {
            return new EAP630Properties(servant);
        } else if (v.equals(Version.parseVersion("6.2.4-patched"))) {
            return new EAP624PatchedProperties(servant);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported EAP version %s", v.getFullFormat()));
        }
    }

    protected File _getJenkinsEapZipFile() {
        return new File(String.format("%s/eap/%s/%s.zip", getServant().isOnLinux() ? hudsonStaticLinux : hudsonStaticWin, version.getMajorMinorMicroFormat(), version.getFullFormat()));
    }

    protected URL _getUrlToEapZip() {
        return Utils.createURLSilently(String.format("%s/%s/%s.zip", getURLPart1(), getURLPart2(), getURLPart3()));
    }

    public String getEapExtractedDirectoryName() {
        return "jboss-eap-" + getVersion().getMajorMinorFormat();
    }

    public File getJenkinsEapZipFile() {
        if (jenkinsEapZipFile == null) {
            jenkinsEapZipFile = _getJenkinsEapZipFile();
        }
        return jenkinsEapZipFile;
    }

    public Servant getServant() {
        return servant;
    }

    protected String getURLPart1() {
        return "http://download.englab.brq.redhat.com/released/JBEAP-6";
    }

    protected String getURLPart2() {
        return version.getMajorMinorMicroFormat();
    }

    protected String getURLPart3() {
        return version.getFullFormat();
    }

    public URL getUrlToEapZip() {
        if (urlToEapZip == null) {
            urlToEapZip = _getUrlToEapZip();
        }
        return urlToEapZip;
    }

    public Version getVersion() {
        return version;
    }
}
