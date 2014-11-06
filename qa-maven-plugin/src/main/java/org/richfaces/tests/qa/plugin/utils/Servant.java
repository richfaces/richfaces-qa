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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.richfaces.tests.qa.plugin.ProcessMojo;
import org.richfaces.tests.qa.plugin.command.Command;
import org.richfaces.tests.qa.plugin.command.JenkinsCommands;
import org.richfaces.tests.qa.plugin.command.LocalCommands;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Servant {

    private static final File HUDSON_LINUX_DIRECTORY = new File("/home/hudson/static_build_env/");
    private static final File HUDSON_WINDOWS_DIRECTORY = new File("h:/hudson/static_build_env/");

    private final ProcessMojo mojo;

    public Servant(ProcessMojo m) {
        this.mojo = m;
    }

    public void downloadFile(URL from, File to) {
        try {
            final long fileSize = from.openConnection().getContentLengthLong();
            if (to.exists() && to.length() == fileSize) {
                getLog().info(String.format("File <%s> is already downloaded.", to.getName()));
            } else {
                if (to.exists()) {
                    getLog().info(String.format("File <%s> exists, but the size of downloaded <%d B> and size from url <%d B> does not match. Deleting the downloaded file.", to.getName(), to.length(), fileSize));
                    to.delete();
                }
                getLog().info(String.format("Trying to download from <%s> to <%s>.", from.toString(), to.getAbsolutePath()));
                final ReadableByteChannel rbc = Channels.newChannel(from.openStream());
                final FileOutputStream fos = new FileOutputStream(to);
                DownloadProgressPrinter progressPrinter = new DownloadProgressPrinter(fos, fileSize);
                progressPrinter.start();
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                rbc.close();
                progressPrinter.join();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void extract(File baseDir, File archive) throws IOException {
        if (archive.getAbsolutePath().endsWith("zip")) {
            getLog().info(String.format("Extracting zip file <%s> to directory <%s>.", archive.getAbsolutePath(), baseDir));
            final ZipInputStream is = new ZipInputStream(new FileInputStream(archive));
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
            getLog().info(String.format("Extracting tar.bz2/tar.gz file <%s> to directory <%s>.", archive.getAbsolutePath(), baseDir));

            // unzip to tar
            FileInputStream in = new FileInputStream(archive);
            File tarfile = new File(baseDir, "archive.tar");
            tarfile.delete();
            FileOutputStream out = new FileOutputStream(tarfile);
            BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
            final byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = bzIn.read(buffer))) {
                out.write(buffer, 0, n);
            }
            out.close();
            bzIn.close();
            // untar
            final TarArchiveInputStream is = new TarArchiveInputStream(new FileInputStream(tarfile));
            TarArchiveEntry entry;
            while ((entry = is.getNextTarEntry()) != null) {
                if (!entry.isDirectory()) {
                    final File file = new File(baseDir, entry.getName());
                    Files.createParentDirs(file);
                    Files.write(ByteStreams.toByteArray(is), file);
                }
            }
            is.close();
            tarfile.delete();
        } else {
            throw new UnsupportedOperationException("Not supported file format " + archive.getName());
        }
        getLog().info(String.format("Extracting of <%s> completed.", archive.getName()));

    }

    public List<String> getAllActivatedProfiles() {
        try {
            Method getInjectedProfileIdsMethod = MavenProject.class.getMethod("getInjectedProfileIds");
            Map<String, List<String>> profilesMap = (Map<String, List<String>>) getInjectedProfileIdsMethod.invoke(mojo.getProject());
            Set<String> result = new HashSet<String>();
            for (List<String> listOfProfiles : profilesMap.values()) {
                for (String profile : listOfProfiles) {
                    result.add(profile.toLowerCase());
                }
            }
            return new TolerantContainsList(result);
        } catch (Exception ex) {
            getLog().error(ex);
        }
        return Collections.EMPTY_LIST;
    }

    public Browser getBrowser() {
        String browserFromSystemProperty = System.getProperty("browser");
        if (browserFromSystemProperty != null && !browserFromSystemProperty.isEmpty()) {
            return Browser.parseFromString(browserFromSystemProperty);
        }
        throw new IllegalStateException("No browser specified! You can use any of these: -Dbrowser=firefox , -Dbrowser=chrome , -Dbrowser=ie");
    }

    public Command getCommands() {
        return isOnJenkins() ? new JenkinsCommands(this) : new LocalCommands(this);
    }

    public Log getLog() {
        return getMojo().getLog();
    }

    public ProcessMojo getMojo() {
        return mojo;
    }

    public String getOSName() {
        return System.getProperty("os.name");
    }

    public String getOsArch() {
        return System.getProperty("os.arch");
    }

    public String getUserName() {
        try {
            Process exec = Runtime.getRuntime().exec("whoami");
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String user = reader.readLine();
            reader.close();
            int indexOf = user.indexOf("\\");// on windows the output equals to computer-name\\username
            return user.substring(indexOf > 0 ? indexOf : 0);
        } catch (IOException ex) {
            getLog().error(ex);
            throw new RuntimeException(ex);
        }
    }

    public boolean is64bitArch() {
        return getOsArch().contains("64");
    }

    public boolean isEAPProfileActivated() {
        return getAllActivatedProfiles().contains("eap");
    }

    public boolean isGlassFishProfileActivated() {
        return getAllActivatedProfiles().contains("glassfish");
    }

    public boolean isJBossASProfileActivated() {
        return getAllActivatedProfiles().contains("jboss") || isEAPProfileActivated() || getAllActivatedProfiles().contains("as");
    }

    public boolean isOnJenkins() {
        return getUserName().toLowerCase().contains("hudson") || HUDSON_LINUX_DIRECTORY.exists() || HUDSON_WINDOWS_DIRECTORY.exists();
    }

    public boolean isOnLinux() {
        return getOSName().toLowerCase().contains("linux");
    }

    public boolean isOnSolaris() {
        return getOSName().toLowerCase().contains("sunos");
    }

    public boolean isOnWindows() {
        return getOSName().toLowerCase().contains("win");
    }

    public boolean isRemoteProfileActivated() {
        return getAllActivatedProfiles().contains("remote");
    }

    public boolean isTomcatProfileActivated() {
        return getAllActivatedProfiles().contains("tomcat");
    }

    public void setProjectProperty(String name, Object value) {
        getLog().info(String.format("Setting project property <%s> to value <%s>.", name, value));
        getMojo().getProject().getProperties().put(name, value.toString());
    }

    public void setSystemProperty(String name, Object value) {
        getLog().info(String.format("Setting system property <%s> to value <%s>.", name, value));
        System.getProperties().put(name, value.toString());
    }
}
