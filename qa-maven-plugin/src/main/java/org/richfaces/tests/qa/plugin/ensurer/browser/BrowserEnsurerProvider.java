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

import java.text.MessageFormat;

import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsChromeEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsFirefoxDirectoryFinder;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsFirefoxEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsInternetExplorerEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.local.LocalChromeEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.local.LocalFirefoxEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.local.LocalInternetExplorerEnsurer;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.utils.Browser;
import org.richfaces.tests.qa.plugin.utils.Servant;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class BrowserEnsurerProvider implements Provider<BrowserEnsurer> {

    private final JenkinsFirefoxDirectoryFinder finder;
    private final PropertiesProvider pp;
    private final Servant servant;

    @Inject
    public BrowserEnsurerProvider(PropertiesProvider pp, Servant servant, JenkinsFirefoxDirectoryFinder finder) {
        this.pp = pp;
        this.servant = servant;
        this.finder = finder;
    }

    @Override
    public BrowserEnsurer get() {
        Browser browser = pp.getBrowser();
        servant.setProjectProperty(pp.getBrowserPropertyName(), pp.getBrowser().getName());
        switch (browser.getName()) {
            case chrome:
                return pp.isOnJenkins() ? new JenkinsChromeEnsurer(pp, servant) : new LocalChromeEnsurer(pp, servant);
            case firefox:
                return pp.isOnJenkins() ? new JenkinsFirefoxEnsurer(pp, servant, finder) : new LocalFirefoxEnsurer(pp, servant);
            case internetExplorer:
                return pp.isOnJenkins() ? new JenkinsInternetExplorerEnsurer(pp, servant) : new LocalInternetExplorerEnsurer(pp, servant);
            case unknown:
            default:
                throw new UnsupportedOperationException(MessageFormat.format("Not supported browser <{0}>.", browser.getName()));
        }
    }

}
