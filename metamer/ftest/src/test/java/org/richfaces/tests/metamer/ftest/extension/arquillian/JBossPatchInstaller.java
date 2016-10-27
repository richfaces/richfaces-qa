/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.extension.arquillian;

import org.jboss.arquillian.container.spi.event.container.BeforeStart;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.richfaces.tests.metamer.ftest.extension.creaper.ApplyPatch;
import org.richfaces.tests.metamer.ftest.utils.ManagementClientProvider;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.offline.OfflineManagementClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.text.MessageFormat.format;

/**
 * Installs patch from System property <code>pathToPatch</code> to JBoss container using the jboss-cli.sh script.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class JBossPatchInstaller {

    private static final String ACTIVATED_MAVEN_PROFILES = System.getProperty("activated.maven.profiles", "");
    private static final String PATH_TO_JBOSS_PATCH_SYSTEM_PROPERTY = "pathToPatch";

    private static boolean isUsingJBossContainer() {
        return ACTIVATED_MAVEN_PROFILES.contains("jbosseap") || ACTIVATED_MAVEN_PROFILES.contains("wildfly");
    }

    /**
     * Apply patch from given path.
     * @throws IOException when there is an error during closing of client
     * @throws CommandFailedException when the apply patch command fails
     */
    private void applyPatch(String pathToPatch) throws IOException, CommandFailedException {
        logInfoMarked(format("Applying patch from path <{0}>", pathToPatch));
        File patch = new File(pathToPatch);

        OfflineManagementClient client = ManagementClientProvider.createOfflineManagementClientForStandaloneServer();

        logInfo("Applying patch " + patch.getName());
        client.apply(new ApplyPatch.Builder(patch).build());

        logInfoMarked("Patch applied succesfully");
    }

    public void applyPatchWhenNeeded(@Observes BeforeStart event) {
        String pathToPatch = System.getProperty(PATH_TO_JBOSS_PATCH_SYSTEM_PROPERTY);
        if (!isUsingJBossContainer() || pathToPatch == null || pathToPatch.isEmpty()) {
            logInfoMarked("No patch to apply.");
        } else {
            if (new File(pathToPatch).exists()) {
                try {
                    applyPatch(pathToPatch);
                } catch (Exception e) {
                    systemExitWithException("Attempt to apply a patch was not succesfull. Exiting.", e);
                }
            } else {
                String message = format("The patch at <{0}> does not exist. Exiting.", pathToPatch);
                systemExitWithException(message, new FileNotFoundException(message));
            }
        }
    }

    private void systemExitWithException(String message, Exception e) {
        logError(message);
        e.printStackTrace();
        System.exit(1);
    }

    private void logError(String msg) {
        System.err.println(msg);
    }

    private void logInfo(String msg) {
        System.out.println(format(" * {0}", msg));
    }

    private void logInfoMarked(String msg) {
        System.out.println(format("### {0} ###", msg));
    }
}
