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
package org.richfaces.tests.metamer.ftest;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.MessageFormat;
import java.util.List;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandLineException;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.importer.ArchiveImportException;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.javaee6.ParamValueType;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.utils.files.LineIdentifiers;
import org.richfaces.tests.metamer.ftest.utils.files.SimpleFileManipulator;

import com.google.common.io.Files;

/**
 * Abstract test case used as a basis for majority of test cases.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22749 $
 */
@RunAsClient
@ArquillianSuiteDeployment
public abstract class AbstractMetamerTest extends Arquillian {

    private static final String EAP_63_AND_UP_REGEX = ".*jbosseap-(managed|remote)-(6-[3-9]).*";
    private static final String EAP_70_AND_UP_REGEX = ".*jbosseap-(managed|remote)-(7-[0-9]).*";
    // Key to enable WS on EAP
    public static final String EAP_WS_ENABLED = "eap.ws.enabled";
    /**
     * Keys to manage resources optimization (in previous releases named mapping), compression and packaging, used in web.xml
     */
    public static final String RESOURCE_OPTIMIZATION_COMPRESSION_STAGES = "org.richfaces.resourceOptimization.compressionStages";
    public static final String RESOURCE_OPTIMIZATION_ENABLED = "org.richfaces.resourceOptimization.enabled";
    public static final String RESOURCE_OPTIMIZATION_PACKAGING_STAGES = "org.richfaces.resourceOptimization.packagingStages";
    public static final String RESOURCE_OPTIMIZATION_PARAM_ALL = "All";
    public static final String RESOURCE_OPTIMIZATION_PARAM_NONE = "None";

    private static final String activatedMavenProfiles = System.getProperty("activated.maven.profiles", "");
    private static WebArchive deployedWar;
    protected static final Boolean runInPortalEnv = Boolean.getBoolean("runInPortalEnv");

