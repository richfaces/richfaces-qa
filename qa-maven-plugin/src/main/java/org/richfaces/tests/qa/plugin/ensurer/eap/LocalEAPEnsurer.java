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
package org.richfaces.tests.qa.plugin.ensurer.eap;

import java.io.File;
import java.io.IOException;

import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;
import org.richfaces.tests.qa.plugin.properties.eap.EAPProperties;
import org.richfaces.tests.qa.plugin.utils.Servant;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LocalEAPEnsurer implements EAPEnsurer {

    private static final String EAP_ZIP_FILENAME = "eap.zip";
    private static final String USER_EAP_DIRECTORY = "selenium/eap";

    private final EAPProperties eapProperties;
    private final PropertiesProvider pp;
    private final Servant servant;
    private final File userEapDirectory;

    @Inject
    public LocalEAPEnsurer(EAPProperties eapProperties, PropertiesProvider pp, Servant servant) {
        this.eapProperties = eapProperties;
        this.pp = pp;
        this.servant = servant;
        this.userEapDirectory = new File(pp.getUserHome(), USER_EAP_DIRECTORY);
    }

    /**
     * Download EAP if not downloaded and return the reference to the zip file.
     */
    protected File downloadEAP() {
        File currentEAPDir = new File(userEapDirectory, eapProperties.getVersion().getMajorMinorMicroSpecifierFormat());
        if (!currentEAPDir.exists()) {
            currentEAPDir.mkdirs();
        }
        File currentEAPZip = new File(currentEAPDir, EAP_ZIP_FILENAME);
        servant.downloadFile(eapProperties.getUrlToEapZip(), currentEAPZip);
        return currentEAPZip;
    }

    @Override
    public void ensure() {
        try {
            ensureEAP();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void ensureEAP() throws IOException {
        File targetDir = new File(pp.getProjectBuildDirectory());
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        servant.extract(targetDir, downloadEAP());
        File extractedEAPDir = new File(targetDir, eapProperties.getEapExtractedDirectoryName());
        servant.setProjectProperty(pp.getEapHomePropertyName(), extractedEAPDir.getAbsolutePath());
    }

}
