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

import static java.text.MessageFormat.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.spi.event.container.BeforeStart;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.as.cli.CommandLineException;

import com.google.common.io.Files;

/**
 * Installs patch from System property <code>pathToPatch</code> to JBoss container using the jboss-cli.sh script.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class JBossPatchInstaller {

    private static final String ACTIVATED_MAVEN_PROFILES = System.getProperty("activated.maven.profiles", "");
    private static final String JBOSS_HOME_SYSTEM_PROPERTY = "JBOSS_HOME";
    private static final String OS_NAME = System.getProperty("os.name");
    private static final String PATH_TO_JBOSS_HOME = System.getProperty(JBOSS_HOME_SYSTEM_PROPERTY);
    private static final String PATH_TO_JBOSS_PATCH_SYSTEM_PROPERTY = "pathToPatch";
    private static final String SUCCESS_STRING = "success";

    private static String formatOutput(List<String> outputFromProcess) {
        StringBuilder sb = new StringBuilder(100);
        boolean performed = false;
        for (String o : outputFromProcess) {
            if (performed) {
                sb.append("\n");
            }
            sb.append(" * ").append(o);
            performed = true;
        }
        return sb.toString();
    }

    private static boolean isUsingJBossContainer() {
        return ACTIVATED_MAVEN_PROFILES.contains("jbosseap") || ACTIVATED_MAVEN_PROFILES.contains("wildfly");
    }

    /**
     * Apply patch from given path.
     */
    private void applyPatch(String pathToPatch) throws CommandLineException {
        if (PATH_TO_JBOSS_HOME == null || PATH_TO_JBOSS_HOME.isEmpty()) {
            logError("JBOSS_HOME not detected. Exiting.");
            System.exit(1);
        }
        logInfoMarked(format("Applying patch from path <{0}>", pathToPatch));
        try {
            File f = new File(PATH_TO_JBOSS_HOME + "/bin/jboss-cli" + (isOnWindows() ? ".bat" : ".sh"));
            f.setExecutable(true);
            File patch = new File(pathToPatch);
            String patchName = patch.getName();
            File patchCopy = new File("target/" + patchName);
            logInfo(format("Copying patch <{0}> to <{1}> for easier manipulation.", patch.getAbsolutePath(), patchCopy.getAbsolutePath()));
            Files.copy(patch, patchCopy);

            String cmd = f.getAbsolutePath() + " --command=\"patch apply " + patchCopy.getAbsolutePath() + "\"";
            String[] cmdArray;
            if (isOnWindows()) {
                // the cmd.exe is hanging with 'Press any key to continue ...'
                // give it some input from empty temporary file to workaround this
                File tmp = new File("tempFile");
                tmp.createNewFile();
                tmp.deleteOnExit();
                cmdArray = new String[] { "cmd.exe", "/C", cmd + " < " + tmp.getAbsolutePath() };
            } else {
                cmdArray = new String[] { "/bin/sh", "-c", cmd };
            }
            // apply patch by running jboss-cli.sh with parameters
            Process p = Runtime.getRuntime().exec(cmdArray);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // check stdin contains success
            String s;
            boolean success = false;
            List<String> outputFromProcess = new ArrayList<String>(10);
            while ((s = stdInput.readLine()) != null) {
                outputFromProcess.add(s);
                if (s.toLowerCase().contains(SUCCESS_STRING)) {
                    success = true;// do not break here, consume all the input
                }
            }
            stdInput.close();
            p.destroy();
            // check stdin contained success, otherwise immediatelly exit
            if (!success) {
                logError(format("Attempt to apply a patch was not succesfull. Apllying patch ended with output:\n{0}",
                    formatOutput(outputFromProcess)));
                logError("Exiting.");
                System.exit(1);
            }
        } catch (IOException ex) {
            logError(ex.toString());
            logError("Attempt to apply a patch was not succesfull. Exiting.");
            System.exit(1);
        }
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
                } catch (CommandLineException e) {
                    logError(e.toString());
                    logError("Was not able to apply the patch. Exiting.");
                    System.exit(1);
                }
            } else {
                logError(format("The patch at <{0}> does not exist. Exiting.", pathToPatch));
                System.exit(1);
            }
        }
    }

    private boolean isOnWindows() {
        return OS_NAME.toLowerCase().contains("win");
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
