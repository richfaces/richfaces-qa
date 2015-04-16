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
package org.richfaces.tests.metamer.ftest.extension.attributes.coverage.result;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * Converts map of results to list of results. Sorts results alphabetically.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CoverageResultsCreator implements ResultsCreator<SimpleCoverageResult> {

    @Override
    public List<SimpleCoverageResult> createResultsFrom(Map<Class<? extends Enum>, EnumSet> map) {
        List<SimpleCoverageResult> result = Lists.newArrayList();
        List<Class<? extends Enum>> keys = Lists.newArrayList(map.keySet());

        // sort
        Collections.sort(keys, new Comparator<Class<? extends Enum>>() {
            @Override
            public int compare(Class<? extends Enum> t, Class<? extends Enum> t1) {
                return t.getName().compareTo(t1.getName());
            }
        });

        // convert
        for (Class<? extends Enum> enumClass : keys) {
            result.add(new SimpleCoverageResult(enumClass, map.get(enumClass)));
        }

        return result;
    }
}
