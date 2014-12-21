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
package org.richfaces.tests.qa.plugin;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.richfaces.tests.qa.plugin.executor.Executor;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.utils.Browser;
import org.richfaces.tests.qa.plugin.utils.JenkinsFirefoxConfiguration;
import org.richfaces.tests.qa.plugin.utils.TolerantContainsList;
import org.richfaces.tests.qa.plugin.utils.Utils;
import org.richfaces.tests.qa.plugin.utils.Version;
import org.richfaces.tests.qa.plugin.utils.cache.LazyLoadedCachedSystemProperty;
import org.richfaces.tests.qa.plugin.utils.cache.LazyLoadedCachedValue;

import com.google.inject.Guice;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Mojo(name = "process")
public class ProcessMojo extends AbstractMojo implements PropertiesProvider {

    private static final String AS = "as";
    private static final String BROWSER = "browser";
    private static final String EAP = "eap";
    private static final String GLASSFISH = "glassfish";
    private static final String HUDSON = "hudson";
    private static final File HUDSON_LINUX_DIRECTORY = new File("/home/hudson/static_build_env/");
    private static final File HUDSON_WINDOWS_DIRECTORY = new File("h:/hudson/static_build_env/");
    private static final String INJECTED_PROFILE_IDS_FIELDNAME = "getInjectedProfileIds";
    private static final String JBOSS = "jboss";
    private static final String LINUX = "linux";
    private static final String MAC = "mac";
    private static final String OS_ARCH = "os.arch";
    private static final String OS_NAME = "os.name";
    private static final String OS_VERSION = "os.version";
    private static final String REMOTE = "remote";
    private static final String SELENIUM_BROWSER_DIR = "selenium/browser/";
    private static final String SELENIUM_DRIVER_DIR = "selenium/driver/";
    private static final String STRING_64 = "64";
    private static final String SUNOS = "sunos";
    private static final String TOMCAT = "tomcat";
    private static final String USER_HOME = "user.home";
    private static final String USER_NAME = "user.name";
    private static final String WIN = "win";

    private LazyLoadedCachedValue<List<String>> activatedProfiles = new LazyLoadedCachedValue<List<String>>() {
        @SuppressWarnings("unchecked")
        @Override
        public List<String> initValue() {
            try {
                Method getInjectedProfileIdsMethod = MavenProject.class.getMethod(INJECTED_PROFILE_IDS_FIELDNAME);
                Map<String, List<String>> profilesMap = (Map<String, List<String>>) getInjectedProfileIdsMethod.invoke(getProject());
                Set<String> result = new HashSet<String>(20);
                for (List<String> listOfProfiles : profilesMap.values()) {
                    for (String profile : listOfProfiles) {
                        result.add(profile.toLowerCase());
                    }
                }
                return new TolerantContainsList(result);
            } catch (Throwable ex) {
                getLog().error(ex);
            }
            return Collections.EMPTY_LIST;
        }
    };

    private final LazyLoadedCachedValue<Browser> browser = new LazyLoadedCachedValue<Browser>() {
        @Override
        protected Browser initValue() {
            String browserFromSystemProperty = System.getProperty(BROWSER);
            if (browserFromSystemProperty != null && !browserFromSystemProperty.isEmpty()) {
                return Browser.parseFromString(browserFromSystemProperty);
            }
            throw new IllegalStateException("No browser specified! You can use any of these: -Dbrowser=firefox , -Dbrowser=chrome , -Dbrowser=ie");
        }
    };

    @Parameter(defaultValue = "qa.webdriver.browser")
    private String browserPropertyName;

    private final LazyLoadedCachedValue<Version> cachedEAPVersion = new LazyLoadedCachedValue<Version>() {
        @Override
        protected Version initValue() {
            return Version.parseEapVersion(eapVersion);
        }
    };
    private final LazyLoadedCachedValue<Version> cachedJenkinsFirefoxVersionMinimal = new LazyLoadedCachedValue<Version>() {
        @Override
        protected Version initValue() {
            return Version.parseFirefoxVersion(jenkinsFirefoxVersionMinimal);
        }
    };
    private final LazyLoadedCachedValue<Version> cachedJenkinsFirefoxVersionOptimal = new LazyLoadedCachedValue<Version>() {
        @Override
        protected Version initValue() {
            return Version.parseFirefoxVersion(jenkinsFirefoxVersionOptimal);
        }
    };

