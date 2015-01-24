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
package org.richfaces.tests.qa.plugin.utils.files;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.text.MessageFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.maven.plugin.logging.Log;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Singleton
public class SimpleExtractor implements Extractor {

    private final Log log;

    @Inject
    public SimpleExtractor(Log log) {
        this.log = log;
    }

    @Override
    public void extract(File baseDir, File archive) throws IOException {
        if (archive.getAbsolutePath().endsWith("zip")) {
            getLog().info(MessageFormat.format("Extracting zip file <{0}> to directory <{1}>.", archive.getAbsolutePath(), baseDir));
            final ZipInputStream is = new ZipInputStream(new BufferedInputStream(new FileInputStream(archive)));
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    final File file = new File(baseDir, entry.getName());
                    if (file.exists()) {
                        file.delete();
                    }
                    Files.createParentDirs(file);
                    Files.write(ByteStreams.toByteArray(is), file);
                }
            }
            is.close();
        } else if (archive.getAbsolutePath().endsWith("tar.bz2") || archive.getAbsolutePath().endsWith("tar.gz")) {
            getLog().info(MessageFormat.format("Extracting tar.bz2/tar.gz file <{0}> to directory <{1}>.", archive.getAbsolutePath(), baseDir));

            // unzip to tar
            File tarfile = new File(baseDir, "archive.tar");
            tarfile.delete();
            BZip2CompressorInputStream from = new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(archive)));
            FileOutputStream to = new FileOutputStream(tarfile);
            to.getChannel().transferFrom(Channels.newChannel(from), 0, Long.MAX_VALUE);
            // untar
            final TarArchiveInputStream is = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(tarfile)));
            TarArchiveEntry entry;
            File file;
            while ((entry = is.getNextTarEntry()) != null) {
                if (!entry.isDirectory()) {
                    file = new File(baseDir, entry.getName());
                    Files.createParentDirs(file);
                    Files.write(ByteStreams.toByteArray(is), file);
                }
            }
            is.close();
            tarfile.delete();
        } else {
            throw new UnsupportedOperationException("Not supported file format " + archive.getName());
        }
        getLog().info(MessageFormat.format("Extracting of <{0}> completed.", archive.getAbsolutePath()));
    }

    public Log getLog() {
        return log;
    }
}
