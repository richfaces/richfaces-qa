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
package org.richfaces.tests.metamer.ftest;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandLineException;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ArchiveImportException;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.javaee6.ParamValueType;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;

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

    // Key to enable WS on EAP
    public static final String EAP_WS_ENABLED = "eap.ws.enabled";
    /** Keys to manage resources optimization (in previous releases named mapping), compression and packaging, used in web.xml */
    public static final String RESOURCE_OPTIMIZATION_COMPRESSION_STAGES = "org.richfaces.resourceOptimization.compressionStages";
    public static final String RESOURCE_OPTIMIZATION_ENABLED = "org.richfaces.resourceOptimization.enabled";
    public static final String RESOURCE_OPTIMIZATION_PACKAGING_STAGES = "org.richfaces.resourceOptimization.packagingStages";
    public static final String RESOURCE_OPTIMIZATION_PARAM_ALL = "All";
    public static final String RESOURCE_OPTIMIZATION_PARAM_NONE = "None";

    private static final String activatedMavenProfiles = System.getProperty("activated.maven.profiles", "");
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

    @Deployment(testable = false, name = "updated")
    @OverProtocol("Servlet 3.0")
    public static WebArchive createTestArchive() throws IOException, URISyntaxException {
        WebArchive war = createWarFromZipFile();
        /*
         * If value on system property "org.richfaces.resourceOptimization.enabled" is set to true, modify context-params in web.xml.
         * For more info see https://issues.jboss.org/browse/RFPL-1682
         */
        if (Boolean.getBoolean(RESOURCE_OPTIMIZATION_ENABLED)) {
            enableResourceOptimization(war);
        }

        if (isUsingEAP()) {
            workaroundCLIVersionInEAP63And64();
        }

        // undeploy all metamer WARs if using a JBoss container
        if (isUsingJBossContainer()) {
            runCLICommand("undeploy *metamer*");
        }

        if (isUsingEAP63()) {
            if (Boolean.getBoolean(EAP_WS_ENABLED)) {
                try {
                    System.out.println("### Enabling WebSockets in EAP ###");
                    enableWebSocketsInEAP63(war);
                    System.out.println("### Enabling of WebSockets in EAP was successful ###");
                } catch (Throwable t) {
                    t.printStackTrace(System.err);
                    System.out.println("### Enabling of WebSockets in EAP was NOT successful ###");
                }
            }
        }
        return war;
    }

    private static WebArchive createWarFromZipFile() throws IOException, IllegalArgumentException, ArchiveImportException {
        return ShrinkWrap.createFromZipFile(WebArchive.class, runInPortalEnv ? new File("target/metamer-portlet.war")
            : new File("target/metamer.war"));
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

    private static void enableWSInJBossCLI() throws IllegalStateException {
        runCLICommand(
            "/subsystem=web/connector=http/:write-attribute(name=protocol,value=org.apache.coyote.http11.Http11NioProtocol)",
            ":reload");
    }

    private static void enableWebSocketsInEAP63(WebArchive war) {
        modifyJBossWebXMLToUseWS(war);
        enableWSInJBossCLI();
    }

    private static boolean isUsingEAP() {
        return activatedMavenProfiles.contains("jbosseap");
    }

    private static boolean isUsingEAP63() {
        return isUsingEAP() && activatedMavenProfiles.contains("-6-3");
    }

    private static boolean isUsingJBossContainer() {
        return isUsingEAP() || isUsingWildFly();
    }

    private static boolean isUsingWildFly() {
        return activatedMavenProfiles.contains("wildfly");
    }

    private static void modifyJBossWebXMLToUseWS(WebArchive war) throws IllegalArgumentException {
        File file = null;
        try {
            file = new File(AbstractMetamerTest.class.getResource("eap/jboss-web.xml").toURI());
        } catch (URISyntaxException ex) {
        }
        war.delete(ArchivePaths.create("WEB-INF/jboss-web.xml"));
        war.addAsWebInfResource(file);
    }

    private static void runCLICommand(String... commands) throws IllegalStateException {
        final CommandContext ctx;
        try {
            ctx = CommandContextFactory.getInstance().newCommandContext();
        } catch (CliInitializationException e) {
            throw new IllegalStateException("Failed to initialize CLI context", e);
        }
        try {
            // connect to the server controller
            ctx.connectController();
            // execute commands and operations
            for (String cmd : commands) {
                ctx.handle(cmd);
            }
        } catch (CommandLineException e) {
            System.err.println(e);
            // the operation or the command has failed
        } finally {
            // terminate the session and
            // close the connection to the controller
            ctx.terminateSession();
        }
    }

    /**
     * Workaround the exception during parsing the jboss-cli.xml. Change the urn:jboss:cli:1.3 to *1.2
     */
    private static void workaroundCLIVersionInEAP63And64() throws URISyntaxException, IOException {
        File[] jbossContainersDirs = new File(System.getProperty("project.build.directory")).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String lowerCaseFileName = file.getName().toLowerCase();
                return file.isDirectory() && (lowerCaseFileName.startsWith("jboss-eap-6.3") || lowerCaseFileName.startsWith("jboss-eap-6.4"));
            }
        });
        for (File containerDir : jbossContainersDirs) {
            File jbossCliFile = new File(containerDir, "bin/jboss-cli.xml");
            File workaroundedJBossCliFile = new File(AbstractMetamerTest.class.getResource("eap/jboss-cli.xml").toURI());
            jbossCliFile.delete();
            Files.copy(workaroundedJBossCliFile, jbossCliFile);
        }
    }

    /**
     * Returns the url to test page to be opened by Selenium
     *
     * @return absolute url to the test page to be opened by Selenium
     */
    public abstract URL getTestUrl();
}
