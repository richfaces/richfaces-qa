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
package org.richfaces.tests.qa.plugin.utils;

import static org.richfaces.tests.qa.plugin.utils.Utils.waitFor;

import java.io.FileOutputStream;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class DownloadProgressPrinter extends Thread {

    private final FileOutputStream fos;
    private final long fileSize;
    private final long updateTimeInMillis;

    public DownloadProgressPrinter(FileOutputStream fos, long fileSize, long updateTimeInMillis) {
        this.fos = fos;
        this.fileSize = fileSize;
        this.updateTimeInMillis = updateTimeInMillis;
    }

    public DownloadProgressPrinter(FileOutputStream fos, long fileSize) {
        this(fos, fileSize, 1500);
    }

    @Override
    public void run() {
        long downloadedSize;
        double progress;
        boolean notCompleted = true;
        while (fos.getChannel().isOpen() || notCompleted) {
            try {
                if (fos.getChannel().isOpen()) {
                    downloadedSize = fos.getChannel().size();
                } else {
                    downloadedSize = fileSize;
                }
                progress = (((downloadedSize * 1.0) / fileSize) * 100.0);
                System.out.println(String.format("downloaded %s B of %s B [%.2f %%]", downloadedSize, fileSize, progress));
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
