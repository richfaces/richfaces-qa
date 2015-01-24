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

import org.richfaces.tests.qa.plugin.ensurer.browser.BrowserEnsurer;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.utils.Servant;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LocalChromeEnsurer implements BrowserEnsurer {

    private static final String CHROMEDRIVER = "chromedriver";
    private static final String DRIVER_ZIP = "driver.zip";
    private static final String LINUX32ZIP = "linux32.zip";
    private static final String LINUX64ZIP = "linux64.zip";
    private static final String WIN32ZIP = "win32.zip";

    private final PropertiesProvider pp;
    private final Servant servant;

    @Inject
    public LocalChromeEnsurer(PropertiesProvider pp, Servant servant) {
        this.pp = pp;
        this.servant = servant;
    }

    @Override
    public void ensure() {
        File currentChromeDriverDir = new File(MessageFormat.format("{0}/chrome/{1}", pp.getUserWebDriverDirectory().getAbsolutePath(), pp.getChromeDriverVersion()));
        if (!currentChromeDriverDir.exists()) {
            currentChromeDriverDir.mkdirs();
        }
        try {
            File currentChromeDriverZip = new File(currentChromeDriverDir, DRIVER_ZIP);
            servant.downloadFile(getChromeDriverBinURL(), currentChromeDriverZip);
            File outputDir = new File(pp.getProjectBuildDirectory());
            servant.extract(outputDir, currentChromeDriverZip);
            File extractedChromeDriverBin = new File(outputDir, CHROMEDRIVER + (pp.isOnWindows() ? ".exe" : ""));
            extractedChromeDriverBin.setExecutable(true);
            servant.setProjectProperty(pp.getChromeDriverBinPropertyName(), extractedChromeDriverBin.getAbsolutePath());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected URL getChromeDriverBinURL() throws MalformedURLException {
        return new URL(pp.getChromeDriverBaseURL(), getCorrectChromeDriverVersionString());
    }

    protected String getCorrectChromeDriverVersionString() {
        String name;
        if (pp.isOnLinux()) {
            name = pp.is64bitArch() ? LINUX64ZIP : LINUX32ZIP;
        } else if (pp.isOnWindows()) {
            name = WIN32ZIP;
        } else {
            throw new UnsupportedOperationException(MessageFormat.format("OS arch <{0}> not supported.", pp.getOsArch()));
        }
        return MessageFormat.format("{0}/chromedriver_{1}", pp.getChromeDriverVersion(), name);
    }

}
