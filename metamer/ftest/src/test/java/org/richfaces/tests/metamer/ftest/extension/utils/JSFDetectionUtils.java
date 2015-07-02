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
package org.richfaces.tests.metamer.ftest.extension.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.net.URL;

/**
 * Contains support methods to detect Mojarra, MyFaces and their respective versions. Used in @Skip annotations. Has to be
 * called after deploying Metamer. Sends a HTTP request and parses information from Metamer page.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class JSFDetectionUtils {

    private static SoftReference<String> cachedResponse = new SoftReference<String>(null);

    /**
     * Detects whether JSF implementation is Mojarra
     *
     * @return true if Mojarra is used, false otherwise
     */
    public static boolean isMojarra() {
        return isPatternPresentInJsfVersionTag(".*Mojarra.*");
    }

    /**
     * Detects whether JSF implementation is MyFaces
     *
     * @return true if MyFaces is used, false otherwise
     */
    public static boolean isMyFaces() {
        return isPatternPresentInJsfVersionTag(".*MyFaces.*");
    }

    /**
     * Detects if JSF version is lower than 2.2
     *
     * @return true if JSF version is lower than 2.2, false otherwise
     */
    public static boolean isVersionLowerThan22() {
        return !isPatternPresentInJsfVersionTag(".*[2-9]{1}[\\.]{1}[2-9].*");
    }

    private static boolean isPatternPresentInJsfVersionTag(String pattern) {
        if (cachedResponse.get() != null) {
            return cachedResponse.get().matches(pattern);
        }

        String possibleMatch = "";

        // send http request to metamer page and parse response
        String url = "http://localhost:8080/metamer/faces/components/richTabPanel/jsfFlowScoped.xhtml";

        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(new URL(url).openConnection().getInputStream()));
            String inputLine;
            // adjusted to the chosen page of metamer, other pages might have different id
            String jsfVersionTag = "<span id=\"jsfVersion\">";

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains(jsfVersionTag)) {
                    possibleMatch = inputLine;
                    cachedResponse = new SoftReference<String>(inputLine);
                    break;
                }
            }
            in.close();

        } catch (IOException ex) {
            // if there was a communication problem during the request, return false
            cachedResponse = new SoftReference<String>(null);
            return false;
        }
        return possibleMatch.matches(pattern);
    }
}
