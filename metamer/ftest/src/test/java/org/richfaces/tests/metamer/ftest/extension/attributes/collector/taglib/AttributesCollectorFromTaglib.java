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
package org.richfaces.tests.metamer.ftest.extension.attributes.collector.taglib;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.WordUtils;
import org.richfaces.component.UIStatus;
import org.richfaces.tests.metamer.ftest.extension.attributes.collector.AttributesCollector;

import com.google.common.collect.Lists;

/**
 * Collects attributes for each rich/a4j tag from taglib.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AttributesCollectorFromTaglib implements AttributesCollector {

    private static final String JS_FUNCTION_NAME = "JSFunction";
    private static final String JS_FUNCTION_TAGNAME = "jsFunction";
    private static final String METAINF_A4J_TAGLIB_XML = "META-INF/a4j.taglib.xml";
    private static final String METAINF_RICH_TAGLIB_XML = "META-INF/rich.taglib.xml";
    private static final String RICHFACES_JAR_REGEXP = ".*richfaces[^/]+jar.*";

    private static String getTagName(Tag tag) {
        String tagName = tag.getTagName();
        return tagName.equals(JS_FUNCTION_TAGNAME) ? JS_FUNCTION_NAME : WordUtils.capitalize(tagName);
    }

    @Override
    public Map<String, List<String>> collectAttributes() {
        Map<String, List<String>> result = new HashMap<String, List<String>>(100);
        try {
            for (Tag tag : getAllTagsSorted(getTaglibFiles(METAINF_A4J_TAGLIB_XML, METAINF_RICH_TAGLIB_XML))) {
                result.put(getTagName(tag), getAttributesFromTag(tag));
            }
        } catch (JAXBException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return result;
    }

    private List<Tag> getAllTagsSorted(List<URL> configFiles) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(FaceletTaglib.class);
        List<Tag> tags = Lists.newArrayList();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        for (URL configFile : configFiles) {
            tags.addAll(((FaceletTaglib) unmarshaller.unmarshal(configFile)).getTags());
        }
        Collections.sort(tags, new Comparator<Tag>() {
            @Override
            public int compare(Tag t, Tag t1) {
                return t.getTagName().compareTo(t1.getTagName());
            }
        });
        return tags;
    }

    private List<String> getAttributesFromTag(Tag tag) {
        List<String> result = new ArrayList<String>(tag.getAttributes().size());
        for (Attribute attribute : tag.getAttributes()) {
            result.add(attribute.getName());
        }
        Collections.sort(result);
        return result;
    }

    private List<URL> getTaglibFiles(String... resources) throws IOException {
        ClassLoader cl = UIStatus.class.getClassLoader();
        List<URL> taglibFiles = Lists.newArrayList();
        for (String resource : resources) {
            Enumeration<URL> resourceURLs = cl.getResources(resource);
            while (resourceURLs.hasMoreElements()) {
                URL url = resourceURLs.nextElement();
                if (url.getPath().matches(RICHFACES_JAR_REGEXP)) {
                    taglibFiles.add(url);
                }
            }
        }
        return taglibFiles;
    }
}
