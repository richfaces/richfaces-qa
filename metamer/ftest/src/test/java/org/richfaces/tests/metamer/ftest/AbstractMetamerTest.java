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

import java.io.File;
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
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ArchiveImportException;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
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

    private static final String PARAM_ALL = "All";
    private static final String PARAM_NONE = "None";
    /** Key to manage compressedStages context-param in web.xml */
    public static final String RESOURCE_MAPPING_COMPRESSED_STAGES = "org.richfaces.resourceMapping.compressedStages";
    /** Key to manage resourceMapping enabling context-param in web.xml */
    public static final String RESOURCE_MAPPING_ENABLED = "org.richfaces.resourceMapping.enabled";
    /** Key to manage packedStages context-param in web.xml */
    public static final String RESOURCE_MAPPING_PACKED_STAGES = "org.richfaces.resourceMapping.packedStages";
    private static final String activatedMavenProfiles = System.getProperty("activated.maven.profiles", "");
    protected static final Boolean runInPortalEnv = Boolean.getBoolean("runInPortalEnv");

    @ArquillianResource
    protected URL contextPath;

    @Templates({ "plain", "richAccordion", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richCollapsiblePanel", "richTabPanel", "richPopupPanel", "a4jRegion", "a4jRepeat", "uiRepeat" })
    protected TemplatesList template;

    @Deployment(testable = false)
    @OverProtocol("Servlet 3.0")
    public static WebArchive createTestArchive() throws IOException, URISyntaxException {
        WebArchive war = createWarFromZipFile();
        /*
         * If value on system property "org.richfaces.resourceMapping.enabled" is set to true, modify context-params in web.xml.
         * For more info see https://issues.jboss.org/browse/RFPL-1682
         */
        if (Boolean.getBoolean(RESOURCE_MAPPING_ENABLED)) {
            enableResourceMapping(war);
        }
        if (isUsingEAP63()) {
            workaroundCLIVersionInEAP63();
        }
        // undeploy all metamer WARs if using a JBoss container
        if (isUsingJBossContainer()) {
            runCLICommand("undeploy *metamer*");
        }
        return war;
    }

    private static WebArchive createWarFromZipFile() throws IOException, IllegalArgumentException, ArchiveImportException {
        File tmpFile = new File("target/metamer-orig.war");
        File originalWar = runInPortalEnv ? new File("target/metamer-portlet.war") : new File("target/metamer.war");
        Files.move(originalWar, tmpFile);// rename the original war file to metamer*-orig.war
        return ShrinkWrap.create(ZipImporter.class, originalWar.getName()).importFrom(tmpFile).as(WebArchive.class);
    }

    /*
     * Update contex-param values in web.xml Call this function cause set org.richfaces.resourceMapping.enabled to true, and
     * remain 2 context-params according to function params values
     */
    private static void enableResourceMapping(WebArchive war) {
        System.out.println("### Enabling resource mapping ###");

        Boolean compressedStages = Boolean.getBoolean(RESOURCE_MAPPING_COMPRESSED_STAGES);
        Boolean packedStages = Boolean.getBoolean(RESOURCE_MAPPING_PACKED_STAGES);
        System.out.println(MessageFormat.format("    compressedStages  = {0}", compressedStages));
        System.out.println(MessageFormat.format("    packedStages  = {0}", compressedStages));

        // 1. load existing web.xml from metamer.war
        WebAppDescriptor webXmlDefault = Descriptors.importAs(WebAppDescriptor.class).fromStream(
            war.get("WEB-INF/web.xml").getAsset().openStream());
        List<ParamValueType<WebAppDescriptor>> allContextParams = webXmlDefault.getAllContextParam();
        // 2. Iterate over all context params and alter the particular ones
        for (ParamValueType<WebAppDescriptor> param : allContextParams) {
            String paramName = param.getParamName();
            if (paramName.equals(RESOURCE_MAPPING_ENABLED)) {
                param.paramValue("true");
            } else if (paramName.equals(RESOURCE_MAPPING_COMPRESSED_STAGES)) {
                param.paramValue(compressedStages ? PARAM_ALL : PARAM_NONE);
            } else if (paramName.equals(RESOURCE_MAPPING_PACKED_STAGES)) {
                param.paramValue(packedStages ? PARAM_ALL : PARAM_NONE);
            }
        }
        // 3. save the params to web.xml
        war.setWebXML(new StringAsset(webXmlDefault.exportAsString()));
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
    private static void workaroundCLIVersionInEAP63() throws URISyntaxException, IOException {
        File jbossCliFile = new File(System.getProperty("project.build.directory"), "jboss-eap-6.3" + File.separator + "bin" + File.separator + "jboss-cli.xml");
        File workaroundedJBossCliFile = new File(AbstractMetamerTest.class.getResource("eap/jboss-cli.xml").toURI());
        jbossCliFile.delete();
        Files.copy(workaroundedJBossCliFile, jbossCliFile);
    }

    /**
     * Returns the url to test page to be opened by Selenium
     *
     * @return absolute url to the test page to be opened by Selenium
     */
    public abstract URL getTestUrl();
}
