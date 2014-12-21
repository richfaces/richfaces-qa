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
package org.richfaces.tests.qa.plugin.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.utils.files.Copier;
import org.richfaces.tests.qa.plugin.utils.files.Downloader;
import org.richfaces.tests.qa.plugin.utils.files.Extractor;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Servant {

    private final Copier copier;
    private final Downloader downloader;
    private final Extractor extractor;
    private final PropertiesProvider pp;

    @Inject
    public Servant(Copier copier, Downloader downloader, Extractor extractor, PropertiesProvider pp) {
        this.copier = copier;
        this.downloader = downloader;
        this.extractor = extractor;
        this.pp = pp;
    }

    public void copy(File from, File to) throws IOException {
        copier.copy(from, to);
    }

    public void downloadFile(URL from, File to) {
        downloader.downloadFile(from, to);
    }

    public void extract(File baseDir, File archive) throws IOException {
        extractor.extract(baseDir, archive);
    }

    public void setProjectProperty(String name, Object value) {
        pp.getLog().info(MessageFormat.format("Setting project property <{0}> to value <{1}>.", name, value));
        pp.getProject().getProperties().put(name, value.toString());
    }

    public void setSystemProperty(String name, Object value) {
        pp.getLog().info(MessageFormat.format("Setting system property <{0}> to value <{1}>.", name, value));
        System.getProperties().put(name, value.toString());
    }
}
