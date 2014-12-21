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
package org.richfaces.tests.qa.plugin.properties;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.richfaces.tests.qa.plugin.utils.Browser;
import org.richfaces.tests.qa.plugin.utils.JenkinsFirefoxConfiguration;
import org.richfaces.tests.qa.plugin.utils.Version;

public interface PropertiesProvider {

    List<String> getAllActivatedProfiles();

    Browser getBrowser();

    String getBrowserPropertyName();

    URL getChromeDriverBaseURL();

    String getChromeDriverBinPropertyName();

    String getChromeDriverVersion();

    String getEapHomePropertyName();

    Version getEapVersion();

    URL getFirefoxBaseURL();

    String getFirefoxBinPropertyName();

    String getFirefoxDefaultBinUnix();

    String getFirefoxDefaultBinWindows();

    URL getIeDriverBaseURL();

    String getIeDriverBinPropertyName();

    List<JenkinsFirefoxConfiguration> getJenkinsFirefoxConfigurations();

    Version getJenkinsFirefoxVersionMinimal();

    Version getJenkinsFirefoxVersionOptimal();

    Log getLog();

    String getOSName();

    String getOSVersion();

    String getOsArch();

    MavenProject getProject();

    String getProjectBuildDirectory();

    Version getSeleniumVersion();

    File getUserBrowserDirectory();

    String getUserHome();

    File getUserHomeDirectory();

    String getUserName();

    File getUserWebDriverDirectory();

    boolean is64bitArch();

    boolean isEAPProfileActivated();

    boolean isEnsureBrowser();

    boolean isEnsureEAP();

    boolean isEnsureTasksCleanup();

    boolean isGlassFishProfileActivated();

    boolean isJBossASProfileActivated();

    boolean isOnJenkins();

    boolean isOnLinux();

    boolean isOnMac();

    boolean isOnSolaris();

    boolean isOnWindows();

    boolean isRemoteProfileActivated();

    boolean isTomcatProfileActivated();
}
