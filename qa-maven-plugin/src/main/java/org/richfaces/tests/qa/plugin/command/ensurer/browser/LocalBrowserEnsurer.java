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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;

import org.richfaces.tests.qa.plugin.ProcessMojo;
import org.richfaces.tests.qa.plugin.command.ensurer.Ensurer;
import org.richfaces.tests.qa.plugin.utils.Browser;
import org.richfaces.tests.qa.plugin.utils.Servant;
import org.richfaces.tests.qa.plugin.utils.Version.Format;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LocalBrowserEnsurer implements Ensurer {

    private final URL chromeDriverBaseUrl;
    private final URL firefoxBaseUrl;
    private final URL ieDriverBinUrl;
    private final Servant servant;
    private final File userBrowserDirectory;
    private final File userDriverDirectory;

    public LocalBrowserEnsurer(Servant servant) {
        this.servant = servant;
        File homePath = new File(System.getProperty("user.home"));
        userBrowserDirectory = new File(homePath, "selenium/browser/");
        userDriverDirectory = new File(homePath, "selenium/driver/");
        try {
            this.firefoxBaseUrl = new URL("https://ftp.mozilla.org/pub/mozilla.org/firefox/releases/");
            this.chromeDriverBaseUrl = new URL("http://chromedriver.storage.googleapis.com");
            this.ieDriverBinUrl = new URL(String.format("http://selenium-release.storage.googleapis.com/%s/IEDriverServer_Win32_%s.zip",
                servant.getMojo().getSeleniumVersion().getMajorMinorFormat(), servant.getMojo().getSeleniumVersion().getMajorMinorFormat() + ".0"));
        } catch (MalformedURLException ex) {
            servant.getLog().error(ex);
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void ensure() {
        Browser browser = servant.getBrowser();
        if (browser.isUnknown()) {
            throw new UnsupportedOperationException("Could not determine the browser.");
        }
        servant.setProjectProperty(getProperty().getBrowserPropertyName(), browser.getName());
        if (browser.isFirefox()) {
            ensureFirefox(browser);
        } else {
            servant.getLog().info(String.format("The <%s> browser is not supported for downloading the binaries. Will use system's default <%s> browser.", browser.getName(), browser.getName()));
            if (browser.isChrome()) {
                ensureChrome();
            } else if (browser.isInternetExplorer()) {
                ensureIE();
            }
        }
    }

    protected void ensureChrome() {
        File currentChromeDriverDir = new File(userDriverDirectory, "chrome/" + getProperty().getChromeDriverVersion());
        if (!currentChromeDriverDir.exists()) {
            currentChromeDriverDir.mkdirs();
        }
        try {
            File currentChromeDriverZip = new File(currentChromeDriverDir, "driver.zip");
            servant.downloadFile(getChromeDriverBinURL(), currentChromeDriverZip);
            servant.extract(currentChromeDriverDir, currentChromeDriverZip);
            File extractedChromeDriverBin = new File(currentChromeDriverDir, "chromedriver" + (servant.isOnWindows() ? ".exe" : ""));
            extractedChromeDriverBin.setExecutable(true);
            servant.setProjectProperty(getProperty().getChromeDriverBinPropertyName(), extractedChromeDriverBin.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void ensureFirefox(Browser browser) {
        if (browser.isUnknownVersion()) {
            servant.getLog().info("Firefox version not specified, using system's firefox.");
            String firefoxBin = servant.isOnWindows() ? getProperty().getFirefoxDefaultBinWindows() : getProperty().getFirefoxDefaultBinLinux();
            if (!new File(firefoxBin).exists()) {
                throw new RuntimeException(String.format("The system's firefox bin was not found at <%s>.", firefoxBin));
            }
            servant.setProjectProperty(getProperty().getFirefoxBinPropertyName(), firefoxBin);
        } else {
            if (servant.isOnWindows()) {
                throw new UnsupportedOperationException("Firefox binaries are not available for Windows.");
            }
            String versionFull = browser.getVersion().getFullFormat();
            File currentFirefoxDir = new File(userBrowserDirectory, "firefox/" + versionFull);
            if (!currentFirefoxDir.exists()) {
                currentFirefoxDir.mkdirs();
            }
            List<String> possibleVersions = Lists.newArrayList();
            possibleVersions.add(versionFull);
            possibleVersions.add(browser.getVersion().getFormat(EnumSet.of(Format.major, Format.minor, Format.specifier)));
            possibleVersions.add(browser.getVersion().getFormat(EnumSet.of(Format.major, Format.specifier)));
            Exception caughtException = null;
            for (String possibleVersion : possibleVersions) {
                try {
                    servant.getLog().info("Trying to find version " + possibleVersion);
                    File currentFirefoxZip = new File(currentFirefoxDir, "firefox-" + possibleVersion + ".tar.bz2");
                    servant.downloadFile(getFirefoxBinURL(possibleVersion), currentFirefoxZip);
                    servant.extract(currentFirefoxDir, currentFirefoxZip);
                    File extractedFirefoxDir = new File(currentFirefoxDir, "firefox");
                    File firefoxBinary = new File(extractedFirefoxDir, "firefox");
                    firefoxBinary.setExecutable(true);
                    servant.setProjectProperty(getProperty().getFirefoxBinPropertyName(), firefoxBinary.getAbsolutePath());
                    return;
                } catch (Exception e) {
                    servant.getLog().info("Version was not found");
                    caughtException = e;
                }
            }
            throw new RuntimeException(caughtException);
        }
    }

    protected void ensureIE() {
        if (!servant.isOnWindows()) {
            throw new RuntimeException(String.format("IE browser cannot be used on <%s> OS.", System.getProperty("os.name")));
        }
        File currentIEDriverDir = new File(userDriverDirectory, "ie/32");
        if (!currentIEDriverDir.exists()) {
            currentIEDriverDir.mkdirs();
        }
        try {
            // always use 32bit version
            File currentIEDriverZip = new File(currentIEDriverDir, "driver.zip");
            servant.downloadFile(getIEDriverBinURL(), currentIEDriverZip);
            servant.extract(currentIEDriverDir, currentIEDriverZip);
            File extractedIEDriverBin = new File(currentIEDriverDir, "IEDriverServer.exe");
            extractedIEDriverBin.setExecutable(true);
            servant.setProjectProperty(getProperty().getIeDriverBinPropertyName(), extractedIEDriverBin.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected URL getFirefoxBinURL(String versionString) throws MalformedURLException {
        return new URL(firefoxBaseUrl, getCorrectFirefoxVersionString(versionString));
    }

    protected URL getChromeDriverBinURL() throws MalformedURLException {
        return new URL(chromeDriverBaseUrl, getCorrectChromeDriverVersionString());
    }

    protected String getCorrectChromeDriverVersionString() {
        String name;
        if (servant.isOnLinux()) {
            name = servant.is64bitArch() ? "linux64.zip" : "linux32.zip";
        } else if (servant.isOnWindows()) {
            name = "win32.zip";
        } else {
            throw new UnsupportedOperationException(String.format("OS arch <%s> not supported.", servant.getOsArch()));
        }
        return String.format("%s/chromedriver_%s", getProperty().getChromeDriverVersion(), name);
    }

    protected String getCorrectFirefoxVersionString(String versionString) {
        String os;
        if (servant.isOnLinux()) {
            os = servant.is64bitArch() ? "linux-x86_64" : "linux-i686";
        } else if (servant.isOnWindows()) {
            os = "win32";
        } else {
            throw new UnsupportedOperationException(String.format("OS arch <%s> not supported.", servant.getOsArch()));
        }
        return String.format("%s/%s/en-US/firefox-%s.tar.bz2", versionString, os, versionString);
    }

    protected URL getIEDriverBinURL() throws MalformedURLException {
        return ieDriverBinUrl;
    }

    private ProcessMojo getProperty() {
        return servant.getMojo();
    }
}
