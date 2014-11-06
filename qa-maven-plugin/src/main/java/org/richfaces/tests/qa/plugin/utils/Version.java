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
package org.richfaces.tests.qa.plugin.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Version implements Comparable<Version> {

    public static final Version UNKNOWN_VERSION = new Version("unknown");

    private static final String eapPrefix = "jboss-eap-";
    private static final String ffPrefix = "firefox-";

    private final String DIGITS = "\\d+";
    private final String NON_DIGITS = "\\D";
    private final String ANYTHING = ".";
    private final String VERSION_SEPARATOR = "\\.";
    private final String DIGITS_FOLLOWED_BY_ANYTHING = MessageFormat.format("{0}{1}+{2}+", DIGITS, NON_DIGITS, ANYTHING);

    private final int major;
    private final int micro;
    private final int minor;
    private final String prefix;
    private final String specifier;

    public static Version parseEapVersion(String versionString) {
        return new Version(versionString, eapPrefix);
    }

    public static Version parseFirefoxVersion(String versionString) {
        return new Version(versionString, ffPrefix);
    }

    public static Version parseVersion(String versionString) {
        return parseVersion(versionString, null);
    }

    public static Version parseVersion(String versionString, String prefix) {
        return new Version(versionString, prefix);
    }

    public Version(String versionString, String prefix) {
        String tmp = versionString;
        this.prefix = prefix;
        if (prefix != null && !prefix.isEmpty() && versionString.startsWith(prefix)) {
            tmp = tmp.substring(tmp.indexOf(prefix) + prefix.length());
        }
        List<String> split = new ArrayList<String>(Arrays.asList(tmp.split(VERSION_SEPARATOR)));
        List<Integer> v = new ArrayList<Integer>();
        String spec = "";
        while (!split.isEmpty() && v.size() != 3) {
            String remove = split.remove(0);
            if (remove.matches(DIGITS_FOLLOWED_BY_ANYTHING)) {
                String replaceFirst = remove.replaceFirst(NON_DIGITS, "+");
                v.add(Integer.valueOf(replaceFirst.substring(0, replaceFirst.indexOf("+"))));
                replaceFirst = remove.replaceFirst(DIGITS, "+");
                spec = replaceFirst.substring(replaceFirst.indexOf("+") + 1);
                break;
            } else if (remove.matches(DIGITS)) {
                v.add(Integer.valueOf(remove));
            } else {
                spec = remove;
                break;
            }
        }
        for (String string : split) {
            spec += "." + string;
        }
        if (v.size() > 0) {
            major = v.remove(0);
            if (v.size() > 0) {
                minor = v.remove(0);
                if (v.size() > 0) {
                    micro = v.remove(0);
                } else {
                    micro = 0;
                }
            } else {
                minor = 0;
                micro = 0;
            }
            specifier = spec;
        } else {
            major = -1;
            minor = 0;
            micro = 0;
            specifier = "";
        }
    }

    public Version(String versionString) {
        this(versionString, null);
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

    @Override
    public String toString() {
        return getFormat(EnumSet.of(Format.major, Format.minor, Format.micro, Format.specifier));
    }

    public enum Format {

        prefix, major, minor, micro, specifier;
    }
}
