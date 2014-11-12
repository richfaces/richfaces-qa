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

import org.richfaces.tests.qa.plugin.ProcessMojo;
import org.richfaces.tests.qa.plugin.command.ensurer.Ensurer;
import org.richfaces.tests.qa.plugin.utils.Browser;
import org.richfaces.tests.qa.plugin.utils.Servant;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class JenkinsBrowserEnsurer implements Ensurer {

    private final File hudsonChromeDriverWin;
    private final File hudsonFirefoxLinux;
    private final File hudsonFirefoxSolaris10;
    private final File hudsonFirefoxSolaris11;
    private final File hudsonFirefoxWindows;
    private final File hudsonIEDriverWin;
    private final String hudsonStaticWin;
    private final File hudsonWebDriverWin;
    private final Servant servant;

    public JenkinsBrowserEnsurer(Servant servant) {
        this.servant = servant;

        hudsonFirefoxLinux = new File("/qa/tools/opt");
        hudsonFirefoxSolaris10 = new File("/qa/tools/SunOS/" + servant.getOsArch());
        hudsonFirefoxSolaris11 = new File("/qa/tools/OpenSolaris/" + servant.getOsArch());
        hudsonFirefoxWindows = new File("t:/opt/windows");

        hudsonStaticWin = "h:/hudson/static_build_env/";
        hudsonWebDriverWin = new File(hudsonStaticWin, "webdriver");
        hudsonChromeDriverWin = new File(hudsonWebDriverWin, "chromedriver/chrome36/win32/chromedriver.exe");
        hudsonIEDriverWin = new File(hudsonWebDriverWin, "iedriver/latest/win64/IEDriverServer.exe");
    }

    @Override
    public void ensure() {
        Browser browser = servant.getBrowser();
        if (browser.isUnknown()) {
            throw new RuntimeException("Could not determine the browser.");
        }
        servant.setProjectProperty(getProperty().getBrowserPropertyName(), browser.getName());
        if (browser.isFirefox()) {
            ensureFirefox(browser);
        } else {
            if (servant.isOnLinux()) {
                throw new RuntimeException("Current browser is not supported on linux.");
            }
            if (browser.isChrome()) {
                ensureChrome();
            } else if (browser.isInternetExplorer()) {
                ensureIE();
            }
        }
    }

    protected void ensureChrome() {
        if (!hudsonChromeDriverWin.exists()) {
            throw new RuntimeException(String.format("The chromedriver binary was not found at <%s>.", hudsonChromeDriverWin.getAbsolutePath()));
        }
        servant.setProjectProperty(getProperty().getChromeDriverBinPropertyName(), hudsonChromeDriverWin.getAbsolutePath());
    }

    protected void ensureFirefox(Browser browser) {
        File firefoxBin = getFirefoxBin(browser);
        if (!firefoxBin.exists()) {
            throw new RuntimeException(String.format("Firefox binary does not exist at <%s>.", firefoxBin.getAbsolutePath()));
        }
        firefoxBin.setExecutable(true);
        servant.setProjectProperty(getProperty().getFirefoxBinPropertyName(), firefoxBin);
    }

    protected void ensureIE() {
        if (!hudsonIEDriverWin.exists()) {
            throw new RuntimeException(String.format("The IEDriver binary was not found at <%s>.", hudsonIEDriverWin.getAbsolutePath()));
        }
        servant.setProjectProperty(getProperty().getIeDriverBinPropertyName(), hudsonIEDriverWin.getAbsolutePath());
    }

    private File getFirefoxBin(Browser browser) {
        File firefoxDir;
        if (browser.isUnknownVersion()) {
            servant.getLog().info(String.format("Firefox version not specified. Using the optimal or minimal version specified with attributes <%s>, <%s>.", "firefoxJenkinsVersionOptimal", "firefoxJenkinsVersionMinimal"));
            servant.getLog().info(String.format("Which are set to <%s> and <%s>.", servant.getMojo().getJenkinsFirefoxVersionOptimal(), servant.getMojo().getJenkinsFirefoxVersionMinimal()));
            firefoxDir = JenkinsFirefoxDirectoryFinder.getOptimalOrMinimalVersion(getFirefoxBinDirPath().listFiles(),
                getProperty().getJenkinsFirefoxVersionOptimal(), getProperty().getJenkinsFirefoxVersionMinimal());
        } else {
            firefoxDir = JenkinsFirefoxDirectoryFinder.getSpecificVersion(getFirefoxBinDirPath().listFiles(), browser.getVersion());
        }
        return new File(firefoxDir, servant.isOnWindows() ? "firefox.exe" : "firefox");
    }

    private File getFirefoxBinDirPath() {
        if (servant.isOnWindows()) {
            return hudsonFirefoxWindows;
        } else if (servant.isOnLinux()) {
            return hudsonFirefoxLinux;
        } else if (servant.isOnSolaris()) {
            String osVersion = servant.getOSVersion();
            if (osVersion.endsWith("10")) {
                return hudsonFirefoxSolaris10;
            } else if (osVersion.endsWith("11")) {
                return hudsonFirefoxSolaris11;
            }
            throw new UnsupportedOperationException(String.format("Not supported Solaris version <%s>.", osVersion));
        }
        throw new UnsupportedOperationException(String.format("Not supported OS <%s>.", servant.getOSName()));
    }

    private ProcessMojo getProperty() {
        return servant.getMojo();
    }
}
