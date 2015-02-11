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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.MessageFormat;

import org.apache.maven.plugin.logging.Log;

import com.google.common.io.Files;
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
        File partiallyDownloadedFile = null;
        try {
            final long fileSize = from.openConnection().getContentLengthLong();
            URLConnection openedConnection = from.openConnection();
            if (to.exists() && to.length() == fileSize) {
                getLog().info(MessageFormat.format("File <{0}> is already downloaded and it has the right size.", to.getName()));
            } else if (fileSize == -1) {// fileSize==-1 => URL is not accessible, work offline
                if (to.exists()) {
                    getLog().info(MessageFormat.format("The URL for download is not accessible, but the file <{0}> is already downloaded. Will try to use that file, but it can be corrupted.", to.getAbsolutePath()));
                } else {
                    throw new RuntimeException(MessageFormat.format("The URL <{0}> is not accessible!", from));
                }
            } else {
                partiallyDownloadedFile = new File(to.getAbsolutePath() + ".part");
                FileChannel toChannel;
                if (partiallyDownloadedFile.exists()) {
                    getLog().info(MessageFormat.format("Found previous attempt to download file <{0}> at <{1}>. Will try to download the remaining <{2} B>.", to.getName(), partiallyDownloadedFile.getAbsolutePath(), (fileSize - partiallyDownloadedFile.length())));
                    toChannel = new FileOutputStream(partiallyDownloadedFile, true).getChannel();
                } else {
                    Files.createParentDirs(partiallyDownloadedFile);
                    toChannel = new FileOutputStream(partiallyDownloadedFile).getChannel();
                }
                getLog().info(MessageFormat.format("Trying to download from <{0}> to <{1}>.", from.toString(), partiallyDownloadedFile.getAbsolutePath()));

                // to skip downloading of already downloaded bits
                openedConnection.setRequestProperty("Range", "Bytes=" + toChannel.position() + "-");
                final ReadableByteChannel fromChannel = Channels.newChannel(openedConnection.getInputStream());

                DownloadProgressPrinter progressPrinter = new DownloadProgressPrinter(toChannel, fileSize);
                progressPrinter.start();

                toChannel.transferFrom(fromChannel, toChannel.position(), Long.MAX_VALUE);

                toChannel.close();
                fromChannel.close();
                progressPrinter.join();

                getLog().info(MessageFormat.format("Download was successfull. Renaming the file from <{0}> to <{1}>.", partiallyDownloadedFile.getName(), to.getName()));
                Files.copy(partiallyDownloadedFile, to);
                partiallyDownloadedFile.delete();
            }
        } catch (FileNotFoundException ex) {
            if (partiallyDownloadedFile != null && partiallyDownloadedFile.exists()) {
                partiallyDownloadedFile.delete();
            }
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Log getLog() {
        return log;
    }
}
