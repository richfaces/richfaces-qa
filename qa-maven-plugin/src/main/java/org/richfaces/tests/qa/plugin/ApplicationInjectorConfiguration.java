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

import org.apache.maven.plugin.logging.Log;
import org.richfaces.tests.qa.plugin.ensurer.EnsurersProvider;
import org.richfaces.tests.qa.plugin.ensurer.SimpleEnsurersProvider;
import org.richfaces.tests.qa.plugin.ensurer.browser.BrowserEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.browser.BrowserEnsurerProvider;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.JenkinsFirefoxDirectoryFinder;
import org.richfaces.tests.qa.plugin.ensurer.browser.jenkins.SimpleJenkinsFirefoxDirectoryFinder;
import org.richfaces.tests.qa.plugin.ensurer.eap.EAPEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.eap.EAPEnsurerProvider;
import org.richfaces.tests.qa.plugin.ensurer.taskkill.SimpleTaskKillEnsurer;
import org.richfaces.tests.qa.plugin.ensurer.taskkill.TaskKillEnsurer;
import org.richfaces.tests.qa.plugin.executor.Executor;
import org.richfaces.tests.qa.plugin.executor.SimpleExecutor;
import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.properties.eap.EAPProperties;
import org.richfaces.tests.qa.plugin.properties.eap.SimpleEAPProperties;
import org.richfaces.tests.qa.plugin.utils.files.Copier;
import org.richfaces.tests.qa.plugin.utils.files.Downloader;
import org.richfaces.tests.qa.plugin.utils.files.Extractor;
import org.richfaces.tests.qa.plugin.utils.files.SimpleCopier;
import org.richfaces.tests.qa.plugin.utils.files.SimpleDownloader;
import org.richfaces.tests.qa.plugin.utils.files.SimpleExtractor;

import com.google.inject.AbstractModule;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ApplicationInjectorConfiguration extends AbstractModule {

    private final PropertiesProvider pp;

    public ApplicationInjectorConfiguration(PropertiesProvider pp) {
        this.pp = pp;
    }

    @Override
    protected void configure() {
        bind(PropertiesProvider.class).toInstance(pp);
        bind(Log.class).toInstance(pp.getLog());

        bind(Executor.class).to(SimpleExecutor.class);

        bind(EnsurersProvider.class).to(SimpleEnsurersProvider.class);

        bind(EAPProperties.class).to(SimpleEAPProperties.class);

        bind(TaskKillEnsurer.class).to(SimpleTaskKillEnsurer.class);
        bind(EAPEnsurer.class).toProvider(EAPEnsurerProvider.class);
        bind(BrowserEnsurer.class).toProvider(BrowserEnsurerProvider.class);

        bind(JenkinsFirefoxDirectoryFinder.class).to(SimpleJenkinsFirefoxDirectoryFinder.class);

        bind(Copier.class).to(SimpleCopier.class);
        bind(Extractor.class).to(SimpleExtractor.class);
        bind(Downloader.class).to(SimpleDownloader.class);
    }

}
