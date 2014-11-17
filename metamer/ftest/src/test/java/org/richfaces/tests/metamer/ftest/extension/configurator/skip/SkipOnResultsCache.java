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
package org.richfaces.tests.metamer.ftest.extension.configurator.skip;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Simple cache for storing results of SkipOn#apply() expressions.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public enum SkipOnResultsCache {

    INSTANCE;

    private final Map<Class<? extends SkipOn>, Boolean> checkedClassesCache = Maps.newHashMap();

    public static SkipOnResultsCache getInstance() {
        return INSTANCE;
    }

    public boolean getResultFor(Class<? extends SkipOn> skipOnClass) {
        if (checkedClassesCache.containsKey(skipOnClass)) {
//            System.out.println(MessageFormat.format("Class <{0}> was in cache with result <{1}>", skipOnClass.getSimpleName(), checkedClassesCache.get(skipOnClass)));
            return checkedClassesCache.get(skipOnClass);
        } else {
            boolean result;
            try {
                result = skipOnClass.newInstance().apply();
            } catch (Exception ex) {
                System.err.println(ex);
                result = false;
            }
//            System.out.println(MessageFormat.format("Inserting class <{0}> to cache with result <{1}>", skipOnClass.getSimpleName(), result));
            checkedClassesCache.put(skipOnClass, result);
            return result;
        }
    }
}
