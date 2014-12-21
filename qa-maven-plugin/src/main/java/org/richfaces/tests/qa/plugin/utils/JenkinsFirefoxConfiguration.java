/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.qa.plugin.utils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class JenkinsFirefoxConfiguration {

    private String firefoxBin;
    private String firefoxVersion;

    private String osArch;
    private String osName;
    private String osVersion;

    public JenkinsFirefoxConfiguration() {
    }

    public String getFirefoxBin() {
        return firefoxBin;
    }

    public String getFirefoxVersion() {
        return firefoxVersion;
    }

    public String getOsArch() {
        return osArch;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setFirefoxBin(String firefoxBin) {
        this.firefoxBin = firefoxBin;
    }

    public void setFirefoxVersion(String firefoxVersion) {
        this.firefoxVersion = firefoxVersion;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JenkinsFirefoxConfiguration{");
        sb.append(" osName=").append(getOsName());
        sb.append(", osVersion=").append(getOsVersion());
        sb.append(", osArch=").append(getOsArch());
        sb.append(", firefoxBin=").append(getFirefoxBin());
        sb.append(", firefoxVersion=").append(getFirefoxVersion());
        sb.append(" }");
        return sb.toString();
    }
}
