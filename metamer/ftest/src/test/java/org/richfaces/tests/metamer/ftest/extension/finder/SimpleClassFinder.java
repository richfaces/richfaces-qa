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
package org.richfaces.tests.metamer.ftest.extension.finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.richfaces.tests.metamer.ftest.extension.finder.ClassFinder.Finder;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class SimpleClassFinder implements ClassFinder, Finder {

    private static final String DOT = ".";
    private static final String DOT_JAVA = DOT + "java";
    private static final String DOT_NOT_REGEXP = "\\.";
    private static final String EMPTY_STRING = "";
    private static final String PACKAGE_STRING = "package";
    private static final String SEMICOLON = ";";

    private File directory;
    private final List<Predicate<Class<?>>> filters = Lists.newArrayList();
    private String folderWithJavaFiles;

    private List<Class<?>> _findClasses(File fromFile) {
        List<Class<?>> classes = Lists.newArrayList();
        if (!fromFile.exists()) {
            return classes;
        }
        for (File file : fromFile.listFiles()) {
            if (file.isDirectory()) {
                if (!file.getName().equals(DOT)) {
                    classes.addAll(_findClasses(file));
                }
            } else if (file.getName().endsWith(DOT_JAVA)) {
                try {
                    classes.add(Class.forName(getFQNFromFile(file)));
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException("ClassLoader does not contain needed class!", ex);
                }
            }
        }
        return classes;
    }

    @Override
    public Finder findAllClassesFrom(String directory) {
        return findAllClassesFrom(new File(directory));
    }

    @Override
    public Finder findAllClassesFrom(File directory) {
        this.directory = directory;
        return this;
    }

    private List<Class<?>> findClasses(File directory) {
        Iterable<Class<?>> result = _findClasses(directory);
        for (Predicate<Class<?>> filter : filters) {
            result = Iterables.filter(result, filter);
        }
        return Lists.newArrayList(result);
    }

    @Override
    public Finder fromPackageStartingWith(final String packageName) {
        filters.add(new Predicate<Class<?>>() {
            @Override
            public boolean apply(Class<?> t) {
                return t.getName().startsWith(packageName);
            }
        });
        return this;
    }

    private String getFQNFromFile(File javaFile) {
        String path = javaFile.getAbsolutePath().replace(DOT_JAVA, EMPTY_STRING);
        if (folderWithJavaFiles == null) {
            folderWithJavaFiles = getFolderWithJavaFilesFromJavaFilePackage(javaFile);
        }
        return path.substring(path.indexOf(folderWithJavaFiles) + folderWithJavaFiles.length()).replaceAll("/", ".");
    }

    private String getFolderWithJavaFilesFromJavaFilePackage(File f) {
        BufferedReader bw = null;
        try {
            bw = new BufferedReader(new FileReader(f));
            String line;
            while ((line = bw.readLine()) != null) {
                if (line.startsWith(PACKAGE_STRING)) {// line with package?
                    // remove 'package' + white spaces + semicolon
                    // 'package x.y.z;' >>> 'x.y.z'
                    line = line.replaceAll(PACKAGE_STRING, EMPTY_STRING).trim().replace(SEMICOLON, EMPTY_STRING);
                    // 'x.y.z' >>> 'x/y/z'
                    line = line.replaceAll(DOT_NOT_REGEXP, File.separator);
                    // '/home/userx/java/files/to/check/org/x/y/z' >>> '/home/userx/java/files/to/check/'
                    line = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf(line));
                    return line;
                }
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        throw new RuntimeException("Package was not found in java file");
    }

    @Override
    public Set<Class<?>> scan() {
        return Sets.newHashSet(findClasses(directory));
    }

    @Override
    public Finder withAnnotation(final Class<? extends Annotation> annotationClass) {
        filters.add(new Predicate<Class<?>>() {
            @Override
            public boolean apply(Class<?> t) {
                return t.getAnnotation(annotationClass) != null;
            }
        });
        return this;
    }

    @Override
    public Finder withClassNameMatching(final String match) {
        filters.add(new Predicate<Class<?>>() {
            @Override
            public boolean apply(Class<?> t) {
                return t.getName().matches(match);
            }
        });
        return this;
    }
}
