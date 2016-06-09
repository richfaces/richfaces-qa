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
package org.richfaces.tests.metamer.ftest.extension.configurator.skip;

import org.richfaces.tests.metamer.ftest.extension.utils.JSFDetectionUtils;
import org.richfaces.tests.qa.plugin.utils.Version;

/**
 * For examples see javadoc of
 * {@link org.richfaces.tests.metamer.ftest.extension.configurator.skip.SkipConfigurator SkipConfigurator}.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class On {

    private static boolean browserPropertyIsContaing(String... possibleBrowserName) {
        return systemPropertyIsContaing("browser", "firefox", possibleBrowserName);
    }

    private static boolean containerProfileActivated(String... profileNames) {
        return systemPropertyIsContaing("activated.maven.profiles", "", profileNames);
    }

    private static boolean eapVersionSetTo(String version) {
        return systemPropertyIsContaing("version.eap", "", version);
    }

    private static boolean getResultFor(Class<? extends SkipOn> caseClass) {
        return SkipOnResultsCache.getInstance().getResultFor(caseClass);
    }

    private static boolean osNamePropertyIsContaining(String... possibleOSName) {
        return systemPropertyIsContaing("os.name", "", possibleOSName);
    }

    private static boolean systemPropertyIsContaing(String systemProperty, String defaultValue, String... options) {
        String property = System.getProperty(systemProperty, defaultValue).toLowerCase();
        if (property.isEmpty()) {
            return Boolean.FALSE;
        }
        for (String option : options) {
            if (property.contains(option)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Skip test in each case. To mark always failing test.
     */
    public static class EachCase implements SkipOn {

        @Override
        public boolean apply() {
            return Boolean.TRUE;
        }
    }

    /**
     * Skip test when use of specific browser is detected (using property <code>browser</code>).
     */
    public interface Browser {

        /**
         * Skip test when use of Chrome browser is detected (when property <code>browser=cr</code> or
         * <code>browser=chrome</code>).
         */
        public static class Chrome implements SkipOn {

            @Override
            public boolean apply() {
                return browserPropertyIsContaing("chrome", "cr");
            }
        }

        /**
         * Skip test when use of Firefox browser is detected (when property <code>browser=ff</code> or
         * <code>browser=firefox</code>).
         */
        public static class Firefox implements SkipOn {

            @Override
            public boolean apply() {
                return browserPropertyIsContaing("firefox", "ff");
            }
        }

        /**
         * Skip test when use of Internet Explorer browser is detected (when property <code>browser=ie</code>,
         * <code>browser=internet</code> or <code>browser=explorer</code>).
         */
        public static class IE implements SkipOn {

            @Override
            public boolean apply() {
                return browserPropertyIsContaing("ie", "internet", "explorer");
            }
        }

    }

    /**
     * Skip test when specific use of container is detected (using property <code>activated.maven.profiles</code> and
     * <code>version.eap</code>).
     */
    public interface Container {

        /**
         * Skip test when use of Tomcat 8 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>tomcat-managed-8</code> or <code>tomcat-remote-8</code>).
         */
        public static class Tomcat8 implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("tomcat-managed-8", "tomcat-remote-8");
            }
        }

        /**
         * Skip test when use of Tomcat 7 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>tomcat-managed-7</code> or <code>tomcat-remote-7</code>).
         */
        public static class Tomcat7 implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("tomcat-managed-7", "tomcat-remote-7");
            }
        }

        /**
         * Skip test when use of EAP 6.2.4 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-2</code> or <code>jbosseap-remote-6-2</code> and <code>version.eap=6.2.4*</code>).
         */
        public static class EAP624 implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("jbosseap-managed-6-2", "jbosseap-remote-6-2") && eapVersionSetTo("6.2.4");
            }
        }

        /**
         * Skip test when use of EAP 6.2.x is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-2</code> or <code>jbosseap-remote-6-2</code>).
         */
        public static class EAP62x implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("jbosseap-managed-6-2", "jbosseap-remote-6-2");
            }
        }

        /**
         * Skip test when use of EAP 6.3.3 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-3</code> or <code>jbosseap-remote-6-3</code> and <code>version.eap=6.3.3*</code>).
         */
        public static class EAP633 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP63x.class) && eapVersionSetTo("6.3.3");
            }
        }

        /**
         * Skip test when use of EAP 6.3.4 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-3</code> or <code>jbosseap-remote-6-3</code> and <code>version.eap=6.3.4*</code>).
         */
        public static class EAP634 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP63x.class) && eapVersionSetTo("6.3.4");
            }
        }

        /**
         * Skip test when use of EAP 6.3.x is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-3</code> or <code>jbosseap-remote-6-3</code>).
         */
        public static class EAP63x implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("jbosseap-managed-6-3", "jbosseap-remote-6-3");
            }
        }

        /**
         * Skip test when use of EAP 6.4.0 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.0*</code>).
         */
        public static class EAP640 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.0");
            }
        }

        /**
         * Skip test when use of EAP 6.4.2 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.2*</code>).
         */
        public static class EAP642 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.2");
            }
        }

        /**
         * Skip test when use of EAP 6.4.3 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.3*</code>).
         */
        public static class EAP643 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.3");
            }
        }

        /**
         * Skip test when use of EAP 6.4.4 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.4*</code>).
         */
        public static class EAP644 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.4");
            }
        }

        /**
         * Skip test when use of EAP 6.4.5 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.5*</code>).
         */
        public static class EAP645 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.5");
            }
        }

        /**
         * Skip test when use of EAP 6.4.6 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.6*</code>).
         */
        public static class EAP646 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.6");
            }
        }

        /**
         * Skip test when use of EAP 6.4.7 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.7*</code>).
         */
        public static class EAP647 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.7");
            }
        }

        /**
         * Skip test when use of EAP 6.4.8 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.8*</code>).
         */
        public static class EAP648 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.8");
            }
        }

        /**
         * Skip test when use of EAP 6.4.9 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and <code>version.eap=6.4.9*</code>).
         */
        public static class EAP649 implements SkipOn {

            @Override
            public boolean apply() {
                return getResultFor(EAP64x.class) && eapVersionSetTo("6.4.9");
            }
        }

        /**
         * Skip test when use of EAP 6.4 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code>).
         */
        public static class EAP64x implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("jbosseap-managed-6-4", "jbosseap-remote-6-4");
            }
        }

        /**
         * Skip test when use of EAP 7.0 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>jbosseap-managed-7-0</code> or <code>jbosseap-remote-7-0</code>).
         */
        public static class EAP70 implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("jbosseap-managed-7-0", "jbosseap-remote-7-0");
            }
        }

        /**
         * Skip test when use of WildFly 8.1 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>wildfly-managed-8-1</code> or <code>wildfly-remote-8-1</code>).
         */
        public static class WildFly81 implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("wildfly-managed-8-1", "wildfly-remote-8-1");
            }
        }

        /**
         * Skip test when use of WildFly 8.2 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>wildfly-managed-8-2</code> or <code>wildfly-remote-8-2</code>).
         */
        public static class WildFly82 implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("wildfly-managed-8-2", "wildfly-remote-8-2");
            }
        }

        /**
         * Skip test when use of WildFly 9.0 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>wildfly-managed-9-0</code> or <code>wildfly-remote-9-0</code>).
         */
        public static class WildFly90 implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("wildfly-managed-9-0", "wildfly-remote-9-0");
            }
        }

        /**
         * Skip test when use of WildFly 10.0 is detected (when <code>activated.maven.profiles</code> are containing
         * <code>wildfly-managed-10-0</code> or <code>wildfly-remote-10-0</code>).
         */
        public static class WildFly100 implements SkipOn {

            @Override
            public boolean apply() {
                return containerProfileActivated("wildfly-managed-10-0", "wildfly-remote-10-0");
            }
        }

        public static class OtherThanEAP64WithVersion {

            private static final String actEapVersion = System.getProperty("version.eap");

            private static boolean isEAPVersionUnder(Version other) {
                if (actEapVersion == null || actEapVersion.isEmpty()) {
                    return false;
                }
                return Version.parseVersion(actEapVersion).compareTo(other) < 0;
            }

            private static boolean isUsingEAP64() {
                return getResultFor(EAP64x.class);
            }

            /**
             * Skip test when not using profile <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code> and skip
             * test when using profile <code>jbosseap-managed-6-4</code> or <code>jbosseap-remote-6-4</code>, but the
             * <code>version.eap</code> is under 6.4.9.
             */
            public static class Under649 implements SkipOn {

                @Override
                public boolean apply() {
                    if (isUsingEAP64()) {
                        return isEAPVersionUnder(Version.parseVersion("6.4.9"));
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    /**
     * Skip test when use of specific JSF implementation is detected from Metamer page.
     */
    public interface JSF {

        /**
         * Skip test when use of Mojarra JSF implementation is detected (when property <code>metamer.classifier</code> does not
         * contain <code>myfaces</code> or it is not used at all).
         */
        public static class Mojarra implements SkipOn {

            @Override
            public boolean apply() {
                return JSFDetectionUtils.isMojarra();
            }
        }

        /**
         * Skip test when use of MyFaces JSF implementation is detected (when property <code>metamer.classifier</code> does
         * contain <code>myfaces</code>).
         */
        public static class MyFaces implements SkipOn {

            @Override
            public boolean apply() {
                return JSFDetectionUtils.isMyFaces();
            }
        }

        /**
         * Skip tests when version of JSF is < 2.2.x
         */
        public static class VersionLowerThan22 implements SkipOn {

            @Override
            public boolean apply() {
                return JSFDetectionUtils.isVersionLowerThan22();
            }
        }

        /**
         * Skip tests when version of Mojarra is < 2.2.12
         */
        public static class VersionMojarraLowerThan2212 implements SkipOn {

            @Override
            public boolean apply() {
                return JSFDetectionUtils.isMojarra() && !JSFDetectionUtils.isVersionGreaterThan2211();
            }
        }

        /**
         * Skip tests when version of Mojarra is > 2.2.11
         *
         */
        public static class VersionMojarraGreaterThan2211 implements SkipOn {

            @Override
            public boolean apply() {
                return JSFDetectionUtils.isMojarra() && JSFDetectionUtils.isVersionGreaterThan2211();
            }
        }
    }

    /**
     * Skip test when specific OS is detected (using <code>os.name</code> system property).
     */
    public interface OS {

        /**
         * Skip test when Linux OS is detected (when <code>os.name</code> contains <code>linux</code>).
         */
        public static class Linux implements SkipOn {

            @Override
            public boolean apply() {
                return osNamePropertyIsContaining("linux");
            }
        }

        /**
         * Skip test when Solaris OS is detected (when <code>os.name</code> contains <code>sunos</code>, <code>sun</code> or
         * <code>solaris</code>).
         */
        public static class Solaris implements SkipOn {

            @Override
            public boolean apply() {
                return osNamePropertyIsContaining("sunos", "sun", "solaris");
            }
        }

        /**
         * Skip test when Windows OS is detected (when <code>os.name</code> contains <code>windows</code> or <code>win</code>).
         */
        public static class Windows implements SkipOn {

            @Override
            public boolean apply() {
                return osNamePropertyIsContaining("windows", "win");
            }
        }
    }

}
