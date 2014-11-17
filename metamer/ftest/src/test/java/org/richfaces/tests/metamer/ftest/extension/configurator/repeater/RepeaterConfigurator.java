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
package org.richfaces.tests.metamer.ftest.extension.configurator.repeater;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.richfaces.tests.metamer.ftest.extension.configurator.ConfiguratorExtension;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.Config;
import org.richfaces.tests.metamer.ftest.extension.configurator.config.NoOpNameValueConfig;

import com.google.common.collect.Lists;

/**
 * Configurator for running all tests multiple times. Just add property -Drepeat (or times, repeats) with number of repeats.
 * Only number greater than 1 is accepted.<br/>
 * Some examples:<br/>
 * <code>mvn verify -Drepeat=2</code><br/>
 * <code>mvn verify -Drepeats=3</code><br/>
 * <code>mvn verify -Dtimes=4</code>
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RepeaterConfigurator implements ConfiguratorExtension {

    private static final String[] POSSIBLE_OPTION_NAMES = { "repeat", "times", "repeats" };
    private static final String NAME = "repeat-cycle";

    private final int numberOfRepeats;

    public RepeaterConfigurator() {
        String times;
        for (String repeatOption : POSSIBLE_OPTION_NAMES) {
            times = System.getProperty(repeatOption);
            if (times != null) {
                this.numberOfRepeats = Integer.valueOf(times);
                return;
            }
        }
        numberOfRepeats = 0;
    }

    @Override
    public List<Config> createConfigurations(Method m, Object testInstance) {
        if (numberOfRepeats <= 1) {
            return Collections.EMPTY_LIST;
        } else {
            List<Config> result = Lists.newArrayList();
            for (int i = 1; i <= numberOfRepeats; i++) {
                result.add(new NoOpNameValueConfig(NAME, i));
            }
            return result;
        }
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
