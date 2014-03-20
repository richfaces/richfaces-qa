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
package org.richfaces.tests.metamer.ftest.extension.configurator.config;

import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SimpleConfig implements Config {

    private final List<FieldConfiguration> injectionsConfigurations;

    public SimpleConfig() {
        injectionsConfigurations = Lists.newLinkedList();
    }

    public SimpleConfig(Object testInstance, Field f, Object value) {
        injectionsConfigurations = Lists.newLinkedList();
        add(testInstance, f, value);
    }

    public SimpleConfig(List<FieldConfiguration> config) {
        this.injectionsConfigurations = Lists.newLinkedList(config);
    }

    public SimpleConfig(Config config) {
        this(config.getConfigurations());
    }

    public final void add(Object testInstance, Field f, Object value) {
        getConfigurations().add(new FieldConfiguration(testInstance, value, f));
    }

    @Override
    public void configure() {
        for (FieldConfiguration crate : injectionsConfigurations) {
            crate.injectValueToField();
        }
    }

    @Override
    public SimpleConfig copy() {
        return new SimpleConfig(injectionsConfigurations);
    }

    @Override
    public List<FieldConfiguration> getConfigurations() {
        return injectionsConfigurations;
    }

    @Override
    public String toString() {
        return injectionsConfigurations.toString().replaceAll("\\[", "").replaceAll("\\]", "");
    }
}
