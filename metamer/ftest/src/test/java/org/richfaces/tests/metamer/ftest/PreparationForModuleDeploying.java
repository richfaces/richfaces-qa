/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2012, Red Hat, Inc. and individual contributors
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

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.richfaces.VersionBean;
import org.richfaces.tests.metamer.Attribute;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.Behavior;
import org.richfaces.tests.metamer.Component;
import org.richfaces.tests.metamer.Extensions;
import org.richfaces.tests.metamer.Phase;
import org.richfaces.tests.metamer.RichPhaseListener;
import org.richfaces.tests.metamer.Template;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.TestIdentityFilter;
import org.richfaces.tests.metamer.bean.PhasesBean;
import org.richfaces.tests.metamer.bean.TemplateBean;
import org.richfaces.tests.metamer.bean.a4j.A4JCommandLinkBean;
import org.richfaces.tests.metamer.converter.TemplateNameConverter;
import org.richfaces.tests.metamer.converter.TemplatesListConverter;

public class PreparationForModuleDeploying extends AbstractGrapheneTest {

    /*
     * private final static String[] LIBRARIES = { "org.richfaces.cdk:annotations",
     * "org.atmosphere:atmosphere-compat-jbossweb:jar:0.8.0-RC1",
     * "org.atmosphere:atmosphere-compat-jetty:jar:0.8.0-RC1", "org.atmosphere:atmosphere-compat-tomcat:jar:0.8.0-RC1",
     * "org.atmosphere:atmosphere-compat-weblogic:jar:0.8.0-RC1", "org.atmosphere:atmosphere-runtime",
     * "commons-beanutils:commons-beanutils:jar:1.8.3", "commons-codec:commons-codec:jar:1.3",
     * "commons-collections:commons-collections:jar:3.2", "commons-digester:commons-digester:jar:1.8",
     * "commons-lang:commons-lang", "commons-logging:commons-logging:jar:1.1.1",
     * "net.sourceforge.cssparser:cssparser:jar:0.9.5", "com.google.guava:guava:jar:r08", "org.hornetq:hornetq-core",
     * "org.hornetq:hornetq-jms", "org.hornetq:hornetq-logging", "javassist:javassist", "javax.jms:jms",
     * "javax.servlet.jsp.jstl:jstl-api", "org.jboss.netty:netty", "org.richfaces.ui:richfaces-components-api",
     * "org.richfaces.ui:richfaces-components-ui", "org.richfaces.core:richfaces-core-api:jar:4.1.0-SNAPSHOT",
     * "org.richfaces.core:richfaces-core-impl:jar:4.1.0-SNAPSHOT", "org.w3c.css:sac:jar:1.3" };
     */
    /*
     * We need war with this structure:
     *
     * /resources/css/ /resources/metamer/ /resources/script/ /templates/
     * /WEB-INF/classes/org/richfaces/test/metamer/ListOfNeededBeans /WEB-INF/lib/ListOfNeededLibs
     * /WEB-INF/faces-config.xml WEB-INF/jboss-web.xml WEB-INF/glassfish-web.xml /WEB-INF/web.xml
     * /components/particularComponent/simple.xhtml /resources/images/ListOfNeddedImages
     */
    // @Deployment(testable = false)
    public static WebArchive createTestArchive() {

        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom(
            "../application/pom.xml");

        Archive<?> css = ShrinkWrap.create(ExplodedImporter.class, "css.war")
            .importDirectory(new File(WEBAPP_SRC + "/resources/css")).as(WebArchive.class);

        Archive<?> metamer = ShrinkWrap.create(ExplodedImporter.class, "metamer.war")
            .importDirectory(new File(WEBAPP_SRC + "/resources/metamer")).as(WebArchive.class);

        Archive<?> script = ShrinkWrap.create(ExplodedImporter.class, "script.war")
            .importDirectory(new File(WEBAPP_SRC + "/resources/script")).as(WebArchive.class);

        Archive<?> templates = ShrinkWrap.create(ExplodedImporter.class, "templates.war")
            .importDirectory(new File(WEBAPP_SRC + "/templates")).as(WebArchive.class);

        Archive<?> war = ShrinkWrap
            .create(WebArchive.class, "commandLink.war")
            .addClasses(TemplateBean.class, A4JCommandLinkBean.class, TemplatesList.class, PhasesBean.class,
                VersionBean.class, RichPhaseListener.class, Attributes.class, Attribute.class,
                TestIdentityFilter.class, Phase.class, TemplatesListConverter.class, TemplateNameConverter.class,
                Template.class, Component.class, Behavior.class, Extensions.class)
            .addAsLibraries(resolver.includeDependenciesFromPom("../application/pom.xml").resolveAsFiles())
            .addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/faces-config.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/jboss-web.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/glassfish-web.xml"))
            .setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"))
            .addAsWebResource(new File(WEBAPP_SRC + "/components/a4jCommandLink/simple.xhtml"),
                "components/a4jCommandLink/simple.xhtml")
            .addAsWebResource(new File(WEBAPP_SRC + "/index.xhtml"), "index.xhtml").merge(css, "resources/css")
            .merge(metamer, "/resources/metamer").merge(script, "/resources/script").merge(templates, "/templates");

        return alterWarAccordingToTestEnvironment((WebArchive) war);

    }

    private WebArchive addResourcesToWar(WebArchive war, String targetDirectory, List<String> resources) {

        war.addAsDirectory(ArchivePaths.create("resources/css"));

        for (int i = 0; i < resources.size(); i++) {

            war.addAsWebResource(new File(WEBAPP_SRC + resources.get(i)), resources.get(i));
        }

        return war;
    }

    @Override
    public URL getTestUrl() {
        // TODO Auto-generated method stub
        return null;
    }
}