    private final LazyLoadedCachedValue<Version> cachedSeleniumVersion = new LazyLoadedCachedValue<Version>() {
        @Override
        public Version initValue() {
            return Version.parseVersion(seleniumVersion);
        }
    };

    private final URL chromeDriverBaseURL = Utils.createURLSilently("http://chromedriver.storage.googleapis.com");
    @Parameter(defaultValue = "qa.chrome.driver.bin")
    private String chromeDriverBinPropertyName;
    @Parameter(property = "chromeDriverVersion")
    private String chromeDriverVersion;

    @Parameter(defaultValue = "qa.eap.home")
    private String eapHomePropertyName;
    @Parameter(property = "eapVersion")
    private String eapVersion;

    @Parameter(property = "ensure.browser")
    private boolean ensureBrowser;
    @Parameter(property = "ensure.eap")
    private boolean ensureEAP;
    @Parameter(property = "ensure.cleanup")
    private boolean ensureTasksCleanup;

    private final URL firefoxBaseURL = Utils.createURLSilently("https://ftp.mozilla.org/pub/mozilla.org/firefox/releases/");
    @Parameter(defaultValue = "qa.firefox.bin")
    private String firefoxBinPropertyName;
    @Parameter(defaultValue = "/usr/bin/firefox")
    private String firefoxDefaultBinUnix;
    @Parameter(defaultValue = "C:/Program Files (x86)/Mozilla Firefox/firefox.exe")
    private String firefoxDefaultBinWindows;

    private final LazyLoadedCachedValue<URL> ieDriverBaseURL = new LazyLoadedCachedValue<URL>() {
        @Override
        protected URL initValue() {
            return Utils.createURLSilently(MessageFormat.format("http://selenium-release.storage.googleapis.com/{0}/IEDriverServer_Win32_{1}.zip",
                getSeleniumVersion().getMajorMinorFormat(), getSeleniumVersion().getMajorMinorFormat() + ".0"));
        }
    };
    @Parameter(defaultValue = "qa.ie.driver.bin")
    private String ieDriverBinPropertyName;

    @Parameter
    private List<JenkinsFirefoxConfiguration> jenkinsFirefoxConfigurations;
    @Parameter(property = "jenkinsFirefoxVersionMinimal")
    private String jenkinsFirefoxVersionMinimal;
    @Parameter(property = "jenkinsFirefoxVersionOptimal")
    private String jenkinsFirefoxVersionOptimal;

    private final LazyLoadedCachedValue<String> osArch = new LazyLoadedCachedSystemProperty(OS_ARCH);
    private final LazyLoadedCachedValue<String> osName = new LazyLoadedCachedSystemProperty(OS_NAME);
    private final LazyLoadedCachedValue<String> osVersion = new LazyLoadedCachedSystemProperty(OS_VERSION);

