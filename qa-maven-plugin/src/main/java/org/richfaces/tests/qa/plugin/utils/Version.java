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
package org.richfaces.tests.qa.plugin.utils;

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Version implements Comparable<Version> {

    private static final Pattern PATTERN_1 = Pattern.compile("(.*?)(\\d+)\\.(\\d+)\\.(\\d+)(.*)");
    private static final Pattern PATTERN_2 = Pattern.compile("(.*?)(\\d+)\\.(\\d+)(.*)");
    private static final Pattern PATTERN_3 = Pattern.compile("(.*?)(\\d+)(.*)");
    public static final Version UNKNOWN_VERSION = new Version("unknown");

    private final int major;
    private final int micro;
    private final int minor;
    private final String prefix;
    private final String specifier;

    public Version(String versionString) {
        Matcher m;
        if ((m = PATTERN_1.matcher(versionString)).find()) {
            prefix = m.group(1);
            major = Integer.parseInt(m.group(2));
            minor = Integer.parseInt(m.group(3));
            micro = Integer.parseInt(m.group(4));
            specifier = m.group(5);
        } else if ((m = PATTERN_2.matcher(versionString)).find()) {
            prefix = m.group(1);
            major = Integer.parseInt(m.group(2));
            minor = Integer.parseInt(m.group(3));
            specifier = m.group(4);
            micro = 0;
        } else if ((m = PATTERN_3.matcher(versionString)).find()) {
            prefix = m.group(1);
            major = Integer.parseInt(m.group(2));
            specifier = m.group(3);
            minor = micro = 0;
        } else {
            major = minor = micro = 0;
            prefix = versionString;
            specifier = "";
        }
    }

    public Version(int major, int micro, int minor, String prefix, String specifier) {
        this.major = major;
        this.micro = micro;
        this.minor = minor;
        this.prefix = prefix;
        this.specifier = specifier;
    }

    public static Version parseVersion(String versionString) {
        return new Version(versionString);
    }

    @Override
    public int compareTo(Version other) {
        int result = this.major - other.major;
        if (result == 0) {
            result = this.minor - other.minor;
        }
        if (result == 0) {
            result = this.micro - other.micro;
        }
        if (result == 0) {
            // todo: improve/change
            result = this.specifier.length() - other.specifier.length();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;
        if (this.major != other.major) {
            return false;
        }
        if (this.minor != other.minor) {
            return false;
        }
        if (this.micro != other.micro) {
            return false;
        }
        if ((this.specifier == null) ? (other.specifier != null) : !this.specifier.equals(other.specifier)) {
            return false;
        }
        return true;
    }

    public String getFormat(EnumSet<Format> formats) {
        if ((formats.contains(Format.micro) || formats.contains(Format.minor)) && !(formats.contains(Format.major))) {
            throw new UnsupportedOperationException("Unsupported format " + formats);
        }

        StringBuilder sb = new StringBuilder();
        if (formats.contains(Format.prefix) && getPrefix() != null && !getPrefix().isEmpty()) {
            sb.append(getPrefix());
        }
        if (formats.contains(Format.major)) {
            sb.append(getMajor());
            if (formats.contains(Format.minor)) {
                sb.append(".").append(getMinor());
                if (formats.contains(Format.micro)) {
                    sb.append(".").append(getMicro());
                }
            }
        }
        if (formats.contains(Format.specifier) && getSpecifier() != null && !getSpecifier().isEmpty()) {
            sb.append(getSpecifier());
        }
        return sb.toString();
    }

    public String getFullFormat() {
        return getFormat(EnumSet.allOf(Format.class));
    }

    public int getMajor() {
        return major;
    }

    public String getMajorMinorFormat() {
        return getFormat(EnumSet.of(Format.major, Format.minor));
    }

    public String getMajorMinorMicroFormat() {
        return getFormat(EnumSet.of(Format.major, Format.minor, Format.micro));
    }

    public String getMajorMinorMicroSpecifierFormat() {
        return getFormat(EnumSet.of(Format.major, Format.minor, Format.micro, Format.specifier));
    }

    public int getMicro() {
        return micro;
    }

    public int getMinor() {
        return minor;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSpecifier() {
        return specifier;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.major;
        hash = 11 * hash + this.minor;
        hash = 11 * hash + this.micro;
        hash = 11 * hash + (this.specifier != null ? this.specifier.hashCode() : 0);
        return hash;
    }

    private int parseIntFromNullableString(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    @Override
    public String toString() {
        return getMajorMinorMicroSpecifierFormat();
    }

    public enum Format {

        prefix, major, minor, micro, specifier;
    }
}
