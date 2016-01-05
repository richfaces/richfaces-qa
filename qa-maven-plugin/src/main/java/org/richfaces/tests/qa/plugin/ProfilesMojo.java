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
package org.richfaces.tests.qa.plugin;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@Mojo(name = "profiles", defaultPhase = LifecyclePhase.TEST_COMPILE)
public class ProfilesMojo extends AbstractMojo {

    private static final String INJECTED_PROFILE_IDS_FIELDNAME = "getInjectedProfileIds";

    @Parameter(defaultValue = "qa.activated.profiles")
    private String profilesPropertyName;

    @Component
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        setProjectProperty(getProfilesPropertyName(), getAllActivatedProfiles());
    }

    @SuppressWarnings("unchecked")
    public String getAllActivatedProfiles() {
        try {
            Method getInjectedProfileIdsMethod = MavenProject.class.getMethod(INJECTED_PROFILE_IDS_FIELDNAME);
            Map<String, List<String>> profilesMap = (Map<String, List<String>>) getInjectedProfileIdsMethod.invoke(getProject());
            Set<String> result = new HashSet<String>(20);
            for (List<String> listOfProfiles : profilesMap.values()) {
                for (String profile : listOfProfiles) {
                    result.add(profile.toLowerCase());
                }
            }
            return result.toString();
        } catch (Throwable ex) {
            getLog().error(ex);
        }
        return "";
    }

    public String getProfilesPropertyName() {
        return profilesPropertyName;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProjectProperty(String name, Object value) {
        getLog().info(MessageFormat.format("Setting project property <{0}> to value <{1}>.", name, value));
        getProject().getProperties().put(name, value.toString());
    }
}