    @Component
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}")
    private String projectBuildDirectory;

    @Parameter(property = "seleniumVersion")
    private String seleniumVersion;

    private final LazyLoadedCachedValue<String> userHome = new LazyLoadedCachedSystemProperty(USER_HOME);
    private final LazyLoadedCachedValue<String> userName = new LazyLoadedCachedSystemProperty(USER_NAME);

    @Override
    public void execute() throws MojoExecutionException {
        Guice.createInjector(new ApplicationInjectorConfiguration(this)).getInstance(Executor.class).execute();
    }

    @Override
    public List<String> getAllActivatedProfiles() {
        return activatedProfiles.getValue();
    }

    @Override
    public Browser getBrowser() {
        return browser.getValue();
    }

    @Override
    public String getBrowserPropertyName() {
        return browserPropertyName;
    }

    @Override
    public URL getChromeDriverBaseURL() {
        return chromeDriverBaseURL;
    }

    @Override
    public String getChromeDriverBinPropertyName() {
        return chromeDriverBinPropertyName;
    }

    @Override
    public String getChromeDriverVersion() {
        return chromeDriverVersion;
    }

    @Override
    public String getEapHomePropertyName() {
        return eapHomePropertyName;
    }

    @Override
    public Version getEapVersion() {
        return cachedEAPVersion.getValue();
    }

    @Override
    public URL getFirefoxBaseURL() {
        return firefoxBaseURL;
    }

    @Override
    public String getFirefoxBinPropertyName() {
        return firefoxBinPropertyName;
    }

    @Override
    public String getFirefoxDefaultBinUnix() {
        return firefoxDefaultBinUnix;
    }

    @Override
    public String getFirefoxDefaultBinWindows() {
        return firefoxDefaultBinWindows;
    }

    @Override
    public URL getIeDriverBaseURL() {
        return ieDriverBaseURL.getValue();
    }

    @Override
    public String getIeDriverBinPropertyName() {
        return ieDriverBinPropertyName;
    }

    @Override
    public List<JenkinsFirefoxConfiguration> getJenkinsFirefoxConfigurations() {
        return jenkinsFirefoxConfigurations;
    }

    @Override
    public Version getJenkinsFirefoxVersionMinimal() {
        return cachedJenkinsFirefoxVersionMinimal.getValue();
    }

    @Override
    public Version getJenkinsFirefoxVersionOptimal() {
        return cachedJenkinsFirefoxVersionOptimal.getValue();
    }

    @Override
    public String getOSName() {
        return osName.getValue();
    }

    @Override
    public String getOSVersion() {
        return osVersion.getValue();
    }

    @Override
    public String getOsArch() {
        return osArch.getValue();
    }

    @Override
    public MavenProject getProject() {
        return project;
    }

    @Override
    public String getProjectBuildDirectory() {
        return projectBuildDirectory;
    }

    @Override
    public Version getSeleniumVersion() {
        return cachedSeleniumVersion.getValue();
    }

    @Override
    public File getUserBrowserDirectory() {
        return new File(getUserHomeDirectory(), SELENIUM_BROWSER_DIR);
    }

    @Override
    public String getUserHome() {
        return userHome.getValue();
    }

    @Override
    public File getUserHomeDirectory() {
        return new File(getUserHome());
    }

    @Override
    public String getUserName() {
        return userName.getValue();
    }

    @Override
    public File getUserWebDriverDirectory() {
        return new File(getUserHomeDirectory(), SELENIUM_DRIVER_DIR);
    }

    @Override
    public boolean is64bitArch() {
        return getOsArch().contains(STRING_64);
    }

    @Override
    public boolean isEAPProfileActivated() {
        return getAllActivatedProfiles().contains(EAP);
    }

    @Override
    public boolean isEnsureBrowser() {
        return ensureBrowser;
    }

    @Override
    public boolean isEnsureEAP() {
        return ensureEAP;
    }

    @Override
    public boolean isEnsureTasksCleanup() {
        return ensureTasksCleanup;
    }

    @Override
    public boolean isGlassFishProfileActivated() {
        return getAllActivatedProfiles().contains(GLASSFISH);
    }

    @Override
    public boolean isJBossASProfileActivated() {
        return getAllActivatedProfiles().contains(JBOSS) || isEAPProfileActivated() || getAllActivatedProfiles().contains(AS);
    }

    @Override
    public boolean isOnJenkins() {
        return getUserName().toLowerCase().contains(HUDSON) || HUDSON_LINUX_DIRECTORY.exists() || HUDSON_WINDOWS_DIRECTORY.exists();
    }

    @Override
    public boolean isOnLinux() {
        return getOSName().toLowerCase().contains(LINUX);
    }

    @Override
    public boolean isOnMac() {
        return getOSName().toLowerCase().contains(MAC);
    }

    @Override
    public boolean isOnSolaris() {
        return getOSName().toLowerCase().contains(SUNOS);
    }

    @Override
    public boolean isOnWindows() {
        return getOSName().toLowerCase().contains(WIN);
    }

    @Override
    public boolean isRemoteProfileActivated() {
        return getAllActivatedProfiles().contains(REMOTE);
    }

    @Override
    public boolean isTomcatProfileActivated() {
        return getAllActivatedProfiles().contains(TOMCAT);
    }
}
