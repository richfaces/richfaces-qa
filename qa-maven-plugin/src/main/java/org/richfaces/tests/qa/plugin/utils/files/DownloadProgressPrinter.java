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

import static org.richfaces.tests.qa.plugin.utils.Utils.waitFor;

import java.nio.channels.FileChannel;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
class DownloadProgressPrinter extends Thread {

    private final FileChannel fc;
    private final long fileSize;
    private final long updateTimeInMillis;

    DownloadProgressPrinter(FileChannel fc, long fileSize, long updateTimeInMillis) {
        this.fc = fc;
        this.fileSize = fileSize;
        this.updateTimeInMillis = updateTimeInMillis;
    }

    DownloadProgressPrinter(FileChannel fos, long fileSize) {
        this(fos, fileSize, 1500);
    }

    @Override
    public void run() {
        long downloadedSize;
        long prevDownloadedSize = 0;
        double progress;
        double downloadSpeedKiBPerSec;
        boolean notCompleted = true;
        while (fc.isOpen() || notCompleted) {
            try {
                if (fc.isOpen()) {
                    downloadedSize = fc.size();
                } else {
                    downloadedSize = fileSize;
                }
                progress = (((downloadedSize * 1.0) / fileSize) * 100.0);
                downloadSpeedKiBPerSec = (downloadedSize - prevDownloadedSize) / updateTimeInMillis;
                System.out.println(String.format("downloaded %s B of %s B [%.2f %%], at %.1f KiB/s", downloadedSize, fileSize, progress, downloadSpeedKiBPerSec));
                prevDownloadedSize = downloadedSize;
                if (downloadedSize != fileSize) {
                    waitFor(updateTimeInMillis);
                } else {
                    notCompleted = false;
                    System.out.println("File downloaded.");
                }
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }
}
