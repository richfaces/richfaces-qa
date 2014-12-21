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
package org.richfaces.tests.qa.plugin.ensurer.browser.jenkins;

import java.io.File;
import java.io.FileFilter;
import java.util.PriorityQueue;

import org.richfaces.tests.qa.plugin.utils.Version;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleJenkinsFirefoxDirectoryFinder implements JenkinsFirefoxDirectoryFinder {

    private boolean containsFirefoxBin(File[] files) {
        boolean isOnMac = System.getProperty("os.name").toLowerCase().contains("mac");
        if (files != null) {
            if (isOnMac) {
                for (File file : files) {
                    if (file.getName().contains("Contents")) {
                        File[] listFiles = file.listFiles(new FilenameEqualsFilter("MacOS"));
                        if (listFiles == null || listFiles.length != 1) {
                            return false;
                        }
                        File f = listFiles[0];
                        if (f != null) {
                            listFiles = f.listFiles(new FilenameEqualsFilter("firefox"));
                            return !(listFiles == null || listFiles.length != 1);
                        }
                    }
                }
            } else {
                for (File file : files) {
                    if (!file.isDirectory()) {
                        if (file.getName().equals("firefox") || file.getName().equals("firefox.exe")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public File getHighestOrSpecificVersion(File[] files, Version version) {
        File highestVersion = getHighestVersion(files);
        File specificVersion = getSpecificVersion(files, version);
        if (specificVersion == null) {
            return highestVersion;
        } else if (highestVersion == null) {
            return specificVersion;
        } else if (VersionedFirefoxDirectory.parseVersion(highestVersion).compareTo(VersionedFirefoxDirectory.parseVersion(specificVersion)) > 0) {
            return highestVersion;
        }
        return specificVersion;
    }

    @Override
    public File getHighestVersion(File[] files) {
        PriorityQueue<VersionedFirefoxDirectory> p = new PriorityQueue<VersionedFirefoxDirectory>();
        for (File file : files) {
            if (isFileMatching(file)) {
                p.add(new VersionedFirefoxDirectory(file));
            }
        }
        if (p.isEmpty()) {
            return null;
        }
        return p.peek().getFile();
    }

    @Override
    public File getOptimalOrMinimalVersion(File[] files, Version versionOptimal, Version versionMinimal) {
        File f = getSpecificVersion(files, versionOptimal);
        if (f == null) {
            return getSpecificVersion(files, versionMinimal);
        }
        return f;
    }

    @Override
    public File getSpecificVersion(File[] files, Version version) {
        for (File file : files) {
            if (isFileMatching(file)) {
                if (VersionedFirefoxDirectory.parseVersion(file).equals(version)) {
                    return file;
                }
            }
        }
        return null;
    }

    @Override
    public File getSpecificVersionOrHighest(File[] files, Version version) {
        File f = getSpecificVersion(files, version);
        if (f == null) {
            return getHighestVersion(files);
        }
        return f;
    }

    private boolean isFileMatching(File f) {
        return f.isDirectory() && f.getName().matches("firefox-\\d{1,2}(.*)?") && containsFirefoxBin(f.listFiles());
    }

    private static class FilenameEqualsFilter implements FileFilter {

        private final String fileName;

        private FilenameEqualsFilter(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public boolean accept(File file) {
            return file.getName().equals(fileName);
        }

    }

    private static class VersionedFirefoxDirectory implements Comparable<VersionedFirefoxDirectory> {

        private final File file;
        private final Version version;

        public VersionedFirefoxDirectory(File f) {
            file = f;
            version = parseVersion(f);
        }

        public static Version parseVersion(File f) {
            return Version.parseFirefoxVersion(f.getName());
        }

        @Override
        public int compareTo(VersionedFirefoxDirectory t) {
            return t.getVersion().compareTo(getVersion());
        }

        public File getFile() {
            return file;
        }

        public Version getVersion() {
            return version;
        }

        @Override
        public String toString() {
            return "VersionedFile{" + "file=" + file + ", version=" + version + '}';
        }
    }
}
