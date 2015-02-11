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
package org.richfaces.tests.qa.plugin.ensurer.browser.local;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.List;

import org.richfaces.tests.qa.plugin.ensurer.browser.BrowserEnsurer;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.utils.Servant;
import org.richfaces.tests.qa.plugin.utils.Version;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LocalFirefoxEnsurer implements BrowserEnsurer {

    private static final String FIREFOX = "firefox";
    private static final String LINUXI686 = "linux-i686";
    private static final String LINUXX86_64 = "linux-x86_64";
    private static final String WIN32 = "win32";

    private final PropertiesProvider pp;
    private final Servant servant;

    @Inject
    public LocalFirefoxEnsurer(PropertiesProvider pp, Servant servant) {
        this.pp = pp;
        this.servant = servant;
    }

    @Override
    public void ensure() {
        if (pp.getBrowser().isUnknownVersion()) {
            pp.getLog().info("Firefox version not specified, using system''s firefox.");
            String firefoxBin = pp.isOnWindows() ? pp.getFirefoxDefaultBinWindows() : pp.getFirefoxDefaultBinUnix();
            if (!new File(firefoxBin).exists()) {
                throw new RuntimeException(MessageFormat.format("The system''s firefox bin was not found at <{0}>.", firefoxBin));
            }
            servant.setProjectProperty(pp.getFirefoxBinPropertyName(), firefoxBin);
        } else {
            if (!pp.isOnLinux()) {
                throw new UnsupportedOperationException("Firefox binaries are available only for linux.");
            }
            String versionFull = pp.getBrowser().getVersion().getMajorMinorMicroSpecifierFormat();
            File currentFirefoxDir = new File(pp.getUserBrowserDirectory(), FIREFOX + File.separator + versionFull);
            if (!currentFirefoxDir.exists()) {
                currentFirefoxDir.mkdirs();
            }
            File targetDir = new File(pp.getProjectBuildDirectory());

            List<String> possibleVersions = Lists.newArrayList();
            possibleVersions.add(versionFull);
            possibleVersions.add(pp.getBrowser().getVersion().getFormat(EnumSet.of(Version.Format.major, Version.Format.minor, Version.Format.specifier)));
            possibleVersions.add(pp.getBrowser().getVersion().getFormat(EnumSet.of(Version.Format.major, Version.Format.specifier)));
            Exception caughtException = null;
            for (String possibleVersion : possibleVersions) {
                try {
                    pp.getLog().info("Trying to find version " + possibleVersion);
                    File currentFirefoxZip = new File(currentFirefoxDir, FIREFOX + "-" + possibleVersion + ".tar.bz2");
                    // download the firefox bin
                    servant.downloadFile(getFirefoxBinURL(possibleVersion, servant), currentFirefoxZip);
                    // extract the firefox bin to project build directory
                    servant.extract(targetDir, currentFirefoxZip);
                    File firefoxBinary = new File(new File(targetDir, FIREFOX), FIREFOX);
                    firefoxBinary.setExecutable(true);
                    servant.setProjectProperty(pp.getFirefoxBinPropertyName(), firefoxBinary.getAbsolutePath());
                    return;
                } catch (Exception e) {
                    pp.getLog().info("Version was not found");
                    caughtException = e;
                }
            }
            throw new RuntimeException(caughtException);
        }
    }

    protected String getCorrectFirefoxVersionString(String versionString, Servant servant) {
        String os;
        if (pp.isOnLinux()) {
            os = pp.is64bitArch() ? LINUXX86_64 : LINUXI686;
        } else if (pp.isOnWindows()) {
            os = WIN32;
        } else {
            throw new UnsupportedOperationException(MessageFormat.format("OS arch <{0}> not supported.", pp.getOsArch()));
        }
        return MessageFormat.format("{0}/{1}/en-US/firefox-{0}.tar.bz2", versionString, os);
    }

    protected URL getFirefoxBinURL(String versionString, Servant servant) throws MalformedURLException {
        return new URL(pp.getFirefoxBaseURL(), getCorrectFirefoxVersionString(versionString, servant));
    }
}
