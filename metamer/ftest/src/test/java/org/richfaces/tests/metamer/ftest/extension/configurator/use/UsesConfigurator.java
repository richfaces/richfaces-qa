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
package org.richfaces.tests.metamer.ftest.extension.configurator.use;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.richfaces.tests.metamer.ftest.extension.configurator.ConfiguratorExtension;
import org.richfaces.tests.metamer.ftest.extension.configurator.ConfiguratorUtils;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.Config;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.Uses;

import com.google.common.collect.Lists;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class UsesConfigurator implements ConfiguratorExtension {

    private List<Config> createConfig(Uses annotation, Object testInstance) {
        UseWithField[] value = annotation.value();
        if (value == null) {
            throw new IllegalArgumentException("The Uses annotation should contain at least one UseWithField.");
        }
        UseWithFieldConfigurator useWithFieldConfigurator = new UseWithFieldConfigurator();
        List<List<Config>> result = Lists.newLinkedList();
        for (UseWithField useWithField : value) {
            result.add(useWithFieldConfigurator.createConfig(useWithField, testInstance));
        }
        return ConfiguratorUtils.mergeAllConfigsToOne(result).get(0);
    }

    @Override
    public List<Config> createConfigurations(Method m, Object testInstance) {
        Uses annotation = m.getAnnotation(Uses.class);
        if (annotation != null) {
            return createConfig(annotation, testInstance);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean ignoreConfigurations() {
        return Boolean.FALSE;
    }

    @Override
    public boolean skipTestIfNoConfiguration() {
        return Boolean.FALSE;
    }
}