    @ArquillianResource
    protected URL contextPath;
    @Templates(value = { "plain", "richAccordion", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richCollapsiblePanel", "richTabPanel", "richPopupPanel", "a4jRegion", "a4jRepeat", "uiRepeat" })
    protected TemplatesList template;

    private static void checkValueIsValidForResourceOptimizationParam(String value) {
        assertNotNull(value, "The parameter for resource optimization can only be <None> or <All>, not null!");
        assertTrue(value.equals(RESOURCE_OPTIMIZATION_PARAM_NONE) || value.equals(RESOURCE_OPTIMIZATION_PARAM_ALL),
            MessageFormat.format("The parameter for resource optimization can only be <{0}> or <{1}>. Now it is set to <{2}>.",
                RESOURCE_OPTIMIZATION_PARAM_ALL, RESOURCE_OPTIMIZATION_PARAM_NONE, value));
    }

    private static File copyJBossWebXMLToTarget(WebArchive war) throws IOException {
        File file = new File("target/jboss-web.xml");
        FileChannel outChannel = new FileOutputStream(file).getChannel();
        ReadableByteChannel fromChannel = Channels.newChannel(war.get(ArchivePaths.create("WEB-INF/jboss-web.xml")).getAsset().openStream());
        outChannel.transferFrom(fromChannel, 0, Integer.MAX_VALUE);
        return file;
    }

    @Deployment(testable = false, name = "updated")
    @OverProtocol("Servlet 3.0")
    public static WebArchive createTestArchive() throws IOException, URISyntaxException {
        WebArchive war = createWarFromZipFile();
        /*
         * If value on system property "org.richfaces.resourceOptimization.enabled" is set to true, modify context-params in
         * web.xml. For more info see https://issues.jboss.org/browse/RFPL-1682
         */
        if (Boolean.getBoolean(RESOURCE_OPTIMIZATION_ENABLED)) {
            enableResourceOptimization(war);
        }

        // advanced features, tested only with browser profile
        if (isUsingBrowserProfile()) {
            File temporaryJBossWebXML = copyJBossWebXMLToTarget(war);
            if (isUsingEAP()) {
                // workaround xml parse error of 'default encoding' in jboss-web.xml in EAP version lesser than 7
                if (!isUsingEAP70AndUp()) {
                    removeDefaultEncodingFromJbossWebXML(war, temporaryJBossWebXML);
                }
                // workaround to enable running commands through JBoss CLI in EAP 6.3 or 6.4
                workaroundCLIVersionInEAP63_or_64();
            }

            // undeploy all metamer WARs if using a JBoss container
            if (isUsingJBossContainer()) {
                try {
                    undeployMetamerWars();
                } catch (CommandLineException ignored) {// no metamer war was deployed
                }

                if (isUsingEAP63_or_64()) {
                    // enable WS in EAP 6.3 or 6.4
                    if (Boolean.getBoolean(EAP_WS_ENABLED)) {
                        try {
                            System.out.println("### Enabling WebSockets in EAP ###");
                            enableWebSocketsInEAP63_or_64(war, temporaryJBossWebXML);
                            System.out.println("### Enabling of WebSockets in EAP was successful ###");
                        } catch (CommandLineException t) {
                            t.printStackTrace(System.err);
                            System.out.println("### Enabling of WebSockets in EAP was NOT successful ###");
                        }
                    }
                }
            }
            temporaryJBossWebXML.deleteOnExit();
        }
        // save actual war to target/metamer-UPDATED.war
        File updatedWar = new File("target/metamer-UPDATED.war");
        war.as(ZipExporter.class).exportTo(updatedWar);
        deployedWar = war;

        return war;
    }

    private static WebArchive createWarFromZipFile() throws IOException, IllegalArgumentException, ArchiveImportException {
        return ShrinkWrap.createFromZipFile(WebArchive.class, runInPortalEnv ? new File("target/metamer-portlet.war")
            : new File("target/metamer.war"));
    }

    /**
     * Deploys target/metamer-UPDATED.war file on current JBoss container.
     */
    public static void deployMetamerWar() throws CommandLineException {
        if (isUsingJBossContainer()) {
            runCLICommandWithWait("deploy target/metamer-UPDATED.war");
        } else {
            System.err.println("Not using any JBoss container. War cannot be deployed.");
        }
    }

    /**
     * Sets javax.faces.PARTIAL_STATE_SAVING to false in web.xml and redeploys the current war. Can only be used with JBoss
     * containers.
     */
    public static void disablePartialStateSavingAndRedeploy() {
        modifyAndDeployWar(new WARModifyAction() {
            @Override
            public WebArchive modify(WebArchive war) {
                WebArchive newWar = (WebArchive) war.shallowCopy();
                // 1. load existing web.xml from WebArchive
                WebAppDescriptor webXML = Descriptors.importAs(WebAppDescriptor.class).fromStream(
                    newWar.get("WEB-INF/web.xml").getAsset().openStream());
                webXML.getOrCreateContextParam().paramName("javax.faces.PARTIAL_STATE_SAVING").paramValue("false");
                // 3. save the params to web.xml
                newWar.setWebXML(new StringAsset(webXML.exportAsString()));
                return newWar;
            }
        });
    }

    /*
     * This will update web.xml with resource optimization values.
     */
    private static void enableResourceOptimization(WebArchive war) {
        System.out.println("### Enabling resource optimization ###");

        String compressedParam = System.getProperty(RESOURCE_OPTIMIZATION_COMPRESSION_STAGES, RESOURCE_OPTIMIZATION_PARAM_NONE);
        checkValueIsValidForResourceOptimizationParam(compressedParam);
        String packedParam = System.getProperty(RESOURCE_OPTIMIZATION_PACKAGING_STAGES, RESOURCE_OPTIMIZATION_PARAM_NONE);
        checkValueIsValidForResourceOptimizationParam(packedParam);

        System.out.println(MessageFormat.format("    {0}  = {1}", RESOURCE_OPTIMIZATION_COMPRESSION_STAGES, compressedParam));
        System.out.println(MessageFormat.format("    {0}  = {1}", RESOURCE_OPTIMIZATION_PACKAGING_STAGES, packedParam));

        // 1. load existing web.xml from metamer.war
        WebAppDescriptor webXmlDefault = Descriptors.importAs(WebAppDescriptor.class).fromStream(
            war.get("WEB-INF/web.xml").getAsset().openStream());
        List<ParamValueType<WebAppDescriptor>> allContextParams = webXmlDefault.getAllContextParam();
        // 2. Iterate over all context params and alter the particular ones
        for (ParamValueType<WebAppDescriptor> param : allContextParams) {
            String paramName = param.getParamName();
            if (paramName.equals(RESOURCE_OPTIMIZATION_ENABLED)) {
                param.paramValue("true");
            } else if (paramName.equals(RESOURCE_OPTIMIZATION_COMPRESSION_STAGES)) {
                param.paramValue(compressedParam);
            } else if (paramName.equals(RESOURCE_OPTIMIZATION_PACKAGING_STAGES)) {
                param.paramValue(packedParam);
            }
        }
        // 3. save the params to web.xml
        war.setWebXML(new StringAsset(webXmlDefault.exportAsString()));
    }

    /**
     * Runs a command through JBoss CLI for enabling WebSockets and restarting the server.
     */
    private static void enableWSInJBossCLI() throws CommandLineException {
        runCLICommandWithWait(
            "/subsystem=web/connector=http/:write-attribute(name=protocol,value=org.apache.coyote.http11.Http11NioProtocol)",
            ":reload");
    }

    private static File enableWSInJbossWebXML(File jbossWebXMLFile) {
        SimpleFileManipulator.inFile(jbossWebXMLFile)
            .appendLine("<enable-websockets>true</enable-websockets>")
            .beforeLine(LineIdentifiers.lineContains("</jboss-web>"))
            .perform();
        return jbossWebXMLFile;
    }

    private static void enableWebSocketsInEAP63_or_64(WebArchive war, File jBossWebXML) throws CommandLineException {
        System.out.println(" * adding  <enable-websockets>true</enable-websockets> to jboss-web.xml");
        replaceJBossWebXMLInWar(war, enableWSInJbossWebXML(jBossWebXML));
        enableWSInJBossCLI();
    }

    private static boolean isUsingBrowserProfile() {
        return activatedMavenProfiles.contains("browser");
    }

    private static boolean isUsingEAP() {
        return activatedMavenProfiles.contains("jbosseap");
    }

    private static boolean isUsingEAP63_or_64() {
        return activatedMavenProfiles.matches(EAP_63_AND_UP_REGEX);
    }

    private static boolean isUsingEAP70AndUp() {
        return activatedMavenProfiles.matches(EAP_70_AND_UP_REGEX);
    }

    private static boolean isUsingJBossContainer() {
        return isUsingEAP() || isUsingWildFly();
    }

    private static boolean isUsingWildFly() {
        return activatedMavenProfiles.contains("wildfly");
    }

    /**
     * Creates a copy of target/metamer-UPDATED.war, updates it according to WARModifyAction and saves it to
     * target/metamerModfied.war. Undeploys all *metamer* wars, deploys the modified one and deletes it in the target directory.
     * Can be used only with JBoss containers,
     */
    public static void modifyAndDeployWar(WARModifyAction a) {
        if (isUsingJBossContainer()) {
            try {
                undeployMetamerWars();
            } catch (CommandLineException t) {
                System.err.println(t);
                System.err.println("Undeployment of war was not successful. Exiting.");
                System.exit(1);
            }
            WebArchive modifiedWar = a.modify(deployedWar);
            File modifiedWarFile = new File("target/metamerModified.war");
            modifiedWarFile.delete();
            modifiedWar.as(ZipExporter.class).exportTo(modifiedWarFile);
            try {
                runCLICommandWithWait("deploy target/metamerModified.war");
            } catch (CommandLineException t) {
                System.err.println(t);
                System.err.println("Modification of war and redeployment was not successful. Exiting.");
                System.exit(1);
            }
            modifiedWarFile.delete();
        } else {
            System.err.println("Not using any JBoss container. War will not be modified.");
        }
    }

    /**
     * If not removed and using EAP, you get: <code>
     * Caused by: java.lang.Exception: {"JBAS014671: Failed services" => {"jboss.deployment.unit.\"metamer.war\".PARSE" => "org.jboss.msc.service.StartException in service jboss.deployment.unit.\"metamer.war\".PARSE: JBAS018733: Failed to process phase PARSE of deployment \"metamer.war\"
     * Caused by: org.jboss.as.server.deployment.DeploymentUnitProcessingException: JBAS018014: Failed to parse XML descriptor \"/content/metamer.war/WEB-INF/jboss-web.xml\" at [4,3]
     * Caused by: javax.xml.stream.XMLStreamException: ParseError at [row,col]:[4,3]
     * Message: Unexpected element 'default-encoding' encountered"}}</code>
     */
    private static void removeDefaultEncodingFromJbossWebXML(WebArchive war, File jBossWebXML) {
        System.out.println(" * removing line containing <default-encoding> from jboss-web.xml");
        replaceJBossWebXMLInWar(war, removeDefaultEncodingInJbossWebXML(jBossWebXML));
    }

    private static File removeDefaultEncodingInJbossWebXML(File jbossWebXMLFile) {
        SimpleFileManipulator.inFile(jbossWebXMLFile)
            .deleteLine(LineIdentifiers.lineContains("<default-encoding>"))
            .perform();
        return jbossWebXMLFile;
    }

    /**
     * Replaces jboss-web.xml in the final WAR with a version with enabled WebSockets.
     */
    private static WebArchive replaceJBossWebXMLInWar(WebArchive war, File jbossWebXML) throws IllegalArgumentException {
        war.delete(ArchivePaths.create("WEB-INF/jboss-web.xml"));
        war.addAsWebInfResource(jbossWebXML);
        return war;
    }

    /**
     * Runs a command through JBoss CLI.
     */
    private static void runCLICommand(String... commands) throws CommandLineException {
        if (commands == null || commands.length == 0) {
            return;
        }
        final CommandContext ctx;
        ctx = CommandContextFactory.getInstance().newCommandContext();
        int MAX_RETRY_COUNT = 20;
        try {
            String cmd;
            int i = 0;
            int retryCount = 0;
            while (i < commands.length) {
                cmd = commands[i];
                try {
                    // connect to the server controller
                    ctx.connectController();
                    // execute the command
                    ctx.handle(cmd);
                    retryCount = 0;
                    i++;
                } catch (CommandLineException e) {
                    if (e.getMessage().contains("The controller is not available")) {
                        retryCount++;
                        if (retryCount >= MAX_RETRY_COUNT) {
                            throw e;
                        }
                        // wait and repeat
                        waiting(1000);
                    } else {
                        throw e;
                    }
                }
            }
        } finally {
            // terminate the session and
            // close the connection to the controller
            ctx.terminateSession();
        }
    }

    /**
     * Runs all CLI commands and then runs an empty command to wait until the container is ready again
     */
    protected static void runCLICommandWithWait(String... commands) throws CommandLineException {
        runCLICommand(commands);
        runCLICommand("");// wait for EAP to be ready, e.g. after restart
    }

    /**
     * Undeploys all metamer wars from current JBoss container.
     */
    public static void undeployMetamerWars() throws CommandLineException {
        if (isUsingJBossContainer()) {
            runCLICommandWithWait("undeploy *metamer*");
        } else {
            System.err.println("Not using any JBoss container. War cannot be undeployed.");
        }
    }

    private static void waiting(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Workaround works only for container placed in project's target directory. Workaround the exception during parsing the
     * jboss-cli.xml. Change the urn:jboss:cli:1.3 to *1.2
     */
    private static void workaroundCLIVersionInEAP63_or_64() throws URISyntaxException, IOException {
        File[] jbossContainersDirs = new File(System.getProperty("project.build.directory")).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String lowerCaseFileName = file.getName().toLowerCase();
                return file.isDirectory() && (lowerCaseFileName.startsWith("jboss-eap-6.3") || lowerCaseFileName.startsWith("jboss-eap-6.4"));
            }
        });
        for (File containerDir : jbossContainersDirs) {
            File jbossCliFile = new File(containerDir, "bin/jboss-cli.xml");
            File workaroundedJBossCliFile = new File(AbstractMetamerTest.class
                .getResource("eap/jboss-cli.xml").toURI());
            jbossCliFile.delete();

            Files.copy(workaroundedJBossCliFile, jbossCliFile);
        }
    }

    /**
     * Returns the url to test page to be opened by Selenium
     *
     * @return absolute url to the test page to be opened by Selenium
     */
    public abstract URL
        getTestUrl();

    public interface WARModifyAction {

        WebArchive modify(WebArchive war);
    }
}
