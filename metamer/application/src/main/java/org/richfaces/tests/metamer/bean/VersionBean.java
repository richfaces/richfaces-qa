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
package org.richfaces.tests.metamer.bean;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.ProjectStage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.log.RichfacesLogger;
import org.richfaces.log.Logger;

/**
 * Vendor and version information for project Metamer.
 *
 * @author asmirnov@exadel.com
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision: 22871 $
 */
@ManagedBean(name = "metamer")
@ApplicationScoped
public final class VersionBean {

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    private static final String UNKNOWN_JSF = "Unknown version of JSF";
    private String implementationVendor;
    private String implementationVersion;
    private String implementationTitle;
    private String fullVersion;
    private String shortVersion;
    private String richFacesVersion;
    private String jsfVersion;
    private String serverVersion;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        Properties properties = new Properties();
        try {
            InputStream inStream = getClass().getClassLoader().getResourceAsStream("version.properties");
            properties.load(inStream);
        } catch (Exception e) {
            LOGGER
                .warn(
                    "Unable to load version.properties using PomVersion.class.getClassLoader().getResourceAsStream(...)",
                    e);
        }

        implementationTitle = properties.getProperty("Implementation-Title");
        implementationVendor = properties.getProperty("Implementation-Vendor");
        implementationVersion = properties.getProperty("Implementation-Version");
        serverVersion = initializeServerVersion();
    }

    public String getVendor() {
        return implementationVendor;
    }

    public String getTitle() {
        return implementationTitle;
    }

    public String getVersion() {
        return implementationVersion;
    }

    public String getFullVersion() {
        if (fullVersion != null) {
            return fullVersion;
        }

        if (implementationVersion == null) {
            implementationVersion = "Metamer, version unknown";
            return implementationVersion;
        }

        fullVersion = implementationTitle + " by " + implementationVendor + ", version " + implementationVersion;
        return fullVersion;
    }

    public String getShortVersion() {
        if (shortVersion != null) {
            return shortVersion;
        }

        if (implementationVersion == null) {
            implementationVersion = "Metamer, version unknown";
            return implementationVersion;
        }

        shortVersion = "Metamer " + implementationVersion;
        return shortVersion;
    }

    public String getRichFacesVersion() {
        if (richFacesVersion != null) {
            return richFacesVersion;
        }

        org.richfaces.VersionBean rfVersionBean = new org.richfaces.VersionBean();
        StringBuilder result = new StringBuilder();
        result.append("RichFaces ");
        result.append(rfVersionBean.getVersion().getImplementationVersion());
        richFacesVersion = result.toString();

        return richFacesVersion;
    }

    public String getJavaVersion() {
        return System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version");
    }

    public String getOsVersion() {
        return System.getProperty("os.name");
    }

    public String getJsfVersion() {

        if (jsfVersion != null) {
            return jsfVersion;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext == null) {
            return UNKNOWN_JSF;
        }

        jsfVersion = UNKNOWN_JSF;

        Class<?> applicationClass = facesContext.getApplication().getClass();
        Package pack = applicationClass.getPackage();

        if (pack.getImplementationTitle() != null) {
            jsfVersion = pack.getImplementationTitle();
            if (pack.getImplementationVersion() != null) {
                jsfVersion += " " + pack.getImplementationVersion();
            }
        }

        return jsfVersion;
    }

    public String getBrowserVersion() {
        Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if (request instanceof HttpServletRequest) {
            // small hack to get it working correctly in portal env
            return ((HttpServletRequest)request).getHeader("user-agent");
        }

        return "Unknown";
    }

    public ProjectStage getProjectStage() {
        return FacesContext.getCurrentInstance().getApplication().getProjectStage();
    }

    public String getServerVersion() {
        return serverVersion;
    }

    private static String initializeServerVersion() {
        String result = null;

        try {
            result = getJBossAS7VersionInfo();
        } catch (FailToRetrieveInfo e) {
            result = e.getMessage();
        }

        if (result == null) {
            try {
                result = getJBossAS6VersionInfo();
            } catch (FailToRetrieveInfo e) {
                result = e.getMessage();
            }
        }

        if (result == null) {
            try {
                result = getTomcatVersionInfo();
            } catch (FailToRetrieveInfo e) {
                result = e.getMessage();
            }
        }

        if (result == null) {
            result = "Server unknown";
        }

        return result;
    }

    public static String getTomcatVersionInfo() {
        String result = (String) new InfoObtainer() {

            @Override
            protected Object obtainInfo() throws ClassNotFoundException, IllegalAccessException,
                InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException,
                InvocationTargetException {

                Class<?> clazz = Class.forName("org.apache.catalina.util.ServerInfo");
                return clazz.getMethod("getServerInfo").invoke(null);
            }
        }.getInfo();

        return result.replace("/", " ");
    }

    public static String getJBossAS6VersionInfo() {
        String versionNumber = (String) new JBossAS6VersionInfoObtainer("getVersionNumber").getInfo();
        String buildNumber = (String) new JBossAS6VersionInfoObtainer("getBuildID").getInfo();

        if (versionNumber == null) {
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append("JBoss AS ");
        buffer.append(versionNumber);

        if (versionNumber.endsWith("SNAPSHOT")) {
            buffer.append(" ");
            buffer.append(buildNumber.replaceFirst("r", "r."));
        }

        return buffer.toString();
    }

    public static String getJBossAS7VersionInfo() {
        try {
            Class.forName("org.jboss.as.version.Version");
        } catch (ClassNotFoundException e) {
            return null;
        }
        return "JBoss AS " + org.jboss.as.version.Version.AS_VERSION;
    }

    private static class JBossAS6VersionInfoObtainer extends InfoObtainer {

        private String methodName;

        public JBossAS6VersionInfoObtainer(String methodName) {
            super();
            this.methodName = methodName;
        }

        @Override
        protected Object obtainInfo() throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

            Class<?> clazz = Class.forName("org.jboss.bootstrap.impl.as.server.ASVersion");
            Object classInstance = clazz.getMethod("getInstance").invoke(null);
            return clazz.getMethod(methodName).invoke(classInstance);
        }
    }

    private abstract static class InfoObtainer {

        protected abstract Object obtainInfo() throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException;

        public final Object getInfo() {
            try {
                return obtainInfo();
            } catch (ClassNotFoundException e) {
                return null;
            } catch (IllegalAccessException e) {
                throw new FailToRetrieveInfo(fail("failed to retrieve info - ", e), e);
            } catch (InstantiationException e) {
                throw new FailToRetrieveInfo(fail("failed to retrieve info - ", e), e);
            } catch (SecurityException e) {
                throw new FailToRetrieveInfo(fail("failed to access version info - ", e), e);
            } catch (NoSuchMethodException e) {
                throw new FailToRetrieveInfo(fail("failed to access version info - ", e), e);
            } catch (IllegalArgumentException e) {
                throw new FailToRetrieveInfo(fail("failed to access version info - ", e), e);
            } catch (InvocationTargetException e) {
                throw new FailToRetrieveInfo(fail("failed to access version info - ", e), e);
            }
        }
    }

    public static class FailToRetrieveInfo extends RuntimeException {

        private static final long serialVersionUID = 4905414716987875382L;

        public FailToRetrieveInfo(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static String fail(String message, Exception e) {
        return message + e.getClass().getSimpleName() + e.getMessage();
    }

    @Override
    public String toString() {
        return getFullVersion();
    }
}
