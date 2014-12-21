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

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.MessageFormat;

import org.apache.maven.plugin.logging.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Singleton
public class SimpleDownloader implements Downloader {

    private final Log log;

    @Inject
    public SimpleDownloader(Log log) {
        this.log = log;
    }

    @Override
    public void downloadFile(URL from, File to) {
        try {
            final long fileSize = from.openConnection().getContentLengthLong();
            if (to.exists() && to.length() == fileSize) {
                getLog().info(MessageFormat.format("File <{0}> is already downloaded and it has the right size.", to.getName()));
            } else if (fileSize == -1) {// fileSize==-1 => URL is not accessible, work offline
                if (to.exists()) {
                    getLog().info(MessageFormat.format("The URL for download is not accessible, but the file <{0}> is already downloaded. Will try to use that file, but it can be corrupted.", to.getAbsolutePath()));
                } else {
                    throw new RuntimeException(MessageFormat.format("The URL <{0}> is not accessible!", from));
                }
            } else {
                if (to.exists()) {
                    getLog().info(MessageFormat.format("File <{0}> exists, but the size of downloaded <{1} B> and size from url <{2} B> does not match. Deleting the downloaded file.", to.getAbsolutePath(), to.length(), fileSize));
                    to.delete();
                }
                getLog().info(MessageFormat.format("Trying to download from <{0}> to <{1}>.", from.toString(), to.getAbsolutePath()));
                final ReadableByteChannel rbc = Channels.newChannel(from.openStream());
                final FileOutputStream fos = new FileOutputStream(to);
                DownloadProgressPrinter progressPrinter = new DownloadProgressPrinter(fos, fileSize);
                progressPrinter.start();
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                rbc.close();
                progressPrinter.join();
            }
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public Log getLog() {
        return log;
    }
}
