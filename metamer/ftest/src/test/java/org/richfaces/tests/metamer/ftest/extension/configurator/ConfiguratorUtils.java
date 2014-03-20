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
package org.richfaces.tests.metamer.ftest.extension.configurator;

import java.util.List;

import org.richfaces.tests.metamer.ftest.extension.configurator.config.Config;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.SimpleConfig;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ConfiguratorUtils {

    public static List<List<Config>> mergeAllConfigsToOne(List<List<Config>> configs) {
        while (configs.size() > 1) {
            configs.add(0, mergeConfigs(configs.remove(0), configs.remove(0)));
        }
        return configs;
    }

    public static List<Config> mergeConfigs(List<Config> list1, List<Config> list2) {
        List<Config> result = Lists.newLinkedList();
        if (list1.isEmpty() && list2.isEmpty()) {
            return result;
        } else if (list1.isEmpty() || list2.isEmpty()) {
            result.addAll(list1);
            result.addAll(list2);
        } else {
            for (Config config1 : list1) {
                for (Config config2 : list2) {
                    result.add(mergeTwoConfigs(config1, config2));
                }
            }
        }
        return result;
    }

    public static Config mergeTwoConfigs(Config config1, Config config2) {
        SimpleConfig config = new SimpleConfig(config1);
        config.getConfigurations().addAll(config2.getConfigurations());
        return config;
    }
}
