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
package org.richfaces.tests.metamer.ftest.utils.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.richfaces.tests.metamer.ftest.utils.files.Manipulator.Appender;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleFileManipulator implements Manipulator, Appender {

    private final File f;
    private final List<String> fileLines;
    private String line;
    private List<ConditionContainer> linesToChange = Lists.newArrayList();

    private SimpleFileManipulator(File f) {
        this.f = f;
        this.fileLines = readFileToListOfLines(f);
    }

    public static List<String> readFileToListOfLines(File f) {
        try {
            List<String> result = Lists.newArrayList();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String read;
            while ((read = br.readLine()) != null) {
                result.add(read);
            }
            br.close();
            return result;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Manipulator inFile(File f) {
        return new SimpleFileManipulator(f);
    }

    public static void writeListOfLinesToFile(List<String> list, File f) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (String line : list) {
                bw.append(line).append("\n");
            }
            bw.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Manipulator afterLine(LineIdentifier lineIdentifier) {
        this.linesToChange.add(new ConditionContainer(lineIdentifier, line, false, false));
        return this;
    }

    @Override
    public Appender appendLine(String line) {
        this.line = line;
        return this;
    }

    @Override
    public Manipulator beforeLine(LineIdentifier lineIdentifier) {
        this.linesToChange.add(new ConditionContainer(lineIdentifier, line, true, false));
        return this;
    }

    @Override
    public Manipulator deleteLine(LineIdentifier line) {
        this.linesToChange.add(new ConditionContainer(line, null, false, true));
        return this;
    }

    @Override
    public void perform() {
        List<String> result = Lists.newLinkedList(fileLines);
        List<String> tmp = Lists.newLinkedList(fileLines);
        for (ConditionContainer c : linesToChange) {
            for (int i = 0; i < tmp.size(); i++) {
                if (c.identifier.isLine(tmp.get(i))) {
                    if (c.toDelete) {
                        result.remove(i);
                        continue;
                    }
                    if (c.isBefore) {
                        result.add(i, c.line);
                        i++;// do not process line again
                    } else {
                        result.add(i + 1, c.line);
                        i++;// do not process inserted line
                    }
                }
            }
            tmp = Lists.newLinkedList(result);
        }
        writeListOfLinesToFile(tmp, f);
    }

    private class ConditionContainer {

        private final LineIdentifier identifier;
        private final String line;
        private final boolean isBefore;
        private final boolean toDelete;

        public ConditionContainer(LineIdentifier identifier, String line, boolean isBefore, boolean toDelete) {
            this.identifier = identifier;
            this.line = line;
            this.isBefore = isBefore;
            this.toDelete = toDelete;
        }
    }
}
