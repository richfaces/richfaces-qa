/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.javaee6.ParamValueType;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Templates;

/**
 * Abstract test case used as a basis for majority of test cases.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision: 22749 $
 */
@RunAsClient
public abstract class AbstractMetamerTest extends Arquillian {

    @ArquillianResource
    protected URL contextPath;
    protected static Boolean runInPortalEnv = Boolean.getBoolean("runInPortalEnv");
    /**
     * The path to the metamer application.
     */
    public static final String WEBAPP_SRC = "../application/src/main/webapp";
    /** Key to manage resourceMapping enabling context-param in web.xml */
    public static final String RESOURCE_MAPPING_ENABLED = "org.richfaces.resourceMapping.enabled";
    /** Key to manage compressedStages context-param in web.xml */
    public static final String RESOURCE_MAPPING_COMPRESSED_STAGES = "org.richfaces.resourceMapping.compressedStages";
    /** Key to manage packedStages context-param in web.xml */
    public static final String RESOURCE_MAPPING_PACKED_STAGES = "org.richfaces.resourceMapping.packedStages";

    @Inject
    @Templates({ "plain", "richAccordion", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid",
        "richCollapsiblePanel", "richTabPanel", "richPopupPanel", "a4jRegion", "a4jRepeat", "uiRepeat" })
    protected TemplatesList template;

    /**
     * Returns the url to test page to be opened by Selenium
     *
     * @return absolute url to the test page to be opened by Selenium
     */
    public abstract URL getTestUrl();

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        WebArchive war;
        if(runInPortalEnv) {
            war = ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/metamer-portlet.war"));
        } else {
            war = ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/metamer.war"));
        }
        /*
         * If value on system property "org.richfaces.resourceMapping.enabled" is set to true, modify context-params in web.xml.
         * For more info see https://issues.jboss.org/browse/RFPL-1682
         */
        // Note that following code verify value of system property with given key
        if (Boolean.getBoolean(RESOURCE_MAPPING_ENABLED)) {
            System.out.println(RESOURCE_MAPPING_ENABLED + "=true");
            Boolean compressedStages = Boolean.getBoolean(RESOURCE_MAPPING_COMPRESSED_STAGES);
            Boolean packedStages = Boolean.getBoolean(RESOURCE_MAPPING_PACKED_STAGES);
            war = updateArchiveWebXml(war, compressedStages, packedStages);
        }
        return war;
    }

    /*
     * Update contex-param values in web.xml Call this function cause set org.richfaces.resourceMapping.enabled to true, and
     * remain 2 context-params according to function params values
     */
    private static WebArchive updateArchiveWebXml(WebArchive defaultWar, Boolean compressedStages, Boolean packedStages) {
        // 1. load existing web.xml from metamer.war
        WebAppDescriptor webXmlDefault = Descriptors.importAs(WebAppDescriptor.class).fromStream(
                defaultWar.get(new BasicPath("WEB-INF/web.xml")).getAsset().openStream());
        List<ParamValueType<WebAppDescriptor>> allContextParams = webXmlDefault.getAllContextParam();
        // 2. Iterate over all context params and alter the particular ones
        for (ParamValueType<WebAppDescriptor> param : allContextParams) {
            String paramName = param.getParamName();
            if (paramName.equals(RESOURCE_MAPPING_ENABLED)) {
                param.paramValue("true");
            } else if (paramName.equals(RESOURCE_MAPPING_COMPRESSED_STAGES)) {
                param.paramValue("All");
            } else if (paramName.equals(RESOURCE_MAPPING_PACKED_STAGES)) {
                param.paramValue("All");
            }
        }
        // 3. create second archive (war). Set here modified web.xml
        WebArchive modifiedWar = ShrinkWrap.create(WebArchive.class);
        modifiedWar.setWebXML(new StringAsset(webXmlDefault.exportAsString()));
        // 4. merge newly created war with metamer.war (this is way how to change descriptor within archive)
        // war.merge(tempWar); -- this way doesn't work
        modifiedWar.merge(defaultWar);
        // 5. return modified archive
        return modifiedWar;
    }

    /**
     * This method should be called in each test class from the method with annotation @Deployment, to ensure that deployed war
     * will contain all environment specific files. In other words when war is deployed on Tomcat or other containers needs to
     * have some specific files, the same apply for testing with different JSF implementations.
     *
     * @param war to be altered
     * @return war which is altered according to the test environment
     */
    protected static WebArchive alterWarAccordingToTestEnvironment(WebArchive war) {
        String tomcat = System.getProperty("TOMCAT");
        if (tomcat != null && tomcat.equals("true")) {
            war = alterAccordingToTomcat(war);
        }
        // TODO
        return (WebArchive) war;
    }

    /**
     * Alter the war according to the Tomcat specifics
     *
     * @param war to be altered
     * @return war to be altered
     */
    private static WebArchive alterAccordingToTomcat(WebArchive war) {
        // TODO
        return war;
    }
}
