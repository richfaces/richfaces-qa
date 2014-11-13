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

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Composite configuration.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class CompositeConfigImpl implements CompositeConfig {

    private final List<Config> configurations;

    public CompositeConfigImpl(Config config1, Config config2) {
        this.configurations = Lists.newLinkedList();
        this.configurations.add(config1.copy());
        this.configurations.add(config2.copy());
    }

    public CompositeConfigImpl(Config config) {
        this.configurations = Lists.newLinkedList();
        this.configurations.add(config.copy());
    }

    public CompositeConfigImpl(List<? extends Config> config) {
        this.configurations = Lists.newLinkedList(config);
    }

    @Override
    public void configure() {
        for (Config c : getConfigurations()) {
            c.configure();
        }
    }

    @Override
    public CompositeConfigImpl copy() {
        return new CompositeConfigImpl(getConfigurations());
    }

    @Override
    public List<Config> getConfigurations() {
        return configurations;
    }

    @Override
    public Config merge(Config otherConfig) {
        return new CompositeConfigImpl(this, otherConfig);
    }

    @Override
    public String toString() {
        return getConfigurations().toString().replaceAll("\\[", "").replaceAll("\\]", "");
    }

    @Override
    public void unconfigure() {
        for (Config c : getConfigurations()) {
            c.unconfigure();
        }
    }
}
