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
package org.richfaces.tests.metamer.ftest.extension.attributes.collector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleAttributesSaver implements AttributesSaver {

    private static final String A4J = "a4j";
    private static final String ATTRIBUTES_DOT_JAVA = "Attributes.java";
    private static final String COMMA = ",";
    private static final String DOT = ".";
    private static final String DOT_JAVA = ".java";
    private static final String EMPTY_STRING = "";
    private static final String FOR_STRING = "for";
    private static final String IMPORT_ATT_ENUM = "import org.richfaces.tests.metamer.ftest.attributes.AttributeEnum;\n";
    private static final String LICENSE
        = "/*\n"
        + " * JBoss, Home of Professional Open Source\n"
        + " * Copyright 2010-" + new DateTime().getYear() + ", Red Hat, Inc. and individual contributors\n"
        + " * by the @authors tag. See the copyright.txt in the distribution for a\n"
        + " * full listing of individual contributors.\n"
        + " *\n"
        + " * This is free software; you can redistribute it and/or modify it\n"
        + " * under the terms of the GNU Lesser General Public License as\n"
        + " * published by the Free Software Foundation; either version 2.1 of\n"
        + " * the License, or (at your option) any later version.\n"
        + " *\n"
        + " * This software is distributed in the hope that it will be useful,\n"
        + " * but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
        + " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU\n"
        + " * Lesser General Public License for more details.\n"
        + " *\n"
        + " * You should have received a copy of the GNU Lesser General Public\n"
        + " * License along with this software; if not, write to the Free\n"
        + " * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA\n"
        + " * 02110-1301 USA, or see the FSF site: http://www.fsf.org.\n"
        + " */";
    private static final String ORG = "org";
    private static final String PATH_TO_SRC_METAMER_FTEST = "src/test/java/org/richfaces/tests/metamer/ftest";
    private static final String RICH = "rich";
    private static final String SLASH = "/";
    private static final String SPACES_4 = "    ";

    private String getComponentNameFromDirectory(File componentsDir) {
        return componentsDir.getName().replaceAll(A4J, EMPTY_STRING).replaceAll(RICH, EMPTY_STRING);
    }

    private String getPackageFromFile(File f) {
        return f.getAbsolutePath().substring(f.getAbsolutePath().indexOf(ORG), f.getAbsolutePath().lastIndexOf(SLASH)).replaceAll(SLASH, DOT).replaceAll(DOT_JAVA, EMPTY_STRING);
    }

    private Set<File> getSetOfComponents() {
        Set<File> componentsDirs = Sets.newHashSet(new File(PATH_TO_SRC_METAMER_FTEST).listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory() && (file.getName().startsWith(A4J) || file.getName().startsWith(RICH));
            }
        }));
        return componentsDirs;
    }

    @Override
    public void save(Map<String, List<String>> allAttributes) throws IOException {
        for (File componentsDir : getSetOfComponents()) {
            String componentName = getComponentNameFromDirectory(componentsDir);
            List<String> attributes = allAttributes.get(componentName);
            if (attributes != null && !attributes.isEmpty()) {
                writeAttributesToFile(new File(componentsDir, componentName + ATTRIBUTES_DOT_JAVA), attributes);
            } else {
                System.err.println("No attributes for component: " + componentName + " were found.");
            }
        }
    }

    private void writeAttributesToFile(File attributesFile, List<String> attributes) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(attributesFile));
        try {
            bw.append(LICENSE);
            bw.newLine();
            bw.append("package " + getPackageFromFile(attributesFile) + ";");
            bw.newLine();
            bw.newLine();
            bw.append(IMPORT_ATT_ENUM);
            bw.newLine();
            bw.append("public enum " + attributesFile.getName().replaceAll(DOT_JAVA, EMPTY_STRING) + " implements AttributeEnum {");
            bw.newLine();
            bw.newLine();
            for (int i = 0; i < attributes.size(); i++) {
                String attributeName = attributes.get(i);
                if (attributeName.equals(FOR_STRING)) {
                    attributeName = attributeName.toUpperCase();
                }
                bw.append(SPACES_4 + attributeName);
                if (i != attributes.size() - 1) {
                    bw.append(COMMA);
                    bw.newLine();
                }
            }
            bw.newLine();
            bw.append("}");
            bw.newLine();
            bw.flush();
        } finally {
            bw.close();
        }
    }
}
