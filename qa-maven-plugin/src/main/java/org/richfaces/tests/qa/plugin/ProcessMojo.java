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
package org.richfaces.tests.qa.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.richfaces.tests.qa.plugin.properties.eap.EAPProperties;
import org.richfaces.tests.qa.plugin.utils.Servant;
import org.richfaces.tests.qa.plugin.utils.Version;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Mojo(name = "process")
public class ProcessMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    @Parameter(defaultValue = "qa.webdriver.browser")
    private String browserPropertyName;

    @Parameter(defaultValue = "qa.chrome.driver.bin")
    private String chromeDriverBinPropertyName;
    @Parameter(property = "chromeDriverVersion")
    private String chromeDriverVersion;

    @Parameter(defaultValue = "qa.eap.home")
    private String eapHomePropertyName;

    @Parameter(property = "eapVersion")
    private String eapVersion;

    @Parameter(defaultValue = "qa.firefox.bin")
    private String firefoxBinPropertyName;
    @Parameter(defaultValue = "/bin/firefox")
    private String firefoxDefaultBinLinux;
    @Parameter(defaultValue = "C:/Program Files (x86)/Mozilla Firefox/firefox.exe")
    private String firefoxDefaultBinWindows;

    @Parameter(defaultValue = "qa.ie.driver.bin")
    private String ieDriverBinPropertyName;

    @Parameter(property = "jenkinsFirefoxVersionMinimal")
    private String jenkinsFirefoxVersionMinimal;
    @Parameter(property = "jenkinsFirefoxVersionOptimal")
    private String jenkinsFirefoxVersionOptimal;

    @Parameter(defaultValue = "${project.build.directory}")
    private String projectBuildDirectory;

    @Parameter(property = "seleniumVersion")
    private String seleniumVersion;

    private EAPProperties eapProperties;

    private final Servant servant = new Servant(this);

    private EAPProperties createEAPProps(String eapVersion) {
        return EAPProperties.getPropertiesForVersion(eapVersion, servant);
    }

    @Override
    public void execute() throws MojoExecutionException {
        if (servant.isEAPProfileActivated()) {
            if ((eapVersion == null || eapVersion.isEmpty())) {
                throw new RuntimeException("The eap profile is activated, but the version of EAP is not specified. Set it by "
                    + "system property 'eapVersion' or 'version.eap' (from qa pom.xml)");
            }
            eapProperties = createEAPProps(eapVersion);
        }
        servant.getCommands().performAll();
    }

    public String getBrowserPropertyName() {
        return browserPropertyName;
    }

    public String getChromeDriverBinPropertyName() {
        return chromeDriverBinPropertyName;
    }

    public String getChromeDriverVersion() {
        return chromeDriverVersion;
    }

    public String getEapHomePropertyName() {
        return eapHomePropertyName;
    }

    public EAPProperties getEapProperties() {
        return eapProperties;
    }

    public String getEapVersion() {
        return eapVersion;
    }

    public String getFirefoxBinPropertyName() {
        return firefoxBinPropertyName;
    }

    public String getFirefoxDefaultBinLinux() {
        return firefoxDefaultBinLinux;
    }

    public String getFirefoxDefaultBinWindows() {
        return firefoxDefaultBinWindows;
    }

    public String getIeDriverBinPropertyName() {
        return ieDriverBinPropertyName;
    }

    public Version getJenkinsFirefoxVersionMinimal() {
        return Version.parseVersion(jenkinsFirefoxVersionMinimal);
    }

    public Version getJenkinsFirefoxVersionOptimal() {
        return Version.parseVersion(jenkinsFirefoxVersionOptimal);
    }

    public MavenProject getProject() {
        return project;
    }

    public String getProjectBuildDirectory() {
        return projectBuildDirectory;
    }

    public Version getSeleniumVersion() {
        return Version.parseVersion(seleniumVersion);
    }
}
