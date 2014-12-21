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
package org.richfaces.tests.qa.plugin.properties.eap;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;

import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.utils.Utils;
import org.richfaces.tests.qa.plugin.utils.Version;
import org.richfaces.tests.qa.plugin.utils.cache.LazyLoadedCachedValue;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Singleton
public class SimpleEAPProperties implements EAPProperties {

    private static final String JBOSSEAP = "jboss-eap-";
    private static final String hudsonStaticOSx = "/Users/hudson/static_build_env/";
    private static final String hudsonStaticUnix = "/home/hudson/static_build_env/";
    private static final String hudsonStaticWin = "h:/hudson/static_build_env/";
    private static final String urlPart1Candidates = "http://download.englab.brq.redhat.com/devel/candidates/JBEAP";
    private static final String urlPart1Released = "http://download.englab.brq.redhat.com/released/JBEAP-6";

    private final LazyLoadedCachedValue<File> cachedJenkinsEapZipFile = new LazyLoadedCachedValue<File>() {
        @Override
        protected File initValue() {
            return new File(MessageFormat.format("{0}/eap/{1}/{2}.zip", pp.isOnWindows() ? hudsonStaticWin : pp.isOnMac() ? hudsonStaticOSx : hudsonStaticUnix,
                getVersion().getMajorMinorMicroSpecifierFormat(), getEAPZipName()));
        }
    };
    private final LazyLoadedCachedValue<URL> cachedURLToEapZip = new LazyLoadedCachedValue<URL>() {
        @Override
        protected URL initValue() {
            return Utils.createURLSilently(MessageFormat.format("{0}/{1}/{2}.zip", getURLPart1(), getURLPart2(), getURLPart3()));
        }
    };
    private final PropertiesProvider pp;

    @Inject
    public SimpleEAPProperties(PropertiesProvider pp) {
        this.pp = pp;
    }

    protected String getEAPZipName() {
        // version 6.x.y, if x > 1 && y > 0  => *-full-build.zip
        return getVersion().getMinor() > 1 && getVersion().getMicro() > 0 ? getVersion().getFullFormat() + "-full-build" : getVersion().getFullFormat();
    }

    @Override
    public String getEapExtractedDirectoryName() {
        return JBOSSEAP + getVersion().getMajorMinorFormat();
    }

    @Override
    public File getJenkinsEapZipFile() {
        return cachedJenkinsEapZipFile.getValue();
    }

    protected String getURLPart1() {
        return isInReleasedRepository() ? urlPart1Released : urlPart1Candidates;
    }

    protected String getURLPart2() {
        return isInReleasedRepository() ? getVersion().getMajorMinorMicroFormat() : "JBEAP-" + getVersion().getMajorMinorMicroSpecifierFormat();
    }

    protected String getURLPart3() {
        return getEAPZipName();
    }

    @Override
    public URL getUrlToEapZip() {
        return cachedURLToEapZip.getValue();
    }

    @Override
    public Version getVersion() {
        return pp.getEapVersion();
    }

    public boolean isInReleasedRepository() {
        return getVersion().getSpecifier().isEmpty();
    }
}
