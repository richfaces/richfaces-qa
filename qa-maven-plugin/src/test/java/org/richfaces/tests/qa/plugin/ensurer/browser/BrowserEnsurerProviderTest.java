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
package org.richfaces.tests.qa.plugin.ensurer.browser;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsChromeEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsFirefoxDirectoryFinder;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsFirefoxEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsInternetExplorerEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.local.LocalChromeEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.local.LocalFirefoxEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.local.LocalInternetExplorerEnsurer;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.utils.Browser;
import org.richfaces.tests.qa.plugin.utils.Browser.BrowserName;
import org.richfaces.tests.qa.plugin.utils.Servant;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class BrowserEnsurerProviderTest {

    @Mock
    private Browser browser;
    @Mock
    private JenkinsFirefoxDirectoryFinder finder;
    @Mock
    private Log log;
    @Mock
    private PropertiesProvider pp;
    private BrowserEnsurerProvider provider;
    @Mock
    private Servant servant;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(pp.getLog()).thenReturn(log);
        provider = new BrowserEnsurerProvider(pp, servant, finder);
    }

    @Test
    public void testGet_whenNotOnJenkinsUsingChrome_returnsLocalChromeEnsurer() {
        when(pp.getBrowser()).thenReturn(browser);
        when(pp.isOnJenkins()).thenReturn(false);
        when(browser.getName()).thenReturn(BrowserName.chrome);

        assertTrue(provider.get() instanceof LocalChromeEnsurer);
    }

    @Test
    public void testGet_whenNotOnJenkinsUsingFirefox_returnsLocalFirefoxEnsurer() {
        when(pp.getBrowser()).thenReturn(browser);
        when(pp.isOnJenkins()).thenReturn(false);
        when(browser.getName()).thenReturn(BrowserName.firefox);

        assertTrue(provider.get() instanceof LocalFirefoxEnsurer);
    }

    @Test
    public void testGet_whenNotOnJenkinsUsingIE_returnsLocalInternetExplorerEnsurer() {
        when(pp.getBrowser()).thenReturn(browser);
        when(pp.isOnJenkins()).thenReturn(false);
        when(browser.getName()).thenReturn(BrowserName.internetExplorer);

        assertTrue(provider.get() instanceof LocalInternetExplorerEnsurer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGet_whenNotOnJenkinsUsingUnknownBrowser_throwsUnsupportedOperationException() {
        when(pp.getBrowser()).thenReturn(browser);
        when(pp.isOnJenkins()).thenReturn(false);
        when(browser.getName()).thenReturn(BrowserName.unknown);

        provider.get();
    }

    @Test
    public void testGet_whenOnJenkinsUsingChrome_returnsJenkinsChromeEnsurer() {
        when(pp.getBrowser()).thenReturn(browser);
        when(pp.isOnJenkins()).thenReturn(true);
        when(browser.getName()).thenReturn(BrowserName.chrome);

        assertTrue(provider.get() instanceof JenkinsChromeEnsurer);
    }

    @Test
    public void testGet_whenOnJenkinsUsingFirefox_returnsJenkinsFirefoxEnsurer() {
        when(pp.getBrowser()).thenReturn(browser);
        when(pp.isOnJenkins()).thenReturn(true);
        when(browser.getName()).thenReturn(BrowserName.firefox);

        assertTrue(provider.get() instanceof JenkinsFirefoxEnsurer);
    }

    @Test
    public void testGet_whenOnJenkinsUsingIE_returnsJenkinsInternetExplorerEnsurer() {
        when(pp.getBrowser()).thenReturn(browser);
        when(pp.isOnJenkins()).thenReturn(true);
        when(browser.getName()).thenReturn(BrowserName.internetExplorer);

        assertTrue(provider.get() instanceof JenkinsInternetExplorerEnsurer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGet_whenOnJenkinsUsingUnknownBrowser_throwsUnsupportedOperationException() {
        when(pp.getBrowser()).thenReturn(browser);
        when(pp.isOnJenkins()).thenReturn(true);
        when(browser.getName()).thenReturn(BrowserName.unknown);

        provider.get();
    }

}
